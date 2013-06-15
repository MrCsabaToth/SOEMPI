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

public class JaccardSimilarityMetricBinaryVersion extends AbstractDistanceMetric
{
	static int[][] UNIONHW_LOOKUP_TABLE = null;
	static Object preComputeGuard = new Object();
	public static void computeUnionHammingWeightLookupTable() {
		if (UNIONHW_LOOKUP_TABLE == null) {
			synchronized(preComputeGuard) {
				if (UNIONHW_LOOKUP_TABLE == null) {
					UNIONHW_LOOKUP_TABLE = new int[256][256];
					for (int i = 0; i < 256; i++) {
						for (int j = 0; j <= i; j++) {
							UNIONHW_LOOKUP_TABLE[i][j] = DiceSimilarityMetricBinaryVersion.HAMMING_WEIGHT_LOOKUP_TABLE[i | j];
							UNIONHW_LOOKUP_TABLE[j][i] = UNIONHW_LOOKUP_TABLE[i][j];
						}
					}
				}
			}
		}
	}

	public JaccardSimilarityMetricBinaryVersion() {
	}

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}

		DiceSimilarityMetricBinaryVersion.computeIntersectionHammingWeightLookupTable();
		computeUnionHammingWeightLookupTable();

		byte[] byteArray1 = (byte[])value1;
		byte[] byteArray2 = (byte[])value2;

		int intersectionBits = 0;
		int unionBits = 0;
		for (int i = 0; i < byteArray1.length; i++) {
			int b1 = (byteArray1[i] & 0xFF);
			int b2 = (byteArray2[i] & 0xFF);
			intersectionBits += DiceSimilarityMetricBinaryVersion.INTERSECTIONHW_LOOKUP_TABLE[b1][b2];
			unionBits += UNIONHW_LOOKUP_TABLE[b1][b2];
		}
		double distance = 1.0;
		if (unionBits != 0)
			distance = (float)intersectionBits / unionBits;
		log.trace("Computed the distance between :" + byteArray1 + ": and :" + byteArray2 + ": to be " + distance);
		return distance;
	}
}
