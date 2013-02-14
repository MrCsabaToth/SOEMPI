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
package org.openempi.webapp.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DatasetWeb extends BaseModelData
{
	private static final long serialVersionUID = 4108609039211814572L;

	public static final String DATASET_ID = "datasetId";
	public static final String TABLE_NAME = "tableName";
	public static final String FILE_NAME = "fileName";
	public static final String TOTAL_RECORDS = "totalRecords";
	public static final String IMPORTED = "imported";
	public static final String DATE_CREATED = "dateCreated";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String COLUMNS_BUTTON = "columnsButton";

	public DatasetWeb() {
		setImported("N");
		setSaveButton("S");
		setColumnsButton("C");
	}

	public Integer getDatasetId() {
		return get(DATASET_ID);
	}

	public void setDatasetId(Integer datasetId) {
		set(DATASET_ID, datasetId);
	}

	public java.lang.String getTableName() {
		return get(TABLE_NAME);
	}

	public void setTableName(java.lang.String tableName) {
		set(TABLE_NAME, tableName);
	}

	public java.lang.String getFileName() {
		return get(FILE_NAME);
	}

	public void setFileName(java.lang.String fileName) {
		set(FILE_NAME, fileName);
	}

	public Long getTotalRecords() {
		return get(TOTAL_RECORDS);
	}

	public void setTotalRecords(Long totalRecords) {
		set(TOTAL_RECORDS, totalRecords);
	}

	public java.lang.String getImported() {
		return get(IMPORTED);
	}

	public void setImported(java.lang.String imported) {
		set(IMPORTED, imported);
	}

	public java.util.Date getDateCreated() {
		return get(DATE_CREATED);
	}

	public void setDateCreated(java.util.Date dateCreated) {
		set(DATE_CREATED, dateCreated);
	}

	public String getSaveButton() {
		return get(SAVE_BUTTON);
	}

	public void setSaveButton(String saveButton) {
		set(SAVE_BUTTON, saveButton);
	}

	public String getColumnsButton() {
		return get(COLUMNS_BUTTON);
	}

	public void setColumnsButton(String columnsButton) {
		set(COLUMNS_BUTTON, columnsButton);
	}

}
