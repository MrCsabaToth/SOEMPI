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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * The status bar
 *
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class StatusBarPane extends Composite {

	private final Label messageLabel;

	public StatusBarPane() {
		messageLabel = new Label();
        messageLabel.setStyleName("MessageLabel");
		initWidget(messageLabel);
        setStyleName("StatusBarPane");
	}
	
	/**
	 * Sets the message to be displayed in this status bar.
     *
     * @param message The message to be displayed in this status bar.
	 */
	public void setMessage(String message) {
		messageLabel.setText(message);
	}

}
