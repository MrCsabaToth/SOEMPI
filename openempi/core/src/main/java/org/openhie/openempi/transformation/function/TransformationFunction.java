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
package org.openhie.openempi.transformation.function;

import java.util.Map;

import org.openhie.openempi.model.FieldType.FieldTypeEnum;

public interface TransformationFunction
{
	public void init(Map<String,String> configParameters);

	public Object transform(Object field, Map<String, Object> parameters);

	public String getName();

	public void setName(String name);
	
	public FieldTypeEnum getInputType();

	public void setInputType(FieldTypeEnum inputType);

	public FieldTypeEnum getOutputType();

	public void setOutputType(FieldTypeEnum outputType);

}
