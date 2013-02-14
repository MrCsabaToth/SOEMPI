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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "match_pair_stat_half", schema = "public")
@SequenceGenerator(name="match_pair_stat_half_seq", sequenceName="public.match_pair_stat_half_seq")
public class MatchPairStatHalf implements Serializable
{
	private static final long serialVersionUID = -7772687797682287294L;

	private Long matchPairStatHalfId;
	private long personPseudoId;
	private boolean matchStatus;

	/** default constructor */
	public MatchPairStatHalf() {
	}

	public MatchPairStatHalf(long personPseudoId) {
		this.personPseudoId = personPseudoId;
		this.matchStatus = false;
	}

	/** full constructor */
	public MatchPairStatHalf(long personPseudoId, boolean matchStatus) {
		this.personPseudoId = personPseudoId;
		this.matchStatus = matchStatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="match_pair_stat_half_seq") 
	@Column(name = "match_pair_stat_half_id", unique = true, nullable = false)
	public Long getMatchPairStatHalfId() {
		return matchPairStatHalfId;
	}

	public void setMatchPairStatHalfId(Long matchPairStatHalfId) {
		this.matchPairStatHalfId = matchPairStatHalfId;
	}

	public void setMatch_pair_stat_half_id(java.math.BigInteger matchPairStatHalfId) {
		setMatchPairStatHalfId(matchPairStatHalfId.longValue());
	}

	@Column(name = "person_pseudo_id", nullable = false)
	public long getPersonPseudoId() {
		return personPseudoId;
	}

	public void setPersonPseudoId(long personPseudoId) {
		this.personPseudoId = personPseudoId;
	}

	public void setPerson_pseudo_id(java.math.BigInteger personPseudoId) {
		setPersonPseudoId(personPseudoId.longValue());
	}

	@Column(name = "person_match_status", nullable = false)
	public boolean getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(boolean matchStatus) {
		this.matchStatus = matchStatus;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof MatchPairStatHalf))
			return false;
		MatchPairStatHalf castOther = (MatchPairStatHalf) other;
		return new EqualsBuilder()
			.append(getMatchPairStatHalfId(), castOther.getMatchPairStatHalfId())
			.append(getPersonPseudoId(), castOther.getPersonPseudoId())
			.append(getMatchStatus(), castOther.getMatchStatus())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMatchPairStatHalfId())
			.append(getPersonPseudoId())
			.append(getMatchStatus())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("matchPairStatHalfId", matchPairStatHalfId)
			.append("personPseudoId", personPseudoId)
			.append("matchStatus", matchStatus)
			.toString();
	}
	
}
