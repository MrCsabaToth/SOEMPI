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

import java.util.Arrays;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Salt;

public class SaltServiceTest extends BaseServiceTestCase
{
	static Long addedSalt1Id = 0L;
	static Long addedSalt2Id = 0L;
	static byte[] originalSaltBytes1 = null;
	static byte[] originalSaltBytes2 = null;
	static byte[] saltBytes1 = null;
	static byte[] saltBytes2 = null;

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

	public void testAddSalt() throws ApplicationException {
		SaltManagerService saltService = Context.getSaltManagerService();

		Salt salt1 = saltService.addSalt();
		addedSalt1Id = salt1.getId();
		originalSaltBytes1 = salt1.getSalt().clone();

		Salt salt2 = saltService.addSalt();
		addedSalt2Id = salt2.getId();
		originalSaltBytes2 = salt2.getSalt().clone();
	}
	
	public void testGetSalt() throws ApplicationException {

		SaltManagerService saltService = Context.getSaltManagerService();

		Salt saltFound1 = saltService.getSalt(addedSalt1Id);
		log.debug("Found the salt: " + saltFound1);
		Salt saltFound2 = saltService.getSalt(addedSalt2Id);
		log.debug("Found the salt: " + saltFound2);

		saltBytes1 = saltFound1.getSalt().clone();
		saltBytes2 = saltFound2.getSalt().clone();

		assertTrue(Arrays.equals(saltBytes1, originalSaltBytes1));
		assertTrue(Arrays.equals(saltBytes2, originalSaltBytes2));
	}

	public void testGetSerializedSalt() throws ApplicationException {

		SaltManagerService saltService = Context.getSaltManagerService();

		Salt saltFound1 = saltService.getSalt(addedSalt1Id);
		log.debug("Found the salt: " + saltFound1);
		Salt saltFound2 = saltService.getSalt(addedSalt2Id);
		log.debug("Found the salt: " + saltFound2);

		byte[] saltBytes1x = saltFound1.getSalt().clone();
		byte[] saltBytes2x = saltFound2.getSalt().clone();
		
		assertTrue(Arrays.equals(saltBytes1, saltBytes1x));
		assertTrue(Arrays.equals(saltBytes2, saltBytes2x));
	}

}
