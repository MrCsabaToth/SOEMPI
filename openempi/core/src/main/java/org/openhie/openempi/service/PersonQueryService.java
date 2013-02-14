/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface PersonQueryService extends PersonServiceBase
{
	/**
	 * Locates a person record using the person's identifier
	 * 
	 * @param personId
	 */
	public Person getPersonById(String tableName, long identifier);
	
	public Person getPersonById(String tableName, long identifier, List<String> fields);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Person getPersonByIdTransactionally(String tableName, long personId);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Person getPersonByIdTransactionally(String tableName, long personId, List<String> fields);
	
	public List<Person> getPersonsByIds(String tableName, List<Long> personIds);
	
	public List<Person> getPersonsByIds(String tableName, List<Long> personIds, List<String> fields);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Person> getPersonsByIdsTransactionally(String tableName, List<Long> personIds);

	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Person> getPersonsByIdsTransactionally(String tableName, List<Long> personIds, List<String> fields);

	/**
	 * This method returns a list of all person records that match any of the person attributes that
	 * are provided in the search object which acts as a template.
	 * 
	 * @param person Person object with any attributes provided as search criteria
	 * @return List of person records that match the search criteria
	 */
	public List<Person> getPersonsByExample(String tableName, Person person);

	public List<Person> getPersonsByExample(String tableName, Person person, List<String> fields);

	/**
	 * This method returns a page of person records that match any of the person attributes that
	 * are provided in the search object which acts as a template.
	 * 
	 * @param person Person object with any attributes provided as search criteria
	 * @param firstResult Start index of the page
	 * @param maxResults Size of the page
	 * @return List of person records that match the search criteria
	 */
	public List<Person> getPersonsByExamplePaged(String tableName, Person person, long firstResult,
			int maxResults);

	public List<Person> getPersonsByExamplePaged(String tableName, Person person, List<String> fields, long firstResult,
			int maxResults);

	public List<Person> getPersonsPaged(String tableName, List<String> fields, long firstResult, int maxResults);

	public List<Person> getPersonsPaged(String tableName, long firstResult, int maxResults);

	public List<Person> getRandomPersons(String tableName, List<String> fields, int maxResults);
	
	public List<Person> getRandomNotNullPersons(String tableName, List<String> fields, int maxResults);

	/**
	 * Gets all PersonMatches which has the given Dataset as right or left dataset.
	 * 
	 * @param dataset: dataset supposedly participated in some matches
	 */
	public List<PersonMatch> getPersonMatches(Dataset dataset);

    /**
     * Retrieves the list of personMatch entries that are associated with a user.
     * @param user parameter to filter on
     * @return List
     */
	public List<PersonMatch> getPersonMatches(User user);

	/**
	 * Gets the PersonMatch which has the given Id.
	 * 
	 * @param personMatchId: person match Id
	 */
	public PersonMatch getPersonMatch(int personMatchId);

	public List<PersonLink> getPersonLinksPaged(String tableName, long startIndex, int pageSize);

	public List<PersonLink> samplePersonLinks(int personMatchId, int numberOfSamples);

}
