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
package org.openhie.openempi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.ValidationService;

public class PersonQueryServiceImpl extends PersonServiceBaseImpl implements PersonQueryService
{
	public Person getPersonById(String tableName, long personId) {
		return getPersonById(tableName, personId, null);
	}

	public Person getPersonById(String tableName, long personId, List<String> fields) {
		Person foundPerson = personDao.getPersonById(tableName, personId, fields);
		return foundPerson;
	}

	public Person getPersonByIdTransactionally(String tableName, long personId) {
		return getPersonById(tableName, personId);
	}

	public Person getPersonByIdTransactionally(String tableName, long personId, List<String> fields) {
		return getPersonById(tableName, personId, fields);
	}

	public List<Person> getPersonsByIds(String tableName, List<Long> personIds) {
		return getPersonsByIds(tableName, personIds, null);
	}

	public List<Person> getPersonsByIds(String tableName, List<Long> personIds, List<String> fields) {
		if (personIds == null || personIds.size() == 0) {
			return null;
		}
		List<Person> persons = personDao.getPersonsByIds(tableName, personIds);
		return persons;
	}

	public List<Person> getPersonsByIdsTransactionally(String tableName, List<Long> personIds) {
		return getPersonsByIds(tableName, personIds);
	}

	public List<Person> getPersonsByIdsTransactionally(String tableName, List<Long> personIds, List<String> fields) {
		return getPersonsByIds(tableName, personIds, fields);
	}

	public List<Person> getPersonsPaged(String tableName, long firstRecord, int maxRecords) {
		return getPersonsByExamplePaged(tableName, null, firstRecord, maxRecords);
	}

	public List<Person> getPersonsPaged(String tableName, List<String> fields, long firstRecord, int maxRecords) {
		return getPersonsByExamplePaged(tableName, null, fields, firstRecord, maxRecords);
	}

	public List<Person> getRandomPersons(String tableName, List<String> fields, int maxResults) {
		List<Person> persons = personDao.getRandomPersonsByExample(tableName, null, fields, maxResults);
		return persons;
	}

	public List<Person> getRandomNotNullPersons(String tableName, List<String> fields, int maxResults) {
		List<Person> persons = personDao.getRandomNotNullPersonsByExample(tableName, null, fields, maxResults);
		return persons;
	}

	public List<Person> getPersonsByExample(String tableName, Person person) {
		return getPersonsByExample(tableName, person, null);
	}

	public List<Person> getPersonsByExample(String tableName, Person person, List<String> fields) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		List<Person> persons = personDao.getPersonsByExample(tableName, person, fields);
		return persons;
	}

	public List<Person> getPersonsByExamplePaged(String tableName, Person person, long firstResult,
			int maxResults) {
		return getPersonsByExamplePaged(tableName, person, null, firstResult, maxResults);
	}

	public List<Person> getPersonsByExamplePaged(String tableName, Person person, List<String> fields,
			long firstResult, int maxResults) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		List<Person> persons = personDao.getPersonsByExamplePaged(tableName, person, fields, firstResult, maxResults);
		return persons;
	}

	public List<PersonMatch> getPersonMatches(Dataset dataset)
	{
		return personMatchDao.getPersonMatches(dataset);
	}

	public List<PersonMatch> getPersonMatches(User user) {
		log.debug("Loading personMatch entries for user: " + user);
		return personMatchDao.getPersonMatches(user);
	}
	
	public PersonMatch getPersonMatch(int personMatchId)
	{
		return personMatchDao.getPersonMatch(personMatchId);
	}

	public List<PersonLink> getPersonLinksPaged(String tableName, long firstResult, int maxResults)
	{
		return personLinkDao.getPersonLinksPaged(tableName, null, null, firstResult, maxResults);
	}

	public List<PersonLink> samplePersonLinks(int personMatchId, int numberOfSamples)
	{
		PersonMatch personMatch = getPersonMatch(personMatchId);
		int stride = (int)(personMatch.getTotalRecords() / numberOfSamples);
		List<PersonLink> samples = new ArrayList<PersonLink>();
		for (long i = 0; i < personMatch.getTotalRecords(); i += stride) {
			List<PersonLink> personLinks = getPersonLinksPaged(personMatch.getMatchTitle(), i, stride);
			if (personLinks != null && personLinks.size() > 0)
				samples.addAll(personLinks);
		}
		return samples;
	}

}
