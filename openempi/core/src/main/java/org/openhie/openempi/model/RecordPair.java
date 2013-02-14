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
package org.openhie.openempi.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RecordPair
{
	private Record leftRecord;
	private Record rightRecord;
	private double weight;
	private ComparisonVector comparisonVector;
	
	public RecordPair(Record leftRecord, Record rightRecord) {
		this.leftRecord = leftRecord;
		this.rightRecord = rightRecord;
	}

	public Record getLeftRecord() {
		return leftRecord;
	}

	public void setLeftRecord(Record leftRecord) {
		this.leftRecord = leftRecord;
	}

	public Record getRightRecord() {
		return rightRecord;
	}

	public void setRightRecord(Record rightRecord) {
		this.rightRecord = rightRecord;
	}

	public Record getRecord(int index) {
		if (index == 0) {
			return leftRecord;
		}
		return rightRecord;
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
		if (!(other instanceof RecordPair))
			return false;
		RecordPair castOther = (RecordPair) other;
		return new EqualsBuilder()
			.append(leftRecord.getRecordId(), castOther.leftRecord.getRecordId())
			.append(rightRecord.getRecordId(), castOther.rightRecord.getRecordId())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(leftRecord.getRecordId())
			.append(rightRecord.getRecordId())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("leftRecord", leftRecord).append("rightRecord", rightRecord).append("weight", weight).toString();
	}
	
	
}
