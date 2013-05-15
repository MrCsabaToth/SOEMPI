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
package org.openempi.webapp.client;

import java.util.List;

import org.openempi.webapp.client.model.ColumnInformationWeb;
import org.openempi.webapp.client.model.ColumnMatchInformationWeb;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.DatasetWeb;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonDataServiceAsync
{
	public void addPerson(String tableName, PersonWeb person, AsyncCallback<String> callback);

	public void deletePerson(String tableName, Long id, AsyncCallback<String> callback);

	public void getPersonById(String tableName, Long id, AsyncCallback<PersonWeb> callback);

	public void getPersonByIdTransactionally(String tableName, Long id, AsyncCallback<PersonWeb> callback);

	public void getPersonsByExample(String tableName, PersonWeb person, AsyncCallback<List<PersonWeb>> callback);

	public void getLinkedPersons(String tableName, PersonWeb person, AsyncCallback<List<LinkedPersonWeb>> callback);
	
	public void addDataset(DatasetWeb dataset, AsyncCallback<DatasetWeb> callback);

	public void getDatasets(String username, AsyncCallback<List<DatasetWeb>> callback);

	public void importDataset(DatasetWeb dataset, String tableName, String keyServerUserName,
			String keyServerPassword, AsyncCallback<String> callback);

	public void initiateRecordLinkage(String recordLinkageProtocolName, DatasetWeb dataset, String tableName, String matchName,
			String keyServerUserName, String keyServerPassword, String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword, AsyncCallback<String> callback);

	public void doesCurrentLoaderConfigurationNeedKeyServer(AsyncCallback<Boolean> callback);

	public void saveDatasetToFile(DatasetWeb dataset, String tableName, AsyncCallback<Void> callback);

	public void deleteDatasetFile(DatasetWeb dataset, AsyncCallback<Void> callback);

	public void removeDataset(DatasetWeb dataset, AsyncCallback<Void> callback);

	public void getColumnInformation(String tableName, AsyncCallback<List<ColumnInformationWeb>> callback);

	public void getPersonMatches(String username, AsyncCallback<List<PersonMatchWeb>> callback);

	public void getColumnMatchInformation(PersonMatchWeb personMatch, AsyncCallback<List<ColumnMatchInformationWeb>> callback);

	public void getFellegiSunterParameters(Integer personMatchId, AsyncCallback<FellegiSunterParametersWeb> callback);

	public void getPersonLinks(String tableName, Long firstResult, Integer maxResults, AsyncCallback<List<PersonLinkWeb>> callback);

	public void samplePersonLinks(Integer personMatchId, Integer numberOfSamples, AsyncCallback<List<PersonLinkWeb>> callback);

	public void getDatasetTableNames(AsyncCallback<List<String>> callback);

	public void testScorePairs(String linkTableName, String leftTableName, String rightTableName,
			String matchingServiceTypeName, String blockingServiceTypeName, Boolean emOnly,
			AsyncCallback<PersonMatchWeb> callback);
}
