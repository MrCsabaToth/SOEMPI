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
package org.openhie.openempi.configuration;

import org.openhie.openempi.blocking.basicblocking.BasicBlockingConstants;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.blocking.basicblocking.BlockingSettings;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.exactmatching.ExactMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ConfigurationTest extends BaseServiceTestCase
{
	@SuppressWarnings("unchecked")
	public void testConfiguration() {
		Configuration configuration = Context.getConfiguration();

		BlockingSettings blockingSettings = (BlockingSettings)
			configuration.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY);
		for (BlockingRound blockingRound : blockingSettings.getBlockingRounds()) {
			log.debug("Blocking round: " + blockingRound);
		}
		
		java.util.List<MatchField> fields = (java.util.List<MatchField>)
			configuration.lookupConfigurationEntry(ExactMatchingConstants.EXACT_MATCHING_FIELDS_REGISTRY_KEY);
		if (fields != null) {
			for (MatchField field : fields) {
				log.debug("Match field is: " + field);
			}
		}
	}
}
