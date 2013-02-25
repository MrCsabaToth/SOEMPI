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
package org.openhie.openempi.transformation.function;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MessageDigestFunction extends AbstractByteArrayTransformationFunction
{
	protected String mdFunctionName; 
	protected MessageDigest md;
	
	public MessageDigestFunction() {
		super();
	}

	public void init() {
		try {
			md = MessageDigest.getInstance(getMdFunctionName());
		} catch (NoSuchAlgorithmException e) {
			log.error(getName() + " consturctor: Couldn't obtain " + getMdFunctionName() + " instance");
			e.printStackTrace();
		}
	}

	public String getMdFunctionName() {
		return mdFunctionName;
	}

	public void setMdFunctionName(String mdFunctionName) {
		this.mdFunctionName = mdFunctionName;
	}

	protected byte[] byteTransformCore(byte[] field, Map<String, Object> parameters) {
		byte[] encodedValue = md.digest(field);
		return encodedValue;
	}
}
