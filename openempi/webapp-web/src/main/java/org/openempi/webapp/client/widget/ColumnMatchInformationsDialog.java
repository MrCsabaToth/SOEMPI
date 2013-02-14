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

import org.openempi.webapp.client.model.ColumnMatchInformationWeb;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class ColumnMatchInformationsDialog extends Dialog {
	private Grid<ColumnMatchInformationWeb> grid;
	private ListStore<ColumnMatchInformationWeb> store = new ListStore<ColumnMatchInformationWeb>();

	public ColumnMatchInformationsDialog() {
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtons(Dialog.CLOSE);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("Column Match Informations");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(1000);
		setResizable(false);

		ColumnConfig leftFieldNameColumn =
				new ColumnConfig(ColumnMatchInformationWeb.LEFT_FIELD_NAME, "Left Fld.Name", 130);
		ColumnConfig rightFieldNameColumn =
				new ColumnConfig(ColumnMatchInformationWeb.RIGHT_FIELD_NAME, "Right Fld.Name", 130);
		ColumnConfig fieldTypeColumn =
				new ColumnConfig(ColumnMatchInformationWeb.FIELD_TYPE, "Column Type", 70);
		ColumnConfig fieldTypeModifierColumn =
				new ColumnConfig(ColumnMatchInformationWeb.FIELD_TYPE_MODIFIER, "Typ.Modif.", 70);
		ColumnConfig fieldMeaningColumn =
				new ColumnConfig(ColumnMatchInformationWeb.FIELD_MEANING, "Column Meaning", 130);
		ColumnConfig fsMValueColumn =
				new ColumnConfig(ColumnMatchInformationWeb.FELLEGI_SUNTER_M_VALUE, "F-S M val.", 120);
		ColumnConfig fsUValueColumn =
				new ColumnConfig(ColumnMatchInformationWeb.FELLEGI_SUNTER_U_VALUE, "F-S U val.", 120);
		ColumnConfig bloomFilterProposedMColumn =
				new ColumnConfig(ColumnMatchInformationWeb.BLOOM_FILTER_PROPOSED_M, "BF Proposed M", 60);
		ColumnConfig bloomFilterPossibleMColumn =
				new ColumnConfig(ColumnMatchInformationWeb.BLOOM_FILTER_POSSIBLE_M, "BF Possible M", 70);
		ColumnConfig bloomFilterFinalMColumn =
				new ColumnConfig(ColumnMatchInformationWeb.BLOOM_FILTER_FINAL_M, "BF Final M", 60);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(leftFieldNameColumn);
		config.add(rightFieldNameColumn);
		config.add(fieldTypeColumn);
		config.add(fieldTypeModifierColumn);
		config.add(fieldMeaningColumn);
		config.add(fsMValueColumn);
		config.add(fsUValueColumn);
		config.add(bloomFilterProposedMColumn);
		config.add(bloomFilterPossibleMColumn);
		config.add(bloomFilterFinalMColumn);

		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<ColumnMatchInformationWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setHeight(500);

		add(grid);
	}

	public void setStore(List<ColumnMatchInformationWeb> columnInformationStore) {
		store.removeAll();
		store.add(columnInformationStore);
	}

}
