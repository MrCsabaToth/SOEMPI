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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openhie.openempi.dao.KeyDao;
import org.openhie.openempi.model.Key;
import org.springframework.orm.hibernate3.HibernateCallback;

public class KeyDaoHibernate extends UniversalDaoHibernate implements KeyDao
{
	public Key addKey(Key key) {
		log.debug("Saving key record: " + key);
		getHibernateTemplate().saveOrUpdate(key);
		getHibernateTemplate().flush();
		log.debug("Finished saving the key.");
		return key;
	}

	public Key getKey(final long keyId) {
		return (Key) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Key k = (Key) session.load(Key.class, keyId);
				if (k != null) {
					k.toStringLong();
				}
				return k;
			}});
	}

	public void deleteKey(Key key) {
		log.trace("Voiding the key record: " + key);
		getHibernateTemplate().merge(key);
		getHibernateTemplate().flush();
	}

}
