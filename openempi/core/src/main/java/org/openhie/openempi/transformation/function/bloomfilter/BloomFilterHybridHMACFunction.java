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
package org.openhie.openempi.transformation.function.bloomfilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openhie.openempi.Constants;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.transformation.function.HMACFunction;
import org.openhie.openempi.util.BitArray;

/**
 * Implementation of a Bloom-filter which uses n-grams automatically internally
 *
 * Inspired by the BloomFilter-class written by Magnus Skjegstad.
 * http://blog.locut.us/2008/01/12/a-decent-stand-alone-java-bloom-filter-implementation/
 * 
 * Our implementation provides cache-ing of the computed bloom filter parts for n-grams.
 * 
 * Also, using not just the lower four bytes of the digest, but the other bytes also gives
 * performance imrovement. An SHA-256 output provides enough bits to calculate 8 bloom filter bits,
 * using SHA-512 is even better in respect of computation cost, because it is only 20-40% slower
 * per call compared to SHA-1, but is able to provide enough bits for 16 32-bit integers
 * (thus 16 bloom filter bits).
 *
 * @author Csaba Toth <csaba.toth@vanderbilt.edu>
 */
public class BloomFilterHybridHMACFunction extends NgrammingBloomFilterFunctionBase
{
	protected HMACFunction hmac1;
	protected HMACFunction hmac2;
	protected String hmacFunctionName2;
	protected static byte[] salt1;
	protected static byte[] salt2;

	public void init() {
		super.init();
		hmac1 = new HMACFunction();
		hmac1.setHmacFunctionName(hmacFunctionName);
		hmac1.setInputType(FieldTypeEnum.String);
		hmac1.setOutputType(FieldTypeEnum.Blob);
		hmac1.init();
		hmac2 = new HMACFunction();
		hmac2.setHmacFunctionName(hmacFunctionName2);
		hmac2.setInputType(FieldTypeEnum.String);
		hmac2.setOutputType(FieldTypeEnum.Blob);
		hmac2.init();
	}

	public void initSalt() {
		super.initSalt();
		salt1 = Arrays.copyOfRange(salts.get(0), 0, 4);
		salt2 = Arrays.copyOfRange(salts.get(0), 4, 8);
	}

	/**
	 * Perform k number of hashes for every n-gram
	 *
	 * @param nGram is an element/n-gram to register in the Bloom filter.
	 */
	protected BitArray getBitArrayForNGram(String nGram) {
		BitArray bloomFilterBitArray = new BitArray(m);
		Map<String, Object> parameters1 = new HashMap<String, Object>();
		parameters1.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, salt1);
		Map<String, Object> parameters2 = new HashMap<String, Object>();
		parameters2.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, salt2);
		byte[] intermediateNGram = nGram.getBytes(Constants.charset);
		byte[] intermediateNGram1 = (byte[])hmac1.transform(intermediateNGram, parameters1);
		long h1 = getLeastSignificantBytes(intermediateNGram1);
		byte[] intermediateNGram2 = (byte[])hmac2.transform(intermediateNGram, parameters2);
		long h2 = getLeastSignificantBytes(intermediateNGram2);
		for (int hashRound = 0; hashRound < k; hashRound++) {
			long h = h1 + hashRound * h2;
			// Do the modulo computation to acquire which bits to set in the filter
			h = Math.abs(h % m);

			bloomFilterBitArray.set((int)h, true);
		}
		return bloomFilterBitArray;
	}

	/**
	 * Gets the bloom filter bits for a String. Template method pattern.
	 *
	 * @param element is an element to register in the Bloom filter.
	 */
	protected BitArray getBloomFilterBitPart(String element) {
		if (salt1 == null || salt2 == null)
			initSalt();
		return super.getBloomFilterBitPart(element);
	}

	public String getHmacFunctionName2() {
		return hmacFunctionName2;
	}

	public void setHmacFunctionName2(String hmacFunctionName2) {
		this.hmacFunctionName2 = hmacFunctionName2;
	}
}
