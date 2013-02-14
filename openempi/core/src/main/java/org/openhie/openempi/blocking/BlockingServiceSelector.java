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

import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;

public interface BlockingServiceSelector
{
	public BlockingServiceType[] getBlockingServiceTypes();
	
	public List<String> getBlockingServiceNames();
	
	public BlockingServiceType getBlockingServiceType(String name);
	
	public void init(String blockingServiceTypeName, Map<String, Object> configParameters);
	
	public RecordPairSource getRecordPairSource(String blockingServiceTypeName,
			String leftTableName, String rightTableName);
	
	public RecordPairSource getRecordPairSource(String blockingServiceTypeName, List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName);
	
	public void getRecordPairs(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters, String leftTableName,
			String rightTableName, String leftOriginalIdFieldName, String rightOriginalIdFieldName,
			List<LeanRecordPair> pairs, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) throws ApplicationException;

	public List<LeanRecordPair> findCandidates(String blockingServiceTypeName, String leftTableName,
			String rightTableName, String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person);

	public void calculateBitStatistics(String blockingServiceTypeName, String matchingServiceTypeName,
			String leftTableName, String rightTableName, String leftOriginalIdFieldName,
			String rightOriginalIdFieldName);
}
