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
package org.openempi.webapp.client.mvc.search;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.RealSearchCriteria;
import org.openempi.webapp.client.widget.ClientUtils;

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
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;

public class RealSearchView extends View
{
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button searchButton;

	private ListStore<ModelPropertyWeb> attributeNameStore = new ListStore<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> attributeNameCombo = new ComboBox<ModelPropertyWeb>();
	private String tableName;

	public RealSearchView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.RealSearchView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.RealSearchRenderData) {
			displayRecords((List<PersonWeb>) event.getData());
		} else if (event.getType() == AppEvents.LeftDatasetColumnNamesArrived) {
			List<ModelPropertyWeb> fieldNames = (List<ModelPropertyWeb>) event.getData();
			reloadAttributeNameCombo(fieldNames);
		} else if (event.getType() == AppEvents.LeftDatasetSelected) {
			tableName = (String)event.getData();
		}
	}

	private void reloadAttributeNameCombo(List<ModelPropertyWeb> columnNameModelPropertyList) {
		String attributeName = ClientUtils.getSelectedStringOfComboBox(attributeNameCombo);
		List<ModelPropertyWeb> attributeNames = columnNameModelPropertyList;
		try {
			attributeNameStore.removeAll();
			attributeNameStore.add(attributeNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
		if (attributeName != null) {
			attributeNameCombo.select(new ModelPropertyWeb(attributeName));
		}
	}

	private void displayRecords(List<PersonWeb> persons) {
		if (grid == null) {
			setupPersonGrid();
		}
		personStore.removeAll();
		personStore.add(persons);
		container.layout();
		status.hide();
		searchButton.unmask();
	}

	private void setupPersonGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();
		column.setId(PersonWeb.PERSON_ID);
		column.setHeader("Person ID");
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId(PersonWeb.ATTRIBUTES);
		column.setHeader("Attributes");
		column.setWidth(800);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonWeb>(personStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		
		gridContainer.add(grid);
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		FormData formData = new FormData("100%");
		
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		
		FormPanel panel = new FormPanel();
		panel.setFrame(true);  
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);
   
		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		left.setLayout(layout);

		final TextField<String> attributeValue = new TextField<String>();
		attributeValue.setFieldLabel("Attribute Value");
		left.add(attributeValue, formData);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);
		right.setLayout(layout);

		attributeNameCombo.setEmptyText("Select attribute...");
		attributeNameCombo.setForceSelection(true);
		attributeNameCombo.setDisplayField("name");
//		attributeNameCombo.setWidth(150);
		attributeNameCombo.setStore(attributeNameStore);
		attributeNameCombo.setTypeAhead(true);
		attributeNameCombo.setTriggerAction(TriggerAction.ALL);

		attributeNameCombo.setFieldLabel("Attribute Name");
		right.add(attributeNameCombo);

		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		panel.add(main, new FormData("60%"));
		searchButton = new Button("Search", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String attribName = ClientUtils.getSelectedStringOfComboBox(attributeNameCombo);
				if (attribName == null) {
					Info.display("Warning", "You must select/enter an attribute name before pressing the search button.");
					return;
				}
				String attributValueString = attributeValue.getValue();
				if (attributValueString == null) {
					Info.display("Warning", "You must enter at least a partial attribute value before pressing the search button.");
					return;
				}
				RealSearchCriteria searchCriteria = new RealSearchCriteria(attribName, attributValueString);
//				Info.display("Information", "The basic search criteria is: " + searchCriteria);

				List<Object> params = new ArrayList<Object>();
				params.add(tableName);
				params.add(searchCriteria);
				controller.handleEvent(new AppEvent(AppEvents.RealSearchInitiate, params));
				status.show();
				searchButton.mask();
			}
		});
		status = new Status();
		status.setBusy("please wait...");
		panel.getButtonBar().add(status);
		status.hide();
	    panel.getButtonBar().add(new FillToolItem());
		panel.getButtonBar().add(searchButton);
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
