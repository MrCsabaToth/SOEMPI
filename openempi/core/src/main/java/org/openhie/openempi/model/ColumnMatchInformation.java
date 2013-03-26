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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.configuration.BaseFieldPair;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.FieldService;

@Entity
@Table(name = "column_match_information")
@SequenceGenerator(name="column_match_information_seq", sequenceName="public.column_match_information_seq")
public class ColumnMatchInformation extends BaseFieldPair
{
	private static final long serialVersionUID = 7183106409066451543L;

	private Integer columnMatchInformationId;
	private PersonMatch personMatch;
	private FieldType fieldType;
	private String fieldTypeModifier;	// VARCHAR length in case of String and format string in case of Date
	private FieldMeaning fieldMeaning;
	private String comparisonFunctionName;
	private Double fellegiSunterMValue;
	private Double fellegiSunterUValue;
	private Integer bloomFilterProposedM;
	private Double bloomFilterPossibleM;
	private Integer bloomFilterFinalM;
	// Special fields to store simple values at startup time (config file read) and fetch the persisted types later
	private FieldTypeEnum fieldTypeEnum;
	private FieldMeaningEnum fieldMeaningEnum;
	// Transient variables for temporary computation
	private Double percentMissing;
	private Double downweightedRange;
	private Double dynamicBloomFilterLength;
	private Double percentBits;

	public ColumnMatchInformation()
	{
		fieldType = null;
		fieldMeaning = null;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="column_match_information_seq") 
	@Column(name = "column_match_information_id", unique = true, nullable = false)
	public Integer getColumnMatchInformationId() {
		return columnMatchInformationId;
	}

	public void setColumnMatchInformationId(Integer columnMatchInformationId) {
		this.columnMatchInformationId = columnMatchInformationId;
	}

	@ManyToOne
	@JoinColumn(name="person_match_id", updatable=false)
	public PersonMatch getPersonMatch() {
		return personMatch;
	}

