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
package org.openhie.openempi.stringcomparison.metrics;

public class ScalarDoubleDistanceMetric extends AbstractDistanceMetric
{
	public ScalarDoubleDistanceMetric() {
	}

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}
		double v1 = 0.0;
		if (value1 instanceof Double) {
			v1 = (Double)value1;
		} else {
			try {
				v1 = Double.valueOf(value1.toString());
			}
			catch (java.lang.NumberFormatException e) {
			}
		}
		double v2 = 0.0;
		if (value2 instanceof Double) {
			v2 = (Double)value2;
		} else {
			try {
				v2 = Double.valueOf(value2.toString());
			}
			catch (java.lang.NumberFormatException e) {
			}
		}
		double distance = Math.abs(v1 - v2);
		log.trace("Computed the distance between :" + value1.toString() + ": and :" + value2.toString() + ": to be " + distance);
		return distance;
	}
}
