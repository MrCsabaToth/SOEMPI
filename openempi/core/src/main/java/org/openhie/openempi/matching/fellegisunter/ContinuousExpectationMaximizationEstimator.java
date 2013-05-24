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
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.model.LeanRecordPair;

public class ContinuousExpectationMaximizationEstimator
{
	protected Log log = LogFactory.getLog(ContinuousExpectationMaximizationEstimator.class);

	private double[] mOfI;
	private double[] uOfI;
	private double[] gOfM;
	private double[] gOfU;
	private double p;
	private int gamma[][];

	public synchronized void estimateMarginalProbabilities(FellegiSunterParameters params,
			MatchConfiguration matchConfig, List<LeanRecordPair> pairs, int pairNumber)
	{
		EMSettings ems = matchConfig.getEmSettings();
		initializeAlgorithm(params, matchConfig, pairs, ems.getmInitial(), ems.getuInitial(),
				ems.getpInitial(), pairNumber);
		
		double error = 1.0;
		int iteration = 1;
		do {
			// Expectation Step
			estimateGammaOfMnU(pairNumber);
			
			// Maximization Step
			estimateMnUOfI(params.getFieldCount(), pairNumber);
			double pPrevious = p;
			estimateProbability(pairNumber);
			error = Math.abs(pPrevious - p);
			log.trace("Error at iteration " + iteration + " is " + error);			
			iteration++;
		} while (error > ems.getConvergenceError() && iteration < ems.getMaxIterations());
		log.trace("EM converged after " + iteration + " iterations");

		params.setMValues(mOfI);
		params.setUValues(uOfI);
	}

	private void estimateGammaOfMnU(int pairCount) {
		for (int j = 0; j < pairCount; j++) {
			double mProduct = p * calculateProduct(j, mOfI);
			double uProduct = (1.0 - p) * calculateProduct(j, uOfI);
			double denominator = mProduct + uProduct;
			gOfM[j] = mProduct / denominator;
			gOfU[j] = uProduct / denominator;
		}
	}

	private void estimateMnUOfI(int fieldCount, int pairCount) {
		for (int i = 0; i < fieldCount; i++) {
			double numeratorSumMOfI = 0.0;
			double numeratorSumUOfI = 0.0;
			// 1.0 starting value instead of 0.0 is to avoid division by zero
			// probabilistic pseudo-counting, a variant of weak-prior on a distribution
			// It's also known as "Laplace's rule of succession"
			double denominatorSumMOfI = 1.0;
			double denominatorSumUOfI = 1.0;
			for (int j = 0; j < pairCount; j++) {
				numeratorSumMOfI += gamma[j][i] * gOfM[j];
				numeratorSumUOfI += gamma[j][i] * gOfU[j];
				denominatorSumMOfI += gOfM[j];
				denominatorSumUOfI += gOfU[j];
			}
			mOfI[i] = numeratorSumMOfI/denominatorSumMOfI;
			uOfI[i] = numeratorSumUOfI/denominatorSumUOfI;
		}
	}

	private void estimateProbability(int pairCount) {
		double numeratorSum = 0.0;
		for (int j = 0; j < pairCount; j++) {
			numeratorSum += gOfM[j];
		}
		p = numeratorSum/pairCount;
	}

	private double calculateProduct(int pairIndex, double[] vector) {
		double product = 1.0;
		for (int i = 0; i < vector.length; i++) {
			double vi = vector[i];
			double gvi = gamma[pairIndex][i];
			double prob = 0.0;
//			prob = (Math.pow(vi, gvi) * Math.pow(1.0 - vi, 1.0 - gvi));
/*			if (vi > 0.5) {
				prob = (2.0 * vi - 1.0) * gvi + 1.0 - vi;
			} else {
				prob = (1.0 - 2.0 * vi) * (1.0 - gvi) + vi;
			}*/
			prob = 2.0 * vi * gvi - gvi - vi + 1.0;
			product *= prob;

/*			if (gamma[pairIndex][i] == 0) {
				product *= (1.0 - vector[i]);
			} else {
				product *= vector[i];
			}*/
		}
		return product;
	}
	
	private void initializeAlgorithm(FellegiSunterParameters params, MatchConfiguration matchConfig,
			List<LeanRecordPair> pairs, double mInitial, double uInitial, double pInitial, int pairNumber)
	{
		Random rng = new Random();

		gOfM = new double[pairNumber];
		gOfU = new double[pairNumber];
		mOfI = new double[params.getFieldCount()];
		initializeMarginalsVector(mOfI, mInitial, rng, true);
		uOfI = new double[params.getFieldCount()];
		initializeMarginalsVector(uOfI, uInitial, rng, false);
		p = /*rng.nextDouble()*/ pInitial;
		gamma = new int[pairNumber][params.getFieldCount()];
		int j = 0;
		for (LeanRecordPair pair : pairs) {
			int[] binaryScores = pair.getComparisonVector().getBinaryVector();
			for (int i = 0; i < params.getFieldCount(); i++) {
				gamma[j][i] = binaryScores[i];
			}
			j++;
		}
	}

	private void initializeMarginalsVector(double[] vector, double initial, Random rng, boolean upper) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = initial;
/*			double rn = rng.nextDouble();
			if (upper && rn < 0.5)
				vector[i] = 2 * rn;
			else if (!upper && rn > 0.5)
				vector[i] = rn / 2;
			else
				vector[i] = rn;*/
		}
	}
}
