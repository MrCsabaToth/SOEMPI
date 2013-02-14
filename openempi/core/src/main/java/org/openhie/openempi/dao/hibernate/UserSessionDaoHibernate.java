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
package org.openhie.openempi.dao.hibernate;

import java.util.List;

import org.openhie.openempi.dao.UserSessionDao;
import org.openhie.openempi.model.UserSession;
import org.openhie.openempi.util.ValidationUtil;

public class UserSessionDaoHibernate extends UniversalDaoHibernate implements UserSessionDao
{
	public UserSession findById(int sessionId) {
		log.debug("Locating session by id: " + sessionId);
		UserSession session = (UserSession) getHibernateTemplate().load(UserSession.class, sessionId);
		log.debug("Found session: " + session);
		return session;
	}

	@SuppressWarnings("unchecked")
	public UserSession findBySessionKey(String sessionKey) {
		log.debug("Locating session by key: " + sessionKey);
		ValidationUtil.sanityCheckSessionKey(sessionKey);
		List<UserSession> list = (List<UserSession>) getHibernateTemplate().find("from UserSession where sessionKey = ?", new String[] { sessionKey });
		if (list.size() == 0) {
			return null;
		}
		UserSession session = list.get(0);
		log.debug("Found session: " + session);
		return session;
	}

	public void saveUserSession(UserSession session) {
		log.debug("Saving session record: " + session);
		getHibernateTemplate().saveOrUpdate(session);
		log.debug("Finished saving the session.");
	}

	@SuppressWarnings("unchecked")
	public List<UserSession> findAll() {
		List<UserSession> sessions = (List<UserSession>) this.getAll(UserSession.class);
		return sessions;
	}
}
