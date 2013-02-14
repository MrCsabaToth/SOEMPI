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

public class LoaderFieldCompositionWeb extends LoaderTargetFieldWeb implements Serializable
{
	private static final long serialVersionUID = -550321621329324552L;

	public static final String INDEX = "index";
	public static final String SEPARATOR = "separator";

	public LoaderFieldCompositionWeb() {
	}

	public LoaderFieldCompositionWeb(Integer index, String separator) {
		set(INDEX, index);
		set(SEPARATOR, separator);
	}

	public Integer getIndex() {
		return get(INDEX);
	}

	public void setIndex(Integer index) {
		set(INDEX, index);
	}

	public java.lang.String getSeparator() {
		return get(SEPARATOR);
	}

	public void setSeparator(java.lang.String separator) {
		set(SEPARATOR, separator);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(INDEX + ": ").append(getIndex());
		sb.append("," + SEPARATOR + ": ").append(getSeparator());
		return sb.toString();
	}
}
