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
package org.openempi.webapp.client.mvc.blocking;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.BlockingDataServiceAsync;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.BlockingSettingsWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BlockingConfigurationController extends AbstractController
{
	private BlockingConfigurationView blockingConfigurationView;
	private BlockingDataServiceAsync blockingService;

	public BlockingConfigurationController() {		
		this.registerEventTypes(AppEvents.BlockingConfigurationReceived);
		this.registerEventTypes(AppEvents.BlockingConfigurationRequest);
		this.registerEventTypes(AppEvents.BlockingConfigurationSave);
		this.registerEventTypes(AppEvents.BlockingConfigurationView);
		this.registerEventTypes(AppEvents.LeftDatasetColumnNamesArrived);
		this.registerEventTypes(AppEvents.RightDatasetColumnNamesArrived);
	}

	@Override
	protected void initialize() {
		blockingConfigurationView = new BlockingConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.BlockingConfigurationView) {
			forwardToView(blockingConfigurationView, event);
		} else if (type == AppEvents.BlockingConfigurationRequest) {
			requestBlockingConfigurationData();
		} else if (type == AppEvents.BlockingConfigurationSave) {
			saveBlockingConfiguration(event);
		} else if (type == AppEvents.LeftDatasetColumnNamesArrived || type == AppEvents.RightDatasetColumnNamesArrived) {
			forwardToView(blockingConfigurationView, event);
		}
	}

	private void saveBlockingConfiguration(AppEvent event) {
		BlockingDataServiceAsync blockingService = getBlockingDataService();
		BlockingSettingsWeb configuration = (BlockingSettingsWeb) event.getData();
		blockingService.saveBlockingConfigurationData(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	      }

	      public void onSuccess(String message) {
	    	  if (message == null || message.length() == 0) {
	    		  Info.display("Information", "Blocking configuration data was saved successfully.");
	    	  } else {
	    		  Info.display("Information", "Failed to save blocking configuration data: " + message);
	    	  }
	      }
	    }));
	}

	private void requestBlockingConfigurationData() {
		BlockingDataServiceAsync blockingService = getBlockingDataService();
		blockingService.loadBlockingConfigurationData(new AsyncCallback<BlockingSettingsWeb>() {
	      public void onFailure(Throwable caught) {
	        Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(BlockingSettingsWeb result) {
	        forwardToView(blockingConfigurationView, AppEvents.BlockingConfigurationReceived, result);
	      }
	    });
	}

	private BlockingDataServiceAsync getBlockingDataService() {
		if (blockingService == null) {
			blockingService = (BlockingDataServiceAsync) Registry.get(Constants.BLOCKING_DATA_SERVICE);
		}
		return blockingService;
	}
}
