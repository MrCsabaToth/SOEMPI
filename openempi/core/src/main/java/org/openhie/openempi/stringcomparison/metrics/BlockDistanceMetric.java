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
package org.openhie.openempi.stringcomparison.metrics;

import uk.ac.shef.wit.simmetrics.similaritymetrics.BlockDistance;

public class BlockDistanceMetric extends AbstractDistanceMetric
{
	private BlockDistance blockDistance = new BlockDistance();
	
	public BlockDistanceMetric() {
	}

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}
		String string1 = value1.toString();
		String string2 = value2.toString();
		double distance = blockDistance.getSimilarity(upperCase(string1), upperCase(string2));
		log.trace("Computed the distance between :" + string1 + ": and :" + string2 + ": to be " + distance);
		return distance;
	}
}
