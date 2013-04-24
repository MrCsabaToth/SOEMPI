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
package org.openhie.openempi.transformation.function.corruption;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.transformation.function.AbstractStringTransformationFunction;

public class SlidingWindowSwapoutCorruptor extends AbstractStringTransformationFunction
{
	public static final String SWAPOUT_PROBABILITY_TAG = "swapout_probability";
	public static final String SLIDING_WINDOW_SIZE_TAG = "sliding_window_size";

	private double defaultSwapoutProbability = 0.0;
	private int defaultSlidingWindowSize = 0;
	
	private List<String> swapoutList = new ArrayList<String>();

	public SlidingWindowSwapoutCorruptor() {
		super();
	}

	public double getDefaultSwapoutProbability() {
		return defaultSwapoutProbability;
	}

	public void setDefaultSwapoutProbability(double probability) {
		this.defaultSwapoutProbability = probability;
	}

	public double getDefaultSlidingWindowSize() {
		return defaultSlidingWindowSize;
	}

	public void setDefaultSlidingWindowSize(int probability) {
		this.defaultSlidingWindowSize = probability;
	}

	protected Object stringTransformCore(String field, java.util.Map<String, Object> parameters) {
		double swapoutProbability = defaultSwapoutProbability;

		String corrupted = field;
		SecureRandom rnd = new SecureRandom();

		if (parameters.containsKey(SWAPOUT_PROBABILITY_TAG))
			swapoutProbability = (Double)parameters.get(SWAPOUT_PROBABILITY_TAG);
		if (rnd.nextDouble() < swapoutProbability && swapoutList.size() > 0)
			corrupted = swapoutList.get(rnd.nextInt(swapoutList.size()));
		swapoutList.add(field);
		if (swapoutList.size() > defaultSlidingWindowSize)
			swapoutList.remove(0);

		return corrupted;
	}
}
