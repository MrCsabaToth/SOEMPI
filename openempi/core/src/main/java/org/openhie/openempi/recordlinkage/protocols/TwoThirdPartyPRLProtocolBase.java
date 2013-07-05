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
package org.openhie.openempi.recordlinkage.protocols;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.recordlinkage.configuration.ComponentSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.util.DiffieHellmanKeyExchange;

public abstract class TwoThirdPartyPRLProtocolBase extends MultiPartyPRLProtocolBase
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public TwoThirdPartyPRLProtocolBase() {
	}
	
	protected boolean twoOrThreeThirdParty() {
		return true;
	}

	protected String getThirdPartyAddress() {
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		ComponentSettings cps = privacySettings.getComponentSettings();
		return cps.getDataIntegratorSettings().getServerAddress();
	}

	protected String getThirdPartyCredential(String dataIntegratorCredential, String parameterManagerCredential) {
		return dataIntegratorCredential;
	}

	protected boolean fillColumnInformationForSend(List<ColumnInformation> matchColumnInformation,
			List<ColumnInformation> columnInformation, List<String> columnNames)
	{
		for (ColumnInformation ci : matchColumnInformation) {
			columnNames.add(ci.getFieldName());
			ColumnInformation ciClone = ci.getClone();
			columnInformation.add(ciClone);
		}
		return false;	// Return value doesn't have meaning in case of 2 3rd party protocol
	}

	protected void sendFirstPhaseData(Dataset dataset, long totalRecords, List<String> columnNames,
			List<ColumnInformation> matchColumnInformation, List<ColumnInformation> noMatchColumnInformation,
			boolean isThereClearField, String defaultHmacFunctionName, String thirdPartyAddress,
			Map<Long,Long> personPseudoIdsReverseLookup, RemotePersonService remotePersonService, String remoteTableName) throws NamingException, ApplicationException {
		long firstResult = 0L;
		boolean morePatients = true;
		List<Person> persons = null;
		PersonQueryService personQueryService = Context.getPersonQueryService();
		List<String> allColumnNames = fillNoMatchColumnNames(noMatchColumnInformation, columnNames);
		do {
			persons = personQueryService.getPersonsPaged(dataset.getTableName(), allColumnNames, firstResult, Constants.PAGE_SIZE);
			morePatients = (persons != null && persons.size() > 0);
			if (morePatients) {
				// Don't use pseudoIds for local experiments
				if (isLocalAddress(thirdPartyAddress)) {
					long i = 0L;
					for (Person person : persons) {
						if (personPseudoIdsReverseLookup != null)
							personPseudoIdsReverseLookup.put(firstResult + i, person.getPersonId());
						person.setPersonId(firstResult + i);
						i++;
					}
				}
				remotePersonService.addPersons(remoteTableName, persons, false, false);
			}
			firstResult += persons.size();
		} while (morePatients);
	}

	protected void performSecondPhase(Dataset dataset, String matchName,
			String blockingServiceName, String matchingServiceName, String thirdPartyAddress,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword,
			int personMatchRequestId, DiffieHellmanKeyExchange dhke,
			Map<Long,Long> personPseudoIdsReverseLookup) throws NamingException, ApplicationException
	{
		// In case of 2 3rd party DI does the linkage, we don't have to wait for returned data or advice
	}

	public void testPMLinkRecords(int leftDatasetId, int rightDatasetId, String blockingServiceName,
			String matchingServiceName) throws ApplicationException {
		throw new ApplicationException("Nothing to test: there's no Parameter Manager link phase during two 3rd party protocols");
	}

	public void testBFReencoding(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException {
		throw new ApplicationException("Nothing to test: there's no BF reencoding during two 3rd party protocols");
	}

	public void testHMACEncoding(int dataSetId, String tableName) throws ApplicationException
	{
		throw new ApplicationException("Nothing to test: there's no BF reencoding during simple send");
	}

	protected BloomFilterParameterAdvice personMatchRequestAcquired(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType, List<MatchPairStat> matchPairStats) throws ApplicationException
	{
		if (componentType == ComponentType.DATA_INTEGRATOR_MODE) {
			return linkPRLRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType);
		}	// No Parameter Manager mode
		log.error("Unhandled component type in personMatchRequestAcquired: " + componentType);
		return null;
	}

}
