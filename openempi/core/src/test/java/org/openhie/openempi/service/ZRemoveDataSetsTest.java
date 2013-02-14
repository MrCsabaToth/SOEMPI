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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Dataset;

public class ZRemoveDataSetsTest extends BaseServiceTestCase
{
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		try {
			super.onSetUp();
			setupContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testRemoveTestDatasets() {
		PersonManagerService personService = Context.getPersonManagerService();
		List<String> tableNames = personService.getDatasetTableNames();
		for (String tableName : tableNames) {
			if (tableName.contains("test")) {
				Dataset datasetForTest = personService.getDatasetByTableName(tableName);
				personService.removeDataset(datasetForTest);
			}
		}
	}
	
}
