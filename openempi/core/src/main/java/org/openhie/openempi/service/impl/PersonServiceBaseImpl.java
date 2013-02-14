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
package org.openhie.openempi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.dao.DatasetDao;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.dao.PersonMatchDao;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonServiceBase;
import org.openhie.openempi.util.ValidationUtil;

public class PersonServiceBaseImpl extends BaseServiceImpl implements PersonServiceBase
{
	protected PersonDao personDao;
	protected DatasetDao datasetDao;
	protected PersonMatchDao personMatchDao;
	protected PersonLinkDao personLinkDao;

	public List<ColumnInformation> getDatasetColumnInformation(String tableName) {
		log.debug("Getting Dataset column information");
		Dataset dataset = getDatasetByTableName(tableName);
		return dataset.getColumnInformation();
	}

	public List<String> getDatasetTableNames()
	{
		log.debug("Querying Dataset table names");
		return datasetDao.getTableNames();
	}

	public long getNumberOfRecords(String tableName) {
		return personDao.getNumberOfRecords(tableName);
	}

    public Dataset getDatasetById(int datasetId) {
    	log.debug("Loading dataset entry with identifier: " + datasetId);
    	return datasetDao.getDataset(datasetId);
    }
    
    public Dataset getDatasetByTableName(String tableName) {
    	ValidationUtil.sanityCheckFieldName(tableName);
    	log.debug("Loading dataset entry with tableName: " + tableName);
    	if (tableName == null) {
    		return null;
    	}
    	return datasetDao.getDataset(tableName);
    }
    
	public List<Dataset> getDatasets(User user) {
		log.debug("Loading dataset entries for user: " + user);
		return datasetDao.getDatasets(user);
	}
	
	public List<String> getPersonModelAttributeNames(String tableName) {
		return getPersonModelAttributeNames(tableName, null, true);
	}
	
	public List<String> getPersonModelAttributeNames(String tableName, FieldTypeEnum fieldTypeSelector) {
		return getPersonModelAttributeNames(tableName, fieldTypeSelector, false);
	}
	
	public List<String> getPersonModelAttributeNames(String tableName, FieldType fieldTypeSelector) {
		return getPersonModelAttributeNames(tableName, fieldTypeSelector.getFieldTypeEnum());
	}
	
	private List<String> getPersonModelAttributeNames(String tableName, FieldTypeEnum fieldTypeSelector, boolean all) {
		List<String> modelAttributeNames = new ArrayList<String>();
		Dataset dataset = getDatasetByTableName(tableName);
		List<ColumnInformation> columnInformations = dataset.getColumnInformation();
		for (ColumnInformation ci : columnInformations) {
			if (all || fieldTypeSelector == null || ci.getFieldType().getFieldTypeEnum() == fieldTypeSelector)
				modelAttributeNames.add(ci.getFieldName());
		}
		return modelAttributeNames;
	}
	
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

	public PersonMatchDao getPersonMatchDao() {
		return personMatchDao;
	}

	public void setPersonMatchDao(PersonMatchDao personMatchDao) {
		this.personMatchDao = personMatchDao;
	}

	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}

}
