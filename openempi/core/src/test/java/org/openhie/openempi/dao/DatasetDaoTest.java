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
package org.openhie.openempi.dao;

import java.util.List;

import org.junit.Test;
import org.openhie.openempi.Constants;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.Dataset;

public class DatasetDaoTest extends BaseDaoTestCase
{
	private UserDao userDao;
	private DatasetDao datasetDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public DatasetDao getDatasetDao() {
		return datasetDao;
	}

	public void setDatasetDao(DatasetDao datasetDao) {
		this.datasetDao = datasetDao;
	}

	@Test
	public void testSaveDataset() {
		try {
			User user = (User) userDao.loadUserByUsername(Constants.DEFAULT_ADMIN_USERNAME);
			
			for (int i=0; i < 10; i++) {
				Dataset aDataset = new Dataset("test" + i, "/tmp/test.file" + i + ".test");
				aDataset.setDateCreated(new java.util.Date());
				aDataset.setOwner(user);			
				datasetDao.saveDataset(aDataset);
			}
			
			List<Dataset> datasets = datasetDao.getDatasets(user);
			for (Dataset dataset : datasets) {
				System.out.println("Found file: " + dataset);
			}
			
			for (Dataset dataset : datasets) {
				System.out.println("Removing file: " + dataset);
				datasetDao.removeDataset(dataset);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
