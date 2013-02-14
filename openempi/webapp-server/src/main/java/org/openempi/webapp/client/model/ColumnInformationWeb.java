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

public class ColumnInformationWeb extends ColumnSpecificationWeb
{
	private static final long serialVersionUID = 7037039258655755473L;

	public static final String COLUMN_INFORMATION_ID = "columnInformationId";
	public static final String DATASET_ID = "datasetId";
	public static final String FIELD_TRANSFORMATION = "fieldTransformation";
	public static final String BLOOM_FILTER_M_PARAMETER = "bloomFilterMParameter";
	public static final String BLOOM_FILTER_K_PARAMETER = "bloomFilterKParameter";
	public static final String AVERAGE_FIELD_LENGTH = "averageFieldLength";
	public static final String NUMBER_OF_MISSING = "numberOfMissing";

	public ColumnInformationWeb()
	{
	}
	
	public ColumnInformationWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
	}
	
	public Integer getColumnInformationId() {
		return get(COLUMN_INFORMATION_ID);
	}

	public void setColumnInformationId(Integer columnInformationId) {
		set(COLUMN_INFORMATION_ID, columnInformationId);
	}

	public Integer getDatasetId() {
		return get(DATASET_ID);
	}

	public void setDatasetId(Integer datasetId) {
		set(DATASET_ID, datasetId);
	}

	public String getFieldTransformation() {
		return get(FIELD_TRANSFORMATION);
	}

	public void setFieldTransformation(String fieldTransformation) {
		set(FIELD_TRANSFORMATION, fieldTransformation);
	}

	public Integer getBloomFilterMParameter() {
		return get(BLOOM_FILTER_M_PARAMETER);
	}

	public void setBloomFilterMParameter(Integer bloomFilterMParameter) {
		set(BLOOM_FILTER_M_PARAMETER, bloomFilterMParameter);
	}

	public Integer getBloomFilterKParameter() {
		return get(BLOOM_FILTER_K_PARAMETER);
	}

	public void setBloomFilterKParameter(Integer bloomFilterKParameter) {
		set(BLOOM_FILTER_K_PARAMETER, bloomFilterKParameter);
	}

	public Double getAverageFieldLength() {
		return get(AVERAGE_FIELD_LENGTH);
	}

	public void setAverageFieldLength(Double averageFieldLength) {
		set(AVERAGE_FIELD_LENGTH, averageFieldLength);
	}

	public Integer getNumberOfMissing() {
		return get(NUMBER_OF_MISSING);
	}

	public void setNumberOfMissing(Integer numberOfMissing) {
		set(NUMBER_OF_MISSING, numberOfMissing);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + COLUMN_INFORMATION_ID + ": ").append(getColumnInformationId());
		sb.append("," + DATASET_ID + ": ").append(getDatasetId());
		sb.append("," + FIELD_TRANSFORMATION + ": ").append(getFieldTransformation());
		sb.append("," + BLOOM_FILTER_M_PARAMETER + ": ").append(getBloomFilterMParameter());
		sb.append("," + BLOOM_FILTER_K_PARAMETER + ": ").append(getBloomFilterKParameter());
		sb.append("," + AVERAGE_FIELD_LENGTH + ": ").append(getAverageFieldLength());
		sb.append("," + NUMBER_OF_MISSING + ": ").append(getNumberOfMissing());
		return sb.toString();
	}
}
