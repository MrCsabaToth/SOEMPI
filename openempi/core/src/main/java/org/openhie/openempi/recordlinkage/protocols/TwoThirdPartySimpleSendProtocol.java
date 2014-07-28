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

import java.util.ArrayList;
import java.util.List;

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
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatchRequest;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocol;
import org.openhie.openempi.recordlinkage.configuration.DataIntegratorSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public class TwoThirdPartySimpleSendProtocol extends BaseServiceImpl implements RecordLinkageProtocol
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private String name;
	
	public TwoThirdPartySimpleSendProtocol() {
	}
	
	public TwoThirdPartySimpleSendProtocol(String name) {
		this.name = name;
	}

	public void createMatchPairStatHalfTable(String statTableName, String datasetTableName,
			boolean withIndexesAndConstraints)
	{
		log.error("createMatchPairStatHalfTable doesn't make sense during simple send");
	}
	
	/**
	 * Add new match pair stat halves to the system.
	 */
	public void addMatchPairStatHalves(String statTableName, List<MatchPairStatHalf> matchPairStatHalves) {
		log.error("addMatchPairStatHalves doesn't make sense during simple send");
	}

	public void addIndexesAndConstraintsToMatchPairStatHalfTable(String statTableName, long seqStart, String datasetTableName)
	{
		log.error("addIndexesAndConstraintsToMatchPairStatHalfTable doesn't make sense during simple send");
	}
	
	public PersonMatchRequest sendPersonMatchRequest(Dataset dataset, String remoteTableName, String matchName,
			String keyServerUserName, String keyServerPassword,
			String dataIntegratorUserName, String dataIntegratorPassword,
			String parameterManagerUserName, String parameterManagerPassword)
	{
		long startTime1 = System.nanoTime();
		log.warn("Send preparation Start: " + startTime1);
		PrivacySettings privacySettings =
				(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
		DataIntegratorSettings dataIntegratorSettings =
				privacySettings.getComponentSettings().getDataIntegratorSettings();
		String serverAddress4DI = dataIntegratorSettings.getServerAddress();

		long startTime2 = 0;
		try {
			RemotePersonService remotePersonService = Context.getRemotePersonService();
	
			remotePersonService.close();
			remotePersonService.authenticate(serverAddress4DI, dataIntegratorUserName, dataIntegratorPassword,
					keyServerUserName, keyServerPassword);

			long endTime1 = System.nanoTime();
			log.warn("Send preparation End: " + endTime1 + ", elapsed: " + (endTime1 - startTime1));
			startTime2 = System.nanoTime();
			log.warn("Send Start: " + startTime2);
			// Send all columns here, it's not PRL
			// Maybe we could do some intelligent cherry picking analyzing the match configuration?
			List<ColumnInformation> columnInformation = dataset.getColumnInformation();
			remotePersonService.createDatasetTable(remoteTableName, columnInformation, dataset.getTotalRecords(),
					false);

			List<String> columnNames = new ArrayList<String>();
			for (ColumnInformation ci : columnInformation) {
				columnNames.add(ci.getFieldName());
			}

			long firstResult = 0L;
			boolean morePatients = true;
			List<Person> persons = null;
			PersonQueryService personQueryService = Context.getPersonQueryService();
			do {
				persons = personQueryService.getPersonsPaged(dataset.getTableName(), columnNames, firstResult, Constants.PAGE_SIZE);
				morePatients = (persons != null && persons.size() > 0);
				if (morePatients) {
					remotePersonService.addPersons(remoteTableName, persons, false, false);
				}
				firstResult += persons.size();
			} while (morePatients);
			remotePersonService.addIndexesAndConstraintsToDatasetTable(remoteTableName, firstResult + 1);
		} catch (NamingException e) {
			log.error("Couldn't connect to Data Integrator (" + serverAddress4DI + ") to send the dataset");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error occured during creation, generation or loading of BF or CBF data");
			e.printStackTrace();
		}
		long endTime2 = System.nanoTime();
		log.warn("Send End: " + endTime2 + ", elapsed: " + (endTime2 - startTime2));

		return null;
	}

	public Integer handlePersonMatchRequest(String tableName, String matchName,
			byte[] dhPublicKey, String matchPairStatHalfTableName) throws ApplicationException
	{
		throw new ApplicationException("handleBloomFilterParameterAdvice doesn't make sense during simple send");
	}

	public BloomFilterParameterAdvice acquireMatchRequests(int personMatchRequestId, ComponentType componentType) {
		log.error("acquireMatchRequests doesn't make sense during simple send");
		return null;
	}

	public void testPMLinkRecords(int leftDatasetId, int rightDatasetId, String blockingServiceName,
			String matchingServiceName) throws ApplicationException
	{
		throw new ApplicationException("Nothing to test: there's no Parameter Manager link phase during simple send");
	}

	public void testPRLLinkRecords(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException
	{
		throw new ApplicationException("Nothing to test: there's no PRL link records phase during simple send");
	}

	public void testBFReencoding(int leftPersonMatchRequestId, int rightPersonMatchRequestId) throws ApplicationException
	{
		throw new ApplicationException("Nothing to test: there's no BF reencoding during simple send");
	}

	public void testHMACEncoding(int dataSetId, String tableName) throws ApplicationException
	{
		throw new ApplicationException("Nothing to test: there's no BF reencoding during simple send");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
