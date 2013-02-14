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

import org.openempi.webapp.client.Constants;

import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.user.client.Window;

public class Viewport extends com.extjs.gxt.ui.client.widget.Viewport
{
	// This is plain ugly but the Viewport uses as the size of the application the size of
	// the browser window and if you are using part of that window for the header, and
	// loading the application on a div tag then the application size is larger than the
	// browser window without a scroll bar.
	//
	public void onAttach() {
		super.onAttach();
		GXT.hideLoadingPanel(getLoadingPanelId());
		setEnableScroll(getEnableScroll());
		setSize(Window.getClientWidth(), Window.getClientHeight() - Constants.HEADER_OFFSET);
	}

	@Override
	protected void onWindowResize(final int width, final int height) {
		setSize(Window.getClientWidth(), Window.getClientHeight() - Constants.HEADER_OFFSET);
	}
}
