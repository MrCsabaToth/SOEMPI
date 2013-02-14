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
package org.openhie.openempi.ejb.util;


import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;

public abstract class AbstractEJBTestCase extends TestCase
 {
	protected Logger log = Logger.getLogger(AbstractEJBTestCase.class);
	
	public AbstractEJBTestCase() {
		super();
	}

	public AbstractEJBTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ServiceLocator.getInitialContext();
	}

	protected SecurityService getSecurityService() throws NamingException {
		return ServiceLocator.getSecurityService();
	}

	protected PersonManagerService getPersonManagerService() throws NamingException {
		return ServiceLocator.getPersonManagerService();
	}

	protected PersonQueryService getPersonQueryService() throws NamingException {
		return ServiceLocator.getPersonQueryService();
	}

	protected KeyManagerService getKeyManagerService() throws NamingException {
		return ServiceLocator.getKeyManagerService();
	}

	protected SaltManagerService getSaltManagerService() throws NamingException {
		return ServiceLocator.getSaltManagerService();
	}

}
