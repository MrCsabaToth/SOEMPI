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

public class ComponentSettingBaseWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 8825550715885122248L;

	public static final String SERVER_ADDRESS = "serverAddress";

	public ComponentSettingBaseWeb() {
	}

	public String getServerAddress() {
		return get(SERVER_ADDRESS);
	}

	public void setServerAddress(String serverAddress) {
		set(SERVER_ADDRESS, serverAddress);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(SERVER_ADDRESS + ": ").append(getServerAddress());
		return sb.toString();
	}
}
