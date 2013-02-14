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
package org.openhie.openempi.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.util.PersonUtils;

public class PersonDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private DatasetDao datasetDao;

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public DatasetDao getDatasetDao() {
		return datasetDao;
	}

	public void setDatasetDao(DatasetDao datasetDao) {
		this.datasetDao = datasetDao;
	}

	@Test
	public void testGetPersonByPersonIdentifier() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		try {
			String datasetName = "test_dataset";
			List<Long> personIds = new ArrayList<Long>();
			PersonUtils.createTestPersonTable(personDao, datasetName, "", datasetDao, true, applicationContext,
					false, null, personIds);
			Person person = personDao.getPersonById(datasetName, personIds.get(0));
			assertTrue(person != null);
			System.out.println("Found person: " + person.toStringLong());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
}
