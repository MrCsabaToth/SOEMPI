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
package org.openhie.openempi.matching.fellegisunter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.dao.hibernate.UniversalDaoHibernate;
import org.openhie.openempi.matching.AbstractMatchingService;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.stringcomparison.DistanceMetricType;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.GeneralUtil;
import org.openhie.openempi.util.SerializationUtil;

public abstract class ProbabilisticMatchingServiceBase extends AbstractMatchingService
{
	protected FellegiSunterParameters fellegiSunterParams;
	protected PersonLinkDao personLinkDao;
	private static final double MIN_MARGINAL_VALUE = 0.000001;
	
	public void init() {
		log.debug("ProbabilisticMatchingServiceBase init");
	}

	protected abstract boolean useBinaryScores();

	public Set<LeanRecordPair> match(String blockingServiceTypeName, String leftTableName, String rightTableName,
			Person person) throws ApplicationException {
		log.debug("Looking for matches on person " + person);
		if (!initialized) {
			throw new ApplicationException("Matching service has not been initialized yet.");
		}
		List<LeanRecordPair> pairs = Context.getBlockingServiceSelector().findCandidates(blockingServiceTypeName,
				leftTableName, rightTableName, person);
		Set<LeanRecordPair> matches = new java.util.HashSet<LeanRecordPair>();
//		scoreRecordPairs(pairs, fellegiSunterParams, false);
		calculateRecordPairWeights(pairs, fellegiSunterParams);
		
		// Apply Fellegi-Sunter decision rule
		for (LeanRecordPair pair : pairs) {
			if (pair.getWeight() >= fellegiSunterParams.getUpperBound()) {
				matches.add(pair);
			} else if (pair.getWeight() > fellegiSunterParams.getLowerBound()) {
				// This is a possible match; need to add it to the list for review
			}
		}
		return matches;
	}

