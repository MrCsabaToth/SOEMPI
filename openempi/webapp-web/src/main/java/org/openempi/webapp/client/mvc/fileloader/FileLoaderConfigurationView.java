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
package org.openempi.webapp.client.mvc.fileloader;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.FieldMeaningWeb;
import org.openempi.webapp.client.model.FieldTypeWeb;
import org.openempi.webapp.client.model.FunctionFieldWeb;
import org.openempi.webapp.client.model.FunctionParameterWeb;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.model.LoaderDataFieldWeb;
import org.openempi.webapp.client.model.LoaderFieldCompositionWeb;
import org.openempi.webapp.client.model.LoaderSubFieldWeb;
import org.openempi.webapp.client.model.LoaderTargetFieldWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.widget.ClientUtils;
import org.openempi.webapp.client.widget.FunctionParametersDialog;
import org.openempi.webapp.client.widget.VType;
import org.openempi.webapp.client.widget.VTypeValidator;

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
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
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

public class FileLoaderConfigurationView extends View
{
	private Grid<LoaderDataFieldWeb> grid;
	private ListStore<LoaderDataFieldWeb> store = new ListStore<LoaderDataFieldWeb>();
	private CheckBox isHeaderPresentCheckBox = new CheckBox();
	private TextField<String> delimiterRegexEdit = new TextField<String>();
	private Dialog addEditLoaderDataFieldDialog;
	private Dialog addEditLoaderSubFieldDialog;
	private Boolean addOrEditFieldMode = true;
	private int editedFieldIndex = 0;
	private LoaderDataFieldWeb editedDataField;
	private Grid<LoaderSubFieldWeb> editSubFieldGrid;
	private ListStore<LoaderSubFieldWeb> subFieldStore = new ListStore<LoaderSubFieldWeb>();
	private TextField<String> fieldNameEdit = new TextField<String>();
	private TextField<String> fieldNameInPlaceEdit = new TextField<String>();
	private NumberField sourceColumnIndexEdit = new NumberField();
	private NumberField sourceColumnIndexInPlaceEdit = new NumberField();
	private TextField<String> sourceFieldNameEdit = new TextField<String>();
	private TextField<String> sourceFieldNameInPlaceEdit = new TextField<String>();
	private ListStore<ModelPropertyWeb> fieldTypeListStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> fieldTypeCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> fieldTypeInPlaceCombo = new SimpleComboBox<String>();
	private TextField<String> fieldTypeModifierEdit = new TextField<String>();
	private TextField<String> fieldTypeModifierInPlaceEdit = new TextField<String>();
	private ListStore<ModelPropertyWeb> fieldMeaningListStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> fieldMeaningCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> fieldMeaningInPlaceCombo = new SimpleComboBox<String>();
	private ListStore<ModelPropertyWeb> allTrafoListStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> fieldTrafoCombo = new ComboBox<ModelPropertyWeb>();
	private SimpleComboBox<String> fieldTrafoInPlaceCombo = new SimpleComboBox<String>();
	private ComboBox<ModelPropertyWeb> subFieldTrafoCombo = new ComboBox<ModelPropertyWeb>();
	private CheckBox isPartOfCompositionCheckBox = new CheckBox();
	private NumberField compositionIndexEdit = new NumberField();
	private TextField<String> separatorEdit = new TextField<String>();
	private Boolean addOrEditSubFieldMode = true;
	private int editedSubFieldIndex = 0;
	private LoaderSubFieldWeb editedSubField;
	private TextField<String> subFieldNameEdit = new TextField<String>();
	private TextField<String> subFieldNameInPlaceEdit = new TextField<String>();
	private NumberField beginIndexEdit = new NumberField();
	private NumberField beginIndexInPlaceEdit = new NumberField();
	private NumberField endIndexEdit = new NumberField();
	private NumberField endIndexInPlaceEdit = new NumberField();

	private Button addEditDataFieldTrafoParametersButton = new Button();
	private Boolean didNotHaveDataFieldTrafoParameters = false;
	private List<FunctionParameterWeb> editedDataFieldTrafoParameters = null;
	private Boolean wereDataFieldTrafoParametersEdited = false;
	private Button addEditSubFieldTrafoParametersButton = new Button();
	private Boolean didNotHaveSubFieldTrafoParameters = false;
	private List<FunctionParameterWeb> editedSubFieldTrafoParameters = null;
	private Boolean wereSubFieldTrafoParametersEdited = false;
	private FunctionParametersDialog functionParameterDialog;

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	
	@SuppressWarnings("unchecked")
	public FileLoaderConfigurationView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> allTrafoNameModelPropList = (List<ModelPropertyWeb>)
				Registry.get(Constants.ALL_TRANSFORMATION_FUNCTION_NAME_MODEL_PROPERTY_LIST);
		try {
			allTrafoListStore.add(allTrafoNameModelPropList);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}

