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
import org.openempi.webapp.client.model.DatasetWeb;
import org.openempi.webapp.client.mvc.AbstractController;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatasetController extends AbstractController
{
	private DatasetView datasetView;
	
	public DatasetController() {
		this.registerEventTypes(AppEvents.DatasetListView);
		this.registerEventTypes(AppEvents.DatasetListUpdateForImport);
		this.registerEventTypes(AppEvents.DatasetListSaveToFile);
		this.registerEventTypes(AppEvents.DatasetListShowColumnsRequest);
		this.registerEventTypes(AppEvents.FileEntryRemove);
		this.registerEventTypes(AppEvents.FileEntryDelete);
		this.registerEventTypes(AppEvents.FileEntryPreImport);
		this.registerEventTypes(AppEvents.FileEntryImport);
		this.registerEventTypes(AppEvents.FileEntryPostImport);
		this.registerEventTypes(AppEvents.DatasetSend);
		this.registerEventTypes(AppEvents.AfterDatasetSend);
		this.registerEventTypes(AppEvents.FileLoaderConfigNeedsKeyServerRequest);
		this.registerEventTypes(AppEvents.FileLoaderConfigNeedsKeyServerResponse);
	}

	public void initialize() {
		datasetView = new DatasetView(this);
	}	

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.DatasetListView) {
			updateDatasetData(false);
			forwardToView(datasetView, event);
		} else if (type == AppEvents.DatasetListUpdateForImport) {
			updateDatasetData(true);
		} else if (type == AppEvents.DatasetListSaveToFile) {
			DatasetWeb dataset = (DatasetWeb)event.getData();
			saveToFileDatasetData(dataset);
		} else if (type == AppEvents.DatasetListShowColumnsRequest) {
			String tableName = (String)event.getData();
			getColumnInformationForDataset(tableName);
		} else if (type == AppEvents.FileEntryRemove) {
			List<DatasetWeb> fileList = event.getData();
			removeDatasetEntries(fileList);
		} else if (type == AppEvents.FileEntryDelete) {
			List<DatasetWeb> fileList = event.getData();
			deleteDatasetEntries(fileList);
		} else if (type == AppEvents.FileEntryImport) {
			List<Object> params = event.getData();
			String tableName = (String)params.get(0);
			DatasetWeb selectedDataset = (DatasetWeb)params.get(1);
			importDatasetEntry(selectedDataset, tableName,
					(String)params.get(2), (String)params.get(3));
		} else if (type == AppEvents.DatasetSend) {
			List<Object> params = event.getData();
			String recordLinkageProtocolName = (String)params.get(0);
			String tableName = (String)params.get(1);
			if (tableName.length() <= 0) {
				Info.display("Information", "Please specify table name.");
				return;
			}
			DatasetWeb selectedDataset = (DatasetWeb)params.get(2);
			initiateRecordLinkage(recordLinkageProtocolName, selectedDataset, tableName,
						(String)params.get(3), (String)params.get(4),
						(String)params.get(5), (String)params.get(6),
						(String)params.get(7), (String)params.get(8),
						(String)params.get(9));
		} else if (type == AppEvents.FileLoaderConfigNeedsKeyServerRequest) {
			requestIfFileLoaderConfigurationDataRequiresKeyServerAuth();
		}
	}
	
	private void saveToFileDatasetData(DatasetWeb dataset) {
		getPersonDataService().saveToFileDataset(dataset, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(Void value) {
			}
		});
	}

	private void getColumnInformationForDataset(String tableName) {
		getPersonDataService().getColumnInformation(tableName, new AsyncCallback<List<ColumnInformationWeb>>() {
		    public void onFailure(Throwable caught) {
		    	Dispatcher.forwardEvent(AppEvents.Error, caught);
		    }

		    public void onSuccess(List<ColumnInformationWeb> result) {
		    	GWT.log("column information requested for table", null);
				forwardToView(datasetView, AppEvents.DatasetListColumnInformationsArrived, result);
		    }
		});
	}
	

	private void removeDatasetEntries(List<DatasetWeb> datasetEntries) {
		for (DatasetWeb dataset : datasetEntries) {
			getPersonDataService().removeDataset(dataset.getDatasetId(), new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					Dispatcher.forwardEvent(AppEvents.Error, caught);
				}

				public void onSuccess(Void value) {
				}
			});
		}
		updateDatasetData(false);
	}

	private void deleteDatasetEntries(List<DatasetWeb> datasetEntries) {
		for (DatasetWeb dataset : datasetEntries)
			deleteDatasetEntry(dataset);
	}
	
	private void deleteDatasetEntry(DatasetWeb datasetEntry) {
		getPersonDataService().deleteDataset(datasetEntry.getDatasetId(), new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(Void value) {
			}
		});
	}
	
	private void importDatasetEntry(final DatasetWeb dataset, String tableName,
			String keyServerUserName, String keyServerPassword)
	{
		getPersonDataService().importDataset(dataset, tableName, keyServerUserName,
				keyServerPassword, new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(String result) {
				if (result != null && result.length() > 0) {
					Info.display("Information", result);
				}
				updateDatasetData(false);
				forwardToView(datasetView, AppEvents.FileEntryPostImport, result);
			}
		});
	}
	
	private void initiateRecordLinkage(String recordLinkageProtocolName,
			DatasetWeb dataset, String tableName, String matchName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword)
	{
		getPersonDataService().initiateRecordLinkage(recordLinkageProtocolName, dataset, tableName, matchName,
				keyServerUserName, keyServerPassword, dataIntegratorUserName, dataIntegratorPassword,
				parameterManagerUserName, parameterManagerPassword, new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(String value) {
				if (value != null && value.length() > 0) {
					Info.display("Information", value);
					forwardToView(datasetView, AppEvents.AfterDatasetSend, value);
				}
			}
		});
	}
	
	private void updateDatasetData(final boolean initiateImportAfter) {
		getPersonDataService().getDatasets(Constants.DEFAULT_ADMIN_USERNAME, new AsyncCallback<List<DatasetWeb>>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(List<DatasetWeb> result) {
				forwardToView(datasetView, AppEvents.DatasetListRender, result);
				if (initiateImportAfter) {
					forwardToView(datasetView, AppEvents.FileEntryPreImport, new ArrayList<Object>());
				}
			}
		});		
	}

	private void requestIfFileLoaderConfigurationDataRequiresKeyServerAuth() {
		getPersonDataService().doesCurrentLoaderConfigurationNeedKeyServer(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				Dispatcher.forwardEvent(AppEvents.Error, caught);
			}

			public void onSuccess(Boolean result) {
				forwardToView(datasetView, AppEvents.FileLoaderConfigNeedsKeyServerResponse, result);
			}
		});
	}

}
