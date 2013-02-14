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

import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;

public class CBFScoringService extends ProbabilisticMatchingServiceBase
{
	protected boolean useBinaryScores()
	{
		return true;
	}

	public void calculateVectorFrequencies(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		// Doesn't make sense to do, there won't be EM
	}

	public void estimateMarginalProbabilities(FellegiSunterParameters fellegiSunterParams,
			MatchConfiguration matchConfig, int pairNumber)
	{
		// Doesn't make sense to do an EM in case of one field (CBF)
	}

	public void calculateRecordPairWeights(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		for (LeanRecordPair pair : pairs) {
			ComparisonVector vector = pair.getComparisonVector();
			pair.setWeight(vector.getScores()[0]);
		}
	}

	protected void calculateRecordPairWeight(LeanRecordPair pair, FellegiSunterParameters fellegiSunterParams,
											double wa[], double wd[])
	{
		ComparisonVector vector = pair.getComparisonVector();
		pair.setWeight(vector.getScores()[0]);
	}

	public void calculateMarginalProbabilities(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams,
			boolean writeStat)
	{
		// Doesn't make sense to do
	}

	public void calculateLowerBound(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		// TODO
		fellegiSunterParams.setLowerBound(0.4);
	}
		
	public void calculateUpperBound(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		// TODO
		fellegiSunterParams.setLowerBound(0.45);
	}

}
