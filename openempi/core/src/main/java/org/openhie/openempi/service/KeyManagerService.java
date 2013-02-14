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
package org.openhie.openempi.service;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Key;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Primary interface for the KeyServer.
 * 
 * @author Csaba Toth
 * @version $Revision:  $ $Date:  $
 */
public interface KeyManagerService
{
	/**
	 * Generates a new key to the keystore.
	 * 
	 * @return The added Key entity
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Key addKey() throws ApplicationException;
	
	/**
	 * Deletes a key from the keystore. The system locates the key record using the key identifier as a search criteria. If the record is not
	 * found an unchecked exception is thrown to notify the caller that this record does not exist in the system. If the record is found, the record
	 * is voided from the system rather than deleted to preserve a history.
	 *  
	 * @param keyId: identifier of the Key entity to be deleted
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteKey(long keyId) throws ApplicationException;
	
	/**
	 * Locates a key record using the key's identifier
	 * 
	 * @param keyId: identifier of the Key entity to be returned
	 * @return The queried Key entity, null if not found
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Key getKey(long keyId);
	
}
