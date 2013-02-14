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

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.util.PersonUtils;

public class PersonServiceTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		try {
			super.onSetUp();
			setupContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAddPerson() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person person = new Person();
		person.setPersonId(200L);
		person.setAttribute(PersonUtils.GIVEN_NAME, "Odysseas");
		person.setAttribute(PersonUtils.FAMILY_NAME, "Pentakalos");
		person.setAttribute(PersonUtils.ADDRESS1_NAME, "2930 Oak Shadow Drive");
		person.setAttribute(PersonUtils.CITY_NAME, "Herndon");
		person.setAttribute(PersonUtils.STATE_NAME, "Virginia");
		person.setAttribute(PersonUtils.SSN_NAME, "555-55-5555");
		person.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		person.setAttribute(PersonUtils.GENDER_NAME, "M");

		personManagerService.addPerson(TABLE_NAME, person, true, false);
	}
	
	public void testAddPersonTwice() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person person = new Person();
		person.setPersonId(200L);
		person.setAttribute(PersonUtils.GIVEN_NAME, "Odysseas");
		person.setAttribute(PersonUtils.FAMILY_NAME, "Pentakalos");
		person.setAttribute(PersonUtils.ADDRESS1_NAME, "2930 Oak Shadow Drive");
		person.setAttribute(PersonUtils.CITY_NAME, "Herndon");
		person.setAttribute(PersonUtils.STATE_NAME, "Virginia");
		person.setAttribute(PersonUtils.SSN_NAME, "555-55-5555");
		person.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		person.setAttribute(PersonUtils.GENDER_NAME, "M");
		
		personManagerService.addPerson(TABLE_NAME, person, true, true);
		personManagerService.addPerson(TABLE_NAME, person, true, true);
	}
	
	public void testNewPersonFields() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		Person person = new Person();
		person.setAttribute(PersonUtils.GIVEN_NAME, "Odysseas");
		person.setAttribute(PersonUtils.FAMILY_NAME, "Pentakalos");
		person.setAttribute(PersonUtils.ADDRESS1_NAME, "2930 Oak Shadow Drive");
		person.setAttribute(PersonUtils.CITY_NAME, "Herndon");
		person.setAttribute(PersonUtils.STATE_NAME, "Virginia");
		person.setAttribute(PersonUtils.SSN_NAME, "555-55-5555");
		person.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		person.setAttribute(PersonUtils.GENDER_NAME, "M");

		try {
			personManagerService.addPerson(TABLE_NAME, person, true, false);
			log.debug("Added person with id: " + person.getPersonId());
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}		
	}
	
	public void testGetPerson() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<Long> personIds = new ArrayList<Long>();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, personIds);
		PersonQueryService personQueryService = Context.getPersonQueryService();
		try {
			Person personFound = personQueryService.getPersonById(TABLE_NAME, personIds.get(0));
			log.debug("Found the person: " + personFound);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	public void testUpdatePerson() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<Long> personIds = new ArrayList<Long>();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, personIds);

		PersonQueryService personQueryService = Context.getPersonQueryService();
		Person personFound = personQueryService.getPersonById(TABLE_NAME, personIds.get(0));
		log.debug("Before update found the person: " + personFound);
		personFound.setAttribute("city", "Fairfax");
		if (personFound.getAttribute("nationality") != null) {
			personFound.setAttribute("nationality", "Greek");
			personFound.setAttribute("nationalityCode", "GRC");
		}
		personManagerService.updatePerson(TABLE_NAME, personFound);
		
		Person updatedPerson = personQueryService.getPersonById(TABLE_NAME, personIds.get(0));
		log.debug("Updated person is: " + updatedPerson);
	}

}
