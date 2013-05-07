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
package org.openhie.openempi.recordlinkage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.ValidationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocol;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolType;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public class RecordLinkageProtocolSelectorImpl extends BaseServiceImpl implements RecordLinkageProtocolSelector
{
	private HashMap<String,RecordLinkageProtocol> recordLinkageProtocolTypeMap;

	// Factory method pattern
	private RecordLinkageProtocol getRecordLinkageProtocol(String protocolTypeName) {
		RecordLinkageProtocol recordLinkageProtocol = recordLinkageProtocolTypeMap.get(protocolTypeName);
		if (recordLinkageProtocol == null) {
			log.error("Unknown record linkage protocol requested: " + protocolTypeName);
			throw new ValidationException("Unknown record linkage protocol requested: " + protocolTypeName);
		}
		return recordLinkageProtocol;
	}
	
	public RecordLinkageProtocolType getRecordLinkageProtocolType(String name) {
		RecordLinkageProtocol recordLinkageProtocol = recordLinkageProtocolTypeMap.get(name);
		if (recordLinkageProtocol == null) {
			return null;
		}
		return new RecordLinkageProtocolType(name, recordLinkageProtocol);
	}

	public RecordLinkageProtocolType[] getRecordLinkageProtocolTypes() {
		RecordLinkageProtocolType[] list = new RecordLinkageProtocolType[recordLinkageProtocolTypeMap.keySet().size()];
		int index=0;
		for (String key : recordLinkageProtocolTypeMap.keySet()) {
			list[index++] = new RecordLinkageProtocolType(key, recordLinkageProtocolTypeMap.get(key));
		}
		return list;
	}
	
	public List<String> getRecordLinkageProtocolNames() {
		List<String> recordLinkageProtocolNames = new ArrayList<String>();
		for (String key : recordLinkageProtocolTypeMap.keySet()) {
			recordLinkageProtocolNames.add(key);
		}
		return recordLinkageProtocolNames;
	}

	public HashMap<String, RecordLinkageProtocol> getRecordLinkageProtocolTypeMap() {
		return recordLinkageProtocolTypeMap;
	}

	public void setRecordLinkageProtocolTypeMap(HashMap<String, RecordLinkageProtocol> recordLinkageProtocolTypeMap) {
		this.recordLinkageProtocolTypeMap = recordLinkageProtocolTypeMap;
	}

	//---
	public void createMatchPairStatHalfTable(String protocolTypeName, String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints) {
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.createMatchPairStatHalfTable(statTableName, datasetTableName, withIndexesAndConstraints);
	}

	public void addMatchPairStatHalf(String protocolTypeName, String statTableName, MatchPairStatHalf matchPairStatHalf) {
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.addMatchPairStatHalf(statTableName, matchPairStatHalf);
	}
	
	public void addMatchPairStatHalves(String protocolTypeName, String statTableName, List<MatchPairStatHalf> matchPairStatHalves) {
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.addMatchPairStatHalves(statTableName, matchPairStatHalves);
	}

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String protocolTypeName, String statTableName, String datasetTableName)
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.addIndexesAndConstraintsToMatchPairStatHalfTable(statTableName, datasetTableName);
	}

	public PersonMatchRequest sendPersonMatchRequest(String protocolTypeName, Dataset dataset, String remoteTableName,
			String matchName, String blockingServiceName, String matchingServiceName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword)
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		return recordLinkageProtocol.sendPersonMatchRequest(dataset, remoteTableName,
				matchName, blockingServiceName, matchingServiceName,
				keyServerUserName, keyServerPassword,
				dataIntegratorUserName, dataIntegratorPassword,
				parameterManagerUserName, parameterManagerPassword);
	}

	public Integer handlePersonMatchRequest(String protocolTypeName, String tableName, String matchName,
			String blockingServiceName, String matchingServiceName,
			Integer nonce, String matchPairStatHalfTableName) throws ApplicationException
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		return recordLinkageProtocol.handlePersonMatchRequest(tableName, matchName,
				blockingServiceName, matchingServiceName,
				nonce, matchPairStatHalfTableName);
	}

	public BloomFilterParameterAdvice acquireMatchRequests(String protocolTypeName, int personMatchRequestId, ComponentType componentType)
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		return recordLinkageProtocol.acquireMatchRequests(personMatchRequestId, componentType);
	}

	// For test only
	public void testPMLinkRecords(String protocolTypeName, int leftDatasetId, int rightDatasetId, String blockingServiceName, String matchingServiceName) throws ApplicationException
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.testPMLinkRecords(leftDatasetId, rightDatasetId, blockingServiceName, matchingServiceName);
	}

	public void testCBFLinkRecords(String protocolTypeName, int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.testPRLLinkRecords(leftPersonMatchRequestId, rightPersonMatchRequestId);
	}

	public void testBFReencoding(String protocolTypeName, int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.testBFReencoding(leftPersonMatchRequestId, rightPersonMatchRequestId);
	}

	public void testHMACEncoding(String protocolTypeName, int dataSetId, long totalRecords) throws ApplicationException
	{
		RecordLinkageProtocol recordLinkageProtocol = getRecordLinkageProtocol(protocolTypeName);
		recordLinkageProtocol.testHMACEncoding(dataSetId, totalRecords);
	}

}
