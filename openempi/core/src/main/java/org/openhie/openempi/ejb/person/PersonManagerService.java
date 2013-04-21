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

import javax.ejb.Remote;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;

@Remote
public interface PersonManagerService
{
	public Dataset createDatasetTable(String sessionKey, String tableName,
			List<ColumnInformation> columnInformation, long totalRecords,
			boolean withIndexesAndConstraints) throws ApplicationException;

	public Person addPerson(String sessionKey, String tableName, Person person,
			boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException;

	public void addPersons(String sessionKey, String tableName, List<Person> persons,
			boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException;

	public void addIndexesAndConstraints(String sessionKey, String tableName) throws ApplicationException;

	public void updatePerson(String sessionKey, String tableName, Person person) throws ApplicationException;

	public void deletePerson(String sessionKey, String tableName, long personIdentifier) throws ApplicationException;
}
