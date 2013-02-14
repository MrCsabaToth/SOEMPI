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
package org.openhie.openempi.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openhie.openempi.dao.SaltDao;
import org.openhie.openempi.model.Salt;
import org.springframework.orm.hibernate3.HibernateCallback;

public class SaltDaoHibernate extends UniversalDaoHibernate implements SaltDao
{
	public void addSalt(Salt salt) {
		log.debug("Saving salt record: " + salt);
		getHibernateTemplate().save(salt);
		getHibernateTemplate().flush();
		log.debug("Finished saving the salt.");
	}

	public Salt getSalt(final long saltId) {
		return (Salt) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Salt s = (Salt) session.load(Salt.class, saltId);
				if (s != null) {
					s.toStringLong();
				}
				return s;
			}});
	}
	
	@SuppressWarnings("unchecked")
	public List<Salt> getSalts(final long startId, final long endId) {
		return (List<Salt>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Salt.class)
					.add(Restrictions.isNull("dateVoided"))
					.add(Restrictions.ge("saltId", startId))
					.add(Restrictions.le("saltId", endId));

				List<Salt> list = criteria.list();
				log.debug("Query by partial identifier returned: " + list.size() + " elements.");
				if (list == null || list.size() <= 0)
					return null;
				return list.get(0);
			}
		});
	}
	
	public void deleteSalt(Salt salt) {
		log.trace("Voiding the salt record: " + salt);
		getHibernateTemplate().merge(salt);
		getHibernateTemplate().flush();
	}

}
