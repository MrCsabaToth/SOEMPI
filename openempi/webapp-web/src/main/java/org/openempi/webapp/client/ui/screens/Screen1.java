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

import java.util.ArrayList;

import org.openempi.webapp.client.ClientState;
import org.openempi.webapp.client.ServiceRegistry;
import org.openempi.webapp.client.domain.Candidate;
import org.openempi.webapp.client.ui.widget.Pane;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Screen1 extends Pane {

	private ListBox listBox;

	public Screen1(ClientState clientState, ServiceRegistry serviceRegistry) {
        super(clientState, serviceRegistry);
        
		//containing panel
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		horizontalPanel.setWidth("25%");

		// buttons
		VerticalPanel buttonsPanel = new VerticalPanel();
		buttonsPanel.setSpacing(18);
		Button hiButton = new Button("Say Hi...");
		hiButton.setWidth("100");
		Button complimentMeButton = new Button("Compliment");
		complimentMeButton.setWidth("100");

		buttonsPanel.add(hiButton);
		buttonsPanel.add(complimentMeButton);

		horizontalPanel.add(buttonsPanel);

		// list
		VerticalPanel listPanel = new VerticalPanel();
		listPanel.add(new Label("Candidates"));
		horizontalPanel.add(listPanel);
		listBox = new ListBox();
		populateCandidatesList();
		listBox.setVisibleItemCount(10);

		listPanel.add(listBox);
		
		Button deleteButton = new Button("Delete...");
		deleteButton.setWidth("100");

		listPanel.add(deleteButton);
		
		//button listeners
		complimentMeButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				callSimpleRemoteService();
			}
		});
		
		hiButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (listBox.getSelectedIndex() == -1) {
					callTestService("Hi Nobody");
				}
				else {	
					callTestService("Hi " + listBox.getItemText(listBox.getSelectedIndex()));
				}
			}
		});
		
		deleteButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				listBox.removeItem(listBox.getSelectedIndex());
			}
		});
		
		
		initWidget(horizontalPanel);
	}

	void callTestService(String message) {
		 getServiceRegistry().getTestService().myMethod(message, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("RPC failed.");
			}
			public void onSuccess(String result) {
				Window.alert("Recieved " + result);
			}
		});
	}
	
	void callSimpleRemoteService() {
		getServiceRegistry().getSampleRemoteService().doComplimentMe(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("RPC failed.");
			}
			public void onSuccess(String result) {
				Window.alert("Recieved " + result);
			}
		});
	}
	
	void populateCandidatesList() {
		getServiceRegistry().getTestService().getCandidates(new AsyncCallback<ArrayList<Candidate>>() {
			public void onFailure(Throwable caught) {
				Window.alert("RPC failed.");
			}

			public void onSuccess(ArrayList<Candidate> candidates) {
				for (Candidate candidate : candidates) {
					listBox.addItem(candidate.getName());
				}
			}
		});
	}
	

}
