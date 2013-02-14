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
package org.openhie.openempi.blocking;

import org.openhie.openempi.configuration.xml.BlockingConfigurationType;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingField;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingFields;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRound;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds;
import org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BlockingServiceSaveConfigurationTest extends BaseServiceTestCase
{
	public void testUpdateBlockingConfiguration() {
		try {
			BlockingConfigurationType blockingNode = Context.getConfiguration().getBlockingConfiguration();
			BasicBlockingType basicBlocking = (BasicBlockingType) blockingNode;
			log.debug("Basic blocking info is: " + basicBlocking);
			
			BasicBlockingType newBasicBlocking = BasicBlockingType.Factory.newInstance();
			BlockingRounds roundsNode = newBasicBlocking.addNewBlockingRounds();
			BlockingRound roundNode = roundsNode.addNewBlockingRound();
			BlockingFields blockingFields = roundNode.addNewBlockingFields();
			BlockingField field = blockingFields.addNewBlockingField();
			field.setLeftFieldName("givenName");
			field.setRightFieldName("givenName");
			field = blockingFields.addNewBlockingField();
			field.setLeftFieldName("familyName");
			field.setRightFieldName("familyName");
			
			log.debug("Modified blocking info is: " + newBasicBlocking);
			
			blockingNode = Context.getConfiguration().saveBlockingConfiguration(newBasicBlocking);
			log.debug("Basic blocking info is: " + basicBlocking);			
			
		} catch (Exception e) {
			log.error("Failed while saving configuration: " + e, e);
		}
	}
}
