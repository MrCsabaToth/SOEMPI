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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.service.BaseServiceTestCase;

public class AuditEventServiceTest extends BaseServiceTestCase
{
	public void testAddAuditEvent() {
		try {
			AuditEventService auditService = Context.getAuditEventService();
			AuditEvent event = auditService.saveAuditEvent(AuditEventType.OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE, "Testing the audit event service");
			System.out.println("Generated the audit event: " + event);
			List<AuditEvent> events = auditService.getAllAuditEvents();
			for (AuditEvent auditEvent : events) {
				System.out.println("Retrieved the audit event: " + auditEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
