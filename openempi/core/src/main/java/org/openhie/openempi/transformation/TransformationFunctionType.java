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
package org.openhie.openempi.transformation;

import java.io.Serializable;

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.transformation.function.TransformationFunction;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class TransformationFunctionType extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -4578201112531798226L;

	private String name;
	private TransformationFunction transformationFunction;
	
	public TransformationFunctionType(String name, TransformationFunction transformationFunction) {
		this.name = name;
		this.transformationFunction = transformationFunction;
	}

	public TransformationFunction getTransformationFunction() {
		return transformationFunction;
	}

	public void setTransformationFunction(TransformationFunction transformationFunction) {
		this.transformationFunction = transformationFunction;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof TransformationFunctionType))
			return false;
		TransformationFunctionType castOther = (TransformationFunctionType) other;
		return new EqualsBuilder().append(name, castOther.name).append(
				transformationFunction, castOther.transformationFunction).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(transformationFunction)
				.toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
