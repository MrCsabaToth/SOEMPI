/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LoaderConfigWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 3753004475702658240L;

	public static final String DELIMITER_REGEX = "delimiterRegex";
	public static final String HEADER_LINE_PRESENT = "headerLinePresent";
	public static final String LOADER_DATA_FIELDS = "loaderDataFields";

	public LoaderDataFieldWeb dummyLoaderDataField;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public LoaderConfigWeb() {
	}

	public java.lang.String getDelimiterRegex() {
		return get(DELIMITER_REGEX);
	}

	public void setDelimiterRegex(java.lang.String delimiterRegex) {
		set(DELIMITER_REGEX, delimiterRegex);
	}

	public Boolean getHeaderLinePresent() {
		return get(HEADER_LINE_PRESENT);
	}

	public void setHeaderLinePresent(Boolean headerLinePresent) {
		set(HEADER_LINE_PRESENT, headerLinePresent);
	}

	public List<org.openempi.webapp.client.model.LoaderDataFieldWeb> getLoaderDataFields() {
		return get(LOADER_DATA_FIELDS);
	}

	public void setLoaderDataFields(List<org.openempi.webapp.client.model.LoaderDataFieldWeb> loaderDataFields) {
		set(LOADER_DATA_FIELDS, loaderDataFields);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(DELIMITER_REGEX + ": ").append(getDelimiterRegex());
		sb.append("," + HEADER_LINE_PRESENT + ": ").append(getHeaderLinePresent());
		sb.append("," + LOADER_DATA_FIELDS + ": ").append(getLoaderDataFields().toString());
		return sb.toString();
	}
}
