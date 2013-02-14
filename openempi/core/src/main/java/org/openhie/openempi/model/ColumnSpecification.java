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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.FieldService;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.context.Context;

@MappedSuperclass
public class ColumnSpecification extends BaseField
{
	private static final long serialVersionUID = 5031249698522267112L;

	private FieldType fieldType;
	private FieldMeaning fieldMeaning;
	private String fieldTypeModifier;	// VARCHAR length in case of String and format string in case of Date
	// Special fields to store simple values at startup time (config file read) and fetch the persisted types later
	private FieldTypeEnum fieldTypeEnum;
	private FieldMeaningEnum fieldMeaningEnum;

	public ColumnSpecification()
	{
		fieldType = null;
		fieldMeaning = null;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "field_type_cd")
	public FieldType getFieldType() {
		if (fieldType == null && fieldTypeEnum != null) {
			FieldService fieldService = Context.getFieldService();
			this.fieldType = fieldService.findFieldTypeByName(fieldTypeEnum.name());
		}
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
		if (fieldType != null)
			this.fieldTypeEnum = fieldType.getFieldTypeEnum();
	}

	@Transient
	public void setFieldType(FieldTypeEnum fieldTypeEnum) {
		setFieldTypeEnum(fieldTypeEnum);
	}

	@Transient
	protected FieldTypeEnum getFieldTypeEnum() {
		return fieldTypeEnum;
	}

	@Transient
	protected void setFieldTypeEnum(FieldTypeEnum fieldTypeEnum) {
		this.fieldTypeEnum = fieldTypeEnum;
		if (fieldType != null)
			if (fieldType.getFieldTypeEnum() != fieldTypeEnum)
				fieldType = null;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "field_meaning_cd")
	public FieldMeaning getFieldMeaning() {
		if (fieldMeaning == null && fieldMeaningEnum != null) {
			FieldService fieldService = Context.getFieldService();
			this.fieldMeaning = fieldService.findFieldMeaningByName(fieldMeaningEnum.name());
		}
		return fieldMeaning;
	}

	public void setFieldMeaning(FieldMeaning fieldMeaning) {
		this.fieldMeaning = fieldMeaning;
		if (fieldMeaning != null)
			this.fieldMeaningEnum = fieldMeaning.getFieldMeaningEnum();
	}

	@Transient
	public void setFieldMeaning(FieldMeaningEnum fieldMeaningEnum) {
		setFieldMeaningEnum(fieldMeaningEnum);
	}

	@Transient
	protected FieldMeaningEnum getFieldMeaningEnum() {
		return fieldMeaningEnum;
	}

	@Transient
	protected void setFieldMeaningEnum(FieldMeaningEnum fieldMeaningEnum) {
		this.fieldMeaningEnum = fieldMeaningEnum;
		if (fieldMeaning != null)
			if (fieldMeaning.getFieldMeaningEnum() != fieldMeaningEnum)
				fieldMeaning = null;
	}

	@Column(name = "field_type_modifier")
	public String getFieldTypeModifier() {
		return fieldTypeModifier;
	}

	public void setFieldTypeModifier(String fieldTypeModifier) {
		this.fieldTypeModifier = fieldTypeModifier;
	}

	@Transient
	public void hydrateAttributes() {
		getFieldType();
		getFieldMeaning();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ColumnSpecification))
			return false;
		ColumnSpecification castOther = (ColumnSpecification) other;
		return new EqualsBuilder().
				append(fieldType, castOther.fieldType).
				append(fieldTypeModifier, castOther.fieldTypeModifier).
				append(fieldMeaning, castOther.fieldMeaning).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fieldType).
				append(fieldTypeModifier).
				append(fieldMeaning).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("fieldType", fieldType).
				append("fieldTypeModifier", fieldTypeModifier).
				append("fieldMeaning", fieldMeaning).
				toString();
	}
}
