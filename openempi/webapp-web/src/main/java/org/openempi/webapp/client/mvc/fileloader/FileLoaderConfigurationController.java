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
package org.openempi.webapp.client.mvc.fileloader;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.FileLoaderDataServiceAsync;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FileLoaderConfigurationController extends AbstractController
{
	private FileLoaderConfigurationView fileLoaderConfigurationView;
	private FileLoaderDataServiceAsync fileLoaderService;

	public FileLoaderConfigurationController() {		
		this.registerEventTypes(AppEvents.FileLoaderConfigurationReceived);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationRequest);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationSave);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationView);
		this.registerEventTypes(AppEvents.FileLoaderDataFieldTrafoParametersEdited);
		this.registerEventTypes(AppEvents.FileLoaderSubFieldTrafoParametersEdited);
	}

	@Override
	protected void initialize() {
		fileLoaderConfigurationView = new FileLoaderConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.FileLoaderConfigurationView) {
			forwardToView(fileLoaderConfigurationView, event);
		} else if (type == AppEvents.FileLoaderConfigurationRequest) {
			requestFileLoaderConfigurationData();
		} else if (type == AppEvents.FileLoaderConfigurationSave) {
			saveFileLoaderConfiguration(event);
		} else if (type == AppEvents.FileLoaderDataFieldTrafoParametersEdited) {
			forwardToView(fileLoaderConfigurationView, event);
		} else if (type == AppEvents.FileLoaderSubFieldTrafoParametersEdited) {
			forwardToView(fileLoaderConfigurationView, event);
		}
	}

	private void saveFileLoaderConfiguration(AppEvent event) {
		FileLoaderDataServiceAsync fileLoaderService = getFileLoaderDataService();
		LoaderConfigWeb configuration = (LoaderConfigWeb) event.getData();
		fileLoaderService.saveFileLoaderConfigurationData(configuration, (new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String message) {
				if (message == null || message.length() == 0) {
					Info.display("Information", "File loader configuration data was saved successfully.");
				} else {
					Info.display("Information", "Failed to save file loader configuration data: " + message);
				}
			}
		}));
	}

	private void requestFileLoaderConfigurationData() {
		FileLoaderDataServiceAsync fileLoaderService = getFileLoaderDataService();
		fileLoaderService.loadFileLoaderConfigurationData(new AsyncCallback<LoaderConfigWeb>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(LoaderConfigWeb result) {
				forwardToView(fileLoaderConfigurationView, AppEvents.FileLoaderConfigurationReceived, result);
			}
		});
	}

	private FileLoaderDataServiceAsync getFileLoaderDataService() {
		if (fileLoaderService == null) {
			fileLoaderService = (FileLoaderDataServiceAsync) Registry.get(Constants.FILE_LOADER_DATA_SERVICE);
		}
		return fileLoaderService;
	}
}
