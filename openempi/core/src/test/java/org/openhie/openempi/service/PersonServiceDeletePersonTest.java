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

public class PersonServiceDeletePersonTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	public void testUpdatePerson() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);

		PersonQueryService personQueryService = Context.getPersonQueryService();
		try {
			List<Person> persons = personQueryService.getPersonsPaged(TABLE_NAME, 1L, 1);
			assertTrue(persons != null);
			assertTrue(persons.size() == 1);
			Long personId = persons.get(0).getPersonId();
			personManagerService.deletePerson(TABLE_NAME, personId);
			log.debug("Deleted person id is: " + personId);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}
}
