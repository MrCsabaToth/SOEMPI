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

import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.util.PersonUtils;

public class MatchConfigurationTest extends BaseServiceTestCase
{
	// All
	public void testGetMatchFields_Mixed_WhenCalledWithAllFields_ReturnsAllMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.AllFields);
		assertEquals(6, matchFields.size());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getRightFieldName());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(3).getLeftFieldName());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(3).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(4).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(4).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(5).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(5).getRightFieldName());
	}
	
	public void testGetMatchFields_Mixed_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetMatchFields_Mixed_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.NoMatchFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetLeftFieldNames_Mixed_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.AllFields);
		assertEquals(6, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(3));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(4));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(5));
	}
	
	public void testGetLeftFieldNames_Mixed_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetLeftFieldNames_Mixed_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_Mixed_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.AllFields);
		assertEquals(6, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(3));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(4));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(5));
	}
	
	public void testGetRightFieldNames_Mixed_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_Mixed_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.AllFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	// Match Only
	public void testGetMatchFields_MatchOnly_WhenCalledWithAllFields_ReturnsAllMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.AllFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetMatchFields_MatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.GIVEN_NAME, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.FAMILY_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.CITY_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetMatchFields_MatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.NoMatchFields);
		assertEquals(0, matchFields.size());
	}
	
	public void testGetLeftFieldNames_MatchOnly_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.AllFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetLeftFieldNames_MatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetLeftFieldNames_MatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(0, fieldNames.size());
	}
	
	public void testGetRightFieldNames_MatchOnly_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.AllFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_MatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.GIVEN_NAME, fieldNames.get(0));
		assertEquals(PersonUtils.FAMILY_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.CITY_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_MatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.MatchOnlyFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(0, fieldNames.size());
	}
	
	// Non Match Only
	public void testGetMatchFields_NonMatchOnly_WhenCalledWithAllFields_ReturnsAllMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.AllFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetMatchFields_NonMatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		assertEquals(0, matchFields.size());
	}
	
	public void testGetMatchFields_NonMatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.NoMatchFields);
		assertEquals(3, matchFields.size());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getLeftFieldName());
		assertEquals(PersonUtils.ORIGINAL_ID, matchFields.get(0).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS1_NAME, matchFields.get(1).getRightFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getLeftFieldName());
		assertEquals(PersonUtils.ADDRESS2_NAME, matchFields.get(2).getRightFieldName());
	}
	
	public void testGetLeftFieldNames_NonMatchOnly_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.AllFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	public void testGetLeftFieldNames_NonMatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(0, fieldNames.size());
	}
	
	public void testGetLeftFieldNames_NonMatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getLeftFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_NonMatchOnly_WhenCalledWithNoMatchFields_ReturnsAllFieldNames() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.AllFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	public void testGetRightFieldNames_NonMatchOnly_WhenCalledWithMatchOnlyFields_ReturnsOnlyMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.MatchOnlyFields);
		assertEquals(0, fieldNames.size());
	}
	
	public void testGetRightFieldNames_NonMatchOnly_WhenCalledWithNonMatchFields_ReturnsOnlyNonMatchFields() {
		MatchConfiguration matchConfig = createTestMatchConfiguration(FieldQuerySelector.NoMatchFields);
		List<String> fieldNames = matchConfig.getRightFieldNames(FieldQuerySelector.NoMatchFields);
		assertEquals(3, fieldNames.size());
		assertEquals(PersonUtils.ORIGINAL_ID, fieldNames.get(0));
		assertEquals(PersonUtils.ADDRESS1_NAME, fieldNames.get(1));
		assertEquals(PersonUtils.ADDRESS2_NAME, fieldNames.get(2));
	}
	
	private MatchConfiguration createTestMatchConfiguration(FieldQuerySelector fieldQuerySelector) {
		// Assemble a MatchConfiguration with 3 match and 3 non-match fields for test purposes
		MatchConfiguration matchConfigurationBackup =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		MatchField firstOriginalMatchField = matchConfigurationBackup.getMatchFields(FieldQuerySelector.MatchOnlyFields).get(0);
		MatchConfiguration matchConfig = new MatchConfiguration();
		// Right now we inherit the values from Parameter Manager's config
		List<MatchField> matchFields = new ArrayList<MatchField>();
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.MatchOnlyFields)
			matchFields.add(createMatchField(PersonUtils.GIVEN_NAME, PersonUtils.GIVEN_NAME, firstOriginalMatchField, false));
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.NoMatchFields)
			matchFields.add(createMatchField(PersonUtils.ORIGINAL_ID, PersonUtils.ORIGINAL_ID, firstOriginalMatchField, true));
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.MatchOnlyFields)
			matchFields.add(createMatchField(PersonUtils.FAMILY_NAME, PersonUtils.FAMILY_NAME, firstOriginalMatchField, false));
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.NoMatchFields)
			matchFields.add(createMatchField(PersonUtils.ADDRESS1_NAME, PersonUtils.ADDRESS1_NAME, firstOriginalMatchField, true));
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.MatchOnlyFields)
			matchFields.add(createMatchField(PersonUtils.CITY_NAME, PersonUtils.CITY_NAME, firstOriginalMatchField, false));
		if (fieldQuerySelector == FieldQuerySelector.AllFields || fieldQuerySelector == FieldQuerySelector.NoMatchFields)
			matchFields.add(createMatchField(PersonUtils.ADDRESS2_NAME, PersonUtils.ADDRESS2_NAME, firstOriginalMatchField, true));
		matchConfig.setMatchFields(matchFields);
		return matchConfig;
	}

	private MatchField createMatchField(String leftFieldName, String rightFieldName, MatchField matchFieldReference, boolean noMatchField) {
		MatchField mf = new MatchField();
		mf.setLeftFieldName(leftFieldName);
		mf.setRightFieldName(rightFieldName);
		// Right now we inherit the values from Parameter Manager's config
		mf.setAgreementProbability(matchFieldReference.getAgreementProbability());
		mf.setDisagreementProbability(matchFieldReference.getDisagreementProbability());
		mf.setMatchThreshold(matchFieldReference.getMatchThreshold());
		// How to decide which comparator function?
		FunctionField functionField = new FunctionField(noMatchField ? Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME : "JaroWinkler");
		mf.setComparatorFunction(functionField);
		return mf;
	}
}
