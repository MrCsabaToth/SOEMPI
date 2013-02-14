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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class FellegiSunterParametersWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = -4184632865967325174L;

	public static final String VECTOR_FREQUENCIES = "vectorFrequencies";
	public static final String M_VALUES = "mValues";
	public static final String U_VALUES = "uValues";
	public static final String LOWER_BOUND = "lowerBound";
	public static final String UPPER_BOUND = "upperBound";
	public static final String LAMBDA = "lambda";
	public static final String MU = "mu";

	public FellegiSunterParametersWeb() {
	}

	public List<Long> getVectorFrequencies() {
		return get(VECTOR_FREQUENCIES);
	}

	public void setVectorFrequencies(List<Long> vectorFrequencies) {
		set(VECTOR_FREQUENCIES, vectorFrequencies);
	}

	public List<Double> getMValues() {
		return get(M_VALUES);
	}

	public void setMValues(List<Double> mValues) {
		set(M_VALUES, mValues);
	}

	public List<Double> getUValues() {
		return get(U_VALUES);
	}

	public void setUValues(List<Double> uValues) {
		set(U_VALUES, uValues);
	}

	public Double getLowerBound() {
		return get(LOWER_BOUND);
	}

	public void setLowerBound(Double lowerBound) {
		set(LOWER_BOUND, lowerBound);
	}

	public Double getUpperBound() {
		return get(UPPER_BOUND);
	}

	public void setUpperBound(Double upperBound) {
		set(UPPER_BOUND, upperBound);
	}

	public Double getLambda() {
		return get(LAMBDA);
	}

	public void setLambda(Double lambda) {
		set(LAMBDA, lambda);
	}

	public Double getMu() {
		return get(MU);
	}

	public void setMu(Double mu) {
		set(MU, mu);
	}

}
