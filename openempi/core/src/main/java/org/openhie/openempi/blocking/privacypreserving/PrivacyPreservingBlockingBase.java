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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.AbstractBlockingServiceBase;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.blocking.basicblocking.BasicBlockingConstants;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.blocking.basicblocking.BlockingSettings;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingField;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingSettings;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.MatchingServiceSelector;
import org.openhie.openempi.matching.MatchingServiceType;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingServiceBase;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;
import org.openhie.openempi.util.ParallelIteratorUtil;
import org.openhie.openempi.util.ParallelPersonListIterator;
import org.openhie.openempi.util.SerializationUtil;

public abstract class PrivacyPreservingBlockingBase extends AbstractBlockingServiceBase
{
	protected static final String BLOOMFILTER_STATISTICS_FILE_NAME = "BloomFilterStatistics.ser";
	protected static final String PERSONBUCKETS_FILE_NAME = "PersonBuckets.ser";
	protected static final String PERSONOTHERBUCKETS_FILE_NAME = "PersonOtherBuckets.ser";

	protected void calculateEmptyBloomFilters(String leftTableName, String rightTableName)
	{
		List<PrivacyPreservingBlockingField> ppbFields =
			Context.getConfiguration().getPrivacyPreservingBlockingSettings().getPrivacyPreservingBlockingFields();
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int bloomFilterDefaultSize = privacySettings.getBloomfilterSettings().getDefaultM();
		// get the function parameters from the persisted import configuration
		PersonQueryService personQueryService = Context.getPersonQueryService();
		Dataset leftDataset = personQueryService.getDatasetByTableName(leftTableName);
		List<ColumnInformation> leftColumnInformation = leftDataset.getColumnInformation();
		Dataset rightDataset = personQueryService.getDatasetByTableName(rightTableName);
		List<ColumnInformation> rightColumnInformation = rightDataset.getColumnInformation();

		for (PrivacyPreservingBlockingField ppbField : ppbFields) {
			if (ppbField.getEmptyBloomFilter() != null)
				break;
			Integer bloomFilterSize = bloomFilterDefaultSize;
			Integer leftBloomFilterK = 0;
			Integer leftBloomFilterM = 0;
			for (ColumnInformation leftCI : leftColumnInformation) {
				if (leftCI.getFieldName().equals(ppbField.getLeftFieldName())) {
					leftBloomFilterK = leftCI.getBloomFilterKParameter();
					leftBloomFilterM = leftCI.getBloomFilterMParameter();
				}
			}
			Integer rightBloomFilterK = 0;
			Integer rightBloomFilterM = 0;
			for (ColumnInformation rightCI : rightColumnInformation) {
				if (rightCI.getFieldName().equals(ppbField.getRightFieldName())) {
					rightBloomFilterK = rightCI.getBloomFilterKParameter();
					rightBloomFilterM = rightCI.getBloomFilterMParameter();
				}
			}
			if (leftBloomFilterK != rightBloomFilterK || leftBloomFilterM != rightBloomFilterM)
				log.error("Left and right bloom filter parameters doesn't match: " +
						ppbField.getLeftFieldName() + " - " + leftBloomFilterK + ", " + leftBloomFilterM + ", " +
						ppbField.getRightFieldName() + " - " + rightBloomFilterK + ", " + rightBloomFilterM, null);
			if (rightBloomFilterM != 0)
				bloomFilterSize = rightBloomFilterM;
			ppbField.createEmptyBloomFilter(bloomFilterSize);
		}
	}

