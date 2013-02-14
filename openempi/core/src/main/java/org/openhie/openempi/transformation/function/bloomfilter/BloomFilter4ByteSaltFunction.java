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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class BloomFilter4ByteSaltFunction extends NgrammingBloomFilterFunctionBase
{
	// We generate the chunk byte[] in advance, because Arrays.copyOfRange clones new byte[] instances
	// That would stress the GC if it was done for every round
	static protected List<byte[]> saltChunks;	// 4 byte salt chunks for the digest runs
	static protected final int chunkByteSize = 4;

	public void initSalt() {
		if (salts == null || salts.size() <= 0)
			super.initSalt();
		// How many rounds we can use one salt for: we break it up to 32 bit (4 byte) parts
		int saltByteSize = salts.get(0).length / 8;
		int chunkTotalCounter = 0;	// How many chunks we added already
		int saltIndex = 0;	// Which is the start position of the seed chunk within the whole salt
		int saltChunkBytePos = 0;	// Which is the start position of the seed chunk within the whole salt
		saltChunks = new ArrayList<byte[]>();
		while (chunkTotalCounter < numberOfSalts) {
			saltChunks.add(Arrays.copyOfRange(salts.get(saltIndex), saltChunkBytePos, saltChunkBytePos + chunkByteSize));
			chunkTotalCounter++;
			saltChunkBytePos += chunkByteSize;
			if (saltChunkBytePos >= saltByteSize - chunkByteSize) {
				saltChunkBytePos = 0;
				saltIndex++;
			}
		}
	}

	/**
	 * Gets the bloom filter bits for a String. Template method pattern.
	 *
	 * @param element is an element to register in the Bloom filter.
	 */
	protected BitArray getBloomFilterBitPart(String element) {
		if (saltChunks == null || saltChunks.size() <= 0)
			initSalt();
		return super.getBloomFilterBitPart(element);
	}

	/**
	 * Template method part: get salt corresponding to the hashRound.
	 *
	 * @param hashRound Which hash round we are at.
	 */
	protected byte[] getSalt(int hashRound)
	{
		return saltChunks.get(hashRound);
	}

}
