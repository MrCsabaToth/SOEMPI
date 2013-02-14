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
package org.openhie.openempi.loader.configuration;

public class LoaderFieldComposition extends LoaderTargetField
{
	private static final long serialVersionUID = 363760197082149288L;

	private int index;
	private String separator;
	private String value;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		if (separator == null)
			return;
		
		if (separator.equals("SPACE"))
			this.separator = " ";
		else if (separator.equals("DASH"))
			this.separator = "-";
		else if (separator.equals("PER"))
			this.separator = "/";
		else if (separator.equals("BACKSLASH"))
			this.separator = "\\";
		else if (separator.equals("COMMA"))
			this.separator = ",";
		else if (separator.equals("PERCENT"))
			this.separator = "%";
		else if (separator.equals("DOLLAR"))
			this.separator = "$";
		else if (separator.equals("HASHMARK"))
			this.separator = "#";
		else if (separator.equals("AT"))
			this.separator = "@";
		else if (separator.equals("CAP"))
			this.separator = "^";
		else if (separator.equals("STAR"))
			this.separator = "*";
		else if (separator.equals("EXCLAMATION"))
			this.separator = "!";
		else if (separator.equals("QUESTION"))
			this.separator = "?";
		else
			this.separator = separator;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
