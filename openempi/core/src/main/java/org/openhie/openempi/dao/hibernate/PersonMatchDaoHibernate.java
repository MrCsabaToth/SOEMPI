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
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openhie.openempi.dao.PersonMatchDao;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonMatchDaoHibernate extends UniversalDaoHibernate implements PersonMatchDao
{
	public PersonMatch addPersonMatch(final PersonMatch personMatch) {
		log.debug("Storing a person match.");
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.saveOrUpdate(personMatch);
				session.flush();
				hydrateColumnMatchInformation(personMatch);
				log.debug("Finished saving the person match.");
				return null;
			}
		});
		return personMatch;
	}

    public void removePersonMatch(PersonMatch personMatch) {
		if (personMatch == null || personMatch.getPersonMatchId() == null) {
			return;
		}
		PersonMatch thePersonMatch = (PersonMatch) getHibernateTemplate().get(PersonMatch.class, personMatch.getPersonMatchId());
		getHibernateTemplate().delete(thePersonMatch);
		getHibernateTemplate().flush();
    }

	public PersonMatch updatePersonMatch(final PersonMatch personMatch) {
		log.debug("Updating a person match.");
		PersonMatch updatedPersonMatch = (PersonMatch) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				PersonMatch result = (PersonMatch) session.merge(personMatch);
				session.flush();
				if (result != null)
					hydrateColumnMatchInformation(result);
				log.debug("Finished updating the person match.");
				return result;
			}
		});
		return updatedPersonMatch;
	}

	@SuppressWarnings("unchecked")
	public List<PersonMatch> getPersonMatches(final Dataset dataset) {
		log.trace("Looking for matches to dataset " + dataset.getDatasetId());
		List<PersonMatch> matches = (List<PersonMatch>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from PersonMatch personMatch where " +
						"leftDataset.datasetId = :leftdsid or rightDataset.datasetId = :rightdsid ");
				query.setParameter("leftdsid", dataset.getDatasetId());
				query.setParameter("rightdsid", dataset.getDatasetId());
				List<PersonMatch> result = (List<PersonMatch>) query.list();
				if (result != null)
		    		for (PersonMatch personMatch : result)
		    			hydrateColumnMatchInformation(personMatch);
				log.trace("Found " + result.size() + " matches to dataset " + dataset.getDatasetId());
				return result;
			}
		});
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<PersonMatch> getPersonMatches(final User user) {
    	if (user == null || user.getId() == null) {
    		return null;
    	}
		List<PersonMatch> personMatches = (List<PersonMatch>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from PersonMatch personMatch where personMatch.userCreatedBy.id = :ownerid ");
				query.setParameter("ownerid", user.getId());
				List<PersonMatch> result = (List<PersonMatch>) query.list();
				return result;
			}
		});
		return personMatches;
	}

	public PersonMatch getPersonMatch(final int personMatchId) {
		return (PersonMatch) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				PersonMatch personMatch = (PersonMatch) session.load(PersonMatch.class, personMatchId);
				if (personMatch != null)
					hydrateColumnMatchInformation(personMatch);
				return personMatch;
			}});
	}

	protected void hydrateColumnMatchInformation(PersonMatch personMatch) {
		// Hydrate ColumnMatchInformation proxy
		List<ColumnMatchInformation> columnMatchInformation = personMatch.getColumnMatchInformation();
		if (columnMatchInformation != null) {
			Iterator<ColumnMatchInformation> it = columnMatchInformation.iterator();
			it.hasNext();
		}
	}
	
}
