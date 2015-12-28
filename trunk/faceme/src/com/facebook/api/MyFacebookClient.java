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

package com.facebook.api;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MyFacebookClient extends FacebookRestClient {

	private static Preferences pref = Preferences
			.userNodeForPackage(MyFacebookClient.class);
	private boolean isAuthNeeded = true;
	private String authToken;

	public MyFacebookClient(String apiKey, String secret)
			throws FacebookException, IOException {
		super(apiKey, secret);

		setIsDesktop(true);

		_sessionKey = pref.get("sessionKey", "");
		_sessionSecret = pref.get("sessionSecret", "");

		if (_sessionKey.equals("") || _sessionSecret.equals("")) {
			authToken = auth_createToken();
		} else {
			isAuthNeeded = false;
		}

	}

	public void finalizeAuth() throws FacebookException, IOException {
		try {
			auth_getSession(authToken);
			pref.put("sessionKey", _sessionKey);
			pref.put("sessionSecret", _sessionSecret);
		} catch (FacebookException e) {
			pref.remove("sessionKey");
			pref.remove("sessionSecret");
			throw e;
		} catch (NullPointerException e) {
			pref.remove("sessionKey");
			pref.remove("sessionSecret");
			throw e;
		}
	}

	public boolean isGrantStatus() throws FacebookException, IOException {
		try {
			return users_hasAppPermission(Permission.STATUS_UPDATE);
		} catch (FacebookException e) {
			pref.remove("sessionKey");
			pref.remove("sessionSecret");
			throw e;
		}
	}

	public boolean isGrantPhoto() throws FacebookException, IOException {
		try {
			return users_hasAppPermission(Permission.PHOTO_UPLOAD);
		} catch (FacebookException e) {
			pref.remove("sessionKey");
			pref.remove("sessionSecret");
			throw e;
		}
	}

	public boolean isAuthNeeded() {
		return isAuthNeeded;
	}

	public String getAuthToken() {
		return authToken;
	}

}
