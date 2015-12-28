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

package fr.sokaris.faceme.widget;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import fr.sokaris.faceme.Messages;

public class Face extends JPanel implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BufferedImage image;
	private final Timer t;
	private final File picture;

	public Face(BufferedImage image, int width, int height) throws IOException {
		
		picture = File.createTempFile("FaceMe-", ".jpg");
		picture.deleteOnExit();

		this.image = image;
		this.t = new Timer(50, this);
		t.start();
		
		addMouseListener(this);
		setDoubleBuffered(true);
		image.setAccelerationPriority(1);
		setSize(width, height);
		setToolTipText(Messages.getString("FaceMe.tooltip"));
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image, 0, 0, this);
	}

	public void actionPerformed(ActionEvent e) {
		this.repaint();
	};

	public void shootMe() throws IOException {
		t.stop();
		ImageIO.write(image, "jpg", picture);
	}
	
	public File getPicture() {
		return picture;
	}
	
	public void reset() {
		t.setInitialDelay(1000);
		t.start();
	}

	public void mouseClicked(MouseEvent e) {
		if (t.isRunning()) {
			try {
				shootMe();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			t.start();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}