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
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.dao.DatasetDao;
import org.openhie.openempi.dao.FieldDao;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.matching.fellegisunter.EMSettings;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonManagerService;
import org.springframework.context.ConfigurableApplicationContext;

public class PersonUtils
{
    public static final String ORIGINAL_ID = "origId";
    public static final String GIVEN_NAME = "givenName";
    public static final String FAMILY_NAME = "familyName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String ADDRESS1_NAME = "address1";
    public static final String ADDRESS2_NAME = "address2";
    public static final String CITY_NAME = "city";
    public static final String STATE_NAME = "state";
    public static final String POSTALCODE_NAME = "postalCode";
    public static final String NATIONALITY_NAME = "nationality";
    public static final String GENDER_NAME = "gender";
    public static final String SSN_NAME = "ssn";
    public static final String DOB_NAME = "dateOfBirth";
    public static final String ACCOUNT_NAME = "account";

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private PersonUtils() {
    }

	static public Dataset createTestPersonTable(PersonManagerService personManagerService,
			String tableName, String fieldNamePostfix, boolean fetchFieldTypes,
			ConfigurableApplicationContext applicationContext, boolean explicitIds,
			List<Person> persons, List<Long> personIds) throws ApplicationException
	{
		return createTestPersonTable(null, personManagerService, tableName, fieldNamePostfix, null,
				fetchFieldTypes, applicationContext, explicitIds, persons, personIds);
	}

	static public Dataset createTestPersonTable(PersonDao personDao, String tableName,
			String fieldNamePostfix, DatasetDao datasetDao, boolean fetchFieldTypes,
			ConfigurableApplicationContext applicationContext, boolean explicitIds,
			List<Person> persons, List<Long> personIds) throws ApplicationException
	{
		return createTestPersonTable(personDao, null, tableName, fieldNamePostfix, datasetDao,
				fetchFieldTypes, applicationContext, explicitIds, persons, personIds);
	}

