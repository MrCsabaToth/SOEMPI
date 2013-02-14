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

import org.openhie.openempi.model.Salt;

public class SaltDaoTest extends BaseDaoTestCase
{
	private SaltDao saltDao;

	public SaltDao getSaltDao() {
		return saltDao;
	}

	public void setSaltDao(SaltDao saltDao) {
		this.saltDao = saltDao;
	}

	public void testGetSaltsBySaltIdentifier() {
		try {
			List<Salt> salts = saltDao.getSalts(1L, 2L);
			for (Salt salt : salts) {
				System.out.println("Salt Is: " + salt);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetSaltBySaltIdentifier() {
		try {
			Salt salt = saltDao.getSalt(1L);
			System.out.println("Found salt: " + salt);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
}
