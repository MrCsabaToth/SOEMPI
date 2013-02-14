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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.transformation.function.bloomfilter.BloomFilter4ByteSaltFunction;
import org.openhie.openempi.util.BitArray;

public class BloomFilterFunctionTest extends BaseServiceTestCase
{
	static Integer m = 1000;
	static boolean sampleInited = false;

	protected NgrammingBloomFilterFunctionBase getBloomFilterFunction(boolean sixtyFourBytes, boolean padded) {
		NgrammingBloomFilterFunctionBase bff = null;
		if (sixtyFourBytes)
			bff = new BloomFilter64ByteSaltFunction();
		else
			bff = new BloomFilter4ByteSaltFunction();
		bff.setNgramPadding(padded);
		bff.setHmacFunctionName("HmacSHA256");
		bff.setInputType(FieldTypeEnum.String);
		bff.setOutputType(FieldTypeEnum.Blob);
		bff.init();
		return bff;
	}

	protected NgrammingBloomFilterFunctionBase getBloomFilterHybridHMACFunction(boolean padded) {
		BloomFilterHybridHMACFunction bfhh = new BloomFilterHybridHMACFunction();
		bfhh.setNgramPadding(padded);
		bfhh.setHmacFunctionName("HmacMD5");
		bfhh.setHmacFunctionName2("HmacSHA1");
		bfhh.setInputType(FieldTypeEnum.String);
		bfhh.setOutputType(FieldTypeEnum.Blob);
		bfhh.init();
		return bfhh;
	}

	List<NgrammingBloomFilterFunctionBase> getBloomFilterFunctions() {
		List<NgrammingBloomFilterFunctionBase> bloomFilterFunctions = new ArrayList<NgrammingBloomFilterFunctionBase>();
		bloomFilterFunctions.add(getBloomFilterFunction(true, true));
		bloomFilterFunctions.add(getBloomFilterFunction(true, false));
		bloomFilterFunctions.add(getBloomFilterFunction(false, true));
		bloomFilterFunctions.add(getBloomFilterFunction(false, false));
		bloomFilterFunctions.add(getBloomFilterHybridHMACFunction(true));
		bloomFilterFunctions.add(getBloomFilterHybridHMACFunction(false));
		return bloomFilterFunctions;
	}

    /**
     * A common initialization function for the tests.
     */
	public void sampleInit()
	{
		if (!sampleInited) {
			sampleInited = true;
			Context.startup();
			KeyServerService ks = Context.getKeyServerService();
			ks.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
		}
	}

	/**
	 * Test of equality of BloomFilters.
	 * @throws Exception
	 */
	@Test
	public void testEquals() throws Exception {
		System.out.println("equals");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances1 = getBloomFilterFunctions();
		List<NgrammingBloomFilterFunctionBase> instances2 = getBloomFilterFunctions();

		for (int i = 0; i < 2; i++) {
			String val = UUID.randomUUID().toString();
			for (NgrammingBloomFilterFunctionBase bf : instances1) {
				bf.add(val);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances2) {
				bf.add(val);
			}
		}

		// The two same instances should be equal now
		Iterator<NgrammingBloomFilterFunctionBase> iter1 = instances1.iterator();
		Iterator<NgrammingBloomFilterFunctionBase> iter2 = instances2.iterator();
		while(iter1.hasNext() && iter2.hasNext()) {
			assertTrue(iter1.next().getBloomFilterValue().equals(iter2.next().getBloomFilterValue()));
		}
		// Padded and unpadded versions should be equal, because we used low-level add, and they won't differ at this level
		// We didn't use the nGramSequencer, where they'd differ in the leadin and the leadout n-gram
		iter1 = instances1.iterator();
		while(iter1.hasNext()) {
			NgrammingBloomFilterFunctionBase paddedBF = iter1.next();
			NgrammingBloomFilterFunctionBase unpaddedBF = iter1.next();
			BitArray paddedBFValue = paddedBF.getBloomFilterValue();
			BitArray unpaddedBFValue = unpaddedBF.getBloomFilterValue();
			assertTrue(paddedBFValue.equals(unpaddedBFValue));
		}
		iter2 = instances2.iterator();
		while(iter2.hasNext()) {
			NgrammingBloomFilterFunctionBase paddedBF = iter2.next();
			NgrammingBloomFilterFunctionBase unpaddedBF = iter2.next();
			BitArray paddedBFValue = paddedBF.getBloomFilterValue();
			BitArray unpaddedBFValue = unpaddedBF.getBloomFilterValue();
			assertTrue(paddedBFValue.equals(unpaddedBFValue));
		}

		for (NgrammingBloomFilterFunctionBase bf : instances1) {
			bf.add("Another entry"); // make instances1 and instances2 different
		}

		// Now the two same type instances should differ
		iter1 = instances1.iterator();
		iter2 = instances2.iterator();
		while(iter1.hasNext() && iter2.hasNext()) {
			assertFalse(iter1.next().getBloomFilterValue().equals(iter2.next().getBloomFilterValue()));
		}

		for (NgrammingBloomFilterFunctionBase bf : instances1) {
			bf.clear();
		}
		for (NgrammingBloomFilterFunctionBase bf : instances2) {
			bf.clear();
		}

		// They should be all empty now
		iter1 = instances1.iterator();
		iter2 = instances2.iterator();
		while(iter1.hasNext() && iter2.hasNext()) {
			assertTrue(iter1.next().getBloomFilterValue().equals(iter2.next().getBloomFilterValue()));
		}

		for (int i = 0; i < 2; i++) {
			String val = UUID.randomUUID().toString();	 
			for (NgrammingBloomFilterFunctionBase bf : instances1) {
				bf.add(val);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances2) {
				bf.add(val);
			}
		}

		// The two same instances should be equal now again
		iter1 = instances1.iterator();
		iter2 = instances2.iterator();
		while(iter1.hasNext() && iter2.hasNext()) {
			assertTrue(iter1.next().getBloomFilterValue().equals(iter2.next().getBloomFilterValue()));
		}
		// Padded and unpadded should be equal just as previous time: low level add works the same, we didn't use the nGramSequencer
		iter1 = instances1.iterator();
		while(iter1.hasNext()) {
			NgrammingBloomFilterFunctionBase paddedBF = iter1.next();
			NgrammingBloomFilterFunctionBase unpaddedBF = iter1.next();
			BitArray paddedBFValue = paddedBF.getBloomFilterValue();
			BitArray unpaddedBFValue = unpaddedBF.getBloomFilterValue();
			assertTrue(paddedBFValue.equals(unpaddedBFValue));
		}
		iter2 = instances2.iterator();
		while(iter1.hasNext()) {
			NgrammingBloomFilterFunctionBase paddedBF = iter2.next();
			NgrammingBloomFilterFunctionBase unpaddedBF = iter2.next();
			BitArray paddedBFValue = paddedBF.getBloomFilterValue();
			BitArray unpaddedBFValue = unpaddedBF.getBloomFilterValue();
			assertTrue(paddedBFValue.equals(unpaddedBFValue));
		}
	}

