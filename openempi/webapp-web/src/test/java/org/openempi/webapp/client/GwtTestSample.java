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

import java.util.List;

import org.openempi.webapp.client.model.PersonWeb;

import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GwtTestSample extends GWTTestCase {

	public String getModuleName() {
		return "org.openempi.webapp.Application";
	}
//
//	public void testSomething() {
//		PersonDataServiceAsync personDataService = (PersonDataServiceAsync) GWT
//				.create(PersonDataService.class);
//		ServiceDefTarget endpoint = (ServiceDefTarget) personDataService;
//		String moduleRelativeURL = Constants.PERSON_DATA_SERVICE;
//		endpoint.setServiceEntryPoint(moduleRelativeURL);
//		
//		PersonIdentifier pi = new PersonIdentifier();
//		pi.setIdentifier("c%");
//		personDataService.getPersonsByIdentifier(pi,
//				new AsyncCallback<List<Person>>() {
//					public void onFailure(Throwable e) {
//						assertTrue("Got an error: " + e, false);
//						finishTest();
//					}
//
//					public void onSuccess(List<Person> result) {
//						XTemplate tpl = XTemplate.create(getTemplate());
//						for (Person person : result) {
//							System.out.println(tpl.applyTemplate(Util.getJsObject(person, 4)));
//						}
//						finishTest();
//					}
//				});
//		// Set a delay period significantly longer than the
//		// event is expected to take.
//		delayTestFinish(500);
//	}
//	
//
//	private final String getTemplate() {
////		return "<p><b>Company:</b> {givenName}</p><br><p><b>Summary:</b> {familyName}</p>";
//		return "<p>Name: {givenName}</p>, <p>Company: {familyName}</p>, <p>Location: {postalCode}</p>, <p>Kids:</p>, " +
//				"<tpl for=\"personIdentifiers\"> " +
//				"<p>{#}. {identifier} {this.identifierDomain}, </p> " +
//				"{ isWorking: function() { return('yes');} }</tpl>";
//	}
}