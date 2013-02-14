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

import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Dataset;

public class DatasetManagementTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================
    private PersonManagerService mgr = null;

    public void setPersonManagerService(PersonManagerService personManager) {
        this.mgr = personManager;
    }
    
    public void testDatasets() throws Exception {
    	Context.startup();
    	Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
     
		for (int i=0; i < 10; i++) {
			Dataset aDataset = new Dataset("test" + i, "/tmp/test.file" + i + ".test");
			aDataset.setOwner(Context.getUserContext().getUser());
			mgr.saveDataset(aDataset);				
		}
		
		List<Dataset> datasets = mgr.getDatasets(Context.getUserContext().getUser());
		for (Dataset dataset : datasets) {
			System.out.println("Found file: " + dataset);
		}
		
		for (Dataset set : datasets) {
			System.out.println("Removing file: " + set);
			mgr.removeDataset(set);
		}
    }

}
