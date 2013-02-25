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
package org.openhie.openempi.loader.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.transformation.TransformationFunctionType;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.transformation.function.TransformationFunction;

public class LoaderConfig implements Serializable
{
	private static final long serialVersionUID = -6759474696811792478L;

	private String delimiterRegex;
	private boolean headerLinePresent;
	private List<LoaderDataField> dataFields;

	public LoaderConfig() {
		dataFields = new ArrayList<LoaderDataField>();
	}

	public String getDelimiterRegex() {
		return delimiterRegex;
	}

	public void setDelimiterRegex(String delimiterRegex) {
		this.delimiterRegex = delimiterRegex;
	}

	public boolean getHeaderLinePresent() {
		return headerLinePresent;
	}

	public void setHeaderLinePresent(boolean headerLinePresent) {
		this.headerLinePresent = headerLinePresent;
	}

	public void addDataField(LoaderDataField dataField) {
		dataFields.add(dataField);
	}

	public List<LoaderDataField> getDataFields() {
		return dataFields;
	}

	@Transient
	public void checkFieldTypesCompatibleWithTransformations()
	{
		TransformationService transformationService = Context.getTransformationService();
		if (transformationService != null) {
			for(LoaderDataField loaderField : dataFields) {
				FunctionField fieldTransformation = loaderField.getFieldTransformation();
				if (fieldTransformation != null) {
					if (fieldTransformation.getFunctionName() != null) {
						TransformationFunctionType trafoFuncType = transformationService.getTransformationFunctionType(fieldTransformation.getFunctionName());
						if (trafoFuncType != null) {
							TransformationFunction trafoFunc = trafoFuncType.getTransformationFunction();
							if (trafoFunc != null) {
								if (trafoFunc.getInputType() != FieldTypeEnum.Any && trafoFunc.getInputType() != loaderField.getFieldType().getFieldTypeEnum())
									throw new UnsupportedOperationException("Not compatible transformation (" + trafoFunc.getInputType() + ") and field type(" +
												loaderField.getFieldType().getFieldTypeEnum() + ")");
							}
						}
					}
				}
			}
		}
	}
	
	public void setDataFields(List<LoaderDataField> dataFields) {
		this.dataFields = dataFields;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("delimiterRegex", delimiterRegex).
				append("headerLinePresent", headerLinePresent).
				append("dataFields", dataFields).
				toString();
	}
}
