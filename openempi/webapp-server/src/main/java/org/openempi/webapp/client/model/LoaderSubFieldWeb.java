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

public class LoaderSubFieldWeb extends LoaderTargetFieldWeb implements Serializable
{
	private static final long serialVersionUID = -7866803999749178918L;

	public static final String BEGIN_INDEX = "beginIndex";
	public static final String END_INDEX = "endIndex";

	public LoaderSubFieldWeb() {
	}

	public LoaderSubFieldWeb(String fieldName, Integer beginIndex, Integer endIndex) {
		set(FIELD_NAME, fieldName);
		set(BEGIN_INDEX, beginIndex);
		set(END_INDEX, endIndex);
	}
	
	public Integer getBeginIndex() {
		return get(BEGIN_INDEX);
	}

	public void setBeginIndex(Integer beginIndex) {
		set(BEGIN_INDEX, beginIndex);
	}

	public Integer getEndIndex() {
		return get(END_INDEX);
	}

	public void setEndIndex(Integer endIndex) {
		set(END_INDEX, endIndex);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(BEGIN_INDEX + ": ").append(getBeginIndex());
		sb.append("," + END_INDEX + ": ").append(getEndIndex());
		return sb.toString();
	}
}
