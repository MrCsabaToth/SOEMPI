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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Person entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba TOth</a>
 */
@Entity
@Table(name = "person")
@SequenceGenerator(name="person_seq", sequenceName="public.person_seq")
public class Person extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -224298034523376469L;

	private Long personId;
	// Various attributes
	private Map<String, Object> attributes;
	// Record management attributes
	private Date dateCreated;
	private User userCreatedBy;

	/** default constructor */
	public Person() {
		this.attributes = new HashMap<String, Object>();
	}

	/** minimal constructor */
	public Person(Long personId, User userByCreatorId, Date dateCreated) {
		this.personId = personId;
		this.userCreatedBy = userByCreatorId;
		this.dateCreated = dateCreated;
		this.attributes = new HashMap<String, Object>();
	}

	/** full constructor */
	public Person(Long personId,
			Map<String, Object> attributes,
			Date dateCreated,
			User userByCreatorId) {
		this.personId = personId;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.attributes = attributes;		
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_seq") 
	@Column(name = "person_id", unique = true, nullable = false)
	public Long getPersonId() {
		return this.personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Transient
	public String getStringAttribute(String originalIdFieldName) {
		if (originalIdFieldName == null)
			return null;
		if (originalIdFieldName.length() <= 0)
			return null;
		Object o = getAttribute(originalIdFieldName);
		if (o instanceof String)
			return (String)o;
		return o.toString();
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

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Person))
			return false;
		Person castOther = (Person) other;
		return new EqualsBuilder().append(personId, castOther.personId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(personId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("personId", personId).toString();
	}

	@Transient
	public String toStringLong() {
		StringBuilder builtString = new StringBuilder(new ToStringBuilder(this)
				.append("personId", personId)
				.append("dateCreated", dateCreated)
				.append("userCreatedBy", userCreatedBy)
				.toString());
		builtString.append("[[[customfields: ");
		Iterator<Entry<String, Object>> it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
			if (pairs.getValue() == null)
				builtString.append(pairs.getKey() + "=null");
			else
				builtString.append(pairs.getKey() + "=" + pairs.getValue().toString());
			if (it.hasNext())
				builtString.append(", ");
		}
		builtString.append("]]]");
		return builtString.toString();
	}

	@Transient
	public Boolean hasAttribute(String fieldName) {
		return attributes.containsKey(fieldName.toLowerCase());
	}

	@Transient
	public Object getAttribute(String fieldName) {
		Object obj = null;
		String lowerCaseFieldName = fieldName.toLowerCase();
		if (attributes.containsKey(lowerCaseFieldName))
			obj = attributes.get(lowerCaseFieldName);
		return obj;
	}

	@Transient
	public void setAttribute(String fieldName, Object fieldValue) {
		attributes.put(fieldName.toLowerCase(), fieldValue);
	}
	
	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}

}