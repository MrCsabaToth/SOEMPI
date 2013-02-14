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
package org.openhie.openempi.blocking.privacypreserving;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BloomFilterStatistics implements Serializable
{
	private static final long serialVersionUID = 6542356047045466359L;

	private int numberOfPairsToSample;
	private List<BloomFilterBitStat> bloomFilterBitStats;

	BloomFilterStatistics(int numberOfPairsToSample, List<BloomFilterBitStat> bloomFilterBitStats) {
		this.setNumberOfPairsToSample(numberOfPairsToSample);
		this.setBloomFilterBitStats(bloomFilterBitStats);
	}

	public int getNumberOfPairsToSample() {
		return numberOfPairsToSample;
	}

	public void setNumberOfPairsToSample(int numberOfPairsToSample) {
		this.numberOfPairsToSample = numberOfPairsToSample;
	}

	public List<BloomFilterBitStat> getBloomFilterBitStats() {
		return bloomFilterBitStats;
	}

	public void setBloomFilterBitStats(List<BloomFilterBitStat> bloomFilterBitStats) {
		this.bloomFilterBitStats = bloomFilterBitStats;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BloomFilterStatistics))
			return false;
		BloomFilterStatistics castOther = (BloomFilterStatistics) other;
		return new EqualsBuilder()
			.append(getNumberOfPairsToSample(), castOther.getNumberOfPairsToSample())
			.append(getBloomFilterBitStats(), castOther.getBloomFilterBitStats())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getNumberOfPairsToSample())
			.append(getBloomFilterBitStats())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("numberOfPairsToSample", numberOfPairsToSample)
			.append("bloomFilterBitStats", bloomFilterBitStats)
			.toString();
	}
	
}
