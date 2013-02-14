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
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.widget.ClientUtils;
import org.openempi.webapp.client.widget.ColumnInformationsDialog;
import org.openempi.webapp.client.widget.ComponentLoginDialog;
import org.openempi.webapp.client.widget.VType;
import org.openempi.webapp.client.widget.VTypeValidator;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DatasetView extends View
{
	private ListStore<DatasetWeb> store;
	private Grid<DatasetWeb> grid;
	private LayoutContainer container;
	private TextField<String> tableNameEdit;
	private TextField<String> matchNameEdit;
	private ToolBar toolBar;
	private ListStore<ModelPropertyWeb> recordLinkageProtocolNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> recordLinkageProtocolNameCombo = new ComboBox<ModelPropertyWeb>();
	private Button sendButton;
	private Button wizardButton;
	private Button importButton;
	private Button exitButton;
	private ComponentLoginDialog componentLoginDialog;
	private ColumnInformationsDialog columnInformationsDialog;
	private Status status;

	@SuppressWarnings("unchecked")
	public DatasetView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> recordLinkageProtocolNames = (List<ModelPropertyWeb>) Registry.get(Constants.RECORD_LINKAGE_PROTOCOL_NAMES);
		try {
			recordLinkageProtocolNameStore.add(recordLinkageProtocolNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.DatasetListView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.DatasetListRender) {
			displayRecords((List<DatasetWeb>) event.getData());
			Dispatcher.get().dispatch(AppEvents.FileLoaderConfigNeedsKeyServerRequest);
		} else if (event.getType() == AppEvents.FileEntryPreImport) {
			componentLoginDialog.startMode(ComponentLoginDialog.LoginDialogMode.KeyServerOnly);
			if (!componentLoginDialog.getNeedsKeyServerAuth())
				handleImportAndSendOperations();
		} else if (event.getType() == AppEvents.FileEntryPostImport) {
			dataTransferEnded();
			Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
			boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);
			if (inWizardMode) {
				toolBar.unmask();
				Dispatcher.get().dispatch(AppEvents.WizardEnded);
			}
		} else if (event.getType() == AppEvents.AfterDatasetSend) {
			dataTransferEnded();
		} else if (event.getType() == AppEvents.FileLoaderConfigNeedsKeyServerResponse) {
			Boolean doesCurrentLoaderConfigurationNeedKeyServer = (Boolean) event.getData();
			componentLoginDialog.setNeedsKeyServerAuth(doesCurrentLoaderConfigurationNeedKeyServer);
		} else if (event.getType() == AppEvents.DatasetListColumnInformationsArrived) {
			List<ColumnInformationWeb> columnInformations = (List<ColumnInformationWeb>) event.getData();
			columnInformationsDialog.setStore(columnInformations);
			columnInformationsDialog.show();
		}
	}
	
	private void dataTransferStarted() {
		status.show();
		toolBar.mask();
		recordLinkageProtocolNameCombo.mask();
		sendButton.mask();
		wizardButton.mask();
		importButton.mask();
		if (exitButton != null)
			exitButton.mask();
	}

	private void dataTransferEnded() {
		status.hide();
		toolBar.unmask();
		recordLinkageProtocolNameCombo.unmask();
		sendButton.unmask();
		wizardButton.unmask();
		importButton.unmask();
		if (exitButton != null)
			exitButton.unmask();
		componentLoginDialog.setNeedsKeyServerAuth(false);
		Dispatcher.get().dispatch(AppEvents.DatasetTableNameRefreshRequest);
	}

	private void displayRecords(List<DatasetWeb> datasets) {
		store.removeAll();
		store.add(datasets);
		container.layout();
	}

	private void handleImportAndSendOperations() {
		if (componentLoginDialog.getMode() != ComponentLoginDialog.LoginDialogMode.Inactive) {
			List<Object> params = new ArrayList<Object>();
			if (componentLoginDialog.getMode() != ComponentLoginDialog.LoginDialogMode.KeyServerOnly) {
				String recordLinkageProtocolName = ClientUtils.getSelectedStringOfComboBox(recordLinkageProtocolNameCombo);
				params.add(recordLinkageProtocolName);
			}
			String tableName = getTableName();
			if (tableName == null || tableName.length() <= 0) {
				Info.display("Information", "Please specify table name for the new dataset.");
				tableName = "";	// GWT doesn't survive null parameters :P
			}
			params.add(tableName);
			DatasetWeb selectedDataset = null;
			if (componentLoginDialog.getMode() != ComponentLoginDialog.LoginDialogMode.KeyServerOnly) {
				selectedDataset = grid.getSelectionModel().getSelectedItem();
			} else {
				List<DatasetWeb> datasetList = store.getModels();
				for (DatasetWeb dataset : datasetList) {
					if (dataset.getTableName().equals(tableName)) {
						selectedDataset = dataset;
						break;
					}
				}
			}
			if (selectedDataset == null) {
				Info.display("Information", "Please select one but only one dataset.");
				return;
			}
			params.add(selectedDataset);
			if (componentLoginDialog.getMode() != ComponentLoginDialog.LoginDialogMode.KeyServerOnly) {
				String matchName = getMatchName();
				if (matchName == null || matchName.length() <= 0) {
					if (componentLoginDialog.getMode() == ComponentLoginDialog.LoginDialogMode.PRL3Login ||
						componentLoginDialog.getMode() == ComponentLoginDialog.LoginDialogMode.PRL2Login)
					{
						Info.display("Information", "Please specify unique match name for the record linkage/PRL procedure.");
						return;
					}
					matchName = "";	// GWT doesn't survive null parameters :P
				}
				params.add(matchName);
			}
			String keyServerUserName = componentLoginDialog.getKeyServerUserName();
			if (keyServerUserName == null)
				keyServerUserName = "";	// GWT doesn't survive null parameters :P
			params.add(keyServerUserName);
			String keyServerPassword = componentLoginDialog.getKeyServerPassword();
			if (keyServerPassword == null)
				keyServerPassword = "";	// GWT doesn't survive null parameters :P
			params.add(keyServerPassword);
			if (componentLoginDialog.getMode() != ComponentLoginDialog.LoginDialogMode.KeyServerOnly) {
				String dataIntegratorUserName = componentLoginDialog.getDataIntegratorUserName();
				if (dataIntegratorUserName == null)
					dataIntegratorUserName = "";	// GWT doesn't survive null parameters :P
				params.add(dataIntegratorUserName);
				String dataIntegratorPassword = componentLoginDialog.getDataIntegratorPassword();
				if (dataIntegratorPassword == null)
					dataIntegratorPassword = "";	// GWT doesn't survive null parameters :P
				params.add(dataIntegratorPassword);
				String parameterManagerUserName = componentLoginDialog.getDataIntegratorUserName();
				if (parameterManagerUserName == null)
					parameterManagerUserName = "";	// GWT doesn't survive null parameters :P
				params.add(parameterManagerUserName);
				String parameterManagerPassword = componentLoginDialog.getParameterManagerPassword();
				if (parameterManagerPassword == null)
					parameterManagerPassword = "";	// GWT doesn't survive null parameters :P
				params.add(parameterManagerPassword);
			}
			dataTransferStarted();
			if (componentLoginDialog.getMode() == ComponentLoginDialog.LoginDialogMode.KeyServerOnly) {
				controller.handleEvent(new AppEvent(AppEvents.FileEntryImport, params));
			} else {
				controller.handleEvent(new AppEvent(AppEvents.DatasetSend, params));
			}
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI of DatasetView at " + time, null);

		componentLoginDialog = new ComponentLoginDialog(controller);
		componentLoginDialog.setClosable(false);
		componentLoginDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
			public void handleEvent(WindowEvent be) {
				handleImportAndSendOperations();
			}
		});

		columnInformationsDialog = new ColumnInformationsDialog();

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		CheckBoxSelectionModel<DatasetWeb> sm = new CheckBoxSelectionModel<DatasetWeb>();
		configs.add(sm.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(DatasetWeb.DATASET_ID);
		column.setHeader("Id");
		column.setWidth(60);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(DatasetWeb.TABLE_NAME);
		column.setHeader("Table Name");
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(DatasetWeb.FILE_NAME);
		column.setHeader("File Name");
		column.setWidth(200);
		configs.add(column);

		column = new ColumnConfig(DatasetWeb.DATE_CREATED, "Date Created", 170);
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setDateTimeFormat(DateTimeFormat.getFullDateTimeFormat());
		configs.add(column);
		
		column = new ColumnConfig(DatasetWeb.IMPORTED, "Imported?", 60);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);

		GridCellRenderer<DatasetWeb> buttonRenderer = new GridCellRenderer<DatasetWeb>() {
			private boolean init;

			public Object render(final DatasetWeb selectedDatasetParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<DatasetWeb> store, final Grid<DatasetWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<DatasetWeb>>() {
						public void handleEvent(GridEvent<DatasetWeb> be) {
							for (int i = 0; i < be.getGrid().getStore().getCount(); i++) {
								if (be.getGrid().getView().getWidget(i, be.getColIndex()) != null
									&& be.getGrid().getView().getWidget(i, be.getColIndex()) instanceof BoxComponent) {
										((BoxComponent) be.getGrid().getView().getWidget(i, be.getColIndex())).setWidth(be.getWidth() - 10);
								}
							}
						}
					});
				}

				Button b = null;
				if (property.equals(DatasetWeb.SAVE_BUTTON)) {
					b = new Button("", IconHelper.create("images/disk.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							controller.handleEvent(new AppEvent(AppEvents.DatasetListSaveToFile, selectedDatasetParam));
						}
					});
					b.setToolTip("Save dataset to a file");
				} else if (property.equals(DatasetWeb.COLUMNS_BUTTON)) {
					b = new Button("", IconHelper.create("images/application_view_columns.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							controller.handleEvent(new AppEvent(AppEvents.DatasetListShowColumnsRequest, selectedDatasetParam.getTableName()));
						}
					});
					b.setToolTip("View column properties of the dataset");
				}

				b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);

				return b;
			}
		};

		column = new ColumnConfig();
		column.setId(DatasetWeb.SAVE_BUTTON);
		column.setHeader("Save");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(DatasetWeb.COLUMNS_BUTTON);
		column.setHeader("Columns");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Dataset Management");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(1000, 500);

		toolBar = new ToolBar();
		wizardButton =
			new Button("Wizard", IconHelper.create("images/wand.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					Dispatcher.get().dispatch(AppEvents.FileImportWizardSelected);
				}
			});
		toolBar.add(wizardButton);
		cp.setTopComponent(toolBar);

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);
		if (inWizardMode)
			toolBar.mask();

		store = new ListStore<DatasetWeb>();
		grid = new Grid<DatasetWeb>(store, cm);
		grid.setAutoExpandColumn(DatasetWeb.TABLE_NAME);
		grid.setStyleAttribute("borderTop", "none");
		grid.setStripeRows(true);
		grid.setBorders(true);
		cp.add(grid);
		
		container.add(cp);
		
		final FormPanel panel = new FormPanel();
		panel.setFrame(true);
		String url = GWT.getModuleBaseURL() + "upload";
		panel.setAction(url);
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setFrame(true);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setWidth(350);

		matchNameEdit = new TextField<String>();
		matchNameEdit.setFieldLabel("Match name");
		matchNameEdit.setName(PersonMatchWeb.MATCH_TITLE);
		matchNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		panel.add(matchNameEdit);

		tableNameEdit = new TextField<String>();
		tableNameEdit.setFieldLabel("Table Name");
		tableNameEdit.setName(DatasetWeb.TABLE_NAME);
		tableNameEdit.setAllowBlank(false);
		tableNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		panel.add(tableNameEdit);

		recordLinkageProtocolNameCombo = new ComboBox<ModelPropertyWeb>();
		recordLinkageProtocolNameCombo.setEmptyText("Select record linkage protocol...");
		recordLinkageProtocolNameCombo.setFieldLabel("Linkage Protocol");
		recordLinkageProtocolNameCombo.setForceSelection(true);
		recordLinkageProtocolNameCombo.setDisplayField("name");
		recordLinkageProtocolNameCombo.setWidth(250);
		recordLinkageProtocolNameCombo.setStore(recordLinkageProtocolNameStore);
		recordLinkageProtocolNameCombo.setTypeAhead(true);
		recordLinkageProtocolNameCombo.setTriggerAction(TriggerAction.ALL);
		recordLinkageProtocolNameCombo.setEditable(false);
		panel.add(recordLinkageProtocolNameCombo);

		final FileUploadField file = new FileUploadField();
		file.setAllowBlank(false);
		file.setFieldLabel("File");
		file.setName(DatasetWeb.FILE_NAME);
		panel.add(file);

		panel.addListener(Events.Submit, new Listener<FormEvent>() {
			public void handleEvent(FormEvent be) {
				GWT.log("Event is " + be, null);
				if (!be.getResultHtml().equals("success")) {
					Info.display("Information", be.getResultHtml());
					return;
				}
				controller.handleEvent(new AppEvent(AppEvents.DatasetListUpdateForImport, be.getResultHtml()));
			}
		});
		panel.addListener(Events.BeforeSubmit, new Listener<FormEvent>() {
			public void handleEvent(FormEvent be) {
				GWT.log("Event is " + be, null);
			}			
		});

		if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
						Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
			  		}
				});
			panel.addButton(previousButton);
		}
		importButton = new Button("Import", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String fileName = file.getValue();
				if (tableNameEdit.getValue() == null || fileName == null) {
					Info.display("Information", "You must enter a table name and filename before pressing the 'Import' button");
					return;
				}
				if (fileName.length() <= 0) {
					Info.display("Information", "You must enter a filename before pressing the 'Import' button");
					return;
				}
				if (existsFileName(fileName)) {
					Info.display("Information", "You already have a file with this name. We'll use that.");
				}
				if (existsTable(tableNameEdit.getValue())) {
					Info.display("Information", "You already have a table with this name. Please choose another name.");
					return;
				}
				panel.submit();
			}
		});
		panel.addButton(importButton);
		sendButton =
			new Button("Send", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					String recordLinkageProtocolName = ClientUtils.getSelectedStringOfComboBox(recordLinkageProtocolNameCombo);
					if (recordLinkageProtocolName != null) {
						if (recordLinkageProtocolName.startsWith("TwoThird")) {
							componentLoginDialog.startMode(ComponentLoginDialog.LoginDialogMode.PRL2Login);
						} else if (recordLinkageProtocolName.startsWith("ThreeThird")) {
							componentLoginDialog.startMode(ComponentLoginDialog.LoginDialogMode.PRL3Login);
						} else {
							componentLoginDialog.startMode(ComponentLoginDialog.LoginDialogMode.DataIntegratorLogin);
						}
					} else {
						Info.display("Information", "Couldn't obtain send method");
						return;
					}
				}
			});
		panel.addButton(sendButton);
		if (inWizardMode) {
			exitButton =
				new Button(Constants.EXIT_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
						Dispatcher.get().dispatch(AppEvents.ExitWizardSelected);
			  		}
				});
			panel.addButton(exitButton);
		}

		status = new Status();
		status.setBusy("data transfer in progress...");
		panel.add(status);
		status.hide();

		cp.add(panel);
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();		
	}

	protected boolean existsFileName(String value) {
		List<DatasetWeb> datasetList = store.getModels();
		for (DatasetWeb dataset : datasetList) {
			if (dataset.getFileName().equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean existsTable(String value) {
		List<DatasetWeb> datasetList = store.getModels();
		for (DatasetWeb dataset : datasetList) {
			if (dataset.getTableName().equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	private String getTableName()
	{
		return tableNameEdit.getValue();
	}

	private String getMatchName()
	{
		return matchNameEdit.getValue();
	}

}
