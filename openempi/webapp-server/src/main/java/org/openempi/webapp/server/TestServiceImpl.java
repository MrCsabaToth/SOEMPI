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
package org.openempi.webapp.server;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.TestService;
import org.openempi.webapp.client.domain.Candidate;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TestServiceImpl extends RemoteServiceServlet implements TestService
{
	private static final long serialVersionUID = -3215301228098301453L;

	public static final Logger LOGGER = Logger.getLogger(TestServiceImpl.class);
	public String myMethod(String s) {
		// Do something interesting with 's' here on the server.
		return s;
	}
	
	public ArrayList<Candidate> getCandidates() {
		Candidate candidate1 = new Candidate("Obama", 45);
		Candidate candidate2 = new Candidate("Hillary", 60);
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		candidates.add(candidate1);
		candidates.add(candidate2);
		LOGGER.info("returning candidates list");
		return candidates;
	}
	
	public String longRunningMethod(int runningTime) {
		
		try {
			Thread.sleep(runningTime);
		} catch (InterruptedException e) {
			// ignore
		}
		return "operation took " + runningTime + " millis";
	}

}