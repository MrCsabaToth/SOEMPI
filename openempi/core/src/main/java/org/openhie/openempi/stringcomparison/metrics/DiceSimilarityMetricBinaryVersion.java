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

public class DiceSimilarityMetricBinaryVersion extends AbstractDistanceMetric
{
	public static int[][] INTERSECTIONHW_LOOKUP_TABLE = null;
	public static final int[] HAMMING_WEIGHT_LOOKUP_TABLE = new int[] {
	    0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
	    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	    4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8
	};
	public static Object preComputeGuard = new Object();
	public static void computeIntersectionHammingWeightLookupTable() {
		if (INTERSECTIONHW_LOOKUP_TABLE == null) {
			synchronized(preComputeGuard) {
				if (INTERSECTIONHW_LOOKUP_TABLE == null) {
					INTERSECTIONHW_LOOKUP_TABLE = new int[256][256];
					for (int i = 0; i < 256; i++) {
						for (int j = 0; j <= i; j++) {
							INTERSECTIONHW_LOOKUP_TABLE[i][j] = HAMMING_WEIGHT_LOOKUP_TABLE[i & j];
							INTERSECTIONHW_LOOKUP_TABLE[j][i] = INTERSECTIONHW_LOOKUP_TABLE[i][j];
						}
					}
				}
			}
		}
	}

	public DiceSimilarityMetricBinaryVersion() {
	}

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}

		computeIntersectionHammingWeightLookupTable();

		byte[] byteArray1 = (byte[])value1;
		byte[] byteArray2 = (byte[])value2;
		int intersectionBits = 0;
		int set1Bits = 0;
		int set2Bits = 0;
		for (int i = 0; i < byteArray1.length; i++) {
			int b1 = (byteArray1[i] & 0xFF);
			int b2 = (byteArray2[i] & 0xFF);
			intersectionBits += INTERSECTIONHW_LOOKUP_TABLE[b1][b2];
			set1Bits += HAMMING_WEIGHT_LOOKUP_TABLE[b1];
			set2Bits += HAMMING_WEIGHT_LOOKUP_TABLE[b2];
		}
		double distance = 1.0;
		if (set1Bits != 0 || set2Bits != 0)
			distance = (float)2.0 * intersectionBits / (set1Bits + set2Bits);
		log.trace("Computed the distance between :" + byteArray1 + ": and :" + byteArray2 + ": to be " + distance);
		return distance;
	}
}
