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

public class ComponentSettingBase extends BaseObject
{
	private static final long serialVersionUID = -2855769400385589052L;

	private String serverAddress;

	public ComponentSettingBase() {
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String ipAddress) {
		this.serverAddress = ipAddress;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("ipAddress", serverAddress).
				toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ComponentSettingBase))
			return false;
		ComponentSettingBase castOther = (ComponentSettingBase) other;
		return new EqualsBuilder().
				append(serverAddress, castOther.serverAddress).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(serverAddress).
				toHashCode();
	}
}
