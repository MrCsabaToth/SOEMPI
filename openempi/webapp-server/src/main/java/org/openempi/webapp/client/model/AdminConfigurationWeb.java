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

public class AdminConfigurationWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 5969050715774751607L;

	public enum ComponentTypeWeb {
		DATA_PROVIDER_MODE,
		DATA_INTEGRATOR_MODE,
		PARAMETER_MANAGER_MODE,	// implicit keyserver also
		KEYSERVER_MODE
	};

	public static final String CONFIG_FILE_DIRECTORY = "configFileDirectory";
	public static final String COMPONENT_MODE = "componentMode";
	public static final String EXPERIMENTAL_MODE = "experimentalMode";

	public ComponentTypeWeb dummyComponentType;	// This is need so ComponentTypeWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public AdminConfigurationWeb() {
	}

	public String getConfigFileDirectory() {
		return get(CONFIG_FILE_DIRECTORY);
	}

	public void setConfigFileDirectory(String configFileDirectory) {
		set(CONFIG_FILE_DIRECTORY, configFileDirectory);
	}

	public ComponentTypeWeb getComponentMode() {
		return get(COMPONENT_MODE);
	}

	public void setComponentMode(ComponentTypeWeb componentMode) {
		set(COMPONENT_MODE, componentMode);
	}

	public Boolean getExperimentalMode() {
		return get(EXPERIMENTAL_MODE);
	}

	public void setExperimentalMode(Boolean experimentalMode) {
		set(EXPERIMENTAL_MODE, experimentalMode);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(CONFIG_FILE_DIRECTORY + ": ").append(getConfigFileDirectory());
		sb.append("," + COMPONENT_MODE + ": ").append(getComponentMode());
		sb.append("," + EXPERIMENTAL_MODE + ": ").append(getExperimentalMode());
		return sb.toString();
	}
}
