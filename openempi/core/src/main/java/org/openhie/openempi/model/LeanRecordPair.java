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
package org.openhie.openempi.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LeanRecordPair implements Serializable {

	private static final long serialVersionUID = -8184940687637304497L;

	private long leftRecordId;
	private long rightRecordId;
	private String leftOriginalRecordId;
	private String rightOriginalRecordId;
	private double weight;
	private ComparisonVector comparisonVector;
	
	public LeanRecordPair(long leftRecordId, String leftOriginalRecordId, long rightRecordId, String rightOriginalRecordId) {
		this.leftRecordId = leftRecordId;
		this.leftOriginalRecordId = leftOriginalRecordId;
		this.rightRecordId = rightRecordId;
		this.rightOriginalRecordId = rightOriginalRecordId;
	}

	public long getLeftRecordId() {
		return leftRecordId;
	}

	public void setLeftRecordId(long leftRecordId) {
		this.leftRecordId = leftRecordId;
	}

	public String getLeftOriginalRecordId() {
		return leftOriginalRecordId;
	}

	public void setLeftOriginalRecordId(String leftOriginalRecordId) {
		this.leftOriginalRecordId = leftOriginalRecordId;
	}
	
	public long getRightRecordId() {
		return rightRecordId;
	}

	public void setRightRecordId(long rightRecordId) {
		this.rightRecordId = rightRecordId;
	}

	public String getRightOriginalRecordId() {
		return rightOriginalRecordId;
	}

	public void setRightOriginalRecordId(String rightOriginalRecordId) {
		this.rightOriginalRecordId = rightOriginalRecordId;
	}

	public long getRecordId(int index) {
		if (index == 0) {
			return leftRecordId;
		}
		return rightRecordId;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public ComparisonVector getComparisonVector() {
		return comparisonVector;
	}

	public void setComparisonVector(ComparisonVector comparisonVector) {
		this.comparisonVector = comparisonVector;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof LeanRecordPair))
			return false;
		LeanRecordPair castOther = (LeanRecordPair) other;
		return new EqualsBuilder()
			.append(getLeftRecordId(), castOther.getLeftRecordId())
			.append(getRightRecordId(), castOther.getRightRecordId())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getLeftRecordId())
			.append(getRightRecordId())
			.append(getLeftOriginalRecordId())
			.append(getRightOriginalRecordId())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("leftRecordId", leftRecordId)
				.append("rightRecordId", rightRecordId)
				.append("leftOriginalRecordId", leftOriginalRecordId)
				.append("rightOriginalRecordId", rightOriginalRecordId)
				.append("weight", weight).toString();
	}

}
