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
package org.openempi.webapp.client.mvc;

import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.KeyDataServiceAsync;
import org.openempi.webapp.client.PersonDataServiceAsync;
import org.openempi.webapp.client.SaltDataServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.Controller;

public abstract class AbstractController extends Controller
{
	private PersonDataServiceAsync personDataService;
	private ConfigurationDataServiceAsync configurationDataService;
	private KeyDataServiceAsync keyDataService;
	private SaltDataServiceAsync saltDataService;

	protected synchronized PersonDataServiceAsync getPersonDataService() {
		if (personDataService == null) {
			personDataService = (PersonDataServiceAsync) Registry.get(Constants.PERSON_DATA_SERVICE);			
		}
		return personDataService;
	}
	
	protected synchronized ConfigurationDataServiceAsync getConfigurationDataService() {
		if (configurationDataService == null) {
			configurationDataService = (ConfigurationDataServiceAsync) Registry.get(Constants.CONFIGURATION_DATA_SERVICE);			
		}
		return configurationDataService;
	}
	
	protected synchronized KeyDataServiceAsync getKeyDataService() {
		if (keyDataService == null) {
			keyDataService = (KeyDataServiceAsync) Registry.get(Constants.KEY_DATA_SERVICE);			
		}
		return keyDataService;
	}
	
	protected synchronized SaltDataServiceAsync getSaltDataService() {
		if (saltDataService == null) {
			saltDataService = (SaltDataServiceAsync) Registry.get(Constants.SALT_DATA_SERVICE);			
		}
		return saltDataService;
	}
	
}
