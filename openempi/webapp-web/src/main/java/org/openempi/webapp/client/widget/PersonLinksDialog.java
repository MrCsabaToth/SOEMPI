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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonWeb;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class PersonLinksDialog extends Dialog {

	private Grid<PersonLinkWeb> grid;
	private ListStore<PersonLinkWeb> store = new ListStore<PersonLinkWeb>();
	private Dialog displayPersonAttributesDialog;
	private Text formText = new Text();

	private Controller controller;

	private String linkTableName;
	private String leftTableName;
	private String rightTableName;
	private Integer pageSize;
	private NumberField firstResultEdit = new NumberField();
	private NumberField nextResultEdit = new NumberField();
	private NumberField totalNumberOfLinksEdit = new NumberField();
	private Button beginningButton;
	private Button fastBackButton;
	private Button pageBackButton;
	private Button pageForwardButton;
	private Button fastForwardButton;
	private Button endButton;

	public PersonLinksDialog(final Controller controller) {
		this.controller = controller;

		buildDisplayPersonAttributesDialog();

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(50);
		layout.setDefaultWidth(80);
		setLayout(layout);

		setButtons(Dialog.CLOSE);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("Person Links");
		setModal(true);
		setBlinkModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(1000);
		setResizable(false);
		setMaximizable(true);

		ColumnConfig leftIdColumn = new ColumnConfig(PersonLinkWeb.LEFT_PERSON_ID, "Left Id", 60);
		ColumnConfig rightIdColumn = new ColumnConfig(PersonLinkWeb.RIGHT_PERSON_ID, "Right Id", 60);
		ColumnConfig weightColumn = new ColumnConfig(PersonLinkWeb.WEIGHT, "Weight", 130);
		ColumnConfig linkStateColumn = new ColumnConfig(PersonLinkWeb.LINK_STATE, "Link State", 60);
		ColumnConfig binaryVectorColumn = new ColumnConfig(PersonLinkWeb.BINARY_VECTOR, "Binary Vector", 180);
		ColumnConfig contVectorColumn = new ColumnConfig(PersonLinkWeb.CONTINOUS_VECTOR, "Continuous Vector", 360);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(leftIdColumn);
		config.add(rightIdColumn);
		config.add(weightColumn);
		config.add(linkStateColumn);
		config.add(binaryVectorColumn);
		config.add(contVectorColumn);

		GridCellRenderer<PersonLinkWeb> buttonRenderer = new GridCellRenderer<PersonLinkWeb>() {
			private boolean init;

			public Object render(final PersonLinkWeb selectedPersonParam, String property, ColumnData config, final int rowIndex,
					final int colIndex, ListStore<PersonLinkWeb> store, final Grid<PersonLinkWeb> gridParam)
			{
				if (!init) {
					init = true;
					gridParam.addListener(Events.ColumnResize, new Listener<GridEvent<PersonLinkWeb>>() {
						public void handleEvent(GridEvent<PersonLinkWeb> be) {
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
				if (property.equals(PersonLinkWeb.SHOW_LEFT_ATTRIBUTES_BUTTON)) {
					b = new Button("", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							List<Object> params = new ArrayList<Object>();
							params.add(leftTableName);
							params.add(selectedPersonParam.getLeftPersonId());
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowPersonAttributesRequest, params));
						}
					});
					b.setToolTip("Show Left Attributes");
				} else if (property.equals(PersonLinkWeb.SHOW_RIGHT_ATTRIBUTES_BUTTON)) {
					b = new Button("", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							List<Object> params = new ArrayList<Object>();
							params.add(rightTableName);
							params.add(selectedPersonParam.getRightPersonId());
							controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowPersonAttributesRequest, params));
						}
					});
					b.setToolTip("Show Right Attributes");
				}

				if (b != null)
					b.setWidth(gridParam.getColumnModel().getColumnWidth(colIndex) - 10);

				return b;
			}
		};

		ColumnConfig column = new ColumnConfig();
		column.setId(PersonLinkWeb.SHOW_LEFT_ATTRIBUTES_BUTTON);
		column.setHeader("SL");
		column.setToolTip("Show Left Attributes");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		config.add(column);

		column = new ColumnConfig();
		column.setId(PersonLinkWeb.SHOW_RIGHT_ATTRIBUTES_BUTTON);
		column.setHeader("SR");
		column.setToolTip("Show Right Attributes");
		column.setWidth(35);
		column.setRenderer(buttonRenderer);
		config.add(column);

		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<PersonLinkWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setHeight(500);

		ToolBar toolBar = new ToolBar();

		beginningButton =
			new Button("|<", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(0L, true);
				}
			});
		beginningButton.setToolTip("Jump to the beginning of list");
		toolBar.add(beginningButton);
		fastBackButton =
			new Button("<<", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(firstResultEdit.getValue().longValue() - 10 * pageSize, true);
				}
			});
		fastBackButton.setToolTip("Rewind by 10 pages");
		toolBar.add(fastBackButton);
		pageBackButton =
			new Button(" <", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(firstResultEdit.getValue().longValue() - pageSize, true);
				}
			});
		pageBackButton.setToolTip("Rewind by one page");
		toolBar.add(pageBackButton);

		toolBar.add(new SeparatorToolItem());

		firstResultEdit.setAllowBlank(false);
		firstResultEdit.setAllowNegative(false);
		firstResultEdit.setMinValue(0);
		firstResultEdit.setWidth(80);
		firstResultEdit.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				Number number = (Number)be.getValue();
				setFirstAndNextResultEdit(number.longValue(), true);
			}
		});
		toolBar.add(firstResultEdit);

		toolBar.add(new Text(" - "));

		nextResultEdit.setAllowBlank(false);
		nextResultEdit.setAllowNegative(false);
		nextResultEdit.setMinValue(0);
		nextResultEdit.setReadOnly(true);
		nextResultEdit.setWidth(80);
		toolBar.add(nextResultEdit);

		toolBar.add(new Text(" ( "));

		totalNumberOfLinksEdit.setAllowBlank(false);
		totalNumberOfLinksEdit.setAllowNegative(false);
		totalNumberOfLinksEdit.setMinValue(0);
		totalNumberOfLinksEdit.setReadOnly(true);
		totalNumberOfLinksEdit.setWidth(80);
		toolBar.add(totalNumberOfLinksEdit);

		toolBar.add(new Text(" ) "));

		toolBar.add(new SeparatorToolItem());

		pageForwardButton =
			new Button("> ", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(firstResultEdit.getValue().longValue() + pageSize, true);
				}
			});
		pageForwardButton.setToolTip("Forward by one page");
		toolBar.add(pageForwardButton);
		fastForwardButton =
			new Button(">>", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(firstResultEdit.getValue().longValue() + 10 * pageSize, true);
				}
			});
		fastForwardButton.setToolTip("Forward by 10 pages");
		toolBar.add(fastForwardButton);
		endButton =
			new Button(">|", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setFirstAndNextResultEdit(totalNumberOfLinksEdit.getValue().longValue() - pageSize - 1, true);
				}
			});
		endButton.setToolTip("Jump to the end of list");
		toolBar.add(endButton);
		setTopComponent(toolBar);

		add(grid);
	}

	public void setParams(String linkTableName, String leftTableName, String rightTableName,
			Long firstResult, Integer pageSize, Long totalNumberOfLinks) {
		this.linkTableName = linkTableName;
		this.leftTableName = leftTableName;
		this.rightTableName = rightTableName;
		this.pageSize = pageSize;
		totalNumberOfLinksEdit.setValue(totalNumberOfLinks);
		setFirstAndNextResultEdit(firstResult, false);
	}
	
	private void setFirstAndNextResultEdit(Long firstResult, boolean sendEvent) {
		firstResultEdit.setValue(Math.max(firstResult, 0L));
		long totalNumberOfLinks = totalNumberOfLinksEdit.getValue().longValue();
		nextResultEdit.setValue(Math.min(totalNumberOfLinks, firstResult + pageSize));
		Long first = firstResultEdit.getValue().longValue();
		if (first == 0L) {
			beginningButton.disable();
			pageBackButton.disable();
			fastBackButton.disable();
		} else {
			beginningButton.enable();
			pageBackButton.enable();
			fastBackButton.enable();
		}
		if (first + pageSize >= totalNumberOfLinks - 1) {
			endButton.disable();
			pageForwardButton.disable();
			fastForwardButton.disable();
		} else {
			endButton.enable();
			pageForwardButton.enable();
			fastForwardButton.enable();
		}
		if (sendEvent) {
			List<Object> params = new ArrayList<Object>();
			params.add(linkTableName);
			params.add(first);
			params.add(pageSize);
			controller.handleEvent(new AppEvent(AppEvents.PersonMatchShowRecordPairListRequest, params));
		}
	}

	public void setStore(List<PersonLinkWeb> personPairStore) {
		store.removeAll();
		store.add(personPairStore);
	}

	public void displayPersonAttributes(PersonWeb person) {
		java.util.Map<String, Object> attributes = person.getAttributes();
		StringBuilder attributeString = new StringBuilder("Person Id: " + person.getPersonId() + "\r\n");
		if (attributes.size() > 0) {
			Iterator<Entry<String, Object>> it = attributes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
				attributeString.append(pair.getKey() + " :\t" + pair.getValue().toString() + "\r\n");
			}
		}
		formText.setText(attributeString.toString());
		displayPersonAttributesDialog.show();
	}

	private void buildDisplayPersonAttributesDialog() {
		displayPersonAttributesDialog = new Dialog();
		displayPersonAttributesDialog.setHideOnButtonClick(true);
		displayPersonAttributesDialog.setButtonAlign(HorizontalAlignment.LEFT);
		displayPersonAttributesDialog.setIcon(IconHelper.create("images/folder_go.png"));
		displayPersonAttributesDialog.setHeading("Show Person Attributes");
		displayPersonAttributesDialog.setModal(true);
		displayPersonAttributesDialog.setBlinkModal(true);
		displayPersonAttributesDialog.setBodyBorder(true);
		displayPersonAttributesDialog.setBodyStyle("padding: 8px;background: none");
		displayPersonAttributesDialog.setSize(800, 400);
		displayPersonAttributesDialog.setResizable(false);
		displayPersonAttributesDialog.setMaximizable(true);
		displayPersonAttributesDialog.setButtons(Dialog.CLOSE);
		displayPersonAttributesDialog.setLayout(new BorderLayout());
		displayPersonAttributesDialog.add(formText);
	}
}
