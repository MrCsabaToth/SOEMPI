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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.SaltDataService;
import org.openempi.webapp.client.model.Salt;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.SaltManagerService;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SaltDataServiceImpl extends RemoteServiceServlet implements SaltDataService
{
	private static final long serialVersionUID = -5247831954079455267L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public Integer addSalts() {
		log.debug("Received request to generate salts.");
		int saltsAdded = 0;
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		int saltsToAdd = privacySettings.getComponentSettings().getKeyServerSettings().getNumberOfSalts();
		for (; saltsAdded < saltsToAdd; saltsAdded++) {
			int numTries = 0;
			int maxNumTries = 5;
			boolean success = false;
			while (!success) {
				try {
					SaltManagerService saltManagerService = Context.getSaltManagerService();
					org.openhie.openempi.model.Salt salt = saltManagerService.addSalt();
					log.trace("Created salt: " + salt.getId() + " " + salt.getSalt().length);
					success = true;
					//Salt dto = ModelTransformer.transformSalt(salt);
				} catch (Throwable t) {
					log.error("Failed to execute: " + t.getMessage(), t);
					numTries++;
					if (numTries > maxNumTries)
						throw new RuntimeException(t);
				}
			}
		}
		log.trace("Generated " + saltsAdded + " salts");
		return saltsAdded;
	}

	public Salt getSalt(Long saltId) {
		log.debug("Received request to retrieve a salt record by identifier.");
		try {
			SaltManagerService saltManagerService = Context.getSaltManagerService();
			org.openhie.openempi.model.Salt salt = saltManagerService.getSalt(saltId);
			log.trace("Found salt: " + salt.getId() + " " + salt.getSalt().length);
			Salt dto = ModelTransformer.transformSalt(salt);
			return dto;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<Salt> getSalts(Long startId, Long endId) {
		log.debug("Received request to retrieve a list of salt records by identifier.");
		try {
			SaltManagerService saltManagerService = Context.getSaltManagerService();
			List<org.openhie.openempi.model.Salt> salts = saltManagerService.getSalts(startId, endId);
			List<Salt> dtos = new ArrayList<Salt>();
			for (org.openhie.openempi.model.Salt salt : salts) {
				log.trace("Found salt: " + salt.getId() + " " + salt.getSalt().length);
				Salt dto = ModelTransformer.transformSalt(salt);
				dtos.add(dto);
			}
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public void deleteSalt(Long saltId) {
		log.debug("Received request to retrieve a salt record by identifier.");
		try {
			SaltManagerService saltManagerService = Context.getSaltManagerService();
			org.openhie.openempi.model.Salt salt = saltManagerService.getSalt(saltId);
			log.trace("Found salt: " + salt.getId() + " " + salt.getSalt().length);
			saltManagerService.deleteSalt(saltId);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

}