	public PersonMatch linkRecords(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters, String linkTableName,
			String leftTableName, String rightTableName, List<LeanRecordPair> pairsParam, ComponentType componentType,
			boolean emOnly, boolean persistLinks) throws ApplicationException {

		long startTime = System.nanoTime();
		MatchConfiguration matchConfig =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfig.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		int fieldCount = matchFields.size();
		fellegiSunterParams = new FellegiSunterParameters(fieldCount, 2);
		List<LeanRecordPair> pairs = pairsParam;
		if (pairs == null && !emOnly)
			pairs = new ArrayList<LeanRecordPair>();
		getRecordPairs(blockingServiceTypeName, blockingServiceCustomParameters,
				matchingServiceTypeName, matchingServiceCustomParameters,
				leftTableName, rightTableName, pairs, emOnly, fellegiSunterParams);
		fellegiSunterParams.setMu(matchConfig.getFalsePositiveProbability());
		fellegiSunterParams.setLambda(matchConfig.getFalseNegativeProbability());

		if (!emOnly) {
//			scoreRecordPairs(pairs, fellegiSunterParams, true);
			calculateVectorFrequencies(pairs, fellegiSunterParams);
		}
		estimateMarginalProbabilities(fellegiSunterParams, matchConfig, pairs.size());
		long blockNfsTime = System.nanoTime();
		long fileOutTime = 0L;
		log.trace("Fellegi Sunter Parameters:\n" + fellegiSunterParams);
		String fileRepoDir = Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
		if (!emOnly) {
			calculateRecordPairWeights(pairs, fellegiSunterParams);
			calculateMarginalProbabilities(pairs, fellegiSunterParams, true, fileRepoDir + "/" + linkTableName);
			fileOutTime = System.nanoTime();
			orderRecordPairsByWeight(pairs, true, fileRepoDir + "/" + linkTableName);
			calculateLowerBound(pairs, fellegiSunterParams);
			calculateUpperBound(pairs, fellegiSunterParams);
		}
		SerializationUtil.serializeObject(fileRepoDir, linkTableName + "_" + Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME, fellegiSunterParams);
		initialized = true;

		PersonManagerService personManagerService = Context.getPersonManagerService();
		Dataset leftDataset = personManagerService.getDatasetByTableName(leftTableName);
		Dataset rightDataset = personManagerService.getDatasetByTableName(rightTableName);
		PersonMatch personMatch = new PersonMatch();
		personMatch.setMatchTitle(linkTableName);
		personMatch.setLeftDataset(leftDataset);
		personMatch.setRightDataset(rightDataset);
		personMatch.setMatchConfiguration(matchConfig);
		personMatch.setMatchFellegiSunter(fellegiSunterParams);
		personMatch.setTotalRecords(Long.valueOf(pairs.size()));
		List<ColumnMatchInformation> columnMatchInformation = new ArrayList<ColumnMatchInformation>();
		Map<String,ColumnInformation> leftColumnInformationHash = new HashMap<String,ColumnInformation>();
		for (ColumnInformation ci : leftDataset.getColumnInformation()) {
			leftColumnInformationHash.put(ci.getFieldName(), ci);
		}
		Map<String,ColumnInformation> rightColumnInformationHash = new HashMap<String,ColumnInformation>();
		for (ColumnInformation ci : rightDataset.getColumnInformation()) {
			rightColumnInformationHash.put(ci.getFieldName(), ci);
		}
		int i = 0;
		long totalRecords = 0;
		double downweightedRangeSum = 0.0;
		boolean parameterManagerMode = (componentType == ComponentType.PARAMETER_MANAGER_MODE);
		if (parameterManagerMode && !emOnly)
			totalRecords = leftDataset.getTotalRecords() + rightDataset.getTotalRecords();
		StringComparisonService comparisonService = Context.getStringComparisonService();
		log.debug("Constructing ColumnMatchInformations...");
		for (MatchField matchField : matchFields) {
			ColumnMatchInformation cmi = new ColumnMatchInformation();
			cmi.setLeftFieldName(matchField.getLeftFieldName());
			cmi.setRightFieldName(matchField.getRightFieldName());
			cmi.setPersonMatch(personMatch);
			ColumnInformation leftCi = leftColumnInformationHash.get(matchField.getLeftFieldName());
			ColumnInformation rightCi = rightColumnInformationHash.get(matchField.getRightFieldName());
			if (!(new EqualsBuilder().
				append(leftCi.getFieldType().getFieldTypeEnum(), rightCi.getFieldType().getFieldTypeEnum()).
				append(leftCi.getFieldTypeModifier(), rightCi.getFieldTypeModifier()).
				append(leftCi.getFieldMeaning().getFieldMeaningEnum(), rightCi.getFieldMeaning().getFieldMeaningEnum()).
				append(leftCi.getFieldTransformation(), rightCi.getFieldTransformation()).
				append(leftCi.getBloomFilterKParameter(), rightCi.getBloomFilterKParameter()).
				append(leftCi.getBloomFilterMParameter(), rightCi.getBloomFilterMParameter()).
				isEquals()))
			{
				throw new ApplicationException("Field informations don't match; left: " + leftCi + ", right: " + rightCi);
			}
			cmi.setFieldType(leftCi.getFieldType().getFieldTypeEnum());
			//cmi.getFieldType();	// to load FieldType before saving
			String funcName = matchField.getComparatorFunction().getFunctionName();
			boolean noMatchField = (funcName.equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME));
			DistanceMetricType distanceMetricType = comparisonService.getDistanceMetricType(funcName);
			if (distanceMetricType.getDistanceMetric().getInputType() != cmi.getFieldType().getFieldTypeEnum())
				throw new ApplicationException("Field types (" + distanceMetricType.getDistanceMetric().getInputType() +
						") are not compatible with the comparison function input type (" + cmi.getFieldType().getFieldTypeEnum() + ")");
			cmi.setFieldTypeModifier(leftCi.getFieldTypeModifier());
			cmi.setFieldMeaning(leftCi.getFieldMeaning().getFieldMeaningEnum());
			cmi.getFieldMeaning();	// to load FieldType before saving
			cmi.setComparisonFunctionName(funcName);
			if (!noMatchField) {
				cmi.setFellegiSunterMValue(fellegiSunterParams.getMValue(i));
				cmi.setFellegiSunterUValue(fellegiSunterParams.getUValue(i));
				if (!emOnly) {
					if (parameterManagerMode) {
						double percentMissing = (Double.valueOf(leftCi.getNumberOfMissing()) +
												Double.valueOf(rightCi.getNumberOfMissing())) /
												Double.valueOf(totalRecords);
						cmi.setPercentMissing(percentMissing);
						double downweightedRange = cmi.getFellegiSunterWRange() * (1.0 - percentMissing);
						cmi.setDownweightedRange(downweightedRange);
						downweightedRangeSum += downweightedRange;
						PrivacySettings privacySettings =
								(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
						// TODO: make p configurable
						double p = 0.5;	// Bloom filter fill factor
						double n = (leftDataset.getTotalRecords() * leftCi.getAverageFieldLength() +
									rightDataset.getTotalRecords() * rightCi.getAverageFieldLength()) /
									totalRecords +
									(privacySettings.getBloomfilterSettings().getNGramSize()
									- 1);
						double kn = leftCi.getBloomFilterKParameter() * n;
						double valueFromBloomFormula = Math.pow(p, 1 / kn);	// root(kn,p)
						double dynamicBFLength = 1 / (1 - valueFromBloomFormula);
						cmi.setDynamicBloomFilterLength(dynamicBFLength);
						cmi.setBloomFilterFinalM((int)dynamicBFLength);
					} else {
						if (leftCi.getBloomFilterMParameter() != null && leftCi.getBloomFilterMParameter() != Integer.MIN_VALUE)
							cmi.setBloomFilterFinalM(leftCi.getBloomFilterMParameter());
					}
				}
			}
			log.debug(cmi.toStringLong());
			columnMatchInformation.add(cmi);
			if (!noMatchField)
				i++;
		}
		if (parameterManagerMode && !emOnly) {
			double maxPossibleLength = 0.0;
			log.debug("Calculating percentBits and BloomFilterPossibleM for ColumnMatchInformations...");
			for (ColumnMatchInformation cmi : columnMatchInformation) {
				if (!cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
					double percentBits = cmi.getDownweightedRange() / downweightedRangeSum;
					cmi.setPercentBits(percentBits);
					double possibleLength = cmi.getDynamicBloomFilterLength() / percentBits;
					cmi.setBloomFilterPossibleM(possibleLength);
					if (possibleLength > maxPossibleLength)
						maxPossibleLength = possibleLength;
				}
				log.debug(cmi.toStringLong());
			}
			log.debug("Calculating BloomFilterProposedM for ColumnMatchInformations...");
			for (ColumnMatchInformation cmi : columnMatchInformation) {
				if (!cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
					double proposedLength = maxPossibleLength * cmi.getPercentBits();
					cmi.setBloomFilterProposedM((int)proposedLength);
				}
				log.debug(cmi.toStringLong());
			}
		}
		personMatch.setColumnMatchInformation(columnMatchInformation);
		// TODO: remove this if user management will be better:
		User currentUser = Context.getUserContext().getUser();
		if (currentUser == null)	// TODO: should not happen in theory
			currentUser = leftDataset.getOwner();
		personMatch.setUserCreatedBy(currentUser);
		personMatch = personManagerService.addPersonMatch(personMatch);

		long linkPersistStartTime = System.nanoTime();
		if (!emOnly) {
			personManagerService.createLinkTable(linkTableName, leftTableName, rightTableName, false);

			if (persistLinks) {
				int countMatched = 0, countUnmatched = 0, countUndecided = 0;
				Long beginIndex = 0L;
				int size = 1;
				while (size > 0) {
					int fromIndex = Math.min(beginIndex.intValue(), pairs.size() - 1);
					int toIndex = Math.min(beginIndex.intValue() + Constants.PAGE_SIZE, pairs.size());
					if (beginIndex.intValue() < pairs.size()) {
						List<LeanRecordPair> subList = pairs.subList(fromIndex, toIndex);
						size = subList.size();
						if (size > 0) {
							List<PersonLink> personLinks = new ArrayList<PersonLink>();
							for (LeanRecordPair pair : subList) {
								Integer linkState = UniversalDaoHibernate.LINK_STATUS_UNDECIDED;
								if (pair.getWeight() <= fellegiSunterParams.getLowerBound()) {
									countUnmatched++;
									linkState = UniversalDaoHibernate.LINK_STATUS_NONMATCH;
								} else if (pair.getWeight() > fellegiSunterParams.getLowerBound() && pair.getWeight() < fellegiSunterParams.getUpperBound()) {
									countUndecided++;
								} else {
									countMatched++;
									linkState = UniversalDaoHibernate.LINK_STATUS_MATCH;
								}
								PersonLink personLink = GeneralUtil.constructPersonLink(personMatch.getPersonMatchId(), pair, linkState);
								personLinks.add(personLink);
							}
							personManagerService.addPersonLinks(linkTableName, personLinks);
							beginIndex += personLinks.size();
						}
					} else {
						size = 0;
					}
				}
				log.debug("We automatically matched " + countMatched + ", unmatched " + countUnmatched + " and have undecided: " + countUndecided);
			}
			personManagerService.addIndexesAndConstraintsToLinkTable(linkTableName, leftTableName, rightTableName);
		}
		long linkPersistEndTime = System.nanoTime();
		log.trace("ns of blocking + EM / FS: " + (blockNfsTime - startTime));
		log.trace("ns of file out + blocking + EM / FS: " + (fileOutTime - startTime));
		log.trace("ns from start to link persist: " + (linkPersistStartTime - startTime));
		log.trace("ns of link persist: " + (linkPersistEndTime - linkPersistStartTime));
		log.trace("ns total: " + (linkPersistEndTime - startTime));

		return personMatch;
	}

