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

import java.util.Random;

public class NicknameCorruptor extends StringCorruptor
{
	static final String REPLACE_PROBABILITY_TAG = "replace_probability";

	private Double defaultReplaceProbability = 0.0;

	public NicknameCorruptor() {
		super();
	}

	public Double getDefaultReplaceProbability() {
		return defaultReplaceProbability;
	}

	public void setDefaultReplaceProbability(Double probability) {
		this.defaultReplaceProbability = probability;
	}

	protected Object stringTransformCore(String field, java.util.Map<String, Object> parameters) {
		double replaceProbability = defaultReplaceProbability;

		String corrupted = field;
		Random rnd = new Random();
		if (parameters.containsKey(REPLACE_PROBABILITY_TAG))
			replaceProbability = (Double)parameters.get(REPLACE_PROBABILITY_TAG);
		if (rnd.nextDouble() < replaceProbability)
			corrupted = NicknameSwapout.swapout(field, rnd);

		return super.stringTransformCore(corrupted, parameters);
	}
}
