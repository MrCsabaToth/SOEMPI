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
package org.openhie.openempi.matching.fellegisunter;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.util.PersonUtils;

public class ProbabilisticMatchingServiceTest extends BaseServiceTestCase
{
	public void testScorePairsFull() throws ApplicationException {
		scorePairs(false);
	}

	public void testScorePairsEMOnly() throws ApplicationException {
		scorePairs(true);
	}

	private void scorePairs(boolean emOnly) throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		PersonManagerService personManagerService = Context.getPersonManagerService();

		String leftDatasetName = "left_test_dataset";
		String rightDatasetName = "right_test_dataset";
		PersonUtils.createTestPersonTable(personManagerService, leftDatasetName, "", false, null, false, null, null);
		PersonUtils.createTestPersonTable(personManagerService, rightDatasetName, "", false, null, false, null, null);

		// Assemble a temporary MatchConfiguration for our purpose
		MatchConfiguration matchConfigurationBackup =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		MatchField firstOriginalMatchField = matchConfigurationBackup.getMatchFields(FieldQuerySelector.MatchOnlyFields).get(0);
		MatchConfiguration matchConfig = new MatchConfiguration();
		// Right now we inherit the values from Parameter Manager's config
		matchConfig.setFalsePositiveProbability(matchConfigurationBackup.getFalsePositiveProbability());
		matchConfig.setFalseNegativeProbability(matchConfigurationBackup.getFalseNegativeProbability());
		List<MatchField> matchFields = new ArrayList<MatchField>();
		matchFields.add(createMatchField(PersonUtils.GIVEN_NAME, PersonUtils.GIVEN_NAME, firstOriginalMatchField));
		matchFields.add(createMatchField(PersonUtils.FAMILY_NAME, PersonUtils.FAMILY_NAME, firstOriginalMatchField));
		matchFields.add(createMatchField(PersonUtils.CITY_NAME, PersonUtils.CITY_NAME, firstOriginalMatchField));
		matchConfig.setMatchFields(matchFields);

		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfig);

		String linkTableName = "test_links";
		ProbabilisticMatchingServiceBase matchingService = (ProbabilisticMatchingServiceBase)
				getApplicationContext().getBean("probabilisticMatchingService");
		List<LeanRecordPair> pairs = new ArrayList<LeanRecordPair>();
		matchingService.linkRecords(Constants.BLOCKING_BYPASS_SERVICE_NAME,
				null, Constants.PROBABILISTIC_MATCHING_SERVICE_WITH_BINARY_SCORES_NAME, null,
				linkTableName, leftDatasetName, rightDatasetName, pairs, ComponentType.DATA_INTEGRATOR_MODE, emOnly, true);
		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfigurationBackup);
	}

	private MatchField createMatchField(String leftFieldName, String rightFieldName, MatchField matchFieldReference) {
		MatchField mf = new MatchField();
		mf.setLeftFieldName(leftFieldName);
		mf.setRightFieldName(rightFieldName);
		// Right now we inherit the values from Parameter Manager's config
		mf.setAgreementProbability(matchFieldReference.getAgreementProbability());
		mf.setDisagreementProbability(matchFieldReference.getDisagreementProbability());
		mf.setMatchThreshold(matchFieldReference.getMatchThreshold());
		// How to decide which comparator function?
		FunctionField functionField = new FunctionField("JaroWinkler");
		mf.setComparatorFunction(functionField);
		return mf;
	}
}
