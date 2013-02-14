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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BloomFilterBitStat implements Serializable
{
	private static final long serialVersionUID = 6542356047045466359L;

	private int compositeFieldIndex = 0;
	private int bloomFilterBitIndex = 0;
	private long bitTotal = 0L;
	private long bitMatchPairMatch = 0L;
	private long bitMatchPairUnmatch = 0L;
	private long bitUnmatchPairMatch = 0L;
	private long bitUnmatchPairUnmatch = 0L;
	private long bitOnePairMatch = 0L;
	private long bitOnePairUnmatch = 0L;
	private long bitZeroPairMatch = 0L;
	private long bitZeroPairUnmatch = 0L;

	public BloomFilterBitStat(int compositeFieldIndex, int bloomFilterBitIndex) {
		this.compositeFieldIndex = compositeFieldIndex;
		this.bloomFilterBitIndex = bloomFilterBitIndex;
		bitMatchPairMatch = 0L;
		bitMatchPairUnmatch = 0L;
		bitUnmatchPairMatch = 0L;
		bitUnmatchPairUnmatch = 0L;
		bitOnePairMatch = 0L;
		bitOnePairUnmatch = 0L;
		bitZeroPairMatch = 0L;
		bitZeroPairUnmatch = 0L;
	}

	public int getCompositeFieldIndex() {
		return compositeFieldIndex;
	}

	public void setCompositeFieldIndex(int compositeFieldIndex) {
		this.compositeFieldIndex = compositeFieldIndex;
	}

	public int getBloomFilterBitIndex() {
		return bloomFilterBitIndex;
	}

	public void setBloomFilterBitIndex(int bloomFilterBitIndex) {
		this.bloomFilterBitIndex = bloomFilterBitIndex;
	}

	public long getBitTotal() {
		return bitTotal;
	}

	public void setBitTotal(long bitTotal) {
		this.bitTotal = bitTotal;
	}

	public void incrementBitTotal() {
		bitTotal++;
	}

	public long getBitMatchPairMatch() {
		return bitMatchPairMatch;
	}

	public void setBitMatchPairMatch(long bitMatchPairMatch) {
		this.bitMatchPairMatch = bitMatchPairMatch;
	}

	public void incrementBitMatchPairMatch() {
		bitMatchPairMatch++;
	}

	public void incrementBitMatchPairMatch(int increment) {
		bitMatchPairMatch += increment;
	}

	public long getBitMatchPairUnmatch() {
		return bitMatchPairUnmatch;
	}

	public void setBitMatchPairUnmatch(long bitMatchPairUnmatch) {
		this.bitMatchPairUnmatch = bitMatchPairUnmatch;
	}

	public void incrementBitMatchPairUnmatch() {
		bitMatchPairUnmatch++;
	}

	public void incrementBitMatchPairUnmatch(int increment) {
		bitMatchPairUnmatch += increment;
	}

	public long getBitUnmatchPairMatch() {
		return bitUnmatchPairMatch;
	}

	public void setBitUnmatchPairMatch(long bitUnmatchPairMatch) {
		this.bitUnmatchPairMatch = bitUnmatchPairMatch;
	}

	public void incrementBitUnmatchPairMatch() {
		bitUnmatchPairMatch++;
	}

	public void incrementBitUnmatchPairMatch(int increment) {
		bitUnmatchPairMatch += increment;
	}

	public long getBitUnmatchPairUnmatch() {
		return bitUnmatchPairUnmatch;
	}

	public void setBitUnmatchPairUnmatch(long bitUnmatchPairUnmatch) {
		this.bitUnmatchPairUnmatch = bitUnmatchPairUnmatch;
	}

	public void incrementBitUnmatchPairUnmatch() {
		bitUnmatchPairUnmatch++;
	}

	public void incrementBitUnmatchPairUnmatch(int increment) {
		bitUnmatchPairUnmatch += increment;
	}

	public long getBitOnePairMatch() {
		return bitOnePairMatch;
	}

	public void setBitOnePairMatch(long bitOnePairMatch) {
		this.bitOnePairMatch = bitOnePairMatch;
	}

	public void incrementBitOnePairMatch() {
		bitOnePairMatch++;
	}

	public void incrementBitOnePairMatch(int increment) {
		bitOnePairMatch += increment;
	}

	public long getBitOnePairUnmatch() {
		return bitOnePairUnmatch;
	}

	public void setBitOnePairUnmatch(long bitOnePairUnmatch) {
		this.bitOnePairUnmatch = bitOnePairUnmatch;
	}

	public void incrementBitOnePairUnmatch() {
		bitOnePairUnmatch++;
	}

	public void incrementBitOnePairUnmatch(int increment) {
		bitOnePairUnmatch += increment;
	}

	public long getBitZeroPairMatch() {
		return bitZeroPairMatch;
	}

	public void setBitZeroPairMatch(long bitZeroPairMatch) {
		this.bitZeroPairMatch = bitZeroPairMatch;
	}

	public void incrementBitZeroPairMatch() {
		bitZeroPairMatch++;
	}

	public void incrementBitZeroPairMatch(int increment) {
		bitZeroPairMatch += increment;
	}

	public long getBitZeroPairUnmatch() {
		return bitZeroPairUnmatch;
	}

	public void setBitZeroPairUnmatch(long bitZeroPairUnmatch) {
		this.bitZeroPairUnmatch = bitZeroPairUnmatch;
	}

	public void incrementBitZeroPairUnmatch() {
		bitZeroPairUnmatch++;
	}

	public void incrementBitZeroPairUnmatch(int increment) {
		bitZeroPairUnmatch += increment;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BloomFilterBitStat))
			return false;
		BloomFilterBitStat castOther = (BloomFilterBitStat) other;
		return new EqualsBuilder()
			.append(getCompositeFieldIndex(), castOther.getCompositeFieldIndex())
			.append(getBloomFilterBitIndex(), castOther.getBloomFilterBitIndex())
			.append(getBitTotal(), castOther.getBitTotal())
			.append(getBitMatchPairMatch(), castOther.getBitMatchPairMatch())
			.append(getBitMatchPairUnmatch(), castOther.getBitMatchPairUnmatch())
			.append(getBitUnmatchPairMatch(), castOther.getBitUnmatchPairMatch())
			.append(getBitUnmatchPairUnmatch(), castOther.getBitUnmatchPairUnmatch())
			.append(getBitOnePairMatch(), castOther.getBitOnePairMatch())
			.append(getBitOnePairUnmatch(), castOther.getBitOnePairUnmatch())
			.append(getBitZeroPairMatch(), castOther.getBitZeroPairMatch())
			.append(getBitZeroPairUnmatch(), castOther.getBitZeroPairUnmatch())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCompositeFieldIndex())
			.append(getBloomFilterBitIndex())
			.append(getBitTotal())
			.append(getBitMatchPairMatch())
			.append(getBitMatchPairUnmatch())
			.append(getBitUnmatchPairMatch())
			.append(getBitUnmatchPairUnmatch())
			.append(getBitOnePairMatch())
			.append(getBitOnePairUnmatch())
			.append(getBitZeroPairMatch())
			.append(getBitZeroPairUnmatch())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("compositeFieldIndex", compositeFieldIndex)
			.append("bloomFilterBitIndex", bloomFilterBitIndex)
			.append("bitTotal", bitTotal)
			.append("bitMatchPairMatch", bitMatchPairMatch)
			.append("bitMatchPairUnmatch", bitMatchPairUnmatch)
			.append("bitUnmatchPairMatch", bitUnmatchPairMatch)
			.append("bitUnmatchPairUnmatch", bitUnmatchPairUnmatch)
			.append("bitOnePairMatch", bitOnePairMatch)
			.append("bitOnePairUnmatch", bitOnePairUnmatch)
			.append("bitZeroPairMatch", bitZeroPairMatch)
			.append("bitZeroPairUnmatch", bitZeroPairUnmatch)
			.toString();
	}

	public String toStringShort() {
		return "cfi " + compositeFieldIndex +
				", bfbi " + bloomFilterBitIndex +
				", bTot " + bitTotal +
				", bmpm " + bitMatchPairMatch +
				", bmpu " + bitMatchPairUnmatch +
				", bupm " + bitUnmatchPairMatch +
				", bupu " + bitUnmatchPairUnmatch +
				", b1pm " + bitOnePairMatch +
				", b1pu " + bitOnePairUnmatch +
				", b0pm " + bitZeroPairMatch +
				", b0pu " + bitZeroPairUnmatch;
	}

}
