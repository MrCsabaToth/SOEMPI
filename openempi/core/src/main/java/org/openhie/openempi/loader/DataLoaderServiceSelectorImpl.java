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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public class DataLoaderServiceSelectorImpl extends BaseServiceImpl implements DataLoaderServiceSelector
{
	private HashMap<String,DataLoaderService> dataServiceServiceTypeMap;

	public DataLoaderServiceSelectorImpl() {
		super();
	}

	// Factory method pattern
	private DataLoaderService getDataLoaderService(String dataLoaderServiceType) {
		DataLoaderService dataLoaderService = dataServiceServiceTypeMap.get(dataLoaderServiceType);
		if (dataLoaderService == null) {
			log.error("Unknown dataLoader service requested: " + dataLoaderServiceType);
			throw new ValidationException("Unknown dataLoader service requested: " + dataLoaderService);
		}
		return dataLoaderService;
	}

	public DataLoaderServiceType getDataLoaderServiceType(String name) {
		DataLoaderService service = dataServiceServiceTypeMap.get(name);
		if (service == null) {
			return null;
		}
		return new DataLoaderServiceType(name, service);
	}

	public DataLoaderServiceType[] getDataLoaderServiceTypes() {
		DataLoaderServiceType[] list = new DataLoaderServiceType[dataServiceServiceTypeMap.keySet().size()];
		int index = 0;
		for (String key : dataServiceServiceTypeMap.keySet()) {
			list[index++] = new DataLoaderServiceType(key, dataServiceServiceTypeMap.get(key));
		}
		return list;
	}

	public HashMap<String, DataLoaderService> getDataLoaderServiceTypeMap() {
		return dataServiceServiceTypeMap;
	}

	public List<String> getDataLoaderServiceNames() {
		List<String> dataLoaderServiceNames = new ArrayList<String>();
		for (String key : dataServiceServiceTypeMap.keySet()) {
			dataLoaderServiceNames.add(key);
		}
		return dataLoaderServiceNames;
	}

	public void setDataLoaderServiceTypeMap(HashMap<String, DataLoaderService> dataLoaderServiceTypeMap) {
		this.dataServiceServiceTypeMap = dataLoaderServiceTypeMap;
	}

	public void loadFile(String dataLoaderServiceTypeName, String filename, String tableName,
			LoaderConfig loaderConfiguration, boolean populateCustomFields)
	{
		DataLoaderService dataLoaderService = getDataLoaderService(dataLoaderServiceTypeName);
		dataLoaderService.loadFile(filename, tableName, loaderConfiguration, populateCustomFields);
	}

	public void loadTable(String dataLoaderServiceTypeName, String hostAddress,
			String dbUserName, String dbPassword, String dbName,
			String sourceTableName, String targetTableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields)
	{
		DataLoaderService dataLoaderService = getDataLoaderService(dataLoaderServiceTypeName);
		dataLoaderService.loadTable(hostAddress, dbUserName, dbPassword, dbName, sourceTableName,
				targetTableName, loaderConfiguration, populateCustomFields);
	}

	public void loadPerson(String dataLoaderServiceTypeName, String tableName, Person person,
			boolean populateCustomFields)
	{
		DataLoaderService dataLoaderService = getDataLoaderService(dataLoaderServiceTypeName);
		dataLoaderService.loadPerson(tableName, person, populateCustomFields);
	}

}
