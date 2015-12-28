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

package fr.sokaris.faceme.facebook;

import java.io.File;
import java.io.IOException;

import com.facebook.api.FacebookException;
import com.facebook.api.MyFacebookClient;

public class FaceBookService extends MyFacebookClient implements Keys {

	private static final String FB_URL_AUTH = "https://www.facebook.com/login.php";
	private static final String FB_URL_GRANT = "https://www.facebook.com/authorize.php";

	private String authUrl;
	private String photoUrl;
	private String statusUrl;

	public FaceBookService() throws FacebookException, IOException {

		super(API_KEY, SECRET_KEY);

		authUrl = FB_URL_AUTH + "?api_key=" + API_KEY + "&v=1.0&auth_token="
				+ getAuthToken();
		photoUrl = FB_URL_GRANT + "?api_key=" + API_KEY
				+ "&v=1.0&ext_perm=photo_upload";
		statusUrl = FB_URL_GRANT + "?api_key=" + API_KEY
				+ "&v=1.0&ext_perm=status_update";
	}

	/* (non-Javadoc)
	 * @see fr.sokaris.faceme.facebook.Service#setStatus(java.lang.String)
	 */
	public void setStatus(String newStatus) throws FacebookException,
			IOException {
		users_setStatus(newStatus);
	}

	/* (non-Javadoc)
	 * @see fr.sokaris.faceme.facebook.Service#getGrantStatusUrl()
	 */
	public String getGrantStatusUrl() {
		return statusUrl;
	}

	/* (non-Javadoc)
	 * @see fr.sokaris.faceme.facebook.Service#getGrantPhotoUrl()
	 */
	public String getGrantPhotoUrl() {
		return photoUrl;
	}

	/* (non-Javadoc)
	 * @see fr.sokaris.faceme.facebook.Service#getAuthUrl()
	 */
	public String getAuthUrl() {
		return authUrl;
	}

	/* (non-Javadoc)
	 * @see fr.sokaris.faceme.facebook.Service#uploadPhoto(java.io.File)
	 */
	public void uploadPhoto(File photo) throws FacebookException, IOException {
		photos_upload(photo);
	}
}
