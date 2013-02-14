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
package org.openhie.openempi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.AuditEventDao;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Key;
import org.openhie.openempi.model.Salt;
import org.openhie.openempi.service.AuditEventService;
import org.openhie.openempi.service.ValidationService;

public class AuditEventServiceImpl extends BaseServiceImpl implements AuditEventService
{
	private AuditEventDao auditEventDao;
	
	private Map<String, AuditEventType> eventTypeMap;
	
	@SuppressWarnings("unchecked")
	public void init() {
		log.info("Initializing the Audit Event Service.");
		eventTypeMap = new HashMap<String, AuditEventType>();
		List<AuditEventType> types = (List<AuditEventType>) auditEventDao.getAll(AuditEventType.class);
		for (AuditEventType auditEventType : types) {
			eventTypeMap.put(auditEventType.getAuditEventTypeCode(), auditEventType);
		}
		log.debug("Built the map of audit event types with count of " + types.size());
	}

	@SuppressWarnings("unchecked")
	public List<AuditEvent> getAllAuditEvents() {
		return (List<AuditEvent>) auditEventDao.getAll(AuditEvent.class);
	}

	public AuditEventType getAuditEventTypeByCode(String eventTypeCode) {
		return eventTypeMap.get(eventTypeCode);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Person refPerson) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, refPerson, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Person refPerson,
			Person altRefPerson) {
		
		AuditEventType auditEventType = getAuditEventTypeByCode(auditEventTypeCode);
		if (auditEventType == null) {
			log.error("Attempted to audit an event with unknown audit event type: " + auditEventTypeCode);
			throw new ValidationException("Invalid audit event type code " + auditEventTypeCode);
		}
		
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), auditEventType, auditEventDescription, Context.getUserContext().getUser());
		auditEvent.setRefPerson(refPerson);
		auditEvent.setAltRefPerson(altRefPerson);
		
		log.debug("About to audit the event " + auditEvent);
		auditEvent = (AuditEvent) auditEventDao.save(auditEvent);
		return auditEvent;
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, (Key)null, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Key refKey) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, refKey, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Key refKey,
			Key altRefKey) {
		
		AuditEventType auditEventType = getAuditEventTypeByCode(auditEventTypeCode);
		if (auditEventType == null) {
			log.error("Attempted to audit an event with unknown audit event type: " + auditEventTypeCode);
			throw new ValidationException("Invalid audit event type code " + auditEventTypeCode);
		}
		
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), auditEventType, auditEventDescription, Context.getUserContext().getUser());
		auditEvent.setRefKey(refKey);
		
		log.debug("About to audit the event " + auditEvent);
		auditEvent = (AuditEvent) auditEventDao.save(auditEvent);
		return auditEvent;
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Salt refSalt) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, refSalt, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Salt refSalt,
			Salt altRefSalt) {
		
		AuditEventType auditEventType = getAuditEventTypeByCode(auditEventTypeCode);
		if (auditEventType == null) {
			log.error("Attempted to audit an event with unknown audit event type: " + auditEventTypeCode);
			throw new ValidationException("Invalid audit event type code " + auditEventTypeCode);
		}
		
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), auditEventType, auditEventDescription, Context.getUserContext().getUser());
		auditEvent.setRefSalt(refSalt);
		
		log.debug("About to audit the event " + auditEvent);
		auditEvent = (AuditEvent) auditEventDao.save(auditEvent);
		return auditEvent;
	}

	public AuditEvent saveAuditEvent(AuditEvent auditEvent) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(auditEvent);
		
		return (AuditEvent) auditEventDao.save(auditEvent);
	}

	public AuditEventDao getAuditEventDao() {
		return auditEventDao;
	}

	public void setAuditEventDao(AuditEventDao auditEventDao) {
		this.auditEventDao = auditEventDao;
	}


}