	protected void getRecordPairs(List<BloomFilterBitStat> bits, List<LeanRecordPair> pairs, Set<String> idPairHash,
			String leftTableName, String rightTableName, String leftOriginalIdFieldName, String rightOriginalIdFieldName,
			boolean emOnly, FellegiSunterParameters fellegiSunterParams) {
		if (emOnly && fellegiSunterParams == null)
			throw new IllegalArgumentException("Must supply FellegiSunterParameters in case if emOnly mode");
		PersonQueryService personQueryService = Context.getPersonQueryService();

		StringComparisonService comparisonService = Context.getStringComparisonService();
		MatchConfiguration matchConfiguration =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfiguration.getMatchFields();

		calculateEmptyBloomFilters(leftTableName, rightTableName);

		List<String> leftMatchFieldNames = matchConfiguration.getLeftFieldNames();
		if (leftOriginalIdFieldName != null)
			leftMatchFieldNames.add(leftOriginalIdFieldName);
		List<String> rightMatchFieldNames = matchConfiguration.getRightFieldNames();
		if (rightOriginalIdFieldName != null)
			rightMatchFieldNames.add(rightOriginalIdFieldName);

		Collections.sort(bits, new BloomFilterBitStatComparatorByBloomFilterIndex());
		PrivacyPreservingBlockingSettings ppbs = Context.getConfiguration().getPrivacyPreservingBlockingSettings();
		List<PrivacyPreservingBlockingField> ppbFields = ppbs.getPrivacyPreservingBlockingFields();
		List<String> ppbLeftFieldNames = ppbs.getPrivacyPreservingBlockingLeftFieldNames();
		if (leftOriginalIdFieldName != null)
			ppbLeftFieldNames.add(leftOriginalIdFieldName);

		int pageSize = Constants.PAGE_SIZE;
		Long pageStart = 0L;
		int numPersons = 0;
		// Allocate buckets
		int numberOfBuckets = (int)Math.pow(2, bits.size());
		List<PairBucketData> buckets = null;
		List<PairBucketData> otherBuckets = null;
		String fileRepositoryDirectory =
			Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
//		String filename = configDirectory + "/" + PERSONBUCKETS_FILE_NAME;
//		File file = new File(filename);
//		if (file.exists()) {
//			buckets = loadPersonBuckets(configDirectory);
//		} else {
			buckets = new ArrayList<PairBucketData>();
			for(int i = 0; i < numberOfBuckets; i++) {
				buckets.add(new PairBucketData());
			}
			// Go through all Person and sort them into the buckets according to the bits
			pageStart = 0L;
			numPersons = 0;
			do {
				List<Person> personList = personQueryService.getPersonsPaged(leftTableName, ppbLeftFieldNames, pageStart, pageSize);
				numPersons = personList.size();
				if (numPersons > 0) {
					for(Person person : personList) {
						byte[] bloomFilterBytes = null;
						Integer previousCompositeFieldIndex = -1;
						// Determine bucket number
						int bucketNumber = 0;
						for(BloomFilterBitStat bloomFilterBit : bits) {
							if (previousCompositeFieldIndex != bloomFilterBit.getCompositeFieldIndex()) {
								PrivacyPreservingBlockingField ppbField = ppbFields.get(bloomFilterBit.getCompositeFieldIndex());
								bloomFilterBytes = (byte[])person.getAttribute(ppbField.getLeftFieldName());
								if (bloomFilterBytes == null)	// TODO: or treat empty fields differently?
									bloomFilterBytes = ppbField.getEmptyBloomFilter();
							}
							int byteIndex = bloomFilterBit.getBloomFilterBitIndex() / 8;
							int bitIndex = bloomFilterBit.getBloomFilterBitIndex() % 8;
							int bucketBit = (bloomFilterBytes[byteIndex] >> bitIndex) & 1;
							bucketNumber = (bucketNumber << 1) + bucketBit;
							previousCompositeFieldIndex = bloomFilterBit.getCompositeFieldIndex();
						}
						// Put into bucket
						buckets.get(bucketNumber).addId(person.getPersonId());
					}
				}
				pageStart += pageSize;
			} while (numPersons > 0);
//		}

		List<String> ppbRightFieldNames = ppbs.getPrivacyPreservingBlockingRightFieldNames();
		if (rightOriginalIdFieldName != null)
			ppbRightFieldNames.add(rightOriginalIdFieldName);

//		filename = configDirectory + "/" + PERSONOTHERBUCKETS_FILE_NAME;
//		file = new File(filename);
//		if (file.exists()) {
//			otherBuckets = loadPersonOtherBuckets(configDirectory);
//		} else {
			otherBuckets = new ArrayList<PairBucketData>();
			for(int i = 0; i < numberOfBuckets; i++) {
				otherBuckets.add(new PairBucketData());
			}
			// Go through all PersonOther and sort them into the buckets according to the bits
			Long otherPageStart = 0L;
			int otherNumPersons = 0;
			do {
				List<Person> personOtherList = personQueryService.getPersonsPaged(rightTableName,
						ppbRightFieldNames, otherPageStart, pageSize);
				otherNumPersons = personOtherList.size();
				if (otherNumPersons > 0) {
					for (Person personOther : personOtherList) {
						byte[] bloomFilterBytes = null;
						Integer previousCompositeFieldIndex = -1;
						// Determine bucket number
						int bucketNumber = 0;
						for(BloomFilterBitStat bloomFilterBit : bits) {
							if (previousCompositeFieldIndex != bloomFilterBit.getCompositeFieldIndex()) {
								PrivacyPreservingBlockingField ppbField = ppbFields.get(bloomFilterBit.getCompositeFieldIndex());
								bloomFilterBytes = (byte[])personOther.getAttribute(ppbField.getRightFieldName());
								if (bloomFilterBytes == null)	// TODO: or treat empty fields differently?
									bloomFilterBytes = ppbField.getEmptyBloomFilter();
							}
							int byteIndex = bloomFilterBit.getBloomFilterBitIndex() / 8;
							int bitIndex = bloomFilterBit.getBloomFilterBitIndex() % 8;
							int bucketBit = (bloomFilterBytes[byteIndex] >> bitIndex) & 1;
							bucketNumber = (bucketNumber << 1) + bucketBit;
							previousCompositeFieldIndex = bloomFilterBit.getCompositeFieldIndex();
						}
						// Put into bucket
						otherBuckets.get(bucketNumber).addId(personOther.getPersonId());
					}
				}
				otherPageStart += pageSize;
			} while (otherNumPersons > 0);
//		}

		if (emOnly) {
			for (int i=0; i < fellegiSunterParams.getVectorCount(); i++) {
				fellegiSunterParams.setVectorFrequency(i, 0);
			}
		}

		// 5. Take each bucket pair and generate pairs from them
		for (int i = 0; i < numberOfBuckets; i++) {
			List<Long> bucket = buckets.get(i).getIds();
			List<Long> otherBucket = otherBuckets.get(i).getIds();
			if (bucket.size() > 0 && otherBucket.size() > 0) {
				List<Person> persons = personQueryService.getPersonsByIdsTransactionally(leftTableName, bucket, leftMatchFieldNames);
				List<Person> personOthers = personQueryService.getPersonsByIdsTransactionally(rightTableName, otherBucket, rightMatchFieldNames);
				for (Person person : persons) {
					for (Person personOther : personOthers) {
						String leftOriginalId = person.getOriginalIdString(leftOriginalIdFieldName);
						String rightOriginalId = personOther.getOriginalIdString(rightOriginalIdFieldName);
						boolean alreadyExamined = false;
						String idPairStr = null;
						if (idPairHash != null) {
							idPairStr = person.getPersonId().toString() + "_" + personOther.getPersonId().toString();
							if (idPairHash.contains(idPairStr))
								alreadyExamined = true;
						}
						if (!alreadyExamined) {
							ComparisonVector comparisonVector =
									GeneralUtil.scoreRecordPair(person, personOther, comparisonService, matchFields);
							if (emOnly) {
								fellegiSunterParams.incrementVectorFrequency(comparisonVector.getBinaryVectorValue());
							} else {
								LeanRecordPair recordPair = new LeanRecordPair(person.getPersonId(),
										leftOriginalId,
										personOther.getPersonId(),
										rightOriginalId);
								recordPair.setComparisonVector(comparisonVector);
								pairs.add(recordPair);
							}
							if (idPairHash != null)
								idPairHash.add(idPairStr);
							if (leftOriginalId != null && rightOriginalId != null) {
								if (leftOriginalId.equals(rightOriginalId)) {
									buckets.get(i).incrementTrueMatchCounter();
									otherBuckets.get(i).incrementTrueMatchCounter();
								}
							}
						}
					}
				}
			}
		}
		savePersonBuckets(fileRepositoryDirectory, buckets);
		savePersonOtherBuckets(fileRepositoryDirectory, otherBuckets);
		// Debug print [
		System.out.println("Selected bits:");
		for(BloomFilterBitStat bloomFilterBit : bits) {
			System.out.println(bloomFilterBit.getCompositeFieldIndex() +
					"(" + ppbFields.get(bloomFilterBit.getCompositeFieldIndex()).getLeftFieldName() +
					"," + ppbFields.get(bloomFilterBit.getCompositeFieldIndex()).getRightFieldName() +
					"), " + bloomFilterBit.getBloomFilterBitIndex());
		}
		System.out.println("Bucket no., size1, size2, true mathes:");
		for (int i = 0; i < numberOfBuckets; i++) {
			List<Long> bucket = buckets.get(i).getIds();
			List<Long> otherBucket = otherBuckets.get(i).getIds();
			System.out.println(i + ".: " + bucket.size() + ", " + otherBucket.size() + ", " +
					buckets.get(i).getTrueMatchCounter());
		}
		// ] Debug print
		System.out.println("Number of pairs: " + pairs.size());
	}

