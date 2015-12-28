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

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.sokaris.faceme.Messages;

public class Book extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton send;
	private JLabel nick;
	private JTextField feel;
	
	public Book() {
		send = new JButton();
		nick = new JLabel(Messages.getString("FaceMe.iam"));
		feel = new JTextField();
		init();
	}
	
	private void init() {
		send.setText(Messages.getString("FaceMe.postit"));
		send.setToolTipText(Messages.getString("FaceMe.tooltip"));
		feel.setToolTipText(Messages.getString("FaceMe.tooltip"));
		send.setEnabled(false);
		
		setLayout(new GridLayout(3, 1));
		add(nick);
		add(feel);
		add(send);
	}

	public final JButton getSend() {
		return send;
	}

	public final JLabel getNick() {
		return nick;
	}

	public final JTextField getFeel() {
		return feel;
	}
	
}
