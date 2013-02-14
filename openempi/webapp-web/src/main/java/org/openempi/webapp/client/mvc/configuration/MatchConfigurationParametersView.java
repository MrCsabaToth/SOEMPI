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
package org.openempi.webapp.client.mvc.configuration;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.AdminConfigurationWeb;
import org.openempi.webapp.client.model.AdminConfigurationWeb.ComponentTypeWeb;
import org.openempi.webapp.client.model.EMSettingsWeb;
import org.openempi.webapp.client.model.MatchConfigurationWeb;

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
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class MatchConfigurationParametersView extends View
{
	private NumberField falseNegativeProbabilityEdit = new NumberField();
	private NumberField falsePositiveProbabilityEdit = new NumberField();
	private NumberField mInitialEdit = new NumberField();
	private NumberField uInitialEdit = new NumberField();
	private NumberField pInitialEdit = new NumberField();
	private NumberField convergenceErrorEdit = new NumberField();
	private NumberField maxIterationsEdit = new NumberField();
	private NumberField maxTriesEdit = new NumberField();

	private LayoutContainer container;
	
	public MatchConfigurationParametersView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.MatchConfigurationParametersView) {
			initUI();
		} else if (event.getType() == AppEvents.MatchConfigurationParametersReceived) {
			MatchConfigurationWeb config = (MatchConfigurationWeb) event.getData();
			falseNegativeProbabilityEdit.setValue(config.getFalseNegativeProbability());
			falsePositiveProbabilityEdit.setValue(config.getFalsePositiveProbability());
			EMSettingsWeb emsWeb = config.getEMSettings();
			mInitialEdit.setValue(emsWeb.getMInitial());
			uInitialEdit.setValue(emsWeb.getUInitial());
			pInitialEdit.setValue(emsWeb.getPInitial());
			convergenceErrorEdit.setValue(emsWeb.getConvergenceError());
			maxIterationsEdit.setValue(emsWeb.getMaxIterations());
			maxTriesEdit.setValue(emsWeb.getMaxTries());
		}
	}

	protected void saveConfiguration()
	{
		MatchConfigurationWeb matchConfig = new MatchConfigurationWeb();
		matchConfig.setFalseNegativeProbability(falseNegativeProbabilityEdit.getValue().doubleValue());
		matchConfig.setFalsePositiveProbability(falsePositiveProbabilityEdit.getValue().doubleValue());
		EMSettingsWeb emsWeb = new EMSettingsWeb();
		emsWeb.setMInitial(mInitialEdit.getValue().doubleValue());
		emsWeb.setUInitial(uInitialEdit.getValue().doubleValue());
		emsWeb.setPInitial(pInitialEdit.getValue().doubleValue());
		emsWeb.setConvergenceError(convergenceErrorEdit.getValue().doubleValue());
		emsWeb.setMaxIterations(maxIterationsEdit.getValue().intValue());
		emsWeb.setMaxTries(maxTriesEdit.getValue().intValue());
		controller.handleEvent(new AppEvent(AppEvents.MatchConfigurationParametersSave, matchConfig));
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.MatchConfigurationParametersRequest));
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Match Parameter Settings");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/table_gear.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(190);
		formLayout.setDefaultWidth(190);
		cp.setLayout(formLayout);
//		cp.setLayout(new FillLayout());
		cp.setSize(400, 320);

		falseNegativeProbabilityEdit.setFieldLabel("False negative Prob.");
		falseNegativeProbabilityEdit.setAllowBlank(false);
		falseNegativeProbabilityEdit.setAllowNegative(false);
		falseNegativeProbabilityEdit.setMaxValue(1.0);
		falseNegativeProbabilityEdit.setFormat(NumberFormat.getDecimalFormat());
		cp.add(falseNegativeProbabilityEdit);

		falsePositiveProbabilityEdit.setFieldLabel("False positive Prob.");
		falsePositiveProbabilityEdit.setAllowBlank(false);
		falsePositiveProbabilityEdit.setAllowNegative(false);
		falsePositiveProbabilityEdit.setMaxValue(1.0);
		falsePositiveProbabilityEdit.setFormat(NumberFormat.getDecimalFormat());
		cp.add(falsePositiveProbabilityEdit);

		mInitialEdit.setFieldLabel("M Initial");
		mInitialEdit.setAllowBlank(false);
		mInitialEdit.setAllowNegative(false);
		mInitialEdit.setMaxValue(1.0);
		mInitialEdit.setFormat(NumberFormat.getDecimalFormat());
		cp.add(mInitialEdit);

		uInitialEdit.setFieldLabel("U Initial");
		uInitialEdit.setAllowBlank(false);
		uInitialEdit.setAllowNegative(false);
		uInitialEdit.setMaxValue(1.0);
		uInitialEdit.setFormat(NumberFormat.getDecimalFormat());
		cp.add(uInitialEdit);

		pInitialEdit.setFieldLabel("P Initial");
		pInitialEdit.setAllowBlank(false);
		pInitialEdit.setAllowNegative(false);
		pInitialEdit.setMaxValue(1.0);
		pInitialEdit.setFormat(NumberFormat.getDecimalFormat());
		cp.add(pInitialEdit);

		convergenceErrorEdit.setFieldLabel("Convergence Error");
		convergenceErrorEdit.setAllowBlank(false);
		convergenceErrorEdit.setAllowNegative(false);
		convergenceErrorEdit.setMaxValue(1.0);
		convergenceErrorEdit.setFormat(NumberFormat.getScientificFormat());
		cp.add(convergenceErrorEdit);

		maxIterationsEdit.setFieldLabel("Max Iterations");
		maxIterationsEdit.setAllowBlank(false);
		maxIterationsEdit.setAllowNegative(false);
		maxIterationsEdit.setMinValue(1);
		cp.add(maxIterationsEdit);

		maxTriesEdit.setFieldLabel("Max Tries");
		maxTriesEdit.setAllowBlank(false);
		maxTriesEdit.setAllowNegative(false);
		maxTriesEdit.setMinValue(1);
		cp.add(maxTriesEdit);

		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.RECORD_LINKAGE_WIZARD_MODE);
		AdminConfigurationWeb adminConfiguration = (AdminConfigurationWeb) Registry.get(Constants.ADMIN_CONFIGURATION);
		ComponentTypeWeb componentType = adminConfiguration.getComponentMode();

		if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			MatchConfigurationParametersView matchConfigurationParametersView = (MatchConfigurationParametersView)sourceButton.getData("this");
			  			matchConfigurationParametersView.saveConfiguration();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.MatchFieldConfigurationView);
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
		  			MatchConfigurationParametersView matchConfigurationParametersView = (MatchConfigurationParametersView)sourceButton.getData("this");
		  			matchConfigurationParametersView.saveConfiguration();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			ComponentTypeWeb componentTyp = (ComponentTypeWeb)sourceButton.getData("componentType");
		  			if (inWizard) {
		  				if (componentTyp == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE)
		  					Dispatcher.get().dispatch(AppEvents.MatchView);
		  				else if (componentTyp == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE)
		  					Dispatcher.get().dispatch(AppEvents.DatasetListView);
		  			}
		  		}
			});
		saveButton.setData("this", this);
		saveButton.setData("inWizardMode", inWizardMode);
		saveButton.setData("componentType", componentType);
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
