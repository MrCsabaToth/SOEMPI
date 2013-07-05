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
package org.openhie.openempi.matching.fellegisunter;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.PersonMatchRequest;

public class BloomFilterParameterAdvice extends BaseObject
{
	private static final long serialVersionUID = 2038711740961564901L;

	private PersonMatchRequest personMatchReuqest;
	private Dataset leftDataset;
	private Dataset rightDataset;
	private List<ColumnMatchInformation> columnMatchInformation;
	private List<MatchPairStatHalf> matchPairStatHalves;
	private byte[] dhPublicKey;
	private Boolean leftOrRightSide;

	public BloomFilterParameterAdvice() {
	}

	public PersonMatchRequest getPersonMatchReuqest() {
		return personMatchReuqest;
	}

	public void setPersonMatchReuqest(PersonMatchRequest personMatchReuqest) {
		this.personMatchReuqest = personMatchReuqest;
	}

	public Dataset getLeftDataset() {
		return leftDataset;
	}

	public void setLeftDataset(Dataset leftDataset) {
		this.leftDataset = leftDataset;
	}

	public Dataset getRightDataset() {
		return rightDataset;
	}

	public void setRightDataset(Dataset rightDataset) {
		this.rightDataset = rightDataset;
	}

	public List<ColumnMatchInformation> getColumnMatchInformation() {
		return columnMatchInformation;
	}

	public void setColumnMatchInformation(List<ColumnMatchInformation> columnMatchInformation) {
		this.columnMatchInformation = columnMatchInformation;
	}

	public List<MatchPairStatHalf> getMatchPairStatHalves() {
		return matchPairStatHalves;
	}

	public void setMatchPairStatHalves(List<MatchPairStatHalf> matchPairStatHalves) {
		this.matchPairStatHalves = matchPairStatHalves;
	}

	public byte[] getDhPublicKey() {
		return dhPublicKey;
	}

	public void setDhPublicKey(byte[] dhPublicKey) {
		this.dhPublicKey = dhPublicKey;
	}

	public Boolean isLeftOrRightSide() {
		return leftOrRightSide;
	}

	public void setLeftOrRightSide(Boolean leftOrRightSide) {
		this.leftOrRightSide = leftOrRightSide;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BloomFilterParameterAdvice))
			return false;
		BloomFilterParameterAdvice castOther = (BloomFilterParameterAdvice) other;
		return new EqualsBuilder()
				.append(personMatchReuqest, castOther.personMatchReuqest)
				.append(leftDataset, castOther.leftDataset)
				.append(rightDataset, castOther.rightDataset)
				.append(columnMatchInformation, castOther.columnMatchInformation)
				.append(matchPairStatHalves, castOther.matchPairStatHalves)
				.append(dhPublicKey, castOther.dhPublicKey)
				.append(leftOrRightSide, castOther.leftOrRightSide)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(personMatchReuqest)
				.append(leftDataset)
				.append(rightDataset)
				.append(columnMatchInformation)
				.append(matchPairStatHalves)
				.append(dhPublicKey)
				.append(leftOrRightSide)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("personMatchReuqest", personMatchReuqest)
				.append("leftDataset", leftDataset)
				.append("rightDataset", rightDataset)
				.append("columnMatchInformation", columnMatchInformation)
				.append("matchPairStatHalves", matchPairStatHalves)
				.append("dhPublicKey", dhPublicKey)
				.append("leftOrRightSide", leftOrRightSide)
				.toString();
	}

}
