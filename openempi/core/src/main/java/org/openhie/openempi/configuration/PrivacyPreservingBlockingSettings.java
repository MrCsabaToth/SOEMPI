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
package org.openhie.openempi.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class PrivacyPreservingBlockingSettings extends BaseObject
{
	private static final long serialVersionUID = -2880062692473274714L;

	private int numberOfBlockingBits;
	private int numberOfRuns;
	private List<PrivacyPreservingBlockingField> privacyPreservingBlockingFields;
	
	public PrivacyPreservingBlockingSettings() {
		privacyPreservingBlockingFields = new ArrayList<PrivacyPreservingBlockingField>();
	}

	public int getNumberOfBlockingBits() {
		return numberOfBlockingBits;
	}

	public void setNumberOfBlockingBits(int numberOfBlockingBits) {
		this.numberOfBlockingBits = numberOfBlockingBits;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public void addPrivacyPreservingBlockingField(PrivacyPreservingBlockingField privacyPreservingBlockingField) {
		privacyPreservingBlockingFields.add(privacyPreservingBlockingField);
	}

	public List<PrivacyPreservingBlockingField> getPrivacyPreservingBlockingFields() {
		return privacyPreservingBlockingFields;
	}

	public void setPrivacyPreservingBlockingFields(List<PrivacyPreservingBlockingField> privacyPreservingBlockingFields) {
		this.privacyPreservingBlockingFields = privacyPreservingBlockingFields;
	}

	public List<String> getPrivacyPreservingBlockingLeftFieldNames()
	{
		List<String> privacyPreservingBlockingFieldNames = new ArrayList<String>();
		for (PrivacyPreservingBlockingField privacyPreservingBlockingField : privacyPreservingBlockingFields)
		{
			privacyPreservingBlockingFieldNames.add(privacyPreservingBlockingField.getLeftFieldName());
		}
		return privacyPreservingBlockingFieldNames;
	}

	public List<String> getPrivacyPreservingBlockingRightFieldNames()
	{
		List<String> privacyPreservingBlockingFieldNames = new ArrayList<String>();
		for (PrivacyPreservingBlockingField privacyPreservingBlockingField : privacyPreservingBlockingFields)
		{
			privacyPreservingBlockingFieldNames.add(privacyPreservingBlockingField.getRightFieldName());
		}
		return privacyPreservingBlockingFieldNames;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("numberOfBlockingBits", numberOfBlockingBits).
				append("numberOfRuns", numberOfRuns).
				append("privacyPreservingBlockingFields", privacyPreservingBlockingFields).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PrivacyPreservingBlockingSettings))
			return false;
		PrivacyPreservingBlockingSettings castOther = (PrivacyPreservingBlockingSettings) other;
		return new EqualsBuilder().
				append(numberOfBlockingBits, castOther.numberOfBlockingBits).
				append(numberOfRuns, castOther.numberOfRuns).
				append(privacyPreservingBlockingFields, castOther.privacyPreservingBlockingFields).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(numberOfBlockingBits).
				append(numberOfRuns).
				append(privacyPreservingBlockingFields).
				toHashCode();
	}
}
