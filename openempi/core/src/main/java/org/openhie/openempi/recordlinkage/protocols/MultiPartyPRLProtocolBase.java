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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingField;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingSettings;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.hibernate.UniversalDaoHibernate;
import org.openhie.openempi.loader.DataLoaderServiceSelector;
import org.openhie.openempi.loader.DataLoaderService;
import org.openhie.openempi.loader.file.ConfigurableFileLoader;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.matching.MatchingServiceSelector;
import org.openhie.openempi.matching.MatchingServiceType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingServiceBase;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.recordlinkage.configuration.DataIntegratorSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.util.BitArray;
import org.openhie.openempi.util.ParallelIteratorUtil;
import org.openhie.openempi.util.ParallelMatchPairHalfStatListIterator;
import org.openhie.openempi.util.ValidationUtil;

public abstract class MultiPartyPRLProtocolBase extends AbstractRecordLinkageProtocol
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public MultiPartyPRLProtocolBase() {
	}

	protected abstract boolean twoOrThreeThirdParty();
	protected abstract String getThirdPartyAddress();
	protected abstract String getThirdPartyCredential(String dataIntegratorUserName, String parameterManagerUserName);
	protected abstract boolean fillColumnInformationForSend(List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> columnInformation, List<String> columnNames);
	protected abstract void sendFirstPhaseData(Dataset dataset, long totalRecords, List<String> matchColumnNames,
			List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> noMatchColumnInformation, boolean isThereClearField, String defaultHmacFunctionName,
			String thirdPartyAddress, Map<Long,Long> personPseudoIdsReverseLookup, RemotePersonService remotePersonService,
			String remoteTableName) throws NamingException, ApplicationException;
	protected abstract void performSecondPhase(Dataset dataset, String matchName, String blockingServiceName, String matchingServiceName,
			String thirdPartyAddress, String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword,
			int personMatchRequestId, int myNonce, Map<Long,Long> personPseudoIdsReverseLookup) throws NamingException, ApplicationException;
	protected abstract int getNonce();

	protected void addNoMatchColumnInformationToSend(List<ColumnInformation> noMatchColumnInformation,
			List<ColumnInformation> columnInformation, List<String> columnNames)
	{
		for (ColumnInformation ci : noMatchColumnInformation) {
			columnNames.add(ci.getFieldName());
			ColumnInformation ciClone = ci.getClone();
			columnInformation.add(ciClone);
		}
	}

	protected List<String> fillNoMatchColumnNames(List<ColumnInformation> noMatchColumnInformation, List<String> columnNames)
	{
		List<String> allColumnNames = new ArrayList<String>();
		allColumnNames.addAll(columnNames);
		for (ColumnInformation ci : noMatchColumnInformation)
			allColumnNames.add(ci.getFieldName());
		return allColumnNames;
	}

	public PersonMatchRequest sendPersonMatchRequest(Dataset dataset, String remoteTableName,
			String matchName, String blockingServiceName, String matchingServiceName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword)
	{
		ValidationUtil.sanityCheckFieldName(remoteTableName);
		ValidationUtil.sanityCheckFieldName(matchName);

		String thirdPartyAddress = getThirdPartyAddress();
		String thirdPartyUserName = getThirdPartyCredential(dataIntegratorUserName, parameterManagerUserName);
		String thirdPartyPassword = getThirdPartyCredential(dataIntegratorPassword, parameterManagerPassword);

		PersonMatchRequest personMatchRequest = null;
		RemotePersonService remotePersonService = Context.getRemotePersonService();
		try {
			remotePersonService.authenticate(thirdPartyAddress, thirdPartyUserName, thirdPartyPassword,
					keyServerUserName, keyServerPassword);

			// 1. Send DataSet and patient data first
			List<ColumnInformation> matchColumnInformation = getColumnsForPRLRequest(dataset, !twoOrThreeThirdParty());
			List<ColumnInformation> noMatchColumnInformation = getNoMatchColumnInformation(dataset);
			List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();
			List<String> matchColumnNames = new ArrayList<String>();
			String defaultHmacFunctionName = Constants.DEFAULT_HMAC_FUNCTION_NAME;
			boolean isThereClearField = fillColumnInformationForSend(matchColumnInformation, columnInformation, matchColumnNames);
			
			long totalRecords = dataset.getTotalRecords();
			List<ColumnInformation> allColumnInformation = new ArrayList<ColumnInformation>();
			allColumnInformation.addAll(matchColumnInformation);
			allColumnInformation.addAll(noMatchColumnInformation);
			remotePersonService.createDatasetTable(remoteTableName, allColumnInformation, totalRecords, false);

			Map<Long,Long> personPseudoIdsReverseLookup = null;
			// Don't use pseudoIds for local experiments
			if (!thirdPartyAddress.equals(Constants.LOCALHOST_IP_ADDRESS) &&
				!thirdPartyAddress.equals(Constants.LOCALHOST_NAME))
			{
				personPseudoIdsReverseLookup = new HashMap<Long,Long>();
			}
			sendFirstPhaseData(dataset, totalRecords, matchColumnNames, matchColumnInformation,
					noMatchColumnInformation, isThereClearField, defaultHmacFunctionName,
					thirdPartyAddress, personPseudoIdsReverseLookup, remotePersonService, remoteTableName);
			remotePersonService.addIndexesAndConstraintsToDatasetTable(remoteTableName);

			// 2. Send MatchRequest right after
			// Preliminary steps
			int myNonce = getNonce();

			personMatchRequest = createPersonMatchRequest(dataset, myNonce, matchName, blockingServiceName, matchingServiceName);
			personMatchRequest = personMatchRequestDao.addPersonMatchRequest(personMatchRequest);
			int personMatchRequestId = remotePersonService.addPersonMatchRequest(getName(), remoteTableName, matchName,
					blockingServiceName, matchingServiceName, myNonce, null);
			remotePersonService.close();	// Need to close the context, so the PersonMatchRequest and other data
											// waiting in the Hibernate 2nd level cache will be flushed after the EJB call returns

			if (thirdPartyAddress.equals(Constants.LOCALHOST_IP_ADDRESS) ||
				thirdPartyAddress.equals(Constants.LOCALHOST_NAME))
			{	// Update dataset with the source file name in case of local experiment
				PersonQueryService personQueryService = Context.getPersonQueryService();
				Dataset newlySentDataset = personQueryService.getDatasetByTableName(remoteTableName);
				newlySentDataset.setFileName(dataset.getFileName());
				Context.getPersonManagerService().updateDataset(newlySentDataset);
			}

			performSecondPhase(dataset, matchName, blockingServiceName, matchingServiceName,
					thirdPartyAddress, keyServerUserName, keyServerPassword,
					dataIntegratorUserName, dataIntegratorPassword,
					parameterManagerUserName, parameterManagerPassword,
					personMatchRequestId, myNonce, personPseudoIdsReverseLookup);
		} catch (NamingException e) {
			log.error("Couldn't connect to third party " + thirdPartyAddress + " to send PersonMatchRequest");
			e.printStackTrace();
		} catch (ApplicationException e) {
			log.error("Couldn't connect to third party " + thirdPartyAddress + " to send PersonMatchRequest");
			e.printStackTrace();
		}
		return personMatchRequest;
	}

	@SuppressWarnings("unused")
	public Dataset bfReencodeDataset(Dataset leftLocalDataset, String newBFTableName, List<ColumnMatchInformation> columnMatchInformation,
			String keyServerUserName, String keyServerPassword, boolean leftOrRightSide, Map<Long,Long> personPseudoIdsReverseLookup,
			List<ColumnInformation> bfColumnInformation) throws ApplicationException
	{
		Dataset newBFDataset = null;
		PersonQueryService personQueryService = Context.getPersonQueryService();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int defaultK = privacySettings.getBloomfilterSettings().getDefaultK();
		// Reencode from original cleartext file or from DB?
		File originalFile = new File(leftLocalDataset.getFileName());
		if (!originalFile.exists() || true) {
			// 1. Try to perform the BF encodings from the Dataset in DB
			List<LoaderDataField> loaderDataForBFReencode = new ArrayList<LoaderDataField>();
			List<String> columnNames = new ArrayList<String>();
			List<String> noMatchColumnNames = new ArrayList<String>();
			for (ColumnMatchInformation cmi : columnMatchInformation) {
				ColumnInformation ci = new ColumnInformation();
				String fn = leftOrRightSide ? cmi.getLeftFieldName() : cmi.getRightFieldName();
				ci.setFieldName(fn);
				ci.setFieldMeaning(cmi.getFieldMeaning().getFieldMeaningEnum());
				if (cmi.getComparisonFunctionName() != null && cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
					ci.setFieldType(cmi.getFieldType().getFieldTypeEnum());
					noMatchColumnNames.add(fn);
				} else {
					ci.setFieldType(FieldType.FieldTypeEnum.Blob);
					ci.setFieldTransformation(Constants.DEFAULT_BLOOM_FILTER_FUNCTION_NAME);
					ci.setBloomFilterKParameter(defaultK);
					ci.setBloomFilterMParameter(cmi.getBloomFilterFinalM());
				}
				bfColumnInformation.add(ci);
	
				if (cmi.getComparisonFunctionName() == null || !cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
					LoaderDataField loaderDataField = new LoaderDataField();
					loaderDataField.setFieldName(fn);
					loaderDataField.setFieldMeaning(cmi.getFieldMeaning().getFieldMeaningEnum());
					loaderDataField.setFieldType(FieldType.FieldTypeEnum.Blob);
					loaderDataField.setSourceColumnIndex(-1);	// Doesn't make sense, this is just in-memory
					loaderDataField.setSourceFieldName(fn);
					loaderDataField.setFieldTransformation(getNewFunctionFieldForBFReencoding(defaultK, cmi));
					loaderDataForBFReencode.add(loaderDataField);
				}

				columnNames.add(fn);	// Assuming that the bf parameter advice has clear text field names (because getColumnsForRequestToPM works like that)
			}
			newBFDataset = personManagerService.createDatasetTable(newBFTableName, bfColumnInformation,
					leftLocalDataset.getTotalRecords(), false, false);

			long firstResult = 0L;
			boolean morePersons = true;
			List<Person> bfReencodedPersons = new ArrayList<Person>();
			TransformationService transformationService = Context.getTransformationService();
			do {
				List<Person> clearTextPersons = personQueryService.getPersonsPaged(leftLocalDataset.getTableName(), columnNames, firstResult, Constants.PAGE_SIZE);
				morePersons = (clearTextPersons != null && clearTextPersons.size() > 0);
				if (morePersons) {
					bfReencodedPersons.clear();
					for (Person clearTextPerson : clearTextPersons) {
						// Perform the actual BF reencoding
						Person bfReencodedPerson = new Person();
						for (LoaderDataField ldf : loaderDataForBFReencode) {
							String fn = ldf.getFieldName();
							Object value = clearTextPerson.getAttribute(fn);
							log.debug("Obtained a value of " + value + " for clear text field " + fn);
							if (value != null) {
								Object transformedValue = transformationService.transform(ldf.getFieldTransformation(), value);
								byte[] byteArrayRep = ((BitArray)transformedValue).getByteArrayRep();
								bfReencodedPerson.setAttribute(fn, byteArrayRep.clone());
								log.debug("Field " + fn + " has now value " + bfReencodedPerson.getAttribute(fn));
							}
						}
						// Add additional piggybacking no-match columns
						for (String ncn : noMatchColumnNames) {
							bfReencodedPerson.setAttribute(ncn, clearTextPerson.getAttribute(ncn));
						}
						bfReencodedPersons.add(bfReencodedPerson);
					}
					personManagerService.addPersons(newBFTableName, bfReencodedPersons, false, false);
				}
				firstResult += clearTextPersons.size();
			} while (morePersons);
			personManagerService.addIndexesAndConstraintsToDatasetTable(newBFTableName);
		} else if (originalFile.exists()) {
			// 2. Try to reload the dataset from file with the new BF encodings
			// TODO: Problem: assuming that the current FileLoaderConfiguration relates to our Dataset
			LoaderConfig loaderConfigurationBackup =
					(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
			LoaderConfig loaderConfig = new LoaderConfig();
			loaderConfig.setDelimiterRegex(loaderConfigurationBackup.getDelimiterRegex());
			loaderConfig.setHeaderLinePresent(loaderConfigurationBackup.getHeaderLinePresent());
			Map<String,LoaderDataField> loaderDataFieldHash = new HashMap<String,LoaderDataField>();
			for (LoaderDataField ldf : loaderConfigurationBackup.getDataFields()) {
				loaderDataFieldHash.put(ldf.getFieldName(), ldf);
			}
			List<LoaderDataField> loaderDataFields = new ArrayList<LoaderDataField>();
			for (ColumnMatchInformation cmi : columnMatchInformation) {
				String fn = leftOrRightSide ? cmi.getLeftFieldName() : cmi.getRightFieldName();
				LoaderDataField ldf = loaderDataFieldHash.get(fn);
				LoaderDataField loaderDataField = new LoaderDataField();
				loaderDataField.setFieldName(ldf.getFieldName());
				loaderDataField.setFieldMeaning(ldf.getFieldMeaning().getFieldMeaningEnum());
				if (cmi.getComparisonFunctionName() != null && cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
					loaderDataField.setFieldType(cmi.getFieldType().getFieldTypeEnum());
				} else {
					loaderDataField.setFieldType(FieldType.FieldTypeEnum.Blob);
					loaderDataField.setFieldTransformation(getNewFunctionFieldForBFReencoding(defaultK, cmi));
				}
				loaderDataField.setSourceColumnIndex(ldf.getSourceColumnIndex());
				loaderDataField.setSourceFieldName(ldf.getSourceFieldName());
				loaderDataFields.add(loaderDataField);
			}
			loaderConfig.setDataFields(loaderDataFields);

			// KeyServer is certainly needed
			KeyServerService ks = Context.getKeyServerService();
			ks.authenticate(keyServerUserName, keyServerPassword);

			Context.getConfiguration().registerConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION, loaderConfig);

			DataLoaderServiceSelector dataLoaderServiceSelector = Context.getDataLoaderServiceSelector();
			DataLoaderService dataLoaderService = dataLoaderServiceSelector.getDataLoaderServiceType(ConfigurableFileLoader.LOADER_ALIAS).getDataServiceService();
			dataLoaderService.loadFile(leftLocalDataset.getFileName(), newBFTableName, loaderConfig, true);
			newBFDataset = personManagerService.getDatasetById(leftLocalDataset.getDatasetId());
			newBFDataset.setImported("Y");
			newBFDataset.setFileName(leftLocalDataset.getFileName());
			personManagerService.updateDataset(newBFDataset);

			if (personPseudoIdsReverseLookup == null) {
				Dataset reencodedDataset = personQueryService.getDatasetByTableName(newBFTableName);
				reencodedDataset.setFileName(leftLocalDataset.getFileName());
				personManagerService.updateDataset(reencodedDataset);
			}

			Context.getConfiguration().registerConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION, loaderConfigurationBackup);
		}
		return newBFDataset;
	}

	protected abstract String sendNewDataset(String newBFTableName, Dataset newBFDataset, String remoteTableName, List<ColumnMatchInformation> columnMatchInformation,
			String keyServerUserName, String keyServerPassword, String dataIntegratorUserName, String dataIntegratorPassword,
			List<MatchPairStatHalf> matchPairStatHalves, Map<Long,Long> personPseudoIdsReverseLookup, int myNonce, int nonce, boolean leftOrRightSide,
			List<ColumnInformation> bfColumnInformation) throws NamingException, ApplicationException;

	protected String sendFBFDataset(String newBFTableName, Dataset newBFDataset, String remoteTableName, List<ColumnMatchInformation> columnMatchInformation,
			String keyServerUserName, String keyServerPassword, String dataIntegratorUserName, String dataIntegratorPassword,
			List<MatchPairStatHalf> matchPairStatHalves, Map<Long,Long> personPseudoIdsReverseLookup, int myNonce, int nonce, boolean leftOrRightSide,
			List<ColumnInformation> bfColumnInformation) throws NamingException, ApplicationException
	{
		PersonQueryService personQueryService = Context.getPersonQueryService();
		RemotePersonService remotePersonService = Context.getRemotePersonService();
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		DataIntegratorSettings dataIntegratorSettings =
				privacySettings.getComponentSettings().getDataIntegratorSettings();
		String serverAddress4DI = dataIntegratorSettings.getServerAddress();

		if (personPseudoIdsReverseLookup != null) {	// Only send remotely in case of non debug
			remotePersonService.close();
			remotePersonService.authenticate(serverAddress4DI, dataIntegratorUserName, dataIntegratorPassword,
					keyServerUserName, keyServerPassword);
			remotePersonService.createDatasetTable(newBFTableName, bfColumnInformation,
					newBFDataset.getTotalRecords(), false);
		}

		long firstResult = 0L;
		boolean morePatients = true;
		do {
			List<Person> persons = personQueryService.getPersonsPaged(newBFTableName, firstResult, Constants.PAGE_SIZE);
			morePatients = (persons != null && persons.size() > 0);
			if (morePatients) {
				if (personPseudoIdsReverseLookup != null)	// Only send remotely in case of non debug
					remotePersonService.addPersons(newBFTableName, persons, false, false);
			}
			firstResult += persons.size();
		} while (morePatients);
		remotePersonService.addIndexesAndConstraintsToDatasetTable(newBFTableName);
		return newBFTableName;
	}

	protected String sendCBFDataset(String newBFTableName, Dataset newBFDataset, String remoteTableName, List<ColumnMatchInformation> columnMatchInformation,
			String keyServerUserName, String keyServerPassword, String dataIntegratorUserName, String dataIntegratorPassword,
			List<MatchPairStatHalf> matchPairStatHalves, Map<Long,Long> personPseudoIdsReverseLookup, int myNonce, int nonce, boolean leftOrRightSide,
			List<ColumnInformation> bfColumnInformation) throws NamingException, ApplicationException
	{
		Integer cbfLength = 0;
		for (ColumnMatchInformation cmi : columnMatchInformation) {
			if (cmi.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob &&
				!cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME))
			{
				cbfLength += cmi.getBloomFilterProposedM();
			}
		}

		long seed = nonce * myNonce;
		Random rnd = new Random(seed);
		List<Integer> bitPermutation = null;
		if (personPseudoIdsReverseLookup != null)	// Only permute if non local, non-debug
			bitPermutation = generateBitPermutation(rnd, cbfLength);

		PersonQueryService personQueryService = Context.getPersonQueryService();
		PersonManagerService personManagerService = Context.getPersonManagerService();

		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int defaultK = privacySettings.getBloomfilterSettings().getDefaultK();
		String newCBFTableName = remoteTableName + UniversalDaoHibernate.CBF_TABLE_NAME_POSTFIX + "_" + getNowString();	// include time into name to avoid collision
		List<ColumnInformation> cbfColumnInformation = new ArrayList<ColumnInformation>();
		ColumnInformation ci = new ColumnInformation();
		ci.setFieldName(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
		ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.CBF);
		ci.setFieldType(FieldType.FieldTypeEnum.Blob);
		ci.setBloomFilterKParameter(defaultK);	// TODO
		ci.setBloomFilterMParameter(cbfLength);
		cbfColumnInformation.add(ci);
		List<String> noMatchColumnNames = new ArrayList<String>();
		for (ColumnMatchInformation cmi : columnMatchInformation) {
			if (cmi.getComparisonFunctionName().equals(Constants.NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME)) {
				String fn = leftOrRightSide ? cmi.getLeftFieldName() : cmi.getRightFieldName();
				ColumnInformation nci = new ColumnInformation();
				nci.setFieldName(fn);
				nci.setFieldMeaning(cmi.getFieldMeaning().getFieldMeaningEnum());
				nci.setFieldType(cmi.getFieldType().getFieldTypeEnum());
				cbfColumnInformation.add(nci);
				noMatchColumnNames.add(fn);
			}
		}
		personManagerService.createDatasetTable(newCBFTableName, cbfColumnInformation,
				newBFDataset.getTotalRecords(), false, false);
		// Sample the recoded bloomfilters -> CBF

		RemotePersonService remotePersonService = Context.getRemotePersonService();
		DataIntegratorSettings dataIntegratorSettings =
				privacySettings.getComponentSettings().getDataIntegratorSettings();
		String serverAddress4DI = dataIntegratorSettings.getServerAddress();
		try {
			if (personPseudoIdsReverseLookup != null) {	// Only send remotely in case of non debug
				remotePersonService.close();
				remotePersonService.authenticate(serverAddress4DI, dataIntegratorUserName, dataIntegratorPassword,
						keyServerUserName, keyServerPassword);
				remotePersonService.createDatasetTable(newCBFTableName, cbfColumnInformation,
						newBFDataset.getTotalRecords(), false);
			}

			long firstResult = 0L;
			boolean morePatients = true;
			List<Person> cbfPersons = new ArrayList<Person>();
			do {
				List<Person> persons = personQueryService.getPersonsPaged(newBFTableName, firstResult, Constants.PAGE_SIZE);
				morePatients = (persons != null && persons.size() > 0);
				if (morePatients) {
					cbfPersons.clear();
					for (Person person : persons) {
						Person cbfPerson = generateCBFWithOverSamplingAndPermutation(
								person, cbfLength, rnd, seed, bitPermutation, columnMatchInformation,
								leftOrRightSide);
						// Add piggy backing no-match columns
						for (String ncn : noMatchColumnNames)
							cbfPerson.setAttribute(ncn, person.getAttribute(ncn));
						cbfPersons.add(cbfPerson);
					}
					personManagerService.addPersons(newCBFTableName, cbfPersons, false, false);
					// Submit the new Dataset to the DataIntegrator at the same time of the local persistence
					if (personPseudoIdsReverseLookup != null)	// Only send remotely in case of non debug
						remotePersonService.addPersons(newCBFTableName, cbfPersons, false, false);
				}
				firstResult += persons.size();
			} while (morePatients);
			personManagerService.addIndexesAndConstraintsToDatasetTable(newCBFTableName);

			String matchPairStatHalfTableName = null;
			// We don't use pseudoIds for local experiments, so no need to resolve them
			if (personPseudoIdsReverseLookup != null) {
				remotePersonService.addIndexesAndConstraintsToDatasetTable(newCBFTableName);
				// No point in sending matchPairStatHalves in case there's a random bit selection blocking at DI => matchPairStatHalves == null
				if (matchPairStatHalves != null) {
					// Resolve pseudo Ids before sending it to DI
					for (MatchPairStatHalf matchPairStatHalf : matchPairStatHalves) {
						matchPairStatHalf.setPersonPseudoId(personPseudoIdsReverseLookup.get(matchPairStatHalf.getPersonPseudoId()));
					}
	
					// Submit the match pair stat half information separately from the match request
					matchPairStatHalfTableName = remoteTableName + "_" + /*UniversalDaoHibernate.MATCHPAIRSTAT_TABLE_NAME_EXTRA_PREFIX + "_" +*/ getNowString();	// including timestamp to avoid collision
					remotePersonService.createMatchPairStatHalfTable(getName(), matchPairStatHalfTableName,
							newCBFTableName, false);
					int i = 0;
					List<MatchPairStatHalf> matchPairStatHalvesPart = new ArrayList<MatchPairStatHalf>();
					do {
						int j = 0;
						while (j < Constants.PAGE_SIZE && i < matchPairStatHalves.size()) {
							matchPairStatHalvesPart.add(matchPairStatHalves.get(i));
							i++; j++;
						}
						remotePersonService.addMatchPairStatHalves(getName(), matchPairStatHalfTableName, matchPairStatHalvesPart);
						matchPairStatHalvesPart.clear();
					} while (i < matchPairStatHalves.size());
					remotePersonService.addIndexesAndConstraintsToMatchPairStatHalfTable(getName(), matchPairStatHalfTableName, newCBFTableName);
				}
			}
		} catch (NamingException e) {
			log.error("Couldn't connect to Data Integrator (" + serverAddress4DI + ") to send back advice about new bloom filter parameters");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error occured during creation, generation or loading of BF/CBF data");
			e.printStackTrace();
		}
		return newCBFTableName;
	}

	protected abstract String getMatchPairStatHalfTableName(String remoteTableName);

	public void handleBloomFilterParameterAdvice(String blockingServiceName, String matchingServiceName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			Dataset leftLocalDataset, Dataset leftRemoteDataset,
			Dataset rightRemoteDataset, List<ColumnMatchInformation> columnMatchInformation, List<MatchPairStatHalf> matchPairStatHalves,
			Map<Long,Long> personPseudoIdsReverseLookup, int myNonce, int nonce, boolean leftOrRightSide,
			String matchName) throws ApplicationException {
		String remoteTableName = leftRemoteDataset.getTableName();

		String newBFTableName = remoteTableName + UniversalDaoHibernate.BF_TABLE_NAME_POSTFIX + "_" + getNowString();	// include time into name to avoid collision
		// Reencode from original cleartext file or from DB?
		List<ColumnInformation> bfColumnInformation = new ArrayList<ColumnInformation>();
		Dataset newBFDataset = bfReencodeDataset(leftLocalDataset, newBFTableName, columnMatchInformation,
				keyServerUserName, keyServerPassword, leftOrRightSide, personPseudoIdsReverseLookup, bfColumnInformation);

		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		DataIntegratorSettings dataIntegratorSettings =
				privacySettings.getComponentSettings().getDataIntegratorSettings();
		String serverAddress4DI = dataIntegratorSettings.getServerAddress();
		try {
			String sentTableName = sendNewDataset(newBFTableName, newBFDataset, remoteTableName, columnMatchInformation,
					keyServerUserName, keyServerPassword, dataIntegratorUserName, dataIntegratorPassword,
					matchPairStatHalves, personPseudoIdsReverseLookup, myNonce, nonce, leftOrRightSide, bfColumnInformation);

			RemotePersonService remotePersonService = Context.getRemotePersonService();

			remotePersonService.close();
			remotePersonService.authenticate(serverAddress4DI, dataIntegratorUserName, dataIntegratorPassword,
					keyServerUserName, keyServerPassword);

			// Sending MatchRequest after loading the data remotely
			int personMatchRequestId = remotePersonService.addPersonMatchRequest(getName(), sentTableName,
					matchName, blockingServiceName, matchingServiceName, null, getMatchPairStatHalfTableName(remoteTableName));
			remotePersonService.close();	// Needed to flush the PersonMatchRequest into the DB on the DI

			// Start the actual computation on the Data Integrator (one of the threads will perform it, the other returns immediately)
			remotePersonService.authenticate(serverAddress4DI, dataIntegratorUserName, dataIntegratorPassword,
					keyServerUserName, keyServerPassword);
			remotePersonService.acquireMatchRequests(getName(), personMatchRequestId, ComponentType.DATA_INTEGRATOR_MODE);
			remotePersonService.close();
		} catch (NamingException e) {
			log.error("Couldn't connect to Data Integrator (" + serverAddress4DI + ") to send back advice about new bloom filter parameters");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error occured during creation, generation or loading of BF or CBF data");
			e.printStackTrace();
		}
	}

	public void testPRLLinkRecords(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException {
		PersonMatchRequest leftPersonMatchRequest = personMatchRequestDao.getPersonMatchRequest(leftPersonMatchRequestId);
		PersonMatchRequest rightPersonMatchRequest = personMatchRequestDao.getPersonMatchRequest(rightPersonMatchRequestId);

		linkPRLRecords(leftPersonMatchRequest, rightPersonMatchRequest, ComponentType.DATA_INTEGRATOR_MODE);
	}

	abstract protected BloomFilterParameterAdvice linkPRLRecords(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType) throws ApplicationException;

	protected List<MatchPairStatHalf> retrieveMatchPairStatHalvesByRequest(PersonMatchRequest personMatchRequest) {
		List<MatchPairStatHalf> matchPairStatHalves = null;
		String matchPairStatHalfTableName = personMatchRequest.getMatchPairStatHalfTableName();
		if (matchPairStatHalfTableName != null)
			if (matchPairStatHalfTableName.length() > 0)
				matchPairStatHalves = retrieveMatchPairStatHalves(matchPairStatHalfTableName);
		return matchPairStatHalves;
	}
	
	protected List<MatchPairStat> retrieveMatchPairStatsFromMatchPairStatHalves(List<MatchPairStatHalf> leftMatchPairStatHalves,
			List<MatchPairStatHalf> rightMatchPairStatHalves) throws ApplicationException
	{
		List<MatchPairStat> matchPairStats = null;
		if (leftMatchPairStatHalves != null && rightMatchPairStatHalves != null) {
			if (leftMatchPairStatHalves.size() != rightMatchPairStatHalves.size())
				throw new ApplicationException("Match pair half statistics' length does not match");
			matchPairStats = new ArrayList<MatchPairStat>();
			ParallelIteratorUtil.iterate(leftMatchPairStatHalves, rightMatchPairStatHalves, matchPairStats,
					new ParallelMatchPairHalfStatListIterator<MatchPairStatHalf, MatchPairStatHalf>() {
				public boolean each(MatchPairStatHalf leftStatHalf, MatchPairStatHalf rightStatHalf,
						List<MatchPairStat> matchPairStats) throws ApplicationException {
					if (leftStatHalf.getMatchStatus() != rightStatHalf.getMatchStatus())
						throw new ApplicationException("Match pair half statistics are wrong: status does not match");
					MatchPairStat matchPairStat = new MatchPairStat(leftStatHalf.getPersonPseudoId(),
							rightStatHalf.getPersonPseudoId(), leftStatHalf.getMatchStatus());
					matchPairStats.add(matchPairStat);
					return true;
				}
			});
		}
		return matchPairStats;
	}

	protected BloomFilterParameterAdvice linkFBFRecords(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType) throws ApplicationException {
		List<MatchPairStatHalf> leftMatchPairStatHalves = retrieveMatchPairStatHalvesByRequest(leftPersonMatchRequest);
		List<MatchPairStatHalf> rightMatchPairStatHalves = retrieveMatchPairStatHalvesByRequest(rightPersonMatchRequest);
		List<MatchPairStat> matchPairStats = retrieveMatchPairStatsFromMatchPairStatHalves(leftMatchPairStatHalves, rightMatchPairStatHalves);

		PrivacyPreservingBlockingSettings ppbsBackup = Context.getConfiguration().getPrivacyPreservingBlockingSettings();
		PrivacyPreservingBlockingSettings ppbs = new PrivacyPreservingBlockingSettings();
		ppbs.setNumberOfBlockingBits(ppbsBackup.getNumberOfBlockingBits());
		ppbs.setNumberOfRuns(ppbsBackup.getNumberOfRuns());

		// Assemble a temporary MatchConfiguration for our purpose
		MatchConfiguration matchConfigurationBackup =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		MatchField firstOriginalMatchField = matchConfigurationBackup.getMatchFields(FieldQuerySelector.MatchOnlyFields).get(0);	// TODO: that's lame, should get probability values in a different way
		MatchConfiguration matchConfig = new MatchConfiguration();
		// TODO: request probability values (and other FS settings) from Data Providers??? Who wins?
		// Right now we inherit the values from Parameter Manager's config
		matchConfig.setFalsePositiveProbability(matchConfigurationBackup.getFalsePositiveProbability());
		matchConfig.setFalseNegativeProbability(matchConfigurationBackup.getFalseNegativeProbability());
		matchConfig.setEmSettings(matchConfigurationBackup.getEmSettings());
		List<MatchField> matchFields = new ArrayList<MatchField>();

		List<PrivacyPreservingBlockingField> ppbfs = new ArrayList<PrivacyPreservingBlockingField>();
		List<ColumnInformation> leftColumnInformation = leftPersonMatchRequest.getDataset().getColumnInformation();
		List<ColumnInformation> rightColumnInformation = rightPersonMatchRequest.getDataset().getColumnInformation();
		for(ColumnInformation leftColumnInfo : leftColumnInformation) {
			if (isBloomFilterField(leftColumnInfo)) {
				for(ColumnInformation rightColumnInfo : rightColumnInformation) {
					if (isBloomFilterField(rightColumnInfo) &&
						leftColumnInfo.getFieldMeaning().getFieldMeaningEnum() == rightColumnInfo.getFieldMeaning().getFieldMeaningEnum())
					{
						if (leftColumnInfo.getBloomFilterKParameter() != rightColumnInfo.getBloomFilterKParameter() ||
							leftColumnInfo.getBloomFilterMParameter() != rightColumnInfo.getBloomFilterMParameter() ||
							leftColumnInfo.getFieldType() != rightColumnInfo.getFieldType())
						{
							throw new ApplicationException("Left and right BF column parameters don't match");
						}
						PrivacyPreservingBlockingField ppbf = new PrivacyPreservingBlockingField();
						ppbf.setLeftFieldName(leftColumnInfo.getFieldName());
						ppbf.setRightFieldName(rightColumnInfo.getFieldName());
						List<Integer> bits = new ArrayList<Integer>();
						ppbf.setBits(bits);
						ppbfs.add(ppbf);

						MatchField mf = new MatchField();
						mf.setLeftFieldName(leftColumnInfo.getFieldName());
						mf.setRightFieldName(rightColumnInfo.getFieldName());
						// TODO: where to get the other values from?
						// Right now we inherit the values from Parameter Manager's config
						mf.setAgreementProbability(firstOriginalMatchField.getAgreementProbability());
						mf.setDisagreementProbability(firstOriginalMatchField.getDisagreementProbability());
						mf.setMatchThreshold(firstOriginalMatchField.getMatchThreshold());
						// How to decide which comparator function?
						FunctionField functionField = new FunctionField("DiceBinary");
						mf.setComparatorFunction(functionField);
						matchFields.add(mf);
					}
				}
			}
		}
		// TODO: maybe we need to add piggy backing no-match field here to the MatchField list?
		
		ppbs.setPrivacyPreservingBlockingFields(ppbfs);
		Context.getConfiguration().saveAndRegisterPrivacyPreservingBlockingSettings(ppbs);

		matchConfig.setMatchFields(matchFields);

		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfig);

		BloomFilterParameterAdvice bfpa = linkRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType, matchPairStats);

		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfigurationBackup);
		Context.getConfiguration().saveAndRegisterPrivacyPreservingBlockingSettings(ppbsBackup);

		return bfpa;
	}

	protected BloomFilterParameterAdvice linkCBFRecords(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType) throws ApplicationException {
		// Generate and score pairs from the current pages
		List<MatchPairStatHalf> leftMatchPairStatHalves = retrieveMatchPairStatHalvesByRequest(leftPersonMatchRequest);
		List<MatchPairStatHalf> rightMatchPairStatHalves = retrieveMatchPairStatHalvesByRequest(rightPersonMatchRequest);
		List<MatchPairStat> matchPairStats = retrieveMatchPairStatsFromMatchPairStatHalves(leftMatchPairStatHalves, rightMatchPairStatHalves);

		PrivacyPreservingBlockingSettings ppbsBackup = Context.getConfiguration().getPrivacyPreservingBlockingSettings();
		PrivacyPreservingBlockingSettings ppbs = new PrivacyPreservingBlockingSettings();
		ppbs.setNumberOfBlockingBits(ppbsBackup.getNumberOfBlockingBits());
		ppbs.setNumberOfRuns(ppbsBackup.getNumberOfRuns());
		PrivacyPreservingBlockingField ppbf = new PrivacyPreservingBlockingField();
		ppbf.setLeftFieldName(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
		ppbf.setRightFieldName(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
		List<Integer> bits = new ArrayList<Integer>();
		ppbf.setBits(bits);
		List<PrivacyPreservingBlockingField> ppbfs = new ArrayList<PrivacyPreservingBlockingField>();
		ppbfs.add(ppbf);
		ppbs.setPrivacyPreservingBlockingFields(ppbfs);
		Context.getConfiguration().saveAndRegisterPrivacyPreservingBlockingSettings(ppbs);

		// Assemble a temporary MatchConfiguration for our purpose
		MatchConfiguration matchConfigurationBackup =
			(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		MatchField firstOriginalMatchField = matchConfigurationBackup.getMatchFields(FieldQuerySelector.MatchOnlyFields).get(0);	// TODO: that's lame, should get probability values in a different way
		MatchConfiguration matchConfig = new MatchConfiguration();
		// TODO: request probability values (and other FS settings) from Data Providers??? Who wins?
		// Right now we inherit the values from Parameter Manager's config
		matchConfig.setFalsePositiveProbability(matchConfigurationBackup.getFalsePositiveProbability());
		matchConfig.setFalseNegativeProbability(matchConfigurationBackup.getFalseNegativeProbability());
		matchConfig.setEmSettings(matchConfigurationBackup.getEmSettings());
		List<MatchField> matchFields = new ArrayList<MatchField>();

		MatchField mf = new MatchField();
		mf.setLeftFieldName(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
		mf.setRightFieldName(UniversalDaoHibernate.CBF_ATTRIBUTE_NAME);
		// TODO: where to get the other values from?
		// Right now we inherit the values from Parameter Manager's config
		mf.setAgreementProbability(firstOriginalMatchField.getAgreementProbability());
		mf.setDisagreementProbability(firstOriginalMatchField.getDisagreementProbability());
		mf.setMatchThreshold(firstOriginalMatchField.getMatchThreshold());
		// How to decide which comparator function?
		FunctionField functionField = new FunctionField("DiceBinary");
		mf.setComparatorFunction(functionField);
		matchFields.add(mf);
		matchConfig.setMatchFields(matchFields);
		// TODO: maybe we need to add piggy backing no-match field here to the MatchField list?

		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfig);

		BloomFilterParameterAdvice bfpa = linkRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType, matchPairStats);

		Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfigurationBackup);
		Context.getConfiguration().saveAndRegisterPrivacyPreservingBlockingSettings(ppbsBackup);

		return bfpa;
	}

	abstract protected String getMatchingServiceTypeName(ComponentType componentType);

	abstract protected String getBlockingServiceTypeName(ComponentType componentType, List<MatchPairStat> matchPairStats);

	protected BloomFilterParameterAdvice linkRecords(PersonMatchRequest leftPersonMatchRequest, PersonMatchRequest rightPersonMatchRequest,
			ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException {
		String matchingServiceTypeName = getMatchingServiceTypeName(componentType);
		MatchingServiceSelector matchingServiceSelector = Context.getMatchingServiceSelector();
		MatchingServiceType matchingServiceType =
			matchingServiceSelector.getMatchingServiceType(matchingServiceTypeName);
		ProbabilisticMatchingServiceBase matchingService = (ProbabilisticMatchingServiceBase)matchingServiceType.getMatchingService();

		Dataset leftDataset = leftPersonMatchRequest.getDataset();
		String leftTableName = leftDataset.getTableName();
		Dataset rightDataset = rightPersonMatchRequest.getDataset();
		String rightTableName = rightDataset.getTableName();

		String linkTableName = /*leftTableName + "_" + rightTableName + "_" +*/ getNowString();
		List<LeanRecordPair> pairs = new ArrayList<LeanRecordPair>();

		BloomFilterParameterAdvice leftBFPA = null;

		String blockingServiceTypeName = getBlockingServiceTypeName(componentType, matchPairStats);
		if (componentType == ComponentType.DATA_INTEGRATOR_MODE) {
			matchingService.linkRecords(
					blockingServiceTypeName, matchPairStats, matchingServiceTypeName,
					null, linkTableName, leftTableName, rightTableName,
					pairs, componentType, false, true);
		} else if (componentType == ComponentType.PARAMETER_MANAGER_MODE) {
			// Assemble a temporary MatchConfiguration for our purpose
			MatchConfiguration matchConfigurationBackup =
				(MatchConfiguration)Context.getConfiguration().lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
			MatchField firstOriginalMatchField = matchConfigurationBackup.getMatchFields(FieldQuerySelector.MatchOnlyFields).get(0);	// TODO: that's lame, should get probability values in a different way
			MatchConfiguration matchConfig = new MatchConfiguration();
			// TODO: request probability values (and other FS settings) from Data Providers??? Who wins?
			// Right now we inherit the values from Parameter Manager's config
			matchConfig.setFalsePositiveProbability(matchConfigurationBackup.getFalsePositiveProbability());
			matchConfig.setFalseNegativeProbability(matchConfigurationBackup.getFalseNegativeProbability());
			matchConfig.setEmSettings(matchConfigurationBackup.getEmSettings());
			List<MatchField> matchFields = new ArrayList<MatchField>();
			for (ColumnInformation leftCi : leftDataset.getColumnInformation()) {
				if (leftCi.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob) {
					for (ColumnInformation rightCi : rightDataset.getColumnInformation()) {
						if (rightCi.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.Blob &&
							leftCi.getFieldMeaning() == rightCi.getFieldMeaning())
						{
							if (!leftCi.getFieldTransformation().contains("Bloom") &&
								!rightCi.getFieldTransformation().contains("Bloom"))
							{
								// Assuming SHA or other hash Encoded fields:
								// distances other than 0.0 and 1.0 doesn't make sense
								// So go with exact match tailored to blobs
								MatchField mf = new MatchField();
								mf.setLeftFieldName(leftCi.getFieldName());
								mf.setRightFieldName(rightCi.getFieldName());
								// TODO: where to get the other values from?
								// Right now we inherit the values from Parameter Manager's config
								mf.setAgreementProbability(firstOriginalMatchField.getAgreementProbability());
								mf.setDisagreementProbability(firstOriginalMatchField.getDisagreementProbability());
								mf.setMatchThreshold(firstOriginalMatchField.getMatchThreshold());
								FunctionField functionField = new FunctionField("ExactBinary");
								mf.setComparatorFunction(functionField);
								matchFields.add(mf);
								break;
							}
						}
					}
				}
			}
			// TODO: maybe we need to add piggy backing no-match field here to the MatchField list?
			matchConfig.setMatchFields(matchFields);

			Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfig);
			// TODO: separate EM for p value estimation (blocking bypass) and m-and-u value estimation (special PM blocking)
			// So here is where we can run several linkRecords with emOnly=true mode
			PersonMatch personMatch = matchingService.linkRecords(blockingServiceTypeName,
					null, matchingServiceTypeName, null,
					linkTableName, leftTableName, rightTableName, pairs, componentType, false, false);

			// Gather statistics information for DPs so they can calculate partial importance and send it to DI
			FellegiSunterParameters fellegiSunterParams = matchingService.getFellegiSunterParameters();
			//persistLinkResults(pairs, fellegiSunterParams, personMatch);	// Uncomment this for persistence and/or debug

			List<MatchPairStatHalf> matchPairStatHalfLeft = null;
			if (!leftPersonMatchRequest.getBlockingServiceName().equals(Constants.PPB_WITH_CRYPTO_RANDOM_BITS_SERVICE_NAME)) {
				matchPairStatHalfLeft = new ArrayList<MatchPairStatHalf>();
				List<MatchPairStatHalf> matchPairStatHalfRight = new ArrayList<MatchPairStatHalf>();
				for (LeanRecordPair pair : pairs) {
					boolean matchState = (pair.getWeight() >= fellegiSunterParams.getUpperBound());
					MatchPairStatHalf matchPairStatLeft = new MatchPairStatHalf(pair.getLeftRecordId(), matchState);
					matchPairStatHalfLeft.add(matchPairStatLeft);
					MatchPairStatHalf matchPairStatRight = new MatchPairStatHalf(pair.getRightRecordId(), matchState);
					matchPairStatHalfRight.add(matchPairStatRight);
				}
				// And persist stat halves
				String leftMatchPairStatHalfTableName = persistMatchPairStatHalves(leftTableName, matchPairStatHalfLeft);	// Uncomment this for persistence and/or debug
				leftPersonMatchRequest.setMatchPairStatHalfTableName(leftMatchPairStatHalfTableName);	// Uncomment this for persistence and/or debug
				String rightMatchPairStatHalfTableName = persistMatchPairStatHalves(rightTableName, matchPairStatHalfRight);
				rightPersonMatchRequest.setMatchPairStatHalfTableName(rightMatchPairStatHalfTableName);
			}

			List<ColumnMatchInformation> columnMatchInformation = personMatch.getColumnMatchInformation();
			leftPersonMatchRequest.setPersonMatchId(personMatch.getPersonMatchId());
			rightPersonMatchRequest.setPersonMatchId(personMatch.getPersonMatchId());

			// Register back the saved MatchConfiguration instead of the temporary one
			Context.getConfiguration().registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, matchConfigurationBackup);

			// prepare the return information for the left side
			leftBFPA = new BloomFilterParameterAdvice();
			leftBFPA.setPersonMatchReuqest(leftPersonMatchRequest);
			leftBFPA.setLeftDataset(leftDataset);
			leftBFPA.setRightDataset(rightDataset);
			leftBFPA.setColumnMatchInformation(columnMatchInformation);
			if (matchPairStatHalfLeft != null)
				leftBFPA.setMatchPairStatHalves(matchPairStatHalfLeft);
			leftBFPA.setNonce(rightPersonMatchRequest.getNonce());
			leftBFPA.setLeftOrRightSide(true);

			leftPersonMatchRequest.setCompleted(true);
			personMatchRequestDao.updatePersonMatchRequest(leftPersonMatchRequest);
			rightPersonMatchRequest.setCompleted(true);
			personMatchRequestDao.updatePersonMatchRequest(rightPersonMatchRequest);
		} else {
			log.error("Unkown component type mode: " + componentType);
		}
		return leftBFPA;
	}

}
