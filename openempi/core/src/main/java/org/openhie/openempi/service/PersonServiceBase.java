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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.User;

public interface PersonServiceBase
{
	/**
	 * Returns the current dataset's column information
	 * 
	 * @return dataset's column information
	 */
	public List<ColumnInformation> getDatasetColumnInformation(String tableName);
	
	/**
	 * Returns the current dataset's originalId field name
	 * 
	 * @return dataset's originalId field's name if any, null otherwise
	 */
	public String getDatasetOriginalIdFieldName(String tableName);
	
	/**
	 * Returns the available dataset tables' name
	 * 
	 * @return name of the dataset tables
	 */
	public List<String> getDatasetTableNames();
	
    /**
     * Retrieves how many records the file entry has
     * @return Long
     */
    public long getNumberOfRecords(String tableName);
    
    /**
     * Retrieves a specific file entry using the entry's primary key
     * @param unique identifier for dataset entry
     * @return Dataset
     */
    public Dataset getDatasetById(int datasetId);
    
    /**
     * Retrieves a specific file entry matching the tableName
     * @param tableName where the file was imported into
     * @return Dataset
     */
    public Dataset getDatasetByTableName(String tableName);
    
    /**
     * Retrieves the list of files that are associated with a user.
     * @param user parameter to filter on
     * @return List
     */
    public List<Dataset> getDatasets(User user);
    
	/**
	 * This method returns the list of all person attributes that are supported by the
	 * current model.
	 * The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @return The list of attributes
	 */
	public List<String> getPersonModelAttributeNames(String tableName);
	
	/**
	 * This method returns a list of wanted person attributes that are supported by the
	 * current model.
	 * The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @param fieldTypeFilter: type of fields we want, null means all fields
	 * 
	 * @return A list of attributes of the wanted type
	 */
	public List<String> getPersonModelAttributeNames(String tableName, FieldTypeEnum fieldTypeFilter);
	
	/**
	 * This method returns a list of wanted person attributes that are supported by the
	 * current model.
	 * The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @param fieldTypeFilter: type of fields we want, null means all fields
	 * 
	 * @return A list of attributes of the wanted type
	 */
	public List<String> getPersonModelAttributeNames(String tableName, FieldType fieldTypeFilter);
	
}
