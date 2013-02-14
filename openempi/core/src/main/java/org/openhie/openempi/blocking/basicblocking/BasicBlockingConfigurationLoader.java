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
package org.openhie.openempi.blocking.basicblocking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingField;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingFields;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds;
import org.openhie.openempi.context.Context;

/**
 * @author Odysseas Pentakalos 
 * @version $Revision: $ $Date:  $
 */
public class BasicBlockingConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(BasicBlockingConfigurationLoader.class);
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the basic blocking service
		//
		if (!(configurationFragment instanceof BasicBlockingType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the blocking service to use when needed.
		//
		BlockingSettings blockingSettings = new BlockingSettings();
		ArrayList<BlockingRound> blockingRounds = new ArrayList<BlockingRound>();
		
		BasicBlockingType blockingConfig = (BasicBlockingType) configurationFragment;
		log.debug("Received xml fragment to parse: " + blockingConfig);
		if (blockingConfig == null || blockingConfig.getBlockingRounds().sizeOfBlockingRoundArray() == 0) {
			log.warn("No blocking rounds were configured; probably a configuration issue.");
		}

		blockingSettings.setNumberOfRecordsToSample(blockingConfig.getNumberOfRecordsToSample());
		if (blockingConfig.getNumberOfRecordsToSample() < 1)
			throw new IndexOutOfBoundsException("Too small number of records to sample doesn't make any sense.");
		for (int i = 0; i < blockingConfig.getBlockingRounds().sizeOfBlockingRoundArray(); i++) {
			org.openhie.openempi.configuration.xml.basicblocking.BlockingRound round = blockingConfig.getBlockingRounds().getBlockingRoundArray(i);
			BlockingRound blockingRound = new BlockingRound();
			for (int j = 0; j < round.getBlockingFields().sizeOfBlockingFieldArray(); j++) {
				org.openhie.openempi.configuration.xml.basicblocking.BlockingField field = round.getBlockingFields().getBlockingFieldArray(j);
				log.trace("Looking for blocking field named: left=" + field.getLeftFieldName() + ", right=" + field.getRightFieldName());
				blockingRound.addField(field.getLeftFieldName(), field.getRightFieldName());
			}
			blockingRounds.add(blockingRound);
		}
		blockingSettings.setBlockingRounds(blockingRounds);
		registry.registerConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY, 
				blockingSettings);
	}

	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationData)
			throws InitializationException {
		BlockingSettings settings = (BlockingSettings) configurationData;
		BasicBlockingType newBasicBlocking = BasicBlockingType.Factory.newInstance();
		if (settings.getNumberOfRecordsToSample() < 1)
			throw new IndexOutOfBoundsException("Too small number of records to sample doesn't make any sense.");
		newBasicBlocking.setNumberOfRecordsToSample(settings.getNumberOfRecordsToSample());
		List<BlockingRound> rounds = settings.getBlockingRounds();
		BasicBlockingType xmlConfigurationFragment = buildConfigurationFileFragment(newBasicBlocking, rounds);
		log.debug("Saving blocking info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveBlockingConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated blocking configuration in configuration registry: " + rounds);
		registry.registerConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY, settings);
	}

	private BasicBlockingType buildConfigurationFileFragment(BasicBlockingType newBasicBlocking, List<BlockingRound> rounds) {
		BlockingRounds roundsNode = newBasicBlocking.addNewBlockingRounds();
		for (BlockingRound blockingRound : rounds) {
			org.openhie.openempi.configuration.xml.basicblocking.BlockingRound roundNode = roundsNode.addNewBlockingRound();
			BlockingFields blockingFields = roundNode.addNewBlockingFields();
			for (org.openhie.openempi.configuration.BaseFieldPair field : blockingRound.getFields()) {
				BlockingField xmlField = blockingFields.addNewBlockingField();
				xmlField.setLeftFieldName(field.getLeftFieldName());
				xmlField.setRightFieldName(field.getRightFieldName());
			}
		}
		return newBasicBlocking;
	}
}
