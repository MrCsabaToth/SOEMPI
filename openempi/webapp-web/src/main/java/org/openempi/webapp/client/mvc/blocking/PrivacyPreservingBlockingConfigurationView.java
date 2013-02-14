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
package org.openempi.webapp.client.mvc.blocking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.model.PrivacyPreservingBaseField;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingFieldWeb;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingSettingsWeb;
import org.openempi.webapp.client.widget.ClientUtils;
import org.openempi.webapp.client.widget.VType;
import org.openempi.webapp.client.widget.VTypeValidator;

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
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;

public class PrivacyPreservingBlockingConfigurationView extends View
{
	private NumberField numberOfBlockingBitsEdit = new NumberField();
	private NumberField numberOfRunsEdit = new NumberField();
	private Grid<PrivacyPreservingBaseField> grid;
	private GroupingStore<PrivacyPreservingBaseField> store = new GroupingStore<PrivacyPreservingBaseField>();
	private Dialog addFieldDialog;
	private ListStore<ModelPropertyWeb> leftFieldListStore = new ListStore<ModelPropertyWeb>();
	private ListStore<ModelPropertyWeb> rightFieldListStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> leftFieldCombo = new ComboBox<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> rightFieldCombo = new ComboBox<ModelPropertyWeb>();
	private Grid<PrivacyPreservingBaseField> addFieldGrid;

	private LayoutContainer container;
	
