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
package org.openempi.webapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.PersonDataService;
import org.openempi.webapp.client.model.ColumnInformationWeb;
import org.openempi.webapp.client.model.ColumnMatchInformationWeb;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.DatasetWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.DataLoaderServiceSelector;
import org.openhie.openempi.loader.DataLoaderService;
import org.openhie.openempi.loader.file.ConfigurableFileLoader;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.matching.MatchingServiceSelector;
import org.openhie.openempi.matching.MatchingServiceType;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingServiceBase;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocol;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolType;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.service.UserManager;
import org.openhie.openempi.util.GeneralUtil;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PersonDataServiceImpl extends RemoteServiceServlet implements PersonDataService
{
	private static final long serialVersionUID = -656758110760491116L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public String addPerson(String tableName, PersonWeb personWeb) {
		log.debug("Received request to add a new person entry to the repository.");
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
			personService.addPerson(tableName, person, true, true);
		} catch (Throwable t) {
			log.error("Failed to add person entry: " + t, t);
			msg = t.getMessage();
		}
		return msg;
	}
	
	public String deletePerson(String tableName, Long personId) {
		log.debug("Received request to add a new person entry to the repository.");
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			personService.deletePerson(tableName, personId);
		} catch (Throwable t) {
			log.error("Failed to delete person entry: " + t, t);
			msg = t.getMessage();
		}
		return msg;
	}

	public PersonWeb getPersonById(String tableName, Long personId) {
		log.debug("Received request to retrieve a filtered list of person records by identifier.");
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person person = personQueryService.getPersonById(tableName, personId);
			PersonWeb dtos = ModelTransformer.map(person, PersonWeb.class);
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public PersonWeb getPersonByIdTransactionally(String tableName, Long personId) {
		log.debug("Received request to retrieve a filtered list of person records by identifier.");
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person person = personQueryService.getPersonByIdTransactionally(tableName, personId);
			PersonWeb dtos = ModelTransformer.convertToClientModel(person);
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public List<LinkedPersonWeb> getLinkedPersons(String tableName, PersonWeb personParam) {
		// TODO
		return new ArrayList<LinkedPersonWeb>();
	}
	
	public List<PersonWeb> getPersonsByExample(String tableName, PersonWeb personParam) {
		log.debug("Received request to retrieve a filtered list of person records by attributes.");
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person personTransformed = ModelTransformer.map(personParam, org.openhie.openempi.model.Person.class);
			List<org.openhie.openempi.model.Person> persons = personQueryService.getPersonsByExample(tableName, personTransformed);
			List<PersonWeb> dtos = ModelTransformer.mapList(persons, PersonWeb.class);
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public DatasetWeb addDataset(DatasetWeb dataset) {
		log.debug("Received request to add a new dataset entry: " + dataset);
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.Dataset theDataset = ModelTransformer.map(dataset, org.openhie.openempi.model.Dataset.class);
			theDataset = personService.saveDataset(theDataset);
			return ModelTransformer.map(theDataset, DatasetWeb.class);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<DatasetWeb> getDatasets(String username) {
		log.debug("Received request to return list of files for user: " + username);
		try {
			UserManager userService = Context.getUserManager();
			User user = userService.getUserByUsername(username);
			PersonManagerService personService = Context.getPersonManagerService();
			List<org.openhie.openempi.model.Dataset> datasets = personService.getDatasets(user);
			List<DatasetWeb> result = ModelTransformer.mapList(datasets, DatasetWeb.class);
			return result;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public void deleteDatasetFile(DatasetWeb dataset) {
		log.debug("Received request to delete dataset's file from the upload directory, dataset entry: " + dataset.getDatasetId());
		try {
			PersonManagerService personManagerService = Context.getPersonManagerService();
			org.openhie.openempi.model.Dataset datasetFound = personManagerService.getDatasetByTableName(dataset.getTableName());
			personManagerService.deleteDatasetFile(datasetFound);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public void removeDataset(DatasetWeb dataset) {
		log.debug("Received request to remove dataset entry: " + dataset.getDatasetId());
		try {
			PersonManagerService personManagerService = Context.getPersonManagerService();
			org.openhie.openempi.model.Dataset datasetFound = personManagerService.getDatasetByTableName(dataset.getTableName());
			personManagerService.removeDataset(datasetFound);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public String importDataset(DatasetWeb dataset, String tableName,
			String keyServerUserName, String keyServerPassword)
	{
		log.debug("Received request to import the contents of file entry: " + dataset);
		if (dataset == null || dataset.getDatasetId() == null || dataset.getFileName() == null) {
			log.error("Unable to import file with insufficient identifying attributes.");
			return "You must specify the dataset to be imported.";
		}
		String resultText = "Failed to parse and upload the file " + dataset.getFileName();
		try {
			importFileEntry(dataset, true, tableName, keyServerUserName, keyServerPassword);
			resultText = "File successfully imported";
		} catch (Exception e) {
			log.error(resultText + " due to " + e.getMessage());
			e.printStackTrace();
		}
		return resultText;
	}
	
	public ApplicationContext getApplicationContext() {
		return context;
	}

	public String sendDatasetToDataIntegrator(DatasetWeb dataset, String remoteTableName,
			String dataIntegratorUserName, String dataIntegratorPassword)
	{
		try {
			log.warn("Send preparation Start");
			RemotePersonService remotePersonService = Context.getRemotePersonService();
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			String serverAddress = privacySettings.getComponentSettings().
									getDataIntegratorSettings().getServerAddress();
			remotePersonService.authenticate(serverAddress, dataIntegratorUserName, dataIntegratorPassword);
			PersonQueryService personQueryService = Context.getPersonQueryService();
			String localTableName = dataset.getTableName();
			List<ColumnInformation> columnInformation = personQueryService.getDatasetColumnInformation(localTableName);
			log.warn("Send preparation End");
			log.warn("Send Start");
			remotePersonService.createDatasetTable(remoteTableName, columnInformation, dataset.getTotalRecords(), false);
			int pageSize = Constants.PAGE_SIZE;
			Long firstResult = 0L;
			boolean morePatients = true;
			do {
				List<Person> persons = personQueryService.getPersonsPaged(localTableName, firstResult, pageSize);
				morePatients = (persons != null && persons.size() > 0);
				if (morePatients)
					remotePersonService.addPersons(remoteTableName, persons, false, false);
				firstResult += persons.size();
			} while (morePatients);
			remotePersonService.addIndexesAndConstraintsToDatasetTable(remoteTableName);
			remotePersonService.close();
			log.warn("Send End");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to send to Data Integrator due to " + e.getMessage());
			return "Failed to send to Data Integrator due to " + e.getMessage();
		}
		return "DB sent successfully";
	}

	public String initiateRecordLinkage(String recordLinkageProtocolName, DatasetWeb dataset, String remoteTableName,
			String matchName, String keyServerUserName, String keyServerPassword, String dataIntegratorUserName,
			String dataIntegratorPassword, String parameterManagerUserName, String parameterManagerPassword)
	{
		RecordLinkageProtocolSelector recordLinkageProtocolSelector = Context.getRecordLinkageProtocolSelector();
		RecordLinkageProtocolType recordLinkageProtocolType = recordLinkageProtocolSelector.getRecordLinkageProtocolType(recordLinkageProtocolName);
		RecordLinkageProtocol recordLinkageProtocol = recordLinkageProtocolType.getRecordLinkageProtocol();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		org.openhie.openempi.model.Dataset datasetFound = personManagerService.getDatasetByTableName(dataset.getTableName());
		recordLinkageProtocol.sendPersonMatchRequest(datasetFound, remoteTableName, matchName,
				Constants.PPB_WITH_CRYPTO_RANDOM_BITS_SERVICE_NAME,	// TODO
				Constants.PROBABILISTIC_MATCHING_SERVICE_WITH_SCALED_SCORES_NAME,	// TODO
				keyServerUserName, keyServerPassword, dataIntegratorUserName, dataIntegratorPassword,
				parameterManagerUserName, parameterManagerPassword);
		return "DB sent successfully";
	}

	public boolean doesCurrentLoaderConfigurationNeedKeyServer() {
		LoaderConfig loaderConfiguration =
			(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
		return GeneralUtil.doesConfigurationNeedKeyServer(loaderConfiguration);
	}

	private void importFileEntry(DatasetWeb dataset, boolean applyFieldTransformations, String tableName,
			String keyServerUserName, String keyServerPassword)
	{
		// KeyService is not needed if we only hash (SHA-1, MD5, etc) fields
		// It is needed only if for Bloomfilters
		LoaderConfig loaderConfiguration =
			(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
		if (GeneralUtil.doesConfigurationNeedKeyServer(loaderConfiguration)) {
			KeyServerService ks = Context.getKeyServerService();
			ks.authenticate(keyServerUserName, keyServerPassword);
		}

		log.warn("Import start");	// This is warning in order to get through into the log even if we deal with a non-debug version
		DataLoaderServiceSelector dataLoaderServiceSelector = Context.getDataLoaderServiceSelector();
		DataLoaderService dataLoaderService = dataLoaderServiceSelector.getDataLoaderServiceType(ConfigurableFileLoader.LOADER_ALIAS).getDataServiceService();
		dataLoaderService.loadFile(dataset.getFileName(), tableName, loaderConfiguration, applyFieldTransformations);
		log.warn("Import end");	// This is warning in order to get through into the log even if we deal with a non-debug version
		PersonManagerService personService = Context.getPersonManagerService();
		org.openhie.openempi.model.Dataset datasetFound = personService.getDatasetByTableName(dataset.getTableName());
		datasetFound.setImported("Y");
		personService.updateDataset(datasetFound);
	}

	public void saveDatasetToFile(DatasetWeb dataset, String tableName) {
		log.debug("Received request to save dataset entry " + dataset.getDatasetId());
		try {
//			PersonManagerService personManagerService = Context.getPersonManagerService();
//			org.openhie.openempi.model.Dataset datasetFound = personManagerService.getDatasetByTableName(dataset.getTableName());
//			personManagerService.saveDatasetToFile(datasetFound, tableName);

			// For HMAC Encode Test
			KeyServerService ks = Context.getKeyServerService();
			ks.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
			RecordLinkageProtocolSelector recordLinkageProtocolSelector = Context.getRecordLinkageProtocolSelector();
			RecordLinkageProtocolType recordLinkageProtocolType = recordLinkageProtocolSelector.getRecordLinkageProtocolType(Constants.THREE_THIRD_PARTY_CBF_PROTOCOL_NAME);
			RecordLinkageProtocol recordLinkageProtocol = recordLinkageProtocolType.getRecordLinkageProtocol();
			recordLinkageProtocol.testHMACEncoding(dataset.getDatasetId(), tableName);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<ColumnInformationWeb> getColumnInformation(String tableName) {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		List<ColumnInformationWeb> columnInformation =
			ModelTransformer.convertToClientModel(personQueryService.getDatasetColumnInformation(tableName));
		return columnInformation;
	}

	public List<ColumnMatchInformationWeb> getColumnMatchInformation(PersonMatchWeb personMatchWeb)
	{
		PersonQueryService personQueryService = Context.getPersonQueryService();
		PersonMatch personMatch = personQueryService.getPersonMatch(personMatchWeb.getPersonMatchId());
		List<ColumnMatchInformationWeb> columnMatchInformation =
			ModelTransformer.convertToClientModel2(personMatch.getColumnMatchInformation());
		return columnMatchInformation;
	}

	public List<PersonMatchWeb> getPersonMatches(String username) {
		log.debug("Received request to return list of files for user: " + username);
		try {
			UserManager userService = Context.getUserManager();
			User user = userService.getUserByUsername(username);
			PersonQueryService personService = Context.getPersonQueryService();
			List<org.openhie.openempi.model.PersonMatch> personMatches = personService.getPersonMatches(user);
			List<PersonMatchWeb> personMatchesWeb = new ArrayList<PersonMatchWeb>();
			if (personMatches != null) {
				for(org.openhie.openempi.model.PersonMatch personMatch : personMatches) {
					personMatchesWeb.add(ModelTransformer.convertToClientModel(personMatch));
				}
			}
			return personMatchesWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public FellegiSunterParametersWeb getFellegiSunterParameters(Integer personMatchId) {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		PersonMatch personMatch = personQueryService.getPersonMatch(personMatchId);
		FellegiSunterParametersWeb fellegiSunterParametersWeb =
			ModelTransformer.convertToClientModel(personMatch.getMatchFellegiSunter());
		return fellegiSunterParametersWeb;
	}

	public List<PersonLinkWeb> getPersonLinks(String tableName, Long firstResult, Integer maxResults) {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		List<PersonLink> personLinks = personQueryService.getPersonLinksPaged(tableName, firstResult, maxResults);
		List<PersonLinkWeb> personLinksWeb = ModelTransformer.convertToClientModel3(personLinks);
		return personLinksWeb;
	}

	public List<PersonLinkWeb> samplePersonLinks(Integer personMatchId, Integer numberOfSamples) {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		List<PersonLink> personLinks = personQueryService.samplePersonLinks(personMatchId, numberOfSamples);
		List<PersonLinkWeb> personLinksWeb = ModelTransformer.convertToClientModel3(personLinks);
		return personLinksWeb;
	}

	public List<String> getDatasetTableNames() {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		return personQueryService.getDatasetTableNames();
	}

	public PersonMatchWeb testScorePairs(String linkTableName, String leftTableName, String rightTableName,
			String blockingServiceTypeName, String matchingServiceTypeName, Boolean emOnly)
	{
		PersonMatchWeb personMatchWeb = null;
		try {
			MatchingServiceSelector matchingServiceSelector = Context.getMatchingServiceSelector();
			MatchingServiceType matchingServiceType =
				matchingServiceSelector.getMatchingServiceType(matchingServiceTypeName);
			// TODO: what if it's not Probabilistic?
			ProbabilisticMatchingServiceBase matchingService = (ProbabilisticMatchingServiceBase)matchingServiceType.getMatchingService();

			PersonMatch personMatch = matchingService.linkRecords(blockingServiceTypeName, null,
						matchingServiceTypeName, null, linkTableName, leftTableName, rightTableName, null,
						ComponentType.DATA_INTEGRATOR_MODE, emOnly, true);
			personMatchWeb = ModelTransformer.convertToClientModel(personMatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personMatchWeb;
	}

}
