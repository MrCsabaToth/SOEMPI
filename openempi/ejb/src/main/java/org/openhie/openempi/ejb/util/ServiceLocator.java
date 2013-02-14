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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.configuration.AdminConfiguration;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;

public class ServiceLocator
{
	protected static final Log log = LogFactory.getLog(ServiceLocator.class);
	
	private static Context context;

	private static AdminConfiguration adminConfig = null;

	private static boolean componentTypeCheck(AdminConfiguration.ComponentType[] allowedComponentTypes) {
/*		if (adminConfig == null)
			adminConfig = org.openhie.openempi.context.Context.getConfiguration().getAdminConfiguration();
		if (adminConfig.getExperimentalMode())
			return true;
		ComponentType componentMode = adminConfig.getComponentMode();
		for (int i = 0; i < allowedComponentTypes.length; i++)
			if (componentMode == allowedComponentTypes[i])
				return true;
		return false;*/
		// Temporarily disable component type check - for research purposes: one SOEMPI instance can serve many roles
		return true;
	}

	public static SecurityService getSecurityService() throws NamingException {
		log.debug("Looking up an instance of the person using: SecurityService/remote");
		SecurityService securityService = (SecurityService) getInitialContext().lookup("openempi/SecurityService");
		log.debug("Obtained an instance of the security service: " + securityService);
		return securityService;
	}
	
	public static PersonManagerService getPersonManagerService() throws NamingException {
		if (componentTypeCheck(new AdminConfiguration.ComponentType[] { ComponentType.DATA_INTEGRATOR_MODE, ComponentType.PARAMETER_MANAGER_MODE }))
			throw new NamingException("getPersonManagerService is not available in mode " + adminConfig.getComponentMode());
		log.debug("Looking up an instance of the person manager using: PersonManagerService/remote");
		PersonManagerService personManagerService = (PersonManagerService) getInitialContext().lookup("openempi/PersonManagerService");
		log.debug("Obtained an instance of the person manager service: " + personManagerService);
		return personManagerService;
	}
	
	public static PersonQueryService getPersonQueryService() throws NamingException {
		if (componentTypeCheck(new AdminConfiguration.ComponentType[] { ComponentType.DATA_INTEGRATOR_MODE }))
			throw new NamingException("getPersonQueryService is not available in mode " + adminConfig.getComponentMode());
		log.debug("Looking up an instance of the person query using: PersonQueryService/remote");
		PersonQueryService personQueryService = (PersonQueryService) getInitialContext().lookup("openempi/PersonQueryService");
		log.debug("Obtained an instance of the person query service: " + personQueryService);
		return personQueryService;		
	}

	public static KeyManagerService getKeyManagerService() throws NamingException {
		if (componentTypeCheck(new AdminConfiguration.ComponentType[] { ComponentType.KEYSERVER_MODE, ComponentType.PARAMETER_MANAGER_MODE }))
			throw new NamingException("getKeyManagerService is not available in mode " + adminConfig.getComponentMode());
		log.debug("Looking up an instance of the key manager using: KeyManagerService/remote");
		KeyManagerService keyManagerService = (KeyManagerService) getInitialContext().lookup("openempi/KeyManagerService");
		log.debug("Obtained an instance of the key manager service: " + keyManagerService);
		return keyManagerService;
	}
	
	public static SaltManagerService getSaltManagerService() throws NamingException {
		if (componentTypeCheck(new AdminConfiguration.ComponentType[] { ComponentType.KEYSERVER_MODE, ComponentType.PARAMETER_MANAGER_MODE }))
			throw new NamingException("getSaltManagerService is not available in mode " + adminConfig.getComponentMode());
		log.debug("Looking up an instance of the salt manager using: SaltManagerService/remote");
		SaltManagerService saltManagerService = (SaltManagerService) getInitialContext().lookup("openempi/SaltManagerService");
		log.debug("Obtained an instance of the salt manager service: " + saltManagerService);
		return saltManagerService;
	}

	/**
	 * Get the initial naming context
	 */
	protected static Context getInitialContext() throws NamingException {
		if (context == null) {
			Hashtable<String,String> props = new Hashtable<String,String>();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			props.put(Context.PROVIDER_URL, "localhost:1099");
			context = new InitialContext(props);
		}
		return context;
	}
}
