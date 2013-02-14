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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * FunctionField encapsulates a field that stores information about a transformation or comparator
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public class FunctionFieldWeb extends BaseModelData
{
	private static final long serialVersionUID = 770032151728545368L;

	public static final String FUNCTION_NAME = "functionName";
	public static final String FUNCTION_PARAMETERS = "functionParameters";

	public FunctionParameterWeb dummyFunctionParameter;	// This is need so FunctionParameterWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public FunctionFieldWeb() {
	}
	
	public FunctionFieldWeb(String functionName) {
		setFunctionName(functionName);
	}
	
	public java.lang.String getFunctionName() {
		return get(FUNCTION_NAME);
	}

	public void setFunctionName(java.lang.String functionName) {
		set(FUNCTION_NAME, functionName);
	}

	public List<FunctionParameterWeb> getFunctionParameters() {
		return get(FUNCTION_PARAMETERS);
	}

	public void setFunctionParameters(List<FunctionParameterWeb> functionParameters) {
		set(FUNCTION_PARAMETERS, functionParameters);
	}
	
	public FunctionFieldWeb clone()
	{
		FunctionFieldWeb clone = new FunctionFieldWeb(getFunctionName());
		clone.setFunctionParameters(cloneParameters());
		return clone;
	}

	public List<FunctionParameterWeb> cloneParameters()
	{
		List<FunctionParameterWeb> cloneParameters = null;
		if (getFunctionParameters() != null) {
			cloneParameters = new ArrayList<FunctionParameterWeb>();
			for (FunctionParameterWeb fpw : getFunctionParameters()) {
				FunctionParameterWeb cloneParameter = fpw.clone();
				cloneParameters.add(cloneParameter);
			}
		}
		return cloneParameters;
	}

}
