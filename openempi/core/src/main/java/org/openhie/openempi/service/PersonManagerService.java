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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Primary interface for the OpenEMPI. It provides access to most of the functionality
 * that is typically available by an EMPI.
 * 
 * @author Odysseas Pentakalos
 * @version $Revision:  $ $Date:  $
 */
public interface PersonManagerService extends PersonServiceBase
{
	/**
	 * Creates a dataset table where the entities will can be stored.
	 * The tableName will get a "tbl_" prefix, and the table will have configurable columns
	 * specified in the columnInformation. The field names will get an "fld_" prefix.
	 * The table will have a primary key column automatically, a sequence will be created for that
	 * column and the tab;e will be indexed by that also.
	 * @param tableName: name of the table, will get "tbl_" postfix
	 * @param columnInformation: list of the wanted columns
	 * @param cloneColumnInformation: should the columnInformation be cloned before action
	 * @param withIndexesAndConstraints: will apply indexes and constraints right after table creation
	 * or it'll be done by the user later with an addIndexesAndConstraints call
	 * 
     * @return dataset the stored dataset object with identifying information
	 */
	public Dataset createDatasetTable(String tableName, List<ColumnInformation> columnInformation,
			long totalRecords, boolean cloneColumnInformation, boolean withIndexesAndConstraints);

    /**
     * Save a dataset entry
     * 
     * @param dataset a populated dataset entry
     * @return dataset the stored dataset object with identifying information
     */
    public Dataset saveDataset(Dataset dataset);
    
    /**
     * Update a dataset entry
     * 
     * @param dataset a populated dataset entry
     * @return dataset the stored dataset object with identifying information
     */
    public Dataset updateDataset(Dataset dataset);
    
    /**
     * Deletes the dataset from the physical upload file store
     *
     * @param dataset entry
     */
    void deleteDataset(Dataset dataset);

    /**
     * Removes a dataset entry from the database by its id
     * and deletes the corresponding physical file from the upload store
     *
     * @param dataset entry
     */
    void removeDataset(Dataset dataset);

	/**
	 * Calculates the missing value counts and average field length (if applicable)
	 * for the given table, and updates the DB.
	 */
	public List<ColumnInformation> updateColumnInformation(Dataset dataset);

	/**
	 * Adds a person match attempt to the SOEMPI database.
	 * User and creation date will be added automatically.
	 * 
	 * @param personMatch: partially filled PersonMatch object
	 */
	public PersonMatch addPersonMatch(PersonMatch personMatch);

	/**
	 * Creates a table for storing record linkage results between two datasets.
	 * The tableName will get a appropriate prefixes.
	 * 
	 * @param linkTableName: name of the link table
	 * @param leftDatasetName: name of the left dataset table
	 * @param rightDatasetName: name of the right dataset table
	 */
	public void createLinkTable(String linkTableName, String leftDatasetName, String rightDatasetName,
			boolean withIndexesAndConstraints);

	/**
	 * Adds a link result between two persons to the current SOEMPI link database.
	 * User and creation date will be added automatically.
	 * 
	 * @param linkTableName: name of the link table
	 * @param personLink: partially filled PersonLink object
	 */
	public void addPersonLink(String linkTableName, PersonLink personLink);

	/**
	 * Adds a link result between two persons to the current SOEMPI link database.
	 * User and creation date will be added automatically.
	 * 
	 * @param linkTableName: name of the link table
	 * @param personLinks: partially filled PersonLink objects
	 */
	public void addPersonLinks(String linkTableName, List<PersonLink> personLinks);

	/**
	 * Construct and add a link result between two persons to the current SOEMPI link database.
	 * User and creation date will be added automatically. Person ids will be deducted from the pair.
	 * 
	 * @param linkTableName: name of the link table
	 * @param personMatchId: id of the related PersonMatch
	 * @param pair: record pair structure of the two records to be linked
	 * @param linkState: link status
	 */
	public PersonLink constructAndAddPersonLink(String linkTableName, Integer personMatchId, LeanRecordPair pair, Integer linkState);

	/**
	 * Add indexes and constraints to a previously created person link table.
	 * "withIndexesAndConstraints" must have been false before during createTable call.
	 * 
	 * @param tableName: name of the person link table
	 * @param leftDatasetTableName: table name of the associated left dataset
	 * @param rightDatasetTableName: table name of the associated right dataset
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addIndexesAndConstraintsToLinkTable(String tableName, String leftDatasetName, String rightDatasetName);

	/**
	 * Adds a person record to the EMPI. The system will first check to see if a person with the same identifier is already known to the system. If the person
	 * is known already then nothing further will be done. If the person record is new, then the system will add the person to the system.
	 * 
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Person addPerson(String tableName, Person person, boolean populateCustomFields, boolean existenceCheck) throws ApplicationException;
	
	/**
	 * Adds person records to the EMPI. The system will check each person if the same identifier is already known to the system. If the person
	 * is known already then nothing further will be done. If a person record is new, then the system will add the person to the system.
	 * 
	 * @param persons
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addPersons(String tableName, List<Person> persons, boolean populateCustomFields, boolean existenceCheck) throws ApplicationException;

	/**
	 * Add indexes and constraints to a previously created dataset table.
	 * "withIndexesAndConstraints" must have been false before during createTable call.
	 * 
	 * @param tableName: name of the dataset table
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addIndexesAndConstraintsToDatasetTable(String tableName);

	/**
	 * Updates the attributes maintained in the EMPI about the person. The system will locate the person record using the person identifiers as
	 * search criteria. If the record is not found, an exception is thrown. The attributes provided by the caller are used to update the person's record
	 * and then the matching algorithm is invoked to adjust the associations between person records based on the modifications that were made to the
	 * person's attributes.
	 * 
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void updatePerson(String tableName, Person person) throws ApplicationException;
	
	/**
	 * Deletes a person from the EMPI. The system locates the person record using the person identifiers as search criteria. If the record is not
	 * found an unchecked exception is thrown to notify the caller that this record does not exist in the system. If the record is found, the record
	 * is voided from the system rather than deleted to preserve a history.
	 *  
	 *  @param personIdentifier
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deletePerson(String tableName, long personIdentifier) throws ApplicationException;

	/**
	 * Temporary until the user manager system functions well.
	 * Currently sometimes User=null, which will cause failure during persistence to tables where user is not null column
	 */
	public User getCurrentUser();
	public User getCurrentUser(User userHint);
}
