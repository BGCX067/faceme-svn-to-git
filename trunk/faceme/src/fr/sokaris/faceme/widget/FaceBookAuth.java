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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.sokaris.faceme.Messages;

public class FaceBookAuth extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FaceBookAuth(String title, String description, String url) {
		init(title, description, url);
	}

	private void init(String title, String description, String url) {
		setTitle(title);
		setModal(true);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel root = new JPanel();
		root.setLayout(new GridLayout(3, 1));
		root.add(new JLabel(description));
		root.add(new JTextField(url, 35));
		JButton close = new JButton(Messages.getString("FaceMe.close"));
		root.add(close);
		close.addActionListener(this);
		setSize(new Dimension(300, 100));
		getContentPane().add(root);
	}

	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
}
