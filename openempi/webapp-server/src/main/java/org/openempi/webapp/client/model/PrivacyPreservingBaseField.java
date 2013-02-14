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

public class PrivacyPreservingBaseField extends BaseFieldPairWeb
{
	private static final long serialVersionUID = -5970362216534102340L;

	public static final String BIT_INDEX = "bitIndex";

	public PrivacyPreservingBaseField() {
	}

	public PrivacyPreservingBaseField(String leftFieldName, String rightFieldName, Integer bitIndex) {
		set(LEFT_FIELD_NAME, leftFieldName);
		set(RIGHT_FIELD_NAME, rightFieldName);
		set(BIT_INDEX, bitIndex);
	}
	
	public Integer getBitIndex() {
		return get(BIT_INDEX);
	}

	public void setBitIndex(Integer bitIndex) {
		set(BIT_INDEX, bitIndex);
	}
	
}
