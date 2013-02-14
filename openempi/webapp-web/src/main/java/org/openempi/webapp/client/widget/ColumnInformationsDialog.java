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

import org.openempi.webapp.client.model.ColumnInformationWeb;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class ColumnInformationsDialog extends Dialog {
	private Grid<ColumnInformationWeb> grid;
	private ListStore<ColumnInformationWeb> store = new ListStore<ColumnInformationWeb>();

	public ColumnInformationsDialog() {
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtons(Dialog.CLOSE);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("Column Informations");
		setModal(true);
		setBlinkModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(1000);
		setResizable(true);
		setMaximizable(true);

		ColumnConfig fieldNameColumn =
				new ColumnConfig(ColumnInformationWeb.FIELD_NAME, "Column Name", 130);
		ColumnConfig fieldTypeColumn =
				new ColumnConfig(ColumnInformationWeb.FIELD_TYPE, "Column Type", 70);
		ColumnConfig fieldTypeModifierColumn =
				new ColumnConfig(ColumnInformationWeb.FIELD_TYPE_MODIFIER, "Typ.Modif.", 70);
		ColumnConfig fieldMeaningColumn =
				new ColumnConfig(ColumnInformationWeb.FIELD_MEANING, "Column Meaning", 130);
		ColumnConfig fieldTransformationColumn =
				new ColumnConfig(ColumnInformationWeb.FIELD_TRANSFORMATION, "Column Transformation", 230);
		ColumnConfig bloomFilterMParameterColumn =
				new ColumnConfig(ColumnInformationWeb.BLOOM_FILTER_M_PARAMETER, "BloomF M", 60);
		ColumnConfig bloomFilterKParameterColumn =
				new ColumnConfig(ColumnInformationWeb.BLOOM_FILTER_K_PARAMETER, "BloomF K", 60);
		ColumnConfig averageFieldLengthColumn =
				new ColumnConfig(ColumnInformationWeb.AVERAGE_FIELD_LENGTH, "Avg.Col.Len.", 80);
		ColumnConfig numberOfMissingColumn =
				new ColumnConfig(ColumnInformationWeb.NUMBER_OF_MISSING, "No.Of.Missing", 80);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(fieldNameColumn);
		config.add(fieldTypeColumn);
		config.add(fieldTypeModifierColumn);
		config.add(fieldMeaningColumn);
		config.add(fieldTransformationColumn);
		config.add(bloomFilterMParameterColumn);
		config.add(bloomFilterKParameterColumn);
		config.add(averageFieldLengthColumn);
		config.add(numberOfMissingColumn);

		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<ColumnInformationWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setHeight(500);

		add(grid);
	}

	public void setStore(List<ColumnInformationWeb> columnInformationStore) {
		store.removeAll();
		store.add(columnInformationStore);
	}

}
