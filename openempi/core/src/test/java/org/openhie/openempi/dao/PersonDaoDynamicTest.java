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
package org.openhie.openempi.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.User;
import org.openhie.openempi.util.BitArray;
import org.openhie.openempi.util.PersonUtils;

public class PersonDaoDynamicTest extends BaseDaoTestCase
{
	public static final String TEST_PERSON_TABLE_NAME = "testperson";

	private PersonDao personDao;
	private DatasetDao datasetDao;
	private FieldDao fieldDao;

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public DatasetDao getDatasetDao() {
		return datasetDao;
	}

	public void setDatasetDao(DatasetDao datasetDao) {
		this.datasetDao = datasetDao;
	}

	public FieldDao getFieldDao() {
		return fieldDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

	@Test
	public void testCreateTable() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String tableName = "dataset";
		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();
		ColumnInformation ci = new ColumnInformation();
		ci.setFieldName("string1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("32");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom1.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("string2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("64");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom2.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("intnumber");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Integer.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom3.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("bigintnumber");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.BigInt.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom4.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("floatnumber");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Float.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom5.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("doublenumber");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Double.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom6.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("blob");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Blob.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom7.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("importantdate");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom8.name()));
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Date.name()));
		columnInformation.add(ci);
		personDao.createTable(tableName, columnInformation, true);
		Dataset dataset = new Dataset(tableName, "N/A");
		dataset.setImported("Y");
		dataset.setColumnInformation(columnInformation);
		UserDao userDao = (UserDao)applicationContext.getBean("userDao");
		User user = (User) userDao.loadUserByUsername("admin");
		dataset.setOwner(user);
		dataset.setDateCreated(new java.util.Date());
		Dataset storedDataset = datasetDao.saveDataset(dataset);
		List<ColumnInformation> columnInformation2 = storedDataset.getColumnInformation();
		assertTrue(columnInformation.equals(columnInformation2));
		Iterator<ColumnInformation> it1 = columnInformation.iterator();
		Iterator<ColumnInformation> it2 = columnInformation2.iterator();
		while (it1.hasNext()) {
			assertTrue(it2.hasNext());
			ColumnInformation ci1 = it1.next();
			ColumnInformation ci2 = it2.next();
			assertTrue(ci1.getFieldName().equals(ci2.getFieldName()));
			assertTrue(ci1.getFieldType() == ci2.getFieldType());
			if (ci1.getFieldTypeModifier() == null)
				assertTrue(ci2.getFieldTypeModifier() == null);
			else
				assertTrue(ci1.getFieldTypeModifier().equals(ci2.getFieldTypeModifier()));
		}
		assertTrue(!it1.hasNext());
		System.out.println("Original column Informations:");
		for (ColumnInformation colInf : columnInformation) {
			System.out.println(colInf.toString());
		}
		System.out.println("Queried column Informations:");
		for (ColumnInformation colInf : columnInformation2) {
			System.out.println(colInf.toString());
		}
		List<String> tableNames = datasetDao.getTableNames();
		assertTrue(tableNames.size() >= 1);
		boolean found = false;
		for (String tn : tableNames) {
			if (tn.equals(tableName)) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void testCreateTables() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String tableName1 = "dataset1";
		List<ColumnInformation> columnInformation1 = new ArrayList<ColumnInformation>();
		ColumnInformation ci = new ColumnInformation();
		ci.setFieldName("string11");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("32");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom1.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("string21");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("64");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom2.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("intnumber1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Integer.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom3.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("bigintnumber1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.BigInt.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom4.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("floatnumber1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Float.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom5.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("doublenumber1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Double.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom6.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("blob1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Blob.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom7.name()));
		columnInformation1.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("importantdate1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Date.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom8.name()));
		columnInformation1.add(ci);
		personDao.createTable(tableName1, columnInformation1, false);
		personDao.addIndexesAndConstraints(tableName1);
		Dataset dataset = new Dataset(tableName1, "N/A");
		dataset.setImported("Y");
		dataset.setColumnInformation(columnInformation1);
		UserDao userDao = (UserDao)applicationContext.getBean("userDao");
		User user = (User) userDao.loadUserByUsername("admin");
		dataset.setOwner(user);
		dataset.setDateCreated(new java.util.Date());
		Dataset storedDataset = datasetDao.saveDataset(dataset);
		List<ColumnInformation> columnInformationBack1 = storedDataset.getColumnInformation();
		assertTrue(columnInformation1.equals(columnInformationBack1));
		System.out.println("Original column Informations1:");
		for (ColumnInformation colInf : columnInformation1) {
			System.out.println(colInf.toString());
		}
		System.out.println("Queried column Informations1:");
		for (ColumnInformation colInf : columnInformationBack1) {
			System.out.println(colInf.toString());
		}
		List<String> tableNames = datasetDao.getTableNames();
		assertTrue(tableNames.size() >= 1);
		boolean found = false;
		for (String tn : tableNames) {
			if (tn.equals(tableName1)) {
				found = true;
				break;
			}
		}
		assertTrue(found);

		String tableName2 = "dataset2";
		List<ColumnInformation> columnInformation2 = new ArrayList<ColumnInformation>();
		ci = new ColumnInformation();
		ci.setFieldName("string12");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("32");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom1.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("string22");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("64");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom2.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("intnumber2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Integer.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom3.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("bigintnumber2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.BigInt.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom4.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("floatnumber2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Float.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom5.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("doublenumber2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Double.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom6.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("blob2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Blob.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom7.name()));
		columnInformation2.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("importantdate2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Date.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom8.name()));
		columnInformation2.add(ci);
		personDao.createTable(tableName2, columnInformation2, true);
		Dataset dataset2 = new Dataset(tableName2, "N/A");
		dataset2.setImported("Y");
		dataset2.setColumnInformation(columnInformation2);
		dataset2.setOwner(user);
		dataset2.setDateCreated(new java.util.Date());
		Dataset storedDataset2 = datasetDao.saveDataset(dataset2);
		List<ColumnInformation> columnInformationBack2 = storedDataset2.getColumnInformation();
		assertTrue(columnInformation2.equals(columnInformationBack2));
		System.out.println("Original column Informations2:");
		for (ColumnInformation colInf : columnInformation2) {
			System.out.println(colInf.toString());
		}
		System.out.println("Queried column Informations2:");
		for (ColumnInformation colInf : columnInformationBack2) {
			System.out.println(colInf.toString());
		}
		tableNames = datasetDao.getTableNames();
		assertTrue(tableNames.size() >= 2);
		found = false;
		for (String tn : tableNames) {
			if (tn.equals(tableName2)) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void testAddUpdateGetPerson() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		try {
			String tableName = "person";
			List<Long> personIds = createTestPersonTable(tableName);

			Person personFound = personDao.getPersonById(tableName, personIds.get(0));
			assertTrue(personFound != null);
			if (personFound != null) {
				System.out.println(personFound.toStringLong());
				personFound.setAttribute(PersonUtils.CITY_NAME, "Fairfax");
				personFound.setAttribute(PersonUtils.NATIONALITY_NAME, "Greek");
				personDao.updatePerson(tableName, personFound);

				Person personFound2 = personDao.getPersonById(tableName, personIds.get(0));
				assertTrue(((String)personFound2.getAttribute(PersonUtils.CITY_NAME)).equals("Fairfax"));
				assertTrue(((String)personFound2.getAttribute(PersonUtils.NATIONALITY_NAME)).equals("Greek"));
				System.out.println(personFound2.toStringLong());
			}

			System.out.println("Found persons:");
			List<Long> personIdsToGet = new ArrayList<Long>();
			personIdsToGet.add(personIds.get(0));
			personIdsToGet.add(personIds.get(3));
			personIdsToGet.add(personIds.get(1));
			List<Person> personsFound = personDao.getPersonsByIds(tableName, personIdsToGet);
			assertTrue(personsFound != null);
			if (personsFound != null) {
				for (Person p : personsFound) {
					System.out.println(p.toStringLong());
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Test
	public void testGetPersonByExample() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, null, "All people (null example):");

		Person personExample = new Person();
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "All people (empty example):");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "USA citizens:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.GENDER_NAME, "F");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "Females:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "Males:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.FAMILY_NAME, "Toth");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "Toths:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.STATE_NAME, "Tennessee");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, "Tennesseneans:");
	}

	@Test
	public void testGetPersonByExamplePaged() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, null, 2, "Paged People (null example)");

		Person personExample = new Person();
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, 2, "Paged People (empty example)");

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, 2, "Paged Males");
	}

	@Test
	public void testGetRandomPersonByExample() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, 2, 3, "Random Person (null example)");

		Person personExample = new Person();
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, 2, 3, "Random Person (empty example)");

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, 2, 3, "Random Males");
	}
	
	@Test
	public void testGetRandomPersonByExampleAndFields() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(PersonUtils.FAMILY_NAME);
		fieldNames.add(PersonUtils.MIDDLE_NAME);
		fieldNames.add(PersonUtils.GIVEN_NAME);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, fieldNames, 2, 3, "Random Person (null example)", false);

		Person personExample = new Person();
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Person (empty example)", false);

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Males", false);
	}
	
	@Test
	public void testGetRandomNotNullPersonByExample() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(PersonUtils.FAMILY_NAME);
		fieldNames.add(PersonUtils.MIDDLE_NAME);
		fieldNames.add(PersonUtils.GIVEN_NAME);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, fieldNames, 2, 3, "Random Person (null example)", true);

		Person personExample = new Person();
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Person (empty example)", true);

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Males", true);

		// Adding two more fields, they should be non null => only Odysseas matches
		fieldNames.add(PersonUtils.SSN_NAME);
		fieldNames.add(PersonUtils.ADDRESS1_NAME);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, fieldNames, 2, 3, "Random Person (null example)", true);

		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Person (empty example)", true);

		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Males", true);

		// Adding one more field, noone has address2, so noona should match
		fieldNames.add(PersonUtils.ADDRESS2_NAME);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, fieldNames, 2, 3, "Random Person (null example)", true);

		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Person (empty example)", true);

		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, fieldNames, 2, 3, "Random Males", true);
	}
	
	@Test
	public void testGetCertainFieldsOnly() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<String> nameFields = new ArrayList<String>();
		nameFields.add(PersonUtils.GIVEN_NAME);
		nameFields.add(PersonUtils.FAMILY_NAME);

		List<String> demographicFields = new ArrayList<String>();
		demographicFields.add(PersonUtils.CITY_NAME);
		demographicFields.add(PersonUtils.STATE_NAME);
		demographicFields.add(PersonUtils.NATIONALITY_NAME);

		List<Long> personIds = createTestPersonTable();

		System.out.println("Found person with name fields:");
		Person personFound = personDao.getPersonById(TEST_PERSON_TABLE_NAME, personIds.get(0), nameFields);
		assertTrue(personFound != null);
		if (personFound != null) {
			for (String nameField : nameFields) {
				assertTrue(personFound.hasAttribute(nameField));
			}
			for (String demographicField : demographicFields) {
				assertTrue(!personFound.hasAttribute(demographicField));
			}
			System.out.println(personFound.toStringLong());
		}
		System.out.println("Found person with demographic fields:");
		personFound = personDao.getPersonById(TEST_PERSON_TABLE_NAME, personIds.get(0), demographicFields);
		assertTrue(personFound != null);
		if (personFound != null) {
			for (String nameField : nameFields) {
				assertTrue(!personFound.hasAttribute(nameField));
			}
			for (String demographicField : demographicFields) {
				assertTrue(personFound.hasAttribute(demographicField));
			}
			System.out.println(personFound.toStringLong());
		}

		System.out.println("Found persons with name fields:");
		List<Long> personIdsToGet = new ArrayList<Long>();
		personIdsToGet.add(personIds.get(0));
		personIdsToGet.add(personIds.get(3));
		personIdsToGet.add(personIds.get(1));
		List<Person> personsFound = personDao.getPersonsByIds(TEST_PERSON_TABLE_NAME, personIdsToGet, nameFields);
		assertTrue(personsFound != null);
		if (personsFound != null) {
			for (Person p : personsFound) {
				for (String nameField : nameFields) {
					assertTrue(p.hasAttribute(nameField));
				}
				for (String demographicField : demographicFields) {
					assertTrue(!p.hasAttribute(demographicField));
				}
				System.out.println(p.toStringLong());
			}
		}		
		System.out.println("Found persons with demographic fields:");
		personsFound = personDao.getPersonsByIds(TEST_PERSON_TABLE_NAME, personIds, demographicFields);
		assertTrue(personsFound != null);
		if (personsFound != null) {
			for (Person p : personsFound) {
				for (String nameField : nameFields) {
					assertTrue(!p.hasAttribute(nameField));
				}
				for (String demographicField : demographicFields) {
					assertTrue(p.hasAttribute(demographicField));
				}
				System.out.println(p.toStringLong());
			}
		}		

		// non-Paged
		Person personExample = new Person();
		personExample.setAttribute(PersonUtils.NATIONALITY_NAME, "USA");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, nameFields, "USA citizens:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.GENDER_NAME, "F");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, nameFields, "Females:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, demographicFields, "Males:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.FAMILY_NAME, "Toth");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, demographicFields, "Toths:");

		personExample = new Person();
		personExample.setAttribute(PersonUtils.STATE_NAME, "Tennessee");
		queryAndPrintResultsByExample(TEST_PERSON_TABLE_NAME, personExample, nameFields, "Tennesseneans:");

		// Paged
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, null, nameFields, 2, "Paged People (null example), name fields");
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, null, demographicFields, 2, "Paged People (null example), demo");

		personExample = new Person();
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, nameFields, 2, "Paged People (empty example), name fields");
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, demographicFields, 2, "Paged People (empty example), demo");

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, nameFields, 2, "Paged Males, name fields");
		queryAndPrintResultsByExamplePaged(TEST_PERSON_TABLE_NAME, personExample, demographicFields, 2, "Paged Males, demo");

		// Random
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, nameFields, 2, 3, "Random Person (null example), name fields", false);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, null, demographicFields, 2, 3, "Random Person (null example), demo", false);

		personExample = new Person();
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, nameFields, 2, 3, "Random Person (empty example), name fields", false);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, demographicFields, 2, 3, "Random Person (empty example), demo", false);

		personExample.setAttribute(PersonUtils.GENDER_NAME, "M");
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, nameFields, 2, 3, "Random Males, name fields", false);
		queryAndPrintRandomResultsByExample(TEST_PERSON_TABLE_NAME, personExample, demographicFields, 2, 3, "Random Males, demo", false);
	}

	@Test
	public void testGetDistinctValues() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintDistinctValuesOfField(TEST_PERSON_TABLE_NAME, PersonUtils.NATIONALITY_NAME);
		queryAndPrintDistinctValuesOfField(TEST_PERSON_TABLE_NAME, PersonUtils.GENDER_NAME);
		queryAndPrintDistinctValuesOfField(TEST_PERSON_TABLE_NAME, PersonUtils.FAMILY_NAME);

		List<String> fields = new ArrayList<String>();
		fields.add(PersonUtils.NATIONALITY_NAME);
		fields.add(PersonUtils.GENDER_NAME);
		queryAndPrintDistinctValuesOfFields(TEST_PERSON_TABLE_NAME, fields);
		fields = new ArrayList<String>();
		fields.add(PersonUtils.NATIONALITY_NAME);
		fields.add(PersonUtils.FAMILY_NAME);
		queryAndPrintDistinctValuesOfFields(TEST_PERSON_TABLE_NAME, fields);
		fields = new ArrayList<String>();
		fields.add(PersonUtils.NATIONALITY_NAME);
		fields.add(PersonUtils.GENDER_NAME);
		fields.add(PersonUtils.FAMILY_NAME);
		queryAndPrintDistinctValuesOfFields(TEST_PERSON_TABLE_NAME, fields);
		fields = new ArrayList<String>();
		fields.add(PersonUtils.GENDER_NAME);
		queryAndPrintDistinctValuesOfFields(TEST_PERSON_TABLE_NAME, fields);
		fields = new ArrayList<String>();
		fields.add(PersonUtils.NATIONALITY_NAME);
		queryAndPrintDistinctValuesOfFields(TEST_PERSON_TABLE_NAME, fields);
	}

	@Test
	public void testGetFieldAverageLength() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.GIVEN_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.FAMILY_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.MIDDLE_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.ADDRESS1_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.ADDRESS2_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.CITY_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.STATE_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.NATIONALITY_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.GENDER_NAME);
		queryAndPrintAverageLengthOfField(TEST_PERSON_TABLE_NAME, PersonUtils.SSN_NAME);
	}
	
	@Test
	public void testGetFieldMissingRatio() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		createTestPersonTable();

		queryAndPrintFieldMissingRatio(TEST_PERSON_TABLE_NAME, PersonUtils.GIVEN_NAME);
		queryAndPrintFieldMissingRatio(TEST_PERSON_TABLE_NAME, PersonUtils.MIDDLE_NAME);
		queryAndPrintFieldMissingRatio(TEST_PERSON_TABLE_NAME, PersonUtils.ADDRESS1_NAME);
		queryAndPrintFieldMissingRatio(TEST_PERSON_TABLE_NAME, PersonUtils.SSN_NAME);
	}
	
	private List<Long> createTestPersonTable(String tableName) throws ApplicationException {
		List<Long> personIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, tableName, "", datasetDao, true, applicationContext,
				false, null, personIds);
		return personIds;
	}

	private List<Long> createTestPersonTable() throws ApplicationException {
		return createTestPersonTable(TEST_PERSON_TABLE_NAME);
	}

	@Test
	public void testBlobAttributes() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String tableName = "blobtest";
		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();
		ColumnInformation ci = new ColumnInformation();
		ci.setFieldName("name");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.String.name()));
		ci.setFieldTypeModifier("32");
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom1.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("blob1");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Blob.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom2.name()));
		columnInformation.add(ci);
		ci = new ColumnInformation();
		ci.setFieldName("blob2");
		ci.setFieldType(fieldDao.findFieldTypeByName(FieldType.FieldTypeEnum.Blob.name()));
		ci.setFieldMeaning(fieldDao.findFieldMeaningByName(FieldMeaning.FieldMeaningEnum.Custom3.name()));
		columnInformation.add(ci);
		personDao.createTable(tableName, columnInformation, false);

		Person person = new Person();
		person.setAttribute("name", "Odysseas Pentakalos");
		byte[] blob1 = new byte[] { 0, 1, 2, 31, 32, 33, 63, 64, 65, 127, -1, -2, -31, -32, -33, -63, -64, -65, -127, -128 };
		person.setAttribute("blob1", blob1);
		byte[] blob2 = new byte[] { -1, -2, -31, -32, -33, -63, -64, -65, -127, -128, 0, 1, 2, 31, 32, 33, 63, 64, 65, 127 };
		person.setAttribute("blob2", blob2);
		personDao.addPerson(tableName, person);
		Long personId0 = person.getPersonId();

		Random rnd = new Random(1234567);
		person = new Person();
		person.setAttribute("name", "Demi Moorxe");
		int blobLength = 32;
		byte[] blob3 = new byte[blobLength];
		rnd.nextBytes(blob3);
		person.setAttribute("blob1", blob3);
		byte[] blob4 = new byte[blobLength];
		rnd.nextBytes(blob4);
		person.setAttribute("blob2", blob4);
		personDao.addPerson(tableName, person);
		Long personId1 = person.getPersonId();

		Person personFound = personDao.getPersonById(tableName, personId0);
		assertTrue(personFound != null);
		if (personFound != null) {
			System.out.println(personFound.toStringLong());
			byte[] blobFound1 = (byte[])personFound.getAttribute("blob1");
			assertTrue(blob1.length == blobFound1.length);
			BitArray ba1Found = new BitArray(blobLength, blobFound1);
			BitArray ba1 = new BitArray(blobLength, blob1);
			assertTrue(ba1.equals(ba1Found));

			byte[] blobFound2 = (byte[])personFound.getAttribute("blob2");
			assertTrue(blob2.length == blobFound2.length);
			BitArray ba2Found = new BitArray(blobLength, blobFound2);
			BitArray ba2 = new BitArray(blobLength, blob2);
			assertTrue(ba2.equals(ba2Found));

			Person personFound2 = personDao.getPersonById(tableName, personId1);
			System.out.println(personFound2.toStringLong());

			byte[] blobFound3 = (byte[])personFound2.getAttribute("blob1");
			assertTrue(blob3.length == blobFound3.length);
			BitArray ba3Found = new BitArray(blobLength, blobFound3);
			BitArray ba3 = new BitArray(blobLength, blob3);
			assertTrue(ba3.equals(ba3Found));

			byte[] blobFound4 = (byte[])personFound2.getAttribute("blob2");
			assertTrue(blob4.length == blobFound4.length);
			BitArray ba4Found = new BitArray(blobLength, blobFound4);
			BitArray ba4 = new BitArray(blobLength, blob4);
			assertTrue(ba4.equals(ba4Found));
		}
		personDao.addIndexesAndConstraints(tableName);
	}
	
	private void queryAndPrintResultsByExample(String tableName, Person personExample, String queryAnnouncement) {
		queryAndPrintResultsByExample(tableName, personExample, null, queryAnnouncement);
	}

	private void queryAndPrintResultsByExample(String tableName, Person personExample, List<String> fields, String queryAnnouncement) {
		System.out.println(queryAnnouncement);
		List<Person> persons = personDao.getPersonsByExample(tableName, personExample, fields);
		printResults(persons);
	}

	private void queryAndPrintResultsByExamplePaged(String tableName, Person personExample, int pagesize, String queryAnnouncement) {
		queryAndPrintResultsByExamplePaged(tableName, personExample, null, pagesize, queryAnnouncement);
	}

	private void queryAndPrintResultsByExamplePaged(String tableName, Person personExample, List<String> fields, int pagesize, String queryAnnouncement) {
		Long start = 0L;
		List<Person> persons = null;
		do {
			persons = personDao.getPersonsByExamplePaged(tableName, personExample, fields, start, pagesize);
			if (persons.size() > 0) {
				System.out.println(queryAnnouncement + " page " + (start / pagesize + 1));
				printResults(persons);
			}
			start += pagesize;
		} while (persons.size() > 0);
	}

	private void queryAndPrintRandomResultsByExample(String tableName, Person personExample, int num, int round, String queryAnnouncement) {
		queryAndPrintRandomResultsByExample(tableName, personExample, null, num, round, queryAnnouncement, false);
	}

	private void queryAndPrintRandomResultsByExample(String tableName, Person personExample, List<String> fields, int num, int round, String queryAnnouncement,
			boolean notNull) {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < round; i++) {
			if (!notNull)
				persons = personDao.getRandomPersonsByExample(tableName, personExample, fields, num);
			else
				persons = personDao.getRandomNotNullPersonsByExample(tableName, personExample, fields, num);
			if (persons.size() > 0) {
				System.out.println(queryAnnouncement + " round " + (i + 1));
				printResults(persons);
			}
		}
	}

	private void queryAndPrintDistinctValuesOfField(String tableName, String field)
	{
		System.out.println("Distinc values of field " + field + ":");
		List<NameValuePair> nvps = personDao.getDistinctValues(tableName, field);
		for(NameValuePair nvp : nvps) {
			System.out.println("\t" + nvp.getValue());
		}
	}

	private void queryAndPrintDistinctValuesOfFields(String tableName, List<String> fields)
	{
		System.out.println("Distinc values of fields " + fields + ":");
		List<List<NameValuePair>> nvpsList = personDao.getDistinctValues(tableName, fields);
		for(List<NameValuePair> nvps : nvpsList) {
			System.out.println("\tList");
			for(NameValuePair nvp : nvps) {
				System.out.println("\t\t" + nvp.getName() + "=" + nvp.getValue());
			}
		}
	}

	private void queryAndPrintAverageLengthOfField(String tableName, String field)
	{
		Double avgLength = personDao.getFieldAverageLength(tableName, field);
		System.out.println("Average field length of " + field + " is " + avgLength);
	}

	private void queryAndPrintFieldMissingRatio(String tableName, String field)
	{
		Double missingRatio = personDao.getFieldMissingRatio(tableName, field);
		System.out.println("Missing ratio of field " + field + " is " + missingRatio);
	}

	private void printResults(List<Person> persons) {
		assertTrue(persons != null);
		if (persons != null) {
			for (Person p : persons) {
				System.out.println("\t" + p.toStringLong());
			}
		}
	}

}
