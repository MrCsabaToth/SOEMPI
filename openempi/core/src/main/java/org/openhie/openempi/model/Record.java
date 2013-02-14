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

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.util.ConvertingWrapDynaBean;
import org.openhie.openempi.util.DateUtil;

public class Record
{
	private long recordId;
	private ConvertingWrapDynaBean dynaBean;
	
	public Record(Object object) {
		this.dynaBean = new ConvertingWrapDynaBean(object); 
	}
	
	public String getAsString(String fieldName) {
		Object obj = dynaBean.get(fieldName);
		if (obj == null) {
			return null;
		}
		if (obj instanceof java.util.Date) {
			return DateUtil.getDate((java.util.Date) obj);
		}
		return obj.toString();
	}

	public Object get(String fieldName) {
		return dynaBean.get(fieldName);
	}
	
	public void set(String fieldName, Object value) {
		dynaBean.set(fieldName, value);
	}
	
	public Object getObject() {
		return dynaBean.getInstance();
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public Set<String> getPropertyNames() {
		return dynaBean.getPropertyNames();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Record))
			return false;
		Record castOther = (Record) other;
		return new EqualsBuilder().append(recordId, castOther.recordId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(recordId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("recordId", recordId).append("dynaBean", dynaBean).toString();
	}	
}
