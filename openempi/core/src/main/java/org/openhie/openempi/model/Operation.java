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
package org.openhie.openempi.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Operation implements Serializable
{
	private static final long serialVersionUID = -8735471015676733746L;

	public static Operation EQ = new Operation("Equals");
	public static Operation NE = new Operation("Not Equals");
	public static Operation LIKE = new Operation("Like");
	public static Operation ISNULL = new Operation("Is NULL");
	public static Operation ISNOTNULL = new Operation("Is Not NULL");
	
	private String name;
	
	private Operation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}	
}
