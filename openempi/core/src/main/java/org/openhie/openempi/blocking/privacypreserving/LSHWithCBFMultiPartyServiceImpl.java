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
package org.openhie.openempi.blocking.privacypreserving;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.hibernate.UniversalDaoHibernate;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.util.SerializationUtil;

public class LSHWithCBFMultiPartyServiceImpl extends PrivacyPreservingBlockingBase
{
	private static final String LEAN_RECORDPAIRS_FILE_NAME = "LeanRecordPairs.ser";

	public void init() {
		log.trace("Initializing the Blocking Bypass Service");
	}

	@SuppressWarnings("unchecked")
	public void getRecordPairs(Object blockingServiceCustomParameters, String matchingServiceTypeName,
			Object matchingServiceCustomParameters, String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, List<LeanRecordPair> pairs,
			boolean emOnly, FellegiSunterParameters fellegiSunterParameters) throws ApplicationException {
		List<BloomFilterBitStat> bitStats = null;
		String fileRepositoryDirectory =
			Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
		String filename = fileRepositoryDirectory + "/" + BLOOMFILTER_STATISTICS_FILE_NAME;
		File file = new File(filename);
//		if (file.exists()) {
//			BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(configDirectory);
//			bitStats = bloomFilterStatistics.getBloomFilterBitStats();
//		} else {
			List<MatchPairStat> matchPairStats = (List<MatchPairStat>)blockingServiceCustomParameters;
			calculateBitStatistics(matchPairStats, leftTableName, rightTableName);
			if (file.exists()) {
				BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(fileRepositoryDirectory);
				bitStats = bloomFilterStatistics.getBloomFilterBitStats();
			} else {
				return;
			}
//		}

		Collections.sort(bitStats, new BloomFilterBitStatComparatorByImportance());
		Integer numberOfImportantBits =
			Context.getConfiguration().getPrivacyPreservingBlockingSettings().getNumberOfBlockingBits();
		List<BloomFilterBitStat> importantBits = new ArrayList<BloomFilterBitStat>();
		for(int i = 0; i < numberOfImportantBits; i++) {
			importantBits.add(bitStats.get(bitStats.size() - 1 - i));
		}
		Collections.sort(importantBits, new BloomFilterBitStatComparatorByBloomFilterIndex());

		getRecordPairs(importantBits, pairs, null, leftTableName, rightTableName,
				leftOriginalIdFieldName, rightOriginalIdFieldName, emOnly, fellegiSunterParameters);
	}

	public List<Person> populateList(List<Person> personList, List<Long> personIdList) {
		List<Person> populatedList = new ArrayList<Person>();
		Map<Long, Person> personMap = new HashMap<Long, Person>();
		for(Person p : personList)
			personMap.put(p.getPersonId(), p);
		for(Long id : personIdList)
			populatedList.add(personMap.get(id));
		return populatedList;
	}

