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
package org.openhie.openempi.configuration;

import java.util.HashMap;
import java.util.Map;

import org.openhie.openempi.model.BaseObject;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * FunctionField encapsulates a field that stores information about a transformation or comparator
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public class FunctionField extends BaseObject implements Cloneable
{
	private static final long serialVersionUID = 997336669829438941L;

	private String functionName;
	private Map<String, Object> functionParameters;
	
	public FunctionField() {
		
	}
	
	public FunctionField(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Map<String, Object> getFunctionParameters() {
		return functionParameters;
	}

	public void setFunctionParameters(Map<String, Object> functionParameters) {
		this.functionParameters = functionParameters;
	}
	
	public Object clone()
	{
		FunctionField clone = new FunctionField(this.functionName);
		if (functionParameters != null) {
			Map<String, Object> cloneParameters = new HashMap<String, Object>();
			for (Map.Entry<String, Object> pairs : functionParameters.entrySet()) {
				cloneParameters.put(pairs.getKey(), pairs.getValue());
			}
		}
		return clone;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof FunctionField))
			return false;
		FunctionField castOther = (FunctionField) other;
		return new EqualsBuilder().
				append(functionName, castOther.functionName).
				append(functionParameters, castOther.functionParameters).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(functionName).
				append(functionParameters).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("functionName", functionName).
				append("functionParameters", functionParameters).
				toString();
	}

}
