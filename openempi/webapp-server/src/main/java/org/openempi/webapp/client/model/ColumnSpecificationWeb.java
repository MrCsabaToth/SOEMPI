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

public class ColumnSpecificationWeb extends BaseFieldWeb
{
	private static final long serialVersionUID = 1507198869640662478L;

	public static final String FIELD_TYPE = "fieldType";
	public static final String FIELD_TYPE_MODIFIER = "fieldTypeModifier";
	public static final String FIELD_MEANING = "fieldMeaning";

	public FieldTypeWeb dummyFieldType;	// This is need so FieldTypeWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public FieldMeaningWeb dummyFieldMeaning;	// This is need so FieldMeaningWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public ColumnSpecificationWeb()
	{
	}
	
	public ColumnSpecificationWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + FIELD_TYPE + ": ").append(getFieldType());
		sb.append("," + FIELD_TYPE_MODIFIER + ": ").append(getFieldTypeModifier());
		sb.append("," + FIELD_MEANING + ": ").append(getFieldMeaning());
		return sb.toString();
	}
}
