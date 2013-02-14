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
package org.openhie.openempi.loader;

import java.util.List;

import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.model.Person;

public interface DataLoaderService
{
	public void loadFile(String filename, String tableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields);

	public void loadTable(String hostAddress, String dbName, String dbUserName, String dbPassword,
			String sourceTableName, String targetTableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields);

	public boolean loadPerson(String tableName, Person person, boolean populateCustomFields);

	public boolean loadPersons(String tableName, List<Person> persons, boolean populateCustomFields);

}
