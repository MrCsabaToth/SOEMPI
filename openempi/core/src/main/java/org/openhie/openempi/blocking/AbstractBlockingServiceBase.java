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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public abstract class AbstractBlockingServiceBase extends BaseServiceImpl implements BlockingService
{
	protected final Log log = LogFactory.getLog(getClass());

	private Map<String, Object> configuration;

	private String name;
	private FieldTypeEnum inputType;
	
	public AbstractBlockingServiceBase() {
	}
	
	public AbstractBlockingServiceBase(String name) {
		this.name = name;
	}
	
	public void init(Map<String, Object> configParameters) {
		configuration = configParameters;
	}

	public Map<String, Object> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, Object> configuration) {
		this.configuration = configuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldTypeEnum getInputType() {
		return inputType;
	}

	public void setInputType(FieldTypeEnum inputType) {
		this.inputType = inputType;
	}

	public abstract RecordPairSource getRecordPairSource(String leftTableName, String rightTableName);
	
	public abstract RecordPairSource getRecordPairSource(List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName);

	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, List<LeanRecordPair> pairs,
			boolean emOnly, FellegiSunterParameters fellegiSunterParameters) throws ApplicationException
	{
		// Default to using the iterators
		RecordPairSource source = getRecordPairSource(leftTableName, rightTableName);
		RecordPairIterator iter = source.iterator(leftTableName, rightTableName,
				leftOriginalIdFieldName, rightOriginalIdFieldName, emOnly, fellegiSunterParameters);
		for (; iter.hasNext(); ) {
			try {
				if (!emOnly)
					pairs.add(iter.next());
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				// This shouldn't happen in theory. What to do?
				log.error("ERROR: blockingService pairs iterator couldn't return a value!");
			}
		}
	}

	public abstract List<LeanRecordPair> findCandidates(String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person);

	public abstract void calculateBitStatistics(String matchingServiceType, String leftTableName,
			String rightTableName, final String leftOriginalIdFieldName, final String rightOriginalIdFieldName);
}
