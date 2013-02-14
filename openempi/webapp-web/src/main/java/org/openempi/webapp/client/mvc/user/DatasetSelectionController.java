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
import org.openempi.webapp.client.model.ColumnInformationWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatasetSelectionController extends AbstractController
{
	private DatasetSelectionView datasetSelectionView;
	
	public DatasetSelectionController() {
		this.registerEventTypes(AppEvents.DatasetSelectionView);
		this.registerEventTypes(AppEvents.LeftDatasetSelected);
		this.registerEventTypes(AppEvents.RightDatasetSelected);
		this.registerEventTypes(AppEvents.DatasetTableNameRefreshRequest);
		this.registerEventTypes(AppEvents.DatasetTableNamesArrived);
	}

	public void initialize() {
		datasetSelectionView = new DatasetSelectionView(this);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.DatasetSelectionView) {
			forwardToView(datasetSelectionView, event);
		} else if (type == AppEvents.LeftDatasetSelected) {
			String tableName = (String)event.getData();
			selectLeftDataset(tableName);
		} else if (type == AppEvents.RightDatasetSelected) {
			String tableName = (String)event.getData();
			selectRightDataset(tableName);
		} else if (type == AppEvents.DatasetTableNameRefreshRequest) {
			updateDatasetTableNames();
		} else if (type == AppEvents.DatasetTableNamesArrived) {
			forwardToView(datasetSelectionView, event);
		}
	}

	private void selectLeftDataset(String tableName) {
		getPersonDataService().getColumnInformation(tableName, new AsyncCallback<List<ColumnInformationWeb>>() {
		    public void onFailure(Throwable caught) {
		    	Dispatcher.forwardEvent(AppEvents.Error, caught);
		    }

		    public void onSuccess(List<ColumnInformationWeb> result) {
		    	GWT.log("Left table has been selected", null);
				Registry.register(Constants.LEFT_TABLE_COLUMN_SPECIFICATION, result);
				List<String> columnNames = getColumnNames(result);
				Registry.register(Constants.LEFT_TABLE_FIELD_NAMES, columnNames);
				List<ModelPropertyWeb> columnNameModelPropertyList = getColumnNameModelPropertyList(result);
				Registry.register(Constants.LEFT_TABLE_FIELD_NAME_MODEL_PROPERTY_LIST, columnNameModelPropertyList);
		    	Dispatcher.forwardEvent(AppEvents.LeftDatasetColumnNamesArrived, columnNameModelPropertyList);
		    }
		});
	}
	
	private void selectRightDataset(String tableName) {
		getPersonDataService().getColumnInformation(tableName, new AsyncCallback<List<ColumnInformationWeb>>() {
		    public void onFailure(Throwable caught) {
		    	Dispatcher.forwardEvent(AppEvents.Error, caught);
		    }

		    public void onSuccess(List<ColumnInformationWeb> result) {
		    	GWT.log("Right table has been selected", null);
				Registry.register(Constants.RIGHT_TABLE_COLUMN_SPECIFICATION, result);
				List<String> columnNames = getColumnNames(result);
				Registry.register(Constants.RIGHT_TABLE_FIELD_NAMES, columnNames);
				List<ModelPropertyWeb> columnNameModelPropertyList = getColumnNameModelPropertyList(result);
				Registry.register(Constants.RIGHT_TABLE_FIELD_NAME_MODEL_PROPERTY_LIST, columnNameModelPropertyList);
		    	Dispatcher.forwardEvent(AppEvents.RightDatasetColumnNamesArrived, columnNameModelPropertyList);
		    }
		});
	}
	
	private List<String> getColumnNames(List<ColumnInformationWeb> columnInformations) {
		List<String> columnNames = new ArrayList<String>();
		for(ColumnInformationWeb ci : columnInformations) {
			columnNames.add(new String(ci.getFieldName()));
		}
		return columnNames;
	}
	
	private List<ModelPropertyWeb> getColumnNameModelPropertyList(List<ColumnInformationWeb> columnInformations) {
		List<ModelPropertyWeb> columnNames = new ArrayList<ModelPropertyWeb>();
		for(ColumnInformationWeb ci : columnInformations) {
			columnNames.add(new ModelPropertyWeb(ci.getFieldName()));
		}
		return columnNames;
	}

	private void updateDatasetTableNames() {
		getPersonDataService().getDatasetTableNames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<String> tableNames) {
				List<ModelPropertyWeb> tableNamesRegistry = new ArrayList<ModelPropertyWeb>(tableNames.size());
				for (String name : tableNames) {
					tableNamesRegistry.add(new ModelPropertyWeb(name));
				}
				Registry.register(Constants.TABLE_NAMES, tableNamesRegistry);
				Dispatcher.forwardEvent(AppEvents.DatasetTableNamesArrived, tableNames);
			}
		});
	}

}
