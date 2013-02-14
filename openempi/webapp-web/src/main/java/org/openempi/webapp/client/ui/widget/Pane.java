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

import org.openempi.webapp.client.ClientState;
import org.openempi.webapp.client.ServiceRegistry;

import com.google.gwt.user.client.ui.Composite;

/**
 * base class for all Panes in our app
 * 
 * Borrowed from Beginning Google Web Toolkit book
 */
public abstract class Pane extends Composite {

    private final ServiceRegistry serviceRegistry;
    private final ClientState clientState;

    /**
     * @param serviceRegistry The service registry each pane has access to.
     */
    protected Pane(ClientState clientState, ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        this.clientState = clientState;
    }
    
    /**
     * 
     * @return The service registry associated with this pane.
     */
    protected ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            throw new RuntimeException("serviceRegistry state in uninitialized");
        }
        return serviceRegistry;
    }
    
    public ClientState getClientState() {
        if (clientState == null) {
            throw new RuntimeException("client state in uninitialized");
        }
        return clientState;
    }
}
