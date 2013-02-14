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
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PrivacyPreservingBlockingSettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 6167574464369668270L;

	public static final String NUMBER_OF_BLOCKING_BITS = "numberOfBlockingBits";
	public static final String NUMBER_OF_RUNS = "numberOfRuns";
	public static final String PRIVACY_PRESERVING_BLOCKING_FIELDS = "privacyPreservingBlockingFields";

	public PrivacyPreservingBlockingFieldWeb dummyPrivacyPreservingBlockingField;	// This is need so PrivacyPreservingBlockingFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public PrivacyPreservingBlockingSettingsWeb() {
	}

	public Integer getNumberOfBlockingBits() {
		return get(NUMBER_OF_BLOCKING_BITS);
	}

	public void setNumberOfBlockingBits(Integer numberOfBlockingBits) {
		set(NUMBER_OF_BLOCKING_BITS, numberOfBlockingBits);
	}

	public Integer getNumberOfRuns() {
		return get(NUMBER_OF_RUNS);
	}

	public void setNumberOfRuns(Integer numberOfRuns) {
		set(NUMBER_OF_RUNS, numberOfRuns);
	}

	public List<PrivacyPreservingBlockingFieldWeb> getPrivacyPreservingBlockingFields() {
		return get(PRIVACY_PRESERVING_BLOCKING_FIELDS);
	}

	public void setPrivacyPreservingBlockingFields(List<PrivacyPreservingBlockingFieldWeb> privacyPreservingBlockingFields) {
		set(PRIVACY_PRESERVING_BLOCKING_FIELDS, privacyPreservingBlockingFields);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(NUMBER_OF_BLOCKING_BITS + ": ").append(getNumberOfBlockingBits());
		sb.append(NUMBER_OF_RUNS + ": ").append(getNumberOfRuns());
		sb.append("," + PRIVACY_PRESERVING_BLOCKING_FIELDS + ": ").append(getPrivacyPreservingBlockingFields().toString());
		return sb.toString();
	}
}
