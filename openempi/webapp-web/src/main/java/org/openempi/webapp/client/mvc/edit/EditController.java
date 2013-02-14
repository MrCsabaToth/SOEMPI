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
package org.openempi.webapp.client.mvc.edit;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditController extends AbstractController
{
	private AddPersonView addPersonView;
	
	public EditController() {
		this.registerEventTypes(AppEvents.AddPersonView);
		this.registerEventTypes(AppEvents.AddPersonInitiate);
		this.registerEventTypes(AppEvents.AddPersonComplete);
		this.registerEventTypes(AppEvents.LeftDatasetSelected);
	}

	@Override
	protected void initialize() {
		addPersonView = new AddPersonView(this);
	}
	
	public void addPersonToRepository(String tableName, PersonWeb person) {
		Info.display("Information", "Submitting request to add person to repository.");
	    getPersonDataService().addPerson(tableName, person, new AsyncCallback<String> () {
		      public void onFailure(Throwable caught) {
		        Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(String result) {
		    	  GWT.log("Result from add operation is message :" + result + ":", null);
		    	  forwardToView(addPersonView, AppEvents.AddPersonComplete, result);
		    	  Info.display("Information", "Result from add operation is message: " + result);
		      }
		    });		
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.AddPersonView ||
			type == AppEvents.AddPersonComplete ||
			type == AppEvents.LeftDatasetSelected)
		{
			forwardToView(addPersonView, event);
		} else if (type == AppEvents.AddPersonInitiate) {
			List<Object> params = event.getData();
			String tableName = (String)params.get(0);
	    	PersonWeb personWeb = (PersonWeb)params.get(1);
	    	addPersonToRepository(tableName, personWeb);
	    }
	}

}
