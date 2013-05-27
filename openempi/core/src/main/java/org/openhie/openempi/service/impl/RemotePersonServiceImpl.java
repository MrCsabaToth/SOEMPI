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

//import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.naming.NamingException;

//import org.jboss.aspects.asynch.AsynchProvider;
//import org.jboss.aspects.asynch.Future;
//import org.jboss.ejb3.asynchronous.Asynch;
import org.openhie.openempi.ApplicationException;
//import org.openhie.openempi.Constants;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.person.RecordLinkageProtocol;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.service.RemotePersonServiceLocator;

public class RemotePersonServiceImpl extends BaseServiceImpl implements RemotePersonService
{
	static String sessionKey;
	static String ipAddress;
	static boolean isAuthenticated = false;

	public void authenticate(String ipAddress, String username, String password) throws NamingException {
		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();

		SecurityService securityService;
		securityService = remotePersonServiceLocator.getSecurityService(ipAddress);
		sessionKey = securityService.authenticate(username, password);
		isAuthenticated = true;
	}

	public void authenticate(String ipAddress, String username, String password,
			String keyServerUserName, String keyServerPassword) throws NamingException {
		authenticate(ipAddress, username, password);

		KeyServerService ks = Context.getKeyServerService();
		ks.authenticate(keyServerUserName, keyServerPassword);
	}

	public void close() throws NamingException, ApplicationException {
		sessionKey = null;
		ipAddress = null;
		isAuthenticated = false;

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		remotePersonServiceLocator.close();
	}

	public Dataset createDatasetTable(String tableName, List<ColumnInformation> columnInformation,
			long totalRecords, boolean withIndexesAndConstraints) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to import person to remote host.");
			throw new ApplicationException("Session is not authenticated while trying to import person to remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		PersonManagerService personManagerService = remotePersonServiceLocator.getPersonManagerService();
		return personManagerService.createDatasetTable(sessionKey, tableName, columnInformation, totalRecords, withIndexesAndConstraints);
	}

	public void addPerson(String tableName, Person person, boolean applyFieldTransformations, boolean existenceCheck) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to import person to remote host.");
			throw new ApplicationException("Session is not authenticated while trying to import person to remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		PersonManagerService personManagerService = remotePersonServiceLocator.getPersonManagerService();
		/*Person personResult =*/ personManagerService.addPerson(sessionKey, tableName, person, applyFieldTransformations, existenceCheck);
	}

	public void addPersons(String tableName, List<Person> persons, boolean applyFieldTransformations, boolean existenceCheck) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to import person to remote host.");
			throw new ApplicationException("Session is not authenticated while trying to import person to remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		PersonManagerService personManagerService = remotePersonServiceLocator.getPersonManagerService();
		/*Person personResult =*/ personManagerService.addPersons(sessionKey, tableName, persons, applyFieldTransformations, existenceCheck);
	}

	public void addIndexesAndConstraintsToDatasetTable(String tableName) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to import person to remote host.");
			throw new ApplicationException("Session is not authenticated while trying to import person to remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		PersonManagerService personManagerService = remotePersonServiceLocator.getPersonManagerService();
		personManagerService.addIndexesAndConstraints(sessionKey, tableName);
	}

	public List<Person> getPersonsPaged(String tableName, long firstResult, int maxResults) throws NamingException,
			ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to load person paged from remote host.");
			throw new ApplicationException("Session is not authenticated while trying to load person paged from remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		PersonQueryService personQueryService = remotePersonServiceLocator.getPersonQueryService();
		List<Person> persons = personQueryService.getPersonsPaged(sessionKey, tableName, firstResult, maxResults);

		return persons;
	}

	public void createMatchPairStatHalfTable(String protocolTypeName, String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to load person paged from remote host.");
			throw new ApplicationException("Session is not authenticated while trying to load person paged from remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		RecordLinkageProtocol recordLinkageProtocol = remotePersonServiceLocator.getRecordLinkageProtocol();
		recordLinkageProtocol.createMatchPairStatHalfTable(sessionKey, protocolTypeName, statTableName,
				datasetTableName, withIndexesAndConstraints);
	}

	public void addMatchPairStatHalves(String protocolTypeName, String statTableName, List<MatchPairStatHalf> matchPairStatHalves) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to load person paged from remote host.");
			throw new ApplicationException("Session is not authenticated while trying to load person paged from remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		RecordLinkageProtocol recordLinkageProtocol = remotePersonServiceLocator.getRecordLinkageProtocol();
		recordLinkageProtocol.addMatchPairStatHalves(sessionKey, protocolTypeName, statTableName, matchPairStatHalves);
	}

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String protocolTypeName, String statTableName, String datasetTableName) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to import person to remote host.");
			throw new ApplicationException("Session is not authenticated while trying to import person to remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		RecordLinkageProtocol recordLinkageProtocol = remotePersonServiceLocator.getRecordLinkageProtocol();
		recordLinkageProtocol.addIndexesAndConstraints(sessionKey, protocolTypeName, statTableName, datasetTableName);
	}

	public int addPersonMatchRequest(String protocolTypeName, String tableName, String matchName,
			Integer nonce, String matchPairStatHalfTableName) throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to load person paged from remote host.");
			throw new ApplicationException("Session is not authenticated while trying to load person paged from remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		RecordLinkageProtocol recordLinkageProtocol = remotePersonServiceLocator.getRecordLinkageProtocol();
		return recordLinkageProtocol.addPersonMatchRequest(sessionKey, protocolTypeName, tableName, matchName,
				nonce, matchPairStatHalfTableName);
	}

	public BloomFilterParameterAdvice acquireMatchRequests(String protocolTypeName, int personMatchRequestId, ComponentType componentType)
			throws NamingException, ApplicationException
	{
		if (!isAuthenticated) {
			log.warn("Session is not authenticated while trying to load person paged from remote host.");
			throw new ApplicationException("Session is not authenticated while trying to load person paged from remote host.");
		}

		RemotePersonServiceLocator remotePersonServiceLocator = Context.getRemotePersonServiceLocator();
		RecordLinkageProtocol recordLinkageProtocol = remotePersonServiceLocator.getRecordLinkageProtocol();

/*		log.debug("Getting an asynchronous instance of the person manager service");
		RecordLinkageProtocol asyncRecordLinkageProtocol = (RecordLinkageProtocol)Asynch.getAsynchronousProxy(recordLinkageProtocol);
		log.debug("Obtained an asynchronous instance of the record linkage protocol service");
		log.debug("Invoking asynchronous call acquireMatchRequests to the async record linkage protocol service");
		BloomFilterParameterAdvice ret = recordLinkageProtocol.acquireMatchRequests(sessionKey, protocolTypeName, personMatchRequestId, componentType);
		log.debug("Direct return of async invocation is: " + ret);
		AsynchProvider asynchProvider = (AsynchProvider)asyncRecordLinkageProtocol;
		Future future = asynchProvider.getFuture();
		log.debug("Got asynchronous result proxy from the record linkage protocol service, waiting for completion...");
		while (!future.isDone()) {
			try {
				Thread.sleep(Constants.STANDARD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			ret = (BloomFilterParameterAdvice)future.get();
		} catch (InterruptedException e) {
			log.debug("Error while waiting for async result.");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.debug("Error while waiting for async result.");
			e.printStackTrace();
		}
		return ret;*/

		return recordLinkageProtocol.acquireMatchRequests(sessionKey, protocolTypeName, personMatchRequestId, componentType);
	}

}
