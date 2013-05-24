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
package org.openempi.webapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.ConfigurationDataService;
import org.openempi.webapp.client.model.AdminConfigurationWeb;
import org.openempi.webapp.client.model.BloomfilterSettingsWeb;
import org.openempi.webapp.client.model.ComponentSettingsWeb;
import org.openempi.webapp.client.model.DataIntegratorSettingsWeb;
import org.openempi.webapp.client.model.EMSettingsWeb;
import org.openempi.webapp.client.model.KeyServerSettingsWeb;
import org.openempi.webapp.client.model.MatchConfigurationWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.model.ParameterManagerSettingsWeb;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingFieldWeb;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingSettingsWeb;
import org.openempi.webapp.client.model.StringComparatorFunctionWeb;
import org.openempi.webapp.client.model.TransformationFunctionWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.configuration.AdminConfiguration;
import org.openhie.openempi.configuration.Component;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingField;
import org.openhie.openempi.configuration.PrivacyPreservingBlockingSettings;
import org.openhie.openempi.configuration.Component.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.fellegisunter.EMSettings;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.matching.fellegisunter.ProbabilisticMatchingConstants;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration.FieldQuerySelector;
import org.openhie.openempi.recordlinkage.configuration.BloomfilterSettings;
import org.openhie.openempi.recordlinkage.configuration.ComponentSettings;
import org.openhie.openempi.recordlinkage.configuration.DataIntegratorSettings;
import org.openhie.openempi.recordlinkage.configuration.KeyServerSettings;
import org.openhie.openempi.recordlinkage.configuration.ParameterManagerSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConfigurationDataServiceImpl extends RemoteServiceServlet implements ConfigurationDataService
{
	private static final long serialVersionUID = 543196329181659673L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}

	public BloomfilterSettingsWeb loadBloomfilterSettings()
	{
		log.debug("Received request to load the bloomfilter settings data.");
		try {
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			return convertToClientModel(privacySettings.getBloomfilterSettings());
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private BloomfilterSettingsWeb convertToClientModel(BloomfilterSettings bloomfilterSettings) {
		BloomfilterSettingsWeb bloomfilterSettingsWeb = new BloomfilterSettingsWeb();
		bloomfilterSettingsWeb.setNGramSize(bloomfilterSettings.getNGramSize());
		bloomfilterSettingsWeb.setDefaultM(bloomfilterSettings.getDefaultM());
		bloomfilterSettingsWeb.setDefaultK(bloomfilterSettings.getDefaultK());
		return bloomfilterSettingsWeb;
	}

	public ComponentSettingsWeb loadComponentSettings()
	{
		log.debug("Received request to load the component settings data.");
		try {
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			return convertToClientModel(privacySettings.getComponentSettings());
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private ComponentSettingsWeb convertToClientModel(ComponentSettings componentSettings) {
		ComponentSettingsWeb componentSettingsWeb = new ComponentSettingsWeb();
		KeyServerSettingsWeb keyServerSettingsWeb = new KeyServerSettingsWeb();
		keyServerSettingsWeb.setServerAddress(componentSettings.getKeyServerSettings().getServerAddress());
		keyServerSettingsWeb.setNumberOfSalts(componentSettings.getKeyServerSettings().getNumberOfSalts());
		keyServerSettingsWeb.setSaltIdStart(componentSettings.getKeyServerSettings().getSaltIdStart());
		keyServerSettingsWeb.setSaltIdStride(componentSettings.getKeyServerSettings().getSaltIdStride());
		componentSettingsWeb.setKeyServerSettings(keyServerSettingsWeb);
		ParameterManagerSettingsWeb parameterManagerSettingsWeb = new ParameterManagerSettingsWeb();
		parameterManagerSettingsWeb.setServerAddress(componentSettings.getParameterManagerSettings().getServerAddress());
		componentSettingsWeb.setParameterManagerSettings(parameterManagerSettingsWeb);
		DataIntegratorSettingsWeb dataIntegratorSettingsWeb = new DataIntegratorSettingsWeb();
		dataIntegratorSettingsWeb.setServerAddress(componentSettings.getDataIntegratorSettings().getServerAddress());
		componentSettingsWeb.setDataIntegratorSettings(dataIntegratorSettingsWeb);
		return componentSettingsWeb;
	}

	public List<StringComparatorFunctionWeb> getStringComparatorFunctionList() {
		log.debug("Received request to retrieve a list of string comparator function.");
		List<StringComparatorFunctionWeb> functionList = new ArrayList<StringComparatorFunctionWeb>();
		String[] functionNames = 
			Context.getApplicationContext().getBeanNamesForType(org.openhie.openempi.stringcomparison.metrics.AbstractDistanceMetric.class);
		log.trace("String Comparator Function Names:");
		for (String functionName: functionNames) {
			StringComparatorFunctionWeb compFunc = new StringComparatorFunctionWeb();
			compFunc.setClassName(functionName);
			log.trace(" " + functionName);
			functionList.add(compFunc);
		}
		return functionList;
	}

	public List<TransformationFunctionWeb> getTransfromationFunctionList() {
		log.debug("Received request to retrieve a list of transformation functions.");
		try {
			List<TransformationFunctionWeb> functionList = new ArrayList<TransformationFunctionWeb>();
			String[] functionNames = 
				Context.getApplicationContext().getBeanNamesForType(org.openhie.openempi.transformation.function.AbstractTransformationFunctionBase.class);
			log.trace("Transormation Function Names:");
			for (String functionName: functionNames) {
				TransformationFunctionWeb compFunc = new TransformationFunctionWeb();
				compFunc.setClassName(functionName);
				log.trace(" " + functionName);
				functionList.add(compFunc);
			}
			return functionList;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public PrivacyPreservingBlockingSettingsWeb loadPrivacyPreservingBlockingSettings() {
		log.debug("Received request to retrieve a Privacy Preserving Blocking Settings.");
		try {
			PrivacyPreservingBlockingSettings ppbs = Context.getConfiguration().getPrivacyPreservingBlockingSettings();
			PrivacyPreservingBlockingSettingsWeb ppbsWeb = new PrivacyPreservingBlockingSettingsWeb();
			ppbsWeb.setNumberOfBlockingBits(ppbs.getNumberOfBlockingBits());
			ppbsWeb.setNumberOfRuns(ppbs.getNumberOfRuns());
			List<PrivacyPreservingBlockingField> ppbFields = ppbs.getPrivacyPreservingBlockingFields();
			List<PrivacyPreservingBlockingFieldWeb> ppbFieldsWeb = new ArrayList<PrivacyPreservingBlockingFieldWeb>();
			for(PrivacyPreservingBlockingField ppbField : ppbFields) {
				PrivacyPreservingBlockingFieldWeb ppbFieldWeb = new PrivacyPreservingBlockingFieldWeb();
				ppbFieldWeb.setLeftFieldName(ppbField.getLeftFieldName());
				ppbFieldWeb.setRightFieldName(ppbField.getRightFieldName());
				List<Integer> bits = new ArrayList<Integer>();
				for (Integer bit : ppbField.getBits())
					bits.add(bit);
				ppbFieldWeb.setBits(bits);
				ppbFieldsWeb.add(ppbFieldWeb);
			}
			ppbsWeb.setPrivacyPreservingBlockingFields(ppbFieldsWeb);
			return ppbsWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private MatchConfiguration loadMatchConfiguration() {
		Configuration configuration = Context.getConfiguration();
		MatchConfiguration matchConfiguration = (MatchConfiguration)
			configuration.lookupConfigurationEntry(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_CONFIGURATION_REGISTRY_KEY);
		return matchConfiguration;
	}

	private MatchConfigurationWeb loadMatchConfigurationWeb() {
		return convertToClientModel(loadMatchConfiguration());
	}

	public List<MatchFieldWeb> loadMatchFieldConfiguration() {
		log.debug("Received request to load the match field configuration data.");
		try {
			return loadMatchConfigurationWeb().getMatchFields();
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public MatchConfigurationWeb loadMatchConfigurationParameters() {
		log.debug("Received request to load the match configuration parameters data.");
		try {
			return loadMatchConfigurationWeb();
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public MatchConfigurationWeb convertToClientModel(MatchConfiguration matchConfiguration) {
		MatchConfigurationWeb matchConfigurationWeb = new MatchConfigurationWeb();
		matchConfigurationWeb.setFalseNegativeProbability(matchConfiguration.getFalseNegativeProbability());
		matchConfigurationWeb.setFalsePositiveProbability(matchConfiguration.getFalsePositiveProbability());
		EMSettingsWeb emSettingsWeb = new EMSettingsWeb();
		EMSettings emSettings = matchConfiguration.getEmSettings();
		emSettingsWeb.setMInitial(emSettings.getmInitial());
		emSettingsWeb.setUInitial(emSettings.getuInitial());
		emSettingsWeb.setPInitial(emSettings.getpInitial());
		emSettingsWeb.setConvergenceError(emSettings.getConvergenceError());
		emSettingsWeb.setMaxIterations(emSettings.getMaxIterations());
		emSettingsWeb.setMaxTries(emSettings.getMaxTries());
		matchConfigurationWeb.setEMSettings(emSettingsWeb);
		List<MatchField> matchFields = matchConfiguration.getMatchFields(FieldQuerySelector.AllFields);
		List<MatchFieldWeb> matchFieldsWeb = new ArrayList<MatchFieldWeb>();
		for(MatchField matchField : matchFields) {
			MatchFieldWeb matchFieldWeb = new MatchFieldWeb();
			matchFieldWeb.setLeftFieldName(matchField.getLeftFieldName());
			matchFieldWeb.setRightFieldName(matchField.getRightFieldName());
			matchFieldWeb.setAgreementProbability(matchField.getAgreementProbability());
			matchFieldWeb.setDisagreementProbability(matchField.getDisagreementProbability());
			matchFieldWeb.setComparatorFunction(ModelTransformer.convertToClientModel(matchField.getComparatorFunction()));
			matchFieldWeb.setMatchThreshold(matchField.getMatchThreshold());
			matchFieldWeb.updateRedunantFields();
			matchFieldsWeb.add(matchFieldWeb);
		}
		matchConfigurationWeb.setMatchFields(matchFieldsWeb);
		return matchConfigurationWeb;
	}

	public AdminConfigurationWeb loadAdminConfiguration() {
		log.debug("Received request to retrieve the admin configuration.");
		try {
			AdminConfiguration adminConfiguration = Context.getConfiguration().getAdminConfiguration();
			AdminConfigurationWeb adminConfigurationWeb = new AdminConfigurationWeb();
			adminConfigurationWeb.setConfigFileDirectory(adminConfiguration.getFileRepositoryDirectory());
			if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.DATA_PROVIDER_MODE)
				adminConfigurationWeb.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE);
			else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.DATA_INTEGRATOR_MODE)
				adminConfigurationWeb.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE);
			else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.PARAMETER_MANAGER_MODE)
				adminConfigurationWeb.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE);
			else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.KEYSERVER_MODE)
				adminConfigurationWeb.setComponentMode(AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE);
			adminConfigurationWeb.setExperimentalMode(adminConfiguration.getExperimentalMode());
			return adminConfigurationWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public String saveBloomfilterSettings(BloomfilterSettingsWeb bloomfilterSettings)
	{
		log.debug("Received request to save bloom filter settings.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.RLPROTOCOL);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			privacySettings.setBloomfilterSettings(convertFromClientModel(bloomfilterSettings));
			loader.saveAndRegisterComponentConfiguration(configuration, privacySettings);
		} catch (Exception e) {
			log.error("Failed while saving the bloomfilter settings: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private BloomfilterSettings convertFromClientModel(BloomfilterSettingsWeb bloomfilterSettingsWeb) {
		BloomfilterSettings bloomfilterSettings = new BloomfilterSettings();
		bloomfilterSettings.setNGramSize(bloomfilterSettingsWeb.getNGramSize());
		bloomfilterSettings.setDefaultM(bloomfilterSettingsWeb.getDefaultM());
		bloomfilterSettings.setDefaultK(bloomfilterSettingsWeb.getDefaultK());
		return bloomfilterSettings;
	}

	public String saveComponentSettings(ComponentSettingsWeb componentSettings)
	{
		log.debug("Received request to save component settings.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.RLPROTOCOL);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			PrivacySettings privacySettings =
					(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
			privacySettings.setComponentSettings(convertFromClientModel(componentSettings));
			loader.saveAndRegisterComponentConfiguration(configuration, privacySettings);
		} catch (Exception e) {
			log.error("Failed while saving the component settings: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private ComponentSettings convertFromClientModel(ComponentSettingsWeb componentSettingsWeb) {
		ComponentSettings componentSettings = new ComponentSettings();
		KeyServerSettings keyServerSettings = new KeyServerSettings();
		keyServerSettings.setServerAddress(componentSettingsWeb.getKeyServerSettings().getServerAddress());
		keyServerSettings.setNumberOfSalts(componentSettingsWeb.getKeyServerSettings().getNumberOfSalts());
		keyServerSettings.setSaltIdStart(componentSettingsWeb.getKeyServerSettings().getSaltIdStart());
		keyServerSettings.setSaltIdStride(componentSettingsWeb.getKeyServerSettings().getSaltIdStride());
		componentSettings.setKeyServerSettings(keyServerSettings);
		ParameterManagerSettings parameterManagerSettings = new ParameterManagerSettings();
		parameterManagerSettings.setServerAddress(componentSettingsWeb.getParameterManagerSettings().getServerAddress());
		componentSettings.setParameterManagerSettings(parameterManagerSettings);
		DataIntegratorSettings dataIntegratorSettings = new DataIntegratorSettings();
		dataIntegratorSettings.setServerAddress(componentSettingsWeb.getDataIntegratorSettings().getServerAddress());
		componentSettings.setDataIntegratorSettings(dataIntegratorSettings);
		return componentSettings;
	}

	public String savePrivacyPreservingBlockingSettings(PrivacyPreservingBlockingSettingsWeb ppBlockingSettingsWeb) {
		log.debug("Received request to save Privacy Preserving Blocking Settings.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			configuration.saveAndRegisterPrivacyPreservingBlockingSettings(
					convertFromClientModel(ppBlockingSettingsWeb));
		} catch (Exception e) {
			log.error("Failed to execute: " + e.getMessage(), e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private PrivacyPreservingBlockingSettings convertFromClientModel(PrivacyPreservingBlockingSettingsWeb ppBlockingSettingsWeb) {
		PrivacyPreservingBlockingSettings ppBlockingSettings = new PrivacyPreservingBlockingSettings();
		ppBlockingSettings.setNumberOfBlockingBits(ppBlockingSettingsWeb.getNumberOfBlockingBits());
		ppBlockingSettings.setNumberOfRuns(ppBlockingSettingsWeb.getNumberOfRuns());
		List<PrivacyPreservingBlockingField> ppBlockingFields = new ArrayList<PrivacyPreservingBlockingField>();
		for(PrivacyPreservingBlockingFieldWeb ppBlockingFieldWeb: ppBlockingSettingsWeb.getPrivacyPreservingBlockingFields()) {
			PrivacyPreservingBlockingField ppBlockingField = new PrivacyPreservingBlockingField();
			ppBlockingField.setLeftFieldName(ppBlockingFieldWeb.getLeftFieldName());
			ppBlockingField.setRightFieldName(ppBlockingFieldWeb.getRightFieldName());
			List<Integer> bits = new ArrayList<Integer>();
			for (Integer bit : ppBlockingFieldWeb.getBits())
				bits.add(bit);
			ppBlockingField.setBits(bits);
			ppBlockingFields.add(ppBlockingField);
		}
		ppBlockingSettings.setPrivacyPreservingBlockingFields(ppBlockingFields);
		return ppBlockingSettings;
	}

	/**
	 * TODO This needs to be moved out of here and into a service that is specific to the matching algorithm
	 * that it configures.
	 */
	public String saveMatchFieldConfiguration(List<MatchFieldWeb> matchFieldsWeb) {
		log.debug("Received request to save match field configuration.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.MATCHING);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			MatchConfiguration matchConfiguration = loadMatchConfiguration();
			matchConfiguration.setMatchFields(convertFromClientModel(matchFieldsWeb));
			loader.saveAndRegisterComponentConfiguration(configuration, matchConfiguration);
		} catch (Exception e) {
			log.warn("Failed while saving the matching configuration: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	/**
	 * TODO This needs to be moved out of here and into a service that is specific to the matching algorithm
	 * that it configures.
	 */
	public String saveMatchConfigurationParameters(MatchConfigurationWeb matchConfigurationWeb) {
		log.debug("Received request to save match configuration.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.MATCHING);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			MatchConfiguration matchConfiguration = loadMatchConfiguration();
			convertFromClientModel(matchConfigurationWeb, matchConfiguration);
			loader.saveAndRegisterComponentConfiguration(configuration, matchConfiguration);
		} catch (Exception e) {
			log.warn("Failed while saving the matching configuration: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private List<MatchField> convertFromClientModel(List<MatchFieldWeb> matchFieldsWeb) {
		List<MatchField> matchFields = new ArrayList<MatchField>();
		for(MatchFieldWeb matchFieldWeb: matchFieldsWeb) {
			MatchField matchField = new MatchField();
			matchField.setLeftFieldName(matchFieldWeb.getLeftFieldName());
			matchField.setRightFieldName(matchFieldWeb.getRightFieldName());
			matchField.setAgreementProbability(matchFieldWeb.getAgreementProbability());
			matchField.setDisagreementProbability(matchFieldWeb.getDisagreementProbability());
			matchField.setComparatorFunction(ModelTransformer.convertFromClientModel(matchFieldWeb.getComparatorFunction()));
			matchField.setMatchThreshold(matchFieldWeb.getMatchThreshold());
			matchFields.add(matchField);
		}
		return matchFields;
	}

	private void convertFromClientModel(MatchConfigurationWeb matchConfigurationWeb, MatchConfiguration matchConfiguration) {
		matchConfiguration.setFalseNegativeProbability(matchConfigurationWeb.getFalseNegativeProbability());
		matchConfiguration.setFalsePositiveProbability(matchConfigurationWeb.getFalsePositiveProbability());
		EMSettings emSettings = new EMSettings();
		EMSettingsWeb emSettingsWeb = matchConfigurationWeb.getEMSettings();
		emSettings.setmInitial(emSettingsWeb.getMInitial());
		emSettings.setuInitial(emSettingsWeb.getUInitial());
		emSettings.setpInitial(emSettingsWeb.getPInitial());
		emSettings.setConvergenceError(emSettings.getConvergenceError());
		emSettings.setMaxIterations(emSettings.getMaxIterations());
		emSettings.setMaxTries(emSettings.getMaxTries());
		matchConfiguration.setEmSettings(emSettings);
	}

	public String saveAdminConfiguration(AdminConfigurationWeb adminConfiguration) {
		log.debug("Received request to save admin configuration.");
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			configuration.saveAndRegisterAdminConfiguration(
					convertFromClientModel(adminConfiguration));
		} catch (Exception e) {
			log.error("Failed to execute: " + e.getMessage(), e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private AdminConfiguration convertFromClientModel(AdminConfigurationWeb adminConfigurationWeb) {
		AdminConfiguration adminConfiguration = new AdminConfiguration();
		adminConfiguration.setFileRepositoryDirectory(adminConfigurationWeb.getConfigFileDirectory());
		if (adminConfigurationWeb.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE)
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.DATA_PROVIDER_MODE);
		else if (adminConfigurationWeb.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE)
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.DATA_INTEGRATOR_MODE);
		else if (adminConfigurationWeb.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE)
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.PARAMETER_MANAGER_MODE);
		else if (adminConfigurationWeb.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE)
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.KEYSERVER_MODE);
		adminConfiguration.setExperimentalMode(adminConfigurationWeb.getExperimentalMode());
		return adminConfiguration;
	}

	public void authenticateUser(String username, String password) {
		String sessionKey = Context.getUserContext().getSessionKey();
		if (sessionKey == null) {
			Context.authenticate(username, password);
		}
		log.debug("Currently logged in user is " + Context.getUserContext().getUser());
	}

}