		List<ModelPropertyWeb> fieldTypeNames = (List<ModelPropertyWeb>) Registry.get(Constants.FIELD_TYPE_NAME_MODEL_PROPERTY_LIST);
		try {
			fieldTypeListStore.add(fieldTypeNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}

		List<ModelPropertyWeb> fieldMeaningNames = (List<ModelPropertyWeb>) Registry.get(Constants.FIELD_MEANING_NAME_MODEL_PROPERTY_LIST);
		try {
			fieldMeaningListStore.add(fieldMeaningNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.FileLoaderConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.FileLoaderConfigurationReceived) {
			store.removeAll();
			LoaderConfigWeb config = (LoaderConfigWeb) event.getData();
			delimiterRegexEdit.setValue(config.getDelimiterRegex());
			isHeaderPresentCheckBox.setValue(config.getHeaderLinePresent());
			store.add(config.getLoaderDataFields());
		} else if (event.getType() == AppEvents.FileLoaderDataFieldTrafoParametersEdited) {
			wereDataFieldTrafoParametersEdited = true;
			editedDataFieldTrafoParameters = (List<FunctionParameterWeb>) event.getData();
		} else if (event.getType() == AppEvents.FileLoaderSubFieldTrafoParametersEdited) {
			wereSubFieldTrafoParametersEdited = true;
			editedSubFieldTrafoParameters = (List<FunctionParameterWeb>) event.getData();
		}
	}

	protected void saveConfiguration()
	{
		LoaderConfigWeb loaderConfig = new LoaderConfigWeb();
		loaderConfig.setDelimiterRegex(delimiterRegexEdit.getValue());
		loaderConfig.setHeaderLinePresent(isHeaderPresentCheckBox.getValue());
		List<LoaderDataFieldWeb> loaderDataFieldsConfig = grid.getStore().getModels();
		loaderConfig.setLoaderDataFields(loaderDataFieldsConfig);
		controller.handleEvent(new AppEvent(AppEvents.FileLoaderConfigurationSave, loaderConfig));
	}

	@SuppressWarnings("unchecked")
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.FileLoaderConfigurationRequest));
		
		buildAddEditFieldDialog();
		buildAddEditSubFieldDialog();
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

		sourceColumnIndexEdit.setAllowBlank(false);
		sourceColumnIndexEdit.setAllowNegative(false);
		sourceColumnIndexInPlaceEdit.setAllowBlank(false);
		sourceColumnIndexInPlaceEdit.setAllowNegative(false);
		ColumnConfig column = new ColumnConfig();
		column.setId(LoaderDataFieldWeb.SOURCE_COLUMN_INDEX);
		column.setHeader("Src.Col.Idx.");
		column.setWidth(70);
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setEditor(new CellEditor(sourceColumnIndexInPlaceEdit));
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderDataFieldWeb.FIELD_NAME);
		column.setHeader("Field Name");
		column.setWidth(150);
		column.setAlignment(HorizontalAlignment.LEFT);
		fieldNameInPlaceEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		column.setEditor(new CellEditor(fieldNameInPlaceEdit));
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderDataFieldWeb.SOURCE_FIELD_NAME);
		column.setHeader("Src.Fld.Name");
		column.setWidth(150);
		column.setAlignment(HorizontalAlignment.LEFT);
		sourceFieldNameInPlaceEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		column.setEditor(new CellEditor(sourceFieldNameInPlaceEdit));
		configs.add(column);

		List<String> fieldTypeNameSimpleValueList = (List<String>)
				Registry.get(Constants.FIELD_TYPE_NAME_MODEL_SIMPLE_VALUE_LIST);
		fieldTypeInPlaceCombo.add(fieldTypeNameSimpleValueList);
		CellEditor fieldTypeInPlaceComboCellEditor = new CellEditor(fieldTypeInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return fieldTypeInPlaceCombo.findModel(value.toString());
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
		column.setId(LoaderDataFieldWeb.FIELD_TYPE);
		column.setHeader("Field Type");
		column.setWidth(80);
		column.setEditor(fieldTypeInPlaceComboCellEditor);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderDataFieldWeb.FIELD_TYPE_MODIFIER);
		column.setHeader("Field Typ.Mod.");
		column.setWidth(80);
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setEditor(new CellEditor(fieldTypeModifierInPlaceEdit));
		configs.add(column);

		List<String> fieldMeaningNameSimpleValueList = (List<String>)
				Registry.get(Constants.FIELD_MEANING_NAME_MODEL_SIMPLE_VALUE_LIST);
		fieldMeaningInPlaceCombo.add(fieldMeaningNameSimpleValueList);
		CellEditor fieldMeaningInPlaceComboCellEditor = new CellEditor(fieldMeaningInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return fieldMeaningInPlaceCombo.findModel(value.toString());
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
		column.setId(LoaderDataFieldWeb.FIELD_MEANING);
		column.setHeader("Field Meaning");
		column.setWidth(80);
		column.setEditor(fieldMeaningInPlaceComboCellEditor);
		configs.add(column);

		List<String> allTrafoNameSimpleValueList = (List<String>)
				Registry.get(Constants.ALL_TRANSFORMATION_FUNCTION_NAME_SIMPLE_VALUE_LIST);
		fieldTrafoInPlaceCombo.add(allTrafoNameSimpleValueList);
		CellEditor fieldTrafoInPlaceComboCellEditor = new CellEditor(fieldTrafoInPlaceCombo) {
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				return fieldTrafoInPlaceCombo.findModel(value.toString());
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
		column.setId(LoaderDataFieldWeb.FIELD_TRANSFORMATION_NAME);
		column.setHeader("Field.Trafo.Name");
		column.setWidth(170);
		column.setEditor(fieldTrafoInPlaceComboCellEditor);
		configs.add(column);
		
		GridCellRenderer<LoaderDataFieldWeb> buttonRenderer = new GridCellRenderer<LoaderDataFieldWeb>() {
			private boolean init;

			public Object render(final LoaderDataFieldWeb editedFieldParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<LoaderDataFieldWeb> store, final Grid<LoaderDataFieldWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<LoaderDataFieldWeb>>() {
						public void handleEvent(GridEvent<LoaderDataFieldWeb> be) {
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
				if (property.equals(LoaderTargetFieldWeb.ADD_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_row_insert.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							// Make sure we are starting with a clean slate
							addOrEditFieldMode = true;
							addEditLoaderDataFieldDialog.show();
							editSubFieldGrid.getStore().removeAll();
							fieldNameEdit.clear();
							sourceColumnIndexEdit.clear();
							sourceFieldNameEdit.clear();
							fieldTypeCombo.clearSelections();
							fieldTypeModifierEdit.clear();
							fieldMeaningCombo.clearSelections();
							fieldTrafoCombo.clearSelections();
							isPartOfCompositionCheckBox.setValue(false);
							compositionIndexEdit.clear();
							separatorEdit.clear();
						}
					});
					b.setToolTip("Add row to Grid");
				} else if (property.equals(LoaderTargetFieldWeb.EDIT_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_edit.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
				  			addOrEditFieldMode = false;
							if (editedFieldParam == null) {
								Info.display("Information", "Cannot determine which field to edit.");
								return;
							}
							addEditLoaderDataFieldDialog.show();
							editedFieldIndex = rowIndex;
							editedDataField = editedFieldParam;
							subFieldStore.removeAll();
							if (editedFieldParam.getLoaderSubFields() != null) {
								if (editedFieldParam.getLoaderSubFields().size() > 0) {
									subFieldStore.add(editedFieldParam.getLoaderSubFields());
								}
							}
							fieldNameEdit.setValue(editedFieldParam.getFieldName());
							fieldTypeCombo.setValue(new ModelPropertyWeb(editedFieldParam.getFieldType().name()));
							fieldTypeModifierEdit.setValue(editedFieldParam.getFieldTypeModifier());
							fieldMeaningCombo.setValue(new ModelPropertyWeb(editedFieldParam.getFieldMeaning().name()));
							sourceColumnIndexEdit.setValue(editedFieldParam.getSourceColumnIndex());
							sourceFieldNameEdit.setValue(editedFieldParam.getSourceFieldName());
							if (editedFieldParam.getFieldTransformation() != null)
								fieldTrafoCombo.setValue(new ModelPropertyWeb(editedFieldParam.getFieldTransformation().getFunctionName()));
							else
								fieldTrafoCombo.clearSelections();
							isPartOfCompositionCheckBox.setValue(editedFieldParam.getLoaderFieldComposition() != null);
							if (editedFieldParam.getLoaderFieldComposition() != null) {
								compositionIndexEdit.setValue(editedFieldParam.getLoaderFieldComposition().getIndex());
								separatorEdit.setValue(editedFieldParam.getLoaderFieldComposition().getSeparator());
							} else {
								compositionIndexEdit.clear();
								separatorEdit.clear();
							}
						}
					});
					b.setToolTip("Edit row details in a separate form");
				} else if (property.equals(LoaderTargetFieldWeb.DELETE_BUTTON)) {
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
				} else if (property.equals(LoaderTargetFieldWeb.DND_IMAGE)) {
					return "<img src='images/mouse.png'></img>";
				}

				if (b != null)
					b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);

				return b;
			}
		};

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.ADD_BUTTON);
		column.setHeader("Add");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.EDIT_BUTTON);
		column.setHeader("Edt");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.DELETE_BUTTON);
		column.setHeader("Del");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.DND_IMAGE);
		column.setHeader("DnD");
		column.setWidth(30);
		column.setToolTip("Drag&Drop row");
		column.setRenderer(buttonRenderer);
		configs.add(column);

		final ColumnModel cm = new ColumnModel(configs);

		final RowEditor<LoaderDataFieldWeb> rowEditor = new RowEditor<LoaderDataFieldWeb>();
		grid = new Grid<LoaderDataFieldWeb>(store, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.addPlugin(rowEditor);

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
		final Button addFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addOrEditFieldMode = true;
					addEditLoaderDataFieldDialog.show();
					editSubFieldGrid.getStore().removeAll();
					fieldNameEdit.clear();
					fieldTypeCombo.clearSelections();
					fieldTypeModifierEdit.clear();
					fieldMeaningCombo.clearSelections();
					sourceColumnIndexEdit.clear();
					sourceFieldNameEdit.clear();
					fieldTrafoCombo.clearSelections();
					isPartOfCompositionCheckBox.setValue(false);
					compositionIndexEdit.clear();
					separatorEdit.clear();
				}
			});
		toolBar.add(addFieldButton);

		ContentPanel cpGrid = new ContentPanel();
		cpGrid.setHeading("File Loader Fields");
		cpGrid.setFrame(false);
		cpGrid.setLayout(new FitLayout());

		new GridDragSource(grid);

		GridDropTarget target = new GridDropTarget(grid);
		target.setAllowSelfAsSource(true);
		target.setFeedback(Feedback.INSERT);

		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

		panel.add(main, new FormData("20%"));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.FILE_IMPORT_WIZARD_MODE);

		if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			FileLoaderConfigurationView fileLoaderConfigurationView = (FileLoaderConfigurationView)sourceButton.getData("this");
			  			fileLoaderConfigurationView.saveConfiguration();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.BloomfilterSettingsView);
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
		  			FileLoaderConfigurationView fileLoaderConfigurationView = (FileLoaderConfigurationView)sourceButton.getData("this");
		  			fileLoaderConfigurationView.saveConfiguration();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			if (inWizard)
						Dispatcher.get().dispatch(AppEvents.DatasetListView);
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
		
		LayoutContainer formPart1 = new LayoutContainer();
		formPart1.setLayout(new FormLayout());

		delimiterRegexEdit.setFieldLabel("Delimiter Regex.");
		//delimiterRegexEdit.setValidator(new VTypeValidator(VType.PRINTABLE));	// For some reason comma was warned to be a non printable character :P
		formPart1.add(delimiterRegexEdit);
		isHeaderPresentCheckBox.setFieldLabel("Header presence");
		isHeaderPresentCheckBox.setBoxLabel("Is header present in file?");
		formPart1.add(isHeaderPresentCheckBox);

		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 45));

		panel.getButtonBar().add(formPart1);

		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		cpGrid.setTopComponent(toolBar);
		cpGrid.add(grid);
		gridContainer.add(cpGrid);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(40, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	private void buildAddEditFieldDialog() {
		addEditLoaderDataFieldDialog = new Dialog();
		addEditLoaderDataFieldDialog.setBodyBorder(false);
		addEditLoaderDataFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditLoaderDataFieldDialog.setHeading("Add/Edit Field");
		addEditLoaderDataFieldDialog.setWidth(600);
		addEditLoaderDataFieldDialog.setHeight(550);
		addEditLoaderDataFieldDialog.setButtons(Dialog.OKCANCEL);
		addEditLoaderDataFieldDialog.setHideOnButtonClick(true);
		addEditLoaderDataFieldDialog.setModal(true);
		addEditLoaderDataFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				LoaderDataFieldWeb loaderDataFieldWeb = new LoaderDataFieldWeb();
				loaderDataFieldWeb.setFieldName(fieldNameEdit.getValue());
				String fieldTypeName = ClientUtils.getSelectedStringOfComboBox(fieldTypeCombo);
				if (fieldTypeName != null) {
					loaderDataFieldWeb.setFieldType(FieldTypeWeb.valueOf(fieldTypeName));
				}
				loaderDataFieldWeb.setFieldTypeModifier(fieldTypeModifierEdit.getValue());
				String fieldMeaningName = ClientUtils.getSelectedStringOfComboBox(fieldMeaningCombo);
				if (fieldMeaningName != null) {
					loaderDataFieldWeb.setFieldMeaning(FieldMeaningWeb.valueOf(fieldMeaningName));
				}
				loaderDataFieldWeb.setSourceColumnIndex(sourceColumnIndexEdit.getValue().intValue());
				loaderDataFieldWeb.setSourceFieldname(sourceFieldNameEdit.getValue());
				String fieldTrafoName = ClientUtils.getSelectedStringOfComboBox(fieldTrafoCombo);
				if (fieldTrafoName != null) {
					FunctionFieldWeb functionFieldWeb = new FunctionFieldWeb(fieldTrafoName);
					if (wereDataFieldTrafoParametersEdited) {
						if (editedDataFieldTrafoParameters != null) {
							if (editedDataFieldTrafoParameters.size() > 0) {
								functionFieldWeb.setFunctionParameters(editedDataFieldTrafoParameters);
							} else {
								functionFieldWeb.setFunctionParameters(null);
							}
						} else {
							functionFieldWeb.setFunctionParameters(null);								
						}
					} else {
						functionFieldWeb.setFunctionParameters(editedDataField.getFieldTransformation().cloneParameters());
					}
					loaderDataFieldWeb.setFieldTransformation(functionFieldWeb);
				}
				if (isPartOfCompositionCheckBox.getValue()) {
					LoaderFieldCompositionWeb loaderFieldCompositionWeb = new LoaderFieldCompositionWeb(
							compositionIndexEdit.getValue().intValue(),
							separatorEdit.getValue());
					loaderDataFieldWeb.setLoaderFieldComposition(loaderFieldCompositionWeb);
				}
				if (editSubFieldGrid.getStore().getCount() > 0) {
					loaderDataFieldWeb.setLoaderSubFields(editSubFieldGrid.getStore().getModels());
				}
				loaderDataFieldWeb.updateRedunantFields();
				if (addOrEditFieldMode) {
					grid.getStore().add(loaderDataFieldWeb);
				} else {
					grid.getStore().remove(editedDataField);
					grid.getStore().insert(loaderDataFieldWeb, editedFieldIndex);
				}
			}
		});

		ContentPanel cp = new ContentPanel();
		cp.setHeading("File Field");
		cp.setFrame(false);
		cp.setIcon(IconHelper.create("images/folder.png"));
		FormLayout formLayout = new FormLayout();
