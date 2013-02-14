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
package org.openempi.webapp.client.mvc.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.RealSearchCriteria;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RealSearchController extends AbstractController
{
	private RealSearchView realSearchView;
	
	public RealSearchController() {
		this.registerEventTypes(AppEvents.RealSearchView);
		this.registerEventTypes(AppEvents.RealSearchInitiate);
		this.registerEventTypes(AppEvents.LeftDatasetSelected);
		this.registerEventTypes(AppEvents.LeftDatasetColumnNamesArrived);
	}

	public void initialize() {
		realSearchView = new RealSearchView(this);
	}
	
	public void search(String tableName, RealSearchCriteria search) {
		final PersonWeb personParam = new PersonWeb();
		String attributeName = search.getAttributeName();
		String attributeValue = search.getAttributeValue();
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put(attributeName, attributeValue);
		personParam.setProperties(properties);
	    getPersonDataService().getPersonsByExample(tableName, personParam, new AsyncCallback<List<PersonWeb>>() {
	      public void onFailure(Throwable caught) {
	        Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<PersonWeb> result) {
	    	  GWT.log("Result has " + result.size() + " records.", null);
	    	  for (PersonWeb person : result) {
				GWT.log("Found a person " + person.getPersonId() + ", " + person.toString(), null);
	    	  }
	    	  forwardToView(realSearchView, AppEvents.RealSearchRenderData, result);
//	    	  Info.display("Information", "We've got the persons: " + result);
	      }
	    });		
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.RealSearchView || type == AppEvents.LeftDatasetSelected) {
			forwardToView(realSearchView, event);
		} else if (type == AppEvents.RealSearchInitiate) {
			List<Object> params = event.getData();
			String tableName = (String)params.get(0);
	    	RealSearchCriteria searchCriteria = (RealSearchCriteria)params.get(1);
	    	search(tableName, searchCriteria);
		} else if (type == AppEvents.LeftDatasetColumnNamesArrived) {
			forwardToView(realSearchView, event);
	    }
	}
}
