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

public class ColumnMatchInformationWeb extends BaseFieldPairWeb
{
	private static final long serialVersionUID = -7317727244939163849L;

	public static final String COLUMN_MATCH_INFORMATION_ID = "columnMatchInformationId";
	public static final String PERSON_MATCH_TITLE = "personMatchTitle";
	public static final String FIELD_TYPE = "fieldType";
	public static final String FIELD_TYPE_MODIFIER = "fieldTypeModifier";
	public static final String FIELD_MEANING = "fieldMeaning";
	public static final String FELLEGI_SUNTER_M_VALUE = "fellegiSunterMValue";
	public static final String FELLEGI_SUNTER_U_VALUE = "fellegiSunterUValue";
	public static final String BLOOM_FILTER_PROPOSED_M = "bloomFilterProposedM";
	public static final String BLOOM_FILTER_POSSIBLE_M = "bloomFilterPossibleM";
	public static final String BLOOM_FILTER_FINAL_M = "bloomFilterFinalM";

	public FieldTypeWeb dummyFieldType;	// This is need so FieldTypeWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public FieldMeaningWeb dummyFieldMeaning;	// This is need so FieldMeaningWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public ColumnMatchInformationWeb()
	{
	}
	
	public Integer getColumnMatchInformationId() {
		return get(COLUMN_MATCH_INFORMATION_ID);
	}

	public void setColumnMatchInformationId(Integer columnMatchInformationId) {
		set(COLUMN_MATCH_INFORMATION_ID, columnMatchInformationId);
	}

	public String getPersonMatchTitle() {
		return get(PERSON_MATCH_TITLE);
	}

	public void setPersonMatchTitle(String personMatchTitle) {
		set(PERSON_MATCH_TITLE, personMatchTitle);
	}

	public FieldTypeWeb getFieldType() {
		return get(FIELD_TYPE);
	}

	public void setFieldType(FieldTypeWeb fieldType) {
		set(FIELD_TYPE, fieldType);
	}

	public String getFieldTypeModifier() {
		return get(FIELD_TYPE_MODIFIER);
	}

	public void setFieldTypeModifier(String fieldTypeModifier) {
		set(FIELD_TYPE_MODIFIER, fieldTypeModifier);
	}

	public FieldMeaningWeb getFieldMeaning() {
		return get(FIELD_MEANING);
	}

	public void setFieldMeaning(FieldMeaningWeb fieldMeaning) {
		set(FIELD_MEANING, fieldMeaning);
	}

	public Double getFellegiSunterMValue() {
		return get(FELLEGI_SUNTER_M_VALUE);
	}

	public void setFellegiSunterMValue(Double fellegiSunterMValue) {
		set(FELLEGI_SUNTER_M_VALUE, fellegiSunterMValue);
	}

	public Double getFellegiSunterUValue() {
		return get(FELLEGI_SUNTER_U_VALUE);
	}

	public void setFellegiSunterUValue(Double fellegiSunterUValue) {
		set(FELLEGI_SUNTER_U_VALUE, fellegiSunterUValue);
	}

	public Integer getBloomFilterProposedM() {
		return get(BLOOM_FILTER_PROPOSED_M);
	}

	public void setBloomFilterProposedM(Integer bloomFilterProposedM) {
		set(BLOOM_FILTER_PROPOSED_M, bloomFilterProposedM);
	}

	public Double getBloomFilterPossibleM() {
		return get(BLOOM_FILTER_POSSIBLE_M);
	}

	public void setBloomFilterPossibleM(Double bloomFilterPossibleM) {
		set(BLOOM_FILTER_POSSIBLE_M, bloomFilterPossibleM);
	}

	public Integer getBloomFilterFinalM() {
		return get(BLOOM_FILTER_FINAL_M);
	}

	public void setBloomFilterFinalM(Integer bloomFilterFinalM) {
		set(BLOOM_FILTER_FINAL_M, bloomFilterFinalM);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + COLUMN_MATCH_INFORMATION_ID + ": ").append(getColumnMatchInformationId());
		sb.append("," + PERSON_MATCH_TITLE + ": ").append(getPersonMatchTitle());
		sb.append("," + FIELD_TYPE + ": ").append(getFieldType());
		sb.append("," + FIELD_TYPE_MODIFIER + ": ").append(getFieldTypeModifier());
		sb.append("," + FIELD_MEANING + ": ").append(getFieldMeaning());
		sb.append("," + FELLEGI_SUNTER_M_VALUE + ": ").append(getFellegiSunterMValue());
		sb.append("," + FELLEGI_SUNTER_U_VALUE + ": ").append(getFellegiSunterUValue());
		sb.append("," + BLOOM_FILTER_PROPOSED_M + ": ").append(getBloomFilterProposedM());
		sb.append("," + BLOOM_FILTER_POSSIBLE_M + ": ").append(getBloomFilterPossibleM());
		sb.append("," + BLOOM_FILTER_FINAL_M + ": ").append(getBloomFilterFinalM());
		return sb.toString();
	}
}
