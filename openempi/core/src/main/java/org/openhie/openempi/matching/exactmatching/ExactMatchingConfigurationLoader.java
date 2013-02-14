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
package org.openhie.openempi.matching.exactmatching;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.xml.MatchingConfigurationType;
import org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.MatchField;

public class ExactMatchingConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(ExactMatchingConfigurationLoader.class);
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the exact matching service
		//
		if (!(configurationFragment instanceof ExactMatchingType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the matching service to use when needed.
		//
		ArrayList<MatchField> matchFields = new ArrayList<MatchField>();
		registry.registerConfigurationEntry(ExactMatchingConstants.EXACT_MATCHING_FIELDS_REGISTRY_KEY, 
				matchFields);
		
		ExactMatchingType matchingConfig = (ExactMatchingType) configurationFragment;
		log.debug("Received xml fragment to parse: " + matchingConfig);
		if (matchingConfig == null || matchingConfig.getMatchFields().sizeOfMatchFieldArray() == 0) {
			log.warn("No matching fields were configured; probably a configuration issue.");
			return;
		}
		
		for (int i=0; i < matchingConfig.getMatchFields().sizeOfMatchFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.exactmatching.MatchField matchField = matchingConfig.getMatchFields().getMatchFieldArray(i);
			matchFields.add(buildMatchFieldFromXml(matchField));
		}
	}

	@SuppressWarnings("unchecked")
	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationData)
			throws InitializationException {
		List<MatchField> fields = (List<MatchField>) configurationData;
		MatchingConfigurationType xmlConfigurationFragment = buildMatchingConfigurationFragment(fields);
		log.debug("Saving matching info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveMatchingConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated matching configuration in configuration registry: " + fields);
		registry.registerConfigurationEntry(ExactMatchingConstants.EXACT_MATCHING_FIELDS_REGISTRY_KEY, fields);
	}
	
	private MatchField buildMatchFieldFromXml(org.openhie.openempi.configuration.xml.exactmatching.MatchField field) {
		MatchField matchField = new MatchField();
		matchField.setLeftFieldName(field.getLeftFieldName());
		matchField.setRightFieldName(field.getRightFieldName());
		if (field.getComparatorFunction() != null) {
			matchField.setComparatorFunction(Configuration.buildFunctionFieldFromXml(field.getComparatorFunction()));
		}
		matchField.setMatchThreshold(field.getMatchThreshold());
		return matchField;
	}

	private org.openhie.openempi.configuration.xml.MatchingConfigurationType buildMatchingConfigurationFragment(List<MatchField> fields) {
		org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType matchingType =
			org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType.Factory.newInstance();
		org.openhie.openempi.configuration.xml.exactmatching.MatchFields matchFieldsXml = matchingType.addNewMatchFields();
		for (MatchField matchField : fields) {
			org.openhie.openempi.configuration.xml.exactmatching.MatchField matchFieldXml = matchFieldsXml.addNewMatchField();
			matchFieldXml.setLeftFieldName(matchField.getLeftFieldName());
			matchFieldXml.setRightFieldName(matchField.getRightFieldName());
			if (matchField.getComparatorFunction() != null) {
				org.openhie.openempi.configuration.xml.FunctionField comparisonFunctionXml = matchFieldXml.addNewComparatorFunction();
				Configuration.buildFunctionFieldFragment(comparisonFunctionXml, matchField.getComparatorFunction());
			}
			matchFieldXml.setMatchThreshold(matchField.getMatchThreshold());
		}
		return matchingType;
	}	
}
