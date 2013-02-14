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

public class PrivacySettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = -4298070980233391645L;

	public static final String COMPONENT_SETTINGS = "componentSettings";
	public static final String BLOOMFILTER_SETTINGS = "bloomfilterSettings";

	public ComponentSettingsWeb dummyComponentSettings;	// This is need so ComponentSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public BloomfilterSettingsWeb dummyBloomfilterSettings;	// This is need so ComponentSettingsWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public PrivacySettingsWeb() {
	}

	public ComponentSettingsWeb getComponentSettings() {
		return get(COMPONENT_SETTINGS);
	}

	public void setComponentSettings(ComponentSettingsWeb componentSettings) {
		set(COMPONENT_SETTINGS, componentSettings);
	}

	public BloomfilterSettingsWeb getBloomfilterSettings() {
		return get(BLOOMFILTER_SETTINGS);
	}

	public void setBloomfilterSettings(BloomfilterSettingsWeb bloomfilterSettings) {
		set(BLOOMFILTER_SETTINGS, bloomfilterSettings);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(COMPONENT_SETTINGS + ": ").append(getComponentSettings().toString());
		sb.append("," + BLOOMFILTER_SETTINGS + ": ").append(getBloomfilterSettings().toString());
		return sb.toString();
	}
}
