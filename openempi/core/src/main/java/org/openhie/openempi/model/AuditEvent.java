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

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * AuditEvent entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "audit_event")
@SequenceGenerator(name="audit_event_seq", sequenceName="public.audit_event_seq")
public class AuditEvent extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -6061320465621019356L;

	private Integer auditEventId;
	private Date dateCreated;
	private AuditEventType auditEventType;
	private String auditEventDescription;
	private Person refPerson;
	private Person altRefPerson;
	private Key refKey;
	private Salt refSalt;
	private User userCreatedBy;

	/** default constructor */
	public AuditEvent() {
	}

	public AuditEvent(Date dateCreated, AuditEventType auditEventType, String auditEventDescription, User userCreatedBy) {
		super();
		this.dateCreated = dateCreated;
		this.auditEventType = auditEventType;
		this.auditEventDescription = auditEventDescription;
		this.userCreatedBy = userCreatedBy;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="audit_event_seq") 
	@Column(name = "audit_event_id", unique = true, nullable = false)
	public Integer getAuditEventId() {
		return auditEventId;
	}

	public void setAuditEventId(Integer auditEventId) {
		this.auditEventId = auditEventId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_event_type_cd")
	public AuditEventType getAuditEventType() {
		return auditEventType;
	}

	public void setAuditEventType(AuditEventType auditEventType) {
		this.auditEventType = auditEventType;
	}

	@Column(name = "audit_event_description", length = 255)
	public String getAuditEventDescription() {
		return auditEventDescription;
	}

	public void setAuditEventDescription(String auditEventDescription) {
		this.auditEventDescription = auditEventDescription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_person_id", nullable = true)
	public Person getRefPerson() {
		return refPerson;
	}

	public void setRefPerson(Person refPerson) {
		this.refPerson = refPerson;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alt_ref_person_id", nullable = true)
	public Person getAltRefPerson() {
		return altRefPerson;
	}

	public void setAltRefPerson(Person altRefPerson) {
		this.altRefPerson = altRefPerson;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_key_id", nullable = true)
	public Key getRefKey() {
		return refKey;
	}

	public void setRefKey(Key refKey) {
		this.refKey = refKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_salt_id", nullable = true)
	public Salt getRefSalt() {
		return refSalt;
	}

	public void setRefSalt(Salt refSalt) {
		this.refSalt = refSalt;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = false)
	public User getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(User userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AuditEvent))
			return false;
		AuditEvent castOther = (AuditEvent) other;
		return new EqualsBuilder().append(auditEventId, castOther.auditEventId).
				append(dateCreated, castOther.dateCreated).
				append(auditEventType, castOther.auditEventType).
				append(auditEventDescription, castOther.auditEventDescription).
				append(refPerson, castOther.refPerson).
				append(altRefPerson, castOther.altRefPerson).
				append(refKey, castOther.refKey).
				append(refSalt, castOther.refSalt).
				append(userCreatedBy, castOther.userCreatedBy).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(auditEventId).
				append(dateCreated).
				append(auditEventType).
				append(auditEventDescription).
				append(refPerson).
				append(altRefPerson).
				append(refKey).
				append(refSalt).
				append(userCreatedBy).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("auditEventId", auditEventId).
				append("dateCreated", dateCreated).
				append("auditEventType", auditEventType).
				append("auditEventDescription", auditEventDescription).
				append("refPerson", refPerson).
				append("altRefPerson", altRefPerson).
				append("refKey", refKey).
				append("refSalt", refSalt).
				append("userCreatedBy", userCreatedBy).toString();
	}

}