	public void calculateBitStatistics(String matchingServiceTypeName, String leftTableName, String rightTableName,
			final String leftOriginalIdFieldName, final String rightOriginalIdFieldName) {
		PersonQueryService personQueryService = Context.getPersonQueryService();

		StringComparisonService comparisonService = Context.getStringComparisonService();
		MatchConfiguration matchConfiguration =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfiguration.getMatchFields();
		List<String> leftMatchFieldNames = matchConfiguration.getLeftFieldNames();
		List<String> rightMatchFieldNames = matchConfiguration.getRightFieldNames();

		Configuration config = Context.getConfiguration();
		PrivacyPreservingBlockingSettings ppbs = config.getPrivacyPreservingBlockingSettings();
		BlockingSettings blockingSettings = (BlockingSettings)
				config.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY);
		Integer numberOfPairsToSample = blockingSettings.getNumberOfRecordsToSample();
		numberOfPairsToSample = numberOfPairsToSample * numberOfPairsToSample;	// Take the square root of it?
		List<String> ppbLeftFieldNames = ppbs.getPrivacyPreservingBlockingLeftFieldNames();
		leftMatchFieldNames.addAll(ppbLeftFieldNames);
		List<String> ppbRightFieldNames = ppbs.getPrivacyPreservingBlockingRightFieldNames();
		rightMatchFieldNames.addAll(ppbRightFieldNames);

