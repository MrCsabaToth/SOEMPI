/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openempi.webapp.client.widget;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class ServerAddressValidator implements Validator {
	public ServerAddressValidator() {
	}

	public String validate(Field<?> field, String value) {
		String res = null;
		if (!isIPAddress(value) && !isHostName(value)) {
			res = value + " doesn't look like a server address (host name or IP address)";
		}
		return res;
	}

	public static Boolean isIPAddress(String str) {
		String[] parts = str.split("\\.");

		if (parts.length != 4) {
			return false;
		}

		for (String s : parts) {
			try {
				int i = Integer.parseInt(s);
	
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
			catch (NumberFormatException e) {
				return false;
			}
		}

		return true;
	}
	
	public static Boolean isHostName(String str) {
		String[] parts = str.split("\\.");

		for (String s : parts) {
			if (!isNormal(s, false, false, true, true)) {
				return false;
			}
		}

		return true;
	}
	
	public static Boolean isNormal(String str, boolean allowUnderscore, boolean allowLeadingDigit,
			boolean allowLowercase, boolean allowDash) {
		for(int ii = 0; ii < str.length(); ii++) {
			char ch = str.charAt(ii);
			if (ch == '_') {	// allow underscore in certain cases
				if (ii == 0 || !allowUnderscore) {	// but not as the first character
					return false;
				} else {
					continue;
				}
			}
			if (ch == '-') {	// allow dash in certain cases
				if (ii == 0 || !allowDash) {	// but not as the first character
					return false;
				} else {
					continue;
				}
			}
			if (!Character.isLetterOrDigit(ch))
				return false;
			if (!allowLowercase) {
				if (Character.isLowerCase(ch))
					return false;
			}
			if (!allowLeadingDigit && ii == 0 && Character.isDigit(ch))	// Do not allow digit as a leading character
				return false;
			if (Character.isSpace(ch))
				return false;
		}
		return true;
	}
	
}
