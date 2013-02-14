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
package org.openhie.openempi.ejb.security;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;

@Stateless(name="SecurityService")
@Interceptors ({SpringInjectionInterceptor.class})
public class SecurityServiceBean extends BaseSpringInjectableBean implements SecurityService
{
	private static final long serialVersionUID = -407957125655403090L;

	public String authenticate(String username, String password)
			throws AuthenticationException {
		log.debug("Received an authentication request from user " + username);
		String sessionKey = Context.authenticate(username, password);
		return sessionKey;
	}

}
