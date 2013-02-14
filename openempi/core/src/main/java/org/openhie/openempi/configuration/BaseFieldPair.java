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
 */
@MappedSuperclass
public class BaseFieldPair extends BaseObject
{
	private static final long serialVersionUID = -2063664309167108235L;

	protected String leftFieldName;
	protected String rightFieldName;
	
	public BaseFieldPair() {
		
	}
	
	public BaseFieldPair(String leftFieldName, String rightFieldName) {
		this.leftFieldName = leftFieldName;
		this.rightFieldName = rightFieldName;
	}
	
	@Column(name = "left_field_name", nullable = false)
	public String getLeftFieldName() {
		return leftFieldName;
	}

	public void setLeftFieldName(String leftFieldName) {
		this.leftFieldName = leftFieldName;
	}

	@Column(name = "right_field_name", nullable = false)
	public String getRightFieldName() {
		return rightFieldName;
	}

	public void setRightFieldName(String rightFieldName) {
		this.rightFieldName = rightFieldName;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BaseFieldPair))
			return false;
		BaseFieldPair castOther = (BaseFieldPair) other;
		return new EqualsBuilder().
				append(leftFieldName, castOther.leftFieldName).
				append(rightFieldName, castOther.rightFieldName).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(leftFieldName).
				append(rightFieldName).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("leftFieldName", leftFieldName).
				append("rightFieldName", rightFieldName).
				toString();
	}

}
