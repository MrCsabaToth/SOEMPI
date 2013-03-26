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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.NamePairValuePair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;

public class BasicRecordPairIterator implements RecordPairIterator
{
	protected final Log log = LogFactory.getLog(getClass());

	private BasicRecordPairSource recordPairSource;
	private boolean initialized = false;
	private int lastNameValuePairsSet;
	private List<List<NamePairValuePair>> nameValuePairsList;
	private List<LeanRecordPair> recordPairs;
	private int lastRecordPair;
	private LeanRecordPair nextPair;
	private StringComparisonService comparisonService;
	private List<MatchField> matchFields;
	private List<String> leftMatchFieldNames;
	private List<String> rightMatchFieldNames;
	private String leftOriginalIdFieldName;
	private String rightOriginalIdFieldName;
	private Set<String> idPairs = new HashSet<String>(); // we don't need this for distinct blocks
	private String leftTableName;
	private String rightTableName;
	private boolean emOnly;
	private FellegiSunterParameters fellegiSunterParameters;
	private boolean distinctBinsMode;	// true - bins are distinct so no record pairs will enumerated more times, no need to check

	public BasicRecordPairIterator(BasicRecordPairSource recordPairSource, String leftTableName,
			String rightTableName, boolean distinctBinsMode, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) {
		this.recordPairSource = recordPairSource;
		this.leftTableName = leftTableName;
		this.rightTableName = rightTableName;
		this.distinctBinsMode = distinctBinsMode;
		this.emOnly = emOnly;
		this.fellegiSunterParameters = fellegiSunterParameters;
	}
	
	private synchronized void initialize() {
		comparisonService = Context.getStringComparisonService();
		idPairs.clear();

		nameValuePairsList = recordPairSource.getBlockingValueList();
		loadRecordPairList(nameValuePairsList.get(0));
		lastNameValuePairsSet = 1;
		lastRecordPair = 0;

		initialized = true;
	}

	public LeanRecordPair next() {
		return nextPair;
	}

	public boolean hasNext() {
		LeanRecordPair pair = null;
		if (!isInitialized()) {
			initialize();
		}
		if (lastRecordPair < recordPairs.size()) {
			pair = recordPairs.get(lastRecordPair);
			lastRecordPair++;
			nextPair = pair;			
			return true;
		}
		if (lastNameValuePairsSet < nameValuePairsList.size()) {
			do {
				List<NamePairValuePair> nameValuePairs = nameValuePairsList.get(lastNameValuePairsSet);
				log.debug("Loading pairs with blocking value of " + nameValuePairs);
				lastNameValuePairsSet++;
				loadRecordPairList(nameValuePairs);
				lastRecordPair = 1;
			} while (lastNameValuePairsSet < nameValuePairsList.size() && recordPairs.size() == 0);
			if (recordPairs == null || recordPairs.size() == 0) {
				return false;
			}
			nextPair = recordPairs.get(0);			
			return true;
		}
		return false;
	}

	private void loadRecordPairList(List<NamePairValuePair> pairs) {
		if (pairs.size() == 1 && pairs.get(0).getValue() == null)	// If we block only by one field and it is null, it probably doesn't make sense to generate record pairs for that
			return;	// (it'd bloat the size of record pairs pretty much, depending on the number of missing fields)
		if (matchFields == null) {
			MatchConfiguration matchConfiguration =
				(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
			PersonQueryService personQueryService = Context.getPersonQueryService();
			matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.MatchOnlyFields);
			leftMatchFieldNames = matchConfiguration.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
			leftOriginalIdFieldName = personQueryService.getDatasetOriginalIdFieldName(leftTableName);
			rightMatchFieldNames = matchConfiguration.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);
			rightOriginalIdFieldName = personQueryService.getDatasetOriginalIdFieldName(rightTableName);
		}
		Person leftExample = new Person();
		Person rightExample = new Person();
		getExamplePerson(pairs, leftExample, rightExample);
		List<Person> persons = recordPairSource.getBlockingDao().blockRecords(leftTableName, leftExample, leftMatchFieldNames);
		List<Person> personsOther = recordPairSource.getBlockingDao().blockRecords(rightTableName, rightExample, rightMatchFieldNames);
		recordPairs = new ArrayList<LeanRecordPair>();
		// If we don't find at least two records then there are no pairs to construct
		if (persons.size() < 1 || personsOther.size() < 1) {
			return;
		}
		log.debug("Found " + persons.size() + " - " + personsOther.size() + " person sets");
		int countTrueMatch = 0;
		for (int i = 0; i < persons.size(); i++) {
			for (int j = 0; j < personsOther.size(); j++) {
				log.debug("Building record pairs using indices " + i + " and " + j);
				Person p = persons.get(i);
				Person po = personsOther.get(j);
				String hashKey = null;
				if (!distinctBinsMode)
					hashKey = p.getPersonId() + "_" + po.getPersonId();
				if (distinctBinsMode || !idPairs.contains(hashKey)) {
					ComparisonVector comparisonVector = GeneralUtil.scoreRecordPair(p, po, comparisonService, matchFields);
					LeanRecordPair recordPair = null;
					if (emOnly) {
						fellegiSunterParameters.incrementVectorFrequency(comparisonVector.getBinaryVectorValue());
					} else {
						recordPair = new LeanRecordPair(p.getPersonId(), po.getPersonId());
						recordPair.setComparisonVector(comparisonVector);
					}
					recordPairs.add(recordPair);
					String leftOriginalId = p.getStringAttribute(leftOriginalIdFieldName);
					String rightOriginalId = p.getStringAttribute(rightOriginalIdFieldName);					
					if (leftOriginalId != null && rightOriginalId != null)
						if (leftOriginalId.equals(rightOriginalId))
							countTrueMatch++;
					if (!distinctBinsMode)
						idPairs.add(hashKey);
				}
			}
		}
		log.debug("Out of which " + countTrueMatch + " are true matches");
		if (recordPairs.size() == 0) {
			log.error("ERROR: loadRecordPairList couldn't find records during blocking, the iterator will crash!");
		}
	}

	private void getExamplePerson(List<NamePairValuePair> pairs, Person leftExample, Person rightExample) {
		log.debug("Constructing new criteria for name-value pair:");
		for (NamePairValuePair pair : pairs) {
			leftExample.setAttribute(pair.getLeftName(), pair.getValue());
			rightExample.setAttribute(pair.getRightName(), pair.getValue());
			log.debug("\t" + pair.getLeftName() + ", " + pair.getRightName() + "=" + pair.getValue());
		}
	}
	
	public void remove() {
		// This is an optional method of the interface and doesn't do
		// anything in this implementation. This is a read-only iterator.
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
