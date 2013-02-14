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
package org.openempi.webapp.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.model.FunctionParameterWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class FunctionParametersDialog extends Dialog {

	private Grid<FunctionParameterWeb> grid;
	private ListStore<FunctionParameterWeb> store = new ListStore<FunctionParameterWeb>();
	private EventType endNotificationEvent;

	private Dialog addEditFunctionParameterDialog;
	private Boolean addOrEditParameterMode = true;
	private int editedParameterIndex = 0;
	private FunctionParameterWeb editedParameter;

	private ListStore<ModelPropertyWeb> parameterTypeStore = new ListStore<ModelPropertyWeb>();

	private TextField<String> parameterNameEdit = new TextField<String>();
	private TextField<String> parameterValueEdit = new TextField<String>();
	private ComboBox<ModelPropertyWeb> parameterTypeCombo = new ComboBox<ModelPropertyWeb>();

	private Controller controller;

	public FunctionParametersDialog(final Controller controller) {
		this.controller = controller;

		ParameterTypeWeb[] typeEnums = FunctionParameterWeb.ParameterTypeWeb.values();
		for(int i = 0; i < typeEnums.length; i++) {
			parameterTypeStore.add(new ModelPropertyWeb(typeEnums[i].name()));
		}

		buildAddEditFunctionParameterDialog();

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtons(Dialog.OKCANCEL);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("Function Parameters");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(300);
		setResizable(false);

		ColumnConfig parameterNameColumn = new ColumnConfig(FunctionParameterWeb.PARAMETER_NAME, "Param.Name", 130);
		ColumnConfig parameterValueColumn = new ColumnConfig(FunctionParameterWeb.PARAMETER_VALUE, "Param.Value", 50);
		ColumnConfig parameterTypeColumn = new ColumnConfig(FunctionParameterWeb.PARAMETER_TYPE, "Param.Type", 50);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(parameterNameColumn);
		config.add(parameterValueColumn);
		config.add(parameterTypeColumn);

		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<FunctionParameterWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setHeight(260);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		final Button addFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addOrEditParameterMode = true;
					addEditFunctionParameterDialog.show();

					parameterNameEdit.clear();
					parameterValueEdit.clear();
					parameterTypeCombo.clearSelections();
				}
			});
		buttonContainer.add(addFieldButton);
		final Button editFieldButton =
			new Button("Edit", IconHelper.create("images/folder_edit.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			addOrEditParameterMode = false;
					FunctionParameterWeb editField = grid.getSelectionModel().getSelectedItem();
					if (editField == null) {
						Info.display("Information", "You must first select a parameter to be edited before pressing the \"Edit\" button.");
						return;
					}
					addEditFunctionParameterDialog.show();
					editedParameterIndex = grid.getStore().indexOf(editField);
					editedParameter = editField;

					parameterNameEdit.setValue(editField.getParameterName());
					parameterValueEdit.setValue(editField.getParameterValue());
					parameterTypeCombo.setValue(new ModelPropertyWeb(editField.getParameterType().name()));
		  		}
		    });
		buttonContainer.add(editFieldButton);
		final Button removeFieldButton =
			new Button("Remove", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					FunctionParameterWeb removeField = grid.getSelectionModel().getSelectedItem();
					if (removeField == null) {
						Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove\" button.");
						return;
					}
					for (FunctionParameterWeb field : grid.getStore().getModels()) {
						if (field == removeField) {
							grid.getStore().remove(field);
						}
					}
				}
		    });
		buttonContainer.add(removeFieldButton);
		final Button moveUpFieldButton =
			new Button("Move Up", IconHelper.create("images/arrow_up.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						FunctionParameterWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Up\" button.");
							return;
						}
						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex > 0) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex - 1);
						} else {
							Info.display("Information", "Cannot move up the first field.");
						}
					}
				}
		    });
		buttonContainer.add(moveUpFieldButton);
		final Button moveDownFieldButton =
			new Button("Move Down", IconHelper.create("images/arrow_down.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						FunctionParameterWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Down\" button.");
							return;
						}
						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex >= 0 && selectionIndex < grid.getStore().getCount() - 1) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex + 1);
						} else {
							Info.display("Information", "Cannot move down the last field.");
						}
					}
				}
		    });
		buttonContainer.add(moveDownFieldButton);

		grid.getSelectionModel().addListener(Events.SelectionChange,
			new Listener<SelectionChangedEvent<FunctionParameterWeb>>() {
				public void handleEvent(SelectionChangedEvent<FunctionParameterWeb> be) {
					List<FunctionParameterWeb> selection = be.getSelection();
					Boolean editFieldEnabled = true;
					Boolean removeFieldEnabled = true;
					Boolean moveUpEnabled = true;
					Boolean moveDownEnabled = true;
					if (selection != null) {
						if (selection.size() <= 0) {
							editFieldEnabled = false;
							removeFieldEnabled = false;
							moveUpEnabled = false;
							moveDownEnabled = false;
						} else {
							editFieldEnabled = true;
							removeFieldEnabled = true;
							int selectionIndex = grid.getStore().indexOf(selection.get(0));
							moveUpEnabled = (selectionIndex > 0);
							moveDownEnabled = (selectionIndex < grid.getStore().getCount() - 1);
						}
					}
					editFieldButton.setEnabled(editFieldEnabled);
					removeFieldButton.setEnabled(removeFieldEnabled);
					moveUpFieldButton.setEnabled(moveUpEnabled);
					moveDownFieldButton.setEnabled(moveDownEnabled);
				}
			});

		add(buttonContainer);
		add(grid);

		getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				controller.handleEvent(new AppEvent(endNotificationEvent, store.getModels()));
			}
	    });
	}

	private void buildAddEditFunctionParameterDialog() {
		addEditFunctionParameterDialog = new Dialog();
		addEditFunctionParameterDialog.setBodyBorder(false);
		addEditFunctionParameterDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditFunctionParameterDialog.setHeading("Add/Edit Match Field");
		addEditFunctionParameterDialog.setWidth(450);
		addEditFunctionParameterDialog.setHeight(200);
		addEditFunctionParameterDialog.setButtons(Dialog.OKCANCEL);
		addEditFunctionParameterDialog.setHideOnButtonClick(true);
		addEditFunctionParameterDialog.setModal(true);
		addEditFunctionParameterDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String parameterTypeName = ClientUtils.getSelectedStringOfComboBox(parameterTypeCombo);
				if (parameterTypeName != null) {
					FunctionParameterWeb functionParameterWeb = new FunctionParameterWeb();
					functionParameterWeb.setParameterName(parameterNameEdit.getValue());
					functionParameterWeb.setParameterValue(parameterValueEdit.getValue());
					functionParameterWeb.setParameterType(ParameterTypeWeb.valueOf(parameterTypeName));
					if (addOrEditParameterMode) {
						grid.getStore().add(functionParameterWeb);
					} else {
						grid.getStore().remove(editedParameter);
						grid.getStore().insert(functionParameterWeb, editedParameterIndex);
					}
				} else {
					Info.display("Information", "Couldn't apply! Bad selection in the type popup!");
				}
			}
	    });
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		addEditFunctionParameterDialog.setLayout(formLayout);

		parameterNameEdit.setFieldLabel("Parameter Name");
		addEditFunctionParameterDialog.add(parameterNameEdit);

		parameterValueEdit.setFieldLabel("Parameter Value");
		addEditFunctionParameterDialog.add(parameterValueEdit);

		parameterTypeCombo.setEmptyText("Select type...");
		parameterTypeCombo.setForceSelection(true);
		parameterTypeCombo.setDisplayField("name");
//		parameterTypeCombo.setWidth(150);
		parameterTypeCombo.setStore(parameterTypeStore);
		parameterTypeCombo.setTypeAhead(true);
		parameterTypeCombo.setTriggerAction(TriggerAction.ALL);

		parameterTypeCombo.setFieldLabel("Parameter Type");
		addEditFunctionParameterDialog.add(parameterTypeCombo);
	}

	public void setStore(List<FunctionParameterWeb> parameterStore) {
		store.removeAll();
		store.add(parameterStore);
	}

	public List<FunctionParameterWeb> getStore()
	{
		return store.getModels();
	}

	public void setEndNotificationEvent(EventType endNotificationEvent) {
		this.endNotificationEvent = endNotificationEvent;
	}

}
