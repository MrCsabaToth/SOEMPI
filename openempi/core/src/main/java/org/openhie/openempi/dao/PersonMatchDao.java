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

import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface PersonMatchDao extends UniversalDao
{
	/**
	 * Add a PersonMatch entity to the table.
	 * 
	 * @param personMatch: the PersonMatch entity to be added
	 * @return the added PersonMatch entity
	 */
    @Transactional
	public PersonMatch addPersonMatch(PersonMatch personMatch);

	/**
	 * Remove/delete a PersonMatch from to the table.
	 * 
	 * @param personMatch: the PersonMatch entity to be removed
	 */
    @Transactional
    public void removePersonMatch(PersonMatch personMatch);

	/**
	 * Update an existing PersonMatch entity in the table.
	 * 
	 * @param personMatch: the PersonMatch entity to be updated
	 * @return the updated PersonMatch entity
	 */
    @Transactional
	public PersonMatch updatePersonMatch(PersonMatch personMatch);
	
	/**
	 * Query PersonMatch entities related to a given dataset.
	 * 
	 * @param dataset: dataset
	 * 
	 * @return PersonMatch entities related to the given dataset
	 */
	public List<PersonMatch> getPersonMatches(Dataset dataset);

	/**
	 * Query PersonMatch entities related to a given dataset.
	 * 
	 * @param dataset: Dataset for which we search related PersonMatch items
	 * 
	 * @return PersonMatch entities related to the given dataset, empty list if no related items found
	 */
	public List<PersonMatch> getPersonMatches(User user);

	/**
	 * Get a PersonMatch entity with a given id.
	 * 
	 * @param personMatchId: PersonMatch identifier
	 * 
	 * @return PersonMatch entity with the given id, null if not found
	 */
	public PersonMatch getPersonMatch(int personMatchId);
}
