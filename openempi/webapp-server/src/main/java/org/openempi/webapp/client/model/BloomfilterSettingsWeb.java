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

public class BloomfilterSettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 3351705928855159045L;

	public static final String N_GRAM_SIZE = "nGramSize";
	public static final String DEFAULT_M = "defaultM";
	public static final String DEFAULT_K = "defaultK";

	public BloomfilterSettingsWeb() {
	}

	public Integer getNGramSize() {
		return get(N_GRAM_SIZE);
	}

	public void setNGramSize(Integer nGramSize) {
		set(N_GRAM_SIZE, nGramSize);
	}

	public Integer getDefaultM() {
		return get(DEFAULT_M);
	}

	public void setDefaultM(Integer defaultM) {
		set(DEFAULT_M, defaultM);
	}

	public Integer getDefaultK() {
		return get(DEFAULT_K);
	}

	public void setDefaultK(Integer defaultK) {
		set(DEFAULT_K, defaultK);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(N_GRAM_SIZE + ": ").append(getNGramSize());
		sb.append("," + DEFAULT_M + ": ").append(getDefaultM());
		sb.append("," + DEFAULT_K + ": ").append(getDefaultK());
		return sb.toString();
	}
}
