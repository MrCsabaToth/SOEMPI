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
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.AdminConfigurationWeb;
import org.openempi.webapp.client.model.AdminConfigurationWeb.ComponentTypeWeb;
import org.openempi.webapp.client.widget.LoginDialog;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View
{
	private Viewport viewport;
	private LayoutContainer west;
	private LayoutContainer center;
	private LayoutContainer north;
	private LoginDialog dialog;

	public AppView(Controller controller) {
		super(controller);
	}

	protected void initialize() {
		dialog = new LoginDialog(controller);
		dialog.setClosable(false);
		dialog.addListener(Events.Hide, new Listener<WindowEvent>() {
			public void handleEvent(WindowEvent be) {
				List<Object> params = new ArrayList<Object>();
				params.add(dialog.getUserName());
				params.add(dialog.getPassword());
				Dispatcher.forwardEvent(AppEvents.Init, params);
			}
		});
		dialog.show();
	}

	private void initUI(AppEvent event) {
		dialog.resetControls();	// Clear name and password from Login dialog, credentials already put into the event data

		viewport = new Viewport();
		viewport.setLayout(new BorderLayout());

		createNorth();
		createWest();
		createCenter();

		// registry serves as a global context
		Registry.register(Constants.VIEWPORT, viewport);
		Registry.register(Constants.NORTH_PANEL, north);
		Registry.register(Constants.WEST_PANEL, west);
		Registry.register(Constants.CENTER_PANEL, center);

		RootPanel.get().add(viewport);

		// Save admin config
		AdminConfigurationWeb adminConfiguration = (AdminConfigurationWeb) Registry.get(Constants.ADMIN_CONFIGURATION);
		Integer componentType = dialog.getComponentType();
		if (componentType == 1)
			adminConfiguration.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE);
		else if (componentType == 2)
			adminConfiguration.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE);
		else if (componentType == 3)
			adminConfiguration.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE);
		else if (componentType == 4)
			adminConfiguration.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE);
		adminConfiguration.setExperimentalMode(dialog.getExperimentalMode());
		Registry.register(Constants.ADMIN_CONFIGURATION, adminConfiguration);
		controller.handleEvent(new AppEvent(AppEvents.AdminConfigurationToSave, adminConfiguration));
		// Authenticate with the supplied credentials
		controller.handleEvent(new AppEvent(AppEvents.AuthenticateWithCredentials, event.getData()));
		// Bring up table selcetion west panel
		Dispatcher.forwardEvent(AppEvents.DatasetSelectionView, null);
	}

	private void createNorth() {
		north = new LayoutContainer();
		String imageName = "soempi_logo.jpg";
		Integer componentType = dialog.getComponentType();
		if (componentType == 1)
			imageName = "dataprovider_logo.jpg";
		else if (componentType == 2)
			imageName = "dataintegrator_logo.jpg";
		else if (componentType == 3)
			imageName = "parametermanager_logo.jpg";
		else if (componentType == 4)
			imageName = "keyserver_logo.jpg";
		Html header = new Html("<div id='header'>" +
				"	 <img src='images/" + imageName + "'/> " +
				"</div>");
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 120);
		data.setMargins(new Margins(0, 0, 0, 0));
		header.setBorders(true);
		header.setWidth("100%");
		header.setHeight(70);
		north.add(header);
		viewport.add(north, data);
	}

	private void createWest() {

		west = new LayoutContainer();
		west.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 150, 150, 400);
		data.setMargins(new Margins(2, 0, 2, 2));

		viewport.add(west, data);
	}

	private void createCenter() {
		center = new LayoutContainer();
		center.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(5, 5, 5, 5));

		viewport.add(center, data);
	}

	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.Init) {
			initUI(event);
		} else if (event.getType() == AppEvents.AdminConfigurationReceived) {
			AdminConfigurationWeb adminConfiguration = event.getData();
			if (adminConfiguration.getComponentMode() == ComponentTypeWeb.DATA_PROVIDER_MODE)
				dialog.setComponentType(1);
			else if (adminConfiguration.getComponentMode() == ComponentTypeWeb.DATA_INTEGRATOR_MODE)
				dialog.setComponentType(2);
			else if (adminConfiguration.getComponentMode() == ComponentTypeWeb.PARAMETER_MANAGER_MODE)
				dialog.setComponentType(3);
			else if (adminConfiguration.getComponentMode() == ComponentTypeWeb.KEYSERVER_MODE)
				dialog.setComponentType(4);
			dialog.setExperimentalMode(adminConfiguration.getExperimentalMode());
			dialog.enableControls();
		}
	}

}