	public void calculateBitStatistics(List<MatchPairStat> matchPairStats,  String leftTableName,
			String rightTableName) throws ApplicationException {
		String fileRepositoryDirectory =
			Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
//		String filename = configDirectory + "/" + Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME;
//		File file = new File(filename);

//		filename = configDirectory + "/" + BLOOMFILTER_STATISTICS_FILE_NAME;
//		file = new File(filename);
//		if (file.exists()) {
//			BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(configDirectory);
//			bitStats = bloomFilterStatistics.getBloomFilterBitStats();
//		} else {
			PersonQueryService personQueryService = Context.getPersonQueryService();
	
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
			List<Person> leftSamplePersons = personQueryService.getPersonsPaged(leftTableName, fieldNames, 1L, 1);
			List<Person> rightSamplePersons = personQueryService.getPersonsPaged(rightTableName, fieldNames, 1L, 1);
			if (leftSamplePersons == null)
				throw new ApplicationException("Cannot fetch sample person from left dataset to measure CBF length");
			if (leftSamplePersons.size() != 1)
				throw new ApplicationException("Cannot fetch sample person from left dataset to measure CBF length");
			if (rightSamplePersons == null)
				throw new ApplicationException("Cannot fetch sample person from right dataset to measure CBF length");
			if (rightSamplePersons.size() != 1)
				throw new ApplicationException("Cannot fetch sample person from right dataset to measure CBF length");
			byte[] leftSampleCBF = (byte[]) leftSamplePersons.get(0).getAttribute(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
			byte[] rightSampleCBF = (byte[]) rightSamplePersons.get(0).getAttribute(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
			if (leftSampleCBF.length <= 0 || rightSampleCBF.length <= 0)
				throw new ApplicationException("Bogus CBF length: left = " + leftSampleCBF.length + ", right = " + rightSampleCBF.length);
			if (leftSampleCBF.length != rightSampleCBF.length)
				throw new ApplicationException("CBF length mismatch: left = " + leftSampleCBF.length + ", right = " + rightSampleCBF.length);
			int bloomFilterByteSize = leftSampleCBF.length;
			int bloomFilterSize = bloomFilterByteSize * 8;
	
			// Calculate Empty Bloom Filter
			byte[] emptyBloomFilter = new byte[bloomFilterByteSize];
			for(int i = 0; i < bloomFilterByteSize; i++) {
				emptyBloomFilter[i] = 0;
			}
	
			List<BloomFilterBitStat> bitStats = null;
			bitStats = new ArrayList<BloomFilterBitStat>();
			for(int i = 0; i < bloomFilterSize; i++) {
				bitStats.add(new BloomFilterBitStat(0, i));
			}

			List<Long> leftPersonIdList = new ArrayList<Long>();
			List<Long> rightPersonIdList = new ArrayList<Long>();
			List<Boolean> matchStatusList = new ArrayList<Boolean>();
			Iterator<MatchPairStat> matchPairStatIt = matchPairStats.iterator();
			int countMatched = 0;
			while (matchPairStatIt.hasNext()) {
				leftPersonIdList.clear();
				rightPersonIdList.clear();
				matchStatusList.clear();
				for (int i = 0; i < Constants.PAGE_SIZE && matchPairStatIt.hasNext(); i++) {
					MatchPairStat matchPairStat = matchPairStatIt.next();
					leftPersonIdList.add(matchPairStat.getLeftPersonPseudoId());
					rightPersonIdList.add(matchPairStat.getRightPersonPseudoId());
					matchStatusList.add(matchPairStat.getMatchStatus());
				}
				// Repopulation maybe needed: if some of the PersonId present more times in the Id list
				// In that case Dao will simplify and give back that person only one time, not as many times as we request
				List<Person> leftPersonList = personQueryService.getPersonsByIds(leftTableName, leftPersonIdList, fieldNames);
				if (leftPersonList.size() == 1) {	// Add the Person another Constants.PAGE_SIZE-1 times => there will Constants.PAGE_SIZE persons
					for(int i = 0; i < leftPersonIdList.size() - 1; i++)
						leftPersonList.add(leftPersonList.get(0));
				} else if (leftPersonList.size() < leftPersonIdList.size()) {	// Repopulation is needed
					leftPersonList = populateList(leftPersonList, leftPersonIdList);
				}
				List<Person> rightPersonList = personQueryService.getPersonsByIds(rightTableName, rightPersonIdList, fieldNames);
				if (rightPersonList.size() == 1) {	// Add the Person another Constants.PAGE_SIZE-1 times => there will Constants.PAGE_SIZE persons
					for(int i = 0; i < rightPersonIdList.size() - 1; i++)
						rightPersonList.add(rightPersonList.get(0));
				} else if (rightPersonList.size() < rightPersonIdList.size()) {	// Repopulation is needed
					rightPersonList = populateList(rightPersonList, rightPersonIdList);
				}

				Iterator<Boolean> matchStatusIterator = matchStatusList.iterator();
				Iterator<Person> leftPersonIterator = leftPersonList.iterator();
				Iterator<Person> rightPersonIterator = rightPersonList.iterator();
				while (matchStatusIterator.hasNext() && leftPersonIterator.hasNext() && rightPersonIterator.hasNext()) {
					boolean pairMatches = matchStatusIterator.next();
					if (pairMatches)
						countMatched++;

					Person leftPerson = leftPersonIterator.next();
					Person rightPerson = rightPersonIterator.next();
					byte[] leftBloomFilterBytes = (byte[])leftPerson.getAttribute(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
					if (leftBloomFilterBytes == null)
						leftBloomFilterBytes = emptyBloomFilter;
					byte[] rightBloomFilterBytes = (byte[])rightPerson.getAttribute(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
					if (rightBloomFilterBytes == null)
						rightBloomFilterBytes = emptyBloomFilter;

					for (int i = 0; i < bloomFilterByteSize; i++) {
						byte leftBloomFilterByte = leftBloomFilterBytes[i];
						byte rightBloomFilterByte = rightBloomFilterBytes[i];
						int xorBits = leftBloomFilterByte ^ rightBloomFilterByte;
						for (int j = 0; j < 8; j++) {
							Integer bitStatIndex = (i * 8 + j);
							BloomFilterBitStat actualBitStat = bitStats.get(bitStatIndex);
							actualBitStat.incrementBitTotal();
							boolean bitMatches = !((xorBits & 1) == 0);
							if (bitMatches) {
								if (pairMatches)
									actualBitStat.incrementBitMatchPairMatch();
								else
									actualBitStat.incrementBitMatchPairUnmatch();
							} else {
								if (pairMatches)
									actualBitStat.incrementBitUnmatchPairMatch();
								else
									actualBitStat.incrementBitUnmatchPairUnmatch();
							}
							xorBits = (xorBits >> 1);
							boolean leftBitSet = !((leftBloomFilterByte & 1) == 0);
							if (leftBitSet) {
								if (pairMatches)
									actualBitStat.incrementBitOnePairMatch();
								else
									actualBitStat.incrementBitOnePairUnmatch();
							} else {
								if (pairMatches)
									actualBitStat.incrementBitZeroPairMatch();
								else
									actualBitStat.incrementBitZeroPairUnmatch();
							}
							boolean rightBitSet = !((rightBloomFilterByte & 1) == 0);
							if (rightBitSet) {
								if (pairMatches)
									actualBitStat.incrementBitOnePairMatch();
								else
									actualBitStat.incrementBitOnePairUnmatch();
							} else {
								if (pairMatches)
									actualBitStat.incrementBitZeroPairMatch();
								else
									actualBitStat.incrementBitZeroPairUnmatch();
							}
						}
					}
				}
			}
			System.out.println("We automatically matched " + countMatched + " out of " + matchPairStats.size());

			// P(A|B) = P(A and B)/P(B)
			// P(bit n matches | M) = P(bit n matches and M)/P(M) =
			//  (num of bit match _and_ pair match) / num of pairs / (num of match / num of pairs) =
			//  (num of bit match _and_ pair match) / (num of match)
			// Calculate importance of bloom filter bits = P(bit n matches | M)

//			Collections.sort(bitStats, new BloomFilterBitStatComparatorByImportance());

			BloomFilterStatistics bloomFilterStatistics =
				new BloomFilterStatistics(matchPairStats.size(), bitStats);
			saveBloomFilterStatistics(fileRepositoryDirectory, bloomFilterStatistics);
//		}
	}

	private void saveLeanRecordPairs(String configDirectory, List<LeanRecordPair> pairs) {
		SerializationUtil.serializeObject(configDirectory, LEAN_RECORDPAIRS_FILE_NAME, pairs);
	}

	private Object loadLeanRecordPairs(String configDirectory) {
		return SerializationUtil.deserializeObject(configDirectory, LEAN_RECORDPAIRS_FILE_NAME);
	}

}
