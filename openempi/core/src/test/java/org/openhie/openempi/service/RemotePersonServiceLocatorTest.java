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

import java.util.List;

import javax.naming.NamingException;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.util.PersonUtils;

public class RemotePersonServiceLocatorTest extends BaseServiceTestCase
{
	public static final String TABLE_NAME = "testtablename";

	private String ipAddress = null;

	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
		ipAddress = "127.0.0.1";
//		ipAddress = Context.getConfiguration().getPrivacySettings().getComponentSettings().
//						getDataIntegratorSettings().getIpAddress();
	}

	public void testGetPersonManagerService() {
		try {
			RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();

			SecurityService securityService = remotePersonServiceLocator.getSecurityService(ipAddress);
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			PersonManagerService personManagerService = remotePersonServiceLocator.getPersonManagerService();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void testGetPersonQueryService() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);

		try {
			org.openhie.openempi.service.PersonManagerService pms = Context.getPersonManagerService();
			PersonUtils.createTestPersonTable(pms, TABLE_NAME, "", false, null, false, null, null);

			RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();

			SecurityService securityService = remotePersonServiceLocator.getSecurityService(ipAddress);
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);

			PersonQueryService personQueryService = remotePersonServiceLocator.getPersonQueryService();
			List<Person> ps = personQueryService.getPersonsPaged(sessionKey, TABLE_NAME, 0L, 2);
			if (ps != null) {
				for (Person p : ps) {
					System.out.println("Found person: " + p);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void testGetPersonByIdQueryRemoteService() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		try {
			org.openhie.openempi.service.PersonManagerService pms = Context.getPersonManagerService();
			PersonUtils.createTestPersonTable(pms, TABLE_NAME, "", false, null, false, null, null);

			RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();

			SecurityService securityService = remotePersonServiceLocator.getSecurityService(ipAddress);
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			PersonQueryService personQueryService = remotePersonServiceLocator.getPersonQueryService();
			Person p = personQueryService.getPersonById(sessionKey, TABLE_NAME, 1L);
			if (p != null) {
				System.out.println("Found person: " + p);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	public void testGetPersonPagedQueryRemoteService() throws ApplicationException {
		try {
			RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();

			SecurityService securityService = remotePersonServiceLocator.getSecurityService(ipAddress);
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			PersonQueryService personQueryService = remotePersonServiceLocator.getPersonQueryService();
			List<Person> ps = personQueryService.getPersonsPaged(sessionKey, "testtablename", 0L, 2);
			if (ps != null) {
				for (Person p : ps) {
					System.out.println("Found person: " + p);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
}
