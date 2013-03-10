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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;

public class BypassRecordPairIterator implements RecordPairIterator
{
	protected final Log log = LogFactory.getLog(getClass());

	private boolean initialized = false;

	private PersonQueryService personQueryService = null;
	private List<Person> personList = null;
	private Iterator<Person> personIterator = null;
	private Person actualPerson = null;
	private List<Person> personOtherList = null;
	private Iterator<Person> personOtherIterator = null;
	private StringComparisonService comparisonService = null;
	private List<MatchField> matchFields = null;
	private List<String> leftMatchFieldNames = null;
	private List<String> rightMatchFieldNames = null;
	private String leftTableName;
	private String rightTableName;
	private String leftOriginalIdFieldName;
	private String rightOriginalIdFieldName;
	private boolean emOnly;
	private FellegiSunterParameters fellegiSunterParameters;

	private Long pageStart = 0L;
	private Long otherPageStart = 0L;

	public BypassRecordPairIterator(BypassRecordPairSource recordPairSource, String leftTableName,
			String rightTableName, String leftOriginalIdFieldName, String rightOriginalIdFieldName, boolean emOnly,
			FellegiSunterParameters fellegiSunterParameters) {
		initialize();
		this.leftTableName = leftTableName;
		this.rightTableName = rightTableName;
		this.leftOriginalIdFieldName = leftOriginalIdFieldName;
		this.rightOriginalIdFieldName = rightOriginalIdFieldName;
		this.emOnly = emOnly;
		this.fellegiSunterParameters = fellegiSunterParameters;
	}
	
	private synchronized void initialize() {
		if (personQueryService == null)
			personQueryService = Context.getPersonQueryService();
		personList = null;
		personIterator = null;
		personOtherList = null;
		personOtherIterator = null;
		if (comparisonService == null)
			comparisonService = Context.getStringComparisonService();
		pageStart = 0L;
		otherPageStart = 0L;
		initialized = true;
	}

	public LeanRecordPair next() {
		Person personOther = personOtherIterator.next();
		ComparisonVector comparisonVector =
				GeneralUtil.scoreRecordPair(actualPerson, personOther, comparisonService, matchFields);
		LeanRecordPair recordPair = null;
		if (emOnly) {
			fellegiSunterParameters.incrementVectorFrequency(comparisonVector.getBinaryVectorValue());
		} else {
			recordPair = new LeanRecordPair(actualPerson.getPersonId(),
											leftOriginalIdFieldName,
											personOther.getPersonId(),
											rightOriginalIdFieldName);
			recordPair.setComparisonVector(comparisonVector);
		}		
		return recordPair;
	}

	public boolean hasNext() {
		return hasNext(false);
	}

	private boolean hasNext(boolean recursiveCall) {
		if (matchFields == null) {
			MatchConfiguration matchConfiguration =
				(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
			matchFields = matchConfiguration.getMatchFields(false);
			leftMatchFieldNames = matchConfiguration.getLeftFieldNames(false);
			rightMatchFieldNames = matchConfiguration.getRightFieldNames(false);
			if (leftOriginalIdFieldName != null)
				leftMatchFieldNames.add(leftOriginalIdFieldName);
			if (rightOriginalIdFieldName != null)
				rightMatchFieldNames.add(rightOriginalIdFieldName);
		}

		if (!isInitialized()) {
			initialize();
		}
		if (personIterator == null || personOtherIterator == null) {
			int personListSize = loadNextPersonList(personList, pageStart, personIterator, leftTableName,
					leftMatchFieldNames);
			if (personListSize <= 0)
				return false;
			if (!personIterator.hasNext())
				return false;
			actualPerson = personIterator.next();
			int personOtherListSize = loadNextPersonList(personOtherList, otherPageStart, personOtherIterator,
					rightTableName, rightMatchFieldNames);
			if (personOtherListSize <= 0)
				return false;
			if (!personOtherIterator.hasNext())
				return false;
			return true;
		} else {
			if (personOtherIterator.hasNext()) {
				return true;
			} else {
				int personOtherListSize = loadNextPersonList(personOtherList, otherPageStart, personOtherIterator,
						rightTableName, rightMatchFieldNames);
				boolean needNewPerson = false;
				if (personOtherListSize <= 0)
					needNewPerson = true;
				if (personOtherIterator.hasNext())
					return true;
				else
					needNewPerson = true;
				if (needNewPerson && !recursiveCall) {
					if (!personIterator.hasNext()) {
						int personListSize = loadNextPersonList(personList, pageStart, personIterator,
								leftTableName, leftMatchFieldNames);
						if (personListSize <= 0)
							return false;
						if (!personIterator.hasNext())
							return false;
					}
					actualPerson = personIterator.next();
					otherPageStart = 0L;
					return hasNext(true);
				}
			}
		}
		return false;
	}

	private int loadNextPersonList(List<Person> personListParam, Long pageStartParam, Iterator<Person> personIteratorParam,
			String tableName, List<String> matchFieldNames) {
		personListParam = personQueryService.getPersonsByExamplePaged(tableName, null, matchFieldNames,
				pageStartParam, Constants.PAGE_SIZE);
		pageStartParam += Constants.PAGE_SIZE;
		personIteratorParam = personListParam.iterator();
		return personListParam.size();
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
