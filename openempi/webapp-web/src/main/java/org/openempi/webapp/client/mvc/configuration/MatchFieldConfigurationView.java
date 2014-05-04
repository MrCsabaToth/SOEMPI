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
package org.openempi.webapp.client.mvc.configuration;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.FunctionFieldWeb;
import org.openempi.webapp.client.model.FunctionParameterWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.widget.ClientUtils;
import org.openempi.webapp.client.widget.FunctionParametersDialog;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class MatchFieldConfigurationView extends View
{
	private Grid<MatchFieldWeb> grid;
	private ListStore<MatchFieldWeb> store = new ListStore<MatchFieldWeb>();
	private Dialog addEditMatchFieldDialog;
	private Boolean addOrEditFieldMode = true;
	private int editedFieldIndex = 0;
	private MatchFieldWeb editedField;
	private Boolean didNotHaveParameters = false;
	private List<FunctionParameterWeb> editedFunctionParameters = null;
	private Boolean wereParametersEdited = false;

	private ListStore<ModelPropertyWeb> leftAttributeNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> leftAttributeNameCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> leftAttributeNameInPlaceCombo = new SimpleComboBox<String>();
	private ListStore<ModelPropertyWeb> rightAttributeNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> rightAttributeNameCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> rightAttributeNameInPlaceCombo = new SimpleComboBox<String>();
	private NumberField agreementProbabilityEdit = new NumberField();
	private NumberField agreementProbabilityInPlaceEdit = new NumberField();
	private NumberField disagreementProbabilityEdit = new NumberField();
	private NumberField disagreementProbabilityInPlaceEdit = new NumberField();
	private ListStore<ModelPropertyWeb> comparatorFuncNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> comparatorFuncNameCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> comparatorFuncNameInPlaceCombo = new SimpleComboBox<String>();
	private NumberField matchThresholdEdit = new NumberField();
	private NumberField matchThresholdInPlaceEdit = new NumberField();
	private Button addEditFunctionParametersButton = new Button();
	private FunctionParametersDialog functionParameterDialog;

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	
	@SuppressWarnings("unchecked")
	public MatchFieldConfigurationView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> comparatorFuncNames = (List<ModelPropertyWeb>) Registry.get(Constants.COMPARATOR_FUNCTION_NAME_MODEL_PROPERTY_LIST);
		try {
			comparatorFuncNameStore.add(comparatorFuncNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.MatchFieldConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.MatchFieldConfigurationReceived) {
			store.removeAll();
			List<MatchFieldWeb> matchFields = (List<MatchFieldWeb>) event.getData();
			store.add(matchFields);
		} else if (event.getType() == AppEvents.MatchFieldComparisonFunctionParametersEdited) {
			wereParametersEdited = true;
			editedFunctionParameters = (List<FunctionParameterWeb>) event.getData();
		} else if (event.getType() == AppEvents.LeftDatasetColumnNamesArrived) {
			List<ModelPropertyWeb> fieldNames = (List<ModelPropertyWeb>)event.getData();
			reloadLeftFieldCombos(fieldNames);
		} else if (event.getType() == AppEvents.RightDatasetColumnNamesArrived) {
			List<ModelPropertyWeb> fieldNames = (List<ModelPropertyWeb>)event.getData();
			reloadRightFieldCombos(fieldNames);
		}
	}

	private void reloadLeftFieldCombos(List<ModelPropertyWeb> fieldNames) {
		reloadFieldCombos(fieldNames, true);
	}

	private void reloadRightFieldCombos(List<ModelPropertyWeb> fieldNames) {
		reloadFieldCombos(fieldNames, false);
	}

	@SuppressWarnings("unchecked")
	private void reloadFieldCombos(List<ModelPropertyWeb> fieldNames, boolean leftOrRight) {
		// Saving selections if any
		String fieldName = null;
		if (leftOrRight)
			fieldName = ClientUtils.getSelectedStringOfComboBox(leftAttributeNameCombo);
		else
			fieldName = ClientUtils.getSelectedStringOfComboBox(rightAttributeNameCombo);
		try {
			if (leftOrRight) {
				leftAttributeNameStore.removeAll();
				leftAttributeNameStore.add(fieldNames);
				leftAttributeNameInPlaceCombo.removeAll();
				List<String> leftAttributeNames =
					(List<String>) Registry.get(Constants.LEFT_TABLE_FIELD_NAMES);
				leftAttributeNameInPlaceCombo.add(leftAttributeNames);
			} else {
				rightAttributeNameStore.removeAll();
				rightAttributeNameStore.add(fieldNames);
				rightAttributeNameInPlaceCombo.removeAll();
				List<String> rightAttributeNames =
					(List<String>) Registry.get(Constants.RIGHT_TABLE_FIELD_NAMES);
				rightAttributeNameInPlaceCombo.add(rightAttributeNames);
			}
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
		if (fieldName != null) {
			if (fieldName.length() > 0) {
				if (leftOrRight)
					leftAttributeNameCombo.select(new ModelPropertyWeb(fieldName));
				else
					rightAttributeNameCombo.select(new ModelPropertyWeb(fieldName));
			}
		}
	}

	protected void saveConfiguration()
	{
		List<MatchFieldWeb> matchFieldsConfig = grid.getStore().getModels();
		controller.handleEvent(new AppEvent(AppEvents.MatchFieldConfigurationSave, matchFieldsConfig));
	}

	@SuppressWarnings("unchecked")
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.MatchFieldConfigurationRequest));
		
		buildAddEditFieldDialog();
		functionParameterDialog = new FunctionParametersDialog(controller);
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());

		FormPanel panel = new FormPanel();
		panel.setFrame(true);
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		List<String> leftAttributeNames =
			(List<String>) Registry.get(Constants.LEFT_TABLE_FIELD_NAMES);
		if (leftAttributeNames != null) {
			if (leftAttributeNames.size() > 0) {
				leftAttributeNameInPlaceCombo.add(leftAttributeNames);
			}
		}
		CellEditor leftAttributeNameInPlaceComboCellEditor = new CellEditor(leftAttributeNameInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return leftAttributeNameInPlaceCombo.findModel(value.toString());
			}
		
			@Override
			public Object postProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		ColumnConfig column = new ColumnConfig();
		column.setId(MatchFieldWeb.LEFT_FIELD_NAME);
		column.setHeader("Left Field Name");
		column.setWidth(130);
		column.setEditor(leftAttributeNameInPlaceComboCellEditor);
		configs.add(column);

		List<String> rightAttributeNames =
			(List<String>) Registry.get(Constants.RIGHT_TABLE_FIELD_NAMES);
		if (rightAttributeNames != null) {
			if (rightAttributeNames.size() > 0) {
				rightAttributeNameInPlaceCombo.add(rightAttributeNames);
			}
		}
		CellEditor rightAttributeNameInPlaceComboCellEditor = new CellEditor(rightAttributeNameInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return rightAttributeNameInPlaceCombo.findModel(value.toString());
			}
		
			@Override
			public Object postProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		column = new ColumnConfig();
		column.setId(MatchFieldWeb.RIGHT_FIELD_NAME);
		column.setHeader("Right Field Name");
		column.setWidth(130);
		column.setEditor(rightAttributeNameInPlaceComboCellEditor);
		configs.add(column);

		agreementProbabilityEdit.setAllowBlank(false);
		agreementProbabilityEdit.setAllowNegative(false);
		agreementProbabilityEdit.setMaxValue(1.0);
		agreementProbabilityEdit.setFormat(NumberFormat.getDecimalFormat());
		agreementProbabilityInPlaceEdit.setAllowBlank(false);
		agreementProbabilityInPlaceEdit.setAllowNegative(false);
		agreementProbabilityInPlaceEdit.setMaxValue(1.0);
		agreementProbabilityInPlaceEdit.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig();
		column.setId(MatchFieldWeb.AGREEMENT_PROBABILITY);
		column.setHeader("Agr.Prob.");
		column.setWidth(80);
		column.setEditor(new CellEditor(agreementProbabilityInPlaceEdit));
		column.setNumberFormat(NumberFormat.getScientificFormat());
		configs.add(column);

		disagreementProbabilityEdit.setAllowBlank(false);
		disagreementProbabilityEdit.setAllowNegative(false);
		disagreementProbabilityEdit.setMaxValue(1.0);
		disagreementProbabilityEdit.setFormat(NumberFormat.getDecimalFormat());
		disagreementProbabilityInPlaceEdit.setAllowBlank(false);
		disagreementProbabilityInPlaceEdit.setAllowNegative(false);
		disagreementProbabilityInPlaceEdit.setMaxValue(1.0);
		disagreementProbabilityInPlaceEdit.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig();
		column.setId(MatchFieldWeb.DISAGREEMENT_PROBABILITY);
		column.setHeader("Disagr.Prob.");
		column.setWidth(80);
		column.setEditor(new CellEditor(disagreementProbabilityInPlaceEdit));
		column.setNumberFormat(NumberFormat.getScientificFormat());
		configs.add(column);

		comparatorFuncNameInPlaceCombo.setForceSelection(true);
		comparatorFuncNameInPlaceCombo.setTriggerAction(TriggerAction.ALL);
		List<String> comparatorFuncNames = (List<String>) Registry.get(Constants.COMPARATOR_FUNCTION_NAME_SIMPLE_VALUE_LIST);
		comparatorFuncNameInPlaceCombo.add(comparatorFuncNames);
		CellEditor comparatorFuncNameInPlaceComboCellEditor = new CellEditor(comparatorFuncNameInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return comparatorFuncNameInPlaceCombo.findModel(value.toString());
			}
	
			@Override
			public Object postProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		column = new ColumnConfig();
		column.setId(MatchFieldWeb.COMPARATOR_FUNCTION_NAME);
		column.setHeader("Comp.Func.Name");
		column.setWidth(170);
		column.setEditor(comparatorFuncNameInPlaceComboCellEditor);
		configs.add(column);

		matchThresholdEdit.setAllowBlank(false);
		matchThresholdEdit.setAllowNegative(false);
		matchThresholdEdit.setMaxValue(1.0);
		matchThresholdEdit.setFormat(NumberFormat.getDecimalFormat());
		matchThresholdInPlaceEdit.setAllowBlank(false);
		matchThresholdInPlaceEdit.setAllowNegative(false);
		matchThresholdInPlaceEdit.setMaxValue(1.0);
		matchThresholdInPlaceEdit.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig();
		column.setId(MatchFieldWeb.MATCH_THRESHOLD);
		column.setHeader("Match Threshold");
		column.setWidth(80);
		column.setEditor(new CellEditor(matchThresholdInPlaceEdit));
		column.setNumberFormat(NumberFormat.getScientificFormat());
		configs.add(column);

		GridCellRenderer<MatchFieldWeb> buttonRenderer = new GridCellRenderer<MatchFieldWeb>() {
			private boolean init;

			public Object render(final MatchFieldWeb editedFieldParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<MatchFieldWeb> store, final Grid<MatchFieldWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<MatchFieldWeb>>() {
						public void handleEvent(GridEvent<MatchFieldWeb> be) {
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
				if (property.equals(MatchFieldWeb.ADD_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_row_insert.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							// Make sure we are starting with a clean slate
							addOrEditFieldMode = true;
							addEditMatchFieldDialog.show();

							leftAttributeNameCombo.clearSelections();
							rightAttributeNameCombo.clearSelections();
							agreementProbabilityEdit.clear();
							disagreementProbabilityEdit.clear();
							comparatorFuncNameCombo.clearSelections();
							matchThresholdEdit.clear();
						}
					});
					b.setToolTip("Add row to Grid");
				} else if (property.equals(MatchFieldWeb.EDIT_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_edit.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							addOrEditFieldMode = false;
							if (editedFieldParam == null) {
								Info.display("Information", "Cannot determine which field to edit.");
								return;
							}
							addEditMatchFieldDialog.show();
							editedFieldIndex = rowIndex;
							editedField = editedFieldParam;
							
							leftAttributeNameCombo.setValue(new ModelPropertyWeb(editedFieldParam.getLeftFieldName()));
							rightAttributeNameCombo.setValue(new ModelPropertyWeb(editedFieldParam.getRightFieldName()));
							agreementProbabilityEdit.setValue(editedFieldParam.getAgreementProbability());
							disagreementProbabilityEdit.setValue(editedFieldParam.getDisagreementProbability());
							comparatorFuncNameCombo.setValue(new ModelPropertyWeb(editedFieldParam.getComparatorFunction().getFunctionName()));
							matchThresholdEdit.setValue(editedFieldParam.getMatchThreshold());
						}
					});
					b.setToolTip("Edit row details in a separate form");
				} else if (property.equals(MatchFieldWeb.DELETE_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_row_delete.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (editedFieldParam == null) {
								Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove\" button.");
								return;
							}
							gridParam.getStore().remove(editedFieldParam);
						}
					});
					b.setToolTip("Delete row from Grid");
				} else if (property.equals(MatchFieldWeb.DND_IMAGE)) {
					return "<img src='images/mouse.png'></img>";
				}

				if (b != null)
					b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);

				return b;
			}
		};

		column = new ColumnConfig();
		column.setId(MatchFieldWeb.ADD_BUTTON);
		column.setHeader("Add");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(MatchFieldWeb.EDIT_BUTTON);
		column.setHeader("Edt");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(MatchFieldWeb.DELETE_BUTTON);
		column.setHeader("Del");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(MatchFieldWeb.DND_IMAGE);
		column.setHeader("DnD");
		column.setWidth(30);
		column.setToolTip("Drag&Drop row");
		column.setRenderer(buttonRenderer);
		configs.add(column);

		final ColumnModel cm = new ColumnModel(configs);

		final RowEditor<MatchFieldWeb> rowEditor = new RowEditor<MatchFieldWeb>();
		grid = new Grid<MatchFieldWeb>(store, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.addPlugin(rowEditor);

		ContentPanel cpGrid = new ContentPanel();
		cpGrid.setHeading("Match Fields");
		cpGrid.setFrame(false);
		cpGrid.setLayout(new FitLayout());

		ToolBar toolBar = new ToolBar();
		Button applyGridChanges = new Button("Apply grid changes", IconHelper.create("images/table_save.png"));
		applyGridChanges.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.commitChanges();
			}
		});
		toolBar.add(applyGridChanges);
		Button cancelGridChanges = new Button("Cancel grid changes", IconHelper.create("images/table_delete.png"));
		cancelGridChanges.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}
		});
		toolBar.add(cancelGridChanges);
		toolBar.add(new SeparatorToolItem());
		Button addFieldButton = new Button("Add Match Field", IconHelper.create("images/table_row_insert.png"));
		addFieldButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Make sure we are starting with a clean slate
				addOrEditFieldMode = true;
				addEditMatchFieldDialog.show();

				leftAttributeNameCombo.clearSelections();
				rightAttributeNameCombo.clearSelections();
				agreementProbabilityEdit.clear();
				disagreementProbabilityEdit.clear();
				comparatorFuncNameCombo.clearSelections();
				matchThresholdEdit.clear();
			}
		});
		toolBar.add(addFieldButton);

		new GridDragSource(grid);

		GridDropTarget target = new GridDropTarget(grid);
		target.setAllowSelfAsSource(true);
		target.setFeedback(Feedback.INSERT);

		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

		panel.add(main, new FormData("20%"));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.RECORD_LINKAGE_WIZARD_MODE);

		if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			MatchFieldConfigurationView matchFieldConfigurationView = (MatchFieldConfigurationView)sourceButton.getData("this");
			  			matchFieldConfigurationView.saveConfiguration();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.PrivacyPreservingBlockingConfigurationView);
			  		}
				});
			previousButton.setData("this", this);
			previousButton.setData("inWizardMode", inWizardMode);
			panel.getButtonBar().add(previousButton);
		}

		Button saveButton =
			new Button(inWizardMode ? Constants.NEXT_PAGE_WIZARD_BUTTON_TEXT : Constants.SAVE_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			Button sourceButton = ce.getButton();
		  			MatchFieldConfigurationView matchFieldConfigurationView = (MatchFieldConfigurationView)sourceButton.getData("this");
		  			matchFieldConfigurationView.saveConfiguration();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			if (inWizard) {
	  					Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
		  			}
		  		}
			});
		saveButton.setData("this", this);
		saveButton.setData("inWizardMode", inWizardMode);
		panel.getButtonBar().add(saveButton);

		if (inWizardMode) {
			Button exitButton =
				new Button(Constants.EXIT_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
						Dispatcher.get().dispatch(AppEvents.ExitWizardSelected);
			  		}
				});
			exitButton.setData("this", this);
			panel.getButtonBar().add(exitButton);
		}

		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 60));

		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		cpGrid.setTopComponent(toolBar);
		cpGrid.add(grid);
		gridContainer.add(cpGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(55, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}	

	private void buildAddEditFieldDialog() {
		addEditMatchFieldDialog = new Dialog();
		addEditMatchFieldDialog.setBodyBorder(false);
		addEditMatchFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditMatchFieldDialog.setHeading("Add/Edit Match Field");
		addEditMatchFieldDialog.setWidth(450);
		addEditMatchFieldDialog.setHeight(250);
		addEditMatchFieldDialog.setButtons(Dialog.OKCANCEL);
		addEditMatchFieldDialog.setHideOnButtonClick(true);
		addEditMatchFieldDialog.setModal(true);
		addEditMatchFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String leftAttribName = ClientUtils.getSelectedStringOfComboBox(leftAttributeNameCombo);
				if (leftAttribName == null) {
					Info.display("Information", "Please select left attribute name.");
					return;
				}
				String rightAttribName = ClientUtils.getSelectedStringOfComboBox(rightAttributeNameCombo);
				if (rightAttribName == null) {
					Info.display("Information", "Please select right attribute name.");
					return;
				}
				String compFuncName = ClientUtils.getSelectedStringOfComboBox(comparatorFuncNameCombo);
				if (compFuncName == null) {
					Info.display("Information", "Please select comparator function.");
					return;
				}

				MatchFieldWeb matchFieldWeb = new MatchFieldWeb();
				matchFieldWeb.setLeftFieldName(leftAttribName);
				matchFieldWeb.setRightFieldName(rightAttribName);
				matchFieldWeb.setAgreementProbability(agreementProbabilityEdit.getValue().doubleValue());
				matchFieldWeb.setDisagreementProbability(disagreementProbabilityEdit.getValue().doubleValue());
				FunctionFieldWeb functionFieldWeb = new FunctionFieldWeb(compFuncName);
				if (wereParametersEdited) {
					if (editedFunctionParameters != null) {
						if (editedFunctionParameters.size() > 0) {
							functionFieldWeb.setFunctionParameters(editedFunctionParameters);
						} else {
							functionFieldWeb.setFunctionParameters(null);
						}
					} else {
						functionFieldWeb.setFunctionParameters(null);								
					}
				} else {
					if (!addOrEditFieldMode && editedField != null)
						functionFieldWeb.setFunctionParameters(editedField.getComparatorFunction().cloneParameters());
				}
				matchFieldWeb.setComparatorFunction(functionFieldWeb);
				matchFieldWeb.setMatchThreshold(matchThresholdEdit.getValue().doubleValue());
				matchFieldWeb.updateRedunantFields();
				if (addOrEditFieldMode) {
					grid.getStore().add(matchFieldWeb);
				} else {
					grid.getStore().remove(editedField);
					grid.getStore().insert(matchFieldWeb, editedFieldIndex);
				}
			}
		});
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		addEditMatchFieldDialog.setLayout(formLayout);

		leftAttributeNameCombo.setEmptyText("Select left attribute...");
		leftAttributeNameCombo.setForceSelection(true);
		leftAttributeNameCombo.setDisplayField("name");
		leftAttributeNameCombo.setStore(leftAttributeNameStore);
		leftAttributeNameCombo.setTypeAhead(true);
		leftAttributeNameCombo.setTriggerAction(TriggerAction.ALL);
		leftAttributeNameCombo.setFieldLabel("Left Attribute Name");
		addEditMatchFieldDialog.add(leftAttributeNameCombo);

		rightAttributeNameCombo.setEmptyText("Select right attribute...");
		rightAttributeNameCombo.setForceSelection(true);
		rightAttributeNameCombo.setDisplayField("name");
		rightAttributeNameCombo.setStore(rightAttributeNameStore);
		rightAttributeNameCombo.setTypeAhead(true);
		rightAttributeNameCombo.setTriggerAction(TriggerAction.ALL);
		rightAttributeNameCombo.setFieldLabel("Right Attribute Name");
		addEditMatchFieldDialog.add(rightAttributeNameCombo);

		agreementProbabilityEdit.setFieldLabel("Agreement Probability");
		addEditMatchFieldDialog.add(agreementProbabilityEdit);

		disagreementProbabilityEdit.setFieldLabel("Disagreement Probability");
		addEditMatchFieldDialog.add(disagreementProbabilityEdit);

		matchThresholdEdit.setFieldLabel("Match Threshold");
		addEditMatchFieldDialog.add(matchThresholdEdit);

		comparatorFuncNameCombo.setEmptyText("Select function...");
		comparatorFuncNameCombo.setForceSelection(true);
		comparatorFuncNameCombo.setDisplayField("name");
		comparatorFuncNameCombo.setStore(comparatorFuncNameStore);
		comparatorFuncNameCombo.setTypeAhead(true);
		comparatorFuncNameCombo.setTriggerAction(TriggerAction.ALL);

		comparatorFuncNameCombo.setFieldLabel("Comparator Func. Name");
		addEditMatchFieldDialog.add(comparatorFuncNameCombo);

		addEditFunctionParametersButton.setText("Add/Edit Function Parameters");
		addEditMatchFieldDialog.add(addEditFunctionParametersButton);
		addEditFunctionParametersButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				List<FunctionParameterWeb> parameters = editedField.getComparatorFunction().getFunctionParameters();
				didNotHaveParameters = (parameters == null);
				if (didNotHaveParameters)
					parameters = new ArrayList<FunctionParameterWeb>();
				functionParameterDialog.setStore(parameters);
				functionParameterDialog.setEndNotificationEvent(AppEvents.MatchFieldComparisonFunctionParametersEdited);
				functionParameterDialog.show();
			}
		});
	}

}
