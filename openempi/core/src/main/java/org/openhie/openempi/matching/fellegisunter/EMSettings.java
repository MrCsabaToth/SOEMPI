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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class EMSettings extends BaseObject
{
	private static final long serialVersionUID = -1417495231879034045L;

	private double mInitial = 0.9f;
	private double uInitial = 0.1f;
	private double pInitial = 0.001f;
	private double convergenceError = 1e-7f;
	private int maxIterations = 1000;
	private int maxTries = 10;

	public double getmInitial() {
		return mInitial;
	}

	public void setmInitial(double mInitial) {
		this.mInitial = mInitial;
	}

	public double getuInitial() {
		return uInitial;
	}

	public void setuInitial(double uInitial) {
		this.uInitial = uInitial;
	}

	public double getpInitial() {
		return pInitial;
	}

	public void setpInitial(double pInitial) {
		this.pInitial = pInitial;
	}

	public double getConvergenceError() {
		return convergenceError;
	}

	public void setConvergenceError(double convergenceError) {
		this.convergenceError = convergenceError;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getMaxTries() {
		return maxTries;
	}

	public void setMaxTries(int maxTries) {
		this.maxTries = maxTries;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("mInitial", mInitial).
				append("uInitial", uInitial).
				append("pInitial", pInitial).
				append("convergenceError", convergenceError).
				append("maxIterations", maxIterations).
				append("maxTries", maxTries).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MatchConfiguration))
			return false;
		EMSettings castOther = (EMSettings) other;
		return new EqualsBuilder().
				append(mInitial, castOther.mInitial).
				append(uInitial, castOther.uInitial).
				append(pInitial, castOther.pInitial).
				append(convergenceError, castOther.convergenceError).
				append(maxIterations, castOther.maxIterations).
				append(maxTries, castOther.maxTries).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(mInitial).
				append(uInitial).
				append(pInitial).
				append(convergenceError).
				append(maxIterations).
				append(maxTries).
				toHashCode();
	}
}
