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

import com.google.gwt.user.client.rpc.RemoteService;

public interface PersonDataService extends RemoteService
{
	public String addPerson(String tableName, PersonWeb person);

	public String deletePerson(String tableName, Long id);

	public PersonWeb getPersonById(String tableName, Long id);

	public PersonWeb getPersonByIdTransactionally(String tableName, Long id);

	public List<PersonWeb> getPersonsByExample(String tableName, PersonWeb person);

	public List<LinkedPersonWeb> getLinkedPersons(String tableName, PersonWeb person);
	
	public DatasetWeb addDataset(DatasetWeb dataset);

	public List<DatasetWeb> getDatasets(String username);

	public String importDataset(DatasetWeb dataset, String tableName,
			String keyServerUserName, String keyServerPassword);

	public String initiateRecordLinkage(String recordLinkageProtocolName, DatasetWeb dataset, String tableName,
			String matchName, String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword);

	public boolean doesCurrentLoaderConfigurationNeedKeyServer();

	public void saveDatasetToFile(DatasetWeb dataset, String tableName);

	public void deleteDatasetFile(DatasetWeb dataset);

	public void removeDataset(DatasetWeb dataset);

	public List<ColumnInformationWeb> getColumnInformation(String tableName);

	public List<PersonMatchWeb> getPersonMatches(String username);

	public List<ColumnMatchInformationWeb> getColumnMatchInformation(PersonMatchWeb personMatch);

	public FellegiSunterParametersWeb getFellegiSunterParameters(Integer personMatchId);

	public List<PersonLinkWeb> getPersonLinks(String tableName, Long firstResult, Integer maxResults);

	public List<PersonLinkWeb> samplePersonLinks(Integer personMatchId, Integer numberOfSamples);

	public List<String> getDatasetTableNames();

	// We need to figure out how to do the following in a non-matching algorithm specific way
	// or by pushing it out to a matching-algorithm specific GUI (more likely choice)
	public PersonMatchWeb testScorePairs(String linkTableName, String leftTableName, String rightTableName,
			String matchingServiceTypeName, String blockingServiceTypeName, Boolean emOnly);

}
