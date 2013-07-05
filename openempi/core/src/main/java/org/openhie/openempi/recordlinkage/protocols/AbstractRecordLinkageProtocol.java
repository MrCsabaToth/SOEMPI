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
package org.openhie.openempi.recordlinkage.protocols;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.MatchPairStatDao;
import org.openhie.openempi.dao.MatchPairStatHalfDao;
import org.openhie.openempi.dao.PersonMatchRequestDao;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.model.User;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocol;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.util.BitArray;
import org.openhie.openempi.util.ValidationUtil;

public abstract class AbstractRecordLinkageProtocol extends BaseServiceImpl implements RecordLinkageProtocol
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private String name;
	
	protected MatchPairStatDao matchPairStatDao;
	protected MatchPairStatHalfDao matchPairStatHalfDao;
	protected PersonMatchRequestDao personMatchRequestDao;

	private boolean usePseudoIds = true;
	private boolean isLshBlocking = false;
	private boolean isLocalThirdParty = false;

	public AbstractRecordLinkageProtocol() {
	}
	
	public AbstractRecordLinkageProtocol(String name) {
		this.name = name;
	}

	public void createMatchPairStatHalfTable(String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints)
	{
		log.debug("Request for create MatchPairStatHalf table " + statTableName);
		ValidationUtil.sanityCheckFieldName(statTableName);
		ValidationUtil.sanityCheckFieldName(datasetTableName);
		matchPairStatHalfDao.createTable(statTableName, datasetTableName, withIndexesAndConstraints);
	}
	
	/**
	 * Add a new match pair stat half to the system.
	 */
	public void addMatchPairStatHalf(String statTableName, MatchPairStatHalf matchPairStatHalf) {
		matchPairStatHalfDao.addMatchPairStatHalf(statTableName, matchPairStatHalf);
	}

	/**
	 * Add new match pair stat halves to the system.
	 */
	public void addMatchPairStatHalves(String statTableName, List<MatchPairStatHalf> matchPairStatHalves) {
		matchPairStatHalfDao.addMatchPairStatHalves(statTableName, matchPairStatHalves);
	}

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String statTableName, long seqStart, String datasetTableName)
	{
		ValidationUtil.sanityCheckFieldName(statTableName);
		ValidationUtil.sanityCheckFieldName(datasetTableName);
		matchPairStatHalfDao.addIndexesAndConstraints(statTableName, seqStart, datasetTableName);
	}
	
	protected boolean isHmacField(ColumnInformation ci) {
		return (ci.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob &&
				ci.getFieldTransformation().startsWith("Hmac"));
	}

	protected boolean isClearTextField(ColumnInformation ci) {
		return (ci.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.String &&
				ci.getFieldTransformation() == null);
	}

	protected boolean isBloomFilterField(ColumnInformation ci) {
		return (ci.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob &&
				ci.getFieldTransformation().contains("Bloom"));
	}

	protected List<ColumnInformation> getNoMatchColumnInformation(Dataset dataset) {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		String localTableName = dataset.getTableName();
		List<ColumnInformation> localColumnInformation = personQueryService.getDatasetColumnInformation(localTableName);
		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();	// column information which will be returned
		MatchConfiguration matchConfiguration =
				(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.NoMatchFields);
		for (MatchField matchField : matchFields) {
			ColumnInformation ci1 = null;
			ColumnInformation ci1r = null;
			// 1. Try to find the column addressed by the match configuration
			for (ColumnInformation ci : localColumnInformation) {
				if (ci.getFieldName().equals(matchField.getLeftFieldName())) {
					ci1 = ci;
					break;
				}
				if (ci.getFieldName().equals(matchField.getRightFieldName()))
					ci1r = ci;
			}
			if (ci1 == null)	// Didn't find it by left field name, get it by the right field name
				ci1 = ci1r;
			if (ci1 != null) {
				ColumnInformation ciClone = ci1.getClone();
				columnInformation.add(ciClone);
			}
		}
		return columnInformation;
	}
	
	protected List<ColumnInformation> getColumnsForPRLRequest(Dataset dataset, boolean hmacOrBF) throws ApplicationException
	{
		PersonQueryService personQueryService = Context.getPersonQueryService();
		String localTableName = dataset.getTableName();
		List<ColumnInformation> localColumnInformation = personQueryService.getDatasetColumnInformation(localTableName);
		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();	// column information which will be returned
		MatchConfiguration matchConfiguration =
				(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		List<MatchField> matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.MatchOnlyFields);
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int defaultK = privacySettings.getBloomfilterSettings().getDefaultK();
		for (MatchField matchField : matchFields) {
			ColumnInformation ci1 = null;
			ColumnInformation ci1r = null;
			// 1. Try to find the column addressed by the match configuration
			for (ColumnInformation ci : localColumnInformation) {
				if (ci.getFieldName().equals(matchField.getLeftFieldName())) {
					ci1 = ci;
					break;
				}
				if (ci.getFieldName().equals(matchField.getRightFieldName()))
					ci1r = ci;
			}
			if (ci1 == null)	// Didn't find it by left field name, get it by the right field name
				ci1 = ci1r;
			if (ci1 != null) {
				// 2.1. Try to find the clear text version of the column
				ColumnInformation ci2Clear = null;
				if (isClearTextField(ci1)) {
					ci2Clear = ci1;
				} else {
					for (ColumnInformation ci : localColumnInformation) {
						if (ci.getFieldMeaning().getFieldMeaningEnum() == ci1.getFieldMeaning().getFieldMeaningEnum() &&
							isClearTextField(ci))
						{
							ci2Clear = ci;
							break;
						}
					}
				}
				// 2.2. Try to find HMAC version of the column
				ColumnInformation ci2Hmac = null;
				if (isHmacField(ci1)) {
					ci2Hmac = ci1;
				} else {
					for (ColumnInformation ci : localColumnInformation) {
						if (ci.getFieldMeaning().getFieldMeaningEnum() == ci1.getFieldMeaning().getFieldMeaningEnum() &&
							isHmacField(ci))
						{
							ci2Hmac = ci;
							break;
						}
					}
				}
				// 2.3. Try to find bloom filter version of the column
				ColumnInformation ci2BF = null;
				if (isBloomFilterField(ci1)) {
					ci2BF = ci1;
				} else {
					for (ColumnInformation ci : localColumnInformation) {
						if (ci.getFieldMeaning().getFieldMeaningEnum() == ci1.getFieldMeaning().getFieldMeaningEnum() &&
							isBloomFilterField(ci))
						{
							ci2BF = ci;
							break;
						}
					}
				}
				if (ci2Clear != null) {
					// 3. Fill in the bloom filter parameters for that field
					ColumnInformation ciToSend = null;
					if (hmacOrBF) {
						if (ci2Hmac != null) {
							ciToSend = ci2Hmac.getClone();
							ciToSend.setAverageFieldLength(ci2Clear.getAverageFieldLength());
							ciToSend.setNumberOfMissing(ci2Clear.getNumberOfMissing());
						} else {
							ciToSend = ci2Clear.getClone();
						}
					}
					if (ci2BF != null) {
						if (hmacOrBF) {
							// We need to pass the corresponding Bloom Filter's K parameter
							ciToSend.setBloomFilterKParameter(ci2BF.getBloomFilterKParameter());
							ciToSend.setBloomFilterMParameter(ci2BF.getBloomFilterMParameter());
						} else {
							ciToSend = ci2BF.getClone();
							ciToSend.setAverageFieldLength(ci2Clear.getAverageFieldLength());
							ciToSend.setNumberOfMissing(ci2Clear.getNumberOfMissing());
						}
					}
					if (ciToSend.getBloomFilterKParameter() == null || ciToSend.getBloomFilterKParameter() == 0)
						ciToSend.setBloomFilterKParameter(defaultK);
					if (hmacOrBF)
						ciToSend.setFieldName(ci2Clear.getFieldName());
					columnInformation.add(ciToSend);
				} else {
					throw new ApplicationException("Couldn't find clear text, BF or HMAC version for PRL match field: " +
							ci1.getFieldName());
				}
			} else {
				throw new ApplicationException("Couldn't find PRL match field: " + matchField.getLeftFieldName() + " (left) or " +
						matchField.getRightFieldName() + " (right)");
			}
		}
		return columnInformation;
	}

	protected PersonMatchRequest createPersonMatchRequest(Dataset dataset, byte[] myDhPublicKey, String matchName,
			String blockingServiceName, String matchingServiceName) {
		User currentUser = Context.getPersonManagerService().getCurrentUser(dataset.getOwner());
		log.debug("Current user is " + currentUser);
		java.util.Date now = new java.util.Date();

		PersonMatchRequest personMatchRequest = new PersonMatchRequest();
		personMatchRequest.setDataset(dataset);
		personMatchRequest.setMatchName(matchName);
		personMatchRequest.setBlockingServiceName(blockingServiceName);
		personMatchRequest.setMatchingServiceName(matchingServiceName);
		personMatchRequest.setDhPublicKey(myDhPublicKey);
		personMatchRequest.setCompleted(false);
		personMatchRequest.setDateCreated(now);
		personMatchRequest.setUserCreatedBy(currentUser);
		return personMatchRequest;
	}

	abstract public PersonMatchRequest sendPersonMatchRequest(Dataset dataset, String remoteTableName, String matchName,
		String keyServerUserName, String keyServerPassword,
		String dataIntegratorUserName, String dataIntegratorPassword,
		String parameterManagerUserName, String parameterManagerPassword);

	protected FunctionField getNewFunctionFieldForBFReencoding(int defaultK, ColumnMatchInformation cmi) {
		FunctionField functionField = new FunctionField();
		String bloomFunctionName = Constants.DEFAULT_BLOOM_FILTER_FUNCTION_NAME;
		functionField.setFunctionName(bloomFunctionName);
		Map<String, Object> functionParameters = new HashMap<String, Object>();
		functionParameters.put("k", defaultK);
		functionParameters.put("m", cmi.getBloomFilterFinalM());
		functionField.setFunctionParameters(functionParameters);
		return functionField;
	}

	protected Map<String, int[][]> generateBitPermutation(Random rnd, int cbfLength,
			List<ColumnMatchInformation> columnMatchInformation, boolean leftOrRightSide)
	{
		Map<String, int[][]> bitPermutation = new HashMap<String, int[][]>();
		int[] bitTargetPermutation = new int[cbfLength];
		int[] bitIndicator = new int[cbfLength];
		int i = 0;
		while(i < cbfLength) {
			int nextBit = rnd.nextInt(cbfLength);
			if (bitIndicator[nextBit] == 0) {
				bitIndicator[nextBit] = 1;
				bitTargetPermutation[i] = nextBit;
				i++;
			}
		}
		i = 0;
		for (ColumnMatchInformation cmi : columnMatchInformation) {
			if (cmi.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob &&
				!cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME))
			{
				String fn = leftOrRightSide ? cmi.getLeftFieldName() : cmi.getRightFieldName();
				int howManyTimesToSample = cmi.getBloomFilterProposedM();
				int fromHowBigPool = cmi.getBloomFilterFinalM();
				// Round up the size to multiplicants of 8, so later the BitArray.performPermutationPart
				// doesn't have to check fractions in case of the last byte
				int roundedUpSize = ((fromHowBigPool + BitArray.BITS_PER_UNIT - 1) / BitArray.BITS_PER_UNIT) * BitArray.BITS_PER_UNIT;
				int[][] permBitCombination = new int[roundedUpSize][];
				for (int j = 0; j < howManyTimesToSample; j++, i++) {
					int sourceBit = rnd.nextInt(fromHowBigPool);
					if (permBitCombination[sourceBit] == null) {
						permBitCombination[sourceBit] = new int[] { bitTargetPermutation[i] };
					} else {
						int[] oldArray = permBitCombination[sourceBit];
						int newArrayLength = permBitCombination[sourceBit].length + 1;
						permBitCombination[sourceBit] = Arrays.copyOf(oldArray, newArrayLength);
						permBitCombination[sourceBit][newArrayLength - 1] = bitTargetPermutation[i];
					}
				}
				for (int k = 0; k < fromHowBigPool; k++) {
					if (permBitCombination[k] != null)
						Arrays.sort(permBitCombination[k]);
				}
				bitPermutation.put(fn, permBitCombination);
			}
		}
		return bitPermutation;
	}

	abstract public void handleBloomFilterParameterAdvice(
			String blockingServiceName, String matchingServiceName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			Dataset leftLocalDataset, Dataset leftRemoteDataset,
			Dataset rightRemoteDataset, List<ColumnMatchInformation> columnMatchInformation,
			List<MatchPairStatHalf> matchPairStatHalves, Map<Long,Long> personPseudoIdsReverseLookup,
			int sharedSecret, boolean leftOrRightSide, String matchName) throws ApplicationException;

	protected List<MatchPairStatHalf> retrieveMatchPairStatHalves(String matchPairStatHalfTableName) {
		if (matchPairStatHalfTableName == null)
			return null;
		if (matchPairStatHalfTableName.length() <= 0)
			return null;
		List<MatchPairStatHalf> matchPairStatHalves = new ArrayList<MatchPairStatHalf>();
		long start = 0L;
		int numRead = 0;
		do {
			List<MatchPairStatHalf> matchPairStatHalvesPart =
					matchPairStatHalfDao.getMatchPairStatHalvesPaged(matchPairStatHalfTableName, start, Constants.PAGE_SIZE);
			matchPairStatHalves.addAll(matchPairStatHalvesPart);
			numRead = matchPairStatHalvesPart.size();
			start += numRead;
		} while (numRead > 0);
		return matchPairStatHalves;
	}

	protected void persistMatchPairStatHalves(String matchPairStatHalfTableName, String datasetTableName, List<MatchPairStatHalf> matchPairStatHalves) {
		matchPairStatHalfDao.createTable(matchPairStatHalfTableName, datasetTableName, false);
		long start = 0L;
		int toIndex = 0;
		do {
			int fromIndex = (int)start;
			toIndex = fromIndex + Constants.PAGE_SIZE;
			if (toIndex > matchPairStatHalves.size())
				toIndex = matchPairStatHalves.size();
			List<MatchPairStatHalf> matchPairStatHalvesPart = matchPairStatHalves.subList(fromIndex, toIndex);
			matchPairStatHalfDao.addMatchPairStatHalves(matchPairStatHalfTableName, matchPairStatHalvesPart);
			start += Constants.PAGE_SIZE;
		} while (toIndex < matchPairStatHalves.size());
		matchPairStatHalfDao.addIndexesAndConstraints(matchPairStatHalfTableName, matchPairStatHalves.size() + 1, datasetTableName);
	}

	protected String getNowString() {
		Date dateNow = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT_STRING_WO_YEAR);
		return dateFormat.format(dateNow);
	}

	abstract protected String getMatchingServiceTypeName(ComponentType componentType);

	abstract protected String getBlockingServiceTypeName(ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException;

	public Integer handlePersonMatchRequest(String tableName, String matchName, byte[] dhPublicKey,
			String matchPairStatHalfTableName) throws ApplicationException {
		Dataset localDataset = Context.getPersonManagerService().getDatasetByTableName(tableName);
		if (localDataset == null)
			throw new ApplicationException("Cannot find the local table with name: " + tableName);

		ComponentType componentType = Context.getConfiguration().getAdminConfiguration().getComponentMode();;
		String blockingServiceName = getBlockingServiceTypeName(componentType, null);
		String matchingServiceName = getMatchingServiceTypeName(componentType);
		PersonMatchRequest personMatchRequest =
				createPersonMatchRequest(localDataset, dhPublicKey, matchName, blockingServiceName, matchingServiceName);
		if (matchPairStatHalfTableName != null && matchPairStatHalfTableName.length() > 0)
			personMatchRequest.setMatchPairStatHalfTableName(matchPairStatHalfTableName);
		personMatchRequest = personMatchRequestDao.addPersonMatchRequest(personMatchRequest);
		return personMatchRequest.getPersonMatchRequestId();
	}

	public BloomFilterParameterAdvice acquireMatchRequests(int personMatchRequestId, ComponentType componentType) {
		PersonMatchRequest personMatchRequest = personMatchRequestDao.getPersonMatchRequest(personMatchRequestId);
		log.debug("PersonMatchRequest searching for match named " + personMatchRequest.getMatchName());

		boolean found = false;
		List<PersonMatchRequest> personMatchRequests = null;
		do {
			personMatchRequests = personMatchRequestDao.getPersonMatchRequestsForMatchName(personMatchRequest.getMatchName());
			if (personMatchRequests != null) {
				if (personMatchRequests.size() > 0) {
					found = true;
				}
			}
			if (!found) {
				try {
					log.debug("Caller is waiting for match request pair named " + personMatchRequest.getMatchName() + "...");
					Thread.sleep(Constants.STANDARD_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while(!found);

		log.debug("Caller paired match named " + personMatchRequest.getMatchName() + "!");
		PersonMatchRequest otherPersonMatchRequest = personMatchRequests.get(0);
		// Decide which will perform the computation and who will wait
		// Who has the smaller personMatchRequestId (probably the first caller and waiter) will perform the computation
		// The other will wait more for the results.

		if (personMatchRequest.getPersonMatchRequestId() > otherPersonMatchRequest.getPersonMatchRequestId()) {
			log.debug("Caller has bigger PersonMatchRequestId (" + personMatchRequest.getPersonMatchRequestId() +
					" vs " + otherPersonMatchRequest.getPersonMatchRequestId() + ") so will start computation");
			try {
				personMatchRequestAcquired(personMatchRequest, otherPersonMatchRequest, componentType, null);
			} catch (ApplicationException e) {
				log.error("linkrecords was not successful after pairing two person match requests in addPersonMatchRequest");
				e.printStackTrace();
			}
		} else {
			if (componentType == ComponentType.PARAMETER_MANAGER_MODE) {
				log.debug("Caller has smaller PersonMatchRequestId (" + personMatchRequest.getPersonMatchRequestId() +
						" vs " + otherPersonMatchRequest.getPersonMatchRequestId() + ") so we'll return with the result or with null and poll later");
				if (personMatchRequestDao.getPersonMatchRequest(personMatchRequest.getPersonMatchRequestId()).getCompleted())
					return getBloomFilterParameterAdviceForRightSide(personMatchRequest, otherPersonMatchRequest);
			}	// else in case of DATA_INTEGRATOR_MODE we won't return data
		}
		return null;
	}

	abstract public void testPMLinkRecords(int leftDatasetId, int rightDatasetId, String blockingServiceName,
			String matchingServiceName) throws ApplicationException;

	abstract public void testPRLLinkRecords(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException;

	abstract public void testBFReencoding(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException;

	abstract public void testHMACEncoding(int dataSetId, String tableName) throws ApplicationException;

	protected BloomFilterParameterAdvice getBloomFilterParameterAdviceForRightSide(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest) {
		// prepare the return information for the right side
		Dataset leftDataset = leftPersonMatchRequest.getDataset();
		Dataset rightDataset = rightPersonMatchRequest.getDataset();
		BloomFilterParameterAdvice rightBFPA = new BloomFilterParameterAdvice();
		rightBFPA.setPersonMatchReuqest(leftPersonMatchRequest);
		rightBFPA.setLeftDataset(leftDataset);
		rightBFPA.setRightDataset(rightDataset);
		int personMatchId = leftPersonMatchRequest.getPersonMatchId();
		PersonMatch personMatch = Context.getPersonQueryService().getPersonMatch(personMatchId);
		rightBFPA.setColumnMatchInformation(personMatch.getColumnMatchInformation());
		String matchPairStatHalfTableName = leftPersonMatchRequest.getMatchPairStatHalfTableName();
		if (matchPairStatHalfTableName != null) {
			if (matchPairStatHalfTableName.length() > 0) {
				List<MatchPairStatHalf> matchPairStatHalfRight = retrieveMatchPairStatHalves(matchPairStatHalfTableName);
				rightBFPA.setMatchPairStatHalves(matchPairStatHalfRight);
			}
		}
		rightBFPA.setDhPublicKey(leftPersonMatchRequest.getDhPublicKey());
		rightBFPA.setLeftOrRightSide(false);
		return rightBFPA;
	}

	abstract protected BloomFilterParameterAdvice personMatchRequestAcquired(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException;

	protected boolean isLocalAddress(String address) {
		return address.equals(Constants.LOCALHOST_IP_ADDRESS) ||
			   address.equals(Constants.LOCALHOST_NAME);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public MatchPairStatDao getMatchPairStatDao() {
		return matchPairStatDao;
	}

	public void setMatchPairStatDao(MatchPairStatDao matchPairStatDao) {
		this.matchPairStatDao = matchPairStatDao;
	}

	public MatchPairStatHalfDao getMatchPairStatHalfDao() {
		return matchPairStatHalfDao;
	}

	public void setMatchPairStatHalfDao(MatchPairStatHalfDao matchPairStatHalfDao) {
		this.matchPairStatHalfDao = matchPairStatHalfDao;
	}

	public PersonMatchRequestDao getPersonMatchRequestDao() {
		return personMatchRequestDao;
	}

	public void setPersonMatchRequestDao(PersonMatchRequestDao personMatchRequestDao) {
		this.personMatchRequestDao = personMatchRequestDao;
	}

	public boolean getUsePseudoIds() {
		return usePseudoIds;
	}
	
	public void setUsePseudoIds(boolean usePseudoIds) {
		this.usePseudoIds = usePseudoIds;
	}

	public boolean getIsLshBlocking() {
		return isLshBlocking;
	}
	
	public void setIsLshBlocking(boolean isLshBlocking) {
		this.isLshBlocking = isLshBlocking;
	}

	public boolean getIsLocalThirdParty() {
		return isLocalThirdParty;
	}
	
	public void setIsLocalThirdParty(boolean isLocalThirdParty) {
		this.isLocalThirdParty = isLocalThirdParty;
	}

}
