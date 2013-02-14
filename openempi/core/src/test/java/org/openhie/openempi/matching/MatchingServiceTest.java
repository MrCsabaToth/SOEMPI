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
package org.openhie.openempi.matching;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.util.PersonUtils;

public class MatchingServiceTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	public void testMatchingService() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		setupContext();
		
		Person personOne = new Person();
		personOne.setAttribute(PersonUtils.ADDRESS1_NAME, "49 Applecross Road");
		personOne.setAttribute(PersonUtils.CITY_NAME, "Palo Alto");
		personOne.setAttribute(PersonUtils.STATE_NAME, "CA");
		personOne.setAttribute(PersonUtils.POSTALCODE_NAME, "94301");
		personOne.setAttribute(PersonUtils.FAMILY_NAME, "Martinez");
		personOne.setAttribute(PersonUtils.GIVEN_NAME, "Javier");
		personOne.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(1977, 2, 14);
		java.util.Date birthDate = calendar.getTime();
		personOne.setAttribute(PersonUtils.DOB_NAME, birthDate);
		
		try {
			personManagerService.addPerson(TABLE_NAME, personOne, true, false);
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
		
		Person personTwo = new Person();
		personTwo.setAttribute(PersonUtils.ADDRESS1_NAME, "49 E. Applecross Road");
		personTwo.setAttribute(PersonUtils.CITY_NAME, "Palo Alto");
		personTwo.setAttribute(PersonUtils.STATE_NAME, "CA");
		personTwo.setAttribute(PersonUtils.POSTALCODE_NAME, "94301");
		personTwo.setAttribute(PersonUtils.FAMILY_NAME, "Marteenez");
		personTwo.setAttribute(PersonUtils.GIVEN_NAME, "Havier");
		personTwo.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		calendar.set(1979, 2, 14);
		personTwo.setAttribute(PersonUtils.DOB_NAME, calendar.getTime());
		
		try {
			personManagerService.addPerson(TABLE_NAME, personTwo, true, false);
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
	}
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		try {
			Person person = new Person();
			person.setAttribute(PersonUtils.GIVEN_NAME, "Havier");
			person.setAttribute(PersonUtils.FAMILY_NAME, "Marteenez");
			deletePerson(TABLE_NAME, person);

			person.setAttribute(PersonUtils.GIVEN_NAME, "Javier");
			person.setAttribute(PersonUtils.FAMILY_NAME, "Martinez");
			deletePerson(TABLE_NAME, person);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
