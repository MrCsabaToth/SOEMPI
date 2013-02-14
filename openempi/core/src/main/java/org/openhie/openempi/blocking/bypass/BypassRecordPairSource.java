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
package org.openhie.openempi.blocking.bypass;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.NamePairValuePair;

public class BypassRecordPairSource implements RecordPairSource
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public void init(String leftTableName, String rightTableName) {
		log.trace("Initializing the Bypass Record Pair Source");
	}

	public List<List<NamePairValuePair>> getBlockingValueList() {
		return null;
	}

	public RecordPairIterator iterator(String leftTableName, String rightTableName, String leftOriginalIdFieldName,
			String rightOriginalIdFieldName, boolean emOnly, FellegiSunterParameters fellegiSunterParameters) {
		BypassRecordPairIterator iterator = new BypassRecordPairIterator(this, leftTableName, rightTableName, leftOriginalIdFieldName,
				rightOriginalIdFieldName, emOnly, fellegiSunterParameters);
		return iterator;
	}

}
