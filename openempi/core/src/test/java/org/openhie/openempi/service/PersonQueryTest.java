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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.util.PersonUtils;

public class PersonQueryTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}

	public void testGetPersonByAttributes() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person personOne = new Person();
		personOne.setAttribute(PersonUtils.CITY_NAME, "Herdon");
		personOne.setAttribute(PersonUtils.FAMILY_NAME, "Pentakalos");
		personOne.setAttribute(PersonUtils.GIVEN_NAME, "Odysseas");
		PersonQueryService personService = Context.getPersonQueryService();
		List<Person> list = personService.getPersonsByExample(TABLE_NAME, personOne);
		for (Person person : list) {
			System.out.println("Found person: " + person);
		}
	}
	
	public void testGetPersonByExtendedAttributes() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person personOne = new Person();
		personOne.setAttribute(PersonUtils.CITY_NAME, "Greenvile");
		personOne.setAttribute(PersonUtils.FAMILY_NAME, "Moorxe");
		personOne.setAttribute(PersonUtils.GIVEN_NAME, "Demi");
		personOne.setAttribute(PersonUtils.GENDER_NAME, "F");
		PersonQueryService personService = Context.getPersonQueryService();
		List<Person> list = personService.getPersonsByExample(TABLE_NAME, personOne);
		for (Person person : list) {
			System.out.println("Found person: " + person);
		}
	}
	
	public void testGetPersonByAccount() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);

		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person personOne = new Person();
		personOne.setAttribute(PersonUtils.ACCOUNT_NAME, "5819980");
		PersonQueryService personService = Context.getPersonQueryService();
		List<Person> list = personService.getPersonsByExample(TABLE_NAME, personOne);
		for (Person person : list) {
			System.out.println("Found person: " + person);
		}
	}

}
