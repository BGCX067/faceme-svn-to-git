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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fr.sokaris.faceme.widget.Book;

public class TakePhotoListener implements MouseListener {

	private Book book;
	
	public TakePhotoListener(Book book) {
		this.book = book;
	}
	
	public void mouseClicked(MouseEvent e) {
		book.getSend().setEnabled(true);
	}

	public void mouseEntered(MouseEvent e) {	}

	public void mouseExited(MouseEvent e) {	}

	public void mousePressed(MouseEvent e) {	}

	public void mouseReleased(MouseEvent e) {	}

}
