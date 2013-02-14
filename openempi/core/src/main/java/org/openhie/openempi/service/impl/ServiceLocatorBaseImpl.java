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
package org.openhie.openempi.service.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.openhie.openempi.Constants;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.service.ServiceLocatorBase;

public abstract class ServiceLocatorBaseImpl extends BaseServiceImpl implements ServiceLocatorBase
{
	private static Context context;

	public SecurityService getSecurityService() throws NamingException {
		log.debug("Looking up an instance of the security using: SecurityService/remote");
		Object lookedupObject = getInitialContext().lookup("openempi/SecurityService");
		SecurityService securityService = (SecurityService) lookedupObject;
		log.debug("Obtained an instance of the security service: " + securityService);
		return securityService;
	}

	public void close() throws NamingException {
		if (context != null) {
			context.close();
			context = null;
		}
	}

	/**
	 * Get the initial naming context
	 */
	public Context getInitialContext() throws NamingException {
		if (context == null) {
			Hashtable<String,String> props = new Hashtable<String,String>();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			props.put(Constants.OPENEMPI_EXTENSION_CONTEXTS, org.openhie.openempi.context.Context.getExtensionContextsString());
//			props.put("javax.net.ssl.trustStore", "C:/openempi/keys/client.truststore");
//			props.put("javax.net.ssl.trustStorePassword", "ClientPassword");
			props.put(Context.PROVIDER_URL, getProviderURL());
			context = new InitialContext(props);
		}
		return context;
	}

	

}
