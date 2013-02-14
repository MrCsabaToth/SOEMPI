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

/**
 * NameValuePair encapsulates the combination of a column or attribute with its corresponding value
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
public class NameValuePair extends BaseObject
{
	private static final long serialVersionUID = -4012644666481353904L;
	
	private String name;
	private Object value;
	
	public NameValuePair() {
	}

	public NameValuePair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof NameValuePair))
			return false;
		NameValuePair castOther = (NameValuePair) other;
		return new EqualsBuilder().append(name, castOther.name).append(value, castOther.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("value", value).toString();
	}
}
