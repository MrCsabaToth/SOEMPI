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

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PrivacyPreservingBlockingField extends BaseFieldPair
{
	private static final long serialVersionUID = 9022235257925286248L;

	private List<Integer> bits;
	private byte[] emptyBloomFilter;	// precomputation

	public PrivacyPreservingBlockingField() {
	}

	public List<Integer> getBits()
	{
		return bits;
	}
	
	public void setBits(List<Integer> bits)
	{
		this.bits = bits;
	}
	
	public byte[] getEmptyBloomFilter() {
		return emptyBloomFilter;
	}

	public void createEmptyBloomFilter(Integer bloomFilterSize) {
		int byteSize = bloomFilterSize / 8;
		emptyBloomFilter = new byte[byteSize];
		for(int i = 0; i < byteSize; i++) {
			emptyBloomFilter[i] = 0;
		}
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PrivacyPreservingBlockingField))
			return false;
		PrivacyPreservingBlockingField castOther = (PrivacyPreservingBlockingField) other;
		return new EqualsBuilder()
				.append(leftFieldName, castOther.leftFieldName)
				.append(rightFieldName, castOther.rightFieldName)
				.append(bits, castOther.bits)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(leftFieldName)
				.append(rightFieldName)
				.append(bits)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(super.toString())
				.append("bits", bits)
				.toString();
	}
}
