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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingField;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingSettings;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonQueryService;

public abstract class BlockingWithRandomBitsServiceBase extends PrivacyPreservingBlockingBase
{
	public abstract Random getRandomSource();
	public Person modelPerson = null;

	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			List<LeanRecordPair> pairs, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) {
		PrivacyPreservingBlockingSettings ppbs = Context.getConfiguration().getPrivacyPreservingBlockingSettings();
		List<PrivacyPreservingBlockingField> ppbFields = ppbs.getPrivacyPreservingBlockingFields();
		Integer numberOfRandomBits = ppbs.getNumberOfBlockingBits();
		Integer numberOfFields = ppbFields.size();
		Random r = getRandomSource();

		PersonQueryService personQueryService = Context.getPersonQueryService();
		Dataset leftDataset = personQueryService.getDatasetByTableName(leftTableName);
		List<ColumnInformation> leftColumnInformation = leftDataset.getColumnInformation();
		Dataset rightDataset = personQueryService.getDatasetByTableName(rightTableName);
		List<ColumnInformation> rightColumnInformation = rightDataset.getColumnInformation();
		Set<String> idPairHash = new HashSet<String>();
		for(Integer runIndex = 0; runIndex < ppbs.getNumberOfRuns(); runIndex++) {
			List<BloomFilterBitStat> selectedBits = new ArrayList<BloomFilterBitStat>();
			for(Integer i = 0; i < numberOfRandomBits; i++) {
				Integer randomFieldIndex = r.nextInt(numberOfFields);
				PrivacySettings privacySettings =
						(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
				Integer bloomFilterSize = privacySettings.getBloomfilterSettings().getDefaultM();
				// get the function parameters from the persisted import configuration and check match
				PrivacyPreservingBlockingField ppbField = ppbFields.get(randomFieldIndex);
				Integer leftBloomFilterK = 0;
				Integer leftBloomFilterM = 0;
				for (ColumnInformation leftCI : leftColumnInformation) {
					if (leftCI.getFieldName().equals(ppbField.getLeftFieldName())) {
						leftBloomFilterK = leftCI.getBloomFilterKParameter();
						leftBloomFilterM = leftCI.getBloomFilterMParameter();
					}
				}
				Integer rightBloomFilterK = 0;
				Integer rightBloomFilterM = 0;
				for (ColumnInformation rightCI : rightColumnInformation) {
					if (rightCI.getFieldName().equals(ppbField.getRightFieldName())) {
						rightBloomFilterK = rightCI.getBloomFilterKParameter();
						rightBloomFilterM = rightCI.getBloomFilterMParameter();
					}
				}
				if (leftBloomFilterK != rightBloomFilterK || leftBloomFilterM != rightBloomFilterM)
					log.error("Left and right bloom filter parameters doesn't match: " +
							ppbField.getLeftFieldName() + " - " + leftBloomFilterK + ", " + leftBloomFilterM + ", " +
							ppbField.getRightFieldName() + " - " + rightBloomFilterK + ", " + rightBloomFilterM, null);
				if (rightBloomFilterM != 0)
					bloomFilterSize = rightBloomFilterM;
				Integer randomBitIndex = r.nextInt(bloomFilterSize);
				BloomFilterBitStat bitStat = new BloomFilterBitStat(randomFieldIndex, randomBitIndex);
				selectedBits.add(bitStat);
			}
			getRecordPairs(selectedBits, pairs, idPairHash, leftTableName, rightTableName, emOnly, fellegiSunterParameters);
		}
	}

}
