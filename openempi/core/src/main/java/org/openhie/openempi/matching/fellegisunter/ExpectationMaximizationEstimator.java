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
package org.openhie.openempi.matching.fellegisunter;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExpectationMaximizationEstimator
{
	protected Log log = LogFactory.getLog(ExpectationMaximizationEstimator.class);
	
	private double[] gOfM;
	private double[] gOfU;
	private double[] mOfI;
	private double[] uOfI;
	private double p;
	// Gamma[J][i]
	private long[][] gamma;
	
	public synchronized void estimateMarginalProbabilities(FellegiSunterParameters params,
			double mInitial, double uInitial, double pInitial, double convergenceError,
			int maxIterations, int maxTries) {
		int tries = 1;
		Random rnd = new Random(1122334455);
		initializeAlgorithm1(params, mInitial, uInitial, pInitial);
		boolean isnan = true;
		do {
			if (tries > 1)
				pInitial = rnd.nextDouble();
			initializeAlgorithm2(params, mInitial, uInitial, pInitial);
	
			double error = 1.0;
			int iteration = 1;
			do {
				// Expectation Step
				estimateGammaOfMAndU(params.getVectorCount());
				
				// Maximization Step
				estimateMOfI(params.getFieldCount(), params.getVectorCount(), params.getVectorFrequencies(), convergenceError);
				estimateUOfI(params.getFieldCount(), params.getVectorCount(), params.getVectorFrequencies(), convergenceError);
				isnan = false;
				double pPrevious = p;
				estimateProbability(params.getVectorCount(), params.getVectorFrequencies(), convergenceError);
				error = Math.abs(pPrevious - p);
				log.trace("Error at iteration " + iteration + " is " + error);
				System.out.println("Iteration no " + iteration + ": p=" + p);
				isnan = isnan || Double.isNaN(p);
				System.out.print("mOfI: ");
				for(int i = 0; i < mOfI.length; i++) {
					isnan = isnan || Double.isNaN(mOfI[i]);
					System.out.print(mOfI[i] + ",");
				}
				System.out.print("uOfI: ");
				for(int i = 0; i < uOfI.length; i++) {
					isnan = isnan || Double.isNaN(uOfI[i]);
					System.out.println(uOfI[i] + ",");
				}
				iteration++;
			} while (error > convergenceError && iteration < maxIterations);
			tries++;
		} while (isnan && tries < maxTries);
		
		params.setMValues(mOfI);
		params.setUValues(uOfI);
	}

	private void estimateGammaOfMAndU(int vectorCount) {
		for (int j = 0; j < vectorCount; j++) {
			double mProduct = p * calculateProduct(j, mOfI);
			double uProduct = (1.0 - p) * calculateProduct(j, uOfI);
			gOfM[j] = mProduct / (mProduct + uProduct);
			gOfU[j] = uProduct / (mProduct + uProduct);
		}
	}
	
	private void estimateMOfI(int fieldCount, int vectorCount, long[] fOfJ, double convergenceError) {
		estimateXOfI(fieldCount, vectorCount, fOfJ, gOfM, mOfI, convergenceError);
	}
	
	private void estimateUOfI(int fieldCount, int vectorCount, long[] fOfJ, double convergenceError) {
		estimateXOfI(fieldCount, vectorCount, fOfJ, gOfU, uOfI, convergenceError);
	}
	
	private void estimateXOfI(int fieldCount, int vectorCount, long[] fOfJ, double[] gOfX, double[] xOfI, double convergenceError) {
		for (int i = 0; i < fieldCount; i++) {
			double numeratorSumOfI = 0.0;
			double denominatorSumOfI = 0.0;
			for (int j = 0; j < vectorCount; j++) {
				numeratorSumOfI += gamma[j][i] * fOfJ[j] * gOfX[j];
				denominatorSumOfI += fOfJ[j] * gOfX[j];
			}
			// Avoid division by zero. Maybe we should let it divide,
			// the algorithm would start a new attempt from random initial values then
			if (denominatorSumOfI < convergenceError)
				denominatorSumOfI = convergenceError;
			xOfI[i] = numeratorSumOfI/denominatorSumOfI;
		}
	}
	
	private void estimateProbability(int vectorCount, long[] fOfJ, double convergenceError) {
		double numeratorSum = 0.0;
		double denominatorSum = 0.0;
		for (int j = 0; j < vectorCount; j++) {
			numeratorSum += gOfM[j] * fOfJ[j];
			denominatorSum += fOfJ[j];
		}
		// Avoid division by zero
		if (denominatorSum < convergenceError)
			denominatorSum = convergenceError;
		p = numeratorSum/denominatorSum;
	}

	private double calculateProduct(int cVectorIndex, double[] vector) {
		double product = 1.0;
		for (int i = 0; i < vector.length; i++) {
			if (gamma[cVectorIndex][i] == 0) {
				product *= (1.0 - vector[i]);
			} else {
				product *= vector[i];
			}
		}
		return product;
	}
	
	private void initializeAlgorithm1(FellegiSunterParameters params, double mInitial, double uInitial, double pInitial) {
		gOfM = new double[params.getVectorCount()];
		gOfU = new double[params.getVectorCount()];
		mOfI = new double[params.getFieldCount()];
		uOfI = new double[params.getFieldCount()];
		gamma = new long[params.getVectorCount()][params.getFieldCount()];
		for (int j = 0; j < params.getVectorCount(); j++) {
			for (int i = 0; i < params.getFieldCount(); i++) {
				gamma[j][i] = getBitPosition(j, i);
			}
		}
	}

	private void initializeAlgorithm2(FellegiSunterParameters params, double mInitial, double uInitial, double pInitial) {
		initializeMarginalsVector(mOfI, mInitial);
		initializeMarginalsVector(uOfI, uInitial);
		p = pInitial;
	}

	private void initializeMarginalsVector(double[] vector, double initial) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = initial;
		}
	}
	
	public int getBitPosition(int number, int position) {
		return (number >> position) & 1;
	}
	
	public void main(String[] args) {
		int[] numbers = { 7, 3, 8, 9, 12 };
		for (int j = 0; j < numbers.length; j++) {
			for (int i = 0; i < 8; i++) {
				System.out.println("Position " + i + " of number " + numbers[j] + " is " + getBitPosition(numbers[j], i));
			}
		}
	}
}
