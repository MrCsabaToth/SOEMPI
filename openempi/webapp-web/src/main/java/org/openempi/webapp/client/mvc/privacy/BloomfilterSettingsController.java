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
package org.openempi.webapp.client.mvc.privacy;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.model.BloomfilterSettingsWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BloomfilterSettingsController extends AbstractController
{
	private BloomfilterSettingsView bloomfilterSettingsView;

	public BloomfilterSettingsController() {		
		this.registerEventTypes(AppEvents.BloomfilterSettingsReceived);
		this.registerEventTypes(AppEvents.BloomfilterSettingsRequest);
		this.registerEventTypes(AppEvents.BloomfilterSettingsSave);
		this.registerEventTypes(AppEvents.BloomfilterSettingsView);
	}

	@Override
	protected void initialize() {
		bloomfilterSettingsView = new BloomfilterSettingsView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.BloomfilterSettingsView) {
			forwardToView(bloomfilterSettingsView, event);
		} else if (type == AppEvents.BloomfilterSettingsRequest) {
			requestBloomfilterSettingsData();
		} else if (type == AppEvents.BloomfilterSettingsSave) {
			saveBloomfilterSettings(event);
		}
	}

	private void saveBloomfilterSettings(AppEvent event) {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		BloomfilterSettingsWeb settings = (BloomfilterSettingsWeb) event.getData();
		configurationDataService.saveBloomfilterSettings(settings, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	      }

	      public void onSuccess(String message) {
	    	  if (message == null || message.length() == 0) {
	    		  Info.display("Information", "Bloomfilter settings data was saved successfully.");
	    	  } else {
	    		  Info.display("Information", "Failed to save bloomfilter settings data: " + message);
	    	  }
	      }
	    }));
	}

	private void requestBloomfilterSettingsData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadBloomfilterSettings(new AsyncCallback<BloomfilterSettingsWeb>() {
	      public void onFailure(Throwable caught) {
	        Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(BloomfilterSettingsWeb result) {
	        forwardToView(bloomfilterSettingsView, AppEvents.BloomfilterSettingsReceived, result);
	      }
	    });
	}

}
