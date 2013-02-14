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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;

public class BaseServiceTestCase extends BaseManagerTestCase
{	
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}
	
	public void setupContext() {
		getApplicationContext().getBean("context");
//		System.out.println(context.getUserDao());
		Context.startup();
		Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
	}

	protected void deletePerson(String tableName, Person person) throws ApplicationException {
		List<Person> persons = Context.getPersonQueryService().getPersonsByExample(tableName, person);
		for (Person personFound : persons) {
			Context.getPersonManagerService().deletePerson(tableName, personFound.getPersonId());
		}
	}
}
