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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PairBucketData implements Serializable
{
	private static final long serialVersionUID = 5321159661730020582L;

	private List<Long> ids = new ArrayList<Long>();
	private int trueMatchCounter = 0;

	PairBucketData() {
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void addId(Long id) {
		ids.add(id);
	}

	public Integer getTrueMatchCounter() {
		return trueMatchCounter;
	}

	public void setTrueMatchCounter(Integer trueMatchCounter) {
		this.trueMatchCounter = trueMatchCounter;
	}

	public void incrementTrueMatchCounter() {
		trueMatchCounter++;
	}

	public void clearTrueMatchCounter() {
		trueMatchCounter = 0;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PairBucketData))
			return false;
		PairBucketData castOther = (PairBucketData) other;
		return new EqualsBuilder()
			.append(getIds(), castOther.getIds())
			.append(getTrueMatchCounter(), castOther.getTrueMatchCounter())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getIds())
			.append(getTrueMatchCounter())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("ids", ids)
			.append("trueMatchCounter", trueMatchCounter)
			.toString();
	}
	
}
