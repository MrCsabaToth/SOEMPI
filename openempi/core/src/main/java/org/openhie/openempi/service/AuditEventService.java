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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Key;
import org.openhie.openempi.model.Salt;

public interface AuditEventService
{
	public AuditEventType getAuditEventTypeByCode(String eventTypeCode);
	
	public AuditEvent saveAuditEvent(AuditEvent auditEvent);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription);

	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Person refPerson);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Person refPerson, Person altRefPerson);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Key refKey);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Salt refSalt);
	
	public List<AuditEvent> getAllAuditEvents();
}
