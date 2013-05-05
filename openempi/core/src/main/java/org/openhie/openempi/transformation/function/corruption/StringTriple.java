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
package org.openhie.openempi.transformation.function.corruption;

import org.openhie.openempi.transformation.function.corruption.SwapoutBase.CaseEnum;

public class StringTriple
{
	public String upperCase;
	public String lowerCase;
	public String camelCase;

	public StringTriple(String upperCase, boolean generate) {
		this.upperCase = upperCase;
		if (generate && upperCase != null) {
			if (upperCase.length() == 0) {
				this.lowerCase = this.upperCase;
				this.camelCase = this.upperCase;
			} else {
				this.lowerCase = upperCase.toLowerCase();
				StringBuilder sb = new StringBuilder(lowerCase);
				sb.setCharAt(0, upperCase.charAt(0));
				this.camelCase = sb.toString();
			}
		}
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof StringTriple)
			return false;
		StringTriple st = (StringTriple)obj;
		return upperCase.equals(st.upperCase);
	}
	
	public String getCase(CaseEnum caseType) {
		if (caseType == CaseEnum.UpperCase || caseType == CaseEnum.Unknown)
			return upperCase;
		if (caseType == CaseEnum.LowerCase)
			return lowerCase;
		return camelCase;
	}
}
