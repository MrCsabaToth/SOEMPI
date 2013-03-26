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
package org.openhie.openempi.matching.fellegisunter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.Constants;
import org.openhie.openempi.model.BaseObject;

public class MatchConfiguration extends BaseObject
{
	private static final long serialVersionUID = -4012644666481353904L;
	
	private double falseNegativeProbability;
	private double falsePositiveProbability;
	private EMSettings emSettings;
	private List<MatchField> matchFields;
	private int numberOfRealMatchFields;
	
	public enum FieldQuerySelector {
		AllFields,
		MatchOnlyFields,
		NoMatchFields
	}

	public MatchConfiguration() {
		matchFields = new ArrayList<MatchField>();
		numberOfRealMatchFields = 0;
	}
	
	public double getFalseNegativeProbability() {
		return falseNegativeProbability;
	}

	public void setFalseNegativeProbability(double falseNegativeProbability) {
		this.falseNegativeProbability = falseNegativeProbability;
	}

	public double getFalsePositiveProbability() {
		return falsePositiveProbability;
	}

	public void setFalsePositiveProbability(double falsePositiveProbability) {
		this.falsePositiveProbability = falsePositiveProbability;
	}

	public EMSettings getEmSettings() {
		return emSettings;
	}

	public void setEmSettings(EMSettings emSettings) {
		this.emSettings = emSettings;
	}

	public void addMatchField(MatchField matchField) {
		matchFields.add(matchField);
		normalizeMatchFields();
	}

	public List<MatchField> getMatchFields(FieldQuerySelector fieldQuerySelector) {
		if (fieldQuerySelector == FieldQuerySelector.MatchOnlyFields && numberOfRealMatchFields < matchFields.size()) {
			return matchFields.subList(0, numberOfRealMatchFields);
		} else if (fieldQuerySelector == FieldQuerySelector.NoMatchFields && numberOfRealMatchFields > 0) {
			return matchFields.subList(numberOfRealMatchFields, matchFields.size());
		}
		return matchFields;
	}

	public void setMatchFields(List<MatchField> matchFields) {
		this.matchFields = matchFields;
		normalizeMatchFields();
	}

	public List<String> getLeftFieldNames(FieldQuerySelector fieldQuerySelector)
	{
		List<String> matchFieldNames = new ArrayList<String>();
		for (MatchField matchField : getMatchFields(fieldQuerySelector)) {
			matchFieldNames.add(matchField.getLeftFieldName());
		}
		return matchFieldNames;
	}

	public List<String> getRightFieldNames(FieldQuerySelector fieldQuerySelector)
	{
		List<String> matchFieldNames = new ArrayList<String>();
		for (MatchField matchField : getMatchFields(fieldQuerySelector)) {
			matchFieldNames.add(matchField.getRightFieldName());
		}
		return matchFieldNames;
	}
	
	public void normalizeMatchFields()
	{
		// The goal is to sort the "NoComparisonJustTransfer" type field pairs to the end of the list.
		// This way we can discard them easier for all the blocking and matching operations.
		// These fields meant to be only transfer for research purposes as is, and shouldn't participate in the
		// actual record linkage procedure. (The OriginalId field is such a field too, but that is treated exceptionally.
		int size = matchFields.size();
		int end = size;
		List<MatchField> noComparisonFields = new ArrayList<MatchField>();
		for(int i = 0; i < end;) {
			if (matchFields.get(i).getComparatorFunction().getFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
				noComparisonFields.add(matchFields.get(i));
				matchFields.remove(i);
				end--;
			} else {
				i++;
			}
		}
		matchFields.addAll(noComparisonFields);
		numberOfRealMatchFields = end;
		noComparisonFields.clear();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("falseNegativeProbability", falseNegativeProbability).
				append("falsePositiveProbability", falsePositiveProbability).
				append("emSettings", emSettings).
				append("matchFields", matchFields).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MatchConfiguration))
			return false;
		MatchConfiguration castOther = (MatchConfiguration) other;
		return new EqualsBuilder().
				append(falseNegativeProbability, castOther.falseNegativeProbability).
				append(falsePositiveProbability, castOther.falsePositiveProbability).
				append(emSettings, castOther.emSettings).
				append(matchFields, castOther.matchFields).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(falseNegativeProbability).
				append(falsePositiveProbability).
				append(emSettings).
				append(matchFields).
				toHashCode();
	}
}
