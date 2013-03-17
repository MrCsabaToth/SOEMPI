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
package org.openhie.openempi.blocking.privacypreserving;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.util.SerializationUtil;

public class LocalitySensitiveHashingWithCompositeBloomFiltersServiceImpl extends PrivacyPreservingBlockingBase
{
	private static final String LEAN_RECORDPAIRS_FILE_NAME = "LeanRecordPairs.ser";

	public void init() {
		log.trace("Initializing the Blocking Bypass Service");
	}

	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			List<LeanRecordPair> pairs, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) {
		List<BloomFilterBitStat> bitStats = null;
		String fileRepositoryDirectory =
			Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
		String filename = fileRepositoryDirectory + "/" + BLOOMFILTER_STATISTICS_FILE_NAME;
		File file = new File(filename);
//		if (file.exists()) {
//			BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(configDirectory);
//			bitStats = bloomFilterStatistics.getBloomFilterBitStats();
//		} else {
			calculateBitStatistics(matchingServiceTypeName, leftTableName, rightTableName);
			if (file.exists()) {
				BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(fileRepositoryDirectory);
				bitStats = bloomFilterStatistics.getBloomFilterBitStats();
			} else {
				return;
			}
//		}

		Collections.sort(bitStats, new BloomFilterBitStatComparatorByImportance());
		Integer numberOfImportantBits =
			Context.getConfiguration().getPrivacyPreservingBlockingSettings().getNumberOfBlockingBits();
		List<BloomFilterBitStat> importantBits = new ArrayList<BloomFilterBitStat>();
		for(int i = 0; i < numberOfImportantBits; i++) {
			importantBits.add(bitStats.get(bitStats.size() - 1 - i));
		}
		Collections.sort(importantBits, new BloomFilterBitStatComparatorByBloomFilterIndex());

		getRecordPairs(importantBits, pairs, null, leftTableName, rightTableName, emOnly, fellegiSunterParameters);
	}

	private void saveLeanRecordPairs(String configDirectory, List<LeanRecordPair> pairs) {
		SerializationUtil.serializeObject(configDirectory, LEAN_RECORDPAIRS_FILE_NAME, pairs);
	}

	private Object loadLeanRecordPairs(String configDirectory) {
		return SerializationUtil.deserializeObject(configDirectory, LEAN_RECORDPAIRS_FILE_NAME);
	}

}
