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
package org.openhie.openempi.transformation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.transformation.function.TransformationFunction;
import org.openhie.openempi.transformation.TransformationFunctionType;
import org.openhie.openempi.transformation.TransformationService;

public class TransformationServiceImpl extends BaseServiceImpl implements TransformationService
{
	private HashMap<String,TransformationFunction> transformationFunctionTypeMap;

	public TransformationServiceImpl() {
		super();
	}

	public void init(String transformationFunctionType, Map<String,String> configParameters) {
		TransformationFunction transformationFunction = getTransformationFunction(transformationFunctionType);
		transformationFunction.init(configParameters);
	}

	public Object transform(FunctionField transformationFunction, Object field) {
		return transform(transformationFunction, field, transformationFunction.getFunctionParameters());
	}

	public Object transform(FunctionField transformationFunction, Object field, Map<String, Object> parameters) {
		TransformationFunction transformationFunc = getTransformationFunction(transformationFunction.getFunctionName());
		return transformationFunc.transform(field, parameters);
	}

	// Factory method pattern
	private TransformationFunction getTransformationFunction(String transformationFunctionTypeName) {
		TransformationFunction transformationFunction = transformationFunctionTypeMap.get(transformationFunctionTypeName);
		if (transformationFunction == null) {
			log.error("Unknown transformation function requested: " + transformationFunctionTypeName);
			throw new ValidationException("Unknown transformation function requested for field transformation: " + transformationFunctionTypeName);
		}
		return transformationFunction;
	}
	
	public TransformationFunctionType getTransformationFunctionType(String name) {
		TransformationFunction function = transformationFunctionTypeMap.get(name);
		if (function == null) {
			return null;
		}
		return new TransformationFunctionType(name, function);
	}

	public TransformationFunctionType[] getTransformationFunctionTypes() {
		TransformationFunctionType[] list = new TransformationFunctionType[transformationFunctionTypeMap.keySet().size()];
		int index = 0;
		for (String key : transformationFunctionTypeMap.keySet()) {
			list[index++] = new TransformationFunctionType(key, transformationFunctionTypeMap.get(key));
		}
		return list;
	}
	
	public HashMap<String, TransformationFunction> getTransformationFunctionTypeMap() {
		return transformationFunctionTypeMap;
	}

	public List<String> getAllTransformationFunctionNames() {
		List<String> transformationFunctionNames = new ArrayList<String>();
		for (String key : transformationFunctionTypeMap.keySet()) {
			transformationFunctionNames.add(key);
		}
		return transformationFunctionNames;
	}
	
	public List<String> getTransformationFunctionNames(FieldTypeEnum fieldTypeEnum) {
		List<String> transformationFunctionNames = new ArrayList<String>();
		for (TransformationFunction trafo : transformationFunctionTypeMap.values()) {
			if (trafo.getOutputType() == fieldTypeEnum)
				transformationFunctionNames.add(trafo.getName());
		}
		return transformationFunctionNames;
	}
	
	public List<String> getTransformationFunctionNames(FieldType fieldType) {
		return getTransformationFunctionNames(fieldType.getFieldTypeEnum());
	}
	
	public void setTransformationFunctionTypeMap(HashMap<String, TransformationFunction> transformationFunctionTypeMap) {
		this.transformationFunctionTypeMap = transformationFunctionTypeMap;
	}
}
