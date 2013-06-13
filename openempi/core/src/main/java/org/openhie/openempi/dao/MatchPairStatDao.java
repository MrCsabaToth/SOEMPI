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

import org.openhie.openempi.model.MatchPairStat;
import org.springframework.transaction.annotation.Transactional;

public interface MatchPairStatDao extends UniversalDao
{
	/**
	 * Create a table where match pair stats will can be stored.
	 * The tableName will get a "tbl_matchpairstat_" prefix.
	 * The table will have a primary key column automatically, a sequence will be created for that
	 * column and the table will be indexed by that also.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param leftDatasetTableName: table name of the associated left dataset
	 * @param rightDatasetTableName: table name of the associated right dataset
	 * @param withIndexesAndConstraints: will apply indexes and constraints right after table creation
	 * or it'll be done by the user later with an addIndexesAndConstraints call
	 */
    @Transactional
	public void createTable(final String tableName, final String leftDatasetTableName,
			final String rightDatasetTableName, final boolean withIndexesAndConstraints);

	/**
	 * Remove a table where certain match pair stats are/could be stored.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 */
    @Transactional
	public void removeTable(final String tableName);

	/**
	 * Add a MatchPairStat entity to the table.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param matchPairStat: the MatchPairStat entity to be added
	 */
    @Transactional
	public void addMatchPairStat(final String tableName, final MatchPairStat matchPairStat);
	
	/**
	 * Add more MatchPairStat entities to the table in a batch with one call.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param matchPairStats: list of the MatchPairStat entities to be added
	 */
    @Transactional
	public void addMatchPairStats(final String tableName, final List<MatchPairStat> matchPairStats);
	
	/**
	 * Add indexes and constraints to the previously created table.
	 * "withIndexesAndConstraints" must have been false before during createTable call.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param seqStart: starting value the created sequence should start from
	 * @param leftDatasetTableName: table name of the associated left dataset
	 * @param rightDatasetTableName: table name of the associated right dataset
	 */
    @Transactional
	public void addIndexesAndConstraints(final String tableName, final long seqStart,
			final String leftDatasetTableName, final String rightDatasetTableName);

	/**
	 * Update an existing MatchPairStat entity in the table.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param matchPairStat: the MatchPairStat entity to be updated
	 */
    @Transactional
	public void updateMatchPairStat(final String tableName, final MatchPairStat matchPairStat);
	
	/**
	 * Query MatchPairStat entities from a given table in a paged manner.
	 * 
	 * @param tableName: name of the table, will get "tbl_matchpairstat_" prefix
	 * @param firstResult: first entity in the order to see
	 * @param maxResults: number of entities we want to query
	 * 
	 * @return MatchPairStat entities fulfilling the query criteria
	 */
	public List<MatchPairStat> getMatchPairStatsPaged(final String tableName, final long firstResult,
			final int maxResults);
}
