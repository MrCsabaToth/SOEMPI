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

import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface DatasetDao extends UniversalDao
{
	/**
	 * Query Datatset entities related to a given user.
	 * 
	 * @param user: User for which we search related Dataset items
	 * 
	 * @return Dataset entities related to the given User, empty list if no related items found
	 */
    public List<Dataset> getDatasets(User user);

	/**
	 * Get a Dataset entity with a given id.
	 * 
	 * @param datasetId: Dataset identifier
	 * 
	 * @return Dataset entity with the given id, null if not found
	 */
    public Dataset getDataset(Integer datasetId);
    
	/**
	 * Get Dataset entity having a specific table name
	 * 
	 * @param tableName: table name of the Dataset we search for
	 * 
	 * @return Dataset entity with a given table, null if not found
	 */
    public Dataset getDataset(String tableName);
    
	/**
	 * Add a Dataset entity to the table.
	 * 
	 * @param dataset: the Dataset entity to be added
	 * @return the added Dataset entity
	 */
    @Transactional
    public Dataset saveDataset(Dataset dataset);
    
	/**
	 * Update an existing Dataset entity in the table.
	 * 
	 * @param dataset: the Dataset entity to be updated
	 * @return the updated Dataset entity
	 */
    @Transactional
    public Dataset updateDataset(Dataset dataset);
    
	/**
	 * Remove/delete a Dataset from to the table.
	 * 
	 * @param dataset: the Dataset entity to be removed
	 */
    @Transactional
    public void removeDataset(Dataset dataset);

	/**
	 * Returns the names of the dataset tables.
	 * @return names of the dataset tables in the Dataset registry
	 */
	public List<String> getTableNames();

}
