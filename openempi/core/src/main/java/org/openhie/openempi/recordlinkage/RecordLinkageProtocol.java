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
package org.openhie.openempi.recordlinkage;

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.PersonMatchRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RecordLinkageProtocol
{
	public void createMatchPairStatHalfTable(String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints);
	
	public void addMatchPairStatHalf(String statTableName, MatchPairStatHalf matchPairStatHalf);
	
	public void addMatchPairStatHalves(String statTableName, List<MatchPairStatHalf> matchPairStatHalves);

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String statTableName, String datasetTableName);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public PersonMatchRequest sendPersonMatchRequest(Dataset dataset, String remoteTableName,
			String matchName, String blockingServiceName, String matchingServiceName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword);

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Integer handlePersonMatchRequest(String tableName, String matchName,
			String blockingServiceName, String matchingServiceName,
			Integer nonce, String matchPairStatHalfTableName) throws ApplicationException;

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public BloomFilterParameterAdvice acquireMatchRequests(int personMatchRequestId, ComponentType componentType);

	// For test only
	public void testPMLinkRecords(int leftDatasetId, int rightDatasetId, String blockingServiceName, String matchingServiceName) throws ApplicationException;
	public void testPRLLinkRecords(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException;
	public void testBFReencoding(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException;
	
	public String getName();
	
	public void setName(String name);
}
