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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that can hold only one widget in its center point.
 *
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class CenterPanel extends Composite {

    private DockPanel container;

    private Widget content;

    /**
     * Constructs a new CenterPanel with no content.
     */
    public CenterPanel() {
        this(null);
    }

    /**
     * Constructs a new CenterPanel with a given content.
     *
     * @param content The content of this panel.
     */
    public CenterPanel(Widget content) {
        container = new DockPanel();
        if (content != null) {
            setContent(content);
        }
        initWidget(container);
        setStyleName("CenterPanel");
    }

    /**
     * Sets the content of this panel. The content will be placed in the center of this panel.
     *
     * @param content The content of this panel.
     */
    public void setContent(Widget content) {
        if (this.content != null) {
            container.remove(this.content);
        }
        this.content = content;
        container.add(content, DockPanel.CENTER);
        container.setCellHorizontalAlignment(content, DockPanel.ALIGN_CENTER);
        container.setCellVerticalAlignment(content, DockPanel.ALIGN_MIDDLE);
    }

    /**
     * Returns the content of this panel.
     *
     * @return The content of this panel.
     */
    public Widget getContent() {
        return content;
    }

}
