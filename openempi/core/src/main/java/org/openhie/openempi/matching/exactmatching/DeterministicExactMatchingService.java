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
package org.openhie.openempi.matching.exactmatching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.AbstractMatchingService;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.stringcomparison.StringComparisonService;

public class DeterministicExactMatchingService extends AbstractMatchingService
{
	private List<MatchField> matchFields;
	private StringComparisonService comparisonService;
	
	public void init(Map<String, Object> configParameters) throws InitializationException {
		log.debug("Matching service " + getClass().getName() + " init");
		init();
	}

	public void init() throws InitializationException {
		Object obj = null;
		if (Context.getConfiguration() != null)
			obj = Context.getConfiguration().lookupConfigurationEntry(ExactMatchingConstants.EXACT_MATCHING_FIELDS_REGISTRY_KEY);
		if (obj == null) {
			log.error("Deterministic exact matching service has not been configured properly; no match fields have been defined.");
			return;
			// TODO throw new RuntimeException("Deterministic exact maching service has not been configured properly.");
		}
		@SuppressWarnings("unchecked")
		List<MatchField> matchFields = (List<MatchField>) obj;
		for (MatchField field : matchFields) {
			log.debug("Matching service " + getClass().getName() + " will perform matching using " + field);
			if (field.getComparatorFunction() != null && comparisonService == null) {
				comparisonService = Context.getStringComparisonService();
			}
		}
		this.matchFields = matchFields;
	}

	public PersonMatch linkRecords(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters,
			String linkTableName, String leftTableName, String rightTableName, String leftOriginalIdFieldName,
			String rightOriginalIdFieldName, List<LeanRecordPair> pairs,
			ComponentType componentType, boolean emOnly, boolean persistLinks) {
		throw new UnsupportedOperationException("linkRecords is not implemented for DeterministicExactMatchingService");
	}

	public Set<LeanRecordPair> match(String blockingServiceTypeName, String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person) {
		log.debug("Looking for matches on person " + person);
		List<LeanRecordPair> candidates = Context.getBlockingServiceSelector().findCandidates(blockingServiceTypeName,
				leftTableName, rightTableName, leftOriginalIdFieldName, rightOriginalIdFieldName, person);
		//List<LeanRecordPair> candidates = Context.getBlockingService().findCandidates(person);
		Set<LeanRecordPair> matches = new java.util.HashSet<LeanRecordPair>();
		for (LeanRecordPair entry : candidates) {
			log.debug("Potential matching record pair found: " + entry);
			boolean overallMatch = isExactMatch(entry);
			
			// If the two records match but they don't refer to the same exact record then these records
			// should be linked together
			if (overallMatch && entry.getRightRecordId() != person.getPersonId()) {
				log.debug("Adding to matches entry: " + entry);
				entry.setWeight(1.0);
				matches.add(entry);
			}
		}
		return matches;
	}

	private boolean isExactMatch(LeanRecordPair recordPair) {
		ComparisonVector compVec = recordPair.getComparisonVector();
		double[] scores = compVec.getScores();
		// Note: for this to work every comparison function should be 'ExactMatch' in the config file
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] < 0.99999999)
			return false;
		}
		return true;
	}

	public List<MatchField> getMatchFields() {
		return matchFields;
	}

	public void setMatchFields(List<MatchField> matchFields) {
		this.matchFields = matchFields;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchFields", matchFields).toString();
	}
}
