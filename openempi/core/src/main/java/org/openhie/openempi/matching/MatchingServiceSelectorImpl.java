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
package org.openhie.openempi.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.ValidationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public class MatchingServiceSelectorImpl extends BaseServiceImpl implements MatchingServiceSelector
{
	private HashMap<String,MatchingService> matchingServiceTypeMap;

	public MatchingServiceSelectorImpl() {
		super();
	}

	// Factory method pattern
	private MatchingService getMatchingService(String matchingServiceType) {
		MatchingService matchingService = matchingServiceTypeMap.get(matchingServiceType);
		if (matchingService == null) {
			log.error("Unknown matching service requested: " + matchingServiceType);
			throw new ValidationException("Unknown matching service requested for field matching: " + matchingServiceType);
		}
		return matchingService;
	}
	
	public MatchingServiceType getMatchingServiceType(String name) {
		MatchingService service = matchingServiceTypeMap.get(name);
		if (service == null) {
			return null;
		}
		return new MatchingServiceType(name, service);
	}

	public MatchingServiceType[] getMatchingServiceTypes() {
		MatchingServiceType[] list = new MatchingServiceType[matchingServiceTypeMap.keySet().size()];
		int index = 0;
		for (String key : matchingServiceTypeMap.keySet()) {
			list[index++] = new MatchingServiceType(key, matchingServiceTypeMap.get(key));
		}
		return list;
	}
	
	public HashMap<String, MatchingService> getMatchingServiceTypeMap() {
		return matchingServiceTypeMap;
	}

	public List<String> getMatchingServiceNames() {
		List<String> matchingServiceNames = new ArrayList<String>();
		for (String key : matchingServiceTypeMap.keySet()) {
			matchingServiceNames.add(key);
		}
		return matchingServiceNames;
	}
	
	public void setMatchingServiceTypeMap(HashMap<String, MatchingService> matchingServiceTypeMap) {
		this.matchingServiceTypeMap = matchingServiceTypeMap;
	}

	//
	public void init(String matchingServiceTypeName, Map<String, Object> configParameters) throws InitializationException
	{
		MatchingService matchingService = getMatchingService(matchingServiceTypeName);
		matchingService.init(configParameters);
	}
	
	public Set<LeanRecordPair> match(String matchingServiceTypeName, String blockingServiceTypeName,
			String leftTableName, String rightTableName, Person person) throws ApplicationException
	{
		MatchingService matchingService = getMatchingService(matchingServiceTypeName);
		return matchingService.match(blockingServiceTypeName, leftTableName, rightTableName, person);
	}
	
	public PersonMatch linkRecords(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters,
			String linkTableName, String leftTableName, String rightTableName, List<LeanRecordPair> pairs,
			ComponentType componentType, boolean emOnly, boolean persistLinks) throws ApplicationException
	{
		MatchingService matchingService = getMatchingService(matchingServiceTypeName);
		return matchingService.linkRecords(blockingServiceTypeName, blockingServiceCustomParameters,
				matchingServiceTypeName, matchingServiceCustomParameters,
				linkTableName, leftTableName, rightTableName, pairs,
				componentType, emOnly, persistLinks);
	}

}
