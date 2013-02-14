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
package org.openempi.webapp.server;

import org.openempi.webapp.server.RandomCompliment;
import org.openempi.webapp.server.SampleRemoteServiceImpl;
import org.openempi.webapp.server.RandomCompliment.compliment;

import junit.framework.TestCase;

public class SampleRemoteServiceImplTest extends TestCase {

	/**
	 * This test is pointless.  It's just here to show how you could and should test the 
	 * functionality of your Impls outside the gwt/servlet container
	 */
	public void testDoComplimentMe() {
		SampleRemoteServiceImpl sampleRemoteServiceImpl = new SampleRemoteServiceImpl();
		String result = sampleRemoteServiceImpl.doComplimentMe();
		compliment[] values = RandomCompliment.compliment.values();
		boolean found = false;
		for (int i = 0; i < values.length; i++) {
			if (values[i].name().equals(result)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}