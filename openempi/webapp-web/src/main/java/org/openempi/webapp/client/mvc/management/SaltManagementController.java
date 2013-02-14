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
package org.openempi.webapp.client.mvc.management;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SaltManagementController extends AbstractController
{
	private SaltManagementView saltManagementView;
	
	public SaltManagementController() {
		this.registerEventTypes(AppEvents.SaltManagementView);
		this.registerEventTypes(AppEvents.SaltManagementAddInitiate);
		this.registerEventTypes(AppEvents.SaltManagementAddRenderData);
	}

	public void initialize() {
		saltManagementView = new SaltManagementView(this);
	}
	
	public void addSalts() {
	    getSaltDataService().addSalts(new AsyncCallback<Integer>() {
	      public void onFailure(Throwable caught) {
	    		Info.display("Failure", "Failure while adding salts");
	    		Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(Integer result) {
				GWT.log("Generated " + result + " salts: ", null);
	    		forwardToView(saltManagementView, AppEvents.SaltManagementAddRenderData, result);
	    		Info.display("Information", "We've added " + result + " salts");
	      }
	    });
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.SaltManagementView) {
			forwardToView(saltManagementView, event);
		} else if (type == AppEvents.SaltManagementAddInitiate) {
	    	addSalts();
	    }
	}
}
