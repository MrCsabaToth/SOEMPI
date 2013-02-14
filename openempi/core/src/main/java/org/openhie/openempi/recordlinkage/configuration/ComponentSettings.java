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
package org.openhie.openempi.recordlinkage.configuration;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class ComponentSettings extends BaseObject
{
	private static final long serialVersionUID = -8124971477792734094L;

	private KeyServerSettings keyServerSettings;
	private ParameterManagerSettings parameterManagerSettings;
	private DataIntegratorSettings dataIntegratorSettings;
	
	public ComponentSettings() {
	}

	public KeyServerSettings getKeyServerSettings() {
		return keyServerSettings;
	}

	public void setKeyServerSettings(KeyServerSettings keyServerSettings) {
		this.keyServerSettings = keyServerSettings;
	}

	public ParameterManagerSettings getParameterManagerSettings() {
		return parameterManagerSettings;
	}

	public void setParameterManagerSettings(ParameterManagerSettings parameterManagerSettings) {
		this.parameterManagerSettings = parameterManagerSettings;
	}

	public DataIntegratorSettings getDataIntegratorSettings() {
		return dataIntegratorSettings;
	}

	public void setDataIntegratorSettings(DataIntegratorSettings dataIntegratorSettings) {
		this.dataIntegratorSettings = dataIntegratorSettings;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
			append("keyServerSettings", keyServerSettings).
			append("parameterManagerSettings", parameterManagerSettings).
			append("dataIntegratorSettings", dataIntegratorSettings).
			toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ComponentSettings))
			return false;
		ComponentSettings castOther = (ComponentSettings) other;
		return new EqualsBuilder().
				append(keyServerSettings, castOther.keyServerSettings).
				append(parameterManagerSettings, castOther.parameterManagerSettings).
				append(dataIntegratorSettings, castOther.dataIntegratorSettings).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(keyServerSettings).
				append(parameterManagerSettings).
				append(dataIntegratorSettings).
				toHashCode();
	}
}
