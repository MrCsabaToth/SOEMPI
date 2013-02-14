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

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PersonMatchWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = -8383389861504279414L;

	public static final String PERSON_MATCH_ID = "personMatchId";
	public static final String MATCH_TITLE = "matchTitle";
	public static final String LEFT_DATASET_NAME = "leftDatasetName";
	public static final String RIGHT_DATASET_NAME = "rightDatasetName";
	public static final String BLOOM_FILTER_K_PARAMETER = "bloomFilterKParameter";
	public static final String BLOOM_FILTER_FILL_FACTOR = "bloomFilterFillFactor";
	public static final String TOTAL_RECORDS = "totalRecords";
	public static final String DATE_CREATED = "dateCreated";

	public static final String MATCH_COLUMNS_BUTTON = "matchColumnsButton";
	public static final String EM_RESULTS_BUTTON = "emResultsButton";
	public static final String SCORE_CHART_BUTTON = "scoreChartButton";
	public static final String RECORD_PAIRS_BUTTON = "recordPairsButton";

	public PersonMatchWeb() {
		setMatchColumnsButton("MC");
		setEMResultsButton("EM");
		setScoreChartButton("SC");
		setRecordPairsButton("RP");
	}

	public Integer getPersonMatchId() {
		return get(PERSON_MATCH_ID);
	}

	public void setPersonMatchId(Integer personMatchId) {
		set(PERSON_MATCH_ID, personMatchId);
	}

	public String getMatchTitle() {
		return get(MATCH_TITLE);
	}

	public void setMatchTitle(String matchTitle) {
		set(MATCH_TITLE, matchTitle);
	}

	public String getLeftDatasetName() {
		return get(LEFT_DATASET_NAME);
	}

	public void setLeftDatasetName(String leftDatasetName) {
		set(LEFT_DATASET_NAME, leftDatasetName);
	}

	public String getRightDatasetName() {
		return get(RIGHT_DATASET_NAME);
	}

	public void setRightDatasetName(String rightDatasetName) {
		set(RIGHT_DATASET_NAME, rightDatasetName);
	}

	public Integer getBloomFilterKParameter() {
		return get(BLOOM_FILTER_K_PARAMETER);
	}

	public void setBloomFilterKParameter(Integer bloomFilterKParameter) {
		set(BLOOM_FILTER_K_PARAMETER, bloomFilterKParameter);
	}

	public Double getBloomFilterFillFactor() {
		return get(BLOOM_FILTER_FILL_FACTOR);
	}

	public void setBloomFilterFillFactor(Double bloomFilterFillFactor) {
		set(BLOOM_FILTER_FILL_FACTOR, bloomFilterFillFactor);
	}

	public Long getTotalRecords() {
		return get(TOTAL_RECORDS);
	}

	public void setTotalRecords(Long totalRecords) {
		set(TOTAL_RECORDS, totalRecords);
	}

	public java.util.Date getDateCreated() {
		return get(DATE_CREATED);
	}

	public void setDateCreated(java.util.Date dateCreated) {
		set(DATE_CREATED, dateCreated);
	}

	public String getMatchColumnsButton() {
		return get(MATCH_COLUMNS_BUTTON);
	}

	public void setMatchColumnsButton(String matchColumnsButton) {
		set(MATCH_COLUMNS_BUTTON, matchColumnsButton);
	}

	public String getEMResultsButton() {
		return get(EM_RESULTS_BUTTON);
	}

	public void setEMResultsButton(String emResultsButton) {
		set(EM_RESULTS_BUTTON, emResultsButton);
	}

	public String getScoreChartButton() {
		return get(SCORE_CHART_BUTTON);
	}

	public void setScoreChartButton(String scoreChartButton) {
		set(SCORE_CHART_BUTTON, scoreChartButton);
	}

	public String getRecordPairsButton() {
		return get(RECORD_PAIRS_BUTTON);
	}

	public void setRecordPairsButton(String recordPairsButton) {
		set(RECORD_PAIRS_BUTTON, recordPairsButton);
	}

}
