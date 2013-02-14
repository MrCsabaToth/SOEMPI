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
package org.openhie.openempi.dao;

import org.openhie.openempi.Constants;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserSession;
import org.openhie.openempi.util.SessionGenerator;

public class UserSessionTest extends BaseDaoTestCase
{
	private UserDao userDao;
	private UserSessionDao userSessionDao;

	public void testAddUserSession() {
		UserSession session = new UserSession();
		User user = (User) userDao.loadUserByUsername(Constants.DEFAULT_ADMIN_USERNAME);
		session.setUser(user);
		session.setDateCreated(new java.util.Date());
		session.setSessionKey(SessionGenerator.generateSessionId());
		try {
			userSessionDao.saveUserSession(session);
			log.debug("Session is: " + session);
		} catch (Exception e) {
			log.error("Failed while saving a new session: " + e, e);
		}
	}
	
	public void testFindUserSession() {
		java.util.List<UserSession> sessions = userSessionDao.findAll();
		for (UserSession session : sessions) {
			log.debug("Found session: " + session);
			UserSession found = userSessionDao.findById(session.getSessionId());
			log.debug("Found by session id: " + found);
			found = userSessionDao.findBySessionKey(session.getSessionKey());
			log.debug("Found by session key: " + found);
		}
	}
	public UserSessionDao getUserSessionDao() {
		return userSessionDao;
	}

	public void setUserSessionDao(UserSessionDao userSessionDao) {
		this.userSessionDao = userSessionDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
