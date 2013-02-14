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
package org.openempi.webapp.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LinkedPersonWeb extends BaseModelData implements Serializable
{
	public LinkedPersonWeb() {
		
	}
	
	public java.lang.String getAddress1() {
		return get("address1");
	}

	public void setAddress1(java.lang.String address1) {
		set("address1", address1);
	}
	
	public java.lang.String getCity() {
		return get("city");
	}

	public void setCity(java.lang.String city) {
		set("city", city);
	}

	public java.util.Date getDateOfBirth() {
		return get("dateOfBirth");
	}

	public void setDateOfBirth(java.util.Date dateOfBirth) {
		set("dateOfBirth", dateOfBirth);
	}

	public java.lang.String getFamilyName() {
		return get("familyName");
	}

	public void setFamilyName(java.lang.String familyName) {
		set("familyName", familyName);
	}
	
	public java.lang.String getGivenName() {
		return get("givenName");
	}

	public void setGivenName(java.lang.String givenName) {
		set("givenName", givenName);
	}
	
	public java.lang.String getPostalCode() {
		return get("postalCode");
	}

	public void setPostalCode(java.lang.String postalCode) {
		set("postalCode", postalCode);
	}
	
	public java.lang.String getState() {
		return get("state");
	}

	public void setState(java.lang.String state) {
		set("state", state);
	}
}
