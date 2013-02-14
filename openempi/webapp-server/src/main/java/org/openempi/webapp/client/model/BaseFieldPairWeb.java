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

public class BaseFieldPairWeb extends BaseModelData
{
	private static final long serialVersionUID = 1162394771348715659L;

	public static final String LEFT_FIELD_NAME = "leftFieldName";
	public static final String RIGHT_FIELD_NAME = "rightFieldName";

	public BaseFieldPairWeb() {
	}

	public BaseFieldPairWeb(String leftFieldName, String rightFieldName) {
		set(LEFT_FIELD_NAME, leftFieldName);
		set(RIGHT_FIELD_NAME, rightFieldName);
	}

	public String getLeftFieldName() {
		return get(LEFT_FIELD_NAME);
	}

	public void setLeftFieldName(String leftFieldName) {
		set(LEFT_FIELD_NAME, leftFieldName);
	}

	public String getRightFieldName() {
		return get(RIGHT_FIELD_NAME);
	}

	public void setRightFieldName(String rightFieldName) {
		set(RIGHT_FIELD_NAME, rightFieldName);
	}
}
