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
package org.openhie.openempi.stringcomparison.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.stringcomparison.DistanceMetricType;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;

public class StringComparisonServiceImpl extends BaseServiceImpl implements StringComparisonService
{
	private HashMap<String,DistanceMetric> distanceMetricTypeMap;

	public double score(String metricType, Object value1, Object value2) {
		DistanceMetric distanceMetric = getDistanceMetric(metricType);
		return distanceMetric.score(value1, value2);
	}
	
	public double score(String metricType, Map<String, Object> parameters, Object value1, Object value2) {
		DistanceMetric distanceMetric = getDistanceMetric(metricType);
		distanceMetric.setParameters(parameters);
		return distanceMetric.score(value1, value2);
	}

	// Factory method pattern
	private DistanceMetric getDistanceMetric(String metricTypeName) {
		DistanceMetric distanceMetric = distanceMetricTypeMap.get(metricTypeName);
		if (distanceMetric == null) {
			log.error("Unknown distance metric requested: " + metricTypeName);
			throw new ValidationException("Unknown distance metric requested for string comparision: " + metricTypeName);
		}
		return distanceMetric;
	}
	
	public DistanceMetricType getDistanceMetricType(String name) {
		DistanceMetric metric = distanceMetricTypeMap.get(name);
		if (metric == null) {
			return null;
		}
		return new DistanceMetricType(name, metric);
	}

	public DistanceMetricType[] getDistanceMetricTypes() {
		DistanceMetricType[] list = new DistanceMetricType[distanceMetricTypeMap.keySet().size()];
		int index=0;
		for (String key : distanceMetricTypeMap.keySet()) {
			list[index++] = new DistanceMetricType(key, distanceMetricTypeMap.get(key));
		}
		return list;
	}
	
	public List<String> getComparisonFunctionNames() {
		List<String> comparisonFunctionNames = new ArrayList<String>();
		for (String key : distanceMetricTypeMap.keySet()) {
			comparisonFunctionNames.add(key);
		}
		return comparisonFunctionNames;
	}

	public List<String> getComparisonFunctionNames(FieldTypeEnum typeSelector) {
		List<String> comparisonFunctionNames = new ArrayList<String>();
		for (Map.Entry<String, DistanceMetric> pairs : distanceMetricTypeMap.entrySet()) {
			if (pairs.getValue().getInputType() == typeSelector)
				comparisonFunctionNames.add(pairs.getKey());
		}
		return comparisonFunctionNames;
	}

	public HashMap<String, DistanceMetric> getDistanceMetricTypeMap() {
		return distanceMetricTypeMap;
	}

	public void setDistanceMetricTypeMap(HashMap<String, DistanceMetric> distanceMetricTypeMap) {
		this.distanceMetricTypeMap = distanceMetricTypeMap;
	}
}
