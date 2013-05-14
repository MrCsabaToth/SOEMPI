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
package org.openhie.openempi.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.MatchPairStatDao;
import org.openhie.openempi.dao.MatchPairStatHalfDao;
import org.openhie.openempi.dao.PersonMatchRequestDao;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.transformation.function.corruption.LastnameCorruptor;
import org.openhie.openempi.util.BitArray;
import org.openhie.openempi.util.GeneralUtil;
import org.openhie.openempi.util.ValidationUtil;

public class PersonManagerServiceImpl extends PersonServiceBaseImpl implements PersonManagerService
{
	protected PersonMatchRequestDao personMatchRequestDao;
	protected MatchPairStatDao matchPairStatDao;
	protected MatchPairStatHalfDao matchPairStatHalfDao;

	protected User currentUser;

	public Dataset createDatasetTable(String tableName, List<ColumnInformation> columnInformation,
			long totalRecords, boolean cloneColumnInformation, boolean withIndexesAndConstraints)
	{
		ValidationUtil.sanityCheckFieldName(tableName);
		List<ColumnInformation> columnInformation2 = null;
		if (cloneColumnInformation)
			columnInformation2 = GeneralUtil.cloneColumnInformationList(columnInformation);
		else
			columnInformation2 = columnInformation;
		log.debug("Request for create Dataset table " + tableName);
		personDao.createTable(tableName, columnInformation2, withIndexesAndConstraints);
		boolean existed = true;
		Dataset dataset = getDatasetByTableName(tableName);
		if (dataset == null) {
			dataset = new Dataset(tableName, Constants.NOT_AVAILABLE);
			dataset.setImported("N");
			existed = false;
		}
		dataset.setTotalRecords(totalRecords);
		dataset.setColumnInformation(columnInformation2);
		if (existed == false)
			return saveDataset(dataset);

		return datasetDao.updateDataset(dataset);
	}

	public Dataset saveDataset(Dataset dataset) {
		dataset.setDateCreated(new java.util.Date());
		dataset.setOwner(getCurrentUser());
		return datasetDao.saveDataset(dataset);
	}

	public Dataset updateDataset(Dataset dataset) {
		return datasetDao.updateDataset(dataset);
	}

	public void deleteDatasetFile(Dataset dataset) {
		log.debug("Deleting a file for dataset entry: " + dataset);
		if (dataset.getDatasetId() == null) {
			return;
		}
		Dataset datasetEntry = datasetDao.getDataset(dataset.getDatasetId());
		if (datasetEntry == null) {
			return;
		}
		String fileName = datasetEntry.getFileName();
		if (fileName != null && fileName.length() > 0 && !fileName.equals(Constants.NOT_AVAILABLE)) {
			File file = new File(fileName);
			boolean deleteOutcome = file.delete();
			log.debug("Deleting physical upload file stored at: " + file.getAbsolutePath() + " returned " + deleteOutcome);
		} else {
			log.debug("There's no correpsonding physical upload file");
		}
	}

	public void removeDataset(Dataset dataset) {
		deleteDatasetFile(dataset);
		List<PersonMatch> personMatches = personMatchDao.getPersonMatches(dataset);
		for (PersonMatch pm : personMatches) {
			String matchName = pm.getMatchTitle();
			List<PersonMatchRequest> personMatchRequests = personMatchRequestDao.getPersonMatchRequestsForMatchName(matchName);
			if (personMatchRequests != null) {
				for (PersonMatchRequest pmr : personMatchRequests) {
					String pmshTableName = pmr.getMatchPairStatHalfTableName();
					if (pmshTableName != null && !pmshTableName.isEmpty()) {
						matchPairStatDao.removeTable(pmshTableName);
						matchPairStatHalfDao.removeTable(pmshTableName);
					}
					personMatchRequestDao.removePersonMatchRequest(pmr);
				}
			}
			personLinkDao.removeTable(pm.getMatchTitle());
			personMatchDao.removePersonMatch(pm);
		}
		log.debug("Removing a dataset entry: " + dataset);
		datasetDao.removeDataset(dataset);
		personDao.removeTable(dataset.getTableName());
	}

