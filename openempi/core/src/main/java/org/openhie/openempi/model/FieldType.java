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
package org.openhie.openempi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * FieldType entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "field_type", schema = "public")
public class FieldType extends BaseObject implements java.io.Serializable
{
	public enum FieldTypeEnum {
		String,
		Integer,
		BigInt,
		Float,
		Double,
		Date,
		Blob,
		Any
	}

	private static final long serialVersionUID = -613747077254871503L;

	private Integer fieldTypeCd;
	private String fieldTypeName;
	private String fieldTypeDescription;
	private String fieldTypeCode;
	
	/** default constructor */
	public FieldType() {
	}

	/** minimal constructor */
	public FieldType(Integer fieldTypeCd, String fieldTypeName) {
		this.fieldTypeCd = fieldTypeCd;
		this.fieldTypeName = fieldTypeName;
	}

	/** full constructor */
	public FieldType(Integer fieldTypeCd, String fieldTypeName, String fieldTypeDescription) {
		this.fieldTypeCd = fieldTypeCd;
		this.fieldTypeName = fieldTypeName;
		this.fieldTypeDescription = fieldTypeDescription;
	}

	@Id
	@Column(name = "field_type_cd", unique = true, nullable = false)
	public Integer getFieldTypeCd() {
		return this.fieldTypeCd;
	}

	public void setFieldTypeCd(Integer fieldTypeCd) {
		this.fieldTypeCd = fieldTypeCd;
	}

	@Column(name = "field_type_name", nullable = false, length = 64)
	public String getFieldTypeName() {
		return this.fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	@Transient
	public FieldTypeEnum getFieldTypeEnum() {
		return FieldTypeEnum.valueOf(fieldTypeName);
	}
	
	@Column(name = "field_type_description")
	public String getFieldTypeDescription() {
		return this.fieldTypeDescription;
	}

	public void setFieldTypeDescription(String fieldTypeDescription) {
		this.fieldTypeDescription = fieldTypeDescription;
	}

	@Column(name = "field_type_code", nullable = false, length = 64)
	public String getFieldTypeCode() {
		return fieldTypeCode;
	}

	public void setFieldTypeCode(String fieldTypeCode) {
		this.fieldTypeCode = fieldTypeCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof FieldType))
			return false;
		FieldType castOther = (FieldType) other;
		return new EqualsBuilder().
				append(fieldTypeCd, castOther.fieldTypeCd).
				append(fieldTypeName, castOther.fieldTypeName).
				append(fieldTypeDescription, castOther.fieldTypeDescription).
				append(fieldTypeCode, castOther.fieldTypeCode).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fieldTypeCd).
				append(fieldTypeName).
				append(fieldTypeDescription).
				append(fieldTypeCode).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("fieldTypeCd", fieldTypeCd).
				append("fieldTypeName", fieldTypeName).
				append("fieldTypeDescription", fieldTypeDescription).
				append("fieldTypeCode", fieldTypeCode).
				toString();
	}

}