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

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.ReferenceDataServiceAsync;
import org.openempi.webapp.client.model.AdminConfigurationWeb;
import org.openempi.webapp.client.model.FieldMeaningWeb;
import org.openempi.webapp.client.model.FieldTypeWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppController extends Controller {

	private AppView appView;
	private ReferenceDataServiceAsync referenceDataService;
	private ConfigurationDataServiceAsync configurationDataService;

	public AppController() {
		registerEventTypes(AppEvents.Init);
		registerEventTypes(AppEvents.Login);
		registerEventTypes(AppEvents.AuthenticateWithCredentials);
		registerEventTypes(AppEvents.Error);
		registerEventTypes(AppEvents.AllTransformationFunctionNamesReceived);
		registerEventTypes(AppEvents.ComparatorFunctionNamesReceived);
		registerEventTypes(AppEvents.BlockingServiceNamesReceived);
		registerEventTypes(AppEvents.MatchingServiceNamesReceived);
		registerEventTypes(AppEvents.RecordLinkageProtocolNamesReceived);
		registerEventTypes(AppEvents.AdminConfigurationReceived);
		registerEventTypes(AppEvents.AdminConfigurationToSave);
	}

	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.Init) {
			onInit(event);
		} else if (type == AppEvents.Login) {
			onLogin(event);
		} else if (type == AppEvents.AuthenticateWithCredentials) {
			List<Object> params = event.getData();
			configurationDataService.authenticateUser((String)params.get(0), (String)params.get(1), new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					Dispatcher.forwardEvent(AppEvents.Error, caught);
				}

				public void onSuccess(Void result) {
					// Info.display("Information", "Authentication successful");
				}
			});
		} else if (type == AppEvents.Error) {
			onError(event);
		} else if (type == AppEvents.ComparatorFunctionNamesReceived) {
			List<String> comparatorFunctionNames = event.getData();
			List<ModelPropertyWeb> compFuncNameModelPropList = new ArrayList<ModelPropertyWeb>();
			List<String> compFuncNameSimpleValueList = new ArrayList<String>();
			for (String name : comparatorFunctionNames) {
				compFuncNameModelPropList.add(new ModelPropertyWeb(name));
				compFuncNameSimpleValueList.add(new String(name));
			}
			Registry.register(Constants.COMPARATOR_FUNCTION_NAME_MODEL_PROPERTY_LIST, compFuncNameModelPropList);
			Registry.register(Constants.COMPARATOR_FUNCTION_NAME_SIMPLE_VALUE_LIST, compFuncNameSimpleValueList);
		} else if (type == AppEvents.AllTransformationFunctionNamesReceived) {
			List<String> transformationFunctionNames = event.getData();
			List<ModelPropertyWeb> trafoFuncNameModelPropList = new ArrayList<ModelPropertyWeb>();
			List<String> trafoFuncNameSimpleValueList = new ArrayList<String>();
			for (String name : transformationFunctionNames) {
				trafoFuncNameModelPropList.add(new ModelPropertyWeb(name));
				trafoFuncNameSimpleValueList.add(new String(name));
			}
			Registry.register(Constants.ALL_TRANSFORMATION_FUNCTION_NAME_MODEL_PROPERTY_LIST, trafoFuncNameModelPropList);
			Registry.register(Constants.ALL_TRANSFORMATION_FUNCTION_NAME_SIMPLE_VALUE_LIST, trafoFuncNameSimpleValueList);
		} else if (type == AppEvents.BlockingServiceNamesReceived) {
			List<String> blockingServiceNames = event.getData();
			List<ModelPropertyWeb> blockingSvcNames = new ArrayList<ModelPropertyWeb>(blockingServiceNames.size());
			for (String name : blockingServiceNames) {
				blockingSvcNames.add(new ModelPropertyWeb(name));
			}
			Registry.register(Constants.BLOCKING_SERVICE_NAMES, blockingSvcNames);
		} else if (type == AppEvents.MatchingServiceNamesReceived) {
			List<String> matchingServiceNames = event.getData();
			List<ModelPropertyWeb> matchingSvcNames = new ArrayList<ModelPropertyWeb>(matchingServiceNames.size());
			for (String name : matchingServiceNames) {
				matchingSvcNames.add(new ModelPropertyWeb(name));
			}
			Registry.register(Constants.MATCHING_SERVICE_NAMES, matchingSvcNames);
		} else if (type == AppEvents.RecordLinkageProtocolNamesReceived) {
			List<String> recordLinkageProtocolNames = event.getData();
			List<ModelPropertyWeb> recordLinkageProtNames = new ArrayList<ModelPropertyWeb>(recordLinkageProtocolNames.size());
			for (String name : recordLinkageProtocolNames) {
				recordLinkageProtNames.add(new ModelPropertyWeb(name));
			}
			Registry.register(Constants.RECORD_LINKAGE_PROTOCOL_NAMES, recordLinkageProtNames);
		} else if (type == AppEvents.AdminConfigurationReceived) {
			AdminConfigurationWeb adminConfiguration = event.getData();
			Registry.register(Constants.ADMIN_CONFIGURATION, adminConfiguration);
			forwardToView(appView, event);
		} else if (type == AppEvents.AdminConfigurationToSave) {
			AdminConfigurationWeb configuration = event.getData();
			configurationDataService.saveAdminConfiguration(configuration, (new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
				}

				public void onSuccess(String message) {
					if (message == null || message.length() == 0) {
						// Info.display("Information", "Admin configuration data was saved successfully.");
					} else {
						// Info.display("Information", "Failed to save admin configuration data: " + message);
					}
				}
			}));
		}
	}

	public void initialize() {
		appView = new AppView(this);
	}

	protected void onError(AppEvent ae) {
		System.out.println("error: " + ae.<Object> getData());
	}

	private void onInit(AppEvent event) {
		forwardToView(appView, event);
		referenceDataService = (ReferenceDataServiceAsync) Registry.get(Constants.REF_DATA_SERVICE);

		List<ModelPropertyWeb> fieldTypeNameModelPropList = new ArrayList<ModelPropertyWeb>();
		List<String> fieldTypeNameSimpleValueList = new ArrayList<String>();
		for(FieldTypeWeb fieldTypeValue : FieldTypeWeb.values()) {
			String fieldTypeName = fieldTypeValue.name();
			fieldTypeNameModelPropList.add(new ModelPropertyWeb(fieldTypeName));
			fieldTypeNameSimpleValueList.add(new String(fieldTypeName));
		}
		Registry.register(Constants.FIELD_TYPE_NAME_MODEL_PROPERTY_LIST, fieldTypeNameModelPropList);
		Registry.register(Constants.FIELD_TYPE_NAME_MODEL_SIMPLE_VALUE_LIST, fieldTypeNameSimpleValueList);

		List<ModelPropertyWeb> fieldMeaningNameModelPropList = new ArrayList<ModelPropertyWeb>();
		List<String> fieldMeaningNameSimpleValueList = new ArrayList<String>();
		for(FieldMeaningWeb fieldMeaningValue : FieldMeaningWeb.values()) {
			String fieldMeaningName = fieldMeaningValue.name();
			fieldMeaningNameModelPropList.add(new ModelPropertyWeb(fieldMeaningName));
			fieldMeaningNameSimpleValueList.add(new String(fieldMeaningName));
		}
		Registry.register(Constants.FIELD_MEANING_NAME_MODEL_PROPERTY_LIST, fieldMeaningNameModelPropList);
		Registry.register(Constants.FIELD_MEANING_NAME_MODEL_SIMPLE_VALUE_LIST, fieldMeaningNameSimpleValueList);

		referenceDataService.getAllTransformationFunctionNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> result) {
				Dispatcher.forwardEvent(AppEvents.AllTransformationFunctionNamesReceived, result);
				// Info.display("Information", "We've got all the transformation function names: " + result);
			}
		});

		referenceDataService.getComparatorFunctionNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> result) {
				Dispatcher.forwardEvent(AppEvents.ComparatorFunctionNamesReceived, result);
				// Info.display("Information", "We've got the comparator function names: " + result);
			}
		});

		referenceDataService.getBlockingServiceNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> result) {
				Dispatcher.forwardEvent(AppEvents.BlockingServiceNamesReceived, result);
				// Info.display("Information", "We've got the blocking service names: " + result);
			}
		});

		referenceDataService.getMatchingServiceNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> result) {
				Dispatcher.forwardEvent(AppEvents.MatchingServiceNamesReceived, result);
				// Info.display("Information", "We've got the matching service names: " + result);
			}
		});

		referenceDataService.getRecordLinkageProtocolNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> result) {
				Dispatcher.forwardEvent(AppEvents.RecordLinkageProtocolNamesReceived, result);
				// Info.display("Information", "We've got the record linkage protocol names: " + result);
			}
		});

	}

	private void onLogin(AppEvent event) {
		forwardToView(appView, event);
		configurationDataService = (ConfigurationDataServiceAsync) Registry.get(Constants.CONFIGURATION_DATA_SERVICE);
		configurationDataService.loadAdminConfiguration(new AsyncCallback<AdminConfigurationWeb>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(AdminConfigurationWeb result) {
				Dispatcher.forwardEvent(AppEvents.AdminConfigurationReceived, result);
				// Info.display("Information", "We've got the admin configuration: " + result);
			}
		});
	}

}
