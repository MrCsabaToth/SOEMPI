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

public class ComponentSettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = -3755943105620353375L;

	public static final String KEY_SERVER_SETTINGS = "keyServerSettings";
	public static final String PARAMETER_MANAGER_SETTINGS = "parameterManagerSettings";
	public static final String DATA_INTEGRATOR_SETTINGS = "dataIntegratorSettings";

	public KeyServerSettingsWeb dummyKeyServerSettings;	// This is need so KeyServerSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public ParameterManagerSettingsWeb dummyParameterManagerSettings;	// This is need so ParameterManagerSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public DataIntegratorSettingsWeb dummyDataIntegratorSettings;	// This is need so DataIntegratorSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public ComponentSettingsWeb() {
	}

	public ParameterManagerSettingsWeb getParameterManagerSettings() {
		return get(PARAMETER_MANAGER_SETTINGS);
	}

	public void setParameterManagerSettings(ParameterManagerSettingsWeb parameterManagerSettings) {
		set(PARAMETER_MANAGER_SETTINGS, parameterManagerSettings);
	}

	public KeyServerSettingsWeb getKeyServerSettings() {
		return get(KEY_SERVER_SETTINGS);
	}

	public void setKeyServerSettings(KeyServerSettingsWeb keyServerSettings) {
		set(KEY_SERVER_SETTINGS, keyServerSettings);
	}

	public DataIntegratorSettingsWeb getDataIntegratorSettings() {
		return get(DATA_INTEGRATOR_SETTINGS);
	}

	public void setDataIntegratorSettings(DataIntegratorSettingsWeb dataIntegratorSettings) {
		set(DATA_INTEGRATOR_SETTINGS, dataIntegratorSettings);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(KEY_SERVER_SETTINGS + ": ").append(getKeyServerSettings().toString());
		sb.append("," + PARAMETER_MANAGER_SETTINGS + ": ").append(getParameterManagerSettings().toString());
		sb.append("," + DATA_INTEGRATOR_SETTINGS + ": ").append(getDataIntegratorSettings().toString());
		return sb.toString();
	}
}