	public List<ColumnInformation> updateColumnInformation(Dataset dataset)
	{
		List<ColumnInformation> columnInformation = dataset.getColumnInformation();
		String tableName = dataset.getTableName();
		for (ColumnInformation ci : columnInformation) {
			if (ci.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.String) {
				Double averageFieldLength = personDao.getFieldAverageLength(tableName, ci.getFieldName());
				ci.setAverageFieldLength(averageFieldLength);
			}
			int numberOfMissing = personDao.getFieldNumberOfMissing(tableName, ci.getFieldName());
			ci.setNumberOfMissing(numberOfMissing);
		}
		dataset.setTotalRecords(personDao.getNumberOfRecords(tableName));
		datasetDao.updateDataset(dataset);
		return dataset.getColumnInformation();
	}

	public PersonMatch addPersonMatch(PersonMatch personMatch)
	{
		if (personMatch.getUserCreatedBy() == null)
			personMatch.setUserCreatedBy(getCurrentUser());
		else if (getCurrentUser() == null)
			getCurrentUser(personMatch.getUserCreatedBy());
		personMatch.setDateCreated(new java.util.Date());
		return personMatchDao.addPersonMatch(personMatch);
	}

	public void createLinkTable(String linkTableName, String leftDatasetName, String rightDatasetName,
			boolean withIndexesAndConstraints)
	{
		ValidationUtil.sanityCheckFieldName(linkTableName);
		ValidationUtil.sanityCheckFieldName(leftDatasetName);
		ValidationUtil.sanityCheckFieldName(rightDatasetName);
		personLinkDao.createTable(linkTableName, leftDatasetName, rightDatasetName, withIndexesAndConstraints);
	}

	private void preparePersonLink(PersonLink personLink) {
		if (personLink.getCreatorId() == null) {
			User currUser = getCurrentUser();
			if (currUser != null)
				personLink.setCreatorId(currUser.getId());
		}
		personLink.setDateCreated(new java.util.Date());		
	}

	public void addPersonLink(String linkTableName, PersonLink personLink)
	{
		preparePersonLink(personLink);
		personLinkDao.addPersonLink(linkTableName, personLink);
	}

	public void addPersonLinks(String linkTableName, List<PersonLink> personLinks)
	{
		for (PersonLink personLink : personLinks)
			preparePersonLink(personLink);
		personLinkDao.addPersonLinks(linkTableName, personLinks);
	}

	public void addIndexesAndConstraintsToLinkTable(String tableName, String leftDatasetName, String rightDatasetName)
	{
		personLinkDao.addIndexesAndConstraints(tableName, leftDatasetName, rightDatasetName);
	}

	private void addPersonInternal(String tableName, Person person, boolean applyFieldTransformations,
			boolean existenceCheck, ValidationService validationService, User currentUser,
			java.util.Date now) throws ApplicationException {
		validationService.validate(person);

		if (existenceCheck) {
			Person personFound = getPerson(tableName, person.getPersonId());
			if (personFound != null) {
				log.warn("While attempting to add a person, found an existing record with same identifier: " + person);
				throw new ApplicationException("Person record to be added already exists in the system.");
			}
		}
		
		// Before we save the entry we need to generate any custom
		// fields that have been requested through configuration
		if (applyFieldTransformations)
			applyFieldTransformations(person);

		person.setDateCreated(now);
		person.setUserCreatedBy(currentUser);
	}

	/**
	 * Add a new person to the system.
	 * 1. First it attempts to locate the person in the system using the persons identifiers. If the person is already in
	 * the system then there is nothing more to do.
	 * 2. Since the person doesn't exist in the system yet, a new record is added.
	 * 3. The matching algorithm is invoked to identify matches and association links are established between the existing
	 * patient and other aliases of the patient that were identified based on the algorithm.
	 */
	public Person addPerson(String tableName, Person person, boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		User currentUser = getCurrentUser();
		log.debug("Current user is " + currentUser);
		java.util.Date now = new java.util.Date();

		addPersonInternal(tableName, person, applyFieldTransformations, existenceCheck, validationService, currentUser, now);

		personDao.addPerson(tableName, person);

		//Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_PERSON_EVENT_TYPE, "Added a new person record", person);

		return person;
	}

