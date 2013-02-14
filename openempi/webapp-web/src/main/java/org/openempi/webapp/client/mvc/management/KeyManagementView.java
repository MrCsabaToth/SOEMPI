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
package org.openempi.webapp.client.mvc.management;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.Key;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;

public class KeyManagementView extends View
{
	private Grid<Key> grid;
	private ListStore<Key> keyStore = new ListStore<Key>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button addButton;

	
	public KeyManagementView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.KeyManagementView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.KeyManagementAddRenderData) {
			displayRecords((Key) event.getData());
		}
	}

	private void displayRecords(Key key) {
		if (grid == null) {
			setupKeyGrid();
		}
		keyStore.removeAll();
		keyStore.add(key);
		container.layout();
		status.hide();
		addButton.unmask();
	}

	private void setupKeyGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
				
		ColumnConfig column = new ColumnConfig();
		column.setId("id");
		column.setHeader("ID");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("publicKeyPart1Length");
		column.setHeader("PbK Part1 len");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("publicKeyPart2Length");
		column.setHeader("PbK Part2 len");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("publicKeyPart3Length");
		column.setHeader("PbK Part3 len");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("privateKeyPart1Length");
		column.setHeader("PvK Part1 len");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("privateKeyPart2Length");
		column.setHeader("PvK Part2 len");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("privateKeyPart3Length");
		column.setHeader("PvK Part3 len");
		column.setWidth(120);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<Key>(keyStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		
		gridContainer.add(grid);
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		
		FormPanel panel = new FormPanel();
		panel.setFrame(true);  
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);
   
		addButton = new Button("Add Key", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				controller.handleEvent(new AppEvent(AppEvents.KeyManagementAddInitiate));
				status.show();
				addButton.mask();
			}
		});
		status = new Status();
		status.setBusy("please wait...");
		panel.getButtonBar().add(status);
		status.hide();
	    panel.getButtonBar().add(new FillToolItem());
		panel.getButtonBar().add(addButton);
		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 39));
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(80, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
