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
package org.openempi.webapp.client.ui.screens;

import org.openempi.webapp.client.ClientState;
import org.openempi.webapp.client.ServiceRegistry;
import org.openempi.webapp.client.ui.common.ProgressBarPopup;
import org.openempi.webapp.client.ui.widget.Pane;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Screen2 extends Pane {

	private ListBox listBox;
	private ProgressBarPopup progressBarPopup;

	public Screen2(ClientState clientState, ServiceRegistry serviceRegistry) {
        super(clientState, serviceRegistry);

		//containing panel
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		horizontalPanel.setWidth("25%");

		// buttons
		VerticalPanel buttonsPanel = new VerticalPanel();
		buttonsPanel.setSpacing(18);
		Button longRunningOperationButton = new Button("Long running operation...");

		buttonsPanel.add(longRunningOperationButton);

		horizontalPanel.add(buttonsPanel);

		
		//button listeners

		longRunningOperationButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				callLongRunningService(15000);
			}
		});
		
		initWidget(horizontalPanel);
	}
	
	void callLongRunningService(int runningTime) {
        progressBarPopup = new ProgressBarPopup();
        progressBarPopup.setText("Running for " + runningTime + " millis...");
		getServiceRegistry().getTestService().longRunningMethod(runningTime, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				progressBarPopup.hide();
				Window.alert("RPC failed.");
			}
			public void onSuccess(String result) {
				progressBarPopup.hide();
				Window.alert("Recieved " + result);
			}
		});
	}
	

}
