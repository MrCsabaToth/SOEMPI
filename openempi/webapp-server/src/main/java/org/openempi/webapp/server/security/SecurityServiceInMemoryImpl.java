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
package org.openempi.webapp.server.security;

import org.openempi.webapp.client.SecurityService;
import org.openempi.webapp.client.domain.Authentication;
import org.openempi.webapp.client.domain.AuthenticationException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Default in-memory implementation of the {@link SecurityService}. Borrowed
 * Inspired by Beginning Google Web Toolkit Book
 * 
 * @author Uri Boness
 * @author Sam Brodkin
 */
public class SecurityServiceInMemoryImpl extends RemoteServiceServlet implements SecurityService {

	private static Authentication authentication = Authentication.ANONYMOUS;

	/**
	 * {@inheritDoc}
	 */
	public void login(String userName) throws AuthenticationException {
		if (userName.equals("badguy")) {
			throw new AuthenticationException();
		}
		authentication = new Authentication(userName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logout() {
		authentication = Authentication.ANONYMOUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isLoggedIn() {
		return authentication != Authentication.ANONYMOUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Authentication getCurrentAuthentication() {
		return authentication;
	}

}
