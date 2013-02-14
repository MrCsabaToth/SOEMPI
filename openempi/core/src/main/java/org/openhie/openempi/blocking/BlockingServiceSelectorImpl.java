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
package org.openhie.openempi.blocking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.ValidationException;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public class BlockingServiceSelectorImpl extends BaseServiceImpl implements BlockingServiceSelector
{
	private HashMap<String,BlockingService> blockingServiceTypeMap;

	public void init(String blockingServiceType, Map<String, Object> configParameters) {
		BlockingService blockingService = getBlockingService(blockingServiceType);
		blockingService.init(configParameters);
	}

	// Factory method pattern
	private BlockingService getBlockingService(String blockingServiceType) {
		BlockingService blockingService = blockingServiceTypeMap.get(blockingServiceType);
		if (blockingService == null) {
			log.error("Unknown blocking service requested: " + blockingServiceType);
			throw new ValidationException("Unknown blocking service requested for field blocking: " + blockingServiceType);
		}
		return blockingService;
	}
	
	public BlockingServiceType getBlockingServiceType(String name) {
		BlockingService service = blockingServiceTypeMap.get(name);
		if (service == null) {
			return null;
		}
		return new BlockingServiceType(name, service);
	}

	public BlockingServiceType[] getBlockingServiceTypes() {
		BlockingServiceType[] list = new BlockingServiceType[blockingServiceTypeMap.keySet().size()];
		int index = 0;
		for (String key : blockingServiceTypeMap.keySet()) {
			list[index++] = new BlockingServiceType(key, blockingServiceTypeMap.get(key));
		}
		return list;
	}
	
	public HashMap<String, BlockingService> getBlockingServiceTypeMap() {
		return blockingServiceTypeMap;
	}

	public List<String> getBlockingServiceNames() {
		List<String> blockingServiceNames = new ArrayList<String>();
		for (String key : blockingServiceTypeMap.keySet()) {
			blockingServiceNames.add(key);
		}
		return blockingServiceNames;
	}
	
	public void setBlockingServiceTypeMap(HashMap<String, BlockingService> blockingServiceTypeMap) {
		this.blockingServiceTypeMap = blockingServiceTypeMap;
	}

	//---
	public RecordPairSource getRecordPairSource(String blockingServiceTypeName,
			String leftTableName, String rightTableName) {
		BlockingService blockingService = getBlockingService(blockingServiceTypeName);
		return blockingService.getRecordPairSource(leftTableName, rightTableName);
	}

	public RecordPairSource getRecordPairSource(String blockingServiceTypeName, List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName) {
		BlockingService blockingService = getBlockingService(blockingServiceTypeName);
		return blockingService.getRecordPairSource(blockingRounds, leftTableName, rightTableName);
	}

	public void getRecordPairs(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters,
			String leftTableName, String rightTableName, String leftOriginalIdFieldName,
			String rightOriginalIdFieldName, List<LeanRecordPair> pairs,
			boolean emOnly, FellegiSunterParameters fellegiSunterParameters) throws ApplicationException {
		BlockingService blockingService = getBlockingService(blockingServiceTypeName);
		blockingService.getRecordPairs(blockingServiceCustomParameters, matchingServiceTypeName,
				matchingServiceCustomParameters, leftTableName, rightTableName, leftOriginalIdFieldName,
				rightOriginalIdFieldName, pairs, emOnly, fellegiSunterParameters);
	}

	public List<LeanRecordPair> findCandidates(String blockingServiceTypeName, String leftTableName,
			String rightTableName, String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person) {
		BlockingService blockingService = getBlockingService(blockingServiceTypeName);
		return blockingService.findCandidates(leftTableName, rightTableName, leftOriginalIdFieldName,
				rightOriginalIdFieldName, person);
	}

	public void calculateBitStatistics(String blockingServiceTypeName, String matchingServiceTypeName,
			String leftTableName, String rightTableName, String leftOriginalIdFieldName,
			String rightOriginalIdFieldName) {
		BlockingService blockingService = getBlockingService(blockingServiceTypeName);
		blockingService.calculateBitStatistics(matchingServiceTypeName, leftTableName, rightTableName,
				leftOriginalIdFieldName, rightOriginalIdFieldName);
	}

}
