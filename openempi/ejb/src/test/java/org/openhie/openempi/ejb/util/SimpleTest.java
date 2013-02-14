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

import javax.ejb.EJBException;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openhie.openempi.Constants;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.model.Person;

public class SimpleTest extends AbstractEJBTestCase
{
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public void testHello() {
		try {
			SecurityService securityService = getSecurityService();
			String sessionKey = securityService.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
			System.out.println("Obtained a session key of " + sessionKey);
			
			PersonManagerService personService = getPersonManagerService();
//			System.out.println(personService.sayHello(sessionKey));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void testAddPerson() {
		try {
			String sessionKey = getSessionKey();
			
			PersonManagerService personService = getPersonManagerService();
			Person person = new Person();
			person.setAttribute("givenName", "Odysseas");
			person.setAttribute("familyName", "Pentakalos");
			person.setAttribute("address1", "2930 Oak Shadow Drive");
			person.setAttribute("city", "Herndon");
			person.setAttribute("state", "Virginia");
	
			try {
				personService.addPerson(sessionKey, "testtablename", person, true, true);
			} catch (EJBException e) {
				log.warn("Person already exists in the system: " + e);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testAddKey() {
		try {
			String sessionKey = getSessionKey();

			try {
				KeyManagerService keyService = getKeyManagerService();
				keyService.addKey(sessionKey);
			} catch (EJBException e) {
				log.warn("Key already exists in the system: " + e);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testAddSalt() {
		try {
			String sessionKey = getSessionKey();

			try {
				SaltManagerService saltService = getSaltManagerService();
				saltService.addSalt(sessionKey);
			} catch (EJBException e) {
				log.warn("Key already exists in the system: " + e);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	private String getSessionKey() {
		String sessionKey=null;
		try {
			SecurityService securityService = getSecurityService();
			sessionKey = securityService.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
			System.out.println("Obtained a session key of " + sessionKey);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		return sessionKey;
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(SimpleTest.class);
		return testSuite;
	}
}