		if (leftOriginalIdFieldName != null)
			leftMatchFieldNames.add(leftOriginalIdFieldName);
		if (rightOriginalIdFieldName != null)
			rightMatchFieldNames.add(rightOriginalIdFieldName);

		int pageSize = Constants.PAGE_SIZE;
		List<Person> randomPersonList = new ArrayList<Person>();
		List<Person> randomPersonOtherList = new ArrayList<Person>();
		List<LeanRecordPair> pairs = new ArrayList<LeanRecordPair>();
		do {
			// Get next page of persons
			List<Person> personList =
				personQueryService.getRandomPersons(leftTableName, leftMatchFieldNames, Math.min(pageSize, numberOfPairsToSample - randomPersonList.size()));
			if (personList.size() > 0)
				randomPersonList.addAll(personList);
			else
				break;
			List<Person> personOtherList =
				personQueryService.getRandomPersons(rightTableName, rightMatchFieldNames, Math.min(pageSize, numberOfPairsToSample - randomPersonOtherList.size()));
			if (personOtherList.size() > 0)
				randomPersonOtherList.addAll(personOtherList);
			else
				break;
			// Generate and score pairs from the current pages
			ParallelIteratorUtil.iterate(personList, personOtherList, pairs, comparisonService, matchFields,
					new ParallelPersonListIterator<Person, Person>() {
				public boolean each(Person p, Person po, List<LeanRecordPair> pairs,
						StringComparisonService comparisonService, List<MatchField> matchFields) {
					String leftOriginalId = p.getOriginalIdString(leftOriginalIdFieldName);
					String rightOriginalId = po.getOriginalIdString(rightOriginalIdFieldName);
					LeanRecordPair recordPair = new LeanRecordPair(p.getPersonId(),
																	leftOriginalId,
																	po.getPersonId(),
																	rightOriginalId);
					ComparisonVector comparisonVector =
							GeneralUtil.scoreRecordPair(p, po, comparisonService, matchFields);
					recordPair.setComparisonVector(comparisonVector);
					pairs.add(recordPair);
					return true;
				}
			});
		} while (randomPersonList.size() < numberOfPairsToSample);

		int fieldCount = matchFields.size();
		FellegiSunterParameters fellegiSunterParams = new FellegiSunterParameters(fieldCount, 2);

		String fileRepositoryDirectory =
			Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
//		String filename = configDirectory + "/" + Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME;
//		File file = new File(filename);

