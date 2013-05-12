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
package org.openempi.webapp.client.widget;

import org.openempi.webapp.client.Constants;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Timer;

public class ComponentLoginDialog extends Dialog {

	public enum LoginDialogMode {
		Inactive,
		KeyServerOnly,
		DataIntegratorLogin,	// Data Integrator
		PRL2Login,	// Key Server + Data Integrator
		PRL3Login	// Key Server + Parameter Manager + Data Integrator
	}
	
	protected Label keyServerLabel;
	protected TextField<String> keyServerUserName;
	protected TextField<String> keyServerPassword;
	protected Label dataIntegratorLabel;
	protected TextField<String> dataIntegratorUserName;
	protected TextField<String> dataIntegratorPassword;
	protected Label parameterManagerLabel;
	protected TextField<String> parameterManagerUserName;
	protected TextField<String> parameterManagerPassword;
	protected Button reset;
	protected Button login;
	protected Status status;
	protected LoginDialogMode mode;
	protected Boolean needsKeyServerAuth;
	private Controller controller;

	public ComponentLoginDialog(Controller controller) {
		this.mode = LoginDialogMode.Inactive;
		this.needsKeyServerAuth = true;
		this.controller = controller;
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtonAlign(HorizontalAlignment.LEFT);
		setButtons("");
		setIcon(IconHelper.createStyle("user"));
		setHeading("Credentials Required");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(300);
		setResizable(false);

		KeyListener keyListener = new KeyListener() {
			public void componentKeyUp(ComponentEvent event) {
				validate();
			}
		};

		keyServerLabel = new Label("KeyServer Credentials");
		add(keyServerLabel);
		
		keyServerUserName = new TextField<String>();
		keyServerUserName.setMinLength(4);
		keyServerUserName.setFieldLabel("Username");
		keyServerUserName.addKeyListener(keyListener);
		keyServerUserName.setValue(Constants.DEFAULT_ADMIN_USERNAME);
		add(keyServerUserName);

		keyServerPassword = new TextField<String>();
		keyServerPassword.setMinLength(4);
		keyServerPassword.setPassword(true);
		keyServerPassword.setFieldLabel("Password");
		keyServerPassword.addKeyListener(keyListener);
		keyServerPassword.setValue(Constants.DEFAULT_ADMIN_PASSWORD);
		add(keyServerPassword);

		dataIntegratorLabel = new Label("Data Integrator Credentials");
		add(dataIntegratorLabel);
		
		dataIntegratorUserName = new TextField<String>();
		dataIntegratorUserName.setMinLength(4);
		dataIntegratorUserName.setFieldLabel("Username");
		dataIntegratorUserName.addKeyListener(keyListener);
		dataIntegratorUserName.setValue(Constants.DEFAULT_ADMIN_USERNAME);
		add(dataIntegratorUserName);

		dataIntegratorPassword = new TextField<String>();
		dataIntegratorPassword.setMinLength(4);
		dataIntegratorPassword.setPassword(true);
		dataIntegratorPassword.setFieldLabel("Password");
		dataIntegratorPassword.addKeyListener(keyListener);
		dataIntegratorPassword.setValue(Constants.DEFAULT_ADMIN_PASSWORD);
		add(dataIntegratorPassword);

		parameterManagerLabel = new Label("Parameter Manager Credentials");
		add(parameterManagerLabel);
		
		parameterManagerUserName = new TextField<String>();
		parameterManagerUserName.setMinLength(4);
		parameterManagerUserName.setFieldLabel("Username");
		parameterManagerUserName.addKeyListener(keyListener);
		parameterManagerUserName.setValue(Constants.DEFAULT_ADMIN_USERNAME);
		add(parameterManagerUserName);

		parameterManagerPassword = new TextField<String>();
		parameterManagerPassword.setMinLength(4);
		parameterManagerPassword.setPassword(true);
		parameterManagerPassword.setFieldLabel("Password");
		parameterManagerPassword.addKeyListener(keyListener);
		parameterManagerPassword.setValue(Constants.DEFAULT_ADMIN_PASSWORD);
		add(parameterManagerPassword);

		// setFocusWidget(userName);
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
				keyServerUserName.reset();
				keyServerPassword.reset();
				dataIntegratorUserName.reset();
				dataIntegratorPassword.reset();
				parameterManagerUserName.reset();
				parameterManagerPassword.reset();
				validate();
				dataIntegratorUserName.focus();
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
				ComponentLoginDialog.this.hide();
			}

		};
		t.schedule(1000);
	}

	protected boolean hasValue(TextField<String> field) {
		return field.getValue() != null && field.getValue().length() > 0;
	}

	protected void validate() {
		if (mode == LoginDialogMode.Inactive)
			login.setEnabled(false);
		boolean keyServerPartOk = true;
		boolean dataIntegratorPartOk = true;
		boolean parameterManagerPartOk = true;
		boolean needsKeyServerLogin = (mode != LoginDialogMode.DataIntegratorLogin || getNeedsKeyServerAuth());
		if (needsKeyServerLogin)
			keyServerPartOk = (hasValue(keyServerUserName) &&
								hasValue(keyServerPassword) &&
								keyServerPassword.getValue().length() > 3);
		if (!getNeedsKeyServerAuth()) {
			if (mode != LoginDialogMode.KeyServerOnly)
				dataIntegratorPartOk = (hasValue(dataIntegratorUserName) &&
										hasValue(dataIntegratorPassword) &&
										dataIntegratorPassword.getValue().length() > 3);
			if (mode == LoginDialogMode.PRL3Login)
				parameterManagerPartOk = (hasValue(parameterManagerUserName) &&
										hasValue(parameterManagerPassword) &&
										parameterManagerPassword.getValue().length() > 3);
		}
		login.setEnabled(keyServerPartOk && dataIntegratorPartOk && parameterManagerPartOk);
	}
	
	public String getKeyServerUserName()
	{
		return keyServerUserName.getValue();
	}

	public String getKeyServerPassword()
	{
		return keyServerPassword.getValue();
	}

	public String getDataIntegratorUserName()
	{
		return dataIntegratorUserName.getValue();
	}

	public String getDataIntegratorPassword()
	{
		return dataIntegratorPassword.getValue();
	}

	public String getParameterManagerUserName()
	{
		return parameterManagerUserName.getValue();
	}

	public String getParameterManagerPassword()
	{
		return parameterManagerPassword.getValue();
	}

	public void resetControls()
	{
		keyServerUserName.reset();
		keyServerPassword.reset();
		dataIntegratorUserName.reset();
		dataIntegratorPassword.reset();
		parameterManagerUserName.reset();
		parameterManagerPassword.reset();
	}

	public LoginDialogMode getMode()
	{
		return mode;
	}

	public void startMode(LoginDialogMode mode) {
		this.mode = mode;
		boolean diStatus = (mode == LoginDialogMode.DataIntegratorLogin ||
							mode == LoginDialogMode.PRL2Login ||
							mode == LoginDialogMode.PRL3Login);
		dataIntegratorUserName.setEnabled(diStatus);
		dataIntegratorPassword.setEnabled(diStatus);
		parameterManagerUserName.setEnabled(mode == LoginDialogMode.PRL2Login);
		parameterManagerPassword.setEnabled(mode == LoginDialogMode.PRL3Login);
		if (mode == LoginDialogMode.Inactive || (!getNeedsKeyServerAuth() && mode == LoginDialogMode.KeyServerOnly)) {
			keyServerUserName.setEnabled(false);
			keyServerPassword.setEnabled(false);
			status.show();
			getButtonBar().disable();
			ComponentLoginDialog.this.hide();
		} else {
			keyServerUserName.setEnabled(mode != LoginDialogMode.DataIntegratorLogin);
			keyServerPassword.setEnabled(mode != LoginDialogMode.DataIntegratorLogin);
			status.hide();
			getButtonBar().enable();
			setFocusWidget(login);
			ComponentLoginDialog.this.show();
		}
	}

	public Boolean getNeedsKeyServerAuth() {
		return needsKeyServerAuth;
	}

	public void setNeedsKeyServerAuth(Boolean needsKeyServerAuth) {
		this.needsKeyServerAuth = needsKeyServerAuth;
	}

}
