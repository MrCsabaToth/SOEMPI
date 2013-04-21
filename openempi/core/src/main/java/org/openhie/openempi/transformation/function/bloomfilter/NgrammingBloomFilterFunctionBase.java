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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.recordlinkage.configuration.BloomfilterSettings;
import org.openhie.openempi.recordlinkage.configuration.KeyServerSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServerService;
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
public abstract class NgrammingBloomFilterFunctionBase extends HMACFunction
{
	static protected boolean staticInitialized = false;
	static protected int ngramSize = 0;	// how many characters an n-gram consists: 2 = bigram, 3 = trigram, etc.
	static protected int defaultM = 0;	// default BloomFilter bit length
	static protected int defaultK = 0;	// default Bloom filter number of runs
	static protected int numberOfSalts = 0;	// Maximum number of salts (that's how many is generated on the KeyServer)
	protected boolean initialized = false;
	protected int m = 0;	// BloomFilter bit length
	protected int k = 0;	// Bloom filter number of runs

	// TODO: later we can speed up initial generation by caching the integer values (before and after modulo m)
	//		 actual bitstream can be generated from these integer lists without actual message digest computation!
	protected Map<String, BitArray> ngramBloomBitsCache;	// Cached bloom filter bits for n-grams
	// TODO: synchronizing these decreases multi-threaded performance
	static protected NgramSequencer ngramSequencer = new NgramSequencer();	// Facility which generates the n-grams for the input string
	static protected List<byte[]> salts = new ArrayList<byte[]>();	// Salts for the digest runs
	protected BitArray bloomFilterValue;	// The calculated bloom filter value for the whole string
	protected Boolean ngramPadding;

	static public void staticInit() {
		if (!staticInitialized) {
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			BloomfilterSettings bloomfilterSettings = privacySettings.getBloomfilterSettings();
			ngramSize = bloomfilterSettings.getNGramSize();
			defaultM = bloomfilterSettings.getDefaultM();
			defaultK = bloomfilterSettings.getDefaultK();
			KeyServerSettings keyServerSettings = privacySettings.getComponentSettings().getKeyServerSettings();
			numberOfSalts = keyServerSettings.getNumberOfSalts();
		}
		staticInitialized = true;
	}

	public void init() {
		super.init();
		if (!initialized) {
			staticInit();
			m = defaultM;
			k = defaultK;
			if (bloomFilterValue == null)
				bloomFilterValue = new BitArray(m);
			if (ngramBloomBitsCache == null)
				ngramBloomBitsCache = new HashMap<String, BitArray>();
		}
		initialized = true;
	}
	
	public void initSalt() {
		init();

		KeyServerService ks = Context.getKeyServerService();
		synchronized (salts) {
			salts = ks.getSalts(numberOfSalts);
			if (salts.size() < numberOfSalts)
				throw new IndexOutOfBoundsException("The number of salt nonces are smaller than the number of hash rounds.");
		}
	}

	@Override
	public Object transform(Object field, Map<String, Object> parameters) {
		return transformString(field, parameters != null ? parameters : new HashMap<String, Object>());
	}

	/**
	 * Calculate a bloom filter for a given string value.
	 * The string is broke down to n-grams,
	 * for every n-gram k number of hash runs are performed
	 * every run results a bit which is set into the m bitlength filter.
	 * 
	 * @param field: String to be consumed
	 */
	@Override
	protected Object stringTransformCore(String field, Map<String, Object> parameters) {
		init();
		m = defaultM;
		if (parameters != null) {
			if (parameters.containsKey("m"))
				m = (Integer)parameters.get("m");
		}
		k = defaultK;
		if (parameters != null) {
			if (parameters.containsKey("k"))
				k = (Integer)parameters.get("k");
		}
		bloomFilterValue = new BitArray(m);
//		bloomFilterValue.clear();

		synchronized (ngramSequencer) {
			ngramSequencer.init((String)field, ngramSize, ngramPadding);
			// Go through every n-gram
			while(ngramSequencer.hasNext()) {
				String ngram = ngramSequencer.next();
				add(ngram);
			}
		}

		return bloomFilterValue;
	}

	/**
	 * Return the bit set used to store the Bloom filter.
	 * @return bit set representing the Bloom filter.
	 */
	public BitArray getBloomFilterValue() {
		return bloomFilterValue;
	}

	/**
	 * Sets all bits to false in the Bloom filter.
	 */
	public void clear() {
		bloomFilterValue.clear();
	}

	/**
	 * Template method part: get salt corresponding to the hashRound.
	 * Cannot be abstract otherwise Spring would have problem to instantiate beans.
	 *
	 * @param hashRound Which hash round we are at.
	 */
	protected byte[] getSalt(int hashRound) {
		throw new UnsupportedOperationException("getSalt is not implemented in the base class");
	}

