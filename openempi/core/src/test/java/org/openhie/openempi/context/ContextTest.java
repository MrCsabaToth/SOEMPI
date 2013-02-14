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
package org.openhie.openempi.context;

import org.openhie.openempi.Constants;
import org.openhie.openempi.Core;
import org.openhie.openempi.service.BaseManagerTestCase;

public class ContextTest extends BaseManagerTestCase
{
	public void testAuthenticate() {
		Context context = (Context) this.getApplicationContext().getBean("context");
		System.out.println(context.getUserManager());
		Context.startup();
		Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_USERNAME);
		assertEquals("Hello", Core.getHello());
	}
}
