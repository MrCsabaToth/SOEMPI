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
package org.openhie.openempi.stringcomparison;

import java.io.Serializable;

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class DistanceMetricType extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1850164680144032068L;

	private String name;
	private DistanceMetric distanceMetric;
	
	public DistanceMetricType(String name, DistanceMetric distanceMetric) {
		this.name = name;
		this.distanceMetric = distanceMetric;
	}

	public DistanceMetric getDistanceMetric() {
		return distanceMetric;
	}

	public void setDistanceMetric(DistanceMetric distanceMetric) {
		this.distanceMetric = distanceMetric;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof DistanceMetricType))
			return false;
		DistanceMetricType castOther = (DistanceMetricType) other;
		return new EqualsBuilder().append(name, castOther.name).append(
				distanceMetric, castOther.distanceMetric).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(distanceMetric)
				.toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
