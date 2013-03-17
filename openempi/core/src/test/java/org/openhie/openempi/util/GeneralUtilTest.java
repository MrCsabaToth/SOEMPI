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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.stringcomparison.StringComparisonService;

public class GeneralUtilTest extends BaseServiceTestCase {
	//~ Instance fields ========================================================

	private final Log log = LogFactory.getLog(GeneralUtilTest.class);

	private LeanRecordPair pair;
	private Person person;
	private Person personOther;

	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}

	public void testConcatByteArraysSimple() {
		byte[] emptyArray = new byte[0];
		byte[] testArray1 = { 0, 1, 2, 3, 4, 5, 6, 7 };
		byte[] testArray2 = { 8, 9, 10, 11, 12, 13, 14, 15 };

		byte[] res1 = GeneralUtil.concatByteArraysSimple(emptyArray, testArray1);
		assertEquals(Arrays.equals(res1, testArray1), true);
		byte[] res2 = GeneralUtil.concatByteArraysSimple(testArray2, emptyArray);
		assertEquals(Arrays.equals(res2, testArray2), true);
		byte[] res3 = GeneralUtil.concatByteArraysSimple(testArray1, testArray2);
		assertEquals(res3.length, testArray1.length + testArray2.length);
		for(int i = 0; i < 16; i++)
			assertEquals(res3[i], i);
	}

	public void testConcatByteArrays() {
		byte[] testArray1 = { 0, 1 };
		byte[] testArray2 = { 4, 5, 6, 7 };
		byte[] res = GeneralUtil.concatByteArrays(testArray1, testArray2, 2, 2);
		assertEquals(res.length, 4);
		assertEquals(res[0], 0);
		assertEquals(res[1], 1);
		assertEquals(res[2], 6);
		assertEquals(res[3], 7);
	}

	private void createTestData() {
		UserDao userDao = (UserDao)applicationContext.getBean("userDao");
		User user = (User) userDao.loadUserByUsername("admin");

		pair = new LeanRecordPair(1L, 1L);
		person = new Person(1L, user, new java.util.Date());
		person.setAttribute("fnl", "Csaba");
		person.setAttribute("lnl", "Toth");
		person.setAttribute("fnblml", new byte[] { 0, 1, 2, 3, 4 });
		personOther = new Person(1L, user, new java.util.Date());
		personOther.setAttribute("fnr", "Attila");
		personOther.setAttribute("lnr", "Toth");
		personOther.setAttribute("fnblmr", new byte[] { 0, 1, 2, 3, 5 });
		StringComparisonService comparisonService = Context.getStringComparisonService();
		List<MatchField> matchFields = new ArrayList<MatchField>();
		MatchField matchField1 = new MatchField();
		matchField1.setAgreementProbability(0.9f);
		matchField1.setDisagreementProbability(0.1f);
		matchField1.setLeftFieldName("fnl");
		matchField1.setRightFieldName("fnr");
		matchField1.setMatchThreshold(0.85f);
		FunctionField jaroWinklerComparator = new FunctionField("JaroWinkler");
		matchField1.setComparatorFunction(jaroWinklerComparator);
		matchFields.add(matchField1);
		MatchField matchField2 = new MatchField();
		matchField2.setAgreementProbability(0.9f);
		matchField2.setDisagreementProbability(0.1f);
		matchField2.setLeftFieldName("lnl");
		matchField2.setRightFieldName("lnr");
		matchField2.setMatchThreshold(0.85f);
		matchField2.setComparatorFunction(jaroWinklerComparator);
		matchFields.add(matchField2);
		MatchField matchField3 = new MatchField();
		matchField3.setAgreementProbability(0.9f);
		matchField3.setDisagreementProbability(0.1f);
		matchField3.setLeftFieldName("fnblml");
		matchField3.setRightFieldName("fnblmr");
		matchField3.setMatchThreshold(0.85f);
		FunctionField diceBinaryComparator = new FunctionField("DiceBinary");
		matchField3.setComparatorFunction(diceBinaryComparator);
		matchFields.add(matchField3);

		ComparisonVector comparisonVector =
				GeneralUtil.scoreRecordPair(person, personOther, comparisonService, matchFields);
		pair.setComparisonVector(comparisonVector);
	}

	public void testScoreRecordPair() {
		createTestData();
		ComparisonVector cv = pair.getComparisonVector();
		System.out.println(cv);
		double[] scores = cv.getScores();
		assertEquals(scores.length, 3);
		assertEquals(scores[0], 0.5777777777777778);
		assertEquals(scores[1], 1.0);
		assertEquals(scores[2], 0.4545454680919647);
		int[] binaryScores = cv.getBinaryVector();
		assertEquals(binaryScores.length, 3);
		assertEquals(binaryScores[0], 0);
		assertEquals(binaryScores[1], 1);
		assertEquals(binaryScores[2], 0);
	}

	public void testConstructPersonLink() {
		createTestData();
		Integer personMatchId = 1;
		pair.setWeight(0.7);
		Integer linkState = 4;

		PersonLink pl1 = GeneralUtil.constructPersonLink(personMatchId, 1L, 2L,
				person.getUserCreatedBy(), pair.getWeight(), linkState);
		assertEquals((Integer)pl1.getPersonMatchId(), personMatchId);
		assertEquals((Long)pl1.getLeftPersonId(), (Long)1L);
		assertEquals((Long)pl1.getRightPersonId(), (Long)2L);
		assertEquals(pl1.getCreatorId(), person.getUserCreatedBy().getId());
		assertEquals((Double)pl1.getWeight(), pair.getWeight());
		assertEquals((Integer)pl1.getLinkState(), linkState);

		PersonLink pl2 = GeneralUtil.constructPersonLink(personMatchId, pair, linkState);
		assertEquals(pl2.getBinaryVector(), pair.getComparisonVector().getBinaryVectorString());
		assertEquals(pl2.getContinousVector(), pair.getComparisonVector().getScoreVectorString());
	}

}
