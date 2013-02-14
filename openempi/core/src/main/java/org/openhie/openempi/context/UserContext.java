/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.UserManager;

/**
 * Implementation of UserContext
 *
 * @author Odysseas Pentakalos
 */
public class UserContext
{
	private static final Log log = LogFactory.getLog(UserContext.class);
	
	private UserManager userManager;
	
	private User user;
	private String sessionKey;
	
	public String authenticate(String username, String password) throws AuthenticationException {
		User user = userManager.authenticate(username, password);
		sessionKey = userManager.createSession(user);
		log.debug("Authentication request succeeded for user " + username);
		this.user = user;
		return sessionKey;
 	}

	public User authenticate(String sessionKey) throws AuthenticationException {
		User user = userManager.authenticate(sessionKey);
		this.sessionKey = sessionKey;
		this.user = user;
		return user;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
