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
package org.openempi.webapp.client.mvc.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.resources.client.model.Gender;
import org.openempi.webapp.resources.client.model.State;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;

public class AddPersonView extends View
{
	private LayoutContainer container;
	private TextField<String> first;
	private TextField<String> last;
	private DateField dateOfBirth;
	private ComboBox<Gender> gender;
	private TextField<String> address1;
	private TextField<String> address2;
	private TextField<String> city;
	private ComboBox<State> state;
	private TextField<String> zip;
	private TextField<String> identifier;
	private Button submitButton;
	private Button cancelButton;

	private ListStore<Gender> genders = new ListStore<Gender>();
	private ListStore<State> states = new ListStore<State>();
	private String tableName;

	public AddPersonView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
		states.add(InputFormData.getStates());
	}
	
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.AddPersonView) {
			initUI();
		} else if (event.getType() == AppEvents.LeftDatasetSelected) {
			tableName = (String)event.getData();
		} else if (event.getType() == AppEvents.AddPersonComplete) {
			String message = event.getData();
			Info.display("Information", "Person was successfully added with message " + message);
		}
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
		FormData formData = new FormData("100%");
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

		first = new TextField<String>();
		first.setFieldLabel("First Name");
		first.setTabIndex(1);
		left.add(first, formData);
		
		dateOfBirth = new DateField();
		dateOfBirth.setFieldLabel("Date of Birth");
		dateOfBirth.setTabIndex(3);
		left.add(dateOfBirth, formData);
		
		address1 = new TextField<String>();
		address1.setFieldLabel("Address 1");
		address1.setTabIndex(5);
		left.add(address1, formData);
		
		city = new TextField<String>();
		city.setFieldLabel("City");
		city.setTabIndex(7);
		left.add(city, formData);
		
		identifier = new TextField<String>();
		identifier.setFieldLabel("Identifier");
		identifier.setTabIndex(10);
		left.add(identifier, formData);
		
		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingRight", "10px");
		layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		right.setLayout(layout);
		
		last = new TextField<String>();
		last.setFieldLabel("Last Name");
		last.setTabIndex(2);
		right.add(last, formData);
		
		gender = new ComboBox<Gender>();
		gender.setFieldLabel("Gender");
		gender.setEmptyText("Select a gender...");
		gender.setDisplayField("name");
		gender.setWidth(100);
		gender.setStore(genders);
		gender.setTypeAhead(true);
		gender.setTriggerAction(TriggerAction.ALL);
		gender.setTabIndex(4);
		right.add(gender, new FormData("40%"));
		
		address2 = new TextField<String>();
		address2.setFieldLabel("Address 2");
		address2.setTabIndex(6);
		right.add(address2, formData);

		LayoutContainer cityZip = new LayoutContainer();
		cityZip.setLayout(new ColumnLayout());
		
		LayoutContainer leftCityZip = new LayoutContainer();
		leftCityZip.setStyleAttribute("paddingRight", "10px");
		FormLayout innerLayout = new FormLayout();
		innerLayout.setLabelAlign(LabelAlign.TOP);
		leftCityZip.setLayout(innerLayout);
		
		state = new ComboBox<State>();
		state.setFieldLabel("State");
		state.setEmptyText("Select a state...");  
		state.setDisplayField("name");  
		state.setWidth(150);  
		state.setStore(states);
		state.setTypeAhead(true);  
	    state.setTriggerAction(TriggerAction.ALL);
	    state.setTabIndex(8);
	    leftCityZip.add(state, new FormData("100%"));

	    LayoutContainer rightCityZip = new LayoutContainer();
	    rightCityZip.setStyleAttribute("paddingRight", "0");
	    innerLayout = new FormLayout();
		innerLayout.setLabelAlign(LabelAlign.TOP);
	    rightCityZip.setLayout(innerLayout);
		
	    zip = new TextField<String>();
		zip.setFieldLabel("Zip Code");
		zip.setTabIndex(9);
		rightCityZip.add(zip, new FormData("40%"));
		
	    cityZip.add(leftCityZip, new ColumnData(.5));
	    cityZip.add(rightCityZip, new ColumnData(.5));
	    right.add(cityZip, formData);
	    
		main.add(left, new ColumnData(.5));
		main.add(right, new ColumnData(.5));
		
		panel.add(main, formData);
		submitButton = new Button("Save", new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				PersonWeb person = new PersonWeb();
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("givenName", first.getValue());
				attributes.put("familyName", last.getValue());
				attributes.put("dateOfBirth", dateOfBirth.getValue());
				Gender genderValue = gender.getValue();
				if (genderValue != null) {
					attributes.put("gender", genderValue.getCode());
				}
				attributes.put("address1", address1.getValue());
				attributes.put("address2", address2.getValue());
				attributes.put("city", city.getValue());
				if (state.getValue() != null) {
					attributes.put("atate", state.getValue().getAbbr());
				}
				attributes.put("postalCode", zip.getValue());
				person.setAttributes(attributes);

				List<Object> params = new ArrayList<Object>();
				params.add(tableName);
				params.add(person);
				controller.handleEvent(new AppEvent(AppEvents.AddPersonInitiate, params));
				submitButton.mask();
			}		
		});
		
		cancelButton = new Button("Cancel", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				clearFormFields();
			}
		});
		panel.addButton(submitButton);
		panel.addButton(cancelButton);
	    
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		container.add(panel, new BorderLayoutData(LayoutRegion.CENTER));
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);		
	}
	
	private void clearFormFields() {
		first.clear();
		last.clear();
		dateOfBirth.clear();
		gender.clear();
		address1.clear();
		address2.clear();
		city.clear();
		state.clear();
		zip.clear();
		identifier.clear();
	}
}
