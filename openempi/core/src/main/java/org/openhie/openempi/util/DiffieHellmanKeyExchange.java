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

public class DiffieHellmanKeyExchange
{
	public static final BigInteger P_DH_PARAM =
			new BigInteger( "96638233045864084518231195061482444508946745418794" +
							"32145426376197328661494156721783116267581866720348" +
							"56569235188055781021126862722394033261228353790026" +
							"74242252342519550057166057717181586900064534416478" +
							"66989456980848803906247815161337494391373137153839" +
							"33939639741821762833572485404071453236110146128678" +
							"95640659");
	public static final BigInteger G_DH_PARAM =
			new BigInteger( "15747374786246668558215402439365766861880018910353" +
							"73114168675609352822845701221083803231254972889042" +
							"07855156227788690363403072390960974948248690468386" +
							"01971993935506051322384408989957908892058425561018" +
							"77319073471747739646901162574581078537710124683072" +
							"39204369669727780274795620256455014913194644458689" +
							"982153231");

	//private int secret;
	private BigInteger secretBigInt;
	private BigInteger publicKey;

	public DiffieHellmanKeyExchange() {
	}
	
	public DiffieHellmanKeyExchange(int secret) {
		//this.secret = secret;
		this.secretBigInt = BigInteger.valueOf(secret);
	}

	public BigInteger computePublicKey() {
        publicKey = G_DH_PARAM.modPow(secretBigInt, P_DH_PARAM);
        return publicKey;
	}

	public int computeSharedSecret(byte[] otherPublicKeyEnc) {
		BigInteger otherPublicKey = new BigInteger(otherPublicKeyEnc);
        BigInteger sharedSecretBigInt = otherPublicKey.modPow(secretBigInt, DiffieHellmanKeyExchange.P_DH_PARAM);
        int sharedSecret = sharedSecretBigInt.mod(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
        return sharedSecret;
	}
}
