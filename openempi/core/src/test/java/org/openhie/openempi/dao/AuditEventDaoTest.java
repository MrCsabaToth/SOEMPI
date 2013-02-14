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

import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.User;

public class AuditEventDaoTest extends BaseDaoTestCase
{
	private AuditEventDao auditEventDao;
	private PersonDao personDao;
	private UserDao userDao;
	
	@SuppressWarnings("unchecked")
	public void testGetAllAuditEventType() {
		try {
			List<AuditEventType> allTypes = auditEventDao.getAll(AuditEventType.class);
			for (AuditEventType auditEventType : allTypes) {
				System.out.println("Found audit event type: " + auditEventType);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void testGetAuditEventTypeByCode() {
		try {
			List<AuditEventType> allTypes = auditEventDao.getAll(AuditEventType.class);
			for (AuditEventType auditEventType : allTypes) {
				AuditEventType auditEventTypeFound = auditEventDao.getAuditEventTypeByCode(auditEventType.getAuditEventTypeCode());
				assertNotNull(auditEventTypeFound);
				System.out.println("Found audit event type: " + auditEventTypeFound.getAuditEventTypeName() + " searching by code " + 
						auditEventType.getAuditEventTypeCode());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testAddAuditEvent() {
		AuditEventType type = auditEventDao.getAuditEventTypeByCode(AuditEventType.OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE);
		User user = (User) userDao.loadUserByUsername(Constants.DEFAULT_ADMIN_USERNAME);
		assertNotNull(type);
		assertNotNull(user);
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), type, "Testing the generation of an audit event", user);
		auditEventDao.save(auditEvent);
		System.out.println("Saved the audit event " + auditEvent);
		List<AuditEvent> auditEvents = (List<AuditEvent>) auditEventDao.getAll(AuditEvent.class);
		for (AuditEvent auditEventFound : auditEvents) {
			System.out.println("Found the audit event: " + auditEventFound.getAuditEventDescription());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testGetAllAuditEvents() {
		List<AuditEvent> auditEvents = auditEventDao.getAll(AuditEvent.class);
		for (AuditEvent auditEvent : auditEvents) {
			System.out.println("Found the audit event: " + auditEvent.getAuditEventDescription());
		}
	}

	public AuditEventDao getAuditEventDao() {
		return auditEventDao;
	}

	public void setAuditEventDao(AuditEventDao auditEventDao) {
		this.auditEventDao = auditEventDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
