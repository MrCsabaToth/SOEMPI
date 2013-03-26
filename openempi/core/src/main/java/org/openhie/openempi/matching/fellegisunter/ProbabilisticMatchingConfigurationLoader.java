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
package org.openhie.openempi.matching.fellegisunter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.xml.MatchingConfigurationType;
import org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.util.ValidationUtil;

public class ProbabilisticMatchingConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(ProbabilisticMatchingConfigurationLoader.class);
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the probabilistic matching service
		//
		if (!(configurationFragment instanceof ProbabilisticMatchingType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration registry so that
		// it is available for the matching service to use when needed.
		//
		MatchConfiguration config = new MatchConfiguration();
		registry.registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, config);
		
		ProbabilisticMatchingType matchConfigXml = (ProbabilisticMatchingType) configurationFragment;
		log.debug("Received xml fragment to parse: " + matchConfigXml);
		config.setFalseNegativeProbability(matchConfigXml.getFalseNegativeProbability());
		config.setFalsePositiveProbability(matchConfigXml.getFalsePositiveProbability());
		EMSettings emSettings = buildEMSettingsFromXml(matchConfigXml.getEmSettings());
		config.setEmSettings(emSettings);
		for (int i=0; i < matchConfigXml.getMatchFields().sizeOfMatchFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.probabilisticmatching.MatchField field = matchConfigXml.getMatchFields().getMatchFieldArray(i);
			MatchField matchField = buildMatchFieldFromXml(field);
			config.addMatchField(matchField);
		}
	}

	private EMSettings buildEMSettingsFromXml(org.openhie.openempi.configuration.xml.probabilisticmatching.EmSettings emSettingsXml) {
		EMSettings emSettings = new EMSettings();
		emSettings.setmInitial(emSettingsXml.getMInitial());
		emSettings.setuInitial(emSettingsXml.getUInitial());
		emSettings.setpInitial(emSettingsXml.getPInitial());
		emSettings.setConvergenceError(emSettingsXml.getConvergenceError());
		emSettings.setMaxIterations(emSettingsXml.getMaxIterations());
		emSettings.setMaxTries(emSettingsXml.getMaxTries());
		return emSettings;
	}

	private MatchField buildMatchFieldFromXml(org.openhie.openempi.configuration.xml.probabilisticmatching.MatchField field) {
		MatchField matchField = new MatchField();
		String leftFieldName = field.getLeftFieldName();
		ValidationUtil.sanityCheckFieldName(leftFieldName);
		matchField.setLeftFieldName(leftFieldName);
		String rightFieldName = field.getRightFieldName();
		ValidationUtil.sanityCheckFieldName(rightFieldName);
		matchField.setRightFieldName(rightFieldName);
		matchField.setAgreementProbability(field.getAgreementProbability());
		matchField.setDisagreementProbability(field.getDisagreementProbability());
		matchField.setMatchThreshold(field.getMatchThreshold());
		org.openhie.openempi.configuration.xml.FunctionField compFuncXml = field.getComparatorFunction();
		FunctionField comparatorFunction = Configuration.buildFunctionFieldFromXml(compFuncXml);
		matchField.setComparatorFunction(comparatorFunction);
		return matchField;
	}

	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationData)
			throws InitializationException {
		MatchConfiguration config = (MatchConfiguration) configurationData;
		MatchingConfigurationType xmlConfigurationFragment = buildMatchingConfigurationFragment(config);
		log.debug("Saving matching info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveMatchingConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated matching configuration in configuration registry: " + config);
		registry.registerConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY, config);
	}

	private ProbabilisticMatchingType buildMatchingConfigurationFragment(MatchConfiguration matchConfig) {
		ProbabilisticMatchingType matchingConfigurationType =
			ProbabilisticMatchingType.Factory.newInstance();
		matchingConfigurationType.setFalseNegativeProbability(matchConfig.getFalseNegativeProbability());
		matchingConfigurationType.setFalsePositiveProbability(matchConfig.getFalsePositiveProbability());
		buildEMSettingsFragment(matchConfig.getEmSettings(), matchingConfigurationType);
		org.openhie.openempi.configuration.xml.probabilisticmatching.MatchFields matchFieldsXml =
			matchingConfigurationType.addNewMatchFields();
		for (MatchField matchField : matchConfig.getMatchFields(FieldQuerySelector.AllFields)) {
			buildMatchFieldFragment(matchField, matchFieldsXml);
		}
		return matchingConfigurationType;
	}

	private void buildEMSettingsFragment(EMSettings emSettings, ProbabilisticMatchingType matchingConfigurationType) {
		org.openhie.openempi.configuration.xml.probabilisticmatching.EmSettings emSettingsXml =
				matchingConfigurationType.addNewEmSettings();
		emSettingsXml.setMInitial(emSettings.getmInitial());
		emSettingsXml.setUInitial(emSettings.getuInitial());
		emSettingsXml.setPInitial(emSettings.getpInitial());
		emSettingsXml.setConvergenceError(emSettings.getConvergenceError());
		emSettingsXml.setMaxIterations(emSettings.getMaxIterations());
		emSettingsXml.setMaxTries(emSettings.getMaxTries());
	}

	private void buildMatchFieldFragment(MatchField matchField,
			org.openhie.openempi.configuration.xml.probabilisticmatching.MatchFields matchFieldsXml) {
		org.openhie.openempi.configuration.xml.probabilisticmatching.MatchField matchFieldXml =
				matchFieldsXml.addNewMatchField();
		String leftFieldName = matchField.getLeftFieldName();
		ValidationUtil.sanityCheckFieldName(leftFieldName);
		matchFieldXml.setLeftFieldName(leftFieldName);
		String rightFieldName = matchField.getRightFieldName();
		ValidationUtil.sanityCheckFieldName(rightFieldName);
		matchFieldXml.setRightFieldName(rightFieldName);
		matchFieldXml.setAgreementProbability(matchField.getAgreementProbability());
		matchFieldXml.setDisagreementProbability(matchField.getDisagreementProbability());
		org.openhie.openempi.configuration.xml.FunctionField compFuncXml = matchFieldXml.addNewComparatorFunction();
		Configuration.buildFunctionFieldFragment(compFuncXml, matchField.getComparatorFunction());
		matchFieldXml.setMatchThreshold(matchField.getMatchThreshold());		
	}

}
