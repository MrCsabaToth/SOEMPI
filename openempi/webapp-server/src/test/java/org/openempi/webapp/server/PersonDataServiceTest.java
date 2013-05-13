/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
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
package org.openempi.webapp.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.DatasetWeb;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.util.PersonUtils;

public class PersonDataServiceTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	public void testGetPersonsByIdentifier() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		PersonWeb person = personService.getPersonById(TABLE_NAME, 1L);
		System.out.println("Found person: " + person.getPersonId() + " " + person.toString());
	}

	public void testGetPersonsByAttributes() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		PersonWeb templatePerson = new PersonWeb();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("familyName", "Mart%");
//		attributes.put("givenName", "CHIP");
		templatePerson.setAttributes(attributes);
		List<PersonWeb> persons = personService.getPersonsByExample(TABLE_NAME, templatePerson);
		for (PersonWeb person : persons) {
			System.out.println("Found person: " + person.getPersonId() + " " + person.toString());
		}		
		System.out.println("List of person has " + persons.size() + " entries");
	}
	
	public void testDatasetSupport() {
		DatasetWeb dataset = new DatasetWeb();
		dataset.setTableName("test");
		dataset.setFileName("/tmp/test.file");
		Context.startup();
		Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		personService.addDataset(dataset);
		
		List<DatasetWeb> datasets = personService.getDatasets(Constants.DEFAULT_ADMIN_USERNAME);
		for (DatasetWeb aDataset : datasets) {
			System.out.println(aDataset);
			personService.removeDataset(aDataset);
			System.out.println("Removed dataset entry");
		}		
	}
}
