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
package org.openempi.webapp.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PersonLinkWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 8075579955061626675L;

	public static final String PERSON_LINK_ID = "personLinkId";
	public static final String PERSON_MATCH_ID = "personMatchId";
	public static final String LEFT_PERSON_ID = "leftPersonId";
	public static final String RIGHT_PERSON_ID = "rightPersonId";
	public static final String BINARY_VECTOR = "binaryVector";
	public static final String CONTINOUS_VECTOR = "continousVector";
	public static final String WEIGHT = "weight";
	public static final String LINK_STATE = "linkState";
	public static final String DATE_CREATED = "dateCreated";

	public static final String SHOW_LEFT_ATTRIBUTES_BUTTON = "showLeftAttributesButton";
	public static final String SHOW_RIGHT_ATTRIBUTES_BUTTON = "showRightAttributesButton";

	public PersonLinkWeb() {
		setShowLeftAttributesButton("SL");
		setShowRightAttributesButton("SR");
	}

	public Long getPersonLinkId() {
		return get(PERSON_LINK_ID);
	}

	public void setPersonLinkId(Long personLinkId) {
		set(PERSON_LINK_ID, personLinkId);
	}

	public Integer getPersonMatchId() {
		return get(PERSON_MATCH_ID);
	}

	public void setPersonMatchId(Integer personMatchId) {
		set(PERSON_MATCH_ID, personMatchId);
	}

	public Long getLeftPersonId() {
		return get(LEFT_PERSON_ID);
	}

	public void setLeftPersonId(Long leftPersonId) {
		set(LEFT_PERSON_ID, leftPersonId);
	}

	public Long getRightPersonId() {
		return get(RIGHT_PERSON_ID);
	}

	public void setRightPersonId(Long rightPersonId) {
		set(RIGHT_PERSON_ID, rightPersonId);
	}

	public String getBinaryVector() {
		return get(BINARY_VECTOR);
	}

	public void setBinaryVector(String binaryVector) {
		set(BINARY_VECTOR, binaryVector);
	}
	
	public String getContinousVector() {
		return get(CONTINOUS_VECTOR);
	}

	public void setContinousVector(String continousVector) {
		set(CONTINOUS_VECTOR, continousVector);
	}
	
	public Double getWeight() {
		return get(WEIGHT);
	}

	public void setWeight(Double weight) {
		set(WEIGHT, weight);
	}

	public Integer getLinkState() {
		return get(LINK_STATE);
	}

	public void setLinkState(Integer linkState) {
		set(LINK_STATE, linkState);
	}

	public java.util.Date getDateCreated() {
		return get(DATE_CREATED);
	}

	public void setDateCreated(java.util.Date dateCreated) {
		set(DATE_CREATED, dateCreated);
	}

	public String getShowLeftAttributesButton() {
		return get(SHOW_LEFT_ATTRIBUTES_BUTTON);
	}

	public void setShowLeftAttributesButton(String showLeftAttributesButton) {
		set(SHOW_LEFT_ATTRIBUTES_BUTTON, showLeftAttributesButton);
	}
	
	public String getShowRightAttributesButton() {
		return get(SHOW_RIGHT_ATTRIBUTES_BUTTON);
	}

	public void setShowRightAttributesButton(String showRightAttributesButton) {
		set(SHOW_RIGHT_ATTRIBUTES_BUTTON, showRightAttributesButton);
	}
	
}
