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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * AuditEventType entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "audit_event_type")
public class AuditEventType extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 6170253797278224912L;
	
	public static final String ADD_PERSON_EVENT_TYPE = "ADD";
	public static final String DELETE_PERSON_EVENT_TYPE = "DEL";
	public static final String IMPORT_PERSON_EVENT_TYPE = "IMP";
	public static final String MERGE_PERSON_EVENT_TYPE = "MRG";
	public static final String UNMERGE_PERSON_EVENT_TYPE = "UMG";
	public static final String UPDATE_PERSON_EVENT_TYPE = "UPD";
	public static final String ADD_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE = "AIA";
	public static final String OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE = "OID";
	public static final String DELETE_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE = "DIA";
	public static final String UPDATE_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE = "UID";

	public static final String ADD_KEY_EVENT_TYPE = "ADDKEY";
	public static final String DELETE_KEY_EVENT_TYPE = "DELKEY";
	public static final String ADD_SALT_EVENT_TYPE = "ADDSALT";
	public static final String DELETE_SALT_EVENT_TYPE = "DELSALT";

	private Integer auditEventTypeCd;
	private String auditEventTypeName;
	private String auditEventTypeDescription;
	private String auditEventTypeCode;
	
	/** default constructor */
	public AuditEventType() {
	}

	/** minimal constructor */
	public AuditEventType(Integer auditEventTypeCd, String auditEventTypeName) {
		this.auditEventTypeCd = auditEventTypeCd;
		this.auditEventTypeName = auditEventTypeName;
	}

	/** full constructor */
	public AuditEventType(Integer auditEventTypeCd, String auditEventTypeName, String auditEventTypeDescription) {
		this.auditEventTypeCd = auditEventTypeCd;
		this.auditEventTypeName = auditEventTypeName;
		this.auditEventTypeDescription = auditEventTypeDescription;
	}

	@Id
	@Column(name = "audit_event_type_cd", unique = true, nullable = false)
	public Integer getAuditEventTypeCd() {
		return this.auditEventTypeCd;
	}

	public void setAuditEventTypeCd(Integer auditEventTypeCd) {
		this.auditEventTypeCd = auditEventTypeCd;
	}

	@Column(name = "audit_event_type_name", nullable = false, length = 64)
	public String getAuditEventTypeName() {
		return this.auditEventTypeName;
	}

	public void setAuditEventTypeName(String auditEventTypeName) {
		this.auditEventTypeName = auditEventTypeName;
	}

	@Column(name = "audit_event_type_description")
	public String getAuditEventTypeDescription() {
		return this.auditEventTypeDescription;
	}

	public void setAuditEventTypeDescription(String auditEventTypeDescription) {
		this.auditEventTypeDescription = auditEventTypeDescription;
	}

	@Column(name = "audit_event_type_code", nullable = false, length = 64)
	public String getAuditEventTypeCode() {
		return auditEventTypeCode;
	}

	public void setAuditEventTypeCode(String auditEventTypeCode) {
		this.auditEventTypeCode = auditEventTypeCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AuditEventType))
			return false;
		AuditEventType castOther = (AuditEventType) other;
		return new EqualsBuilder().append(auditEventTypeCd, castOther.auditEventTypeCd)
				.append(auditEventTypeName, castOther.auditEventTypeName)
				.append(auditEventTypeDescription, castOther.auditEventTypeDescription)
				.append(auditEventTypeCode, castOther.auditEventTypeCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(auditEventTypeCd).append(auditEventTypeName).append(auditEventTypeDescription).append(
				auditEventTypeCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("auditEventTypeCd", auditEventTypeCd).append("auditEventTypeName", auditEventTypeName).append(
				"auditEventTypeDescription", auditEventTypeDescription).append("auditEventTypeCode", auditEventTypeCode).toString();
	}
}