	public void setPersonMatch(PersonMatch personMatch) {
		this.personMatch = personMatch;
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

	@Column(name = "field_type_modifier")
	public String getFieldTypeModifier() {
		return fieldTypeModifier;
	}

	public void setFieldTypeModifier(String fieldTypeModifier) {
		this.fieldTypeModifier = fieldTypeModifier;
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

	@Transient
	public void hydrateAttributes() {
		getFieldType();
		getFieldMeaning();
	}

	@Column(name = "comparison_function_name")
	public String getComparisonFunctionName() {
		return comparisonFunctionName;
	}

	public void setComparisonFunctionName(String comparisonFunctionName) {
		this.comparisonFunctionName = comparisonFunctionName;
	}

	@Column(name = "fs_m_value", nullable = false)
	public Double getFellegiSunterMValue() {
		return fellegiSunterMValue;
	}

	public void setFellegiSunterMValue(Double fellegiSunterMValue) {
		this.fellegiSunterMValue = fellegiSunterMValue;
	}

	@Column(name = "fs_u_value", nullable = false)
	public Double getFellegiSunterUValue() {
		return fellegiSunterUValue;
	}

	public void setFellegiSunterUValue(Double fellegiSunterUValue) {
		this.fellegiSunterUValue = fellegiSunterUValue;
	}

	@Transient
	private Double fellegiSunterWa = null;

	@Transient
	public Double getFellegiSunterWa() {
		if (fellegiSunterWa == null)
			fellegiSunterWa = Math.log(fellegiSunterMValue/fellegiSunterUValue);
		return fellegiSunterWa;
	}

	@Transient
	private Double fellegiSunterWd = null;

	@Transient
	public Double getFellegiSunterWd() {
		if (fellegiSunterWd == null)
			fellegiSunterWd = Math.log((1 - fellegiSunterMValue)/(1 - fellegiSunterUValue));
		return fellegiSunterWd;
	}

	@Transient
	public Double getFellegiSunterWRange() {
		return Math.abs(getFellegiSunterWa() - getFellegiSunterWd());
	}

	@Column(name = "bf_proposed_m")
	public Integer getBloomFilterProposedM() {
		return bloomFilterProposedM;
	}

	public void setBloomFilterProposedM(Integer bloomFilterProposedM) {
		this.bloomFilterProposedM = bloomFilterProposedM;
	}

	@Column(name = "bf_possible_m")
	public Double getBloomFilterPossibleM() {
		return bloomFilterPossibleM;
	}

	public void setBloomFilterPossibleM(Double bloomFilterPossibleM) {
		this.bloomFilterPossibleM = bloomFilterPossibleM;
	}

	@Column(name = "bf_final_m")
	public Integer getBloomFilterFinalM() {
		return bloomFilterFinalM;
	}

	public void setBloomFilterFinalM(Integer bloomFilterFinalM) {
		this.bloomFilterFinalM = bloomFilterFinalM;
	}

	@Transient
	public Double getPercentMissing() {
		return percentMissing;
	}

	public void setPercentMissing(Double percentMissing) {
		this.percentMissing = percentMissing;
	}

	@Transient
	public Double getDownweightedRange() {
		return downweightedRange;
	}

	public void setDownweightedRange(Double downweightedRange) {
		this.downweightedRange = downweightedRange;
	}

	@Transient
	public Double getDynamicBloomFilterLength() {
		return dynamicBloomFilterLength;
	}

	public void setDynamicBloomFilterLength(Double dynamicBloomFilterLength) {
		this.dynamicBloomFilterLength = dynamicBloomFilterLength;
	}

	@Transient
	public Double getPercentBits() {
		return percentBits;
	}

	public void setPercentBits(Double percentBits) {
		this.percentBits = percentBits;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ColumnMatchInformation))
			return false;
		ColumnMatchInformation castOther = (ColumnMatchInformation) other;
		return new EqualsBuilder().
				append(fieldType, castOther.fieldType).
				append(fieldTypeModifier, castOther.fieldTypeModifier).
				append(fieldMeaning, castOther.fieldMeaning).
				append(fellegiSunterMValue, castOther.fellegiSunterMValue).
				append(fellegiSunterUValue, castOther.fellegiSunterUValue).
				append(bloomFilterProposedM, castOther.bloomFilterProposedM).
				append(bloomFilterPossibleM, castOther.bloomFilterPossibleM).
				append(bloomFilterFinalM, castOther.bloomFilterFinalM).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fieldType).
				append(fieldTypeModifier).
				append(fieldMeaning).
				append(fellegiSunterMValue).
				append(fellegiSunterUValue).
				append(bloomFilterProposedM).
				append(bloomFilterPossibleM).
				append(bloomFilterFinalM).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("fieldType", fieldType).
				append("fieldTypeModifier", fieldTypeModifier).
				append("fieldMeaning", fieldMeaning).
				append("fellegiSunterMValue", fellegiSunterMValue).
				append("fellegiSunterUValue", fellegiSunterUValue).
				append("bloomFilterProposedM", bloomFilterProposedM).
				append("bloomFilterPossibleM", bloomFilterPossibleM).
				append("bloomFilterFinalM", bloomFilterFinalM).
				toString();
	}

	public String toStringLong() {
		return new ToStringBuilder(this).
				append("fieldType", fieldType).
				append("fieldTypeModifier", fieldTypeModifier).
				append("fieldMeaning", fieldMeaning).
				append("fellegiSunterMValue", fellegiSunterMValue).
				append("fellegiSunterUValue", fellegiSunterUValue).
				append("bloomFilterProposedM", bloomFilterProposedM).
				append("bloomFilterPossibleM", bloomFilterPossibleM).
				append("bloomFilterFinalM", bloomFilterFinalM).
				append("percentMissing", percentMissing).
				append("downweightedRange", downweightedRange).
				append("dynamicBloomFilterLength", dynamicBloomFilterLength).
				append("percentBits", percentBits).
				toString();
	}
}
