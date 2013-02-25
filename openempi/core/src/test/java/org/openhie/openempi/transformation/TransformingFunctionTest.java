/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.transformation;

import java.util.HashMap;
import java.util.Map;

import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.transformation.function.TransformationFunction;
import org.openhie.openempi.util.BitArray;

import edu.emory.mathcs.backport.java.util.Arrays;

public class TransformingFunctionTest extends BaseServiceTestCase
{
	private String[] strings = {
		new String("Odysseas"),
		new String("Odysseus"),
		new String("John"),
		new String("Bob"),
		new String("Frank"),
		new String("James"),
		new String("Sophia"),
		new String("Sofia"),
		new String("Jensen"),
		new String("Jonassen"),
		new String("Jonasen"),
		new String("Jacobsen"),
		new String("Jakobsen"),
		new String("Fischer"),
		new String("Fisher"),
		new String("Gustavsen"),
		new String("Gustafsson"),
		new String("Gustafsen"),
		new String("Handeland"),
		new String("Javier"),
		new String("Havier"),
		new String("Martinez"),
		new String("Marteenez"),
		new String("testing"),
		new String("test"),

		new String("0"),
		new String("3"),
		new String("0"),
		new String("0d"),
		new String("3d"),
		new String("0d"),
		new String("0.0f"),
		new String("3.0f"),
		new String("0.0f"),
		new String("0.0d"),
		new String("3.0d"),
		new String("0.0d"),
		new String("0.5e-10"),
		new String("0.5e10")
	};

	private byte[][] bitStreams = {
		new byte[]{0,1,2,3,4,5,6,7,8,9},
		new byte[]{10,1,2,3,4,5,6,7,8,9},
		new byte[]{0,1,2,3,4,9,8,7,6,5},
		new byte[]{9,8,7,6,5,4,3,2,1,0},
		new byte[]{0,0,0},
		new byte[]{-127,-127,-127},
		null
	};

	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
		KeyServerService ks = Context.getKeyServerService();
		ks.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
	}

	public void testDistanceMetrics() {
		TransformationService transformationService = Context.getTransformationService();
		TransformationFunctionType[] list = transformationService.getTransformationFunctionTypes();
		byte[] signingKey = new byte[]{0,1,2,3,4,5,6,7,8,9};
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, signingKey);
		for (TransformationFunctionType tft : list) {
			TransformationFunction trafo = tft.getTransformationFunction();
			System.out.println("=== Testing " + trafo.getInputType().toString() + " Metric " + tft.getName() + " ===");
			if (trafo.getInputType() == FieldType.FieldTypeEnum.String || trafo.getInputType() == FieldType.FieldTypeEnum.Any) {
				for (String str : strings) {
					Object output1 = trafo.transform(str, parameters);
					System.out.println(tft.getName() + " transformation of " + str + " is " + output1 + " (1st time)");
					Object output2 = trafo.transform(str, parameters);
					System.out.println(tft.getName() + " transformation of " + str + " is " + output2 + " (2nd time)");
					if (output1 == null) {
						assertTrue(output2 == null);
					} else {
						if (trafo.getOutputType() == FieldType.FieldTypeEnum.String) {
							String output1str = (String)output1;
							String output2str = (String)output2;
							assertTrue(output1str.equals(output2str));
						} else {
							if (output1 instanceof BitArray) {	// BF
								BitArray output1bitArray = (BitArray)output1;
								BitArray output2bitArray = (BitArray)output2;
								assertTrue(output1bitArray.equals(output2bitArray));
							} else {	// HMAC
								byte[] output1byteArray = (byte[])output1;
								byte[] output2byteArray = (byte[])output2;
								assertTrue(Arrays.equals(output1byteArray, output2byteArray));								
							}
						}
					}
				}
			}
			if (trafo.getInputType() != FieldType.FieldTypeEnum.String || trafo.getInputType() == FieldType.FieldTypeEnum.Any) {
				for (byte[] bitStream : bitStreams) {
					System.out.println(tft.getName() + " transformation of " + bitStream + " is " + trafo.transform(bitStream, null));
				}
			}
		}
	}
	
	public void testHMACs() {
		TransformationService transformationService = Context.getTransformationService();
		FunctionField hmacMD5 = new FunctionField("HMACMD5Function");
		FunctionField hmacSHA1 = new FunctionField("HMACSHA1Function");
		FunctionField hmacSHA256 = new FunctionField("HMACSHA256Function");
		String nonEmptyKey = new String("key");
		String nonEmptyString = new String("The quick brown fox jumps over the lazy dog");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, nonEmptyKey);

		byte[] hmacMD5NonEmptyValue = (byte[])transformationService.transform(hmacMD5, nonEmptyString, parameters);
		System.out.println("HMAC MD5 non-empty: " + hmacMD5NonEmptyValue);
		// [-128, 7, 7, 19, 70, 62, 119, 73, -71, 12, 45, -62, 73, 17, -30, 117]
		// 0x 80 07 07  13  46  3e   77  49   b9  0c  2d   c2  49  11   e2   75
		byte[] hmacMD5NonEmptyCheck = new byte[] { -128, 7, 7, 19, 70, 62, 119, 73, -71, 12, 45, -62, 73, 17, -30, 117 };
		assertTrue(Arrays.equals(hmacMD5NonEmptyValue, hmacMD5NonEmptyCheck));

		byte[] hmacSHA1NonEmptyValue = (byte[])transformationService.transform(hmacSHA1, nonEmptyString, parameters);
		System.out.println("HMAC SHA1 non-empty: " + hmacSHA1NonEmptyValue);
		// [-34, 124, -101, -123, -72, -73, -118, -90, -68, -118, 122, 54, -9, 10, -112, 112, 28, -99, -76, -39]
		// 0xde   7c    9b    85   b8   b7    8a   a6   bc    8a   7a  36  f7  0a    90   70  1c   9d   b4   d9
		byte[] hmacSHA1NonEmptyCheck = new byte[] { -34, 124, -101, -123, -72, -73, -118, -90, -68, -118, 122, 54, -9, 10, -112, 112, 28, -99, -76, -39 };
		assertTrue(Arrays.equals(hmacSHA1NonEmptyValue, hmacSHA1NonEmptyCheck));

		byte[] hmacSHA256NonEmptyValue = (byte[])transformationService.transform(hmacSHA256, nonEmptyString, parameters);
		System.out.println("HMAC SHA256 non-empty: " + hmacSHA256NonEmptyValue);
		// [-9, -68, -125, -12, 48, 83, -124, 36, -79, 50, -104, -26, -86, 111, -79, 67, -17, 77, 89, -95, 73, 70, 23, 89, -105, 71, -99, -68, 45, 26, 60, -40]
		// 0xf7  bc    83   f4  30  53    84  24   b1  32    98   e6   aa   6f   b1  43   ef  4d  59   a1  49  46  17  59    97  47   9d   bc  2d  1a  3c   d8
		byte[] hmacSHA256NonEmptyCheck = new byte[] { -9, -68, -125, -12, 48, 83, -124, 36, -79, 50, -104, -26, -86, 111, -79, 67, -17, 77, 89, -95, 73, 70, 23, 89, -105, 71, -99, -68, 45, 26, 60, -40 };
		assertTrue(Arrays.equals(hmacSHA256NonEmptyValue, hmacSHA256NonEmptyCheck));
	}
	
}
