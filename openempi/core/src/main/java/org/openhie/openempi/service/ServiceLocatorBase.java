/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.service;

import javax.naming.Context;
import javax.naming.NamingException;

import org.openhie.openempi.ejb.security.SecurityService;

public interface ServiceLocatorBase
{
	public SecurityService getSecurityService() throws NamingException;

	public void close() throws NamingException;

	public abstract String getProviderURL();

	public Context getInitialContext() throws NamingException;
}