	/**
	 * Test of add method, of class BloomFilterFunction.
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {
		System.out.println("add");
		sampleInit();

		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		for (int i = 0; i < 5; i++) {
			String val = UUID.randomUUID().toString();
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.add(val);
				assertTrue(bf.contains(val));
			}
		}
	}

	/**
	 * Test of addAll method, of class BloomFilterFunction.
	 * @throws Exception
	 */
	@Test
	public void testAddAll() throws Exception {
		System.out.println("addAll");
		sampleInit();
		Vector<String> v = new Vector<String>();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		for (int i = 0; i < 5; i++)
			v.add(UUID.randomUUID().toString());

		for (NgrammingBloomFilterFunctionBase bf : instances) {
			bf.addAll(v);
		}

		for (int i = 0; i < 5; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertTrue(bf.contains(v.get(i)));
			}
		}

		for (NgrammingBloomFilterFunctionBase bf : instances) {
			assertFalse(bf.contains(UUID.randomUUID().toString()));
		}
	}

	/**
	 * Test of contains method, of class BloomFilterFunction.
	 * @throws Exception
	 */
	@Test
	public void testContains() throws Exception {
		System.out.println("contains");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		for (int i = 0; i < 5; i++) {
			String numberStr = Integer.toBinaryString(i);
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.add(numberStr);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.contains(numberStr);
			}
		}

		for (NgrammingBloomFilterFunctionBase bf : instances) {
			assertFalse(bf.contains(UUID.randomUUID().toString()));
		}
	}

	/**
	 * Test of containsAll method, of class BloomFilterFunction.
	 * @throws Exception
	 */
	@Test
	public void testContainsAll() throws Exception {
		System.out.println("containsAll");
		sampleInit();
		Vector<String> v = new Vector<String>();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		for (int i = 0; i < 5; i++) {
			String randomStr = UUID.randomUUID().toString();
			v.add(randomStr);
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.add(randomStr);
			}
		}

		for (NgrammingBloomFilterFunctionBase bf : instances) {
			assertTrue(bf.containsAll(v));
		}
	}

	/**
	 * Test of getBit method, of class BloomFilterFunction.
	 */
	@Test
	public void testGetBit() {
		System.out.println("getBit");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();
		Random r = new Random();

		for (int i = 0; i < 100; i++) {
			boolean b = r.nextBoolean();
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.setBit(i, b);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), b);
			}
		}
	}

	/**
	 * Test of setBit method, of class BloomFilterFunction.
	 */
	@Test
	public void testSetBit() {
		System.out.println("setBit");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		// false by default
		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), false);
			}
		}

		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.setBit(i, true);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), true);
			}
		}

		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.setBit(i, false);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), false);
			}
		}

		// false again
		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), false);
			}
		}

		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.setBit(i, true);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), true);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				bf.setBit(i, false);
			}
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), false);
			}
		}

		// false again
		for (int i = 0; i < m; i++) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				assertSame(bf.getBit(i), false);
			}
		}
	}

	/**
	 * Test of size method, of class BloomFilterFunction.
	 */
	@Test
	public void testSize() {
		System.out.println("size");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();
		for (NgrammingBloomFilterFunctionBase bf : instances) {
			assertEquals((Integer)bf.size(), m);
		}
	}

    /**
     * A general test of class BloomFiltersFunction.
     * @throws Exception
     */
	@Test
	public void testBloomFilterFunction() throws Exception
	{
		System.out.println("general");
		sampleInit();
		List<NgrammingBloomFilterFunctionBase> instances = getBloomFilterFunctions();

		List<String> names = new ArrayList<String>();
		names.add("BERRY");
		names.add("STRICKLAND");
		names.add("TRAVIS");
		names.add("BROOKS");
		names.add("SUBASI");
		names.add("CAREY");
		names.add("TEACHEY");
		names.add("PATE");
		names.add("WARD");
		names.add("BRIGGS");
		names.add("The quick brown fox jumps over the lazy dog. Plus I write another sentence to get it very loong!");

		for(String name : names) {
			for (NgrammingBloomFilterFunctionBase bf : instances) {
				BitArray bloomFilter = (BitArray)bf.transform(name, null);
				System.out.println("BF bits for " + name + ": " + bloomFilter.toString());
			}
		}
	}

    /**
     * Test of parameters given to the class of BloomFiltersFunction.
     * @throws Exception
     */
	@Test
	public void testBloomFilterFunctionParameters() throws Exception
	{
		System.out.println("general");
		sampleInit();
		
		List<NgrammingBloomFilterFunctionBase> instances1 = getBloomFilterFunctions();
		List<NgrammingBloomFilterFunctionBase> instances2 = getBloomFilterFunctions();
		List<NgrammingBloomFilterFunctionBase> instances3 = getBloomFilterFunctions();
		List<NgrammingBloomFilterFunctionBase> instances4 = getBloomFilterFunctions();
		List<NgrammingBloomFilterFunctionBase> instances5 = getBloomFilterFunctions();
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("m", (Integer)900);
		params1.put("k", (Integer)30);
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("m", (Integer)900);
		params2.put("k", (Integer)40);
		Map<String, Object> params3 = new HashMap<String, Object>();
		params3.put("m", (Integer)1000);
		params3.put("k", (Integer)30);
		Map<String, Object> params4 = new HashMap<String, Object>();
		params4.put("m", (Integer)1000);
		params4.put("k", (Integer)40);

		List<String> names = new ArrayList<String>();
		names.add("BERRY");
		names.add("STRICKLAND");
		names.add("TRAVIS");
		names.add("BROOKS");
		names.add("SUBASI");
		names.add("CAREY");
		names.add("TEACHEY");
		names.add("PATE");
		names.add("WARD");
		names.add("BRIGGS");
		names.add("The quick brown fox jumps over the lazy dog. Plus I write another sentence to get it very loong!");

		for(String name : names) {
			Iterator<NgrammingBloomFilterFunctionBase> iter1 = instances1.iterator();
			Iterator<NgrammingBloomFilterFunctionBase> iter2 = instances2.iterator();
			Iterator<NgrammingBloomFilterFunctionBase> iter3 = instances3.iterator();
			Iterator<NgrammingBloomFilterFunctionBase> iter4 = instances4.iterator();
			Iterator<NgrammingBloomFilterFunctionBase> iter5 = instances5.iterator();
			while(iter1.hasNext()) {
				BitArray bloomFilter1 = (BitArray)iter1.next().transform(name, params1);
				BitArray bloomFilter2 = (BitArray)iter2.next().transform(name, params2);
				BitArray bloomFilter3 = (BitArray)iter3.next().transform(name, params3);
				BitArray bloomFilter4 = (BitArray)iter4.next().transform(name, params4);
				BitArray bloomFilter5 = (BitArray)iter5.next().transform(name, null);
				System.out.println("Bloomfilter bits for (900,30) " + name + ": " + bloomFilter1.toString());
				System.out.println("Bloomfilter bits for (900,40) " + name + ": " + bloomFilter2.toString());
				System.out.println("Bloomfilter bits for (1000,30) " + name + ": " + bloomFilter3.toString());
				System.out.println("Bloomfilter bits for (1000,40) " + name + ": " + bloomFilter4.toString());
				System.out.println("Bloomfilter bits for default " + name + ": " + bloomFilter5.toString());
				assertFalse(bloomFilter1.equals(bloomFilter2));
				assertFalse(bloomFilter1.equals(bloomFilter3));
				assertFalse(bloomFilter1.equals(bloomFilter4));
				assertFalse(bloomFilter2.equals(bloomFilter3));
				assertFalse(bloomFilter2.equals(bloomFilter4));
				assertFalse(bloomFilter3.equals(bloomFilter4));
			}
		}
	}

}
