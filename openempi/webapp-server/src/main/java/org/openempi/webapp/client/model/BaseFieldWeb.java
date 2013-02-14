/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class BaseFieldWeb extends BaseModelData
{
	private static final long serialVersionUID = -8700850916366090820L;

	public static final String FIELD_NAME = "fieldName";

	public BaseFieldWeb() {
	}

	public BaseFieldWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
	}

	public String getFieldName() {
		return get(FIELD_NAME);
	}

	public void setFieldName(String fieldName) {
		set(FIELD_NAME, fieldName);
	}
}
