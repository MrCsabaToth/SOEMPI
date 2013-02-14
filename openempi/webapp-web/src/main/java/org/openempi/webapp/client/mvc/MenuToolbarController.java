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

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;

public class MenuToolbarController extends Controller {
	private MenuToolbarView menuToolbarView;

	public MenuToolbarController() {
		registerEventTypes(AppEvents.Init);
		registerEventTypes(AppEvents.FileImportWizardSelected);
		registerEventTypes(AppEvents.RecordLinkageWizardSelected);
		registerEventTypes(AppEvents.ExitWizardSelected);
		registerEventTypes(AppEvents.WizardEnded);
	}

	public void initialize() {
		menuToolbarView = new MenuToolbarView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.Init) {
			onInit(event);
		} else if (event.getType() == AppEvents.FileImportWizardSelected) {
			onFileImportWizard(event);
		} else if (event.getType() == AppEvents.RecordLinkageWizardSelected) {
			onRecordLinkageWizard(event);
		} else if (event.getType() == AppEvents.ExitWizardSelected) {
			onWizardEnded(event);
		} else if (event.getType() == AppEvents.WizardEnded) {
			onWizardEnded(event);
		}
	}

	private void onInit(AppEvent event) {
		forwardToView(menuToolbarView, event);
	}

	private void onFileImportWizard(AppEvent event) {
		Registry.register(Constants.WIZARD_MODE, (Object)Constants.FILE_IMPORT_WIZARD_MODE);
		forwardToView(menuToolbarView, event);
		Dispatcher.get().dispatch(AppEvents.ComponentSettingsView);
	}

	private void onRecordLinkageWizard(AppEvent event) {
		Registry.register(Constants.WIZARD_MODE, (Object)Constants.RECORD_LINKAGE_WIZARD_MODE);
		forwardToView(menuToolbarView, event);
		Dispatcher.get().dispatch(AppEvents.BlockingConfigurationView);
	}

	private void onWizardEnded(AppEvent event) {
		Registry.register(Constants.WIZARD_MODE, (Object)Constants.NO_WIZARD_MODE);
		forwardToView(menuToolbarView, event);
	}
}
