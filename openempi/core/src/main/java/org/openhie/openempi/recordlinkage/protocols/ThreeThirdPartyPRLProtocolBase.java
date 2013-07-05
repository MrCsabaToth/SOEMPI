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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.basicblocking.BasicBlockingConstants;
import org.openhie.openempi.blocking.basicblocking.BlockingSettings;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.recordlinkage.configuration.ComponentSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.util.DiffieHellmanKeyExchange;
import org.openhie.openempi.util.GeneralUtil;

public abstract class ThreeThirdPartyPRLProtocolBase extends MultiPartyPRLProtocolBase
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public ThreeThirdPartyPRLProtocolBase() {
	}

	protected boolean twoOrThreeThirdParty() {
		return false;
	}

	protected String getThirdPartyAddress() {
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		ComponentSettings cps = privacySettings.getComponentSettings();
		return cps.getParameterManagerSettings().getServerAddress();
	}

	protected String getThirdPartyCredential(String dataIntegratorCredential, String parameterManagerCredential) {
		return parameterManagerCredential;
	}

	protected boolean fillColumnInformationForSend(List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> columnInformation, List<String> columnNames)
	{
		boolean isThereClearField = false;
		String defaultHmacFunctionName = Constants.DEFAULT_HMAC_FUNCTION_NAME;
		// Searching for default HMAC transformation
		for (ColumnInformation ci : matchColumnInformation) {
			if (ci.getFieldTransformation() != null) {
				defaultHmacFunctionName = ci.getFieldTransformation();
				break;
			}
		}
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int defaultK = privacySettings.getBloomfilterSettings().getDefaultK();
		for (ColumnInformation ci : matchColumnInformation) {
			columnNames.add(ci.getFieldName());
			ColumnInformation ciClone = ci.getClone();
			if (isClearTextField(ci)) {
				isThereClearField = true;
				ciClone.setFieldTransformation(defaultHmacFunctionName);	// Data sent will be all HMAC
				ciClone.setFieldType(FieldType.FieldTypeEnum.Blob);
			}
			ciClone.setBloomFilterKParameter(defaultK);
			columnInformation.add(ciClone);
		}
		return isThereClearField;
	}

	private List<Person> hmacEncodeSamplesForSend(Dataset dataset, long totalRecords, List<String> columnNames, List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> noMatchColumnInformation, boolean isThereClearField, String defaultHmacFunctionName) {
		Configuration config = Context.getConfiguration();
		BlockingSettings blockingSettings = (BlockingSettings)
				config.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY);
		int numRecordsToSend1 = blockingSettings.getNumberOfRecordsToSample();
		int numRecordsToSend = Math.min(numRecordsToSend1, ((Long)totalRecords).intValue());
		// Do not break down random sampling by PAGE_SIZE, because that could cause id collision
		PersonQueryService personQueryService = Context.getPersonQueryService();
		log.warn("Random sampling for HMAC encode begin");
		List<Person> persons = personQueryService.getRandomNotNullPersons(dataset.getTableName(),
				fillNoMatchColumnNames(noMatchColumnInformation, columnNames), numRecordsToSend);
		log.warn("Random sampling for HMAC encode end");
		// HMAC transform the needed fields
		if (isThereClearField) {
			TransformationService transformationService = Context.getTransformationService();
			FunctionField defaultHmac = new FunctionField(defaultHmacFunctionName);
			KeyServerService ks = Context.getKeyServerService();
			byte[] signingKey = ks.getSalts(1).get(0);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, signingKey);
			log.warn("HMAC encode begin");
			for (Person person : persons) {
				for (ColumnInformation ci : matchColumnInformation) {
					if (isClearTextField(ci)) {
						Object value = person.getAttribute(ci.getFieldName());
						log.debug("Obtained a value of " + value + " for clear text field " + ci.getFieldName());
						if (value != null) {
							Object transformedValue = transformationService.transform(defaultHmac, value, parameters);
							person.setAttribute(ci.getFieldName(), transformedValue);
							log.debug("Field " + ci.getFieldName() + " has now value " +
									person.getAttribute(ci.getFieldName()));
						}
					}
				}
			}
			log.warn("HMAC encode end");
		}
		return persons;
	}

	protected void sendFirstPhaseData(Dataset dataset, long totalRecords, List<String> columnNames, List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> noMatchColumnInformation, boolean isThereClearField, String defaultHmacFunctionName, String thirdPartyAddress,
			Map<Long,Long> personPseudoIdsReverseLookup, RemotePersonService remotePersonService, String remoteTableName) throws NamingException, ApplicationException {
		List<Person> persons = hmacEncodeSamplesForSend(dataset, totalRecords, columnNames, matchColumnInformation,
				noMatchColumnInformation, isThereClearField, defaultHmacFunctionName);
		// Don't use pseudoIds for local experiments
		if (!thirdPartyAddress.equals(Constants.LOCALHOST_IP_ADDRESS) &&
			!thirdPartyAddress.equals(Constants.LOCALHOST_NAME))
		{
			long i = 0L;
			for (Person person : persons) {
				personPseudoIdsReverseLookup.put(i, person.getPersonId());
				person.setPersonId(i);
				i++;
			}
		}
		remotePersonService.addPersons(remoteTableName, persons, false, false);
	}

	protected void performSecondPhase(Dataset dataset, String matchName,
			String blockingServiceName, String matchingServiceName, String thirdPartyAddress,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword,
			int personMatchRequestId, DiffieHellmanKeyExchange dhke,
			Map<Long,Long> personPseudoIdsReverseLookup) throws NamingException, ApplicationException
	{
		RemotePersonService remotePersonService = Context.getRemotePersonService();
		BloomFilterParameterAdvice bfpa = null;
		while (bfpa == null) {
			remotePersonService.authenticate(thirdPartyAddress, parameterManagerUserName, parameterManagerPassword,
					keyServerUserName, keyServerPassword);
			bfpa = remotePersonService.acquireMatchRequests(getName(), personMatchRequestId, ComponentType.PARAMETER_MANAGER_MODE);
			remotePersonService.close();
			if (bfpa == null) {
				try {
					log.debug("Waiting " + Constants.STANDARD_SLEEP_TIME + " ms to call back w " + personMatchRequestId + " personMatchRequestId for the results...");
					Thread.sleep(Constants.STANDARD_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		int sharedSecret = dhke.computeSharedSecret(bfpa.getDhPublicKey());
		handleBloomFilterParameterAdvice(blockingServiceName, matchingServiceName,
				keyServerUserName, keyServerPassword,
				dataIntegratorUserName, dataIntegratorPassword, dataset,
				bfpa.getLeftDataset(), bfpa.getRightDataset(), bfpa.getColumnMatchInformation(),
				bfpa.getMatchPairStatHalves(), personPseudoIdsReverseLookup, sharedSecret,
				bfpa.isLeftOrRightSide(), matchName);
	}

	public void testPMLinkRecords(int leftDatasetId, int rightDatasetId, String blockingServiceName,
			String matchingServiceName) throws ApplicationException {
		log.warn("PAM EM calculation Start");
		SecureRandom rnd = new SecureRandom();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		Dataset leftDataset = personManagerService.getDatasetById(leftDatasetId);
		int leftNonce = rnd.nextInt();
		DiffieHellmanKeyExchange dhkeLeft = new DiffieHellmanKeyExchange(leftNonce);
		BigInteger dhPublicKeyLeft = dhkeLeft.computePublicKey();
		PersonMatchRequest leftPersonMatchRequest = createPersonMatchRequest(leftDataset, dhPublicKeyLeft.toByteArray(),
				Constants.LOCALHOST_IP_ADDRESS, blockingServiceName, matchingServiceName);
		leftPersonMatchRequest = personMatchRequestDao.addPersonMatchRequest(leftPersonMatchRequest);

		Dataset rightDataset = personManagerService.getDatasetById(rightDatasetId);
		int rightNonce = rnd.nextInt();
		DiffieHellmanKeyExchange dhkeRight = new DiffieHellmanKeyExchange(rightNonce);
		BigInteger dhPublicKeyRight = dhkeRight.computePublicKey();
		PersonMatchRequest rightPersonMatchRequest = createPersonMatchRequest(rightDataset, dhPublicKeyRight.toByteArray(),
				Constants.LOCALHOST_IP_ADDRESS, blockingServiceName, matchingServiceName);
		rightPersonMatchRequest = personMatchRequestDao.addPersonMatchRequest(rightPersonMatchRequest);

		linkRecords(leftPersonMatchRequest, rightPersonMatchRequest, ComponentType.PARAMETER_MANAGER_MODE, null);
		log.warn("PAM EM calculation End");
	}

	public void testBFReencoding(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException {
		PersonMatchRequest leftPersonMatchRequest = personMatchRequestDao.getPersonMatchRequest(leftPersonMatchRequestId);
		PersonMatchRequest rightPersonMatchRequest = personMatchRequestDao.getPersonMatchRequest(rightPersonMatchRequestId);

		int leftDhSecret = leftPersonMatchRequest.getDhSecret();
		DiffieHellmanKeyExchange dhkeLeft = new DiffieHellmanKeyExchange(leftDhSecret);
		BigInteger dhPublicKeyLeft = dhkeLeft.computePublicKey();
		byte[] dhPublicKeyEncLeft = dhPublicKeyLeft.toByteArray();
		Assert.assertArrayEquals(dhPublicKeyEncLeft, leftPersonMatchRequest.getDhPublicKey());

		int rightDhSecret = rightPersonMatchRequest.getDhSecret();
		DiffieHellmanKeyExchange dhkeRight = new DiffieHellmanKeyExchange(rightDhSecret);
		BigInteger dhPublicKeyRight = dhkeRight.computePublicKey();
		byte[] dhPublicKeyEncRight = dhPublicKeyRight.toByteArray();
		Assert.assertArrayEquals(dhPublicKeyEncRight, rightPersonMatchRequest.getDhPublicKey());

		int sharedSecretLeft = dhkeLeft.computeSharedSecret(dhPublicKeyEncRight);
		int sharedSecretRight = dhkeRight.computeSharedSecret(dhPublicKeyEncLeft);
		Assert.assertEquals(sharedSecretLeft, sharedSecretRight);

		BloomFilterParameterAdvice leftBfpa = getBloomFilterParameterAdviceForRightSide(leftPersonMatchRequest, rightPersonMatchRequest);
		handleBloomFilterParameterAdvice(
				leftPersonMatchRequest.getBlockingServiceName(), leftPersonMatchRequest.getMatchingServiceName(),
				Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD,
				Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD,
				leftPersonMatchRequest.getDataset(), leftPersonMatchRequest.getDataset(), rightPersonMatchRequest.getDataset(),
				leftBfpa.getColumnMatchInformation(), leftBfpa.getMatchPairStatHalves(),
				null, sharedSecretLeft, true, Constants.LOCALHOST_IP_ADDRESS);

		BloomFilterParameterAdvice rightBfpa = getBloomFilterParameterAdviceForRightSide(rightPersonMatchRequest, leftPersonMatchRequest);
		handleBloomFilterParameterAdvice(
				rightPersonMatchRequest.getBlockingServiceName(), rightPersonMatchRequest.getMatchingServiceName(),
				Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD,
				Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD,
				rightPersonMatchRequest.getDataset(), rightPersonMatchRequest.getDataset(), leftPersonMatchRequest.getDataset(),
				rightBfpa.getColumnMatchInformation(), rightBfpa.getMatchPairStatHalves(),
				null, sharedSecretRight, false, Constants.LOCALHOST_IP_ADDRESS);
	}

	public void testHMACEncoding(int dataSetId, String tableName) throws ApplicationException
	{
		PersonManagerService personManagerService = Context.getPersonManagerService();
		Dataset dataset = personManagerService.getDatasetById(dataSetId);
		List<ColumnInformation> allCiList = personManagerService.getDatasetColumnInformation(dataset.getTableName());
		List<ColumnInformation> ciToEncode = new ArrayList<ColumnInformation>();
		List<ColumnInformation> ciNotToEncode = new ArrayList<ColumnInformation>();
		List<String> columnNamesToEncode = new ArrayList<String>();
		for (ColumnInformation ci : allCiList) {
			if (ci.getFieldType().getFieldTypeEnum() == FieldTypeEnum.String &&
			    ci.getFieldTransformation() == null)
			{
				if (ci.getFieldMeaning().getFieldMeaningEnum() != FieldMeaningEnum.OriginalId) {
					ciToEncode.add(ci);
					columnNamesToEncode.add(ci.getFieldName());
				} else if (tableName != null && !tableName.isEmpty()) {
					ciNotToEncode.add(ci);
				}
			}
		}
		List<Person> persons = hmacEncodeSamplesForSend(dataset, dataset.getTotalRecords(), columnNamesToEncode, ciToEncode,
				ciNotToEncode, true, Constants.DEFAULT_HMAC_FUNCTION_NAME);
		for (ColumnInformation ci : ciNotToEncode) {
			ciToEncode.add(0, ci);	// insert
		}
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int defaultK = privacySettings.getBloomfilterSettings().getDefaultK();
		if (tableName != null && !tableName.isEmpty()) {
			List<ColumnInformation> ciClone = GeneralUtil.cloneColumnInformationList(ciToEncode);
			for (ColumnInformation ci : ciClone) {
				if (ci.getFieldMeaning().getFieldMeaningEnum() != FieldMeaningEnum.OriginalId) {
					ci.setFieldType(FieldTypeEnum.Blob);
					ci.setFieldTransformation(Constants.DEFAULT_HMAC_FUNCTION_NAME);
					ci.setFieldTypeModifier(null);
				}
				ci.setBloomFilterKParameter(defaultK);
			}
			personManagerService.createDatasetTable(tableName, ciClone, persons.size(), false, false);
			personManagerService.addPersons(tableName, persons, false, false);
			personManagerService.addIndexesAndConstraintsToDatasetTable(tableName, persons.size() + 1);
		}
	}

	protected BloomFilterParameterAdvice personMatchRequestAcquired(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException
	{
		if (componentType == ComponentType.DATA_INTEGRATOR_MODE) {
			return linkPRLRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType);
		} else if (componentType == ComponentType.PARAMETER_MANAGER_MODE) {
			return linkRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType, null);
		}
		log.error("Unhandled component type in personMatchRequestAcquired: " + componentType);
		return null;
	}

	protected String getBlockingServiceTypeName(ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException {
		if (componentType == ComponentType.DATA_INTEGRATOR_MODE) {
			if (getIsLshBlocking()) {
				if (matchPairStats == null)
					throw new ApplicationException("LSH blocking helping is specified but MatchPairStats is null");
				return Constants.LSH_WITH_CBF_MULTI_PARTY_SERVICE_NAME;
			}
			return Constants.PPB_WITH_CRYPTO_RANDOM_BITS_SERVICE_NAME;
		} else if (componentType == ComponentType.PARAMETER_MANAGER_MODE) {
			return Constants.BLOCKING_BYPASS_SERVICE_NAME;
		}
		throw new ApplicationException("Unkown Component Type: " + componentType);
	}

}
