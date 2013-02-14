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
package org.openhie.openempi.util;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Utility class for validation.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public final class ValidationUtil {
    private static final Log log = LogFactory.getLog(ValidationUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private ValidationUtil() {
    }

	public static void sanityCheckFieldName(String fieldName)
	{
		if (fieldName != null) {
			if (!isNormal(fieldName, true, true, true, false)) {
				String errorText = "FieldName '" + fieldName + "' doesn't seem to be valid!";
				log.error(errorText);
				throw new IllegalArgumentException(errorText);
			}
		}
	}
	
	public static void sanityCheckSessionKey(String sessionKey)
	{
		if (sessionKey != null) {
			if (!isNormal(sessionKey, false, true, false, false)) {
				String errorText = "SessionId '" + sessionKey + "' doesn't seem to be valid!";
				log.error(errorText);
				throw new IllegalArgumentException(errorText);
			}
		}
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
			if (Character.isWhitespace(ch))
				return false;
		}
		return true;
	}
	
	// TODO: remove
	public static void sanityCheckIPAddress(String ipAddress)
	{
		if (ipAddress != null) {
			if (!isIPAddress(ipAddress)) {
				String errorText = "IP Address '" + ipAddress + "' is not well formed!";
				log.error(errorText);
				throw new IllegalArgumentException(errorText);
			}
		}
	}
	
	public static Boolean isIPAddress(String str) {
		// Do not forget to update web client side version of this function
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
		// Do not forget to update web client side version of this function
		String[] parts = str.split("\\.");

		for (String s : parts) {
			if (!isNormal(s, false, false, true, true)) {
				return false;
			}
		}

		return true;
	}

	public static Boolean isServerAddres(String str) {
		return isIPAddress(str) || isHostName(str);
	}

	public static void sanityCheckServerAddress(String str)
	{
		if (str != null) {
			if (!isServerAddres(str)) {
				String errorText = "Server address '" + str + "' is not well formed (host name or IP address)!";
				log.error(errorText);
				throw new IllegalArgumentException(errorText);
			}
		}
	}
	

	private static Pattern p = Pattern.compile("^[A-Za-z0-9]+$");

	public static Boolean isAlnumRegex(String str) {
		return p.matcher(str).matches();
	}

}
