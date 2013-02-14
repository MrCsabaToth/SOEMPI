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
 * Key entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "key")
@SequenceGenerator(name="key_seq", sequenceName="public.key_seq")
public class Key extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -6061320465621019356L;

	private Long id;
	@Lob
	private byte[] publicKeyPart1;
	@Lob
	private byte[] publicKeyPart2;
	@Lob
	private byte[] publicKeyPart3;
	@Lob
	private byte[] privateKeyPart1;
	@Lob
	private byte[] privateKeyPart2;
	@Lob
	private byte[] privateKeyPart3;
	private Date dateCreated;
	private User userCreatedBy;
	private Date dateVoided;
	private User userVoidedBy;

	/** default constructor */
	public Key() {
	}

	/** minimal constructor */
	public Key(Long id, Date dateCreated, User userByCreatorId, User userByVoidedById) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.userVoidedBy = userByVoidedById;
	}

	/** full constructor */
	public Key(Long id, byte[] publicKeyPart1, byte[] publicKeyPart2,
			byte[] publicKeyPart3, byte[] privateKeyPart1,
			byte[] privateKeyPart2, byte[] privateKeyPart3,
			Date dateCreated, User userByCreatorId, Date dateVoided, User userByVoidedById) {
		this.id = id;
		this.publicKeyPart1 = publicKeyPart1;
		this.publicKeyPart2 = publicKeyPart2;
		this.publicKeyPart3 = publicKeyPart3;
		this.privateKeyPart1 = privateKeyPart1;
		this.privateKeyPart2 = privateKeyPart2;
		this.privateKeyPart3 = privateKeyPart3;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.dateVoided = dateVoided;
		this.userVoidedBy = userByVoidedById;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="key_seq") 
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
	@JoinColumn(name = "creator_id", nullable = false)
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

	public byte[] getPublicKeyPart1() {
		return publicKeyPart1;
	}

	public void setPublicKeyPart1(byte[] publicKeyPart1) {
		this.publicKeyPart1 = publicKeyPart1;
	}

	public byte[] getPublicKeyPart2() {
		return publicKeyPart2;
	}

	public void setPublicKeyPart2(byte[] publicKeyPart2) {
		this.publicKeyPart2 = publicKeyPart2;
	}

	public byte[] getPublicKeyPart3() {
		return publicKeyPart3;
	}

	public void setPublicKeyPart3(byte[] publicKeyPart3) {
		this.publicKeyPart3 = publicKeyPart3;
	}

	public byte[] getPrivateKeyPart1() {
		return privateKeyPart1;
	}

	public void setPrivateKeyPart1(byte[] privateKeyPart1) {
		this.privateKeyPart1 = privateKeyPart1;
	}

	public byte[] getPrivateKeyPart2() {
		return privateKeyPart2;
	}

	public void setPrivateKeyPart2(byte[] privateKeyPart2) {
		this.privateKeyPart2 = privateKeyPart2;
	}

	public byte[] getPrivateKeyPart3() {
		return privateKeyPart3;
	}

	public void setPrivateKeyPart3(byte[] privateKeyPart3) {
		this.privateKeyPart3 = privateKeyPart3;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Key))
			return false;
		Key castOther = (Key) other;
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
						append("publicKeyPart1", publicKeyPart1).
						append("publicKeyPart2", publicKeyPart2).
						append("publicKeyPart3", publicKeyPart3).
						append("privateKeyPart1", privateKeyPart1).
						append("privateKeyPart2", privateKeyPart2).
						append("privateKeyPart3", privateKeyPart3).
						append("dateCreated", dateCreated).
						append("userCreatedBy", userCreatedBy).
						append("dateVoided", dateVoided).
						append("userVoidedBy", userVoidedBy).toString();
	}

}
