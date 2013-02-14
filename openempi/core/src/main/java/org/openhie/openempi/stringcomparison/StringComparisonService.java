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
package org.openhie.openempi.stringcomparison;

import java.util.List;

import org.openhie.openempi.model.FieldType.FieldTypeEnum;

public interface StringComparisonService
{
	public DistanceMetricType[] getDistanceMetricTypes();
	
	public List<String> getComparisonFunctionNames();
	
	public List<String> getComparisonFunctionNames(FieldTypeEnum typeSelector);
	
	public DistanceMetricType getDistanceMetricType(String name);
	
	public double score(String metricType, Object value1, Object value2);
	
	public double score(String metricType, java.util.Map<String, Object> parameters, Object value1, Object value2);
}