	/**
	 * Add new persons to the system.
	 * 1. (if needed) First it attempts to locate each person in the system using the persons identifiers. If the person is already in
	 * the system then there is nothing more to do.
	 * 2. If the person doesn't exist in the system yet, a new record is added.
	 * 3. The matching algorithm is invoked to identify matches and association links are established between the existing
	 * patient and other aliases of the patient that were identified based on the algorithm.
	 */
	public void addPersons(String tableName, List<Person> persons, boolean applyFieldTransformations, boolean existenceCheck) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		User currentUser = getCurrentUser();
		log.debug("Current user is " + currentUser);
		java.util.Date now = new java.util.Date();
		for (Person person : persons) {
			addPersonInternal(tableName, person, applyFieldTransformations, existenceCheck, validationService, currentUser, now);
		}

		personDao.addPersons(tableName, persons);

		//Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_PERSON_EVENT_TYPE, "Added a new person record", person);
	}

	public void addIndexesAndConstraintsToDatasetTable(String tableName)
	{
		personDao.addIndexesAndConstraints(tableName);
	}

	public void deletePerson(String tableName, long personIdentifier) throws ApplicationException {
		Person personFound = personDao.getPersonById(tableName, personIdentifier);
		if (personFound == null) {
			log.warn("While attempting to delete a person was not able to locate a record with the given identifier: " + personIdentifier);
			throw new ApplicationException("Person record to be deleted does not exist in the system.");
		}

		deletePerson(tableName, personFound);
	}
		
	public void mergePersons(String tableName, long retiredIdentifier, long survivingIdentifier) throws ApplicationException {
		Person personSurviving = personDao.getPersonById(tableName, survivingIdentifier);
		if (personSurviving == null) {
			log.warn("While attempting to merge two persons was not able to locate a record with the given identifier: " + survivingIdentifier);
			throw new ApplicationException("Person record to be the survivor of a merge does not exist in the system.");
		}
		
		Person personRetiring = personDao.getPersonById(tableName, retiredIdentifier);
		if (personRetiring == null) {
			log.warn("While attempting to merge two persons was not able to locate a record with the given identifier: " + retiredIdentifier);
			throw new ApplicationException("Person record to be deleted as part of a merge does not exist in the system.");
		}
	}

	public void updatePerson(String tableName, Person person) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);

		Person personFound = getPerson(tableName, person.getPersonId());
		if (personFound == null) {
			log.warn("While attempting to update a person was not able to locate a record with the given identifier: " + person);
			throw new ApplicationException("Person record to be updated does not exist in the system.");
		}

		// Before we save the entry we need to generate any custom
		// fields that have been requested through configuration
		applyFieldTransformations(person);
		
		updatePerson(tableName, person, personFound);
		
		//Context.getAuditEventService().saveAuditEvent(AuditEventType.UPDATE_PERSON_EVENT_TYPE, "Updated an existing person record", person);
	}

	private void updatePerson(String tableName, Person person, Person personFound) {
		person.setPersonId(personFound.getPersonId());
		personDao.updatePerson(tableName, person);
	}

	private void deletePerson(String tableName, Person person) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);

		Person personFound = getPerson(tableName, person.getPersonId());
		if (personFound == null) {
			log.warn("While attempting to delete a person was not able to locate a record with the given identifier: " + person.getPersonId());
			throw new ApplicationException("Person record to be deleted does not exist in the system.");
		}		

