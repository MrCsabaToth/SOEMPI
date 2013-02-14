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

package org.openempi.webapp.client;

import org.openempi.webapp.client.ui.mainpane.MainPane;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The entry point for the app.
 */
public class ClientMain implements EntryPoint {

	private SimplePanel main;
	private MainPane mainPane;

	public void onModuleLoad() {

        //get better exception handling
        setUncaughtExceptionHandler();
        
        // Ha ha, who needs Spring!  Seriously, integrating it on the GWT client side
        // is more trouble than it's worth!
        //
        // Bootstrap our dependencies (every client gets their own instance)
        mainPane = new MainPane(new ClientState(), new ServiceRegistry());

        main = new SimplePanel();
        main.setSize("100%", "100%");
        main.setWidget(mainPane);

        RootPanel.get().add(main);
	}


	/**
	 * better exception handling
	 */ 
    private void setUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(Throwable e) {
                if (e.getCause() != null && e.getCause() instanceof StatusCodeException) {
                    GWT.log("Exception (server-side) :(", e);
                    Window.alert("Exception (server-side)");
                } else {
                    GWT.log("Exception :(", e);
                    Window.alert("Exception " + e.getMessage());
                }
            }
        });
    }

}
