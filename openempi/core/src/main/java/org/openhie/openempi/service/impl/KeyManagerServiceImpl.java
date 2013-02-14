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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.KeyDao;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.Key;
import org.openhie.openempi.service.KeyManagerService;

public class KeyManagerServiceImpl extends BaseServiceImpl implements KeyManagerService
{
	private KeyDao keyDao;
	
	/**
	 * Add a new key to the system.
	 * 1. First it attempts to locate the key in the system using the keys identifiers. If the key is already in
	 * the system then there is nothing more to do.
	 * 2. Since the key doesn't exist in the system yet, a new record is added.
	 * 3. The matching algorithm is invoked to identify matches and association links are established between the existing
	 * patient and other aliases of the patient that were identified based on the algorithm.
	 */
	public Key addKey() throws ApplicationException {

		Key key = new Key();
		SecureRandom rnd = new SecureRandom();
		byte[] keyPart1 = new byte[Constants.PUBLIC_KEY_PART_LENGTH];
		byte[] keyPart2 = new byte[Constants.PUBLIC_KEY_PART_LENGTH];
		byte[] keyPart3 = new byte[Constants.PUBLIC_KEY_PART_LENGTH];
		byte[] keyPart4 = new byte[Constants.PRIVATE_KEY_PART_LENGTH];
		byte[] keyPart5 = new byte[Constants.PRIVATE_KEY_PART_LENGTH];
		byte[] keyPart6 = new byte[Constants.PRIVATE_KEY_PART_LENGTH];
		rnd.nextBytes(keyPart1);
		rnd.nextBytes(keyPart2);
		rnd.nextBytes(keyPart3);
		rnd.nextBytes(keyPart4);
		rnd.nextBytes(keyPart5);
		rnd.nextBytes(keyPart6);
		key.setPublicKeyPart1(keyPart1);
		key.setPublicKeyPart2(keyPart2);
		key.setPublicKeyPart3(keyPart3);
		key.setPrivateKeyPart1(keyPart4);
		key.setPrivateKeyPart2(keyPart5);
		key.setPrivateKeyPart3(keyPart6);

		key = saveKey(key);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_KEY_EVENT_TYPE, "Added a new key record", key);
		
		return key;
	}

	public void deleteKey(long keyId) throws ApplicationException {

		Key keyFound = keyDao.getKey(keyId);
		if (keyFound == null) {
			log.warn("While attempting to delete a key was not able to locate a record with the given identifier: " + keyId);
			throw new ApplicationException("Key record to be deleted does not exist in the system.");
		}
		java.util.Date now = new java.util.Date();
		keyFound.setDateVoided(now);
		keyFound.setUserVoidedBy(Context.getUserContext().getUser());
		keyDao.deleteKey(keyFound);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_KEY_EVENT_TYPE, "Deleted a key record", keyFound);
	}

	public Key getKey(long keyId) {
		Key keyFound = keyDao.getKey(keyId);
		if (keyFound != null) {
			return keyFound;
		}
		return null;
	}

	private Key saveKey(Key key) {
		log.debug("Current user is " + Context.getUserContext().getUser());
		java.util.Date now = new java.util.Date();
		key.setDateCreated(now);
		key.setUserCreatedBy(Context.getUserContext().getUser());
		return keyDao.addKey(key);
	}

	public KeyDao getKeyDao() {
		return keyDao;
	}

	public void setKeyDao(KeyDao keyDao) {
		this.keyDao = keyDao;
	}

}