	public FellegiSunterParameters getFellegiSunterParameters() {
		return fellegiSunterParams;
	}
	
	public void init(Map<String, Object> configParameters) throws InitializationException {
		try {
			fellegiSunterParams = (FellegiSunterParameters) SerializationUtil.deserializeObject(
					Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory(),
					Constants.FELLEGI_SUNTER_CONFIG_FILE_NAME);
			initialized = true;
		} catch (Exception e) {
			initialized = false;
		}
	}

	public void getRecordPairs(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters,
			String leftTableName, String rightTableName, List<LeanRecordPair> pairs,
			boolean emOnly, FellegiSunterParameters fellegiSunterParams) throws ApplicationException {
		Context.getBlockingServiceSelector().getRecordPairs(blockingServiceTypeName, blockingServiceCustomParameters,
				matchingServiceTypeName, matchingServiceCustomParameters, leftTableName, rightTableName, pairs, emOnly,
				fellegiSunterParams);
	}

	public void calculateVectorFrequencies(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		for (int i=0; i < fellegiSunterParams.getVectorCount(); i++) {
			fellegiSunterParams.setVectorFrequency(i, 0);
		}
		for (LeanRecordPair pair : pairs) {
			ComparisonVector vector = pair.getComparisonVector();
			int vectorValue = vector.getBinaryVectorValue();
			fellegiSunterParams.incrementVectorFrequency(vectorValue);
		}
	}
	
