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
 * IdentifierDomain entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "user_session")
@SequenceGenerator(name="user_session_seq", sequenceName="public.user_session_seq")
public class UserSession implements Serializable
{
	private static final long serialVersionUID = 2611151383140968220L;

	private Integer sessionId;
	private String sessionKey;
	private User user;
	private Date dateCreated;
	
	/** default constructor */	
	public UserSession() {
	}

	/** Common constructor */
	/** full constructor */
	public UserSession(String sessionKey, User user, Date dateCreated) {
		this.sessionKey = sessionKey;
		this.user = user;
		this.dateCreated = dateCreated;
	}
	
	/** full constructor */
	public UserSession(Integer sessionId, String sessionKey, User user, Date dateCreated) {
		this.sessionId = sessionId;
		this.sessionKey = sessionKey;
		this.user = user;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_session_seq") 	
	@Column(name = "session_id", unique = true, nullable = false)
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "session_key")
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof UserSession))
			return false;
		UserSession castOther = (UserSession) other;
		return new EqualsBuilder().append(sessionKey, castOther.sessionKey).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(sessionKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("sessionId", sessionId).append("sessionKey", sessionKey).append("user",
				user).append("dateCreated", dateCreated).toString();
	}
}