//		formLayout.setLabelWidth(170);
//		formLayout.setDefaultWidth(250);
		cp.setLayout(formLayout);
//		cp.setLayout(new FillLayout());
		cp.setSize(680, 530);

		fieldNameEdit.setFieldLabel("Field Name");
		fieldNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		cp.add(fieldNameEdit);

		sourceFieldNameEdit.setFieldLabel("Src.Fld.Name");
		sourceFieldNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		cp.add(sourceFieldNameEdit);

		sourceColumnIndexEdit.setFieldLabel("Src.col.idx");
		cp.add(sourceColumnIndexEdit);

		fieldTypeCombo.setEmptyText("Select a type...");
		fieldTypeCombo.setForceSelection(true);
		fieldTypeCombo.setDisplayField("name");
		fieldTypeCombo.setStore(fieldTypeListStore);
		fieldTypeCombo.setTypeAhead(true);
		fieldTypeCombo.setTriggerAction(TriggerAction.ALL);
		fieldTypeCombo.setFieldLabel("Field Type");
		cp.add(fieldTypeCombo);

		fieldTypeModifierEdit.setFieldLabel("Field Type Modifier");
		cp.add(fieldTypeModifierEdit);

		fieldMeaningCombo.setEmptyText("Select meaning...");
		fieldMeaningCombo.setForceSelection(true);
		fieldMeaningCombo.setDisplayField("name");
		fieldMeaningCombo.setStore(fieldMeaningListStore);
		fieldMeaningCombo.setTypeAhead(true);
		fieldMeaningCombo.setTriggerAction(TriggerAction.ALL);
		fieldMeaningCombo.setFieldLabel("Field Meaning");
		cp.add(fieldMeaningCombo);

		fieldTrafoCombo.setEmptyText("Select a function...");
		fieldTrafoCombo.setForceSelection(true);
		fieldTrafoCombo.setDisplayField("name");
		fieldTrafoCombo.setStore(allTrafoListStore);
		fieldTrafoCombo.setTypeAhead(true);
		fieldTrafoCombo.setTriggerAction(TriggerAction.ALL);
		fieldTrafoCombo.setFieldLabel("Transformation");
		cp.add(fieldTrafoCombo);

		isPartOfCompositionCheckBox.setFieldLabel("Is part of composition");
		isPartOfCompositionCheckBox.setBoxLabel("Is part of composition?");
		cp.add(isPartOfCompositionCheckBox);

		compositionIndexEdit.setFieldLabel("Index in composition");
		compositionIndexEdit.setAllowBlank(false);
		compositionIndexEdit.setAllowNegative(false);
		cp.add(compositionIndexEdit);

		separatorEdit.setFieldLabel("Composition Separator");
		cp.add(separatorEdit);

		addEditDataFieldTrafoParametersButton.setText("Add/Edit Transformation Parameters");
		cp.add(addEditDataFieldTrafoParametersButton);
		addEditDataFieldTrafoParametersButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				List<FunctionParameterWeb> parameters = editedDataField.getFieldTransformation().getFunctionParameters();
				didNotHaveDataFieldTrafoParameters = (parameters == null);
				if (didNotHaveDataFieldTrafoParameters)
					parameters = new ArrayList<FunctionParameterWeb>();
				functionParameterDialog.setStore(parameters);
				functionParameterDialog.setEndNotificationEvent(AppEvents.FileLoaderDataFieldTrafoParametersEdited);
				functionParameterDialog.show();
			}
		});

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId(LoaderSubFieldWeb.FIELD_NAME);
		column.setHeader("Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(120);
		subFieldNameInPlaceEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		column.setEditor(new CellEditor(subFieldNameInPlaceEdit));
		configs.add(column);

		beginIndexEdit.setAllowBlank(false);
		beginIndexEdit.setAllowNegative(false);
		beginIndexInPlaceEdit.setAllowBlank(false);
		beginIndexInPlaceEdit.setAllowNegative(false);
		column = new ColumnConfig();
		column.setId(LoaderSubFieldWeb.BEGIN_INDEX);
		column.setHeader("Begin Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(70);
		column.setEditor(new CellEditor(beginIndexInPlaceEdit));
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		configs.add(column);

		endIndexEdit.setAllowBlank(false);
		endIndexEdit.setAllowNegative(false);
		endIndexInPlaceEdit.setAllowBlank(false);
		endIndexInPlaceEdit.setAllowNegative(false);
		column = new ColumnConfig();
		column.setId(LoaderSubFieldWeb.END_INDEX);
		column.setHeader("End Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(70);
		column.setEditor(new CellEditor(endIndexInPlaceEdit));
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderSubFieldWeb.FIELD_TRANSFORMATION_NAME);
		column.setHeader("Transformation");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(150);
		configs.add(column);

		GridCellRenderer<LoaderSubFieldWeb> buttonRenderer = new GridCellRenderer<LoaderSubFieldWeb>() {
			private boolean init;

			public Object render(final LoaderSubFieldWeb editedSubFieldParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<LoaderSubFieldWeb> store, final Grid<LoaderSubFieldWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<LoaderSubFieldWeb>>() {
						public void handleEvent(GridEvent<LoaderSubFieldWeb> be) {
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
				if (property.equals(LoaderTargetFieldWeb.ADD_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_row_insert.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
				  			addOrEditSubFieldMode = true;
							addEditLoaderSubFieldDialog.show();
							subFieldNameEdit.clear();
							beginIndexEdit.clear();
							endIndexEdit.clear();
							subFieldTrafoCombo.clearSelections();
						}
					});
					b.setToolTip("Add row to Grid");
				} else if (property.equals(LoaderTargetFieldWeb.EDIT_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_edit.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
				  			addOrEditSubFieldMode = false;
							if (editedSubFieldParam == null) {
								Info.display("Information", "Cannot determine which subfield to edit.");
								return;
							}
							addEditLoaderSubFieldDialog.show();
							subFieldNameEdit.setValue(editedSubFieldParam.getFieldName());
							editedSubFieldIndex = rowIndex;
							editedSubField = editedSubFieldParam;
							beginIndexEdit.setValue(editedSubFieldParam.getBeginIndex());
							endIndexEdit.setValue(editedSubFieldParam.getEndIndex());
							if (editedSubFieldParam.getFieldTransformation() != null)
								subFieldTrafoCombo.setValue(new ModelPropertyWeb(editedSubFieldParam.getFieldTransformation().getFunctionName()));
							else
								subFieldTrafoCombo.clearSelections();
						}
					});
					b.setToolTip("Edit row details in a separate form");
/*				} else if (property.equals(LoaderTargetFieldWeb.UP_BUTTON)) {
					b = new Button("", IconHelper.create("images/arrow_up.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (editedSubFieldParam == null) {
								Info.display("Information", "Cannot determine which field to move up.");
								return;
							}
							if (rowIndex > 0 && gridParam.getStore().getCount() > 1) {
								gridParam.getStore().remove(editedSubFieldParam);
								gridParam.getStore().insert(editedSubFieldParam, rowIndex - 1);
							} else {
								Info.display("Information", "Cannot move up the first field.");
							}
						}
					});
					b.setToolTip("Move up row in Grid");
				} else if (property.equals(LoaderTargetFieldWeb.DOWN_BUTTON)) {
					b = new Button("", IconHelper.create("images/arrow_down.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (editedSubFieldParam == null) {
								Info.display("Information", "Cannot determine which field to move down.");
								return;
							}
							if (rowIndex >= 0 && rowIndex < gridParam.getStore().getCount() - 1) {
								gridParam.getStore().remove(editedSubFieldParam);
								gridParam.getStore().insert(editedSubFieldParam, rowIndex + 1);
							} else {
								Info.display("Information", "Cannot move down the last field.");
							}
						}
					});
					b.setToolTip("Move down row in Grid");*/
				} else if (property.equals(LoaderTargetFieldWeb.DELETE_BUTTON)) {
					b = new Button("", IconHelper.create("images/table_row_delete.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (editedSubFieldParam == null) {
								Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove\" button.");
								return;
							}
							gridParam.getStore().remove(editedSubFieldParam);
						}
					});
					b.setToolTip("Delete row from Grid");
				}

				b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);
				b.setToolTip("Click for more information");

				return b;
			}
		};

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.ADD_BUTTON);
		column.setHeader("Add");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.EDIT_BUTTON);
		column.setHeader("Edt");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

