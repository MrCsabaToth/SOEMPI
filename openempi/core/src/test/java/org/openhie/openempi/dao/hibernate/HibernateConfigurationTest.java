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

import org.openhie.openempi.dao.BaseDaoTestCase;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.persister.entity.EntityPersister;

import java.util.Map;

public class HibernateConfigurationTest extends BaseDaoTestCase {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@SuppressWarnings("rawtypes")
	public void testColumnMapping() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Map metadata = sessionFactory.getAllClassMetadata();
            for (Object o : metadata.values()) {
                EntityPersister persister = (EntityPersister) o;
                String className = persister.getEntityName();
                log.debug("Trying select * from: " + className);
                Query q = session.createQuery("from " + className + " c");
                q.iterate();
                log.debug("ok: " + className);
            }
        } finally {
            session.close();
        }
    }
}
