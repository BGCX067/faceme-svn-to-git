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

package fr.sokaris.faceme.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.facebook.api.FacebookException;

import fr.sokaris.faceme.FaceMe;


public class WorkCompleteListener implements ActionListener {

	private FaceMe faceMe;

	public WorkCompleteListener(FaceMe faceMe) {
		this.faceMe = faceMe;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			faceMe.postIt();
		} catch (FacebookException ex) {
			JOptionPane.showMessageDialog(null, ex.getCause(), ex
					.getLocalizedMessage(), JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getCause(), ex
					.getLocalizedMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
