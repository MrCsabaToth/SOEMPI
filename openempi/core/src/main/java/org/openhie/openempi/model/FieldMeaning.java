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
 * FieldMeaning entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "field_meaning")
public class FieldMeaning extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -8537699194939840450L;

	public enum FieldMeaningEnum {
		OriginalId,
		
		GivenName,
		FamilyName,
		MiddleName,
		NamePrefix,
		NameSuffix,
		
		DateOfBirth,
		BirthWeight,
		BirthCity,
		BirthState,
		BirthCountry,
		MothersMaidenName,
		MothersWeightAtBirth,
		
		SSN,
		Gender,
		EthnicGroup,
		Race,
		Nationality,
		Language,
		Religion,
		MaritalStatus,
		Degree,
		
		Email,
		AddressLine1,
		AddressLine2,
		City,
		County,
		State,
		Country,
		PostalCode,
		AddressNumber,
		AddressFraction,
		AddressDirection,
		AddressStreetName,
		AddressType,
		AddressPostDirection,
		AddressOther,
		Address,

		PhoneCountryCode,
		PhoneAreaCode,
		PhoneNumber,
		PhoneExtension,

		DateCreated,
		CreatorId,
		DateChanged,
		ChangedById,
		DateVoided,
		VoidedById,

		DiagnosisCodes,

		DeathIndication,
		DeathTime,
		
		Custom1,
		Custom2,
		Custom3,
		Custom4,
		Custom5,
		Custom6,
		Custom7,
		Custom8,
		Custom9,
		Custom10,
		Custom11,
		Custom12,
		Custom13,
		Custom14,
		Custom15,
		Custom16,
		Custom17,
		Custom18,
		Custom19,
		Custom20,
		Custom21,
		Custom22,
		Custom23,
		Custom24,
		Custom25,
		Custom26,
		Custom27,
		Custom28,
		Custom29,
		Custom30,
		
		CBF
	}

	private Integer fieldMeaningCd;
	private String fieldMeaningName;
	private String fieldMeaningDescription;
	private String fieldMeaningCode;
	
	/** default constructor */
	public FieldMeaning() {
	}

	/** minimal constructor */
	public FieldMeaning(Integer fieldMeaningCd, String fieldMeaningName) {
		this.fieldMeaningCd = fieldMeaningCd;
		this.fieldMeaningName = fieldMeaningName;
	}

	/** full constructor */
	public FieldMeaning(Integer fieldMeaningCd, String fieldMeaningName, String fieldMeaningDescription) {
		this.fieldMeaningCd = fieldMeaningCd;
		this.fieldMeaningName = fieldMeaningName;
		this.fieldMeaningDescription = fieldMeaningDescription;
	}

	@Id
	@Column(name = "field_meaning_cd", unique = true, nullable = false)
	public Integer getFieldMeaningCd() {
		return this.fieldMeaningCd;
	}

	public void setFieldMeaningCd(Integer fieldMeaningCd) {
		this.fieldMeaningCd = fieldMeaningCd;
	}

	@Column(name = "field_meaning_name", nullable = false, length = 64)
	public String getFieldMeaningName() {
		return this.fieldMeaningName;
	}

	public void setFieldMeaningName(String fieldMeaningName) {
		this.fieldMeaningName = fieldMeaningName;
	}

	@Transient
	public FieldMeaningEnum getFieldMeaningEnum() {
		return FieldMeaningEnum.valueOf(fieldMeaningName);
	}
	
	@Column(name = "field_meaning_description")
	public String getFieldMeaningDescription() {
		return this.fieldMeaningDescription;
	}

	public void setFieldMeaningDescription(String fieldMeaningDescription) {
		this.fieldMeaningDescription = fieldMeaningDescription;
	}

	@Column(name = "field_meaning_code", nullable = false, length = 64)
	public String getFieldMeaningCode() {
		return fieldMeaningCode;
	}

	public void setFieldMeaningCode(String fieldMeaningCode) {
		this.fieldMeaningCode = fieldMeaningCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof FieldMeaning))
			return false;
		FieldMeaning castOther = (FieldMeaning) other;
		return new EqualsBuilder().
				append(fieldMeaningCd, castOther.fieldMeaningCd).
				append(fieldMeaningName, castOther.fieldMeaningName).
				append(fieldMeaningDescription, castOther.fieldMeaningDescription).
				append(fieldMeaningCode, castOther.fieldMeaningCode).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fieldMeaningCd).
				append(fieldMeaningName).
				append(fieldMeaningDescription).
				append(fieldMeaningCode).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("fieldMeaningCd", fieldMeaningCd).
				append("fieldMeaningName", fieldMeaningName).
				append("fieldMeaningDescription", fieldMeaningDescription).
				append("fieldMeaningCode", fieldMeaningCode).
				toString();
	}

}