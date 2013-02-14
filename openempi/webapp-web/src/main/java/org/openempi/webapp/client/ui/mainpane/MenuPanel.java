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

import org.openempi.webapp.client.ServiceRegistry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Tree;

/**
 * Menu on the left
 */
public class MenuPanel extends Composite {

	public MenuPanel(ServiceRegistry serviceRegistry, final MainPane mainPane) {
		Tree menuTree = new Tree() {
			public void onBrowserEvent(Event e) {
				{
					sinkEvents(Event.ONCLICK);
				}
				super.onBrowserEvent(e);
				if (DOM.eventGetType(e) == Event.ONCLICK) {
					handleEvent(getSelectedItem().getText());
				}
			}

			private void handleEvent(String text) {
//				GWT.log(text,null);
				if ("Screen 1".equalsIgnoreCase(text)) {
					mainPane.showScreen1();
				}
				else if ("Screen 2".equalsIgnoreCase(text)) {
					mainPane.showScreen2();
				}
			}
		};
		
		DockPanel main = new DockPanel();

		menuTree.addItem("Screen 1");
		menuTree.addItem("Screen 2");
		main.add(menuTree, DockPanel.NORTH);
		initWidget(main);
	}

}
