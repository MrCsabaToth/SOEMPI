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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class EMSettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = -4184632865967325174L;

	public static final String M_INITIAL = "mInitial";
	public static final String U_INITIAL = "uInitial";
	public static final String P_INITIAL = "pInitial";
	public static final String CONVERGENCE_ERROR = "convergenceError";
	public static final String MAX_ITERATIONS = "maxIterations";
	public static final String MAX_TRIES = "maxTries";

	public EMSettingsWeb() {
	}

	public Double getMInitial() {
		return get(M_INITIAL);
	}

	public void setMInitial(Double mInitial) {
		set(M_INITIAL, mInitial);
	}

	public Double getUInitial() {
		return get(U_INITIAL);
	}

	public void setUInitial(Double uInitial) {
		set(U_INITIAL, uInitial);
	}

	public Double getPInitial() {
		return get(P_INITIAL);
	}

	public void setPInitial(Double pInitial) {
		set(P_INITIAL, pInitial);
	}

	public Double getConvergenceError() {
		return get(CONVERGENCE_ERROR);
	}

	public void setConvergenceError(Double convergenceError) {
		set(CONVERGENCE_ERROR, convergenceError);
	}

	public Integer getMaxIterations() {
		return get(MAX_ITERATIONS);
	}

	public void setMaxIterations(Integer maxIterations) {
		set(MAX_ITERATIONS, maxIterations);
	}

	public Integer getMaxTries() {
		return get(MAX_TRIES);
	}

	public void setMaxTries(Integer maxTries) {
		set(MAX_TRIES, maxTries);
	}

}
