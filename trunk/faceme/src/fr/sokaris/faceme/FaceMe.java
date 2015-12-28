/*
 *  This file is part of FaceMe.
 *
 *  FaceMe is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FaceMe is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FaceMe; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *  
 *  Author: Sylvain Maucourt, smaucourt@gmail.com
 */

package fr.sokaris.faceme;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.facebook.api.FacebookException;

import fr.sokaris.faceme.actions.TakePhotoListener;
import fr.sokaris.faceme.actions.WorkCompleteListener;
import fr.sokaris.faceme.facebook.FaceBookService;
import fr.sokaris.faceme.webcam.Capture;
import fr.sokaris.faceme.webcam.WebCamException;
import fr.sokaris.faceme.widget.Book;
import fr.sokaris.faceme.widget.Face;
import fr.sokaris.faceme.widget.FaceBookAuth;

public class FaceMe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Face face;
	private Book book;
	private FaceBookService fbService;

	public FaceMe(Capture c) throws WebCamException, FacebookException,
			IOException {
		fbService = new FaceBookService();
		face = new Face(c.showSource(), c.getWidth(), c.getHeight());
		book = new Book();

		initFaceMe(c.getWidth(), c.getHeight());
		initFaceBook();
	}

	private void initFaceBook() throws FacebookException, IOException {
		FaceBookAuth auth = null;

		if (fbService.isAuthNeeded()) {
			auth = new FaceBookAuth(Messages.getString("FaceMe.login"),
					Messages.getString("FaceMe.authConnect"), fbService
							.getAuthUrl());
			auth.setVisible(true);
			fbService.finalizeAuth();
		}

		if (!fbService.isGrantPhoto()) {
			auth = new FaceBookAuth(Messages.getString("FaceMe.login"),
					Messages.getString("FaceMe.grantPhoto"), fbService
							.getGrantPhotoUrl());
			auth.setVisible(true);
		}

		if (!fbService.isGrantStatus()) {
			auth = new FaceBookAuth(Messages.getString("FaceMe.login"),
					Messages.getString("FaceMe.grantStatus"), fbService
							.getGrantStatusUrl());
			auth.setVisible(true);
		}
	}

	private void initFaceMe(int width, int height) {
		setTitle(Messages.getString("FaceMe.Title"));

		setLocationByPlatform(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel root = new JPanel(new BorderLayout());
		add(root);

		root.add(BorderLayout.CENTER, face);
		root.add(BorderLayout.SOUTH, book);

		setBounds(100, 100, width, height + 110);
		book.getSend().addActionListener(new WorkCompleteListener(this));
		face.addMouseListener(new TakePhotoListener(book));
	}

	public void postIt() throws FacebookException, IOException {
		File f = face.getPicture();
		fbService.uploadPhoto(f);
		fbService.setStatus(book.getFeel().getText());
		face.reset();
		JOptionPane.showMessageDialog(this, Messages.getString("FaceMe.done"),
				Messages.getString("FaceMe.postit"),
				JOptionPane.INFORMATION_MESSAGE);
		book.getSend().setEnabled(false);
	}

	private static Capture detectHardware() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		String os = System.getProperty("os.name");
		String className = "fr.sokaris.faceme.webcam." + os.replaceAll(" ", "")
				+ "Capture";

		Class<?> capture = Class.forName(className);
		return (Capture) capture.newInstance();
	}

	public static void main(String args[]) {

		Capture c = null;
		try {
			c = detectHardware();
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), Messages
					.getString("FaceMe.Title"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (InstantiationException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), Messages
					.getString("FaceMe.Title"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IllegalAccessException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), Messages
					.getString("FaceMe.Title"), JOptionPane.ERROR_MESSAGE);
		}

		try {
			c.start();
			JFrame faceMe = new FaceMe(c);
			faceMe.setVisible(true);
		} catch (WebCamException ex) {
			c.stop();
			JOptionPane.showMessageDialog(null, ex.getMessage(), Messages
					.getString("FaceMe.Title"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (FacebookException ex) {
			JOptionPane.showMessageDialog(null, Messages
					.getString("FaceMe.errorCode")
					+ ex.getCode(), Messages.getString("FaceMe.Title"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), Messages
					.getString("FaceMe.Title"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

	}
}