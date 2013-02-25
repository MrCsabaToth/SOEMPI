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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.openhie.openempi.Constants;

public class HMACFunction extends AbstractByteArrayTransformationFunction
{
	protected String hmacFunctionName; 
	protected Mac hmac;
	
	public HMACFunction() {
		super();
	}

	public void init() {
		try {
			hmac = Mac.getInstance(getHmacFunctionName());
		} catch (NoSuchAlgorithmException e) {
			log.error(getName() + " consturctor: Couldn't obtain " + getHmacFunctionName() + " instance");
			e.printStackTrace();
		}
	}

	public String getHmacFunctionName() {
		return hmacFunctionName;
	}

	public void setHmacFunctionName(String hmacFunctionName) {
		this.hmacFunctionName = hmacFunctionName;
	}

	protected byte[] byteTransformCore(byte[] field, Map<String, Object> parameters) {
		if (!parameters.containsKey(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME)) {
			log.error("No signing key passed as a parameter for " + getHmacFunctionName());
			return null;
		}
		Object signingKeyObject = parameters.get(Constants.SIGNING_KEY_HMAC_PARAMETER_NAME);
		byte[] signingKeyBytes = null;
		if (signingKeyObject instanceof byte[]) {
			signingKeyBytes = (byte[])signingKeyObject;
		} else {
			String skString = signingKeyObject.toString();
			signingKeyBytes = skString.getBytes(Constants.charset);			
		}
		log.debug("Signing key for " + getHmacFunctionName() + " is OK");

		SecretKeySpec signingKey = new SecretKeySpec(signingKeyBytes, getHmacFunctionName());
		try {
			hmac.init(signingKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] encodedValue = hmac.doFinal(field);
		return encodedValue;
	}
}
