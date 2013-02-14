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
import org.openhie.openempi.model.Key;

public class KeyServiceTest extends BaseServiceTestCase
{
	static Long addedKeyId = 0L;
	static byte[] originalKeyPart1 = null;
	static byte[] originalKeyPart2 = null;
	static byte[] originalKeyPart3 = null;
	static byte[] originalKeyPart4 = null;
	static byte[] originalKeyPart5 = null;
	static byte[] originalKeyPart6 = null;
	static byte[] keyPart1 = null;
	static byte[] keyPart2 = null;
	static byte[] keyPart3 = null;
	static byte[] keyPart4 = null;
	static byte[] keyPart5 = null;
	static byte[] keyPart6 = null;

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

	public void testAddKey() throws ApplicationException {
		KeyManagerService keyService = Context.getKeyManagerService();
		
		Key key = keyService.addKey();
		addedKeyId = key.getId();
		originalKeyPart1 = key.getPublicKeyPart1().clone();
		originalKeyPart2 = key.getPublicKeyPart2().clone();
		originalKeyPart3 = key.getPublicKeyPart3().clone();
		originalKeyPart4 = key.getPrivateKeyPart1().clone();
		originalKeyPart5 = key.getPrivateKeyPart2().clone();
		originalKeyPart6 = key.getPrivateKeyPart3().clone();
	}
	
	public void testGetKey() {

		KeyManagerService keyService = Context.getKeyManagerService();

		Key keyFound = keyService.getKey(addedKeyId);
		log.debug("Found the key: " + keyFound);

		keyPart1 = keyFound.getPublicKeyPart1().clone();
		keyPart2 = keyFound.getPublicKeyPart2().clone();
		keyPart3 = keyFound.getPublicKeyPart3().clone();
		keyPart4 = keyFound.getPrivateKeyPart1().clone();
		keyPart5 = keyFound.getPrivateKeyPart2().clone();
		keyPart6 = keyFound.getPrivateKeyPart3().clone();

		assertTrue(Arrays.equals(keyPart1, originalKeyPart1));
		assertTrue(Arrays.equals(keyPart2, originalKeyPart2));
		assertTrue(Arrays.equals(keyPart3, originalKeyPart3));
		assertTrue(Arrays.equals(keyPart4, originalKeyPart4));
		assertTrue(Arrays.equals(keyPart5, originalKeyPart5));
		assertTrue(Arrays.equals(keyPart6, originalKeyPart6));
	}

	public void testGetSerializedKey() {

		KeyManagerService keyService = Context.getKeyManagerService();

		Key keyFound = keyService.getKey(addedKeyId);
		log.debug("Found the key: " + keyFound);

		byte[] keyPart1x = keyFound.getPublicKeyPart1().clone();
		byte[] keyPart2x = keyFound.getPublicKeyPart2().clone();
		byte[] keyPart3x = keyFound.getPublicKeyPart3().clone();
		byte[] keyPart4x = keyFound.getPrivateKeyPart1().clone();
		byte[] keyPart5x = keyFound.getPrivateKeyPart2().clone();
		byte[] keyPart6x = keyFound.getPrivateKeyPart3().clone();

		assertTrue(Arrays.equals(keyPart1, keyPart1x));
		assertTrue(Arrays.equals(keyPart2, keyPart2x));
		assertTrue(Arrays.equals(keyPart3, keyPart3x));
		assertTrue(Arrays.equals(keyPart4, keyPart4x));
		assertTrue(Arrays.equals(keyPart5, keyPart5x));
		assertTrue(Arrays.equals(keyPart6, keyPart6x));
	}

}
