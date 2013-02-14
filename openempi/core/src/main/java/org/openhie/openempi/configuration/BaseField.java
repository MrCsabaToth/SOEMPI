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
package org.openhie.openempi.configuration;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.openhie.openempi.model.BaseObject;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * BaseField encapsulates a base field that contains information about the person object
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@MappedSuperclass
public class BaseField extends BaseObject
{
	private static final long serialVersionUID = -4012644666481353904L;

	protected String fieldName;
	
	public BaseField() {
		
	}
	
	public BaseField(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Column(name = "field_name", nullable = false)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BaseField))
			return false;
		BaseField castOther = (BaseField) other;
		return new EqualsBuilder().append(fieldName, castOther.fieldName).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(fieldName).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("fieldName", fieldName).toString();
	}

}
