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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Salt entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Yimin Xie</a>
 */
@Entity
@Table(name = "salt")
@SequenceGenerator(name="salt_seq", sequenceName="public.salt_seq")
public class Salt extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -5340725356211813687L;

	private Long id;
	@Lob
	private byte[] salt;
	private Date dateCreated;
	private User userCreatedBy;
	private Date dateVoided;
	private User userVoidedBy;

	/** default constructor */
	public Salt() {
	}

	/** minimal constructor */
	public Salt(Long id, Date dateCreated, User userByCreatorId, User userByVoidedById) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.userVoidedBy = userByVoidedById;
	}

	/** full constructor */
	public Salt(Long id, byte[] salt, Date dateCreated, User userByCreatorId,
			Date dateVoided, User userByVoidedById) {
		this.id = id;
		this.salt = salt;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.dateVoided = dateVoided;
		this.userVoidedBy = userByVoidedById;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="salt_seq") 
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id"/*, nullable = false*/)
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User userByCreatorId) {
		this.userCreatedBy = userByCreatorId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided", length = 8)
	public Date getDateVoided() {
		return this.dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voided_by_id")
	public User getUserVoidedBy() {
		return this.userVoidedBy;
	}

	public void setUserVoidedBy(User userByVoidedById) {
		this.userVoidedBy = userByVoidedById;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Salt))
			return false;
		Salt castOther = (Salt) other;
		return new EqualsBuilder().append(id, castOther.id).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).toString();
	}

	public String toStringLong() {
		return new ToStringBuilder(this).append("id", id).
						append("salt", salt).
						append("dateCreated", dateCreated).
						append("userCreatedBy", userCreatedBy).
						append("dateVoided", dateVoided).
						append("userVoidedBy", userVoidedBy).toString();
	}

}