	static private Dataset createTestPersonTable(PersonDao personDao, PersonManagerService personManagerService,
			String tableName, String fieldNamePostfix, DatasetDao datasetDao, boolean fetchFieldTypes,
			ConfigurableApplicationContext applicationContext, boolean explicitIds, List<Person> persons,
			List<Long> personIds) throws ApplicationException
	{
		FieldDao fieldDao = null;
		if (fetchFieldTypes)
			fieldDao = (FieldDao)applicationContext.getBean("fieldDao");

		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();
		ColumnInformation ci = new ColumnInformation();
		String origIdFieldName = ORIGINAL_ID + fieldNamePostfix;
		ci.setFieldName(origIdFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Integer.name()));
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.OriginalId.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.Integer);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.OriginalId);
		}
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String givenNameFieldName = GIVEN_NAME + fieldNamePostfix;
		ci.setFieldName(givenNameFieldName);
		FieldType stringFieldType = null;
		FieldType dateFieldType = null;
		if (fetchFieldTypes) {
			stringFieldType = fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name());
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.GivenName.name()));
			dateFieldType = fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Date.name());
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.GivenName);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String familyNameFieldName = FAMILY_NAME + fieldNamePostfix;
		ci.setFieldName(familyNameFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.FamilyName.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.FamilyName);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String middleNameFieldName = MIDDLE_NAME + fieldNamePostfix;
		ci.setFieldName(middleNameFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.MiddleName.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.MiddleName);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String address1FieldName = ADDRESS1_NAME + fieldNamePostfix;
		ci.setFieldName(address1FieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.AddressLine1.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.AddressLine1);
		}
		ci.setFieldTypeModifier("128");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String address2FieldName = ADDRESS2_NAME + fieldNamePostfix;
		ci.setFieldName(address2FieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.AddressLine2.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.AddressLine2);
		}
		ci.setFieldTypeModifier("128");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String cityFieldName = CITY_NAME + fieldNamePostfix;
		ci.setFieldName(cityFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.City.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.City);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String stateFieldName = STATE_NAME + fieldNamePostfix;
		ci.setFieldName(stateFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.State.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.State);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String postalCodeFieldName = POSTALCODE_NAME + fieldNamePostfix;
		ci.setFieldName(postalCodeFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.PostalCode.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.PostalCode);
		}
		ci.setFieldTypeModifier("16");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String nationalityFieldName = NATIONALITY_NAME + fieldNamePostfix;
		ci.setFieldName(nationalityFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Nationality.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.Nationality);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String genderFieldName = GENDER_NAME + fieldNamePostfix;
		ci.setFieldName(genderFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Gender.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.Gender);
		}
		ci.setFieldTypeModifier("64");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String ssnFieldName = SSN_NAME + fieldNamePostfix;
		ci.setFieldName(ssnFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.SSN.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.SSN);
		}
		ci.setFieldTypeModifier("32");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String dateOfBirthFieldName = DOB_NAME + fieldNamePostfix;
		ci.setFieldName(dateOfBirthFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(dateFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.DateOfBirth.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.Date);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.DateOfBirth);
		}
		ci.setFieldTypeModifier("16");
		columnInformation.add(ci);
		ci = new ColumnInformation();
		String accountFieldName = ACCOUNT_NAME + fieldNamePostfix;
		ci.setFieldName(accountFieldName);
		if (fetchFieldTypes) {
			ci.setFieldType(stringFieldType);
			ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom1.name()));
		} else {
			ci.setFieldType(FieldType.FieldTypeEnum.String);
			ci.setFieldMeaning(FieldMeaning.FieldMeaningEnum.Custom1);
		}
		ci.setFieldTypeModifier("16");
		columnInformation.add(ci);

		Dataset dataset = null;
		PersonManagerService pms = personManagerService;
		if (pms != null) {
			dataset = pms.createDatasetTable(tableName, columnInformation, 6, false, false);
		} else {
			personDao.createTable(tableName, columnInformation, false);
			if (datasetDao != null) {
				dataset = new Dataset(tableName, "N/A");
				dataset.setImported("Y");
				dataset.setColumnInformation(columnInformation);
				dataset.setTotalRecords(6L);
				UserDao userDao = (UserDao)applicationContext.getBean("userDao");
				User user = (User) userDao.loadUserByUsername("admin");
				dataset.setOwner(user);
				dataset.setDateCreated(new java.util.Date());
				dataset = datasetDao.saveDataset(dataset);
			}
		}

		Person person = new Person();
		if (explicitIds)
			person.setPersonId(100L);
		person.setAttribute(origIdFieldName, 100L);
		person.setAttribute(givenNameFieldName, "Odysseas");
		person.setAttribute(familyNameFieldName, "Pentakalos");
		person.setAttribute(address1FieldName, "2930 Oak Shadow Drive");
		person.setAttribute(cityFieldName, "Herndon");
		person.setAttribute(stateFieldName, "Virginia");
		person.setAttribute(ssnFieldName, "555-55-5555");
		person.setAttribute(nationalityFieldName, "USA");
		person.setAttribute(genderFieldName, "M");
		if (pms != null)
			pms.addPerson(tableName, person, false, false);
		else
			personDao.addPerson(tableName, person);
		if (persons != null)
			persons.add(person);
		if (personIds != null)
			personIds.add(person.getPersonId());

		person = new Person();
		if (explicitIds)
			person.setPersonId(101L);
		person.setAttribute(origIdFieldName, 101L);
		person.setAttribute(givenNameFieldName, "Demi");
		person.setAttribute(familyNameFieldName, "Moorxe");
		person.setAttribute(cityFieldName, "Greenville");
		person.setAttribute(stateFieldName, "California");
		person.setAttribute(nationalityFieldName, "USA");
		person.setAttribute(genderFieldName, "F");
		if (pms != null)
			pms.addPerson(tableName, person, false, false);
		else
			personDao.addPerson(tableName, person);
		if (persons != null)
			persons.add(person);
		if (personIds != null)
			personIds.add(person.getPersonId());

		person = new Person();
		if (explicitIds)
			person.setPersonId(102L);
		person.setAttribute(origIdFieldName, 102L);
		person.setAttribute(givenNameFieldName, "Steve");
		person.setAttribute(middleNameFieldName, "L");
		person.setAttribute(familyNameFieldName, "Nyemba");
		person.setAttribute(cityFieldName, "Hermitage");
		person.setAttribute(stateFieldName, "Tennessee");
		person.setAttribute(nationalityFieldName, "Cameroon");
		person.setAttribute(genderFieldName, "M");
		if (pms != null)
			pms.addPerson(tableName, person, false, false);
		else
			personDao.addPerson(tableName, person);
		if (persons != null)
			persons.add(person);
		if (personIds != null)
			personIds.add(person.getPersonId());

		List<Person> innerCircle = new ArrayList<Person>();
		person = new Person();
		if (explicitIds)
			person.setPersonId(103L);
		person.setAttribute(origIdFieldName, 103L);
		person.setAttribute(givenNameFieldName, "Beth");
		person.setAttribute(middleNameFieldName, "Anne");
		person.setAttribute(familyNameFieldName, "Weinman");
		person.setAttribute(cityFieldName, "St Paul");
		person.setAttribute(stateFieldName, "Minnesota");
		person.setAttribute(nationalityFieldName, "USA");
		person.setAttribute(genderFieldName, "F");
		innerCircle.add(person);

		person = new Person();
		if (explicitIds)
			person.setPersonId(104L);
		person.setAttribute(origIdFieldName, 104L);
		person.setAttribute(givenNameFieldName, "Attila");
		person.setAttribute(familyNameFieldName, "Toth");
		person.setAttribute(cityFieldName, "Birmingham");
		person.setAttribute(stateFieldName, "Alabama");
		person.setAttribute(nationalityFieldName, "Hungary");
		person.setAttribute(genderFieldName, "M");
		innerCircle.add(person);

		person = new Person();
		if (explicitIds)
			person.setPersonId(105L);
		person.setAttribute(origIdFieldName, 105L);
		person.setAttribute(givenNameFieldName, "Csaba");
		person.setAttribute(familyNameFieldName, "Toth");
		person.setAttribute(cityFieldName, "Nashville");
		person.setAttribute(stateFieldName, "Tennessee");
		person.setAttribute(nationalityFieldName, "Hungary");
		person.setAttribute(genderFieldName, "M");
		innerCircle.add(person);

		if (pms != null)
			pms.addPersons(tableName, innerCircle, false, false);
		else
			personDao.addPersons(tableName, innerCircle);

		for (Person innerPerson : innerCircle) {
			if (persons != null)
				persons.add(innerPerson);
			if (personIds != null)
				personIds.add(innerPerson.getPersonId());
		}

		if (pms != null)
			pms.addIndexesAndConstraintsToDatasetTable(tableName, 106L);
		else
			personDao.addIndexesAndConstraints(tableName, 106L);

		return dataset;
	}

	public static MatchConfiguration constructDummyMatchConfiguration() {
		MatchConfiguration matchConfig = new MatchConfiguration();
		EMSettings emSettings = new EMSettings();
		emSettings.setmInitial(0.9);
		emSettings.setuInitial(0.1);
		emSettings.setpInitial(0.001);
		emSettings.setConvergenceError(1e-6);
		emSettings.setMaxIterations(100);
		emSettings.setMaxTries(8);
		matchConfig.setFalseNegativeProbability(0.1);
		matchConfig.setFalsePositiveProbability(0.9);
		matchConfig.setEmSettings(emSettings);
		List<MatchField> matchFields = new ArrayList<MatchField>();
		MatchField mf = new MatchField();
		mf.setLeftFieldName("lfn");
		mf.setRightFieldName("rfn");
		mf.setAgreementProbability(0.9);
		mf.setDisagreementProbability(0.15);
		mf.setMatchThreshold(0.85);
		mf.setComparatorFunction(new FunctionField("JaroWinkler"));
		matchFields.add(mf);
		matchConfig.setMatchFields(matchFields);
		return matchConfig;
	}

	public static FellegiSunterParameters constructDummyFellegiSunterParameters(double bias) {
		FellegiSunterParameters fsp = new FellegiSunterParameters();
		fsp.setFieldCount(2);
		fsp.setVectorFrequencies(new long[] { 1L, 2L, 3L });
		fsp.setVectorCount(3);
		fsp.setMValues(new double[] { 0.93 + bias, 0.94 + bias });
		fsp.setUValues(new double[] { 0.13 + bias, 0.14 + bias });
		fsp.setLowerBound(0.11 + bias);
		fsp.setUpperBound(0.91 + bias);
		fsp.setLambda(0.12 + bias);
		fsp.setMu(0.92 + bias);
		return fsp;
	}

}
