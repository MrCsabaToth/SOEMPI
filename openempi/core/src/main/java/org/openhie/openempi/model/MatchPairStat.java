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
@Table(name = "match_pair_stat", schema = "public")
@SequenceGenerator(name="match_pair_stat_seq", sequenceName="public.match_pair_stat_seq")
public class MatchPairStat implements Serializable
{
	private static final long serialVersionUID = -7772687797682287294L;

	private Long matchPairStatId;
	private long leftPersonPseudoId;
	private long rightPersonPseudoId;
	private boolean matchStatus;

	/** default constructor */
	public MatchPairStat() {
	}

	/** full constructor */
	public MatchPairStat(long leftPersonPseudoId, long rightPersonPseudoId, boolean matchStatus) {
		this.leftPersonPseudoId = leftPersonPseudoId;
		this.rightPersonPseudoId = rightPersonPseudoId;
		this.matchStatus = matchStatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="match_pair_stat_seq") 
	@Column(name = "match_pair_stat_id", unique = true, nullable = false)
	public Long getMatchPairStatId() {
		return matchPairStatId;
	}

	public void setMatchPairStatId(Long matchPairStatId) {
		this.matchPairStatId = matchPairStatId;
	}

	public void setMatch_pair_stat_id(java.math.BigInteger matchPairStatId) {
		setMatchPairStatId(matchPairStatId.longValue());
	}

	@Column(name = "left_person_pseudo_id", unique = true, nullable = false)
	public long getLeftPersonPseudoId() {
		return leftPersonPseudoId;
	}

	public void setLeftPersonPseudoId(long leftPersonPseudoId) {
		this.leftPersonPseudoId = leftPersonPseudoId;
	}

	public void setLeft_person_pseudo_id(java.math.BigInteger leftPersonPseudoId) {
		setLeftPersonPseudoId(leftPersonPseudoId.longValue());
	}

	@Column(name = "right_person_pseudo_id", unique = true, nullable = false)
	public long getRightPersonPseudoId() {
		return rightPersonPseudoId;
	}

	public void setRightPersonPseudoId(long rightPersonPseudoId) {
		this.rightPersonPseudoId = rightPersonPseudoId;
	}

	public void setRight_person_pseudo_id(java.math.BigInteger rightPersonPseudoId) {
		setRightPersonPseudoId(rightPersonPseudoId.longValue());
	}

	@Column(name = "match_status", nullable = false)
	public boolean getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(boolean matchStatus) {
		this.matchStatus = matchStatus;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof MatchPairStat))
			return false;
		MatchPairStat castOther = (MatchPairStat) other;
		return new EqualsBuilder()
			.append(getMatchPairStatId(), castOther.getMatchPairStatId())
			.append(getLeftPersonPseudoId(), castOther.getLeftPersonPseudoId())
			.append(getRightPersonPseudoId(), castOther.getRightPersonPseudoId())
			.append(getMatchStatus(), castOther.getMatchStatus())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMatchPairStatId())
			.append(getLeftPersonPseudoId())
			.append(getRightPersonPseudoId())
			.append(getMatchStatus())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("matchPairStatId", matchPairStatId)
			.append("leftPersonPseudoId", leftPersonPseudoId)
			.append("rightPersonPseudoId", rightPersonPseudoId)
			.append("matchStatus", matchStatus)
			.toString();
	}
	
}
