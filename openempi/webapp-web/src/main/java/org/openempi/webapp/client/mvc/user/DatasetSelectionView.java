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

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.widget.ClientUtils;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;

public class DatasetSelectionView extends View
{
	private LayoutContainer container;
	private ListStore<ModelPropertyWeb> datasetNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> leftDatasetNameCombo = new ComboBox<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> rightDatasetNameCombo = new ComboBox<ModelPropertyWeb>();

	public DatasetSelectionView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.DatasetSelectionView) {
			initUI();
		} else if (event.getType() == AppEvents.DatasetTableNamesArrived) {
			reloadCombos((List<String>) event.getData());
		}
	}

	@SuppressWarnings("unchecked")
	private void reloadCombos(List<String> tableNames) {
		//container.layout();
		// Saving selections if any
		String leftTableName = ClientUtils.getSelectedStringOfComboBox(leftDatasetNameCombo);
		String rightTableName = ClientUtils.getSelectedStringOfComboBox(rightDatasetNameCombo);
		List<ModelPropertyWeb> tableNamesRegistry = (List<ModelPropertyWeb>) Registry.get(Constants.TABLE_NAMES);
		try {
			datasetNameStore.removeAll();
			datasetNameStore.add(tableNamesRegistry);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
		if (leftTableName != null) {
			leftDatasetNameCombo.select(new ModelPropertyWeb(leftTableName));
		}
		if (rightTableName != null) {
			rightDatasetNameCombo.select(new ModelPropertyWeb(rightTableName));
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
		container = new LayoutContainer();
		container.setLayout(new FlowLayout());
		container.setBorders(true);
/*		container.setLayout(new BorderLayout());

		FormData formData = new FormData("100%");
		FormPanel panel = new FormPanel();
		panel.setFrame(true);  
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);
		panel.setHeading("Table selection Panel");
		panel.setWidth(100);
		panel.setLabelWidth(80);
		panel.setFieldWidth(80);

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		left.setLayout(layout);*/

		LabelField leftTableNameLabel = new LabelField("Left Dataset");
		container.add(leftTableNameLabel);

		leftDatasetNameCombo.setEmptyText("Select left dataset...");
		leftDatasetNameCombo.setFieldLabel("Left Dataset");
		leftDatasetNameCombo.setForceSelection(true);
		leftDatasetNameCombo.setDisplayField("name");
		leftDatasetNameCombo.setWidth(140);
		leftDatasetNameCombo.setStore(datasetNameStore);
		leftDatasetNameCombo.setTypeAhead(true);
		leftDatasetNameCombo.setTriggerAction(TriggerAction.ALL);
		leftDatasetNameCombo.addSelectionChangedListener(new SelectionChangedListener<ModelPropertyWeb>() {
			public void selectionChanged(SelectionChangedEvent<ModelPropertyWeb> se) {
				String leftTableName = null;
		        List<ModelPropertyWeb> leftTableNameSelection = se.getSelection();
				if (leftTableNameSelection != null) {
					if (leftTableNameSelection.size() > 0) {
						leftTableName = leftTableNameSelection.get(0).getName();
						Dispatcher.forwardEvent(AppEvents.LeftDatasetSelected, leftTableName);
					}
				}
			}
		});
		//left.add(leftDatasetNameCombo);
		container.add(leftDatasetNameCombo);

		LabelField rightTableNameLabel = new LabelField("Right Dataset");
		container.add(rightTableNameLabel);

		rightDatasetNameCombo.setEmptyText("Select right dataset...");
		rightDatasetNameCombo.setFieldLabel("Right Dataset");
		rightDatasetNameCombo.setForceSelection(true);
		rightDatasetNameCombo.setDisplayField("name");
		rightDatasetNameCombo.setWidth(140);
		rightDatasetNameCombo.setStore(datasetNameStore);
		rightDatasetNameCombo.setTypeAhead(true);
		rightDatasetNameCombo.setTriggerAction(TriggerAction.ALL);
		rightDatasetNameCombo.addSelectionChangedListener(new SelectionChangedListener<ModelPropertyWeb>() {
			public void selectionChanged(SelectionChangedEvent<ModelPropertyWeb> se) {
				String rightTableName = null;
				List<ModelPropertyWeb> rightTableNameSelection = se.getSelection();
				if (rightTableNameSelection != null) {
					if (rightTableNameSelection.size() > 0) {
						rightTableName = rightTableNameSelection.get(0).getName();
						Dispatcher.forwardEvent(AppEvents.RightDatasetSelected, rightTableName);
					}
				}
			}
		});
		container.add(rightDatasetNameCombo);
//		left.add(rightDatasetNameCombo);

//		panel.add(left, formData);
//		container.add(panel, new BorderLayoutData(LayoutRegion.WEST, 200));
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.WEST_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
		Dispatcher.forwardEvent(AppEvents.DatasetTableNameRefreshRequest, null);
	}
}
