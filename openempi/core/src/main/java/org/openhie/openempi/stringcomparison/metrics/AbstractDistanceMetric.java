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
package org.openhie.openempi.stringcomparison.metrics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;

public abstract class AbstractDistanceMetric implements DistanceMetric
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private String name;
	private FieldTypeEnum inputType;
	private OutputType outputType;
	private Map<String, Object> parameters;
	
	public AbstractDistanceMetric() {
		parameters = new HashMap<String, Object>();
	}
	
	public AbstractDistanceMetric(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public AbstractDistanceMetric(String name) {
		this.name = name;
		parameters = new HashMap<String, Object>();
	}
	
	public AbstractDistanceMetric(String name, Map<String, Object> parameters) {
		this.name = name;
		this.parameters = parameters;
	}
	
	public String upperCase(String value) {
		if (value == null) {
			return null;
		}
		return value.toUpperCase();
	}
	
	public boolean missingValues(Object value1, Object value2) {
		if (value1 == null || value2 == null) {
			return true;
		}
		return false;
	}
	
	public double handleMissingValues(Object value1, Object value2) {
		double distance = -1.0;
//		if (value1 == null && value2 == null) {
//			distance = 1.0;
//		}
		log.trace("Computed the distance between :" + value1 + ": and :" + value2 + ": to be " + distance);
		return distance;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldTypeEnum getInputType() {
		return inputType;
	}

	public void setInputType(FieldTypeEnum inputType) {
		this.inputType = inputType;
	}

	public OutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}

	public void setParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	public Object getParameter(String key) {
		return this.parameters.get(key);
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	public void setParameters(Map<String, Object> parameters) {
		for (String parameterName : parameters.keySet()) {
			setParameter(parameterName, parameters.get(parameterName));
		}
	}
	
	public String toString() {
		return name;
	}
}
