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
package org.openhie.openempi.service.impl;

import java.security.SecureRandom;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.SaltDao;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.Salt;
import org.openhie.openempi.service.SaltManagerService;

public class SaltManagerServiceImpl extends BaseServiceImpl implements SaltManagerService
{
	private SaltDao saltDao;
	
	/**
	 * Add a new salt to the system.
	 * 1. First it attempts to locate the salt in the system using the salts identifiers. If the salt is already in
	 * the system then there is nothing more to do.
	 * 2. Since the salt doesn't exist in the system yet, a new record is added.
	 * 3. The matching algorithm is invoked to identify matches and association links are established between the existing
	 * patient and other aliases of the patient that were identified based on the algorithm.
	 */
	public Salt addSalt() throws ApplicationException {
		
		Salt salt = new Salt();
		SecureRandom rnd = new SecureRandom();
		byte[] saltBytes = new byte[Constants.SALT_LENGTH];
		rnd.nextBytes(saltBytes);
		salt.setSalt(saltBytes);

		saveSalt(salt);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_SALT_EVENT_TYPE, "Added a new salt record", salt);
		
		return salt;
	}

	public void deleteSalt(long saltId) throws ApplicationException {

		Salt saltFound = saltDao.getSalt(saltId);
		if (saltFound == null) {
			log.warn("While attempting to delete a salt was not able to locate a record with the given identifier: " + saltId);
			throw new ApplicationException("Salt record to be deleted does not exist in the system.");
		}		

		java.util.Date now = new java.util.Date();
		saltFound.setDateVoided(now);
		saltFound.setUserVoidedBy(Context.getUserContext().getUser());
		saltDao.deleteSalt(saltFound);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_SALT_EVENT_TYPE, "Deleted a salt record", saltFound);
	}

	private void saveSalt(Salt salt) {
		log.debug("Current user is " + Context.getUserContext().getUser());
		java.util.Date now = new java.util.Date();
		salt.setDateCreated(now);
		salt.setUserCreatedBy(Context.getUserContext().getUser());
		saltDao.addSalt(salt);
	}
	
	public SaltDao getSaltDao() {
		return saltDao;
	}

	public void setSaltDao(SaltDao saltDao) {
		this.saltDao = saltDao;
	}

	public Salt getSalt(long saltId) throws ApplicationException {
		Salt saltFound = saltDao.getSalt(saltId);
		if (saltFound != null) {
			return saltFound;
		}
		return null;
	}

	public List<Salt> getSalts(long startId, long endId) throws ApplicationException {
		List<Salt> saltsFound = saltDao.getSalts(startId, endId);
		return saltsFound;
	}

}
