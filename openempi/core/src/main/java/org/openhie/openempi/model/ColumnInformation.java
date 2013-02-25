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

@Entity
@Table(name = "column_information")
@SequenceGenerator(name="column_information_seq", sequenceName="public.column_information_seq")
public class ColumnInformation extends ColumnSpecification
{
	private static final long serialVersionUID = -2980408172577533410L;

	private Integer columnInformationId;
	private Dataset dataset;
	private String fieldTransformation;
	private Integer bloomFilterMParameter;
	private Integer bloomFilterKParameter;
	private Double averageFieldLength;
	private Integer numberOfMissing;

	public ColumnInformation() {
		averageFieldLength = 0.0;
		numberOfMissing = 0;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="column_information_seq") 
	@Column(name = "column_information_id", unique = true, nullable = false)
	public Integer getColumnInformationId() {
		return columnInformationId;
	}

	public void setColumnInformationId(Integer columnInformationId) {
		this.columnInformationId = columnInformationId;
	}

	@ManyToOne
	@JoinColumn(name="dataset_id", updatable=false)
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Column(name = "field_transformation")
	public String getFieldTransformation() {
		return fieldTransformation;
	}

	public void setFieldTransformation(String fieldTransformation) {
		this.fieldTransformation = fieldTransformation;
	}

	@Column(name = "bf_m_parameter")
	public Integer getBloomFilterMParameter() {
		return bloomFilterMParameter;
	}

	public void setBloomFilterMParameter(Integer bloomFilterMParameter) {
		this.bloomFilterMParameter = bloomFilterMParameter;
	}

	@Column(name = "bf_k_parameter")
	public Integer getBloomFilterKParameter() {
		return bloomFilterKParameter;
	}

	public void setBloomFilterKParameter(Integer bloomFilterKParameter) {
		this.bloomFilterKParameter = bloomFilterKParameter;
	}

	@Column(name = "average_field_length")
	public Double getAverageFieldLength() {
		return averageFieldLength;
	}

	public void setAverageFieldLength(Double averageFieldLength) {
		this.averageFieldLength = averageFieldLength;
	}

	@Column(name = "number_of_missing")
	public Integer getNumberOfMissing() {
		return numberOfMissing;
	}

	public void setNumberOfMissing(Integer numberOfMissing) {
		this.numberOfMissing = numberOfMissing;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ColumnInformation))
			return false;
		ColumnInformation castOther = (ColumnInformation) other;
		return new EqualsBuilder().
				append(fieldTransformation, castOther.fieldTransformation).
				append(bloomFilterMParameter, castOther.bloomFilterMParameter).
				append(bloomFilterKParameter, castOther.bloomFilterKParameter).
				append(averageFieldLength, castOther.averageFieldLength).
				append(numberOfMissing, castOther.numberOfMissing).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fieldTransformation).
				append(bloomFilterMParameter).
				append(bloomFilterKParameter).
				append(averageFieldLength).
				append(numberOfMissing).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("fieldTransformation", fieldTransformation).
				append("averageFieldLength", averageFieldLength).
				append("averageFieldLength", averageFieldLength).
				append("averageFieldLength", averageFieldLength).
				append("numberOfMissing", numberOfMissing).
				toString();
	}

	@Transient
	public ColumnInformation getClone() {
		ColumnInformation ci = new ColumnInformation();
//		ci.setColumnInformationId(getColumnInformationId());
		ci.setFieldName(getFieldName());
		ci.setFieldType(getFieldType().getFieldTypeEnum());
		ci.setFieldMeaning(getFieldMeaning().getFieldMeaningEnum());
		ci.setFieldTypeModifier(getFieldTypeModifier());
		ci.setDataset(getDataset());
		ci.setFieldTransformation(getFieldTransformation());
		ci.setBloomFilterMParameter(getBloomFilterMParameter());
		ci.setBloomFilterKParameter(getBloomFilterKParameter());
		ci.setAverageFieldLength(getAverageFieldLength());
		ci.setNumberOfMissing(getNumberOfMissing());
		return ci;
	}
}
