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

import javax.naming.NamingException;

import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServiceLocator;

public class KeyServiceLocatorImpl extends ServiceLocatorBaseImpl implements KeyServiceLocator
{
	public KeyManagerService getKeyManagerService() throws NamingException {
		log.debug("Looking up an instance of the key manager using: KeyManagerService/remote");
		KeyManagerService keyManagerService = (KeyManagerService) getInitialContext().lookup("openempi/KeyManagerService");
		log.debug("Obtained an instance of the key manager service: " + keyManagerService);
		return keyManagerService;
	}
	
	public SaltManagerService getSaltManagerService() throws NamingException {
		log.debug("Looking up an instance of the salt manager using: SaltManagerService/remote");
		SaltManagerService saltManagerService = (SaltManagerService) getInitialContext().lookup("openempi/SaltManagerService");
		log.debug("Obtained an instance of the salt manager service: " + saltManagerService);
		return saltManagerService;
	}

	public String getProviderURL() {
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		return privacySettings.getComponentSettings().getKeyServerSettings().getServerAddress() +
			":" + Constants.RMI_DEFAULT_PORT_NUMBER;
	}

}
