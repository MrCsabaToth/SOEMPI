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

public class LastnameCorruptor extends StringCorruptor
{
	static final String FEMALE_REPLACE_PROBABILITY_TAG = "female_replace_probability";
	static final String MALE_REPLACE_PROBABILITY_TAG = "male_replace_probability";
	static final String HYPHENATE_PROBABILITY_TAG = "hyphenate_probability";
	static final String GENDER_TAG = "gender";

	private double defaultFemaleReplaceProbability = 0.0;
	private double defaultMaleReplaceProbability = 0.0;
	private double defaultHyphenateProbability = 0.0;

	public LastnameCorruptor() {
		super();
	}

	public double getDefaultFemaleReplaceProbability() {
		return defaultFemaleReplaceProbability;
	}

	public void setDefaultFemaleReplaceProbability(String probability) {
		this.defaultFemaleReplaceProbability = Double.parseDouble(probability);
	}

	public double getDefaultMaleReplaceProbability() {
		return defaultMaleReplaceProbability;
	}

	public void setDefaultMaleReplaceProbability(String probability) {
		this.defaultMaleReplaceProbability = Double.parseDouble(probability);
	}

	public double getDefaultHyphenateProbability() {
		return defaultHyphenateProbability;
	}

	public void setDefaultHyphenateProbability(String probability) {
		this.defaultHyphenateProbability = Double.parseDouble(probability);
	}

	protected Object stringTransformCore(String field, java.util.Map<String, Object> parameters) {
		double femaleReplaceProbability = defaultFemaleReplaceProbability;
		double maleReplaceProbability = defaultFemaleReplaceProbability;
		double hyphenateProbability = defaultHyphenateProbability;

		String corrupted = field;
		Random rnd = new Random();
		if (parameters.containsKey(HYPHENATE_PROBABILITY_TAG))
			hyphenateProbability = (Double)parameters.get(HYPHENATE_PROBABILITY_TAG);
		if (rnd.nextDouble() < hyphenateProbability) {
			corrupted = corrupted + "-" + LastnameSwapout.swapout(rnd);
		} else {
			if (parameters.containsKey(FEMALE_REPLACE_PROBABILITY_TAG))
				femaleReplaceProbability = (Double)parameters.get(FEMALE_REPLACE_PROBABILITY_TAG);
			if (parameters.containsKey(MALE_REPLACE_PROBABILITY_TAG))
				maleReplaceProbability = (Double)parameters.get(MALE_REPLACE_PROBABILITY_TAG);

			double replaceProbability = 0.0;
			if (parameters.containsKey(GENDER_TAG)) {
				String genderStr = (String)parameters.get(FEMALE_REPLACE_PROBABILITY_TAG);
				if (genderStr.toUpperCase().startsWith("M"))
					replaceProbability = maleReplaceProbability;
				else
					replaceProbability = femaleReplaceProbability;
			} else {
				replaceProbability = (femaleReplaceProbability + maleReplaceProbability) / 2;
			}
			if (rnd.nextDouble() < replaceProbability)
				corrupted = LastnameSwapout.swapout(rnd);
		}

		return super.stringTransformCore(corrupted, parameters);
	}
}
