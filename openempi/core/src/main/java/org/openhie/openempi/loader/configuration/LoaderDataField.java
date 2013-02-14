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
package org.openhie.openempi.loader.configuration;

import java.util.List;

public class LoaderDataField extends LoaderTargetField
{
	private static final long serialVersionUID = 8825789982726958680L;

	private Integer sourceColumnIndex;
	private String sourceFieldName;
	private LoaderFieldComposition fieldComposition;
	private List<LoaderSubField> subFields;

	public Integer getSourceColumnIndex() {
		return sourceColumnIndex;
	}

	public void setSourceColumnIndex(Integer sourceColumnIndex) {
		this.sourceColumnIndex = sourceColumnIndex;
	}

	public String getSourceFieldName() {
		return sourceFieldName;
	}

	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	public LoaderFieldComposition getFieldComposition() {
		return fieldComposition;
	}

	public void setFieldComposition(LoaderFieldComposition fieldComposition) {
		this.fieldComposition = fieldComposition;
	}

	public List<LoaderSubField> getSubFields() {
		return subFields;
	}

	public void setSubFields(List<LoaderSubField> subFields) {
		this.subFields = subFields;
	}

}
