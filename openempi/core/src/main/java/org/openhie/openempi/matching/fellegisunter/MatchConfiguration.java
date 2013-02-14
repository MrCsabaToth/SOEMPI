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
import org.openhie.openempi.model.BaseObject;

public class MatchConfiguration extends BaseObject
{
	private static final long serialVersionUID = -4012644666481353904L;
	
	private double falseNegativeProbability;
	private double falsePositiveProbability;
	private EMSettings emSettings;
	private List<MatchField> matchFields;
	
	public MatchConfiguration() {
		matchFields = new ArrayList<MatchField>();
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
	}

	public List<MatchField> getMatchFields() {
		return matchFields;
	}

	public void setMatchFields(List<MatchField> matchFields) {
		this.matchFields = matchFields;
	}

	public List<String> getLeftFieldNames()
	{
		List<String> matchFieldNames = new ArrayList<String>();
		for (MatchField matchField : matchFields) {
			matchFieldNames.add(matchField.getLeftFieldName());
		}
		return matchFieldNames;
	}

	public List<String> getRightFieldNames()
	{
		List<String> matchFieldNames = new ArrayList<String>();
		for (MatchField matchField : matchFields) {
			matchFieldNames.add(matchField.getRightFieldName());
		}
		return matchFieldNames;
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
