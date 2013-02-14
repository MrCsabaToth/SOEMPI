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
package org.openhie.openempi.service;

import javax.naming.NamingException;

import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;

import org.openhie.openempi.context.Context;

public class KeyServiceLocatorTest extends BaseServiceTestCase
{
	
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}

	public void testGetKeyService() {
		try {
			KeyServiceLocator keyServiceLocator = Context.getKeyServiceLocator();

			SecurityService securityService = keyServiceLocator.getSecurityService();
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			KeyManagerService keyManagerService = keyServiceLocator.getKeyManagerService();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetSaltService() {
		try {
			KeyServiceLocator keyServiceLocator = Context.getKeyServiceLocator();

			SecurityService securityService = keyServiceLocator.getSecurityService();
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			SaltManagerService saltManagerService = keyServiceLocator.getSaltManagerService();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
