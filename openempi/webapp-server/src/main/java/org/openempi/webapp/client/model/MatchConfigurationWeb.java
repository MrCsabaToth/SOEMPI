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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.Constants;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class MatchConfigurationWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 846769159971406453L;

	public static final String FALSE_NEGATIVE_PROBABILITY = "falseNegativeProbability";
	public static final String FALSE_POSITIVE_PROBABILITY = "falsePositiveProbability";
	public static final String EM_SETTINGS = "emSettings";
	public static final String MATCH_FIELDS = "matchFields";

	public EMSettingsWeb dummyEMSettings;	// This is need so EMSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public MatchFieldWeb dummyMatchField;	// This is need so MatchFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	
	public MatchConfigurationWeb() {
	}

	public Double getFalseNegativeProbability() {
		return get(FALSE_NEGATIVE_PROBABILITY);
	}

	public void setFalseNegativeProbability(Double falseNegativeProbability) {
		set(FALSE_NEGATIVE_PROBABILITY, falseNegativeProbability);
	}

	public Double getFalsePositiveProbability() {
		return get(FALSE_POSITIVE_PROBABILITY);
	}

	public void setFalsePositiveProbability(Double falsePositiveProbability) {
		set(FALSE_POSITIVE_PROBABILITY, falsePositiveProbability);
	}

	public EMSettingsWeb getEMSettings() {
		return get(EM_SETTINGS);
	}

	public void setEMSettings(EMSettingsWeb emSettings) {
		set(EM_SETTINGS, emSettings);
	}

	public List<org.openempi.webapp.client.model.MatchFieldWeb> getMatchFields() {
		return normalizeMatchFields();
	}

	public void setMatchFields(List<org.openempi.webapp.client.model.MatchFieldWeb> matchFields) {
		set(MATCH_FIELDS, normalizeMatchFields(matchFields));
	}

	public List<org.openempi.webapp.client.model.MatchFieldWeb> normalizeMatchFields() {
		List<org.openempi.webapp.client.model.MatchFieldWeb> matchFields = get(MATCH_FIELDS);
		return normalizeMatchFields(matchFields);
	}

	public List<org.openempi.webapp.client.model.MatchFieldWeb> normalizeMatchFields(List<org.openempi.webapp.client.model.MatchFieldWeb> matchFields) {
		// The goal is to sort the "NoComparisonJustTransfer" type field pairs to the end of the list.
		// This way we can discard them easier for all the blocking and matching operations.
		// These fields meant to be only transfer for research purposes as is, and shouldn't participate in the
		// actual record linkage procedure. (The OriginalId field is such a field too, but that is treated exceptionally.
		int size = matchFields.size();
		int end = size;
		List<org.openempi.webapp.client.model.MatchFieldWeb> noComparisonFields = new ArrayList<org.openempi.webapp.client.model.MatchFieldWeb>();
		for(int i = 0; i < end;) {
			if (matchFields.get(i).getComparatorFunction().getFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
				noComparisonFields.add(matchFields.get(i));
				matchFields.remove(i);
				end--;
			} else {
				i++;
			}
		}
		for(org.openempi.webapp.client.model.MatchFieldWeb mf : noComparisonFields)
			matchFields.add(mf);
		noComparisonFields.clear();
		return matchFields;
	}

}
