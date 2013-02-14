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
import org.openempi.webapp.client.model.Key;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class KeyManagementController extends AbstractController
{
	private KeyManagementView keyManagementView;
	
	public KeyManagementController() {
		this.registerEventTypes(AppEvents.KeyManagementView);
		this.registerEventTypes(AppEvents.KeyManagementAddInitiate);
	}

	public void initialize() {
		keyManagementView = new KeyManagementView(this);
	}
	
	public void addKey() {
	    getKeyDataService().addKey(new AsyncCallback<Key>() {
	      public void onFailure(Throwable caught) {
	    	  	Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(Key result) {
				GWT.log("Found a key " + result.getId() +
						" publicKeyPart1: " + result.getPublicKeyPart1Length() +
						" publicKeyPart2: " + result.getPublicKeyPart2Length() +
						" publicKeyPart3: " + result.getPublicKeyPart3Length() +
						" privateKeyPart1: " + result.getPrivateKeyPart1Length() +
						" privateKeyPart2: " + result.getPrivateKeyPart2Length() +
						" privateKeyPart3: " + result.getPrivateKeyPart3Length(), null);
	    		forwardToView(keyManagementView, AppEvents.KeyManagementAddRenderData, result);
//	    		Info.display("Information", "We've got the key: " + result);
	      }
	    });
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.KeyManagementView) {
			forwardToView(keyManagementView, event);
		} else if (type == AppEvents.KeyManagementAddInitiate) {
	    	addKey();
	    }
	}
}
