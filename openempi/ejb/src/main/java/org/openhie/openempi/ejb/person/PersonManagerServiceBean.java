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
package org.openhie.openempi.ejb.person;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;

@Stateless(name="PersonManagerService")
@Interceptors ({SpringInjectionInterceptor.class})
public class PersonManagerServiceBean extends BaseSpringInjectableBean implements PersonManagerService {
	private static final long serialVersionUID = 4099807451716337994L;

	public Dataset createDatasetTable(String sessionKey, String tableName, List<ColumnInformation> columnInformation,
			long totalRecords, boolean withIndexesAndConstraints) throws ApplicationException
	{
		log.trace("In createTable method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		return personService.createDatasetTable(tableName, columnInformation, totalRecords, true, withIndexesAndConstraints);
	}

	public Person addPerson(String sessionKey, String tableName, Person person, boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException {
		log.trace("In addPerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		return personService.addPerson(tableName, person, applyFieldTransformations, existenceCheck);
	}

	public void addPersons(String sessionKey, String tableName, List<Person> persons, boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException {
		log.trace("In addPersons method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		personService.addPersons(tableName, persons, applyFieldTransformations, existenceCheck);
	}

	public void addIndexesAndConstraints(String sessionKey, String tableName, Long seqStart) throws ApplicationException
	{
		log.trace("In addIndexesAndConstraints method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		personService.addIndexesAndConstraintsToDatasetTable(tableName, seqStart);
	}

	public void updatePerson(String sessionKey, String tableName, Person person) throws ApplicationException {
		log.trace("In updatePerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		personService.updatePerson(tableName, person);
	}

	public void deletePerson(String sessionKey, String tableName, long personIdentifier) throws ApplicationException {
		log.trace("In deletePerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService =
				Context.getPersonManagerService();
		personService.deletePerson(tableName, personIdentifier);
	}
}