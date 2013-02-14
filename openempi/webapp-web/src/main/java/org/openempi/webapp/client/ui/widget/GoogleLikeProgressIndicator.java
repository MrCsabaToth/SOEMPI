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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A {@link ProgressIndicator} implementation which mimic the progress indicator used by Google in their product. This
 * indicator basically shows a label on to top-center of the screen.
 *
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class GoogleLikeProgressIndicator extends PopupPanel implements ProgressIndicator {

    private final Label messageLabel;

    /**
     * Constructs a new GoogleLikeProgressIndicator with "Loading" as a default initial message.
     */
    public GoogleLikeProgressIndicator() {
        this(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new GoogleLikeProgressIndicator with a given initial message.
     *
     * @param message The message the indicator should initialy show.
     */
    public GoogleLikeProgressIndicator(String message) {
        super(false, true);
		messageLabel = new Label(message);
		messageLabel.setStyleName("Message");
		setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int x = Window.getClientWidth()/2 - offsetWidth/2;
                setPopupPosition(x, 0);
            }
        });
        setWidget(messageLabel);
        setStyleName("GoogleLikeProgressIndicator");
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
