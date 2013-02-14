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

import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.util.GeneralUtil;

public class SaltedMessageDigestFunction extends MessageDigestFunction
{
	static protected Integer saltGuard = 0;
	static protected byte[] salt = null;	// Salts for the salting

	public SaltedMessageDigestFunction() {
		super();
	}

	public void initSalt() {
		KeyServerService ks = Context.getKeyServerService();
		synchronized (saltGuard) {
			salt = ks.getSalts(1).get(0);
		}
	}

	public Object transform(Object field, java.util.Map<String, Object> parameters) {
		if (salt == null)
			initSalt();
		log.debug("Applying the salt and " + getMdFunctionName() + " hash transformation to field with value: " + field);
		if (field == null) {
			return null;
		}
		byte[] fieldBytes = null;
		if (field instanceof byte[]) {
			fieldBytes = (byte[])field;
		} else {
			String fieldString = field.toString();
			fieldBytes = fieldString.getBytes(Constants.charset);
		}
		log.debug("Salting");
		byte[] saltedBytes = GeneralUtil.concatByteArraysSimple(fieldBytes, salt);
		byte[] encodedValue = md.digest(saltedBytes);
		log.debug("The salted message digest value for field: '" + field + "' is '" + encodedValue + "'");
		return encodedValue;
	}
}
