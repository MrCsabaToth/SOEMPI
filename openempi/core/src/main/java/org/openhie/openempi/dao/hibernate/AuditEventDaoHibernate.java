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

import org.openhie.openempi.dao.AuditEventDao;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;

public class AuditEventDaoHibernate extends UniversalDaoHibernate implements AuditEventDao
{
	public List<AuditEvent> getAuditEventByType(AuditEventType auditEventType) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public AuditEventType getAuditEventTypeByCode(String auditEventTypeCode) {
		if (auditEventTypeCode == null || auditEventTypeCode.length() == 0) {
			return null;
		}
		String query = "from AuditEventType aet where aet.auditEventTypeCode = '" + auditEventTypeCode + "'";
        List<AuditEventType> values = getHibernateTemplate().find(query);
        log.trace("Search for audit event types by type: " + auditEventTypeCode + " found " + values.size() + " entries.");
        return values.get(0);
	}
}
