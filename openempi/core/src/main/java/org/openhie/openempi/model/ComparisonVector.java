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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.matching.fellegisunter.MatchField;

public class ComparisonVector extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 7534673028743277151L;
	protected final Log log = LogFactory.getLog(getClass());
	
	private List<MatchField> matchFields;
	private double[] scores;
	private int[] binaryScores;
	private double vectorProbGivenM;
	private double vectorProbGivenU;
	
	public ComparisonVector(List<MatchField> matchFields) {
		this.matchFields = matchFields;
		scores = new double[matchFields.size()];
		binaryScores = new int[matchFields.size()];
	}
	
	public ComparisonVector(MatchField[] matchFieldsArray) {
		matchFields = Arrays.asList(matchFieldsArray);
		scores = new double[matchFields.size()];
		binaryScores = new int[matchFields.size()];
	}

	public int[] getBinaryVector() {
		return binaryScores;
	}
	
	public void setScore(int index, double value/*, double[] levelThresholds, double[] levelValues*/) {
		if (index < 0 || index > scores.length-1) {
			throw new IndexOutOfBoundsException("Index value of " + index + " outside valid range of 0 - " + (scores.length-1));
		}
		scores[index] = value;
		if (value >= matchFields.get(index).getMatchThreshold()) {
			binaryScores[index] = 1;
		} else if (value >= 0.0) {
			binaryScores[index] = 0;
		} else {	// -1 -> null values
			binaryScores[index] = 0;	// TODO
		}
	}
	
	public int getBinaryVectorValue() {
		int value = 0;
		for (int i=0; i < binaryScores.length; i++) {
			if (binaryScores[i] == 1) {
				value |= (1<<i);
			}
		}
		return value;
	}
	
	public double[] getScores() {
		return scores;
	}

	public void setScores(double[] scores) {
		this.scores = scores;
	}

	public int getLength() {
		return matchFields.size();
	}
	
	public String getBinaryVectorString() {
		StringBuffer sb = new StringBuffer("[ ");
		for (int i = 0; i < binaryScores.length; i++) {
//			sb.append(((binaryScores[i] == 1) ? "1" : "0"));
			sb.append(Integer.toString(binaryScores[i]));
			sb.append((i < binaryScores.length - 1) ? ", " : " ");
		}
		sb.append("]");
		return sb.toString();
	}

	public String getScoreVectorString() {
		StringBuffer sb = new StringBuffer("[ ");
		for (int i = 0; i < scores.length; i++) {
			sb.append(scores[i]);
			sb.append((i < scores.length-1) ? ", " : " ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ComparisonVector))
			return false;
		ComparisonVector castOther = (ComparisonVector) other;
		return new EqualsBuilder().append(scores, castOther.scores).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(scores).toHashCode();
	}

	public double getVectorProbGivenM() {
		return vectorProbGivenM;
	}

	public void calculateProbabilityGivenMatch(double[] estimatedMarginals, boolean useBinaryScores) {
		vectorProbGivenM = calculateVectorProbability(estimatedMarginals, useBinaryScores);
	}

	private double calculateVectorProbability(double[] estimatedMarginals, boolean useBinaryScores) {
		if (estimatedMarginals.length == 0 || estimatedMarginals.length != binaryScores.length) {
			log.error("Unable to calculate vector marginal probability since length of estimated marginals is less than vector length:\n" + this.toString());
			throw new RuntimeException("Unable to calculate vector marginal probabilities.");
		}
		double product = 1.0;
		for (int i=0; i < binaryScores.length; i++) {
			if (scores[i] >= 0.0) { // Don't count if the score is -1: one of the fields are null
				if (useBinaryScores) {
					if (binaryScores[i] == 0) {
						product *= (1.0 - estimatedMarginals[i]);
					} else if (binaryScores[i] == 1) {
						product *= estimatedMarginals[i];
					} else {
						// It can be -1, if one of the fields are null, but we filtered for that
					}
				} else {
					double est = estimatedMarginals[i];
					double si = scores[i];
//					Double si = quantizedScores[i];
					double prob = 0.0;
//					prob = (Math.pow(est, si) * Math.pow(1.0 - est, 1.0 - si));
					prob = 2.0 * est * si - si - est + 1.0;
					product *= prob;
				}
			}
		}
		return product;
	}
	
	public void setVectorProbGivenM(double vectorProbGivenM) {
		this.vectorProbGivenM = vectorProbGivenM;
	}

	public void calculateProbabilityGivenNonmatch(double[] estimatedMarginals, boolean useBinaryScores) {
		vectorProbGivenU = calculateVectorProbability(estimatedMarginals, useBinaryScores);
	}
	
	public double getVectorProbGivenU() {
		return vectorProbGivenU;
	}

	public void setVectorProbGivenU(double vectorProbGivenU) {
		this.vectorProbGivenU = vectorProbGivenU;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("matchFields", matchFields).
				append("scores", scores).
				append("binaryScores", binaryScores).
				append("vectorProbGivenM", vectorProbGivenM).
				append("vectorProbGivenU", vectorProbGivenU).toString();
	}

}
