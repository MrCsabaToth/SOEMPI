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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.model.PersonMatchRequest;
import org.springframework.transaction.annotation.Transactional;

public interface PersonMatchRequestDao extends UniversalDao
{
	/**
	 * Add a PersonMatchRequest entity to the table.
	 * 
	 * @param personMatchRequest: the PersonMatchRequest entity to be added
	 * @return the added PersonMatchRequest entity
	 */
	@Transactional
	public PersonMatchRequest addPersonMatchRequest(PersonMatchRequest personMatchRequest);
	
	/**
	 * Update an existing PersonMatchRequest entity in the table.
	 * 
	 * @param personMatchRequest: the PersonMatchRequest entity to be updated
	 * @return the updated PersonMatchRequest entity
	 */
	@Transactional
	public PersonMatchRequest updatePersonMatchRequest(PersonMatchRequest personMatchRequest);
	
	/**
	 * Get a PersonMatchRequest entity with a given id.
	 * 
	 * @param personMatchRequestId: PersonMatchRequest identifier
	 * 
	 * @return PersonMatchRequest entity with the given id, null if not found
	 */
	public PersonMatchRequest getPersonMatchRequest(int personMatchRequestId);

	/**
	 * Query PersonMatchRequest entities related to a given match.
	 * 
	 * @param matchName: String od match name for which we search related PersonMatchRequest items
	 * 
	 * @return PersonMatchRequest entities related to the given match, empty list if no related items found
	 */
	public List<PersonMatchRequest> getPersonMatchRequestsForMatchName(String  matchName);

}
