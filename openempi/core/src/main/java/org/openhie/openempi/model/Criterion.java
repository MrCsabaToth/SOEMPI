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
package org.openhie.openempi.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Criterion extends BaseObject
{
	private static final long serialVersionUID = 2658973973160456090L;

	private String name;
	private Operation operation;
	private Object value;
	
	public Criterion() {
	}
	
	public Criterion(String name, Operation operation, Object value) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Criterion))
			return false;
		Criterion castOther = (Criterion) other;
		return new EqualsBuilder().append(name, castOther.name).append(operation, castOther.operation).append(value,
				castOther.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(operation).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("operation", operation).append("value", value)
				.toString();
	}

}
