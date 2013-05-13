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

import java.util.List;

import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.springframework.transaction.annotation.Transactional;

public interface PersonLinkDao extends UniversalDao
{
	/**
	 * Creates a table where the person link entities will/can be stored.
	 * The tableName will get a "tbl_link_" prefix.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param leftDatasetTableName: table name of the associated left dataset
	 * @param rightDatasetTableName: table name of the associated right dataset
	 * @param withIndexesAndConstraints: will apply indexes and constraints right after table creation
	 * or it'll be done by the user later with an addIndexesAndConstraints call
	 */
    @Transactional
	public void createTable(final String tableName, final String leftDatasetTableName,
			final String rightDatasetTableName, final boolean withIndexesAndConstraints);

	/**
	 * Deletes a table including all constraints, indexes and keys.
	 * The tableName will get a "tbl_link_" prefix.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 */
    public void removeTable(final String tableName);
    
	/**
	 * Add a PersonLink entity to the table.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param personLink: the PersonLink entity to be added
	 */
	public void addPersonLink(final String tableName, final PersonLink personLink);
	
	/**
	 * Add more PersonLink entities to the table in a batch with one call.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param personLinks: list of the PersonLink entities to be added
	 */
	public void addPersonLinks(final String tableName, final List<PersonLink> personLinks);
	
	/**
	 * Add indexes and constraints to the previously created table.
	 * "withIndexesAndConstraints" must have been false before during createTable call.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param leftDatasetTableName: table name of the associated left dataset
	 * @param rightDatasetTableName: table name of the associated right dataset
	 */
    @Transactional
	public void addIndexesAndConstraints(final String tableName, final String leftDatasetTableName,
			final String rightDatasetTableName);

	/**
	 * Query PersonLink entities from a given table, filtering by example if needed.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param leftPerson: person entity for filtering by example on the left side of the link
	 * @param rightPerson: person entity for filtering by example on the right side of the link
	 * 
	 * @return PersonLink entities fulfilling the query criteria
	 */
	public List<PersonLink> getPersonLinks(String tableName, Person leftPerson, Person rightPerson);
	
	/**
	 * Query PersonLink entities from a given table in a paged manner, filtering by example if needed.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param leftPerson: person entity for filtering by example on the left side of the link
	 * @param rightPerson: person entity for filtering by example on the right side of the link
	 * @param firstResult: first entity in the order to see
	 * @param maxResults: number of entities we want to query
	 * 
	 * @return PersonLink entities fulfilling the query criteria
	 */
	public List<PersonLink> getPersonLinksPaged(String tableName, Person leftPerson, Person rightPerson,
			long firstResult, int maxResults);
	
	/**
	 * Query PersonLink entities related to a left person, from a given, filtering by example.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param person: person entity for filtering by example on the left side of the link
	 * @param rightPerson: person entity for filtering by example on the right side of the link
	 * 
	 * @return PersonLink entities fulfilling the query criteria
	 */
	public List<PersonLink> getLinksForLeftPerson(String tableName, Person person);
	
	/**
	 * Query PersonLink entities related to a right person, from a given, filtering by example.
	 * 
	 * @param tableName: name of the table, will get "tbl_link_" prefix
	 * @param person: person entity for filtering by example on the right side of the link
	 * 
	 * @return PersonLink entities fulfilling the query criteria
	 */
	public List<PersonLink> getLinksForRightPerson(String tableName, Person person);

}
