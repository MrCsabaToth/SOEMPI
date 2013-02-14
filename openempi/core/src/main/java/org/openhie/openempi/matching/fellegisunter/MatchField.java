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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.configuration.BaseFieldPair;
import org.openhie.openempi.configuration.FunctionField;

public class MatchField extends BaseFieldPair
{
	private static final long serialVersionUID = -4012644666481353904L;

	private double agreementProbability;
	private double disagreementProbability;
	private FunctionField comparatorFunction;
	private double matchThreshold;
	
	public double getAgreementProbability() {
		return agreementProbability;
	}

	public void setAgreementProbability(double agreementProbability) {
		this.agreementProbability = agreementProbability;
	}

	public double getDisagreementProbability() {
		return disagreementProbability;
	}

	public void setDisagreementProbability(double disagreementProbability) {
		this.disagreementProbability = disagreementProbability;
	}

	public FunctionField getComparatorFunction() {
		return comparatorFunction;
	}

	public void setComparatorFunction(FunctionField comparatorFunction) {
		this.comparatorFunction = comparatorFunction;
	}

	public double getMatchThreshold() {
		return matchThreshold;
	}

	public void setMatchThreshold(double matchThreshold) {
		this.matchThreshold = matchThreshold;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof MatchField))
			return false;
		MatchField castOther = (MatchField) other;
		return new EqualsBuilder()
				.append(leftFieldName, castOther.leftFieldName)
				.append(rightFieldName, castOther.rightFieldName)
				.append(agreementProbability, castOther.agreementProbability)
				.append(disagreementProbability, castOther.disagreementProbability)
				.append(comparatorFunction, castOther.comparatorFunction)
				.append(matchThreshold, castOther.matchThreshold)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(leftFieldName)
				.append(rightFieldName)
				.append(agreementProbability)
				.append(disagreementProbability)
				.append(comparatorFunction)
				.append(matchThreshold)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("leftFieldName", leftFieldName)
				.append("rightFieldName", rightFieldName)
				.append("agreementProbability", agreementProbability)
				.append("disagreementProbability", disagreementProbability)
				.append("comparatorFunction", comparatorFunction)
				.append("matchThreshold", matchThreshold)
				.toString();
	}

}
