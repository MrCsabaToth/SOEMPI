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
import org.openempi.webapp.client.domain.Authentication;
import org.openempi.webapp.client.ui.widget.Pane;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Pane} that shows the header of the main page. This header consists
 * of a title and a logout button/link.
 * 
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class HeaderPane extends Pane {

	private PopupPanel confirmPopup;
	private DockPanel main;

	/**
	 * Constructs a new HeaderPane that displays the given text.
	 * 
	 * @param titleText
	 *            The text this header should show as a title.
	 */
	public HeaderPane(String titleText, ClientState clientState,
			ServiceRegistry serviceRegistry) {
		super(clientState, serviceRegistry);
		main = new DockPanel();
		main.add(new Label("Brodkin's GWT Sample App"), DockPanel.CENTER);
		reset();
		initWidget(main);
		setStyleName("HeaderPane");
	}

	public void reset() {
		
		getServiceRegistry().getSecurityService().isLoggedIn(
				new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					public void onSuccess(Boolean result) {
						Boolean isLoggedIn = result;
						HorizontalPanel panel = buildHeaderPanel(isLoggedIn);
						main.remove(0);
						main.add(panel, DockPanel.EAST);
					}
				});
	}

	/**
	 * Build this conditionally based on isLoggedIn
	 * @param isLoggedIn
	 * @return
	 */
	private HorizontalPanel buildHeaderPanel(boolean isLoggedIn) {
		HorizontalPanel panel = new HorizontalPanel();
		if (isLoggedIn) {
			panel.setSpacing(10);
			panel.add(new Label("Welcome " + getClientState().getUserName()));

			PushButton logoutButton = new PushButton("Logout");
			logoutButton.setStyleName("LogoutButton");
			logoutButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					logout();
				}
			});
			panel.add(logoutButton);
		} else {
			PushButton loginButon = new PushButton("Login");
			loginButon.setStyleName("LoginButton");
			loginButon.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					showNewUserNameRequestPopupPanel();
				}
			});
			panel.add(loginButon);
		}
		return panel;
	}

	private void logout() {
//		getServiceRegistry().getSecurityService().logout(
//				new AsyncCallback<String>() {
//					public void onFailure(Throwable caught) {
//						throw new RuntimeException(caught);
//					}
//
//					public void onSuccess(String caught) {
//						reset();
//					}
//				});
	}

	private void login(String userName) {

//		getServiceRegistry().getSecurityService().login(userName,
//				new AsyncCallback<String>() {
//					public void onFailure(Throwable caught) {
//						throw new RuntimeException(caught);
//					}
//
//					//after we log them in, make another call to get their name
//					//and set it in the ClientState.  This is inefficient, but it 
//					//demonstrates making two asynchronous calls serially
//					public void onSuccess(String caught) {
//						getServiceRegistry().getSecurityService().getCurrentAuthentication(
//						(new AsyncCallback<Authentication>() {
//							public void onFailure(Throwable caught) {
//								throw new RuntimeException(caught);
//							}
//							public void onSuccess(Authentication authentication) {
//								getClientState().setUserName(authentication.getUsername());
//								reset();
//							}
//						}));
//					}
//				});

	}

	private void showNewUserNameRequestPopupPanel() {
		confirmPopup = new PopupPanel(false, true);
		VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("What is your login Name?"));
		final TextBox textBox = new TextBox();
		panel.add(textBox);
		Button ok = new Button("OK");
		ok.addClickListener(new ClickListener() {
			public void onClick(Widget w) {
				confirmPopup.hide();
				login(textBox.getText());
			}
		});

		panel.add(ok);
        confirmPopup.add(panel);
        confirmPopup.center();
        textBox.setFocus(true);
        confirmPopup.show();
	}
}