//		java.util.Date now = new java.util.Date();
//		User currUser = getCurrentUser();
//		person.setDateVoided(now);
//		person.setUserVoidedBy(currUser);
		log.trace("Voiding the person record: " + person);
		personDao.updatePerson(tableName, person);

		//Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_PERSON_EVENT_TYPE, "Deleted a person record", personFound);
	}

	public PersonLink constructAndAddPersonLink(String linkTableName, Integer personMatchId, LeanRecordPair pair, Integer linkState) {
		PersonLink personLink = GeneralUtil.constructPersonLink(personMatchId, pair, linkState);
		addPersonLink(linkTableName, personLink);
		return personLink;
	}

	public void initializeRepository() throws ApplicationException {
		log.trace("Initialized the repository from the beginning using the underlying matching algorithm to do so.");
		Context.getMatchingService().initializeRepository();
	}
	
	private void applyFieldTransformations(Person person) {
		TransformationService transformationService = Context.getTransformationService();
//		List<LoaderDataField> loaderFields = Context.getFileLoaderConfigurationService().getConfiguration().getDataFields();
		LoaderConfig currentLoaderConfiguration =
				(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
		List<LoaderDataField> loaderFields = currentLoaderConfiguration.getDataFields();
		byte[] signingKey = null;
		if (GeneralUtil.doesConfigurationNeedKeyServer(currentLoaderConfiguration)) {
			KeyServerService ks = Context.getKeyServerService();
			signingKey = ks.getSalts(1).get(0);
		}
		for (LoaderDataField loaderField : loaderFields) {
			FunctionField fieldTransformation = loaderField.getFieldTransformation();
			if (fieldTransformation != null) {
				if (fieldTransformation.getFunctionName() != null) {
					log.trace("Need to generate a value for field " + loaderField.getFieldName() + " using function " +
							fieldTransformation.getFunctionName());
					// [ Type check BEGIN
					//TransformationFunctionType trafoFuncType = transformationService.getTransformationFunctionType(fieldTransformation.getFunctionName());
					//TransformationFunction trafoFunc = trafoFuncType.getTransformationFunction();
					//if (trafoFunc.getInputType() != FieldTypeEnum.Any && trafoFunc.getInputType() != loaderField.getFieldType().getFieldTypeEnum())
					//	throw new UnsupportedOperationException("Not compatible transformation (" + trafoFunc.getInputType() + ") and field type(" +
					//			loaderField.getFieldType().getFieldTypeEnum() + ")");
					// ] Type check END
					Object value = person.getAttribute(loaderField.getFieldName());
					log.debug("Obtained a value of " + value + " for field " + loaderField.getFieldName());
					try {
						if (value != null) {
							Map<String, Object> originalParameters = fieldTransformation.getFunctionParameters();
							Map<String, Object> parameters = null;
							if (originalParameters != null)
								parameters = (Map<String, Object>)((HashMap<String, Object>)originalParameters).clone();
							Object transformedValue = null;
							if (fieldTransformation.getFunctionName().contains("HMAC")) {	// Can be bare HMAC, or hybrid HMAC bloom filter
								if (parameters == null)
									parameters = new HashMap<String, Object>();
								parameters.put(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME, signingKey);
								transformedValue = transformationService.transform(fieldTransformation, value, parameters);
							} else {
								// Passing gender parameter for LastnameCorruptor
								// There are different swapout probabilities for male and female
								if (fieldTransformation.getFunctionName().equals(LastnameCorruptor.LASTNAME_CORRUPTOR_NAME)) {
									for (LoaderDataField ldf : loaderFields) {
										if (ldf.getFieldMeaning().getFieldMeaningEnum() == FieldMeaningEnum.Gender) {
											Object genderAttribObj = person.getAttribute(ldf.getFieldName());
											if (genderAttribObj != null) {
												String genderStr = genderAttribObj.toString();
												if (parameters == null)
													parameters = new HashMap<String, Object>();
												parameters.put(LastnameCorruptor.GENDER_TAG, genderStr);
											}
										}
									}
								}
								transformedValue = transformationService.transform(fieldTransformation, value, parameters);
							}
							if (transformedValue instanceof BitArray) {
								byte[] byteArrayRep = ((BitArray)transformedValue).getByteArrayRep();
								person.setAttribute(loaderField.getFieldName(), byteArrayRep.clone());
								log.debug("Custom field " + loaderField.getFieldName() + " is a blob");
							} else {
								person.setAttribute(loaderField.getFieldName(), transformedValue);
								log.debug("Custom field " + loaderField.getFieldName() + " has value " + person.getAttribute(
										loaderField.getFieldName()));
							}
						}
					} catch (Exception e) {
						log.error("Failed while trying to obtain property for field " + loaderField.getFieldName() + ":" + e.getMessage(), e);
					}
				}
			}
		}
	}

	private Person getPerson(String tableName, long personIdentifier) throws ApplicationException {
		return personDao.getPersonById(tableName, personIdentifier, null);
	}

	public User getCurrentUser() {
		return getCurrentUser(null);
	}

	public User getCurrentUser(User hint) {
		User currUser = Context.getUserContext().getUser();
		if (currUser != null) {
			if (this.currentUser != currUser)
				this.currentUser = currUser;
			return currUser;
		}
		// In theory this should not happen: User should not be null
		// But try to work from hint
		if (hint != null) {
			if (this.currentUser != hint)
				this.currentUser = hint;
			return hint;
		}
		// Nothing else left, return currentUser whether it's null or not
		return this.currentUser;
	}

    public void saveDatasetToFile(Dataset dataset)
    {
		log.debug("Saving a dataset entry: " + dataset);
		if (dataset.getDatasetId() == null) {
			return;
		}
		Dataset datasetEntry = datasetDao.getDataset(dataset.getDatasetId());
		if (datasetEntry == null) {
			return;
		}
		String fileName = datasetEntry.getFileName();
		if (fileName != null && fileName.length() > 0 && !fileName.equals(Constants.NOT_AVAILABLE)) {
			StringBuilder newFileNameBuilder = new StringBuilder(fileName);
			SecureRandom rnd = new SecureRandom();
			String uniqeIdStr = Integer.toString(rnd.nextInt());
			int fileExtensionDotIndex = newFileNameBuilder.lastIndexOf(".");
			if (fileExtensionDotIndex > 0)
				newFileNameBuilder.insert(fileExtensionDotIndex, uniqeIdStr);
			else
				newFileNameBuilder.append(uniqeIdStr);
			BufferedWriter writer = null;
			try {
				File newFile = new File(newFileNameBuilder.toString());
				LoaderConfig currentLoaderConfiguration =
					(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
				log.debug("Saving physical upload file, stored at: " + newFile.getAbsolutePath());
				writer = new BufferedWriter(new FileWriter(newFile));
				List<ColumnInformation> columnInformation = dataset.getColumnInformation();
				List<String> fieldNames = new ArrayList<String>();
				ColumnInformation lastCi = columnInformation.get(columnInformation.size() - 1);
				for(ColumnInformation ci : columnInformation) {
					fieldNames.add(ci.getFieldName());
					if (currentLoaderConfiguration.getHeaderLinePresent()) {
						writer.append(ci.getFieldName());
						if (!ci.getFieldName().equals(lastCi.getFieldName()))
							writer.append(",");
						else
							writer.newLine();
					}
				}

				PersonQueryService personQueryService = Context.getPersonQueryService();
				String lastFn = fieldNames.get(fieldNames.size() - 1);;
				int pageSize = Constants.PAGE_SIZE;
				Long pageStart = 0L;
				int numPersons = 0;
				do {
					List<Person> personList = personQueryService.getPersonsPaged(dataset.getTableName(), fieldNames, pageStart, pageSize);
					numPersons = personList.size();
					if (numPersons > 0) {
						for (Person person : personList) {
							for (String fn: fieldNames) {
								String value = person.getStringAttribute(fn);
								if (value != null)
									writer.append(value);
								if (!fn.equals(lastFn))
									writer.append(",");
								else
									writer.newLine();
							}
						}
					}
					pageStart += pageSize;
				} while (numPersons > 0);

			}
			catch(IOException e) {
				log.error("Failed while saving file. Error: " + e);
				throw new RuntimeException("Failed while saving file.");
			}
			finally {
				try {
					writer.close();
				} catch (IOException e) {
					log.error("Failed to close output file. Error: " + e);
					throw new RuntimeException("Failed to close output file.");
				}
			}

		} else {
			log.debug("Cannot find the upload location to save to");
		}
    }

	public PersonMatchRequestDao getPersonMatchRequestDao() {
		return personMatchRequestDao;
	}

	public void setPersonMatchRequestDao(PersonMatchRequestDao personMatchRequestDao) {
		this.personMatchRequestDao = personMatchRequestDao;
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

}
