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
package org.openhie.openempi.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eaio.uuid.UUID;

import junit.framework.TestCase;

public class UUIDGeneratorTest extends TestCase
{
	private final Log log = LogFactory.getLog(UUIDGeneratorTest.class);
	
	public void testGenerateGUID() {
		UUID uuid = new UUID();
		log.trace(uuid.toString());
		
		UUID uuid2 = new UUID();
		log.trace(uuid2.toString());
		
		for (int i=0; i < 100; i++) {
			log.trace(new UUID().toString());
		}
		
		log.trace("The two uuid are the same is a " + uuid.compareTo(uuid2) + " statement.");
	}
}