		fellegiSunterParams.setMu(matchConfiguration.getFalsePositiveProbability());
		fellegiSunterParams.setLambda(matchConfiguration.getFalseNegativeProbability());
		MatchingServiceSelector matchingServiceSelector = Context.getMatchingServiceSelector();
		MatchingServiceType matchingServiceType = matchingServiceSelector.getMatchingServiceType(matchingServiceTypeName);
		// TODO: what if it's not Probabilistic?
		ProbabilisticMatchingServiceBase matchingService = (ProbabilisticMatchingServiceBase)matchingServiceType.getMatchingService();
//		scoreRecordPairs(pairs, fellegiSunterParams, true);
		matchingService.calculateVectorFrequencies(pairs, fellegiSunterParams);
		matchingService.estimateMarginalProbabilities(fellegiSunterParams, matchConfiguration, pairs.size());
		System.out.println("Fellegi Sunter Parameters:\n" + fellegiSunterParams);
		matchingService.calculateRecordPairWeights(pairs, fellegiSunterParams);
		matchingService.calculateMarginalProbabilities(pairs, fellegiSunterParams, false, null);
		matchingService.orderRecordPairsByWeight(pairs, false, null);
		matchingService.calculateLowerBound(pairs, fellegiSunterParams);
		matchingService.calculateUpperBound(pairs, fellegiSunterParams);
		saveFellegiSunterParameters(fileRepositoryDirectory, fellegiSunterParams);

		calculateEmptyBloomFilters(leftTableName, rightTableName);
		List<PrivacyPreservingBlockingField> ppbFields = ppbs.getPrivacyPreservingBlockingFields();

		List<BloomFilterBitStat> bitStats = null;
//		filename = configDirectory + "/" + BLOOMFILTER_STATISTICS_FILE_NAME;
//		file = new File(filename);
//		if (file.exists()) {
//			BloomFilterStatistics bloomFilterStatistics = loadBloomFilterStatistics(configDirectory);
//			bitStats = bloomFilterStatistics.getBloomFilterBitStats();
//		} else {
			bitStats = new ArrayList<BloomFilterBitStat>();
			int fieldIndex = 0;
			for(PrivacyPreservingBlockingField ppbField : ppbFields) {
				int bloomFilterSize = ppbField.getEmptyBloomFilter().length * 8;
				for(int j = 0; j < bloomFilterSize; j++) {
					bitStats.add(new BloomFilterBitStat(fieldIndex, j));
				}
				fieldIndex++;
			}

