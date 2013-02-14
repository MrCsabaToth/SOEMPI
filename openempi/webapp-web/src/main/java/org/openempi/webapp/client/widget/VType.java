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
package org.openempi.webapp.client.widget;

public enum VType {
	ALPHABET("^[a-zA-Z_]+$", "Alphabet"),
	ALPHANUMERIC("^[a-zA-Z0-9_]+$", "Alphanumeric"),
	NUMERIC("^[+0-9]+$", "Numeric"),
	EMAIL("^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$", "Email"),
	PRINTABLE("^[\\p{Print}]+$", "Printable characters"),
	VISIBLE("^[\\p{Graph}]+$", "Visible characters");
	String regex;
	String name;

	VType(String regex, String name) {
		this.regex = regex;
		this.name = name;
	}
}
