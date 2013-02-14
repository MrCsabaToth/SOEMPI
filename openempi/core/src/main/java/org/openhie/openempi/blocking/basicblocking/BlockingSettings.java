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
package org.openhie.openempi.blocking.basicblocking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class BlockingSettings extends BaseObject
{
	private static final long serialVersionUID = 849868111225861516L;

	private int numberOfRecordsToSample;
	private List<BlockingRound> blockingRounds;
	
	public BlockingSettings() {
		setBlockingRounds(new ArrayList<BlockingRound>());
	}

	public int getNumberOfRecordsToSample() {
		return numberOfRecordsToSample;
	}

	public void setNumberOfRecordsToSample(int numberOfRecordsToSample) {
		this.numberOfRecordsToSample = numberOfRecordsToSample;
	}

	public List<BlockingRound> getBlockingRounds() {
		return blockingRounds;
	}

	public void setBlockingRounds(List<BlockingRound> blockingRounds) {
		this.blockingRounds = blockingRounds;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("numberOfRecordsToSample", numberOfRecordsToSample).
				append("blockingRounds", blockingRounds).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BlockingSettings))
			return false;
		BlockingSettings castOther = (BlockingSettings) other;
		return new EqualsBuilder().
				append(numberOfRecordsToSample, castOther.numberOfRecordsToSample).
				append(blockingRounds, castOther.blockingRounds).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(numberOfRecordsToSample).
				append(blockingRounds).
				toHashCode();
	}
}