			Iterator<LeanRecordPair> recordPairIterator = pairs.iterator();
			Iterator<Person> personIterator = randomPersonList.iterator();
			Iterator<Person> personOtherIterator = randomPersonOtherList.iterator();
			int countMatched = 0;
			boolean canCountTrueMatches = (leftOriginalIdFieldName != null && rightOriginalIdFieldName != null);
			int countTrueMatch = 0;
			int countTrueMatchMatch = 0;
			while (recordPairIterator.hasNext() && personIterator.hasNext() && personOtherIterator.hasNext()) {
				LeanRecordPair pair = recordPairIterator.next();
				boolean trueMatch = false;
				if (canCountTrueMatches)
					trueMatch = pair.getLeftOriginalRecordId().equals(pair.getRightOriginalRecordId());
				boolean pairMatches = (pair.getWeight() >= fellegiSunterParams.getUpperBound());
				if (pairMatches) {
					countMatched++;
					if (trueMatch)
						countTrueMatchMatch++;
				}
				if (trueMatch)
					countTrueMatch++;
				System.out.println("Pair " + pair.getLeftRecordId() + " (" + pair.getLeftOriginalRecordId() + ")" +
									", " + pair.getRightRecordId() + " (" + pair.getRightOriginalRecordId() + ")" +
									(trueMatch ? "*" : "-"));
				Person leftPerson = personIterator.next();
				Person rightPerson = personOtherIterator.next();
				int bloomFilterIndex = 0;
				for(PrivacyPreservingBlockingField ppbField : ppbFields) {
					byte[] leftBloomFilterBytes = (byte[])leftPerson.getAttribute(ppbField.getLeftFieldName());
					if (leftBloomFilterBytes == null)
						leftBloomFilterBytes = ppbField.getEmptyBloomFilter();
					byte[] rightBloomFilterBytes = (byte[])rightPerson.getAttribute(ppbField.getRightFieldName());
					if (rightBloomFilterBytes == null)
						rightBloomFilterBytes = ppbField.getEmptyBloomFilter();

					int bloomFilterByteSize = ppbField.getEmptyBloomFilter().length;
					int bloomFilterSize = bloomFilterByteSize * 8;
					for (int i = 0; i < bloomFilterByteSize; i++) {
						byte leftBloomFilterByte = leftBloomFilterBytes[i];
						byte rightBloomFilterByte = rightBloomFilterBytes[i];
						int xorBits = leftBloomFilterByte ^ rightBloomFilterByte;
						for (int j = 0; j < 8; j++) {
							// TODO: bug if there are multiple fields and BF sizes are not the same
							Integer bitStatIndex = bloomFilterIndex * bloomFilterSize + (i * 8 + j);
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
					bloomFilterIndex++;
				}
			}
			System.out.println("We automatically matched " + countMatched + " out of " + pairs.size());
			System.out.println("True matches: " + countTrueMatch + ", true matches out of our matches: " + countTrueMatchMatch);

			// P(A|B) = P(A and B)/P(B)
			// P(bit n matches | M) = P(bit n matches and M)/P(M) =
			//  (num of bit match _and_ pair match) / num of pairs / (num of match / num of pairs) =
			//  (num of bit match _and_ pair match) / (num of match)
			// Calculate importance of bloom filter bits = P(bit n matches | M)

//			Collections.sort(bitStats, new BloomFilterBitStatComparatorByImportance());

			BloomFilterStatistics bloomFilterStatistics =
				new BloomFilterStatistics(numberOfPairsToSample, bitStats);
			saveBloomFilterStatistics(fileRepositoryDirectory, bloomFilterStatistics);
//		}
	}

	protected void saveFellegiSunterParameters(String configDirectory, FellegiSunterParameters params) {
		SerializationUtil.serializeObject(configDirectory, Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME, params);
	}

	protected Object loadFellegiSunterParameters(String configDirectory) {
		return SerializationUtil.deserializeObject(configDirectory, Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME);
	}

	protected void saveBloomFilterStatistics(String configDirectory, BloomFilterStatistics bloomFilterStatistics) {
		SerializationUtil.serializeObject(configDirectory, BLOOMFILTER_STATISTICS_FILE_NAME, bloomFilterStatistics);
	}

	protected BloomFilterStatistics loadBloomFilterStatistics(String configDirectory) {
		return (BloomFilterStatistics)SerializationUtil.deserializeObject(configDirectory, BLOOMFILTER_STATISTICS_FILE_NAME);
	}

	protected void savePersonBuckets(String configDirectory, List<PairBucketData> buckets) {
		SerializationUtil.serializeObject(configDirectory, PERSONBUCKETS_FILE_NAME, buckets);
	}

	@SuppressWarnings("unchecked")
	protected List<PairBucketData> loadPersonBuckets(String configDirectory) {
		return (List<PairBucketData>)SerializationUtil.deserializeObject(configDirectory, PERSONBUCKETS_FILE_NAME);
	}

	protected void savePersonOtherBuckets(String configDirectory, List<PairBucketData> otherBuckets) {
		SerializationUtil.serializeObject(configDirectory, PERSONOTHERBUCKETS_FILE_NAME, otherBuckets);
	}

	@SuppressWarnings("unchecked")
	protected List<PairBucketData> loadPersonOtherBuckets(String configDirectory) {
		return (List<PairBucketData>)SerializationUtil.deserializeObject(configDirectory, PERSONOTHERBUCKETS_FILE_NAME);
	}

	public List<LeanRecordPair> findCandidates(String leftTableName, String rightTableName,
			String leftOriginalIdFieldName, String rightOriginalIdFieldName, Person person) {
		throw new UnsupportedOperationException("findCandidates is not implemented in PrivacyPreservingBlocking algorithms");
	}

	public RecordPairSource getRecordPairSource(String leftTableName, String rightTableName) {
		throw new UnsupportedOperationException("getRecordPairSource is not implemented in PrivacyPreservingBlocking algorithms");
	}

	public RecordPairSource getRecordPairSource(List<BlockingRound> blockingRounds,
			String leftTableName, String rightTableName) {
		throw new UnsupportedOperationException("getRecordPairSource is not implemented in PrivacyPreservingBlocking algorithms");
	}

}
