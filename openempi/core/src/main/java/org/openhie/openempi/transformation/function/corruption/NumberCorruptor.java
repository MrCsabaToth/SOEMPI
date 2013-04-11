/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
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
package org.openhie.openempi.transformation.function.corruption;

import java.util.Random;

import org.openhie.openempi.transformation.function.AbstractStringTransformationFunction;

public class NumberCorruptor extends AbstractStringTransformationFunction
{
	static final String INSERTION_PROBABILITY_TAG = "insertion_probability";
	static final String DELETION_PROBABILITY_TAG = "deletion_probability";
	static final String SUBSTITUTION_PROBABILITY_TAG = "substitution_probability";
	static final String TRANSPOSITION_PROBABILITY_TAG = "transposition_probability";

	public NumberCorruptor() {
		super();
	}
	
	protected Object stringTransformCore(String field, java.util.Map<String, Object> parameters) {
		double insertionProbability = 0.0;		// default
		double deletionProbability = 0.0;		// default
		double substitutionProbability = 0.0;	// default
		double transpositionProbability = 0.0;	// default

		String corrupted = field;
		Random rnd = new Random();
		if (parameters.containsKey(INSERTION_PROBABILITY_TAG))
			insertionProbability = (Double)parameters.get(INSERTION_PROBABILITY_TAG);
		if (rnd.nextDouble() < insertionProbability)
			corrupted = TypographicError.insertNumber(field, rnd);
		if (parameters.containsKey(DELETION_PROBABILITY_TAG))
			deletionProbability = (Double)parameters.get(DELETION_PROBABILITY_TAG);
		if (rnd.nextDouble() < deletionProbability)
			corrupted = TypographicError.delete(corrupted, rnd);
		if (parameters.containsKey(SUBSTITUTION_PROBABILITY_TAG))
			substitutionProbability = (Double)parameters.get(SUBSTITUTION_PROBABILITY_TAG);
		if (rnd.nextDouble() < substitutionProbability)
			corrupted = TypographicError.substituteNumber(corrupted, rnd);
		if (parameters.containsKey(TRANSPOSITION_PROBABILITY_TAG))
			transpositionProbability = (Double)parameters.get(TRANSPOSITION_PROBABILITY_TAG);
		if (rnd.nextDouble() < transpositionProbability)
			corrupted = TypographicError.transpose(corrupted, rnd);
		return corrupted;
	}
}
