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
package org.openhie.openempi.service;

import java.util.List;

import javax.naming.NamingException;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.Person;

public interface RemotePersonService
{
	public void authenticate(String ipAddress, String username, String password) throws NamingException;

	public void authenticate(String ipAddress, String username, String password,
			String keyServerUserName, String keyServerPassword) throws NamingException;

	public void close() throws NamingException, ApplicationException;

	public Dataset createDatasetTable(String tableName, List<ColumnInformation> columnInformation,
			long totalRecords, boolean withIndexesAndConstraints) throws NamingException, ApplicationException;

	public void addPerson(String tableName, Person person, boolean applyFieldTransformations,
			boolean existenceCheck) throws NamingException, ApplicationException;

	public void addPersons(String tableName, List<Person> person,
			boolean applyFieldTransformations, boolean existenceCheck) throws NamingException, ApplicationException;

	public List<Person> getPersonsPaged(String tableName, long firstResult,
			int maxResults) throws NamingException, ApplicationException;

	public void addIndexesAndConstraintsToDatasetTable(String tableName, long seqStart) throws NamingException, ApplicationException;

	public void createMatchPairStatHalfTable(String protocolTypeName, String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints) throws NamingException, ApplicationException;

	public void addMatchPairStatHalves(String protocolTypeName, String statTableName, List<MatchPairStatHalf> matchPairStatHalves) throws NamingException, ApplicationException;

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String protocolTypeName, String statTableName, long seqStart, String datasetTableName) throws NamingException, ApplicationException;

	public int addPersonMatchRequest(String protocolTypeName, String tableName, String matchName,
			byte[] dhPublicKey, String matchPairStatHalfTableName) throws NamingException, ApplicationException;

	public BloomFilterParameterAdvice acquireMatchRequests(String protocolTypeName, int personMatchRequestId,
			ComponentType componentType) throws NamingException, ApplicationException;
}