	public PrivacyPreservingBlockingConfigurationView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.PrivacyPreservingBlockingConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.PrivacyPreservingBlockingConfigurationReceived) {
			store.removeAll();
			PrivacyPreservingBlockingSettingsWeb ppbSettings = (PrivacyPreservingBlockingSettingsWeb) event.getData();
			numberOfBlockingBitsEdit.setValue(ppbSettings.getNumberOfBlockingBits());
			numberOfRunsEdit.setValue(ppbSettings.getNumberOfRuns());
			for (PrivacyPreservingBlockingFieldWeb ppbField : ppbSettings.getPrivacyPreservingBlockingFields()) {
				for (Integer bit : ppbField.getBits()) {
					store.add(new PrivacyPreservingBaseField(ppbField.getLeftFieldName(), ppbField.getRightFieldName(), bit));
				}
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
    	PrivacyPreservingBlockingSettingsWeb ppbSettings = new PrivacyPreservingBlockingSettingsWeb();
    	ppbSettings.setNumberOfBlockingBits(numberOfBlockingBitsEdit.getValue().intValue());
    	ppbSettings.setNumberOfRuns(numberOfRunsEdit.getValue().intValue());
    	List<PrivacyPreservingBaseField> configuration = grid.getStore().getModels();
    	Map<String,PrivacyPreservingBlockingFieldWeb> blockingFieldStore = new HashMap<String,PrivacyPreservingBlockingFieldWeb>();
    	// We implicitly assume here that only one right field corresponds to a left field
    	for(PrivacyPreservingBaseField ppbField : configuration) {
    		String fieldName = ppbField.getLeftFieldName();
    		if (blockingFieldStore.containsKey(fieldName)) {
    			List<Integer> bitList = blockingFieldStore.get(fieldName).getBits();
    			bitList.add(ppbField.getBitIndex());
    		} else {
    			PrivacyPreservingBlockingFieldWeb ppbFieldWeb = new PrivacyPreservingBlockingFieldWeb();
    			ppbFieldWeb.setLeftFieldName(ppbField.getLeftFieldName());
    			ppbFieldWeb.setRightFieldName(ppbField.getRightFieldName());
    			List<Integer> bitList = new ArrayList<Integer>();
    			bitList.add(ppbField.getBitIndex());
    			ppbFieldWeb.setBits(bitList);
    			blockingFieldStore.put(fieldName, ppbFieldWeb);
    		}
    	}
    	List<PrivacyPreservingBlockingFieldWeb> ppbFieldsWeb = new ArrayList<PrivacyPreservingBlockingFieldWeb>();
    	for (Map.Entry<String, PrivacyPreservingBlockingFieldWeb> mapEntry : blockingFieldStore.entrySet())
    	{
    		ppbFieldsWeb.add(mapEntry.getValue());
    	}
    	ppbSettings.setPrivacyPreservingBlockingFields(ppbFieldsWeb);
    	controller.handleEvent(new AppEvent(AppEvents.PrivacyPreservingBlockingConfigurationSave, ppbSettings));
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		store.groupBy(PrivacyPreservingBaseField.LEFT_FIELD_NAME);
		controller.handleEvent(new AppEvent(AppEvents.PrivacyPreservingBlockingConfigurationRequest));
		
		buildAddBlockingFieldDialog();
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.LEFT_FIELD_NAME);
		column.setHeader("Left Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.RIGHT_FIELD_NAME);
		column.setHeader("Right Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.BIT_INDEX);
		column.setHeader("Bit Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(40);
		configs.add(column);
		
		final ColumnModel cm = new ColumnModel(configs);

		GroupingView view = new GroupingView();
		view.setShowGroupedColumn(true);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String f = cm.getColumnById(data.field).getHeader();
				String l = data.models.size() == 1 ? "Bit" : "Bits";
				return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
			}});

		grid = new Grid<PrivacyPreservingBaseField>(store, cm);
		grid.setView(view);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.setAutoWidth(true);
		grid.setHeight(230);
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Privacy Preserving Blocking Configuration");
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
		final Button addFieldButton =
			new Button("Add Field", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
	        	@Override
	        	public void componentSelected(ButtonEvent ce) {
	        		// Make sure we are starting with a clean slate
	        		addFieldGrid.getStore().removeAll();
	        		addFieldDialog.show();
	        	}
			});
		buttonContainer.add(addFieldButton);
		final Button removeFieldButton =
			new Button("Remove Field", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
	        	@Override
	        	public void componentSelected(ButtonEvent ce) {
	        		PrivacyPreservingBaseField removeField = grid.getSelectionModel().getSelectedItem();
	        		if (removeField == null) {
	        			Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove Field\" button.");
	        			return;
	        		}
	        		for (PrivacyPreservingBaseField field : grid.getStore().getModels()) {
	        			if (field.getLeftFieldName().equals(removeField.getLeftFieldName()) &&
	        				field.getRightFieldName().equals(removeField.getRightFieldName()))
	        			{
	        				store.remove(field);
	        			}
	        		}
	        	}
			});
		buttonContainer.add(removeFieldButton);

		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		Integer wizardMode = (Integer)Registry.get(Constants.WIZARD_MODE);
		boolean inWizardMode = (wizardMode == Constants.RECORD_LINKAGE_WIZARD_MODE);

	    if (inWizardMode) {
			Button previousButton =
				new Button(Constants.PREVIOUS_PAGE_WIZARD_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
			  		@Override
			  		public void componentSelected(ButtonEvent ce) {
			  			Button sourceButton = ce.getButton();
			  			PrivacyPreservingBlockingConfigurationView privacyPreservingBlockingConfigurationView =
			  				(PrivacyPreservingBlockingConfigurationView)sourceButton.getData("this");
			  			privacyPreservingBlockingConfigurationView.saveConfiguration();
			  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
			  			if (inWizard)
							Dispatcher.get().dispatch(AppEvents.BlockingConfigurationView);
			  		}
			    });
			previousButton.setData("this", this);
			previousButton.setData("inWizardMode", inWizardMode);
			c.add(previousButton, layoutData);
	    }

		Button saveButton =
			new Button(inWizardMode ? Constants.NEXT_PAGE_WIZARD_BUTTON_TEXT : Constants.SAVE_BUTTON_TEXT, IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
		        @Override
		        public void componentSelected(ButtonEvent ce) {
		  			Button sourceButton = ce.getButton();
		  			PrivacyPreservingBlockingConfigurationView privacyPreservingBlockingConfigurationView =
		  				(PrivacyPreservingBlockingConfigurationView)sourceButton.getData("this");
		  			privacyPreservingBlockingConfigurationView.saveConfiguration();
		  			Boolean inWizard = (Boolean)sourceButton.getData("inWizardMode");
		  			if (inWizard)
						Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
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

		numberOfBlockingBitsEdit.setFieldLabel("No. of bits to choose from");
		numberOfBlockingBitsEdit.setAllowBlank(false);
		numberOfBlockingBitsEdit.setAllowNegative(false);
		numberOfBlockingBitsEdit.setMinValue(2);
		cp.add(numberOfBlockingBitsEdit);

		numberOfRunsEdit.setFieldLabel("No. of runs to repeat");
		numberOfRunsEdit.setAllowBlank(false);
		numberOfRunsEdit.setAllowNegative(false);
		numberOfRunsEdit.setMinValue(1);
		cp.add(numberOfRunsEdit);

		cp.add(buttonContainer);

		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}

	private void buildAddBlockingFieldDialog() {
		addFieldDialog = new Dialog();
		addFieldDialog.setBodyBorder(false);
		addFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addFieldDialog.setHeading("Add Blocking Field");
		addFieldDialog.setWidth(450);
		addFieldDialog.setHeight(500);
		addFieldDialog.setButtons(Dialog.OKCANCEL);
		addFieldDialog.setHideOnButtonClick(true);
		addFieldDialog.setModal(true);
		addFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	        @Override
	        public void componentSelected(ButtonEvent ce) {
	        	for (PrivacyPreservingBaseField field : addFieldGrid.getStore().getModels()) {
	        		store.add(new PrivacyPreservingBaseField(field.getLeftFieldName(), field.getRightFieldName(), field.getBitIndex()));
	        	}
	        }
	    });
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		addFieldDialog.setLayout(formLayout);
		
		leftFieldCombo.setEmptyText("Select left field...");
		leftFieldCombo.setForceSelection(true);
		leftFieldCombo.setDisplayField("name");
		leftFieldCombo.setWidth(150);
		leftFieldCombo.setStore(leftFieldListStore);
		leftFieldCombo.setTypeAhead(true);
		leftFieldCombo.setTriggerAction(TriggerAction.ALL);
		leftFieldCombo.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		leftFieldCombo.setFieldLabel("Leftt Field Name");

		rightFieldCombo.setEmptyText("Select right field...");
		rightFieldCombo.setForceSelection(true);
		rightFieldCombo.setDisplayField("name");
		rightFieldCombo.setWidth(150);
		rightFieldCombo.setStore(rightFieldListStore);
		rightFieldCombo.setTypeAhead(true);
		rightFieldCombo.setTriggerAction(TriggerAction.ALL);
		rightFieldCombo.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
		rightFieldCombo.setFieldLabel("Right Field Name");

		final NumberField bitIndexEdit = new NumberField();
		bitIndexEdit.setFieldLabel("Bit Index");

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		final Button addBitButton =
			new Button("Add Bit", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
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
		        	Integer bitIndex = bitIndexEdit.getValue().intValue();
		        	if (bitIndex < 0) { // TODO: check for bloomfilter length also
		        		Info.display("Information", "Please enter valid bit index before pressing the \"Add Bit\" button.");
		        		return;
		        	}
		        	if (!fieldInList(leftFieldName, rightFieldName, bitIndex, addFieldGrid.getStore()) &&
		        		!fieldInList(leftFieldName, rightFieldName, bitIndex, grid.getStore()))
		        	{
		        		addFieldGrid.getStore().add(new PrivacyPreservingBaseField(leftFieldName, rightFieldName, bitIndex));
		        	}
		        }
	
				private boolean fieldInList(String leftFieldName, String rightFieldName,
						Integer bitIndex, ListStore<PrivacyPreservingBaseField> listStore)
				{
					for (PrivacyPreservingBaseField item : listStore.getModels()) {
						if (item.getLeftFieldName().equalsIgnoreCase(leftFieldName) &&
							item.getRightFieldName().equalsIgnoreCase(rightFieldName) &&
							item.getBitIndex() == bitIndex)
						{
							return true;
						}
					}
					return false;
				}
		    });
		buttonContainer.add(addBitButton);
		final Button removeBitButton =
			new Button("Remove Bit", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
		        @Override
		        public void componentSelected(ButtonEvent ce) {
		        	PrivacyPreservingBaseField field = addFieldGrid.getSelectionModel().getSelectedItem();
		        	if (field == null) {
		        		Info.display("Information", "You must first select a field before pressing the \"Remove Field\" button.");
		        		return;
		        	}
		        	addFieldGrid.getStore().remove(field);
		        }
		    });
		buttonContainer.add(removeBitButton);
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.LEFT_FIELD_NAME);
		column.setHeader("Left Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.RIGHT_FIELD_NAME);
		column.setHeader("Right Field Name");
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PrivacyPreservingBaseField.BIT_INDEX);
		column.setHeader("Bit Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(40);
		configs.add(column);

		ListStore<PrivacyPreservingBaseField> store = new ListStore<PrivacyPreservingBaseField>();

		ColumnModel cm = new ColumnModel(configs);

		addFieldGrid = new Grid<PrivacyPreservingBaseField>(store, cm);
		addFieldGrid.setStyleAttribute("borderTop", "none");
		addFieldGrid.setBorders(true);
		addFieldGrid.setStripeRows(true);
		addFieldGrid.setAutoWidth(true);
		addFieldGrid.setHeight(260);

		addFieldDialog.add(leftFieldCombo);
		addFieldDialog.add(rightFieldCombo);
		addFieldDialog.add(bitIndexEdit);

		addFieldDialog.add(buttonContainer);

		addFieldDialog.add(addFieldGrid);
	}

}
