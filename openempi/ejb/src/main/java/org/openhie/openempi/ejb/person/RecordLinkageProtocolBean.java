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
package org.openhie.openempi.ejb.person;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;
import org.openhie.openempi.ejb.person.RecordLinkageProtocol;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.MatchPairStatHalf;

@Stateless(name="RecordLinkageProtocol")
@Interceptors ({SpringInjectionInterceptor.class})
public class RecordLinkageProtocolBean extends BaseSpringInjectableBean implements RecordLinkageProtocol {
	private static final long serialVersionUID = -6381549112790070749L;

	public void createMatchPairStatHalfTable(String sessionKey, String protocolTypeName, String statTableName,
			String datasetTableName, boolean withIndexesAndConstraints) throws ApplicationException
	{
		log.trace("In createMatchPairStatHalfTable method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		recordLinkageProtocolType.getRecordLinkageProtocol().createMatchPairStatHalfTable(statTableName,
				datasetTableName, withIndexesAndConstraints);
	}
	
	public void addMatchPairStatHalf(String sessionKey, String protocolTypeName, String statTableName,
			MatchPairStatHalf matchPairStatHalf) throws ApplicationException {
		log.trace("In addMatchPairStatHalf method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		recordLinkageProtocolType.getRecordLinkageProtocol().addMatchPairStatHalf(statTableName, matchPairStatHalf);
	}
	
	public void addMatchPairStatHalves(String sessionKey, String protocolTypeName, String statTableName,
			List<MatchPairStatHalf> matchPairStatHalves) throws ApplicationException {
		log.trace("In addMatchPairStatHalves method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		recordLinkageProtocolType.getRecordLinkageProtocol().addMatchPairStatHalves(statTableName,
				matchPairStatHalves);
	}
	
	public void addIndexesAndConstraints(String sessionKey, String protocolTypeName, String statTableName,
			String datasetTableName) throws ApplicationException
	{
		log.trace("In addIndexesAndConstraints method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		recordLinkageProtocolType.getRecordLinkageProtocol().addIndexesAndConstraintsToMatchPairStatHalfTable(
				statTableName, datasetTableName);
	}
	
	public int addPersonMatchRequest(String sessionKey, String protocolTypeName, String tableName,
			String matchName, String blockingServiceName, String matchingServiceName,
			Integer nonce, String matchPairStatHalfTableName) throws ApplicationException {
		log.trace("In addPersonMatchRequest method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		return recordLinkageProtocolType.getRecordLinkageProtocol().handlePersonMatchRequest(tableName, matchName,
				blockingServiceName, matchingServiceName, nonce, matchPairStatHalfTableName);
	}
	
	public BloomFilterParameterAdvice acquireMatchRequests(String sessionKey, String protocolTypeName,
			int personMatchRequestId, ComponentType componentType) throws ApplicationException {
		log.trace("In acquireMatchRequests method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector recordLinkageProtocolSelector =
				Context.getRecordLinkageProtocolSelector();
		org.openhie.openempi.recordlinkage.RecordLinkageProtocolType recordLinkageProtocolType =
				recordLinkageProtocolSelector.getRecordLinkageProtocolType(protocolTypeName);
		return recordLinkageProtocolType.getRecordLinkageProtocol().acquireMatchRequests(personMatchRequestId, componentType);
	}
	
}