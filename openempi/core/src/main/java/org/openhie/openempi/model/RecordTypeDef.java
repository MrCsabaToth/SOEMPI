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
package org.openhie.openempi.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.util.ConvertUtil;

public class RecordTypeDef
{
	private List<String> fieldNames = new ArrayList<String>();
	
	public RecordTypeDef(Object object) {
		fieldNames = ConvertUtil.extractProperties(object);
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}
	
	public void addFieldName(String fieldName) {
		fieldNames.add(fieldName);
	}
	
	public int fieldCount() {
		return fieldNames.size();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("fieldNames", fieldNames).toString();
	}	
}
