/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.loader;

import java.util.List;

import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.model.Person;

public interface DataLoaderServiceSelector
{
	public DataLoaderServiceType[] getDataLoaderServiceTypes();
	
	public List<String> getDataLoaderServiceNames();
	
	public DataLoaderServiceType getDataLoaderServiceType(String name);
	
	public void loadFile(String dataLoaderServiceTypeName, String filename, String tableName,
			LoaderConfig loaderConfiguration, boolean applyFieldTransformations);

	public void loadTable(String dataLoaderServiceTypeName, String hostAddress,
			String dbUserName, String dbPassword, String dbName, String sourceTableName,
			String targetTableName, LoaderConfig loaderConfiguration, boolean applyFieldTransformations);

	public void loadPerson(String dataLoaderServiceTypeName, String tableName, Person person,
			boolean applyFieldTransformations);
}