	public void calculateRecordPairWeights(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		// Precompute wa, wd, nominator and denominator
		int fieldCount = fellegiSunterParams.getFieldCount();
		double wa[] = new double[fieldCount];
		double wd[] = new double[fieldCount];
		if (fieldCount > 1) {
			for (int i = 0; i < fellegiSunterParams.getFieldCount(); i++) {
				double mVali = fellegiSunterParams.getMValue(i);
				double uVali = fellegiSunterParams.getUValue(i);
				double numerator1 = adjustMinimumValue(mVali);
				double denominator1 = adjustMinimumValue(uVali);
				wa[i] = Math.log(numerator1/denominator1) / Math.log(2.0);
				double numerator2 = 1.0 - mVali;
				double denominator2 = 1.0 - uVali;
				numerator2 = adjustMinimumValue(numerator2);
				denominator2 = adjustMinimumValue(denominator2);
				wd[i] = Math.log(numerator2/denominator2) / Math.log(2.0);
			}
		}
		for (LeanRecordPair pair : pairs) {
			calculateRecordPairWeight(pair, fellegiSunterParams, wa, wd);
		}
	}
	
	protected abstract void calculateRecordPairWeight(LeanRecordPair pair, FellegiSunterParameters fellegiSunterParams,
											double wa[], double wd[]);

	private double adjustMinimumValue(double numerator) {
		if (numerator < MIN_MARGINAL_VALUE) {
			numerator = MIN_MARGINAL_VALUE;
		}
		return numerator;
	}

