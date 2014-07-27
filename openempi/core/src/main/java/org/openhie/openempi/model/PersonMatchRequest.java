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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PersonLink entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "person_match_request", schema = "public")
@SequenceGenerator(name="person_match_request_seq", sequenceName="public.person_match_request_seq")
public class PersonMatchRequest extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -197987665999121559L;

	private Integer personMatchRequestId;
	private Dataset dataset;
	private String matchName;
	private String blockingServiceName;
	private String matchingServiceName;
	private Integer dhSecret;
	private byte[] dhPublicKey;
	private String matchPairStatHalfTableName;
	private Integer personMatchId;	// TODO: replace with PersonMatch instead of id?
	private Boolean completed;
	private User userCreatedBy;
	private Date dateCreated;

	/** default constructor */
	public PersonMatchRequest() {
	}

	/** full constructor */
	public PersonMatchRequest(Integer personMatchRequestId, Dataset dataset, String matchName,
			String blockingServiceName, String matchingServiceName, byte[] dhPublicKey, String matchPairStatHalfTableName,
			Integer personMatchId, Boolean completed, User userCreatedBy, Date dateCreated) {
		this.personMatchRequestId = personMatchRequestId;
		this.dataset = dataset;
		this.matchName = matchName;
		this.blockingServiceName = blockingServiceName;
		this.matchingServiceName = matchingServiceName;
		this.dhPublicKey = dhPublicKey;
		this.matchPairStatHalfTableName = matchPairStatHalfTableName;
		this.personMatchId = personMatchId;
		this.completed = completed;
		this.userCreatedBy = userCreatedBy;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_match_request_seq") 
	@Column(name = "person_match_request_id", unique = true, nullable = false)
	public Integer getPersonMatchRequestId() {
		return this.personMatchRequestId;
	}

	public void setPersonMatchRequestId(Integer personMatchRequestId) {
		this.personMatchRequestId = personMatchRequestId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dataset_id", nullable = false)
	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Column(name = "match_name", nullable = false)
	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	@Column(name = "blocking_service_name", nullable = false)
	public String getBlockingServiceName() {
		return blockingServiceName;
	}

	public void setBlockingServiceName(String blockingServiceName) {
		this.blockingServiceName = blockingServiceName;
	}

	@Column(name = "matching_service_name", nullable = false)
	public String getMatchingServiceName() {
		return matchingServiceName;
	}

	public void setMatchingServiceName(String matchingServiceName) {
		this.matchingServiceName = matchingServiceName;
	}

	@Column(name = "dh_secret")
	public Integer getDhSecret() {
		return dhSecret;
	}

	public void setDhSecret(Integer dhSecret) {
		this.dhSecret = dhSecret;
	}

	@Column(name = "dh_public_key")
	public byte[] getDhPublicKey() {
		return dhPublicKey;
	}

	public void setDhPublicKey(byte[] dhPublicKey) {
		this.dhPublicKey = dhPublicKey;
	}

	@Column(name = "match_pair_stat_half_table_name")
	public String getMatchPairStatHalfTableName() {
		return matchPairStatHalfTableName;
	}

	public void setMatchPairStatHalfTableName(String matchPairStatHalfTableName) {
		this.matchPairStatHalfTableName = matchPairStatHalfTableName;
	}

	@Column(name = "person_match_id")
	public Integer getPersonMatchId() {
		return personMatchId;
	}

	public void setPersonMatchId(Integer personMatchId) {
		this.personMatchId = personMatchId;
	}

	public void setPerson_match_id(Integer personMatchId) {
		setPersonMatchId(personMatchId);
	}

	@Column(name = "completed", nullable = false)
	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id"/*, nullable = false*/)
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User userByCreatorId) {
		this.userCreatedBy = userByCreatorId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PersonMatchRequest))
			return false;
		PersonMatchRequest castOther = (PersonMatchRequest) other;
		return new EqualsBuilder().append(personMatchRequestId, castOther.personMatchRequestId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(personMatchRequestId).append(dataset.getDatasetId()).append(dhSecret)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
			append("personMatchRequestId", personMatchRequestId).
			append("dataset", dataset.getDatasetId()).
			append("matchName", matchName).
			append("blockingServiceName", blockingServiceName).
			append("matchingServiceName", matchingServiceName).
			append("personMatchId", personMatchId).
			append("matchPairStatHalfTableName", matchPairStatHalfTableName).
			append("dhSecret", dhSecret).
			append("dhPublicKey", dhPublicKey).
			append("completed", completed).
			append("creatorId", userCreatedBy).
			append("dateCreated", dateCreated).
			toString();
	}

}