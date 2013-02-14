/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openempi.webapp.client.mvc.blocking;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.BlockingFieldBaseWeb;
import org.openempi.webapp.client.model.BlockingSettingsWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.widget.ClientUtils;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

public class BlockingConfigurationView extends View
{
	private NumberField numberOfRecordsToSampleEdit = new NumberField();
	private Grid<BlockingFieldBaseWeb> grid;
	private GroupingStore<BlockingFieldBaseWeb> store = new GroupingStore<BlockingFieldBaseWeb>();
	private Dialog addBlockingRoundDialog;
	private ListStore<ModelPropertyWeb> leftFieldListStore = new ListStore<ModelPropertyWeb>();
	private ListStore<ModelPropertyWeb> rightFieldListStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> leftFieldCombo = new ComboBox<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> rightFieldCombo = new ComboBox<ModelPropertyWeb>();
	private Grid<BlockingFieldBaseWeb> addRoundGrid;
	
	private LayoutContainer container;
	
	public BlockingConfigurationView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.BlockingConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.BlockingConfigurationReceived) {
			store.removeAll();
			BlockingSettingsWeb settings = (BlockingSettingsWeb) event.getData();
			numberOfRecordsToSampleEdit.setValue(settings.getNumberOfRecordsToSample());
			for (BlockingFieldBaseWeb baseField : settings.getBlockingFields()) {
				store.add(baseField);
			}
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

	private void reloadFieldCombos(List<ModelPropertyWeb> fieldNames, boolean leftOrRight) {
		// Saving selections if any
		String fieldName = null;
		if (leftOrRight)
			fieldName = ClientUtils.getSelectedStringOfComboBox(leftFieldCombo);
		else
			fieldName = ClientUtils.getSelectedStringOfComboBox(rightFieldCombo);
		try {
			if (leftOrRight) {
				leftFieldListStore.removeAll();
				leftFieldListStore.add(fieldNames);
			} else {
				rightFieldListStore.removeAll();
				rightFieldListStore.add(fieldNames);
			}
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
		if (fieldName != null) {
			if (fieldName.length() > 0) {
				if (leftOrRight)
					leftFieldCombo.select(new ModelPropertyWeb(fieldName));
				else
					rightFieldCombo.select(new ModelPropertyWeb(fieldName));
			}
		}
	}

	protected void saveConfiguration()
	{
		BlockingSettingsWeb settings = new BlockingSettingsWeb();
		settings.setNumberOfRecordsToSample(numberOfRecordsToSampleEdit.getValue().intValue());
		settings.setBlockingFields(grid.getStore().getModels());
		controller.handleEvent(new AppEvent(AppEvents.BlockingConfigurationSave, settings));
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		store.groupBy("blockingRound");
		controller.handleEvent(new AppEvent(AppEvents.BlockingConfigurationRequest));
		
		buildAddBlockingRoundDialog();
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig blockingRound = new ColumnConfig(BlockingFieldBaseWeb.BLOCKING_ROUND, "Blocking Round", 60);
		ColumnConfig fieldIndex = new ColumnConfig(BlockingFieldBaseWeb.FIELD_INDEX, "Field Index", 60);
		ColumnConfig leftFieldName = new ColumnConfig(BlockingFieldBaseWeb.LEFT_FIELD_NAME, "Left Field Name", 100);
		ColumnConfig rightFieldName = new ColumnConfig(BlockingFieldBaseWeb.RIGHT_FIELD_NAME, "Right Field Name", 100);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(blockingRound);
		config.add(fieldIndex);
		config.add(leftFieldName);
		config.add(rightFieldName);
		
		final ColumnModel cm = new ColumnModel(config);

		GroupingView view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String f = cm.getColumnById(data.field).getHeader();
				String l = data.models.size() == 1 ? "Field" : "Fields";
				return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
			}});

		grid = new Grid<BlockingFieldBaseWeb>(store, cm);
		grid.setView(view);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.setAutoWidth(true);
		grid.setHeight(230);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Blocking Configuration");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		cp.setLayout(formLayout);
		cp.setSize(500, 400);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		final Button addRoundButton =
			new Button("Add Round", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addRoundGrid.getStore().removeAll();
					addBlockingRoundDialog.show();
				}
			});
		buttonContainer.add(addRoundButton);
		final Button removeRoundButton =
			new Button("Remove Round", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					BlockingFieldBaseWeb removeField = grid.getSelectionModel().getSelectedItem();
					if (removeField == null) {
						Info.display("Information","You must first select a field in the round to be deleted before pressing the \"Remove Round\" button.");
						return;
					}
					for (BlockingFieldBaseWeb field : grid.getStore().getModels()) {
						if (field.getBlockingRound() == removeField.getBlockingRound()) {
							store.remove(field);
						} else if (field.getBlockingRound() > removeField.getBlockingRound()) {
							BlockingFieldBaseWeb theField = field;
							store.remove(field);
							theField.setBlockingRound(theField.getBlockingRound() - 1);
							store.add(theField);
						}
					}
				}
			});
		buttonContainer.add(removeRoundButton);
		
		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.RECORD_LINKAGE_WIZARD_MODE);
		Button saveButton =
			new Button(inWizardMode ? Constants.NEXT_PAGE_WIZARD_BUTTON_TEXT : Constants.SAVE_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					Button sourceButton = ce.getButton();
					BlockingConfigurationView blockingConfigurationView = (BlockingConfigurationView)sourceButton.getData("this");
					blockingConfigurationView.saveConfiguration();
					Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
					if (inWizard)
						Dispatcher.get().dispatch(AppEvents.MatchFieldConfigurationView);
				}
			});
		saveButton.setData("this", this);
		saveButton.setData("inWizardMode", inWizardMode);
		c.add(saveButton, layoutData);

		if (inWizardMode) {
			Button exitButton =
				new Button(Constants.EXIT_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Dispatcher.get().dispatch(AppEvents.ExitWizardSelected);
					}
				});
			exitButton.setData("this", this);
			c.add(exitButton, layoutData);
		}
		
		cp.setBottomComponent(c);

		numberOfRecordsToSampleEdit.setFieldLabel("No. of records to sample");
		numberOfRecordsToSampleEdit.setAllowBlank(false);
		numberOfRecordsToSampleEdit.setAllowNegative(false);
		numberOfRecordsToSampleEdit.setMinValue(1);
		cp.add(numberOfRecordsToSampleEdit);

		cp.add(buttonContainer);

		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}

	private void buildAddBlockingRoundDialog() {
		addBlockingRoundDialog = new Dialog();
		addBlockingRoundDialog.setBodyBorder(false);
		addBlockingRoundDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addBlockingRoundDialog.setHeading("Add Blocking Round");
		addBlockingRoundDialog.setWidth(370);
		addBlockingRoundDialog.setHeight(300);
		addBlockingRoundDialog.setButtons(Dialog.OKCANCEL);
		addBlockingRoundDialog.setHideOnButtonClick(true);
		addBlockingRoundDialog.setModal(true);
		addBlockingRoundDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				int roundIndex = getCurrentRoundCount(store) + 1;
				for (BlockingFieldBaseWeb field : addRoundGrid.getStore().getModels()) {
					store.add(new BlockingFieldBaseWeb(roundIndex, field.getFieldIndex(),
							field.getLeftFieldName(), field.getRightFieldName()));
				}
			}

			private int getCurrentRoundCount(GroupingStore<BlockingFieldBaseWeb> store) {
				int roundCount = 0;
				for (BlockingFieldBaseWeb field : store.getModels()) {
					if (field.getBlockingRound() > roundCount) {
						roundCount = field.getBlockingRound();
					}
				}
				return roundCount;
			}
		});
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Blocking Round");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(370, 300);
		
		ToolBar toolBar = new ToolBar();
		
		leftFieldCombo.setEmptyText("Select left field...");
		leftFieldCombo.setForceSelection(true);
		leftFieldCombo.setDisplayField("name");
		leftFieldCombo.setWidth(150);
		leftFieldCombo.setStore(leftFieldListStore);
		leftFieldCombo.setTypeAhead(true);
		leftFieldCombo.setTriggerAction(TriggerAction.ALL);
		toolBar.add(leftFieldCombo);

		rightFieldCombo.setEmptyText("Select right field...");
		rightFieldCombo.setForceSelection(true);
		rightFieldCombo.setDisplayField("name");
		rightFieldCombo.setWidth(150);
		rightFieldCombo.setStore(rightFieldListStore);
		rightFieldCombo.setTypeAhead(true);
		rightFieldCombo.setTriggerAction(TriggerAction.ALL);
		toolBar.add(rightFieldCombo);

		toolBar.add(new Button("Add Field", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String leftFieldName = ClientUtils.getSelectedStringOfComboBox(leftFieldCombo);
				if (leftFieldName == null) {
					Info.display("Information", "Please select left field before pressing the \"Add Field\" button.");
					return;
				}
				String rightFieldName = ClientUtils.getSelectedStringOfComboBox(rightFieldCombo);
				if (rightFieldName == null) {
					Info.display("Information", "Please select right field before pressing the \"Add Field\" button.");
					return;
				}
				if (!fieldInList(leftFieldName, rightFieldName, addRoundGrid.getStore())) {
					addRoundGrid.getStore().add(new BlockingFieldBaseWeb(1, addRoundGrid.getStore().getCount() + 1,
							leftFieldName, rightFieldName));
				}
			}

			private boolean fieldInList(String leftFieldName, String rightFieldName,
					ListStore<BlockingFieldBaseWeb> listStore)
			{
				for (BlockingFieldBaseWeb item : listStore.getModels()) {
					if (item.getLeftFieldName().equalsIgnoreCase(leftFieldName) &&
						item.getRightFieldName().equalsIgnoreCase(rightFieldName))
					{
						return true;
					}
				}
				return false;
			}
		}));
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Field", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				BlockingFieldBaseWeb field = addRoundGrid.getSelectionModel().getSelectedItem();
				if (field == null) {
					Info.display("Information","You must first select a field before pressing the \"Remove Field\" button.");
					return;
				}
				addRoundGrid.getStore().remove(field);
			}
		}));
		cp.setTopComponent(toolBar);
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId(BlockingFieldBaseWeb.FIELD_INDEX);
		column.setHeader("Field Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(BlockingFieldBaseWeb.LEFT_FIELD_NAME);
		column.setHeader("Left Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(BlockingFieldBaseWeb.RIGHT_FIELD_NAME);
		column.setHeader("Right Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(150);
		configs.add(column);

		ListStore<BlockingFieldBaseWeb> store = new ListStore<BlockingFieldBaseWeb>();

		ColumnModel cm = new ColumnModel(configs);

		addRoundGrid = new Grid<BlockingFieldBaseWeb>(store, cm);
		addRoundGrid.setStyleAttribute("borderTop", "none");
		addRoundGrid.setBorders(true);
		addRoundGrid.setStripeRows(true);
		cp.add(addRoundGrid);

		addBlockingRoundDialog.add(cp);
	}

}
