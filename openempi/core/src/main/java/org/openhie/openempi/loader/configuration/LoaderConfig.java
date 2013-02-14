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
package org.openhie.openempi.loader.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LoaderConfig implements Serializable
{
	private static final long serialVersionUID = -6759474696811792478L;

	private String delimiterRegex;
	private boolean headerLinePresent;
	private List<LoaderDataField> dataFields;

	public LoaderConfig() {
		dataFields = new ArrayList<LoaderDataField>();
	}

	public String getDelimiterRegex() {
		return delimiterRegex;
	}

	public void setDelimiterRegex(String delimiterRegex) {
		this.delimiterRegex = delimiterRegex;
	}

	public boolean getHeaderLinePresent() {
		return headerLinePresent;
	}

	public void setHeaderLinePresent(boolean headerLinePresent) {
		this.headerLinePresent = headerLinePresent;
	}

	public void addDataField(LoaderDataField dataField) {
		dataFields.add(dataField);
	}

	public List<LoaderDataField> getDataFields() {
		return dataFields;
	}

	public void setDataFields(List<LoaderDataField> dataFields) {
		this.dataFields = dataFields;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("delimiterRegex", delimiterRegex).
				append("headerLinePresent", headerLinePresent).
				append("dataFields", dataFields).
				toString();
	}
}
