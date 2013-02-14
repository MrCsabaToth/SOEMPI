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
package org.openhie.openempi.service.impl;

import javax.naming.NamingException;

import org.openhie.openempi.Constants;
import org.openhie.openempi.service.RemotePersonServiceLocator;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.person.RecordLinkageProtocol;

public class RemotePersonServiceLocatorImpl extends ServiceLocatorBaseImpl implements RemotePersonServiceLocator
{
	private String ipAddress;

	public SecurityService getSecurityService(String ipAddress) throws NamingException {
		this.ipAddress = ipAddress;
		return getSecurityService();
	}
	
	public PersonManagerService getPersonManagerService() throws NamingException {
		log.debug("Looking up an instance of the person manager service using: PersonManagerService/remote");
		PersonManagerService personManagerService = (PersonManagerService) getInitialContext().lookup("openempi/PersonManagerService");
		log.debug("Obtained an instance of the person manager service: " + personManagerService);
		return personManagerService;
	}
	
	public PersonQueryService getPersonQueryService() throws NamingException {
		log.debug("Looking up an instance of the person query service using: PersonQueryService/remote");
		PersonQueryService personQueryService = (PersonQueryService) getInitialContext().lookup("openempi/PersonQueryService");
		log.debug("Obtained an instance of the person query service: " + personQueryService);
		return personQueryService;
	}

	public RecordLinkageProtocol getRecordLinkageProtocol() throws NamingException {
		log.debug("Looking up an instance of the record linkage protocol using: RecordLinkageProtocol/remote");
		RecordLinkageProtocol recordLinkageProtocol = (RecordLinkageProtocol) getInitialContext().lookup("openempi/RecordLinkageProtocol");
		log.debug("Obtained an instance of the record linkage protocol: " + recordLinkageProtocol);
		return recordLinkageProtocol;
	}

	public void close() throws NamingException {
		ipAddress = null;
		super.close();
	}

	public String getProviderURL() {
		return ipAddress + ":" + Constants.RMI_DEFAULT_PORT_NUMBER;
	}

}
