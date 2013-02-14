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
package org.openhie.openempi.blocking;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.util.PersonUtils;

public class BlockingServiceFindCandidatesTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	public void testFindCandidates() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<Person> ps = new ArrayList<Person>();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, ps, null);

		String[] matchingAttributes = { PersonUtils.GIVEN_NAME, PersonUtils.FAMILY_NAME };
		for (String attribute : matchingAttributes) {
			System.out.println("The record has a value of " + ps.get(0).getAttribute(attribute) + " for field " + attribute);
		}
		BlockingService blockingService = Context.getBlockingService();
		java.util.List<LeanRecordPair> persons = blockingService.findCandidates(TABLE_NAME, TABLE_NAME, null, null, ps.get(0));
		for (LeanRecordPair entry : persons) {
			System.out.println("Found candicate matching record pair: " + entry);
		}
	}
}
