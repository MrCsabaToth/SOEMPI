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
package org.openhie.openempi.blocking;

import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;

public interface BlockingService
{
	public void init(Map<String, Object> configParameters);

	public String getName();

	public void setName(String name);

	public FieldTypeEnum getInputType();

	public void setInputType(FieldTypeEnum inputType);

	/**
	 * The getRecordPairSource returns an object that represents a collection of
	 * all the record pairs from the default data source of the system (the entire
	 * repository of records). The record pairs are retrieved from the system
	 * based on the configuration of the blocking algorithm so this data source
	 * should typically return a number of record pairs that is considerably
	 * smaller than the complete set of n(n-1) record pairs.
	 * 
	 * @return
	 */
	public RecordPairSource getRecordPairSource(String leftTableName, String rightTableName);
	
	/**
	 * The getRecordPairSource returns an object that represents a collection of
	 * all the record pairs from the default data source of the system (the entire
	 * repository of records). The record pairs are retrieved from the system
	 * based on the configuration of the blocking algorithm specified by
	 * the list of BlockingRound objects. Depending on the blocking algorithm
	 * this data source should typically return a number of record pairs that is
	 * considerably smaller than the complete set of n(n-1) record pairs.
	 * 
	 * @return
	 */
	public RecordPairSource getRecordPairSource(List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName);

	/**
	 * The getRecordPairs retrieves the record pairs from the system
	 * based on the configuration of the blocking algorithm
	 * (typically return a number of record pairs that is considerably
	 * smaller than the complete set of n(n-1) record pairs.)
	 * 
	 * @return
	 * @throws ApplicationException 
	 */
	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, List<LeanRecordPair> pairs, boolean emOnly,
			FellegiSunterParameters fellegiSunterParameters) throws ApplicationException;
	
	/**
	 * Given a particular person, the findCandidates method returns a list
	 * of records that given the current configuration of the blocking algorithm
	 * are potential matches to the person passed in.
	 * 
	 * @param record A record that should be used along with the blocking algorithm
	 * to determine similar records that are potential matches.
	 * 
	 * @return
	 */
	public List<LeanRecordPair> findCandidates(String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person);
	
	/**
	 * Calculate bit statistics which later can be used for
	 * privacy preserving blocking. The data is serialized out.
	 */
	public void calculateBitStatistics(String matchingServiceType, String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName);
}