/*		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.UP_BUTTON);
		column.setHeader("Up");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.DOWN_BUTTON);
		column.setHeader("Dn");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);*/

		column = new ColumnConfig();
		column.setId(LoaderTargetFieldWeb.DELETE_BUTTON);
		column.setHeader("Del");
		column.setWidth(30);
		column.setRenderer(buttonRenderer);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		final RowEditor<LoaderDataFieldWeb> rowEditor2 = new RowEditor<LoaderDataFieldWeb>();
		editSubFieldGrid = new Grid<LoaderSubFieldWeb>(subFieldStore, cm);
		editSubFieldGrid.setStyleAttribute("borderTop", "none");
		editSubFieldGrid.setBorders(true);
		editSubFieldGrid.setStripeRows(true);
		editSubFieldGrid.setAutoWidth(true);
		editSubFieldGrid.addPlugin(rowEditor2);

		ToolBar toolBar = new ToolBar();
		Button applyGridChanges = new Button("Apply grid changes", IconHelper.create("images/table_save.png"));
		applyGridChanges.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				subFieldStore.commitChanges();
			}
		});
		toolBar.add(applyGridChanges);
		Button cancelGridChanges = new Button("Cancel grid changes", IconHelper.create("images/table_delete.png"));
		cancelGridChanges.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				subFieldStore.rejectChanges();
			}
		});
		toolBar.add(cancelGridChanges);
		toolBar.add(new SeparatorToolItem());
		Button addSubFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
		  			addOrEditSubFieldMode = true;
					addEditLoaderSubFieldDialog.show();
					subFieldNameEdit.clear();
					beginIndexEdit.clear();
					endIndexEdit.clear();
					subFieldTrafoCombo.clearSelections();
				}
			});
		toolBar.add(addSubFieldButton);

		ContentPanel cpGrid = new ContentPanel();
		cpGrid.setHeading("File Loader SubFields");
		cpGrid.setFrame(false);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setSize(560, 300);

		cpGrid.add(editSubFieldGrid);

		new GridDragSource(editSubFieldGrid);

		GridDropTarget target = new GridDropTarget(editSubFieldGrid);
		target.setAllowSelfAsSource(true);
		target.setFeedback(Feedback.INSERT);

		cpGrid.setTopComponent(toolBar);

		cp.add(cpGrid);

		addEditLoaderDataFieldDialog.add(cp);
	}

	private void buildAddEditSubFieldDialog() {
		addEditLoaderSubFieldDialog = new Dialog();
		addEditLoaderSubFieldDialog.setBodyBorder(false);
		addEditLoaderSubFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditLoaderSubFieldDialog.setHeading("Add/Edit SubField");
		addEditLoaderSubFieldDialog.setWidth(450);
		addEditLoaderSubFieldDialog.setHeight(270);
		addEditLoaderSubFieldDialog.setButtons(Dialog.OKCANCEL);
		addEditLoaderSubFieldDialog.setHideOnButtonClick(true);
		addEditLoaderSubFieldDialog.setModal(true);
		addEditLoaderSubFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ((endIndexEdit.getValue().intValue() >= 0 &&
					beginIndexEdit.getValue().intValue() >= endIndexEdit.getValue().intValue()) ||
					beginIndexEdit.getValue().intValue() < 0)
				{
					Info.display("Information", "Begin index must be smaller than End index.");
					return;
				}
				LoaderSubFieldWeb loaderSubFieldWeb =
					new LoaderSubFieldWeb(subFieldNameEdit.getValue(), beginIndexEdit.getValue().intValue(),
							endIndexEdit.getValue().intValue());

				String fieldTrafoName = ClientUtils.getSelectedStringOfComboBox(subFieldTrafoCombo);
				if (fieldTrafoName != null) {
					FunctionFieldWeb functionFieldWeb = new FunctionFieldWeb(fieldTrafoName);
					if (wereSubFieldTrafoParametersEdited) {
						if (editedSubFieldTrafoParameters != null) {
							if (editedSubFieldTrafoParameters.size() > 0) {
								functionFieldWeb.setFunctionParameters(editedSubFieldTrafoParameters);
							} else {
								functionFieldWeb.setFunctionParameters(null);
							}
						} else {
							functionFieldWeb.setFunctionParameters(null);								
						}
					} else {
						functionFieldWeb.setFunctionParameters(editedSubField.getFieldTransformation().cloneParameters());
					}
					loaderSubFieldWeb.setFieldTransformation(functionFieldWeb);
				} else {
					loaderSubFieldWeb.setFieldTransformation(null);
				}
				loaderSubFieldWeb.updateRedunantFields();
				if (addOrEditSubFieldMode) {
					editSubFieldGrid.getStore().add(loaderSubFieldWeb);
				} else {
					editSubFieldGrid.getStore().remove(editedSubField);
					editSubFieldGrid.getStore().insert(loaderSubFieldWeb, editedSubFieldIndex);
				}
			}
		});
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(170);
		formLayout.setDefaultWidth(250);
		addEditLoaderSubFieldDialog.setLayout(formLayout);

		subFieldNameEdit.setFieldLabel("Sub Field Name");
		subFieldNameEdit.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		addEditLoaderSubFieldDialog.add(subFieldNameEdit);

		subFieldTrafoCombo.setEmptyText("Select a function...");
		subFieldTrafoCombo.setForceSelection(true);
		subFieldTrafoCombo.setDisplayField("name");
