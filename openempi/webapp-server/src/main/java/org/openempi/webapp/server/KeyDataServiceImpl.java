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
package org.openempi.webapp.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.KeyDataService;
import org.openempi.webapp.client.model.Key;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.KeyManagerService;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class KeyDataServiceImpl extends RemoteServiceServlet implements KeyDataService
{
	private static final long serialVersionUID = 7482056827325581995L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public Key addKey() {
		log.debug("Received request to retrieve a key record by identifier.");
		try {
			KeyManagerService keyManagerService = Context.getKeyManagerService();
			org.openhie.openempi.model.Key key = keyManagerService.addKey();
			log.trace("Created key: " + key.getId() + " " +
					key.getPublicKeyPart1().length + " " +
					key.getPublicKeyPart2().length + " " +
					key.getPublicKeyPart3().length + " " +
					key.getPrivateKeyPart1().length + " " +
					key.getPrivateKeyPart2().length + " " +
					key.getPrivateKeyPart3().length);
			Key dto = ModelTransformer.transformKey(key);
			return dto;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public Key getKey(Long keyId) {
		log.debug("Received request to retrieve a key record by identifier.");
		try {
			KeyManagerService keyManagerService = Context.getKeyManagerService();
			org.openhie.openempi.model.Key key = keyManagerService.getKey(keyId);
			log.trace("Found key: " + key.getId() + " " +
					key.getPublicKeyPart1().length + " " +
					key.getPublicKeyPart2().length + " " +
					key.getPublicKeyPart3().length + " " +
					key.getPrivateKeyPart1().length + " " +
					key.getPrivateKeyPart2().length + " " +
					key.getPrivateKeyPart3().length);
			Key dto = ModelTransformer.transformKey(key);
			return dto;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public void deleteKey(Long keyId) {
		log.debug("Received request to retrieve a key record by identifier.");
		try {
			KeyManagerService keyManagerService = Context.getKeyManagerService();
			org.openhie.openempi.model.Key key = keyManagerService.getKey(keyId);
			log.trace("Found key: " + key.getId() + " " +
					key.getPublicKeyPart1().length + " " +
					key.getPublicKeyPart2().length + " " +
					key.getPublicKeyPart3().length + " " +
					key.getPrivateKeyPart1().length + " " +
					key.getPrivateKeyPart2().length + " " +
					key.getPrivateKeyPart3().length);
			keyManagerService.deleteKey(keyId);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

}