	/**
	 * Gets the bloom filter bits for a String. Template method pattern.
	 *
	 * @param element is an element to register in the Bloom filter.
	 */
	protected BitArray getBloomFilterBitPart(String element) {
		if (salts == null || salts.size() <= 0)
			initSalt();

		BitArray bloomFilterTemp = null;
		// Cache-ing mechanism
		String lookupStringM = element + "_" + m;
		String lookupStringMK = lookupStringM + "_" + k;
		if (ngramBloomBitsCache.containsKey(lookupStringMK)) {
			bloomFilterTemp = ngramBloomBitsCache.get(lookupStringMK);
		} else {
			bloomFilterTemp = getBitArrayForNGram(element);
			ngramBloomBitsCache.put(lookupStringMK, bloomFilterTemp);
		}
		return bloomFilterTemp;
	}

	/**
	 * Perform k number of hashes for every n-gram
	 *
	 * @param nGram is an element/n-gram to register in the Bloom filter.
	 */
	protected BitArray getBitArrayForNGram(String nGram) {
		BitArray bloomFilterBitArray = new BitArray(m);
		Map<String, Object> parameters = new HashMap<String, Object>();
		byte[] intermediateNGram = nGram.getBytes(Constants.charset);
		for (int hashRound = 0; hashRound < k; hashRound++) {
			parameters.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, getSalt(hashRound));
			intermediateNGram = (byte[])transformByteArray(intermediateNGram, parameters);

			// Truncate the four least significant byte from the digest, and get the integer value
			long h = getLeastSignificantBytes(intermediateNGram);
			// Do the modulo computation to acquire which bits to set in the filter
			h = Math.abs(h % m);

			bloomFilterBitArray.set((int)h, true);
		}
		return bloomFilterBitArray;
	}

	/**
	 * Truncate the four least significant byte from the digest, and get the integer value (in a long variable).
	 *
	 * @param bytes byte array representing a long number.
	 */
	protected long getLeastSignificantBytes(byte[] bytes) {
		long h = 0;
		for (int i = 0; i < 7; i++) {
			h <<= 8;
			h |= ((long) bytes[i]) & 0xFF;
		}
		return h;
	}

	/**
	 * Adds an object to the Bloom filter.
	 *
	 * @param element is an element to register in the Bloom filter.
	 */
	public void add(String element) {
		BitArray bloomFilterTemp = getBloomFilterBitPart(element);
		bloomFilterValue.or(bloomFilterTemp);
	}

	/**
	 * Adds all elements from a Collection to the Bloom filter.
	 * @param c Collection of elements.
	 */
	public void addAll(Collection<String> c) {
		for (String element : c)
			add(element);
	}

	/**
	 * Returns true if the element could have been inserted into the Bloom filter.
	 * Use getFalsePositiveProbability() to calculate the probability of this
	 * being correct.
	 *
	 * @param element element to check.
	 * @return true if the element could have been inserted into the Bloom filter.
	 */
	public boolean contains(String element) {
		BitArray bloomFilterBitPart = getBloomFilterBitPart(element);
		return bloomFilterValue.intersects(bloomFilterBitPart);
	}

	/**
	 * Returns true if all the elements of a Collection could have been inserted
	 * into the Bloom filter. Note, that there's only just certain probability of
	 * the answer being correct.
	 * @param c elements to check.
	 * @return true if all the elements in c could have been inserted into the Bloom filter.
	 */
	public boolean containsAll(Collection<String> c) {
		for (String element : c)
			if (!contains(element))
				return false;
		return true;
	}

	/**
	 * Read a single bit from the Bloom filter.
	 * @param bit the bit to read.
	 * @return true if the bit is set, false if it is not.
	 */
	public boolean getBit(int bit) {
		return bloomFilterValue.get(bit);
	}

	/**
	 * Set a single bit in the Bloom filter.
	 * @param bit is the bit to set.
	 * @param value If true, the bit is set. If false, the bit is cleared.
	 */
	public void setBit(int bit, boolean value) {
		bloomFilterValue.set(bit, value);
	}

	/**
	 * Return the bit set used to store the Bloom filter.
	 * @return bit set representing the Bloom filter.
	 */
	public BitArray getBitArray() {
		return getBloomFilterValue();
	}

	/**
	 * Returns the number of bits in the Bloom filter. Use count() to retrieve
	 * the number of inserted elements.
	 *
	 * @return the size of the bitset used by the Bloom filter.
	 */
	public int size() {
		return bloomFilterValue.byteLength() * 8;
	}

	public Boolean getNgramPadding() {
		return ngramPadding;
	}

	public void setNgramPadding(Boolean ngramPadding) {
		this.ngramPadding = ngramPadding;
	}

}
