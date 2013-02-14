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
package org.openempi.webapp.client.mvc.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.resources.client.model.Gender;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.XTemplate;
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
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class TemplateSearchView extends View
{
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();
	ListStore<Gender> genders = new ListStore<Gender>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button searchButton;

	
	public TemplateSearchView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.AdvancedSearchView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.AdvancedSearchRenderData) {
			displayRecords((List<PersonWeb>) event.getData());
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
		
		XTemplate tpl = XTemplate.create(getTemplate());
		GWT.log("Maximum depth is " + tpl.getMaxDepth(), null);
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);
   
		configs.add(expander);
		
		ColumnConfig column = new ColumnConfig();
		column.setId("givenName");  
		column.setHeader("First Name");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("familyName");  
		column.setHeader("Last Name");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("dateOfBirth");  
		column.setHeader("Date of Birth");
		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("address1");  
		column.setHeader("Address");  
		column.setWidth(150);
		configs.add(column);
		
		
		column = new ColumnConfig();
		column.setId("city");  
		column.setHeader("City");  
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("state");  
		column.setHeader("State");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("postalCode");  
		column.setHeader("Zip Code");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("country");
		column.setHeader("Country");
		column.setWidth(120);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonWeb>(personStore, cm);
		grid.setStyleAttribute("borderTop", "none");
//		grid.setAutoExpandColumn("givenName");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.addPlugin(expander);
		
		gridContainer.add(grid);
	}

	private String getTemplate() {
		return "<p class=\"identifierBlock\">" +
				"<b>Person Identifiers:</b><br>" +
				"<table class=\"identifierTable\"> " +
				"<tr>" +
				"<th class=\"identifierColumn\">Identifier</th>" +
				"<th class=\"namespaceColumn\">Namespace Identifier</th>" +
				"<th class=\"universalIdentifierColumn\">Universal Identifier</th>" +
				"<th class=\"universalIdentifierTypeColumn\">Universal Identifier Type</th></tr> " +
				"<tpl for=\"personIdentifiers\"> " +
				"<tr> " +
				"<td>{identifier}</td><td>{namespaceIdentifier}</td><td>{universalIdentifier}</td><td>{universalIdentifierTypeCode}</td>" +
				"</tr> " +
				"</tpl>" +
				"<table> " +
				"<tr>" +
				"<th class=\"universalIdentifierTypeColumn\">First Name</th></tr> " +
				"<tpl for=\"linkedPersons\"> " +
				"<tr> " +
				"<td>{givenName}</td>" +
				"</tr> " +
				"</tpl>" +				
				"</p>";
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

		final TextField<String> givenName = new TextField<String>();
		givenName.setFieldLabel("First Name");
		givenName.setTabIndex(1);
		left.add(givenName, formData);

		final DateField dateOfBirth = new DateField();
		dateOfBirth.setFieldLabel("Date of Birth");
		dateOfBirth.setTabIndex(3);
		left.add(dateOfBirth, formData);
		
		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);
		right.setLayout(layout);

		final TextField<String> familyName = new TextField<String>();
		familyName.setFieldLabel("Last Name");
		familyName.setTabIndex(2);
		right.add(familyName, formData);
		
		ComboBox<Gender> gender = new ComboBox<Gender>();
		gender.setFieldLabel("Gender");
		gender.setEmptyText("Select a gender...");
		gender.setDisplayField("name");
		gender.setWidth(100);
		gender.setStore(genders);
		gender.setTypeAhead(true);
		gender.setTriggerAction(TriggerAction.ALL);
		gender.setTabIndex(4);
		right.add(gender, new FormData("40%"));
		
		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		panel.add(main, new FormData("60%"));
		searchButton = new Button("Search", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (givenName.getValue() == null && familyName.getValue() == null) {
					Info.display("Warning", "You must enter at least a partial first or last name before pressing the search button.");
					return;
				}
				if (givenName.getValue().length() <= 0 && familyName.getValue().length() <= 0) {
					Info.display("Warning", "You must enter at least a partial first or last name before pressing the search button.");
					return;
				}
				PersonWeb template = new PersonWeb();
				// TODO <String, Object>
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("givenName", givenName.getValue());
				attributes.put("familyName", familyName.getValue());
				template.setAttributes(attributes);
//				IdentifierDomainTypeCodeWeb idType = null;
//				if (listIdentifierTypes.getSelection().size() > 0) {
//					idType = listIdentifierTypes.getSelection().get(0);
//				}
//				BasicSearchCriteriaWeb searchCriteria = new BasicSearchCriteriaWeb(id, idType);
//				Info.display("Information", "The basic search criteria is: " + searchCriteria);
				controller.handleEvent(new AppEvent(AppEvents.AdvancedSearchInitiate, template));
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
		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 59));
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(102, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