//		subFieldTrafoCombo.setWidth(150);
		subFieldTrafoCombo.setStore(allTrafoListStore);
		subFieldTrafoCombo.setTypeAhead(true);
		subFieldTrafoCombo.setTriggerAction(TriggerAction.ALL);
		subFieldTrafoCombo.setFieldLabel("String Trafo");
		addEditLoaderSubFieldDialog.add(subFieldTrafoCombo);

		beginIndexEdit.setFieldLabel("Begin Index");
		addEditLoaderSubFieldDialog.add(beginIndexEdit);

		endIndexEdit.setFieldLabel("End Index");
		addEditLoaderSubFieldDialog.add(endIndexEdit);

		addEditSubFieldTrafoParametersButton.setText("Add/Edit String Trafo Parameters");
		addEditLoaderSubFieldDialog.add(addEditSubFieldTrafoParametersButton);
		addEditSubFieldTrafoParametersButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				List<FunctionParameterWeb> parameters = editedSubField.getFieldTransformation().getFunctionParameters();
				didNotHaveSubFieldTrafoParameters = (parameters == null);
				if (didNotHaveSubFieldTrafoParameters)
					parameters = new ArrayList<FunctionParameterWeb>();
				functionParameterDialog.setStore(parameters);
				functionParameterDialog.setEndNotificationEvent(AppEvents.FileLoaderSubFieldTrafoParametersEdited);
				functionParameterDialog.show();
			}
		});
	}

}
