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

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.model.PersonMatchRequest;

public class TwoThirdPartyCBFProtocol extends TwoThirdPartyPRLProtocolBase
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public TwoThirdPartyCBFProtocol() {
	}
	
	protected int getNonce() {
		SecureRandom rnd = new SecureRandom();
		return rnd.nextInt();
	}

	protected String sendNewDataset(String newBFTableName, Dataset newBFDataset, String remoteTableName, List<ColumnMatchInformation> columnMatchInformation,
			String keyServerUserName, String keyServerPassword, String dataIntegratorUserName, String dataIntegratorPassword,
			List<MatchPairStatHalf> matchPairStatHalves, Map<Long,Long> personPseudoIdsReverseLookup, int sharedSecret, boolean leftOrRightSide,
			List<ColumnInformation> bfColumnInformation) throws NamingException, ApplicationException
	{
		return sendCBFDataset(newBFTableName, newBFDataset, remoteTableName, columnMatchInformation,
				keyServerUserName, keyServerPassword, dataIntegratorUserName, dataIntegratorPassword,
				matchPairStatHalves, personPseudoIdsReverseLookup, sharedSecret, leftOrRightSide,
				bfColumnInformation);
	}

	protected String getMatchPairStatHalfTableName(String remoteTableName)
	{
		if (!getIsLshBlocking())
			return null;
		return remoteTableName + "_" + /*UniversalDaoHibernate.MATCHPAIRSTAT_TABLE_NAME_EXTRA_PREFIX + "_" +*/ getNowString();	// including timestamp to avoid collision
	}

	protected BloomFilterParameterAdvice linkPRLRecords(PersonMatchRequest leftPersonMatchRequest,
			PersonMatchRequest rightPersonMatchRequest, ComponentType componentType) throws ApplicationException {
		return linkCBFRecords(leftPersonMatchRequest, rightPersonMatchRequest, componentType);
	}

	protected String getMatchingServiceTypeName(ComponentType componentType) {
		return Constants.CBF_SCORING_SERVICE_NAME;
	}

	protected String getBlockingServiceTypeName(ComponentType componentType, List<MatchPairStat> matchPairStats) {
		return Constants.PPB_WITH_CRYPTO_RANDOM_BITS_SERVICE_NAME;
	}

}
