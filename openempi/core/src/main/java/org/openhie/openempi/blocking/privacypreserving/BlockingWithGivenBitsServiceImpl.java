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
import java.util.List;

import org.openhie.openempi.configuration.PrivacyPreservingBlockingField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.LeanRecordPair;

public class BlockingWithGivenBitsServiceImpl extends PrivacyPreservingBlockingBase
{
	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			List<LeanRecordPair> pairs, boolean emOnly, FellegiSunterParameters fellegiSunterParams) {
		List<BloomFilterBitStat> selectedBits = new ArrayList<BloomFilterBitStat>();
		List<PrivacyPreservingBlockingField> ppbFields =
			Context.getConfiguration().getPrivacyPreservingBlockingSettings().getPrivacyPreservingBlockingFields();
		for(PrivacyPreservingBlockingField ppbField : ppbFields) {
			List<Integer> bits = ppbField.getBits();
			for(Integer bit : bits) {
				BloomFilterBitStat bitStat = new BloomFilterBitStat(ppbFields.indexOf(ppbField), bit);
				selectedBits.add(bitStat);
			}
		}
		getRecordPairs(selectedBits, pairs, null, leftTableName, rightTableName, emOnly, fellegiSunterParams);
	}

}
