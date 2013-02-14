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

import org.apache.commons.lang.builder.ToStringBuilder;

public class KeyServerSettings extends ComponentSettingBase
{
	private static final long serialVersionUID = -411341049463247176L;

	private int numberOfSalts;
	private int saltIdStart;
	private int saltIdStride;
	
	public KeyServerSettings() {
	}

	public int getNumberOfSalts() {
		return numberOfSalts;
	}

	public void setNumberOfSalts(int numberOfSalts) {
		this.numberOfSalts = numberOfSalts;
	}

	public int getSaltIdStart() {
		return saltIdStart;
	}

	public void setSaltIdStart(int saltIdStart) {
		this.saltIdStart = saltIdStart;
	}

	public int getSaltIdStride() {
		return saltIdStride;
	}

	public void setSaltIdStride(int saltIdStride) {
		this.saltIdStride = saltIdStride;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("numberOfSalts", numberOfSalts)
			.append("saltIdStart", saltIdStart)
			.append("saltIdStride", saltIdStride)
			.toString();
	}
}
