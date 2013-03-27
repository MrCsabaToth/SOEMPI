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
package org.openempi.webapp.client.mvc.user;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ColumnMatchInformationWeb;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MatchController extends AbstractController
{
	private MatchView matchView;
	
	public MatchController() {
		this.registerEventTypes(AppEvents.MatchView);
		this.registerEventTypes(AppEvents.MatchInitiate);
		this.registerEventTypes(AppEvents.LeftDatasetSelected);
		this.registerEventTypes(AppEvents.RightDatasetSelected);
		this.registerEventTypes(AppEvents.PersonMatchShowMatchColumnsRequest);
		this.registerEventTypes(AppEvents.PersonMatchShowEMResultsRequest);
		this.registerEventTypes(AppEvents.PersonMatchShowScoreChartRequest);
		this.registerEventTypes(AppEvents.PersonMatchShowRecordPairListRequest);
		this.registerEventTypes(AppEvents.PersonMatchShowPersonAttributesRequest);
	}

	public void initialize() {
		matchView = new MatchView(this);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.MatchView) {
			updateMatchData();
			forwardToView(matchView, event);
		} else if (type == AppEvents.MatchInitiate) {
			List<Object> params = event.getData();
			String linkTableName = (String)params.get(0);
			String blockingServiceName = (String)params.get(1);
			String matchingServiceName = (String)params.get(2);
			String leftTableName = (String)params.get(3);
			String rightTableName = (String)params.get(4);
			Boolean emOnly = (Boolean)params.get(5);
			match(linkTableName, leftTableName, rightTableName, blockingServiceName, matchingServiceName, emOnly);
		} else if (type == AppEvents.LeftDatasetSelected || type == AppEvents.RightDatasetSelected) {
			forwardToView(matchView, event);
		} else if (type == AppEvents.PersonMatchShowMatchColumnsRequest) {
			PersonMatchWeb personMatch = (PersonMatchWeb)event.getData();
			getColumnMatchInformationForPersonMatch(personMatch);
		} else if (type == AppEvents.PersonMatchShowEMResultsRequest) {
			PersonMatchWeb personMatch = (PersonMatchWeb)event.getData();
			getFellegiSunterParametersForPersonMatch(personMatch.getPersonMatchId());
		} else if (type == AppEvents.PersonMatchShowScoreChartRequest) {
			PersonMatchWeb personMatch = (PersonMatchWeb)event.getData();
			samplePersonLinks(personMatch.getPersonMatchId());
		} else if (type == AppEvents.PersonMatchShowRecordPairListRequest) {
			List<Object> params = event.getData();
			String tableName = (String)params.get(0);
			Long firstResult = (Long)params.get(1);
			Integer maxResults = (Integer)params.get(2);
			getPersonLinks(tableName, firstResult, maxResults);
		} else if (type == AppEvents.PersonMatchShowPersonAttributesRequest) {
			List<Object> params = event.getData();
			String tableName = (String)params.get(0);
			Long personId = (Long)params.get(1);
			getPersonAttributes(tableName, personId);
		}
	}

	private void match(String linkTableName, String leftTableName, String rightTableName,
			String blockingServiceName, String matchingServiceName, Boolean emOnly) {
		getPersonDataService().testScorePairs(linkTableName, leftTableName, rightTableName, blockingServiceName,
				matchingServiceName, emOnly, new AsyncCallback<PersonMatchWeb>()
		{
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(PersonMatchWeb result) {
				GWT.log("Result created " + result.getPersonMatchId() + " person match entry and " +
						result.getMatchTitle() + " link table", null);
				updateMatchData();
			}
		});
	}

	private void getColumnMatchInformationForPersonMatch(PersonMatchWeb personMatch) {
		getPersonDataService().getColumnMatchInformation(personMatch, new AsyncCallback<List<ColumnMatchInformationWeb>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<ColumnMatchInformationWeb> result) {
				GWT.log("Result has " + result.size() + " match column information to display", null);
				forwardToView(matchView, AppEvents.PersonMatchColumnMatchInformationsArrived, result);
			}
		});
	}

	private void getFellegiSunterParametersForPersonMatch(Integer personMatchId) {
		getPersonDataService().getFellegiSunterParameters(personMatchId, new AsyncCallback<FellegiSunterParametersWeb>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(FellegiSunterParametersWeb result) {
				GWT.log("Got result to display", null);
				forwardToView(matchView, AppEvents.PersonMatchFellegiSunterParametersArrived, result);
			}
		});
	}

	private void getPersonLinks(String tableName, Long firstResult, Integer maxResults) {
		getPersonDataService().getPersonLinks(tableName, firstResult, maxResults, new AsyncCallback<List<PersonLinkWeb>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<PersonLinkWeb> result) {
				GWT.log("Got result to display", null);
				forwardToView(matchView, AppEvents.PersonMatchRecordPairListArrived, result);
			}
		});
	}

	private void samplePersonLinks(final Integer personMatchId) {
		getPersonDataService().getFellegiSunterParameters(personMatchId, new AsyncCallback<FellegiSunterParametersWeb>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(final FellegiSunterParametersWeb result1) {
				GWT.log("Got FS parameters", null);
				getPersonDataService().samplePersonLinks(personMatchId, Constants.MAXIMUM_POINTS_ON_GRAPH, new AsyncCallback<List<PersonLinkWeb>>() {
					public void onFailure(Throwable caught) {
						Dispatcher.forwardEvent(AppEvents.Error, caught);
					}

					public void onSuccess(List<PersonLinkWeb> result2) {
						GWT.log("Got result to display", null);
						List<Object> params = new ArrayList<Object>();
						params.add(result1.getLowerBound());
						params.add(result1.getUpperBound());
						params.add(result2);
						forwardToView(matchView, AppEvents.PersonMatchScoreDataArrived, params);
					}
				});
			}
		});
	}

	private void updateMatchData() {
		getPersonDataService().getPersonMatches(Constants.DEFAULT_ADMIN_USERNAME, new AsyncCallback<List<PersonMatchWeb>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<PersonMatchWeb> result) {
				forwardToView(matchView, AppEvents.MatchRenderData, result);
			}
		});
	}

	private void getPersonAttributes(String tableName, Long personId) {
		getPersonDataService().getPersonByIdTransactionally(tableName, personId, new AsyncCallback<PersonWeb>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(PersonWeb result) {
				forwardToView(matchView, AppEvents.PersonMatchPersonAttributesArrived, result);
			}
		});
	}

}
