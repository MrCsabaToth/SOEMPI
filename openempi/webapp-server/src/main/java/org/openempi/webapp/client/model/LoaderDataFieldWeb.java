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
import java.util.List;

public class LoaderDataFieldWeb extends LoaderTargetFieldWeb implements Serializable
{
	private static final long serialVersionUID = -8743150588784066726L;

	public static final String SOURCE_COLUMN_INDEX = "sourceColumnIndex";
	public static final String SOURCE_FIELD_NAME = "sourceFieldName";
	public static final String LOADER_FIELD_COMPOSITION = "loaderFieldComposition";
	public static final String LOADER_SUB_FIELDS = "loaderSubFields";

	public LoaderFieldCompositionWeb dummyLoaderFieldComposition;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public LoaderSubFieldWeb dummyLoaderSubField;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public LoaderDataFieldWeb() {
	}

	public LoaderDataFieldWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
	}
	
	public LoaderDataFieldWeb(Integer sourceColumnIndex, String sourceFieldName, String fieldName) {
		set(SOURCE_COLUMN_INDEX, sourceColumnIndex);
		set(SOURCE_FIELD_NAME, sourceFieldName);
		set(FIELD_NAME, fieldName);
	}
	
	public Integer getSourceColumnIndex() {
		return get(SOURCE_COLUMN_INDEX);
	}

	public void setSourceColumnIndex(Integer sourceColumnIndex) {
		set(SOURCE_COLUMN_INDEX, sourceColumnIndex);
	}

	public String getSourceFieldName() {
		return get(SOURCE_FIELD_NAME);
	}

	public void setSourceFieldname(String sourceFieldName) {
		set(SOURCE_FIELD_NAME, sourceFieldName);
	}

	public org.openempi.webapp.client.model.LoaderFieldCompositionWeb getLoaderFieldComposition() {
		return get(LOADER_FIELD_COMPOSITION);
	}

	public void setLoaderFieldComposition(org.openempi.webapp.client.model.LoaderFieldCompositionWeb loaderFieldComposition) {
		set(LOADER_FIELD_COMPOSITION, loaderFieldComposition);
	}

	public List<org.openempi.webapp.client.model.LoaderSubFieldWeb> getLoaderSubFields() {
		return get(LOADER_SUB_FIELDS);
	}

	public void setLoaderSubFields(List<org.openempi.webapp.client.model.LoaderSubFieldWeb> loaderSubFields) {
		set(LOADER_SUB_FIELDS, loaderSubFields);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + SOURCE_COLUMN_INDEX + ": ").append(getSourceColumnIndex());
		sb.append("," + SOURCE_FIELD_NAME + ": ").append(getSourceFieldName());
		sb.append("," + LOADER_FIELD_COMPOSITION + ": ").append(getLoaderFieldComposition().toString());
		sb.append("," + LOADER_SUB_FIELDS + ": ").append(getLoaderSubFields().toString());
		return sb.toString();
	}
}
