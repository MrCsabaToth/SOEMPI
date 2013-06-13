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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Person;
import org.springframework.transaction.annotation.Transactional;

public interface PersonDao extends UniversalDao
{
	/**
	 * Create a table where the entities will can be stored.
	 * The tableName will get a "tbl_dataset_" prefix, and the table will have configurable columns
	 * specified in the columnSpecification. The field names will get an "fld_" prefix.
	 * The table will have a primary key column automatically, a sequence will be created for that
	 * column and the table will be indexed by that also.
	 * 
	 * @param tableName: name of the table, will get "tbl_dataset_" prefix
	 * @param columnSpecification: list of the wanted columns
	 * @param withIndexesAndConstraints: will apply indexes and constraints right after table creation
	 * or it'll be done by the user later with an addIndexesAndConstraints call
	 */
    @Transactional
	public void createTable(final String tableName, final List<ColumnInformation> columnInformation,
			final boolean withIndexesAndConstraints);

	/**
	 * Remove a table where person entities are stored.
	 * 
	 * @param tableName: name of the table, will get "tbl_dataset_" prefix
	 */
    @Transactional
	public void removeTable(String tableName);

	/**
	 * Add a Person entity to the table.
	 * 
	 * @param tableName: name of the table, will get "tbl_dataset_" prefix
	 * @param person: the Person entity to be added
	 */
	public void addPerson(final String tableName, final Person person);

	/**
	 * Add more Person entities to the table in a batch with one call.
	 * 
	 * @param tableName: name of the table, will get "tbl_dataset_" prefix
	 * @param persons: list of the Person entities to be added
	 */
	public void addPersons(final String tableName, final List<Person> persons);

	/**
	 * Add indexes and constraints to the previously created table.
	 * "withIndexesAndConstraints" must have been false before during createTable call.
	 * 
	 * @param tableName: name of the table, will get "tbl_dataset_" prefix
	 * @param seqStart: starting value the created sequence should start from
	 */
    @Transactional
	public void addIndexesAndConstraints(final String tableName, final long seqStart);

	/**
	 * Update an existing Person entity in the table.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param person: the Person entity to be updated
	 */
	public void updatePerson(final String tableName, final Person person);

	/**
	 * Get a Person entity from a given table with a given identifier.
	 * 
	 * @param tableName: name of the particular Dataset table which supposed to contain the person
	 * @param personIdentifier: identifier of the person
	 * 
	 * @return Person entity or empty if no such person found in the given table
	 */
	public Person getPersonById(String tableName, long personIdentifier);

	/**
	 * Get a certain columns/fields of a Person entity from a given table with a given identifier.
	 * 
	 * @param tableName: name of the particular Dataset table which supposed to contain the person
	 * @param personIdentifier: identifier of the person
	 * @param fields: list of field names we want to retrieve with the Person
	 * 
	 * @return Person entity or empty if no such person found in the given table
	 */
	public Person getPersonById(String tableName, long personIdentifier, List<String> fields);

	/**
	 * Get a Person entities from the given table with the given identifiers.
	 * 
	 * @param tableName: name of the particular Dataset table which supposed to contain the person
	 * @param personIds: list of person identifiers
	 * 
	 * @return list of Person entities with the given ids, or empty list if no such persons found in the given table
	 */
	public List<Person> getPersonsByIds(String tableName, List<Long> personIds);

	/**
	 * Get a Person entities (but only certain columns/fields) from the given table with the given identifiers.
	 * 
	 * @param tableName: name of the particular Dataset table which supposed to contain the person
	 * @param personIds: list of person identifiers
	 * @param fields: list of field names we want to retrieve with the Person
	 * 
	 * @return list of Person entities with the given ids and fields, or empty list if no such persons found in the given table
	 */
	public List<Person> getPersonsByIds(final String tableName, final List<Long> personIds, final List<String> fields);

	/**
	 * Get a Person entity from a given table with which matches the example Person entity.
	 * The code will return only those entities, which has the same value in the fields as the example entity.
	 * 
	 * @param tableName: name of the particular Dataset table which supposed to contain the person
	 * @param example: a person containing certain fields
	 * 
	 * @return list of Person entity or empty list if no such persons found in the given table
	 */
	public List<Person> getPersonsByExample(String tableName, Person example);

	public List<Person> getPersonsByExample(String tableName, Person example, List<String> fields);

	public List<Person> getPersonsByExamplePaged(String tableName, Person example,
			long firstResult, int maxResults);

	public List<Person> getPersonsByExamplePaged(String tableName, Person example, List<String> fields,
			long firstResult, int maxResults);

	// For blocking
	public List<Person> getRandomPersonsByExample(String tableName, Person example, int maxResults);

	public List<Person> getRandomPersonsByExample(String tableName, Person example, List<String> fields,
			int maxResults);

	public List<Person> getRandomNotNullPersonsByExample(String tableName, Person example,
			int maxResults);

	public List<Person> getRandomNotNullPersonsByExample(String tableName, Person example, List<String> fields,
			int maxResults);

	public List<Person> blockRecords(String tableName, Person example, List<String> fields);
	
	public List<NameValuePair> getDistinctValues(final String tableName, String field);
	
	public List<List<NameValuePair>> getDistinctValues(final String tableName, List<String> fields);

	public double getFieldAverageLength(final String tableName, final String field);

	public long getNumberOfRecords(final String tableName);

	public int getFieldNumberOfMissing(final String tableName, final String field);

	public double getFieldMissingRatio(final String tableName, final String field);
}
