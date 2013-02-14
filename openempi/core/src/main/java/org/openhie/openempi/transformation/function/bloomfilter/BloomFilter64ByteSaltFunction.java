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
public class BloomFilter64ByteSaltFunction extends NgrammingBloomFilterFunctionBase
{

	/**
	 * Template method part: get salt corresponding to the hashRound.
	 *
	 * @param hashRound Which hash round we are at.
	 */
	protected byte[] getSalt(int hashRound)
	{
		return salts.get(hashRound);
	}

}
