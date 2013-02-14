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
import org.openempi.webapp.client.model.BloomfilterSettingsWeb;

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

public class BloomfilterSettingsView extends View
{
	private NumberField ngramSizeEdit = new NumberField();
	private NumberField defaultMEdit = new NumberField();
	private NumberField defaultKEdit = new NumberField();

	private LayoutContainer container;
	
	public BloomfilterSettingsView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.BloomfilterSettingsView) {
			initUI();
		} else if (event.getType() == AppEvents.BloomfilterSettingsReceived) {
			BloomfilterSettingsWeb config = (BloomfilterSettingsWeb) event.getData();
			ngramSizeEdit.setValue(config.getNGramSize());
			defaultMEdit.setValue(config.getDefaultM());
			defaultKEdit.setValue(config.getDefaultK());
		}
	}

	protected void saveSettings()
	{
		BloomfilterSettingsWeb bloomfilterSettings = new BloomfilterSettingsWeb();
		bloomfilterSettings.setNGramSize(ngramSizeEdit.getValue().intValue());
		bloomfilterSettings.setDefaultM(defaultMEdit.getValue().intValue());
		bloomfilterSettings.setDefaultK(defaultKEdit.getValue().intValue());
		controller.handleEvent(new AppEvent(AppEvents.BloomfilterSettingsSave, bloomfilterSettings));
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.BloomfilterSettingsRequest));
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Bloomfilter Settings");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/table_gear.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(190);
		formLayout.setDefaultWidth(190);
		cp.setLayout(formLayout);
		cp.setSize(400, 225);

		ngramSizeEdit.setFieldLabel("Default N-gram size");
		ngramSizeEdit.setAllowBlank(false);
		ngramSizeEdit.setAllowNegative(false);
		ngramSizeEdit.setMinValue(1);
		cp.add(ngramSizeEdit);

		defaultMEdit.setFieldLabel("Def.no.of bits (=m)");
		defaultMEdit.setAllowBlank(false);
		defaultMEdit.setAllowNegative(false);
		cp.add(defaultMEdit);

		defaultKEdit.setFieldLabel("Def.no.of rounds (=k)");
		defaultKEdit.setAllowBlank(false);
		defaultKEdit.setAllowNegative(false);
		cp.add(defaultKEdit);

		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);

	    if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			BloomfilterSettingsView bloomfilterSettingsView = (BloomfilterSettingsView)sourceButton.getData("this");
			  			bloomfilterSettingsView.saveSettings();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.ComponentSettingsView);
			  		}
			    });
			previousButton.setData("this", this);
			previousButton.setData("inWizardMode", inWizardMode);
			c.add(previousButton, layoutData);
	    }

		Button saveButton =
			new Button(inWizardMode ? Constants.NEXT_PAGE_WIZARD_BUTTON_TEXT : Constants.SAVE_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			Button sourceButton = ce.getButton();
		  			BloomfilterSettingsView bloomfilterSettingsView = (BloomfilterSettingsView)sourceButton.getData("this");
		  			bloomfilterSettingsView.saveSettings();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			if (inWizard)
						Dispatcher.get().dispatch(AppEvents.FileLoaderConfigurationView);
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
