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
package org.openhie.openempi.blocking.basicblocking;

import java.util.List;

import org.openhie.openempi.blocking.AbstractBlockingServiceBase;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.BaseFieldPair;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;

public class BlockingServiceImpl extends AbstractBlockingServiceBase
{
	private PersonDao personDao;
	private List<BlockingRound> blockingRounds;
	private boolean distinctBinsMode;	// true - inhibit check for existing gathered record pairs

	public void init() {
		log.trace("Initializing the Blocking Service");
	}
	
	public RecordPairSource getRecordPairSource(List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName) {
		BasicRecordPairSource recordPairSource = new BasicRecordPairSource(distinctBinsMode);
		recordPairSource.setBlockingDao(getPersonDao());
		recordPairSource.setBlockingRounds(blockingRounds);
		recordPairSource.init(leftTableName, rightTableName);
		return recordPairSource;
	}
	
	public RecordPairSource getRecordPairSource(String leftTableName, String rightTableName) {
		return getRecordPairSource(getBlockingRounds(), leftTableName, rightTableName);
	}
	
	/**
	 * Iterates over the list of blocking rounds that have been
	 * defined and accumulates patients that match the search criteria
	 * configured for the specific values present in the record
	 * provided.
	 * 
	 */
	public List<LeanRecordPair> findCandidates(String leftTableName, String rightTableName, Person person) {
		blockingRounds = getBlockingRounds();
		int count = 0;

		MatchConfiguration matchConfiguration =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<String> leftMatchFieldNames = matchConfiguration.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
		List<String> rightMatchFieldNames = matchConfiguration.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);

		List<Person> records = new java.util.ArrayList<Person>();
		for (BlockingRound round : blockingRounds) {
			List<BaseFieldPair> fields = round.getFields();
			Person example = new Person();
			for (BaseFieldPair field : fields) {
				example.setAttribute(field.getLeftFieldName(), person.getAttribute(field.getLeftFieldName()));
				log.debug("In round " + count + " added criterion: " + field.getLeftFieldName() + "=" + person.getAttribute(field.getLeftFieldName()));
			}
			records.addAll(personDao.blockRecords(leftTableName, example, leftMatchFieldNames));
			records.addAll(personDao.blockRecords(rightTableName, example, rightMatchFieldNames));
			count++;
		}
		List<LeanRecordPair> pairs = new java.util.ArrayList<LeanRecordPair>();
		StringComparisonService comparisonService = Context.getStringComparisonService();
		List<MatchField> matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		for (Person entry : records) {
			ComparisonVector comparisonVector =
					GeneralUtil.scoreRecordPair(person, entry, comparisonService, matchFields);
			LeanRecordPair pair = new LeanRecordPair(person.getPersonId(), entry.getPersonId());
			pair.setComparisonVector(comparisonVector);
			pairs.add(pair);
		}
		return pairs;
	}

	private List<BlockingRound> getBlockingRounds() {
		if (blockingRounds == null) {
			Configuration config = Context.getConfiguration();
			if (config != null) {
				BlockingSettings blockingSettings = (BlockingSettings)
					config.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY);
				blockingRounds = blockingSettings.getBlockingRounds();
			} else {
				log.debug("Connot load configuration because the Context.Configuration is null");
			}
		}
		return blockingRounds;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao blockingDao) {
		this.personDao = blockingDao;
	}

	public boolean isDistinctBinsMode() {
		return distinctBinsMode;
	}

	public void setDistinctBinsMode(boolean distinctBinsMode) {
		this.distinctBinsMode = distinctBinsMode;
	}

	public void calculateBitStatistics(String matchingServiceType, String leftTableName, String rightTableName) {
		throw new UnsupportedOperationException("CalculateBitStatistics is not implemented for BasicBlockingService");
	}

}
