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
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ComponentSettingsWeb;
import org.openempi.webapp.client.model.DataIntegratorSettingsWeb;
import org.openempi.webapp.client.model.KeyServerSettingsWeb;
import org.openempi.webapp.client.model.ParameterManagerSettingsWeb;
import org.openempi.webapp.client.widget.ServerAddressValidator;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;

public class ComponentSettingsView extends View
{
	private TextField<String> keyserverAddressEdit = new TextField<String>();
	private NumberField numberOfSaltsEdit = new NumberField();
	private NumberField saltIdStartEdit = new NumberField();
	private NumberField saltIdStrideEdit = new NumberField();
	private TextField<String> parameterManagerAddressEdit = new TextField<String>();
	private TextField<String> dataIntegratorAddressEdit = new TextField<String>();
	
	private LayoutContainer container;
	
	public ComponentSettingsView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ComponentSettingsView) {
			initUI();
		} else if (event.getType() == AppEvents.ComponentSettingsReceived) {
			ComponentSettingsWeb componentSettingsWeb = (ComponentSettingsWeb) event.getData();
			keyserverAddressEdit.setValue(componentSettingsWeb.getKeyServerSettings().getServerAddress());
			numberOfSaltsEdit.setValue(componentSettingsWeb.getKeyServerSettings().getNumberOfSalts());
			saltIdStartEdit.setValue(componentSettingsWeb.getKeyServerSettings().getSaltIdStart());
			saltIdStrideEdit.setValue(componentSettingsWeb.getKeyServerSettings().getSaltIdStride());
			parameterManagerAddressEdit.setValue(componentSettingsWeb.getParameterManagerSettings().getServerAddress());
			dataIntegratorAddressEdit.setValue(componentSettingsWeb.getDataIntegratorSettings().getServerAddress());
		}
	}

	protected void saveSettings()
	{
		ComponentSettingsWeb componentSettingsWeb = new ComponentSettingsWeb();

		KeyServerSettingsWeb keyServerSettingsWeb = new KeyServerSettingsWeb();
		keyServerSettingsWeb.setServerAddress(keyserverAddressEdit.getValue());
		keyServerSettingsWeb.setNumberOfSalts(numberOfSaltsEdit.getValue().intValue());
		keyServerSettingsWeb.setSaltIdStart(saltIdStartEdit.getValue().intValue());
		keyServerSettingsWeb.setSaltIdStride(saltIdStrideEdit.getValue().intValue());
		componentSettingsWeb.setKeyServerSettings(keyServerSettingsWeb);

		ParameterManagerSettingsWeb parameterManagerSettingsWeb = new ParameterManagerSettingsWeb();
		parameterManagerSettingsWeb.setServerAddress(parameterManagerAddressEdit.getValue());
		componentSettingsWeb.setParameterManagerSettings(parameterManagerSettingsWeb);

		DataIntegratorSettingsWeb dataIntegratorSettingsWeb = new DataIntegratorSettingsWeb();
		dataIntegratorSettingsWeb.setServerAddress(dataIntegratorAddressEdit.getValue());
		componentSettingsWeb.setDataIntegratorSettings(dataIntegratorSettingsWeb);

		controller.handleEvent(new AppEvent(AppEvents.ComponentSettingsSave, componentSettingsWeb));
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.ComponentSettingsRequest));
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Component Settings");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/table_gear.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(190);
		formLayout.setDefaultWidth(190);
		cp.setLayout(formLayout);
//		cp.setLayout(new FillLayout());
		cp.setSize(400, 320);

/*		ContentPanel groupBox = new ContentPanel();
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(90);
		formLayout.setDefaultWidth(155);
		groupBox.setLayout(formLayout);
		groupBox.setHeading("KeyServer Settings");*/
//		CaptionPanel groupBox = new CaptionPanel();
//		groupBox.setCaptionText("KeyServer Settings");

		keyserverAddressEdit.setFieldLabel("KeyServer Address");
		keyserverAddressEdit.setValidator(new ServerAddressValidator());
		cp.add(keyserverAddressEdit);

		numberOfSaltsEdit.setFieldLabel("Number of salts");
		numberOfSaltsEdit.setAllowBlank(false);
		numberOfSaltsEdit.setAllowNegative(false);
		numberOfSaltsEdit.setMaxValue(100);
		cp.add(numberOfSaltsEdit);

		saltIdStartEdit.setFieldLabel("Salt ID Start");
		saltIdStartEdit.setAllowBlank(false);
		saltIdStartEdit.setAllowNegative(false);
		cp.add(saltIdStartEdit);

		saltIdStrideEdit.setFieldLabel("Salt ID Stride");
		saltIdStrideEdit.setAllowBlank(false);
		saltIdStrideEdit.setAllowNegative(false);
		cp.add(saltIdStrideEdit);
//		cp.add(groupBox);

		parameterManagerAddressEdit.setFieldLabel("Parameter Manager Address");
		parameterManagerAddressEdit.setValidator(new ServerAddressValidator());
		cp.add(parameterManagerAddressEdit);

//		cp.add(groupBox);

/*		groupBox = new ContentPanel();
		formLayout = new FormLayout();
		formLayout.setLabelWidth(90);
		formLayout.setDefaultWidth(155);
		groupBox.setLayout(formLayout);
		groupBox.setHeading("Data Integrator Settings");*/
/*		groupBox = new CaptionPanel();
		groupBox.setCaptionText("Data Integrator Settings");*/

		dataIntegratorAddressEdit.setFieldLabel("Data Integrator Address");
		dataIntegratorAddressEdit.setValidator(new ServerAddressValidator());
		cp.add(dataIntegratorAddressEdit);

//		cp.add(groupBox);

		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);
		Button saveButton =
			new Button(inWizardMode ? Constants.NEXT_PAGE_WIZARD_BUTTON_TEXT : Constants.SAVE_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			Button sourceButton = ce.getButton();
		  			ComponentSettingsView componentSettingsView = (ComponentSettingsView)sourceButton.getData("this");
		  			componentSettingsView.saveSettings();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			if (inWizard)
						Dispatcher.get().dispatch(AppEvents.BloomfilterSettingsView);
		  		}
		    });
		saveButton.setData("this", this);
		saveButton.setData("inWizardMode", inWizardMode);
	    c.add(saveButton, layoutData);

	    if (inWizardMode) {
			Button exitButton =
				new Button(Constants.EXIT_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
						Dispatcher.get().dispatch(AppEvents.ExitWizardSelected);
			  		}
			    });
			exitButton.setData("this", this);
		    c.add(exitButton, layoutData);
	    }
		
		cp.setBottomComponent(c);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}

}
