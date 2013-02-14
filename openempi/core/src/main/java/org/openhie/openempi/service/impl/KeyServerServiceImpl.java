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

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.ejb.service.KeyManagerService;
import org.openhie.openempi.ejb.service.SaltManagerService;
import org.openhie.openempi.model.Key;
import org.openhie.openempi.model.Salt;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.recordlinkage.configuration.KeyServerSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.KeyServiceLocator;

public class KeyServerServiceImpl implements KeyServerService {
	private Log log = LogFactory.getLog(Configuration.class);

	private static String sessionKey;
	private static boolean isAuthenticated = false;

	// Cached salts
	static protected List<byte[]> salts = new ArrayList<byte[]>();

	public void authenticate(String username, String password) {
		KeyServiceLocator keyServiceLocator = Context.getKeyServiceLocator();

		SecurityService securityService;
		try {
			securityService = keyServiceLocator.getSecurityService();
			sessionKey = securityService.authenticate(username, password);
			isAuthenticated = true;
		} catch (NamingException e) {
			e.printStackTrace();
            log.error(e.getMessage());
		}
	}

	public List<byte[]> getKeys(long keyId) {
		// TODO: if (!isAuthenticated)

		KeyServiceLocator keyServiceLocator = Context.getKeyServiceLocator();
		List<byte[]> keyParts = null;
		try {
			KeyManagerService keyManagerService = keyServiceLocator.getKeyManagerService();
			Key key = keyManagerService.getKey(sessionKey, keyId);

			keyParts = new ArrayList<byte[]>();
			byte[] publicKeyPart1 = key.getPublicKeyPart1().clone();
			keyParts.add(publicKeyPart1);
			byte[] publicKeyPart2 = key.getPublicKeyPart2().clone();
			keyParts.add(publicKeyPart2);
			byte[] publicKeyPart3 = key.getPublicKeyPart3().clone();
			keyParts.add(publicKeyPart3);

			byte[] privateKeyPart1 = key.getPrivateKeyPart1().clone();
			keyParts.add(privateKeyPart1);
			byte[] privateKeyPart2 = key.getPrivateKeyPart2().clone();
			keyParts.add(privateKeyPart2);
			byte[] privateKeyPart3 = key.getPrivateKeyPart3().clone();
			keyParts.add(privateKeyPart3);
		} catch (NamingException e) {
			e.printStackTrace();
            log.error(e.getMessage());
		} catch (ApplicationException e) {
			e.printStackTrace();
            log.error(e.getMessage());
		}

		return keyParts;
	}

	public List<byte[]> getSalts(int numberOfSalts) {
		// TODO: if (!isAuthenticated)

		synchronized (salts) {
			if (salts == null || salts.size() <= 0) {
				PrivacySettings privacySettings =
						(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
				KeyServerSettings keyServerSettings =
						privacySettings.getComponentSettings().getKeyServerSettings();
		
				KeyServiceLocator keyServiceLocator = Context.getKeyServiceLocator();
				try {
					SaltManagerService saltManagerService = keyServiceLocator.getSaltManagerService();
		
					if (salts == null)
						salts = new ArrayList<byte[]>();
					Long saltId = Long.valueOf(keyServerSettings.getSaltIdStart());
					for (Long saltIndex = 0L; saltIndex < keyServerSettings.getNumberOfSalts(); saltIndex++) {
						Salt salt = saltManagerService.getSalt(sessionKey, saltId);
						byte[] saltPart = salt.getSalt().clone();
						salts.add(saltPart);
						saltId += Long.valueOf(keyServerSettings.getSaltIdStride());
					}
				} catch (NamingException e) {
					e.printStackTrace();
		            log.error(e.getMessage());
				} catch (ApplicationException e) {
					e.printStackTrace();
		            log.error(e.getMessage());
				}
			}
		}

		return salts.subList(0, numberOfSalts);
	}

}
