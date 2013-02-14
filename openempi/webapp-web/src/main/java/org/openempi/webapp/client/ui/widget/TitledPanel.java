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
package org.openempi.webapp.client.ui.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class TitledPanel extends Composite {

	private final static int TITLE_ROW = 0;
	private final static int CONTENT_ROW = 1;

    private final static String STYLE_NAME = "TitledPanel";
	private final static String TITLE_STYLE_NAME = "TitleText";
	private final static String CONTENT_STYLE_NAME = "Content";
	private final static String TOOLBAR_STYLE_NAME = "Toolbar";
	private final static String TOOL_BUTTON_STYLE_NAME = "ToolButton";
	
	private final Label titleLabel;
	private final Grid grid;
	private final DockPanel title;
	private final HorizontalPanel toolbar;

	public TitledPanel() {
		this("");
	}
	
	public TitledPanel(String titleText) {
		this(titleText, null);
	}

	public TitledPanel(String titleText, Widget content) {
		titleLabel = new Label(titleText);
        titleLabel.setStyleName(TITLE_STYLE_NAME);
		toolbar = new HorizontalPanel();
		toolbar.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		toolbar.setSpacing(0);
		toolbar.setBorderWidth(0);
		toolbar.setStyleName(TOOLBAR_STYLE_NAME);
		
		title = new DockPanel();
		title.setStyleName(TITLE_STYLE_NAME);
		title.add(titleLabel, DockPanel.CENTER);
		title.setCellVerticalAlignment(titleLabel, DockPanel.ALIGN_MIDDLE);
		title.setCellWidth(titleLabel, "100%");
		title.add(toolbar, DockPanel.EAST);
		title.setWidth("100%");
		
        grid = new Grid(2, 1);
        grid.setBorderWidth(0);
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.setWidget(TITLE_ROW, 0, title);
        grid.getCellFormatter().setWidth(TITLE_ROW, 0, "100%");
        if (content != null) {
        	grid.setWidget(CONTENT_ROW, 0, content);
        }
        grid.getCellFormatter().setWidth(CONTENT_ROW, 0, "100%");
        grid.getCellFormatter().setHeight(CONTENT_ROW, 0, "100%");
        grid.getCellFormatter().setStyleName(CONTENT_ROW, 0, CONTENT_STYLE_NAME);
        
        initWidget(grid);
        setStyleName(STYLE_NAME);
	}
	
	public void setTitleText(String text) {
		titleLabel.setText(text);
	}
	
	public void setContent(Widget content) {
		grid.setWidget(CONTENT_ROW, 0, content);
	}
	
	public void setContentVerticalAlignment(HasVerticalAlignment.VerticalAlignmentConstant alignment) {
		grid.getCellFormatter().setVerticalAlignment(CONTENT_ROW, 0, alignment);
	}
	
	public void setContentHorizontalAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
		grid.getCellFormatter().setHorizontalAlignment(CONTENT_ROW, 0, alignment);
	}
	
	public PushButton addToolButton(String text, String title, ClickListener clickListener) {
		PushButton button = new PushButton(text, clickListener);
		if (title != null) {
			button.setTitle(title);
		}
		addToolButton(button);
		return button;
	}
	
	public PushButton addToolButton(Image image, ClickListener clickListener) {
		PushButton button = new PushButton(image, clickListener);
		addToolButton(button);
		return button;
	}
	
	public void addToolButton(PushButton button) {
		button.setStyleName(TOOL_BUTTON_STYLE_NAME);
		toolbar.add(button);
		toolbar.setCellVerticalAlignment(button, HorizontalPanel.ALIGN_MIDDLE);
	}

}

