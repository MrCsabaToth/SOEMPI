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
package org.openhie.openempi.configuration;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.BaseObject;

public class AdminConfiguration extends BaseObject
{
	private static final long serialVersionUID = 7957396772048650595L;

	public enum ComponentType {
		DATA_PROVIDER_MODE,
		DATA_INTEGRATOR_MODE,
		PARAMETER_MANAGER_MODE,	// implicit keyserver also
		KEYSERVER_MODE
	};

	private String fileRepositoryDirectory;
	private ComponentType componentMode;
	private boolean experimentalMode;

	public AdminConfiguration() {
	}

	public String getFileRepositoryDirectory() {
		return fileRepositoryDirectory;
	}

	public void setFileRepositoryDirectory(String fileRepsoitoryDirectory) {
		this.fileRepositoryDirectory = fileRepsoitoryDirectory;
	}

	public ComponentType getComponentMode() {
		return componentMode;
	}

	public void setComponentMode(ComponentType componentMode) {
		this.componentMode = componentMode;
	}

	public boolean getExperimentalMode() {
		return experimentalMode;
	}

	public void setExperimentalMode(boolean experimentalMode) {
		this.experimentalMode = experimentalMode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("configFileDirectory", fileRepositoryDirectory)
			.append("componentMode", componentMode)
			.append("experimentalMode", experimentalMode)
			.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof AdminConfiguration))
			return false;
		AdminConfiguration castOther = (AdminConfiguration) other;
		return new EqualsBuilder().
				append(fileRepositoryDirectory, castOther.fileRepositoryDirectory).
				append(componentMode, castOther.componentMode).
				append(experimentalMode, castOther.experimentalMode).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(fileRepositoryDirectory).
				append(componentMode).
				append(experimentalMode).
				toHashCode();
	}
}
