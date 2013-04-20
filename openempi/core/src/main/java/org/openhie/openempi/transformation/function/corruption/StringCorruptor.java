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

public class StringCorruptor extends NumberCorruptor
{
	static final String PHONETIC_ERROR_PROBABILITY_TAG = "phonetic_error_probability";
	static final String OCR_ERROR_PROBABILITY_TAG = "ocr_error_probability";

	private double defaultPhoneticErrorProbability = 0.0;
	private double defaultOcrErrorProbability = 0.0;

	public StringCorruptor() {
		super();
	}

	public double getDefaultPhoneticErrorProbability() {
		return defaultPhoneticErrorProbability;
	}

	public void setDefaultPhoneticErrorProbability(double probability) {
		this.defaultPhoneticErrorProbability = probability;
	}

	public double getDefaultOcrErrorProbability() {
		return defaultOcrErrorProbability;
	}

	public void setDefaultOcrErrorProbability(double probability) {
		this.defaultOcrErrorProbability = probability;
	}

	protected Object stringTransformCore(String field, java.util.Map<String, Object> parameters) {
		double phoneticErrorProbability = defaultPhoneticErrorProbability;
		double ocrErrorProbability = defaultOcrErrorProbability;
		double insertionProbability = getDefaultInsertionProbability();
		double deletionProbability = getDefaultDeletionProbability();
		double substitutionProbability = getDefaultSubstitutionProbability();
		double transpositionProbability = getDefaultTranspositionProbability();

		String corrupted = field;
		Random rnd = new Random();
		if (parameters.containsKey(PHONETIC_ERROR_PROBABILITY_TAG))
			phoneticErrorProbability = (Double)parameters.get(PHONETIC_ERROR_PROBABILITY_TAG);
		if (rnd.nextDouble() < phoneticErrorProbability)
			corrupted = PhoneticError.phoneticError(field, rnd);

		if (parameters.containsKey(OCR_ERROR_PROBABILITY_TAG))
			ocrErrorProbability = (Double)parameters.get(OCR_ERROR_PROBABILITY_TAG);
		if (rnd.nextDouble() < ocrErrorProbability)
			corrupted = OCRError.ocrError(corrupted, rnd);

		if (parameters.containsKey(INSERTION_PROBABILITY_TAG))
			insertionProbability = (Double)parameters.get(INSERTION_PROBABILITY_TAG);
		if (rnd.nextDouble() < insertionProbability)
			corrupted = TypographicError.insertLetter(corrupted, rnd);

		if (parameters.containsKey(DELETION_PROBABILITY_TAG))
			deletionProbability = (Double)parameters.get(DELETION_PROBABILITY_TAG);
		if (rnd.nextDouble() < deletionProbability)
			corrupted = TypographicError.delete(corrupted, rnd);

		if (parameters.containsKey(SUBSTITUTION_PROBABILITY_TAG))
			substitutionProbability = (Double)parameters.get(SUBSTITUTION_PROBABILITY_TAG);
		if (rnd.nextDouble() < substitutionProbability)
			corrupted = TypographicError.substituteLetter(corrupted, rnd);

		if (parameters.containsKey(TRANSPOSITION_PROBABILITY_TAG))
			transpositionProbability = (Double)parameters.get(TRANSPOSITION_PROBABILITY_TAG);
		if (rnd.nextDouble() < transpositionProbability)
			corrupted = TypographicError.transpose(corrupted, rnd);
		return corrupted;
	}
}
