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
package org.openempi.webapp.client.ui.mainpane;

import org.openempi.webapp.client.ClientState;
import org.openempi.webapp.client.ServiceRegistry;
import org.openempi.webapp.client.ui.screens.Screen1;
import org.openempi.webapp.client.ui.screens.Screen2;
import org.openempi.webapp.client.ui.widget.Pane;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point and flow control of our app.
 * Also instantiates and injects ServiceRegistry and ClientState into screens
 * @author BDBRODS
 *
 */
public class MainPane extends Pane {

    private HeaderPane headerPane;
    private StatusBarPane statusBarPane;
    private SimplePanel centerPane;
//    private CategoryPane menuPane;
    private MenuPanel menuPanel; //menu
    
	private Screen1 screen1;
	private Screen2 screen2;

	public MainPane(ClientState clientState, ServiceRegistry serviceRegistry) {
        super(clientState, serviceRegistry);

        DockPanel mainPanel = new DockPanel();
		mainPanel.setBorderWidth(5);    	
		mainPanel.setSize("100%", "100%");
		
    	headerPane = new HeaderPane("GWT Maven Sample", clientState, serviceRegistry);
        mainPanel.add(headerPane, DockPanel.NORTH);
    	mainPanel.setCellHeight(headerPane, "30px");
		mainPanel.setCellHorizontalAlignment(headerPane, DockPanel.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(headerPane, DockPanel.ALIGN_MIDDLE);
    	
    	statusBarPane = new StatusBarPane();
		mainPanel.add(statusBarPane, DockPanel.SOUTH); 
		mainPanel.setCellHeight(statusBarPane, "25px");
		mainPanel.setCellHorizontalAlignment(statusBarPane, DockPanel.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(statusBarPane, DockPanel.ALIGN_MIDDLE);
    		
		HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSplitPosition("150px");
		centerPane = new SimplePanel();
		centerPane.add(new Label("right panel"));
		horizontalSplitPanel.setRightWidget(centerPane);
		menuPanel = new MenuPanel(serviceRegistry, this);
		
		horizontalSplitPanel.setLeftWidget(menuPanel);
		mainPanel.add(horizontalSplitPanel, DockPanel.CENTER);
			
		screen1 = new Screen1(clientState, serviceRegistry);
		screen2 = new Screen2(clientState, serviceRegistry);
		initWidget(mainPanel);
	}
	
    /**
     * Shows the login pane.
     */
	public void showScreen1() {
        centerPane.setWidget(screen1);
	}

	public void showScreen2() {
		centerPane.setWidget(screen2);
	}	
    

}
