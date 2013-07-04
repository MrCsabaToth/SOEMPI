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
