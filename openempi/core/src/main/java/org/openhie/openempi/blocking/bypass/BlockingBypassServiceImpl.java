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
package org.openhie.openempi.blocking.bypass;

import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.AbstractBlockingServiceBase;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;

public class BlockingBypassServiceImpl extends AbstractBlockingServiceBase
{
	public void init() {
		log.trace("Initializing the Blocking Bypass Service");
	}

	public RecordPairSource getRecordPairSource(List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName) {
		return getRecordPairSource(leftTableName, rightTableName);
	}
	
	public RecordPairSource getRecordPairSource(String leftTableName, String rightTableName) {
		BypassRecordPairSource recordPairSource = new BypassRecordPairSource();
		recordPairSource.init(leftTableName, rightTableName);
		return recordPairSource;
	}
	
	public List<LeanRecordPair> findCandidates(String leftTableName, String rightTableName, Person person) {
		throw new UnsupportedOperationException("findCandidates is not implemented for BypassBlockingService");
	}

	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			List<LeanRecordPair> pairs, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) {
		PersonQueryService personQueryService = Context.getPersonQueryService();

		StringComparisonService comparisonService = Context.getStringComparisonService();
		MatchConfiguration matchConfiguration =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		List<String> leftMatchFieldNames = matchConfiguration.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
		List<String> rightMatchFieldNames = matchConfiguration.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);

		int pageSize = Constants.PAGE_SIZE;
		long pageStart = 0L;
		int numPersons = 0;
		do {
			List<Person> personList = personQueryService.getPersonsByExamplePaged(leftTableName, null, leftMatchFieldNames,
					pageStart, pageSize);
			numPersons = personList.size();
			if (numPersons > 0) {
				long otherPageStart = 0L;
				int otherNumPersons = 0;
				do {
					List<Person> personOtherList = personQueryService.getPersonsByExamplePaged(rightTableName,
							null, rightMatchFieldNames, otherPageStart, pageSize);
					otherNumPersons = personOtherList.size();
					if (otherNumPersons > 0) {
						for (Person person : personList) {
							for (Person personOther : personOtherList) {
								ComparisonVector comparisonVector =
										GeneralUtil.scoreRecordPair(person, personOther, comparisonService, matchFields);
								if (emOnly) {
									fellegiSunterParameters.incrementVectorFrequency(comparisonVector.getBinaryVectorValue());
								} else {
									LeanRecordPair recordPair = new LeanRecordPair(person.getPersonId(), personOther.getPersonId());
									recordPair.setComparisonVector(comparisonVector);
									pairs.add(recordPair);
								}
							}
						}
					}
					otherPageStart += pageSize;
				} while (otherNumPersons > 0);
			}
			pageStart += pageSize;
		} while (numPersons > 0);
	}

	public void calculateBitStatistics(String matchingServiceType, String leftTableName, String rightTableName) {
		throw new UnsupportedOperationException("CalculateBitStatistics is not implemented for BlockingBypssService");
	}

}
