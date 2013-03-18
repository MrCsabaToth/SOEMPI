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
package org.openempi.webapp.client;

import org.openempi.webapp.client.KeyDataService;
import org.openempi.webapp.client.KeyDataServiceAsync;
import org.openempi.webapp.client.SaltDataService;
import org.openempi.webapp.client.SaltDataServiceAsync;
import org.openempi.webapp.client.SecurityService;
import org.openempi.webapp.client.SecurityServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * 
 * Make and hold a singleton (per app user) reference to service proxy.
 * Pass this to every screen
 *
 */
public class ServiceRegistry {
	//proxies to our services
	private KeyDataServiceAsync keyDataService;
	private SaltDataServiceAsync saltDataService;
	private SecurityServiceAsync securityService;

	
	public KeyDataServiceAsync getKeyDataService() {
		if(keyDataService == null) {
			keyDataService = GWT.create(KeyDataService.class);
			((ServiceDefTarget)keyDataService).setServiceEntryPoint(GWT.getModuleBaseURL() + "keyDataService");
		}
		return keyDataService;
	}
	
	public SaltDataServiceAsync getSaltDataService() {
		if(saltDataService == null) {
			saltDataService = GWT.create(SaltDataService.class);
			((ServiceDefTarget)saltDataService).setServiceEntryPoint(GWT.getModuleBaseURL() + "saltDataService");
		}
		return saltDataService;
	}
	
	public SecurityServiceAsync getSecurityService() {
		if(securityService == null) {
			securityService = GWT.create(SecurityService.class);
			((ServiceDefTarget)securityService).setServiceEntryPoint(GWT.getModuleBaseURL() + "securityService");
		}
		return securityService;
	}
	
}
