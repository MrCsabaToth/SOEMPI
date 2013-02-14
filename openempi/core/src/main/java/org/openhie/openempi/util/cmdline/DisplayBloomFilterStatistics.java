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
package org.openhie.openempi.util.cmdline;

import java.util.Collections;
import java.util.List;

import org.openhie.openempi.blocking.privacypreserving.BloomFilterBitStat;
import org.openhie.openempi.blocking.privacypreserving.BloomFilterBitStatComparatorByImportance;
import org.openhie.openempi.blocking.privacypreserving.BloomFilterStatistics;
import org.openhie.openempi.util.SerializationUtil;

public class DisplayBloomFilterStatistics
{
	public DisplayBloomFilterStatistics() {
	}
	
	private static void loadFile(String filename) {
		BloomFilterStatistics bloomFilterStatistics = (BloomFilterStatistics)SerializationUtil.deserializeObject(filename);
		System.out.println("Number of pairs to sample: " + bloomFilterStatistics.getNumberOfPairsToSample());
		List<BloomFilterBitStat> bloomFilterBitStats = bloomFilterStatistics.getBloomFilterBitStats();
		System.out.println("Number of bits: " + bloomFilterBitStats.size());
		Collections.sort(bloomFilterBitStats, new BloomFilterBitStatComparatorByImportance());
		int i = 0;
		for(BloomFilterBitStat bloomFilterBitStat : bloomFilterBitStats) {
			System.out.println(i + ". bit: " + bloomFilterBitStat.toStringShort());
			i++;
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		String filename = args[0];
		System.out.println("Displaying the BloomFilter statistics file " + filename);

		loadFile(filename);
	}
	
	public static void usage() {
		System.out.println("Usage: " + DisplayBloomFilterStatistics.class.getName() + " <file-to-display>");
	}

}
