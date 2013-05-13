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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openhie.openempi.dao.PersonMatchRequestDao;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonMatchRequestDaoHibernate extends UniversalDaoHibernate implements PersonMatchRequestDao
{
	public PersonMatchRequest addPersonMatchRequest(final PersonMatchRequest personMatchRequest) {
		log.debug("Storing a person matchRequest.");
		getHibernateTemplate().saveOrUpdate(personMatchRequest);
		getHibernateTemplate().flush();
		log.debug("Finished saving the person matchRequest.");
		return personMatchRequest;
	}

    public void removePersonMatchRequest(PersonMatchRequest personMatchRequest) {
		if (personMatchRequest == null || personMatchRequest.getPersonMatchRequestId() == null) {
			return;
		}
		PersonMatchRequest thePersonMatchRequest = (PersonMatchRequest) getHibernateTemplate().get(PersonMatchRequest.class, personMatchRequest.getPersonMatchRequestId());
		getHibernateTemplate().delete(thePersonMatchRequest);
		getHibernateTemplate().flush();
    }

	public PersonMatchRequest updatePersonMatchRequest(final PersonMatchRequest personMatchRequest) {
		log.debug("Updating a person matchRequest.");
		getHibernateTemplate().merge(personMatchRequest);
		getHibernateTemplate().flush();
		log.debug("Finished updating the person matchRequest.");
		return personMatchRequest;
	}

	public PersonMatchRequest getPersonMatchRequest(final int personMatchRequestId) {
		log.debug("Looking for match requests if id " + personMatchRequestId);
		return (PersonMatchRequest) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				PersonMatchRequest pmr = (PersonMatchRequest) session.load(PersonMatchRequest.class, personMatchRequestId);
				if (pmr != null) {
					pmr.toString();
				}
				log.debug("Found match requests " + pmr + " to id " + personMatchRequestId);
				return pmr;
			}
		});
	}
	
	// TODO: remove when the match will be done by the match name
	@SuppressWarnings("unchecked")
	public List<PersonMatchRequest> getPersonMatchRequestsForMatchName(final String matchName) {
		log.debug("Looking for match requests for match name " + matchName);
		ValidationUtil.sanityCheckFieldName(matchName);
		return (List<PersonMatchRequest>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<PersonMatchRequest> pmrs =
						(List<PersonMatchRequest>) getHibernateTemplate().find("from PersonMatchRequest " +
							"where matchName=? order by dateCreated", matchName);
				if (pmrs != null) {
					for (PersonMatchRequest pmr : pmrs)
						pmr.toString();
				}
				log.debug("Found " + pmrs.size() + " match requests for match name " + matchName);
				return pmrs;
			}
		});
	}
	
}