	public List<LeanRecordPair> orderRecordPairsByWeight(List<LeanRecordPair> pairs, boolean writeStat, String pathPrefix) {
		Collections.sort(pairs, new RecordPairComparatorByWeight());
		if (writeStat) {
			try {
				FileWriter fw = new FileWriter(pathPrefix + " " + Constants.WEIGHTS_FILE_NAME);
				BufferedWriter bw = new BufferedWriter(fw);
				try {
					for (LeanRecordPair pair : pairs) {
						bw.append(Double.toString(pair.getWeight()));	// TODO: write some kind of ids
						bw.newLine();	// System.getProperty("line.separator")
					}
				}
				finally {
					bw.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return pairs;
	}
	
	public List<LeanRecordPair> orderRecordPairsByProbabilityGivenM(List<LeanRecordPair> pairs) {
		Collections.sort(pairs, new RecordPairComparatorByProbabilityGivenM());
		return pairs;
	}
	
	public List<LeanRecordPair> orderRecordPairsByProbabilityGivenU(List<LeanRecordPair> pairs) {
		Collections.sort(pairs, new RecordPairComparatorByProbabilityGivenU());
		return pairs;
	}
	
	public void calculateMarginalProbabilities(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams,
			boolean writeStat, String pathPrefix) {
		double mpsum = 0.0;
		double upsum = 0.0;

		try {
			FileWriter fw = null;
			BufferedWriter bw = null;
			if (writeStat) {
				fw = new FileWriter(pathPrefix + "_" + Constants.MARGINAL_PROBABILITIES_FILE_NAME);
				bw = new BufferedWriter(fw);
			}
			try {
				for (LeanRecordPair pair : pairs) {
//					log.trace("Pair: " + getRecordPairMatchFields(pair));
					String marginalProbs = "";
					ComparisonVector vector = pair.getComparisonVector();
					vector.calculateProbabilityGivenMatch(fellegiSunterParams.getMValues(), useBinaryScores());
					vector.calculateProbabilityGivenNonmatch(fellegiSunterParams.getUValues(), useBinaryScores());
					mpsum += vector.getVectorProbGivenM();
					upsum += vector.getVectorProbGivenU();
					if (writeStat) {
						marginalProbs += (pair.getWeight() + "," + vector.getVectorProbGivenM() + "," + vector.getVectorProbGivenU());
						bw.append(marginalProbs);
						bw.newLine();	// System.getProperty("line.separator")
					}
				}
			}
			finally {
				if (writeStat)
					bw.close();
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		log.trace("mpsum: " + mpsum + " upsum: " + upsum);
	}
	
	public abstract void estimateMarginalProbabilities(FellegiSunterParameters fellegiSunterParams,
			MatchConfiguration matchConfig, int pairNumber);
	
	public void calculateLowerBound(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		double sum = 0;
		int index = 0;
		double probability = 0;
		for (LeanRecordPair pair : pairs) {
			probability = pair.getComparisonVector().getVectorProbGivenM();
			sum += probability;
			index++;
			if (sum > fellegiSunterParams.getLambda()) {
				break;
			}
		}
		log.debug("Sum: " + sum + ", lambda: " + fellegiSunterParams.getLambda() + ", index: " + (index - 1) +
				", weight: " + pairs.get(index - 1).getWeight() + ", prob: " + probability);
		fellegiSunterParams.setLowerBound(pairs.get(index-1).getWeight());
	}
		
	public void calculateUpperBound(List<LeanRecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
		double sum = 0;
		int index = 0;
		double probability = 0;
		index = pairs.size()-1;
		for (int i = index; i >= 0; i--) {
			probability = pairs.get(i).getComparisonVector().getVectorProbGivenU();
			sum += probability;
			if (sum > fellegiSunterParams.getMu()) {
				break;
			}
			index--;
		}
		if (index < 0)
			index = 0;
		log.debug("Sum: " + sum + ", mu: " + fellegiSunterParams.getMu() + ", index: " + index +
				", weight: " + pairs.get(index).getWeight() + ", prob: " + probability);
		fellegiSunterParams.setUpperBound(pairs.get(index).getWeight());
	}
	
	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}
}
