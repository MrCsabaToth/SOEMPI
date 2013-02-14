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
package org.openhie.openempi.recordlinkage.configuration;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class BloomfilterSettings extends BaseObject
{
	private static final long serialVersionUID = -8741114814923366131L;

	private int nGramSize;	// how many characters an n-gram consists: 2 = bigram, 3 = trigram, etc.
	private int defaultM;	// Default BloomFilter bit length
	private int defaultK;	// Default BloomFilter number of runs
	
	public BloomfilterSettings() {
	}

	/**
	 * Gets the n-gram size.<br />
	 * The string will be broken into n-grams, and bloom filter
	 * parts will be computed and put together for all of the n-grams<br />
	 * <br />
	 * @return n-gram size.
	 */
	public int getNGramSize() {
		return nGramSize;
	}

	/**
	 * Sets the n-gram size. The string will be broken into n-grams, and bloom filter
	 * parts will be computed and put together for all of the n-grams<br />
	 */
	public void setNGramSize(int nGramSize) {
		this.nGramSize = nGramSize;
	}

	/**
	 * Returns the default size (bits) of the Bloom filter.
	 *
	 * @return the default size of the bitset used by the Bloom filter.
	 */
	public int getDefaultM() {
		return defaultM;
	}

	/**
	 * Sets the default number of bits in the Bloom filter.
	 */
	public void setDefaultM(int defaultM) {
		this.defaultM = defaultM;
	}

	/**
	 * Returns the default value of K.<br />
	 * <br />
	 * K is the number of hash functions. Should be determined based on the size
	 * of the Bloom filter and the expected number of inserted elements.
	 *
	 * @return default K, default number of hash function runs.
	 */
	public int getDefaultK() {
		return defaultK;
	}

	/**
	 * Sets the default value of K.<br />
	 * <br />
	 * default K is the default number of hash functions performed during the composition of the final
	 * bitstream.
	 */
	public void setDefaultK(int defaultK) {
		this.defaultK = defaultK;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("nGramSize", nGramSize).
				append("defaultM", defaultM).
				append("defaultK", defaultK).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BloomfilterSettings))
			return false;
		BloomfilterSettings castOther = (BloomfilterSettings) other;
		return new EqualsBuilder().
				append(nGramSize, castOther.nGramSize).
				append(defaultM, castOther.defaultM).
				append(defaultK, castOther.defaultK).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
			append(nGramSize).
			append(defaultM).
			append(defaultK).
			toHashCode();
	}
}
