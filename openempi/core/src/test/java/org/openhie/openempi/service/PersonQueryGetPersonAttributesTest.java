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
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.util.PersonUtils;

public class PersonQueryGetPersonAttributesTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testperson";

	public void testGetPersonModelAttributeNames() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);
		PersonQueryService personService = Context.getPersonQueryService();
		for(FieldType.FieldTypeEnum fieldTypeFilter : FieldType.FieldTypeEnum.values()) {
			List<String> list = personService.getPersonModelAttributeNames(TABLE_NAME, fieldTypeFilter);
			System.out.println("Listing " + fieldTypeFilter + " attributes:");
			for (String attribName : list) {
				System.out.println("\t" + attribName);
			}
		}
	}

	public void testGetPersonOtherModelAttributeNames() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, TABLE_NAME, "", false, null, false, null, null);
		PersonQueryService personQueryService = Context.getPersonQueryService();
		for(FieldType.FieldTypeEnum fieldTypeFilter : FieldType.FieldTypeEnum.values()) {
			List<String> list = personQueryService.getPersonModelAttributeNames(TABLE_NAME, fieldTypeFilter);
			System.out.println("Listing " + fieldTypeFilter + " attributes:");
			for (String attribName : list) {
				System.out.println("\t" + attribName);
			}
		}
	}

}
