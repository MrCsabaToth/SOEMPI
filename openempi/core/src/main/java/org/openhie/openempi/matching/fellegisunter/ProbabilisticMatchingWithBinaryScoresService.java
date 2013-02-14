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

import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;

public class ProbabilisticMatchingWithBinaryScoresService extends ProbabilisticMatchingServiceBase
{
	protected boolean useBinaryScores()
	{
		return true;
	}

	protected void calculateRecordPairWeight(LeanRecordPair pair, FellegiSunterParameters fellegiSunterParams,
											double wa[], double wd[])
	{
		ComparisonVector vector = pair.getComparisonVector();
		double weight = 0.0;
		for (int i = 0; i < fellegiSunterParams.getFieldCount(); i++) {
			Double scorei = vector.getScores()[i];
			// If score is -1 (one of the fields were null or empty), then do not contribute to weight
			if (Math.abs(scorei + 1.0) >= 10e-5) {
				if (vector.getBinaryVector()[i] == 1) {
					weight += wa[i];
				} else {
					weight += wd[i];
				}
			}
		}
		pair.setWeight(weight);
	}

	public void estimateMarginalProbabilities(FellegiSunterParameters fellegiSunterParams,
			MatchConfiguration matchConfig, int pairNumber)
	{
		ExpectationMaximizationEstimator estimator = new ExpectationMaximizationEstimator();
		EMSettings ems = matchConfig.getEmSettings();
		estimator.estimateMarginalProbabilities(fellegiSunterParams, ems.getmInitial(),
				ems.getuInitial(), ems.getpInitial(), ems.getConvergenceError(),
				ems.getMaxIterations(), ems.getMaxTries());
	}
	
}
