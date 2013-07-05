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
package org.openhie.openempi.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import junit.framework.TestCase;

public class DiffieHellmanKeyExchangeTest extends TestCase {

	public DiffieHellmanKeyExchangeTest(String name) {
        super(name);
    }

	void testProtocol() {
		SecureRandom rnd = new SecureRandom();
	
	    DiffieHellmanKeyExchange dhkeAlice = new DiffieHellmanKeyExchange(Math.abs(rnd.nextInt()));
	    BigInteger A = dhkeAlice.computePublicKey();
	    byte[] alicePubKeyEnc = A.toByteArray();
	
	    DiffieHellmanKeyExchange dhkeBob = new DiffieHellmanKeyExchange(Math.abs(rnd.nextInt()));
	    BigInteger B = dhkeBob.computePublicKey();
	    byte[] bobPubKeyEnc = B.toByteArray();
	
	    int sharedSecretAtAlice = dhkeAlice.computeSharedSecret(bobPubKeyEnc);
        System.out.println("Shared secret at Alice=" + sharedSecretAtAlice);

        int sharedSecretAtBob = dhkeBob.computeSharedSecret(alicePubKeyEnc);
        System.out.println("Shared secret at Bob=" + sharedSecretAtBob);

        assertEquals(sharedSecretAtAlice, sharedSecretAtBob);
	}
}
