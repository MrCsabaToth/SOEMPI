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

/**
 * FunctionParameter encapsulates a parameters for a function
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public class FunctionParameterWeb extends BaseModelData
{
	private static final long serialVersionUID = -4815023943551995649L;

	public enum ParameterTypeWeb { STRING, INTEGER, DOUBLE, FLOAT };

	public static final String PARAMETER_NAME = "parameterName";
	public static final String PARAMETER_VALUE = "parameterValue";
	public static final String PARAMETER_TYPE = "parameterType";

	public ParameterTypeWeb dummyParameterType;	// This is need so ParameterTypeWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public FunctionParameterWeb() {
	}
	
	public String getParameterName() {
		return get(PARAMETER_NAME);
	}

	public void setParameterName(String parameterName) {
		set(PARAMETER_NAME, parameterName);
	}

	public String getParameterValue() {
		return get(PARAMETER_VALUE);
	}

	public void setParameterValue(String parameterValue) {
		set(PARAMETER_VALUE, parameterValue);
	}

	public ParameterTypeWeb getParameterType() {
		return get(PARAMETER_TYPE);
	}

	public void setParameterType(ParameterTypeWeb parameterType) {
		set(PARAMETER_TYPE, parameterType);
	}

	public FunctionParameterWeb clone() {
		FunctionParameterWeb clone = new FunctionParameterWeb();
		clone.setParameterName(getParameterName());
		clone.setParameterValue(getParameterValue());
		clone.setParameterType(getParameterType());
		return clone;
	}

}
