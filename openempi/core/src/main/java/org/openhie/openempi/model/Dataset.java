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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Dataset entity.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
@Entity
@Table(name = "dataset")
@SequenceGenerator(name="dataset_seq", sequenceName="public.dataset_seq")
public class Dataset extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 6170253797278224912L;

	private Integer datasetId;
	private User owner;
	private String tableName;
	private String fileName;
	private List<ColumnInformation> columnInformation;
	private Long totalRecords;
	private String imported;
	private Date dateCreated;
	
	public Dataset() {
		this.totalRecords = 0L;
	}

	public Dataset(String tableName, String fileName) {
		this.tableName = tableName;
		this.fileName = fileName;
		this.totalRecords = 0L;
		this.imported = "N";
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dataset_seq") 
	@Column(name = "dataset_id", unique = true, nullable = false)
	public Integer getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Integer datasetId) {
		this.datasetId = datasetId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Column(name = "table_name", nullable = false)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "file_name", nullable = false)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@OneToMany(mappedBy="dataset", cascade=CascadeType.ALL)
	@JoinColumn(name="dataset_id")
	public List<ColumnInformation> getColumnInformation() {
		return columnInformation;
	}

	public void setColumnInformation(List<ColumnInformation> columnInformation) {
		this.columnInformation = columnInformation;
	}

	@Transient
	public void referenceThisInChildEntities() {
		if (columnInformation != null) {
			if (columnInformation.size() > 0) {
				for (ColumnInformation ci : columnInformation) {
					ci.setDataset(this);
					ci.hydrateAttributes();
				}
			}
		}
	}

	@Column(name = "total_records")
	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Column(name = "imported_ind", nullable = false)
	public String getImported() {
		return imported;
	}

	public void setImported(String imported) {
		this.imported = imported;
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
		if (!(other instanceof Dataset))
			return false;
		Dataset castOther = (Dataset) other;
		return new EqualsBuilder().
				append(datasetId, castOther.datasetId).
				append(owner, castOther.owner).
				append(tableName, castOther.tableName).
				append(fileName, castOther.fileName).
				append(totalRecords, castOther.totalRecords).
				append(imported, castOther.imported).
				append(dateCreated, castOther.dateCreated).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(datasetId).
				append(owner).
				append(tableName).
				append(fileName).
				append(totalRecords).
				append(imported).
				append(dateCreated).
				toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("datasetId", datasetId).
				append("owner", owner).
				append("tableName", tableName).
				append("fileName", fileName).
				append("totalRecords", totalRecords).
				append("imported", imported).
				append("dateCreated", dateCreated).
				toString();
	}
}
