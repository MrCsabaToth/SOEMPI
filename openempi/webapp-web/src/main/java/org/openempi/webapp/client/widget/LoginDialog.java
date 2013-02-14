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
package org.openempi.webapp.client.widget;

import org.openempi.webapp.client.Constants;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Timer;

public class LoginDialog extends Dialog {

	protected TextField<String> userName;
	protected TextField<String> password;
	protected RadioGroup componentModeGroup;
	protected Radio dataProviderModeRadio;
	protected Radio dataIntegratorModeRadio;
	protected Radio parameterManagerModeRadio;
	protected Radio keyServerModeRadio;
	protected CheckBox experimentalModeCheck;
	protected Button reset;
	protected Button login;
	protected Status status;
	private Controller controller;

	public LoginDialog(Controller controller) {
		this.controller = controller;
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtonAlign(HorizontalAlignment.LEFT);
		setButtons("");
		setIcon(IconHelper.createStyle("user"));
		setHeading("SOEMPI Login");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(350);
		setResizable(false);

		KeyListener keyListener = new KeyListener() {
			public void componentKeyUp(ComponentEvent event) {
				validate();
			}
		};

		userName = new TextField<String>();
		userName.setMinLength(4);
		userName.setFieldLabel("Username");
		userName.addKeyListener(keyListener);
		userName.setValue(Constants.DEFAULT_ADMIN_USERNAME);
		add(userName);

		password = new TextField<String>();
		password.setMinLength(4);
		password.setPassword(true);
		password.setFieldLabel("Password");
		password.addKeyListener(keyListener);
		password.setValue(Constants.DEFAULT_ADMIN_PASSWORD);
		add(password);

		dataProviderModeRadio = new Radio();
		dataProviderModeRadio.setBoxLabel("Data Provider");
		dataProviderModeRadio.setValue(true);

		dataIntegratorModeRadio = new Radio();
		dataIntegratorModeRadio.setBoxLabel("Data Integrator");

		parameterManagerModeRadio = new Radio();
		parameterManagerModeRadio.setBoxLabel("Parameter Manager");

		keyServerModeRadio = new Radio();
		keyServerModeRadio.setBoxLabel("Key Server");

		componentModeGroup = new RadioGroup();
		componentModeGroup.setFieldLabel("Component Mode");
		componentModeGroup.setOrientation(Orientation.VERTICAL);
		componentModeGroup.add(dataProviderModeRadio);
		componentModeGroup.add(dataIntegratorModeRadio);
		componentModeGroup.add(parameterManagerModeRadio);
		componentModeGroup.add(keyServerModeRadio);
		add(componentModeGroup);

		experimentalModeCheck = new CheckBox();
		experimentalModeCheck.setFieldLabel("Experimental Mode");
		experimentalModeCheck.setBoxLabel("Experimental Mode");
		experimentalModeCheck.setValue(true);
		add(experimentalModeCheck);

		// setFocusWidget(userName);
		setFocusWidget(login);

		componentModeGroup.disable();
		dataProviderModeRadio.disable();
		dataIntegratorModeRadio.disable();
		parameterManagerModeRadio.disable();
		keyServerModeRadio.disable();
		experimentalModeCheck.disable();
		reset.disable();
		login.disable();
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		status = new Status();
		status.setBusy("please wait...");
		status.hide();
		status.setAutoWidth(true);
		getButtonBar().add(status);

		getButtonBar().add(new FillToolItem());

		reset = new Button("Reset");
		reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				userName.reset();
				password.reset();
				validate();
				userName.focus();
			}

		});

		login = new Button("Login");
		// login.disable();
		login.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				onSubmit();
			}
		});

		addButton(reset);
		addButton(login);

	}

	protected void onSubmit() {
		status.show();
		getButtonBar().disable();
		Timer t = new Timer() {

			@Override
			public void run() {
				LoginDialog.this.hide();
			}

		};
		t.schedule(1000);
	}

	protected boolean hasValue(TextField<String> field) {
		return field.getValue() != null && field.getValue().length() > 0;
	}

	protected void validate() {
		login.setEnabled(hasValue(userName) && hasValue(password)
				&& password.getValue().length() > 3);
	}
	
	public String getUserName()
	{
		return userName.getValue();
	}

	public String getPassword()
	{
		return password.getValue();
	}

	public Integer getComponentType()
	{
		if (dataProviderModeRadio.getValue())
			return 1;
		else if (dataIntegratorModeRadio.getValue())
			return 2;
		else if (parameterManagerModeRadio.getValue())
			return 3;
		else if (keyServerModeRadio.getValue())
			return 4;
		else
			return 0;
	}
	
	public void setComponentType(Integer componentType)
	{
		if (componentType == 1)
			dataProviderModeRadio.setValue(true);
		else if (componentType == 2)
			dataIntegratorModeRadio.setValue(true);
		else if (componentType == 3)
			parameterManagerModeRadio.setValue(true);
		else if (componentType == 4)
			keyServerModeRadio.setValue(true);
	}

	public Boolean getExperimentalMode()
	{
		return experimentalModeCheck.getValue();
	}
	
	public void setExperimentalMode(Boolean experimentalMode)
	{
		experimentalModeCheck.setValue(experimentalMode);
	}

	public void enableControls()
	{
		componentModeGroup.enable();
		dataProviderModeRadio.enable();
		dataIntegratorModeRadio.enable();
		experimentalModeCheck.enable();
		parameterManagerModeRadio.enable();
		keyServerModeRadio.enable();
		reset.enable();
		login.enable();
	}

	public void resetControls()
	{
		userName.reset();
		password.reset();
	}

}
