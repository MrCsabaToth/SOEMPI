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
package org.openhie.openempi.stringcomparison.metrics;

public class ExactMatchDistanceMetricBinaryVersion extends AbstractDistanceMetric
{
	public final static String TRUNCATION_LENGTH = "truncation-length";
	
	private Integer truncationLength;
	
	public ExactMatchDistanceMetricBinaryVersion() {
	}

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}
		byte[] byteArray1 = (byte[])value1;
		byte[] byteArray2 = (byte[])value2;
		if (java.util.Arrays.equals(byteArray1, byteArray2))
			return 1.0;
		return 0.0;
	}

	@Override
	public void setParameter(String key, Object value) {
		if (key == null || !key.equalsIgnoreCase(TRUNCATION_LENGTH)) {
			return;
		}
		if (value instanceof String) {
			try {
				truncationLength = Integer.parseInt((String) value);
				log.debug("Set the truncation length to " + truncationLength);
			} catch (NumberFormatException e) {	
				truncationLength = null;
			}
		}
	}
}
