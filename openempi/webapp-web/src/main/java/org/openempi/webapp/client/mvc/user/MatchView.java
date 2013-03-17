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
import org.openempi.webapp.client.model.DatasetWeb;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.widget.ClientUtils;
import org.openempi.webapp.client.widget.ColumnMatchInformationsDialog;
import org.openempi.webapp.client.widget.FellegiSunterParametersChartDialog;
import org.openempi.webapp.client.widget.MatchScoresChartDialog;
import org.openempi.webapp.client.widget.PersonLinksDialog;
import org.openempi.webapp.client.widget.VType;
import org.openempi.webapp.client.widget.VTypeValidator;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class MatchView extends View
{
	private Grid<PersonMatchWeb> grid;
	private ListStore<PersonMatchWeb> matchStore = new ListStore<PersonMatchWeb>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;

	private Status status;
	private Button matchButton;
	private TextField<String> tableNameEdit = new TextField<String>();
	private ListStore<ModelPropertyWeb> blockingServiceNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> blockingServiceNameCombo = new ComboBox<ModelPropertyWeb>();
	private ListStore<ModelPropertyWeb> matchingServiceNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> matchingServiceNameCombo = new ComboBox<ModelPropertyWeb>();
	private CheckBox emOnlyCheckBox;

	private String leftTableName;
	private String rightTableName;

	private ColumnMatchInformationsDialog columnMatchInformationsDialog;
	private PersonLinksDialog personLinksDialog;

	@SuppressWarnings("unchecked")
	public MatchView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> blockingServiceNames = (List<ModelPropertyWeb>) Registry.get(Constants.BLOCKING_SERVICE_NAMES);
		List<ModelPropertyWeb> matchingServiceNames = (List<ModelPropertyWeb>) Registry.get(Constants.MATCHING_SERVICE_NAMES);
		try {
			blockingServiceNameStore.add(blockingServiceNames);
			matchingServiceNameStore.add(matchingServiceNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.MatchView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.MatchRenderData) {
			displayMatches((List<PersonMatchWeb>) event.getData());
		} else if (event.getType() == AppEvents.LeftDatasetSelected) {
			leftTableName = (String)event.getData();
		} else if (event.getType() == AppEvents.RightDatasetSelected) {
			rightTableName = (String)event.getData();
		} else if (event.getType() == AppEvents.PersonMatchColumnMatchInformationsArrived) {
			List<ColumnMatchInformationWeb> columnMatchInformation = (List<ColumnMatchInformationWeb>)event.getData();
			columnMatchInformationsDialog.setStore(columnMatchInformation);
			columnMatchInformationsDialog.show();
		} else if (event.getType() == AppEvents.PersonMatchFellegiSunterParametersArrived) {
			FellegiSunterParametersWeb fellegiSunterParameters = (FellegiSunterParametersWeb)event.getData();
			FellegiSunterParametersChartDialog fellegiSunterParametersChartDialog =
					new FellegiSunterParametersChartDialog(fellegiSunterParameters);
			fellegiSunterParametersChartDialog.show();
		} else if (event.getType() == AppEvents.PersonMatchScoreDataArrived) {
			List<Object> params = (List<Object>)event.getData();
			Double lowerBound = (Double)params.get(0);
			Double upperBound = (Double)params.get(1);
			List<PersonLinkWeb> personLinks = (List<PersonLinkWeb>)params.get(2);
			MatchScoresChartDialog matchScoresChartDialog = new MatchScoresChartDialog(personLinks, lowerBound, upperBound);
			matchScoresChartDialog.show();
		} else if (event.getType() == AppEvents.PersonMatchRecordPairListArrived) {
			List<PersonLinkWeb> personLinks = (List<PersonLinkWeb>)event.getData();
			personLinksDialog.setStore(personLinks);
			if (!personLinksDialog.isVisible())
				personLinksDialog.show();
		} else if (event.getType() == AppEvents.PersonMatchPersonAttributesArrived) {
			PersonWeb person = (PersonWeb)event.getData();
			personLinksDialog.displayPersonAttributes(person);
		}
	}

	private void displayMatches(List<PersonMatchWeb> personMatches) {
		if (grid == null) {
			setupMatchGrid();
		}

		matchStore.removeAll();
		matchStore.add(personMatches);
		container.layout();
		status.hide();
	}

	private void setupMatchGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId(PersonMatchWeb.PERSON_MATCH_ID);
		column.setHeader("Id");
		column.setWidth(70);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.MATCH_TITLE);
		column.setHeader("Match Title");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.LEFT_DATASET_NAME);
		column.setHeader("Left Dataset Name");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.RIGHT_DATASET_NAME);
		column.setHeader("Right Dataset Name");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.TOTAL_RECORDS);
		column.setHeader("No. of pairs");
		column.setWidth(70);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.BLOOM_FILTER_K_PARAMETER);
		column.setHeader("BF K Param");
		column.setWidth(70);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.BLOOM_FILTER_FILL_FACTOR);
		column.setHeader("BF Fill Factor");
		column.setWidth(70);
		configs.add(column);

		column = new ColumnConfig(PersonMatchWeb.DATE_CREATED, "Date Created", 170);
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setDateTimeFormat(DateTimeFormat.getFullDateTimeFormat());
		configs.add(column);

		GridCellRenderer<PersonMatchWeb> buttonRenderer = new GridCellRenderer<PersonMatchWeb>() {
			private boolean init;

			public Object render(final PersonMatchWeb selectedMatchParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<PersonMatchWeb> store, final Grid<PersonMatchWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<PersonMatchWeb>>() {
						public void handleEvent(GridEvent<PersonMatchWeb> be) {
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
				if (property.equals(PersonMatchWeb.MATCH_COLUMNS_BUTTON)) {
					b = new Button("", IconHelper.create("images/application_view_columns.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowMatchColumnsRequest, selectedMatchParam));
						}
					});
					b.setToolTip("View Match Column Informations");
				} else if (property.equals(PersonMatchWeb.EM_RESULTS_BUTTON)) {
					b = new Button("", IconHelper.create("images/application_view_icons.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowEMResultsRequest, selectedMatchParam));
						}
					});
					b.setToolTip("View EM Results of the match");
				} else if (property.equals(PersonMatchWeb.SCORE_CHART_BUTTON)) {
					b = new Button("", IconHelper.create("images/application_view_icons.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowScoreChartRequest, selectedMatchParam));
						}
					});
					b.setToolTip("View score chart of the match");
				} else if (property.equals(PersonMatchWeb.RECORD_PAIRS_BUTTON)) {
					b = new Button("", IconHelper.create("images/application_view_gallery.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							personLinksDialog.setParams(selectedMatchParam.getMatchTitle(), selectedMatchParam.getLeftDatasetName(),
									selectedMatchParam.getRightDatasetName(), 0L, Constants.PERSON_LINK_PAGE_SIZE,
									selectedMatchParam.getTotalRecords());
							List<Object> params = new ArrayList<Object>();
							params.add(selectedMatchParam.getMatchTitle());
							params.add(0L);
							params.add(Constants.PERSON_LINK_PAGE_SIZE);
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowRecordPairListRequest, params));
						}
					});
					b.setToolTip("View List of Record Pairs");
				}

				b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);

				return b;
			}
		};

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.MATCH_COLUMNS_BUTTON);
		column.setHeader("COL");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.EM_RESULTS_BUTTON);
		column.setHeader("EM");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.SCORE_CHART_BUTTON);
		column.setHeader("SC");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PersonMatchWeb.RECORD_PAIRS_BUTTON);
		column.setHeader("RP");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonMatchWeb>(matchStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);

		gridContainer.add(grid);
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		columnMatchInformationsDialog = new ColumnMatchInformationsDialog();
		personLinksDialog = new PersonLinksDialog(controller);

		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		
		FormPanel panel = new FormPanel();
		panel.setFrame(true);
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);

		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

		panel.add(main, new FormData("20%"));

	 	if (inWizardMode) {
			Button previousButton =
				new Button("Previous", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
			  		}
			    });
			previousButton.setData("inWizardMode", inWizardMode);
			panel.getButtonBar().add(previousButton);
		}

		matchButton = new Button("Match", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (tableNameEdit.getValue() == null) {
					Info.display("Information", "You must enter a link table name before pressing 'Match' button");
					return;
				}
				if (existsTable(tableNameEdit.getValue())) {
					Info.display("Information", "You already have a link table with this name. Please choose another name.");
					return;
				}
				String blockingServiceName = ClientUtils.getSelectedStringOfComboBox(blockingServiceNameCombo);
				if (blockingServiceName == null) {
					Info.display("Information", "Couldn't obtain blocking service selection.");
					return;
				}
				String matchingServiceName = ClientUtils.getSelectedStringOfComboBox(matchingServiceNameCombo);
				if (matchingServiceName == null) {
					Info.display("Information", "Couldn't obtain matching service selection.");
					return;
				}
				List<Object> params = new ArrayList<Object>();
				params.add(tableNameEdit.getValue());
				params.add(blockingServiceName);
				params.add(matchingServiceName);
				params.add(leftTableName);
				params.add(rightTableName);
				params.add(emOnlyCheckBox.getValue());
				controller.handleEvent(new AppEvent(AppEvents.MatchInitiate, params));
				status.show();
				matchButton.mask();
				blockingServiceNameCombo.mask();
				matchingServiceNameCombo.mask();
				emOnlyCheckBox.mask();
				Dispatcher.get().dispatch(AppEvents.WizardEnded);
			}
		});
		status = new Status();
		status.setBusy("please wait...");
		panel.getButtonBar().add(status);
		status.hide();
		panel.getButtonBar().add(new FillToolItem());
		panel.getButtonBar().add(matchButton);

		LayoutContainer formPart1 = new LayoutContainer();
		formPart1.setLayout(new FormLayout());

		tableNameEdit.setFieldLabel("Link Table Name");
		tableNameEdit.setName(DatasetWeb.TABLE_NAME);
		tableNameEdit.setAllowBlank(false);
		tableNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		formPart1.add(tableNameEdit);

		emOnlyCheckBox = new CheckBox();
		emOnlyCheckBox.setFieldLabel("EM only (no rec. pairs)");
		emOnlyCheckBox.setBoxLabel("");
		emOnlyCheckBox.setValue(false);
		formPart1.add(emOnlyCheckBox);

		LayoutContainer formPart2 = new LayoutContainer();
		formPart2.setLayout(new FormLayout());

		blockingServiceNameCombo.setEmptyText("Select blocking service...");
		blockingServiceNameCombo.setFieldLabel("Blocking Service");
		blockingServiceNameCombo.setForceSelection(true);
		blockingServiceNameCombo.setDisplayField("name");
		blockingServiceNameCombo.setWidth(250);
		blockingServiceNameCombo.setStore(blockingServiceNameStore);
		blockingServiceNameCombo.setTypeAhead(true);
		blockingServiceNameCombo.setTriggerAction(TriggerAction.ALL);
		blockingServiceNameCombo.setEditable(false);
		formPart2.add(blockingServiceNameCombo);

		matchingServiceNameCombo.setEmptyText("Select matching service...");
		matchingServiceNameCombo.setFieldLabel("Matching Service");
		matchingServiceNameCombo.setForceSelection(true);
		matchingServiceNameCombo.setDisplayField("name");
		matchingServiceNameCombo.setWidth(250);
		matchingServiceNameCombo.setStore(matchingServiceNameStore);
		matchingServiceNameCombo.setTypeAhead(true);
		matchingServiceNameCombo.setTriggerAction(TriggerAction.ALL);
		matchingServiceNameCombo.setEditable(false);
		formPart2.add(matchingServiceNameCombo);

		emOnlyCheckBox.setValue(false);

		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 54));

		panel.getButtonBar().add(formPart1);
		panel.getButtonBar().add(formPart2);
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(75, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}

	protected boolean existsTable(String value) {
		// TODO
		return false;
	}

}
