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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;

/**
 * PersonMatch entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "person_match", schema = "public")
@SequenceGenerator(name="person_match_seq", sequenceName="public.person_match_seq")
public class PersonMatch extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 7422344246867431143L;

	private Integer personMatchId;
	private String matchTitle;
	private Dataset leftDataset;
	private Dataset rightDataset;
	@Lob
	private MatchConfiguration matchConfiguration;
	@Lob
	private FellegiSunterParameters matchFellegiSunter;
	@Lob
	private FellegiSunterParameters blockFellegiSunter;
	private List<ColumnMatchInformation> columnMatchInformation;
	private Long totalRecords;
	private Integer bloomFilterKParameter;
	private Double bloomFilterFillFactor;
	private User userCreatedBy;
	private Date dateCreated;

	/** default constructor */
	public PersonMatch() {
	}

	/** full constructor */
	public PersonMatch(Integer personMatchId, Dataset leftDataset, Dataset rightDataset, User userCreatedBy,
			Date dateCreated) {
		this.personMatchId = personMatchId;
		this.leftDataset = leftDataset;
		this.rightDataset = rightDataset;
		this.userCreatedBy = userCreatedBy;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_match_seq") 
	@Column(name = "person_match_id", unique = true, nullable = false)
	public Integer getPersonMatchId() {
		return this.personMatchId;
	}

	public void setPersonMatchId(Integer personMatchId) {
		this.personMatchId = personMatchId;
	}

	@Column(name = "match_title", nullable = false)
	public String getMatchTitle() {
		return matchTitle;
	}

	public void setMatchTitle(String matchTitle) {
		this.matchTitle = matchTitle;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "left_dataset_id", nullable = false)
	public Dataset getLeftDataset() {
		return this.leftDataset;
	}

	public void setLeftDataset(Dataset leftDataset) {
		this.leftDataset = leftDataset;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "right_dataset_id"/*, nullable = false*/)
	public Dataset getRightDataset() {
		return this.rightDataset;
	}

	public void setRightDataset(Dataset rightDataset) {
		this.rightDataset = rightDataset;
	}

	public MatchConfiguration getMatchConfiguration() {
		return matchConfiguration;
	}

	public void setMatchConfiguration(MatchConfiguration matchConfiguration) {
		this.matchConfiguration = matchConfiguration;
	}

	public FellegiSunterParameters getMatchFellegiSunter() {
		return matchFellegiSunter;
	}

	public void setMatchFellegiSunter(FellegiSunterParameters matchFellegiSunter) {
		this.matchFellegiSunter = matchFellegiSunter;
	}

	public FellegiSunterParameters getBlockFellegiSunter() {
		return blockFellegiSunter;
	}

	public void setBlockFellegiSunter(FellegiSunterParameters blockFellegiSunter) {
		this.blockFellegiSunter = blockFellegiSunter;
	}

	@Column(name = "total_records")
	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Column(name = "bf_k_parameter")
	public Integer getBloomFilterKParameter() {
		return bloomFilterKParameter;
	}

	public void setBloomFilterKParameter(Integer bloomFilterKParameter) {
		this.bloomFilterKParameter = bloomFilterKParameter;
	}

	@Column(name = "bf_fill_factor")
	public Double getBloomFilterFillFactor() {
		return bloomFilterFillFactor;
	}

	public void setBloomFilterFillFactor(Double bloomFilterFillFactor) {
		this.bloomFilterFillFactor = bloomFilterFillFactor;
	}

	@OneToMany(mappedBy="personMatch", cascade=CascadeType.ALL)
	@OrderBy("columnMatchInformationId")
	public List<ColumnMatchInformation> getColumnMatchInformation() {
		return columnMatchInformation;
	}

	public void setColumnMatchInformation(List<ColumnMatchInformation> columnMatchInformation) {
		this.columnMatchInformation = columnMatchInformation;
	}

	@Transient
	public void referenceThisInChildEntities() {
		if (columnMatchInformation != null) {
			if (columnMatchInformation.size() > 0) {
				for (ColumnMatchInformation cmi : columnMatchInformation) {
					cmi.setPersonMatch(this);
					cmi.hydrateAttributes();
				}
			}
		}
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id", nullable = false)
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
		if (!(other instanceof PersonMatch))
			return false;
		PersonMatch castOther = (PersonMatch) other;
		return new EqualsBuilder().append(personMatchId, castOther.personMatchId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(personMatchId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
			append("personMatchId", personMatchId).
			append("leftDatasetId", leftDataset.getDatasetId()).
			append("rightDatasetId", rightDataset.getDatasetId()).
			append("creatorId", userCreatedBy).
			append("dateCreated", dateCreated).
			toString();
	}

}