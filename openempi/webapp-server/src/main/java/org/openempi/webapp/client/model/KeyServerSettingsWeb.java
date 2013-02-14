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

public class KeyServerSettingsWeb extends ComponentSettingBaseWeb implements Serializable
{
	private static final long serialVersionUID = 6187284220083406680L;

	public static final String NUMBER_OF_SALT = "numberOfSalts";
	public static final String SALT_ID_START = "saltIdStart";
	public static final String SALT_ID_STRIDE = "saltIdStride";

	public KeyServerSettingsWeb() {
	}

	public Integer getNumberOfSalts() {
		return get(NUMBER_OF_SALT);
	}

	public void setNumberOfSalts(Integer numberOfSalts) {
		set(NUMBER_OF_SALT, numberOfSalts);
	}

	public Integer getSaltIdStart() {
		return get(SALT_ID_START);
	}

	public void setSaltIdStart(Integer saltIdStart) {
		set(SALT_ID_START, saltIdStart);
	}

	public Integer getSaltIdStride() {
		return get(SALT_ID_STRIDE);
	}

	public void setSaltIdStride(Integer saltIdStride) {
		set(SALT_ID_STRIDE, saltIdStride);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + NUMBER_OF_SALT + ": ").append(getNumberOfSalts());
		sb.append("," + SALT_ID_START + ": ").append(getSaltIdStart());
		sb.append("," + SALT_ID_STRIDE + ": ").append(getSaltIdStride());
		return sb.toString();
	}
}
