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

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Salt;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * One of the primary interfaces of the KeyServer.
 * 
 * @author Csaba Toth
 * @version $Revision:  $ $Date:  $
 */
public interface SaltManagerService
{
	/**
	 * Adds a salt record to the KeyServer.
	 * 
	 * @return The added Salt entity
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Salt addSalt() throws ApplicationException;
	
	/**
	 * Deletes a salt from the EMPI. The system locates the salt record using the salt identifiers as search criteria. If the record is not
	 * found an unchecked exception is thrown to notify the caller that this record does not exist in the system. If the record is found, the record
	 * is voided from the system rather than deleted to preserve a history.
	 *  
	 * @param saltId: identifier of the Salt entity to be deleted
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteSalt(long saltId) throws ApplicationException;
	
	/**
	 * Locates a salt record using the salt's identifier
	 * 
	 * @param saltId: identifier of the Salt entity to be returned
	 * @return The queried Salt entity, null if not found
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Salt getSalt(long saltId) throws ApplicationException;
	
	/**
	 * Locates a range of salt records using two bound identifiers
	 * 
	 * @param startId: start of the salt identifier range
	 * @param endId: end of the salt identifier range
	 * @return The queried Salt entities, empty list if none found
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public List<Salt> getSalts(long startId, long endId) throws ApplicationException;
	
}
