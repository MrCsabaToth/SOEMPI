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

import org.openempi.webapp.client.mvc.AppController;
import org.openempi.webapp.client.mvc.MenuToolbarController;
import org.openempi.webapp.client.mvc.blocking.BlockingConfigurationController;
import org.openempi.webapp.client.mvc.blocking.PrivacyPreservingBlockingConfigurationController;
import org.openempi.webapp.client.mvc.configuration.MatchConfigurationParametersController;
import org.openempi.webapp.client.mvc.configuration.MatchFieldConfigurationController;
import org.openempi.webapp.client.mvc.edit.EditController;
import org.openempi.webapp.client.mvc.fileloader.FileLoaderConfigurationController;
import org.openempi.webapp.client.mvc.management.KeyManagementController;
import org.openempi.webapp.client.mvc.management.SaltManagementController;
import org.openempi.webapp.client.mvc.privacy.BloomfilterSettingsController;
import org.openempi.webapp.client.mvc.privacy.ComponentSettingsController;
import org.openempi.webapp.client.mvc.search.RealSearchController;
import org.openempi.webapp.client.mvc.user.DatasetSelectionController;
import org.openempi.webapp.client.mvc.user.DatasetController;
import org.openempi.webapp.client.mvc.user.MatchController;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MainApp implements EntryPoint
{
	public void onModuleLoad() {
		GXT.setDefaultTheme(Theme.GRAY, true);

		ReferenceDataServiceAsync refDataService = (ReferenceDataServiceAsync) GWT.create(ReferenceDataService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) refDataService;
		String moduleRelativeURL = Constants.REF_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.REF_DATA_SERVICE, refDataService);

		PersonDataServiceAsync personDataService = (PersonDataServiceAsync) GWT.create(PersonDataService.class);
		endpoint = (ServiceDefTarget) personDataService;
		moduleRelativeURL = Constants.PERSON_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.PERSON_DATA_SERVICE, personDataService);
		
		BlockingDataServiceAsync blockingDataService = (BlockingDataServiceAsync) GWT.create(BlockingDataService.class);
		endpoint = (ServiceDefTarget) blockingDataService;
		moduleRelativeURL = Constants.BLOCKING_DATA_SERVICE;
		endpoint.setServiceEntryPoint(moduleRelativeURL);
		Registry.register(Constants.BLOCKING_DATA_SERVICE, blockingDataService);
		
		FileLoaderDataServiceAsync fileLoaderDataService = (FileLoaderDataServiceAsync) GWT.create(FileLoaderDataService.class);
		endpoint = (ServiceDefTarget) fileLoaderDataService;
		moduleRelativeURL = Constants.FILE_LOADER_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.FILE_LOADER_DATA_SERVICE, fileLoaderDataService);
		
		ConfigurationDataServiceAsync configuartionDataService = (ConfigurationDataServiceAsync) GWT.create(ConfigurationDataService.class);
		endpoint = (ServiceDefTarget) configuartionDataService;
		moduleRelativeURL = Constants.CONFIGURATION_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.CONFIGURATION_DATA_SERVICE, configuartionDataService);
		
		KeyDataServiceAsync keyDataService = (KeyDataServiceAsync) GWT.create(KeyDataService.class);
		endpoint = (ServiceDefTarget) keyDataService;
		moduleRelativeURL = Constants.KEY_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.KEY_DATA_SERVICE, keyDataService);

		SaltDataServiceAsync saltDataService = (SaltDataServiceAsync) GWT.create(SaltDataService.class);
		endpoint = (ServiceDefTarget) saltDataService;
		moduleRelativeURL = Constants.SALT_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.SALT_DATA_SERVICE, saltDataService);

		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new AppController());
		dispatcher.addController(new DatasetSelectionController());
		dispatcher.addController(new RealSearchController());
		dispatcher.addController(new EditController());
		dispatcher.addController(new MenuToolbarController());
		dispatcher.addController(new DatasetController());
		dispatcher.addController(new BlockingConfigurationController());
		dispatcher.addController(new PrivacyPreservingBlockingConfigurationController());
		dispatcher.addController(new FileLoaderConfigurationController());
		dispatcher.addController(new ComponentSettingsController());
		dispatcher.addController(new BloomfilterSettingsController());
		dispatcher.addController(new MatchFieldConfigurationController());
		dispatcher.addController(new MatchConfigurationParametersController());
		dispatcher.addController(new MatchController());
		dispatcher.addController(new KeyManagementController());
		dispatcher.addController(new SaltManagementController());

		dispatcher.dispatch(AppEvents.Login);

		GXT.hideLoadingPanel("loading");
	}

}
