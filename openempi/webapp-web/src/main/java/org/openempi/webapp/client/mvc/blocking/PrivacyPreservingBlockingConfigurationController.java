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
package org.openempi.webapp.client.mvc.blocking;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingSettingsWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PrivacyPreservingBlockingConfigurationController extends AbstractController
{
	private PrivacyPreservingBlockingConfigurationView privacyPreservingBlockingConfigurationView;

	public PrivacyPreservingBlockingConfigurationController() {		
		this.registerEventTypes(AppEvents.PrivacyPreservingBlockingConfigurationReceived);
		this.registerEventTypes(AppEvents.PrivacyPreservingBlockingConfigurationRequest);
		this.registerEventTypes(AppEvents.PrivacyPreservingBlockingConfigurationSave);
		this.registerEventTypes(AppEvents.PrivacyPreservingBlockingConfigurationView);
		this.registerEventTypes(AppEvents.LeftDatasetColumnNamesArrived);
		this.registerEventTypes(AppEvents.RightDatasetColumnNamesArrived);
	}

	@Override
	protected void initialize() {
		privacyPreservingBlockingConfigurationView = new PrivacyPreservingBlockingConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.PrivacyPreservingBlockingConfigurationView) {
			forwardToView(privacyPreservingBlockingConfigurationView, event);
		} else if (type == AppEvents.PrivacyPreservingBlockingConfigurationRequest) {
			requestPrivacyPreservingBlockingSettingsData();
		} else if (type == AppEvents.PrivacyPreservingBlockingConfigurationSave) {
			savePrivacyPreservingBlockingSettings(event);
		} else if (type == AppEvents.LeftDatasetColumnNamesArrived || type == AppEvents.RightDatasetColumnNamesArrived) {
			forwardToView(privacyPreservingBlockingConfigurationView, event);
		}
	}

	private void savePrivacyPreservingBlockingSettings(AppEvent event) {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		PrivacyPreservingBlockingSettingsWeb ppBlockingSettings = (PrivacyPreservingBlockingSettingsWeb) event.getData();
		configurationDataService.savePrivacyPreservingBlockingSettings(ppBlockingSettings, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	      }

	      public void onSuccess(String message) {
	    	  if (message == null || message.length() == 0) {
	    		  Info.display("Information", "Privacy preserving blocking settings data was saved successfully.");
	    	  } else {
	    		  Info.display("Information", "Failed to save Privacy preserving blocking settings data: " + message);
	    	  }
	      }
	    }));
	}

	private void requestPrivacyPreservingBlockingSettingsData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadPrivacyPreservingBlockingSettings(new AsyncCallback<PrivacyPreservingBlockingSettingsWeb>() {
	      public void onFailure(Throwable caught) {
	        Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(PrivacyPreservingBlockingSettingsWeb result) {
	        forwardToView(privacyPreservingBlockingConfigurationView, AppEvents.PrivacyPreservingBlockingConfigurationReceived, result);
	      }
	    });
	}

}
