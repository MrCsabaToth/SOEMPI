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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openhie.openempi.Constants;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.configuration.Component.ComponentType;
import org.openhie.openempi.configuration.Component.ExtensionInterface;
import org.openhie.openempi.configuration.xml.BlockingConfigurationType;
import org.openhie.openempi.configuration.xml.DataLoaderConfigurationType;
import org.openhie.openempi.configuration.xml.MatchingConfigurationType;
import org.openhie.openempi.configuration.xml.MpiConfigDocument;
import org.openhie.openempi.configuration.xml.PrivacyPreservingBlocking;
import org.openhie.openempi.configuration.xml.RecordLinkageProtocolConfigurationType;
import org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType;
import org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface.Enum;
import org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument;
import org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.configuration.DataLoaderConfigurationService;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.recordlinkage.configuration.RecordLinkageProtocolConfigurationService;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.util.ValidationUtil;

public class Configuration extends BaseServiceImpl implements ConfigurationRegistry
{
	protected static final Log log = LogFactory.getLog(Configuration.class);
	
	private String configFile;
	private MpiConfigDocument configuration;
	private PrivacyPreservingBlockingSettings ppbSettings;
	private AdminConfiguration adminConfiguration;
	
	private Map<String, Object> configurationRegistry;
	private Map<String, Component> extensionRegistry;
	
	public void init() {
		if (Context.getApplicationContext() == null)	// Needed for SOEMPI-SOEMPI EJB3 connections
			Context.startup();							// Otherwise we get an exception
		configureLoggingEnvironment();
		configurationRegistry = new HashMap<String, Object>();
		extensionRegistry = new HashMap<String, Component>();
		try {
			configuration = loadConfigurationFromSource();
			validateConfiguration(configuration);
			processConfiguration(configuration);
			log.info("System configuration: " + this.toString());
		} catch (Exception e) {
			log.error("Failed while locating and parsing the configuration file. System is shutting down due to: " + e, e);
			throw new RuntimeException("Failed while locating and parsing the configuration file. System is shutting down.");
		}
	}
	
	private void configureLoggingEnvironment() {
		String openEmpiHome = Context.getOpenEmpiHome();
		if (openEmpiHome != null && openEmpiHome.length() > 0) {
			String loggingConfigurationFile = openEmpiHome + "/conf/log4j.properties";
			PropertyConfigurator.configure(loggingConfigurationFile);
			log.info("Set the logging configuration file to " + loggingConfigurationFile);
		}
	}
	
	public DataLoaderConfigurationType getDataLoaderConfiguration() {
		return configuration.getMpiConfig().getDataLoaderConfiguration();
	}
	
	public DataLoaderConfigurationType saveDataLoaderConfiguration(DataLoaderConfigurationType dataLoaderConfig) {
		configuration.getMpiConfig().setDataLoaderConfiguration(dataLoaderConfig);
		return configuration.getMpiConfig().getDataLoaderConfiguration();
	}
	
	public BlockingConfigurationType getBlockingConfiguration() {
		return configuration.getMpiConfig().getBlockingConfiguration();
	}
	
	public BlockingConfigurationType saveBlockingConfiguration(BlockingConfigurationType blocking) {
		configuration.getMpiConfig().setBlockingConfiguration(blocking);
		return configuration.getMpiConfig().getBlockingConfiguration();
	}
	
	public RecordLinkageProtocolConfigurationType saveRecordLinkageProtoclConfiguration(RecordLinkageProtocolConfigurationType recordLinkageProtocolConfigurationType) {
		configuration.getMpiConfig().setRecordLinkageProtocolConfiguration(recordLinkageProtocolConfigurationType);
		return configuration.getMpiConfig().getRecordLinkageProtocolConfiguration();
	}
	
	private void processConfiguration(MpiConfigDocument configuration) throws NoSuchAlgorithmException {
		boolean canProcessModules = (Context.getApplicationContext() != null);
		if (canProcessModules) {
			processRecordLinkageProtocolConfiguration(configuration);
			processDataLoaderConfiguration(configuration);
			processBlockingConfiguration(configuration);
		}
		ppbSettings = processPrivacyPreservingBlockingSettings(configuration);
		if (canProcessModules)
			processMatchConfiguration(configuration);
		adminConfiguration = processAdminConfiguration(configuration);
	}

	private AdminConfiguration processAdminConfiguration(MpiConfigDocument configuration) {
		adminConfiguration = new AdminConfiguration();
		org.openhie.openempi.configuration.xml.AdminConfiguration adminConfigurationXml = configuration.getMpiConfig().getAdminConfiguration();
		if (configuration.getMpiConfig().getAdminConfiguration() != null) {
			adminConfiguration.setFileRepositoryDirectory(adminConfigurationXml.getFileRepositoryDirectory());
		}
		if (adminConfiguration.getFileRepositoryDirectory() == null) {
			adminConfiguration.setFileRepositoryDirectory(Constants.DEFAULT_FILE_REPOSITORY_DIRECTORY);
		}
		if (adminConfigurationXml.getComponentMode() == org.openhie.openempi.configuration.xml.ComponentType.DATA_PROVIDER) {
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.DATA_PROVIDER_MODE);
		} else if (adminConfigurationXml.getComponentMode() == org.openhie.openempi.configuration.xml.ComponentType.DATA_INTEGRATOR) {
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.DATA_INTEGRATOR_MODE);
		} else if (adminConfigurationXml.getComponentMode() == org.openhie.openempi.configuration.xml.ComponentType.PARAMETER_MANAGER) {
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.PARAMETER_MANAGER_MODE);
		} else if (adminConfigurationXml.getComponentMode() == org.openhie.openempi.configuration.xml.ComponentType.KEY_SERVER) {
			adminConfiguration.setComponentMode(AdminConfiguration.ComponentType.KEYSERVER_MODE);
		}
		adminConfiguration.setExperimentalMode(adminConfigurationXml.getExperimentalMode());
		registerConfigurationEntry(ConfigurationRegistry.ADMIN_CONFIGURATION, adminConfiguration);
		return adminConfiguration;
	}

	public void saveAndRegisterAdminConfiguration(AdminConfiguration adminConfiguration) {
		org.openhie.openempi.configuration.xml.AdminConfiguration adminConfigurationType =
			buildAdminConfigurationFileFragment(adminConfiguration);
		configuration.getMpiConfig().setAdminConfiguration(adminConfigurationType);
		saveConfiguration();
		registerConfigurationEntry(ConfigurationRegistry.ADMIN_CONFIGURATION, adminConfiguration);
	}

	private org.openhie.openempi.configuration.xml.AdminConfiguration buildAdminConfigurationFileFragment(AdminConfiguration adminConfiguration) {
		org.openhie.openempi.configuration.xml.AdminConfiguration adminConfigurationType =
			org.openhie.openempi.configuration.xml.AdminConfiguration.Factory.newInstance();

		adminConfigurationType.setFileRepositoryDirectory(adminConfiguration.getFileRepositoryDirectory());
		if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.DATA_PROVIDER_MODE) {
			adminConfigurationType.setComponentMode(org.openhie.openempi.configuration.xml.ComponentType.DATA_PROVIDER);
		} else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.DATA_INTEGRATOR_MODE) {
			adminConfigurationType.setComponentMode(org.openhie.openempi.configuration.xml.ComponentType.DATA_INTEGRATOR);
		} else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.PARAMETER_MANAGER_MODE) {
			adminConfigurationType.setComponentMode(org.openhie.openempi.configuration.xml.ComponentType.PARAMETER_MANAGER);
		} else if (adminConfiguration.getComponentMode() == AdminConfiguration.ComponentType.KEYSERVER_MODE) {
			adminConfigurationType.setComponentMode(org.openhie.openempi.configuration.xml.ComponentType.KEY_SERVER);
		}
		adminConfigurationType.setExperimentalMode(adminConfiguration.getExperimentalMode());

		return adminConfigurationType;
	}

	private PrivacyPreservingBlockingSettings processPrivacyPreservingBlockingSettings(MpiConfigDocument configuration)
	{
		PrivacyPreservingBlockingSettings settings = new PrivacyPreservingBlockingSettings();
		PrivacyPreservingBlocking ppbSettingsXml = configuration.getMpiConfig().getPrivacyPreservingBlocking();
		if (ppbSettingsXml.getNumberOfBlockingBits() < 1)
			throw new IndexOutOfBoundsException("Too small number of blocking bits doesn't make any sense.");
		settings.setNumberOfBlockingBits(ppbSettingsXml.getNumberOfBlockingBits());
		if (ppbSettingsXml.getNumberOfRuns() < 1)
			throw new IndexOutOfBoundsException("Too small number of runs doesn't make any sense.");
		settings.setNumberOfRuns(ppbSettingsXml.getNumberOfRuns());
		for (int i = 0; i < ppbSettingsXml.getPrivacyPreservingBlockingSourceFields().sizeOfPrivacyPreservingBlockingSourceFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.PrivacyPreservingBlockingSourceField field =
				ppbSettingsXml.getPrivacyPreservingBlockingSourceFields().getPrivacyPreservingBlockingSourceFieldArray(i);
			PrivacyPreservingBlockingField ppbField = buildPPBFieldFromXml(field);
			settings.addPrivacyPreservingBlockingField(ppbField);
		}
		registerConfigurationEntry(ConfigurationRegistry.PRIVACY_PRESERVING_BLOCKING_SETTINGS, settings);
		return settings;
	}

	private PrivacyPreservingBlockingField buildPPBFieldFromXml(org.openhie.openempi.configuration.xml.PrivacyPreservingBlockingSourceField field) {
		PrivacyPreservingBlockingField ppbField = new PrivacyPreservingBlockingField();
		String leftFieldName = field.getLeftFieldName();
		ValidationUtil.sanityCheckFieldName(leftFieldName);
		ppbField.setLeftFieldName(leftFieldName);
		String rightFieldName = field.getRightFieldName();
		ValidationUtil.sanityCheckFieldName(rightFieldName);
		ppbField.setRightFieldName(rightFieldName);
		org.openhie.openempi.configuration.xml.Bits bitsXml = field.getBits();
		List<Integer> bits = new ArrayList<Integer>();
		for (int i = 0; i < bitsXml.getBitArray().length; i++) {
			if (bitsXml.getBitArray(i) < 0)
				throw new IndexOutOfBoundsException("Negative bit index doesn't make any sense.");
			bits.add(bitsXml.getBitArray(i));
		}
		ppbField.setBits(bits);
		return ppbField;
	}
	
	public void saveAndRegisterPrivacyPreservingBlockingSettings(PrivacyPreservingBlockingSettings ppbSettings) {
		this.ppbSettings = ppbSettings;
		org.openhie.openempi.configuration.xml.PrivacyPreservingBlocking ppbSettingsType =
			buildPrivacyPreservingBlockingSettingsFragment(ppbSettings);
		configuration.getMpiConfig().setPrivacyPreservingBlocking(ppbSettingsType);
		saveConfiguration();
		registerConfigurationEntry(ConfigurationRegistry.PRIVACY_PRESERVING_BLOCKING_SETTINGS, ppbSettings);
	}

	private org.openhie.openempi.configuration.xml.PrivacyPreservingBlocking buildPrivacyPreservingBlockingSettingsFragment(PrivacyPreservingBlockingSettings ppbSettings) {
		org.openhie.openempi.configuration.xml.PrivacyPreservingBlocking privacyPreservingBlockingType =
			org.openhie.openempi.configuration.xml.PrivacyPreservingBlocking.Factory.newInstance();
		if (ppbSettings.getNumberOfBlockingBits() < 1)
			throw new IndexOutOfBoundsException("Too small number of blocking bits doesn't make any sense.");
		privacyPreservingBlockingType.setNumberOfBlockingBits(ppbSettings.getNumberOfBlockingBits());
		if (ppbSettings.getNumberOfRuns() < 1)
			throw new IndexOutOfBoundsException("Too small number of runs doesn't make any sense.");
		privacyPreservingBlockingType.setNumberOfRuns(ppbSettings.getNumberOfRuns());
		org.openhie.openempi.configuration.xml.PrivacyPreservingBlockingSourceFields ppbFieldsXml =
			privacyPreservingBlockingType.addNewPrivacyPreservingBlockingSourceFields();
		for (PrivacyPreservingBlockingField ppbField : ppbSettings.getPrivacyPreservingBlockingFields()) {
			org.openhie.openempi.configuration.xml.PrivacyPreservingBlockingSourceField ppbFieldXml =
				ppbFieldsXml.addNewPrivacyPreservingBlockingSourceField();
			String leftFieldName = ppbField.getLeftFieldName();
			ValidationUtil.sanityCheckFieldName(leftFieldName);
			ppbFieldXml.setLeftFieldName(leftFieldName);
			String rightFieldName = ppbField.getRightFieldName();
			ValidationUtil.sanityCheckFieldName(rightFieldName);
			ppbFieldXml.setRightFieldName(rightFieldName);
			org.openhie.openempi.configuration.xml.Bits bits =
				ppbFieldXml.addNewBits();
			for(Integer bit : ppbField.getBits()) {
				if (bit < 0)
					throw new IndexOutOfBoundsException("Negative bit index doesn't make any sense.");
				bits.addBit(bit);
			}
		}
		return privacyPreservingBlockingType;
	}

	static public FunctionField buildFunctionFieldFromXml(org.openhie.openempi.configuration.xml.FunctionField functionFieldXml)
	{
		String functionName = functionFieldXml.getFunctionName();
		ValidationUtil.sanityCheckFieldName(functionName);
		FunctionField functionField = new FunctionField(functionName);
		if (functionFieldXml.isSetParameters()) {
			org.openhie.openempi.configuration.xml.Parameters parametersXml = functionFieldXml.getParameters();
			Map<String, Object> funcParams = new HashMap<String, Object>();
			for (int i = 0; i < parametersXml.sizeOfParameterArray(); i++) {
				org.openhie.openempi.configuration.xml.Parameter parameterXml = parametersXml.getParameterArray(i);
				Object value =  null;
				if (parameterXml.getParameterType() == org.openhie.openempi.configuration.xml.ParameterType.STRING)
					value = parameterXml.getParameterValue();
				else if (parameterXml.getParameterType() == org.openhie.openempi.configuration.xml.ParameterType.INTEGER)
					value = Integer.valueOf(parameterXml.getParameterValue());
				else if (parameterXml.getParameterType() == org.openhie.openempi.configuration.xml.ParameterType.DOUBLE)
					value = Double.valueOf(parameterXml.getParameterValue());
				else if (parameterXml.getParameterType() == org.openhie.openempi.configuration.xml.ParameterType.FLOAT)
					value = Float.valueOf(parameterXml.getParameterValue());
				funcParams.put(parameterXml.getParameterName(), value);
			}
			functionField.setFunctionParameters(funcParams);
		}
		return functionField;
	}
	
	public static void buildFunctionFieldFragment(org.openhie.openempi.configuration.xml.FunctionField functionFieldXml, FunctionField functionField) {
		String functionName = functionField.getFunctionName();
		ValidationUtil.sanityCheckFieldName(functionName);
		functionFieldXml.setFunctionName(functionName);
		if (functionField.getFunctionParameters() != null) {
			org.openhie.openempi.configuration.xml.Parameters functionParametersXml =
				functionFieldXml.addNewParameters();
			for (Map.Entry<String, Object> pairs : functionField.getFunctionParameters().entrySet()) {
				org.openhie.openempi.configuration.xml.Parameter functionParameterXml =
					functionParametersXml.addNewParameter();
				functionParameterXml.setParameterName(pairs.getKey());
				if (pairs.getValue() instanceof String) {
					functionParameterXml.setParameterValue((String)pairs.getValue());
					functionParameterXml.setParameterType(org.openhie.openempi.configuration.xml.ParameterType.STRING);
				} else if (pairs.getValue() instanceof Integer) {
					functionParameterXml.setParameterValue(Integer.toString((Integer)pairs.getValue()));
					functionParameterXml.setParameterType(org.openhie.openempi.configuration.xml.ParameterType.INTEGER);
				} else if (pairs.getValue() instanceof Double) {
					functionParameterXml.setParameterValue(Double.toString((Double)pairs.getValue()));
					functionParameterXml.setParameterType(org.openhie.openempi.configuration.xml.ParameterType.DOUBLE);
				} else if (pairs.getValue() instanceof Float) {
					functionParameterXml.setParameterValue(Float.toString((Float)pairs.getValue()));
					functionParameterXml.setParameterType(org.openhie.openempi.configuration.xml.ParameterType.FLOAT);
				}
		    }
		}
	}

	private void processRecordLinkageProtocolConfiguration(MpiConfigDocument configuration) {
		checkConfiguration(configuration);
		
		RecordLinkageProtocolConfigurationType obj = configuration.getMpiConfig().getRecordLinkageProtocolConfiguration();
		
		log.debug("Object is of type: " + obj.getDomNode().getNamespaceURI());
		String namespaceUriStr = obj.getDomNode().getNamespaceURI();
		URI namespaceURI = getNamespaceURI(namespaceUriStr);

		String resourcePath = generateComponentResourcePath(namespaceURI);
		Component component = loadAndRegisterComponentFromNamespaceUri(resourcePath);
		
		String configurationLoaderBean = getExtensionBeanNameFromComponent(component);
		
		ConfigurationLoader loader = (ConfigurationLoader) Context.getApplicationContext().getBean(configurationLoaderBean);
		loader.loadAndRegisterComponentConfiguration(this, obj);
		
		Component.Extension extension = component.getExtensionByExtensionInterface(ExtensionInterface.IMPLEMENTATION);
		if (extension == null) {
			log.error("Encountered a custom record linkage protocol component with no implementation extension: " + component);
			throw new InitializationException("Unable to locate an implementation component for custom record linkage protocol component " + component.getName());
		}
		log.debug("Registering implementation of file loader component named " + extension.getName() + " and implementation key " +
				extension.getImplementationKey());
		RecordLinkageProtocolConfigurationService recordLinkageProtocolConfigurationService = (RecordLinkageProtocolConfigurationService) 
			Context.getApplicationContext().getBean(extension.getImplementationKey());
		Context.registerCustomRecordLinkageProtocolConfigurationService(recordLinkageProtocolConfigurationService);
	}

	private void processBlockingConfiguration(MpiConfigDocument configuration) {
		checkConfiguration(configuration);
		
		BlockingConfigurationType obj = configuration.getMpiConfig().getBlockingConfiguration();
		
		log.debug("Object is of type: " + obj.getDomNode().getNamespaceURI());
		String namespaceUriStr = obj.getDomNode().getNamespaceURI();
		URI namespaceURI = getNamespaceURI(namespaceUriStr);

		String resourcePath = generateComponentResourcePath(namespaceURI);
		Component component = loadAndRegisterComponentFromNamespaceUri(resourcePath);
		
		String configurationLoaderBean = getExtensionBeanNameFromComponent(component);
		
		ConfigurationLoader loader = (ConfigurationLoader) Context.getApplicationContext().getBean(configurationLoaderBean);
		loader.loadAndRegisterComponentConfiguration(this, obj);
		
		Component.Extension extension = component.getExtensionByExtensionInterface(ExtensionInterface.IMPLEMENTATION);
		if (extension == null) {
			log.error("Encountered a custom blocking component with no implementation extension: " + component);
			throw new InitializationException("Unable to locate an implementation component for custom blocking component " + component.getName());
		}
		log.debug("Registering implementation of blocking component named " + extension.getName() + " and implementation key " +
				extension.getImplementationKey());
		BlockingService blockingService = (BlockingService) 
			Context.getApplicationContext().getBean(extension.getImplementationKey());
		Context.registerCustomBlockingService(blockingService);
	}
	
	public MatchingConfigurationType saveMatchingConfiguration(MatchingConfigurationType matchingConfiguration) {
		configuration.getMpiConfig().setMatchingConfiguration(matchingConfiguration);
		return configuration.getMpiConfig().getMatchingConfiguration();
	}
	
	private void processMatchConfiguration(MpiConfigDocument configuration) {
		
		MatchingConfigurationType obj = configuration.getMpiConfig().getMatchingConfiguration();
		
		log.debug("Object is of type: " + obj.getDomNode().getNamespaceURI());
		String namespaceUriStr = obj.getDomNode().getNamespaceURI();
		URI namespaceURI = getNamespaceURI(namespaceUriStr);

		String resourcePath = generateComponentResourcePath(namespaceURI);
		Component component = loadAndRegisterComponentFromNamespaceUri(resourcePath);
		
		String configurationLoaderBean = getExtensionBeanNameFromComponent(component);
		
		ConfigurationLoader loader = (ConfigurationLoader) Context.getApplicationContext().getBean(configurationLoaderBean);
		loader.loadAndRegisterComponentConfiguration(this, obj);
		
		Component.Extension extension = component.getExtensionByExtensionInterface(ExtensionInterface.IMPLEMENTATION);
		if (extension == null) {
			log.error("Encountered a custom matching component with no implementation extension: " + component);
			throw new InitializationException("Unable to locate an implementation component for custom matching component " + component.getName());
		}
		log.debug("Registering implementation of matching component named " + extension.getName() + " and implementation key " +
				extension.getImplementationKey());
		MatchingService matchingService = (MatchingService) 
			Context.getApplicationContext().getBean(extension.getImplementationKey());
		Context.registerCustomMatchingService(matchingService);		
	}
	
	private void processDataLoaderConfiguration(MpiConfigDocument configuration2) {
		checkConfiguration(configuration);
		
		DataLoaderConfigurationType obj = configuration.getMpiConfig().getDataLoaderConfiguration();
		
		log.debug("Object is of type: " + obj.getDomNode().getNamespaceURI());
		String namespaceUriStr = obj.getDomNode().getNamespaceURI();
		URI namespaceURI = getNamespaceURI(namespaceUriStr);

		String resourcePath = generateComponentResourcePath(namespaceURI);
		Component component = loadAndRegisterComponentFromNamespaceUri(resourcePath);
		
		String configurationLoaderBean = getExtensionBeanNameFromComponent(component);
		
		ConfigurationLoader loader = (ConfigurationLoader) Context.getApplicationContext().getBean(configurationLoaderBean);
		loader.loadAndRegisterComponentConfiguration(this, obj);
		
		Component.Extension extension = component.getExtensionByExtensionInterface(ExtensionInterface.IMPLEMENTATION);
		if (extension == null) {
			log.error("Encountered a custom file loader component with no implementation extension: " + component);
			throw new InitializationException("Unable to locate an implementation component for custom file loader component " + component.getName());
		}
		log.debug("Registering implementation of file loader component named " + extension.getName() + " and implementation key " +
				extension.getImplementationKey());
		DataLoaderConfigurationService dataLoaderConfigurationService = (DataLoaderConfigurationService) 
			Context.getApplicationContext().getBean(extension.getImplementationKey());
		Context.registerCustomDataLoaderConfigurationService(dataLoaderConfigurationService);
	}

	public String getExtensionBeanNameFromComponent(Component component) {
		Component.Extension extension = component.getExtensionByExtensionInterface(ExtensionInterface.CONFIGURATION_LOADER);
		if (extension == null) {
			log.error("Encountered a custom component with no configuration loader extension: " + component);
			throw new InitializationException("Unable to load configuration for custom component " + component.getName());
		}
		return extension.getImplementationKey();
	}

	public Component lookupExtensionComponentByComponentType(Component.ComponentType type) {
		if (type == null || type.componentTypeName() == null || type.componentTypeName().length() == 0) {
			log.warn("Looked up extension component with blank type name: " + type);
			return null;
		}
		
		log.debug("Looking up extension component of type: " + type.componentTypeName());
		return extensionRegistry.get(type.componentTypeName());
	}
	
	private Component loadAndRegisterComponentFromNamespaceUri(String resourcePath) {
		Component component;
		try {
			InputStream stream = Configuration.class.getResourceAsStream(resourcePath);
			MpiComponentDefinitionDocument componentDoc = MpiComponentDefinitionDocument.Factory.parse(stream);
			MpiComponentType componentXml = componentDoc.getMpiComponentDefinition().getMpiComponent();
			component = buildComponentFromXml(componentXml);
			log.debug("Loaded component: " + component);
			extensionRegistry.put(component.getComponentType().componentTypeName(), component);
			return component;
		} catch (IOException e) {
			log.error("Failed while loading component configuration file: " + resourcePath, e);
			throw new InitializationException("Failed while loading component configuration file " + resourcePath);
		} catch (XmlException e) {
			log.error("Failed while parsing component configuration file: " + resourcePath, e);
			throw new InitializationException("Failed while parsing component configuration file " + resourcePath);
		}
	}

	private Component buildComponentFromXml(MpiComponentType componentXml) {
		Component component = new Component(componentXml.getName());
		if (componentXml.getDescription() != null) {
			component.setDescription(componentXml.getDescription());
		}
		
		if (componentXml.getComponentType().intValue() == MpiComponentType.ComponentType.INT_BLOCKING) {
			component.setComponentType(ComponentType.BLOCKING);
		} else if (componentXml.getComponentType().intValue() == MpiComponentType.ComponentType.INT_MATCHING) {
			component.setComponentType(ComponentType.MATCHING);
		} else if (componentXml.getComponentType().intValue() == MpiComponentType.ComponentType.INT_DATALOADER) {
			component.setComponentType(ComponentType.DATALOADER);
		} else if (componentXml.getComponentType().intValue() == MpiComponentType.ComponentType.INT_RLPROTOCOL) {
			component.setComponentType(ComponentType.RLPROTOCOL);
		}

		log.debug("Component configuration: " + component.getName() + " of type " + component.getComponentType());
		for (int i=0; i < componentXml.getExtensions().sizeOfExtensionArray(); i++) {
			ExtensionType extension = componentXml.getExtensions().getExtensionArray(i);
			log.debug("Extension definition is " + extension);
			component.addExtension(extension.getName(), extension.getImplementation(), getExtensionInterfaceTypeById(extension.getInterface()));
		}
		return component;
	}

	private ExtensionInterface getExtensionInterfaceTypeById(Enum extensionInterface) {
		if (extensionInterface.intValue() == ExtensionType.Interface.INT_CONFIGURATION_LOADER) {
			return ExtensionInterface.CONFIGURATION_LOADER;
		} else if (extensionInterface.intValue() == ExtensionType.Interface.INT_CONFIGURATION_GUI) {
			return ExtensionInterface.CONFIGURATION_GUI;
		} else if (extensionInterface.intValue() == ExtensionType.Interface.INT_IMPLEMENTATION) {
			return ExtensionInterface.IMPLEMENTATION;
		}
		log.error("Unknown extension interface type encountered: " + extensionInterface);
		throw new RuntimeException("Unknown extension interface type encountered: " + extensionInterface);
	}

	private String generateComponentResourcePath(URI namespaceURI) {
        String resourcePath = "/META-INF" + namespaceURI.getPath() + "-openempi.xml";
//        File resourceFile = new File(resourcePath);
//        if (!resourceFile.exists() || !resourceFile.canRead()) {
//        	log.error("Unable to load component configuration file: " + resourcePath);
//        	throw new RuntimeException("Component configuration file " + resourcePath + " must be readable and present in the classpath");
//        }
//        String baseURI = resourceFile.getParent().replace('\\', '/');
        log.debug("Will locate configuration information for namespace from: " + resourcePath);
        return resourcePath;
	}

	private URI getNamespaceURI(String namespaceUriStr) {
		log.debug("Generating namespace URI for namespace " + namespaceUriStr);
		try {
			URI namespaceURI = new URI(namespaceUriStr);
			return namespaceURI;
		} catch (URISyntaxException e) {
			log.error("Failed to construct a namespace URI for namespace " + namespaceUriStr, e);
			throw new InitializationException("Unable to parse extended config namespace URI '" + namespaceUriStr + "'.", e);
		}
	}

	private void checkConfiguration(MpiConfigDocument configuration) {
		if (configuration == null) {
			log.error("The configuration of the system has not been initialized.");
			throw new RuntimeException("The configuration of the system has not been properly initialized.");
		}
	}
	
	private void validateConfiguration(MpiConfigDocument configuration) {

		// Set up the validation error listener.
		ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		// During validation, errors are added to the ArrayList for
		// retrieval and printing by the printErrors method.
		boolean isValid = configuration.validate(validationOptions);

		// Print the errors if the XML is invalid.
		if (!isValid)
		{
		    java.util.Iterator<XmlError> iter = validationErrors.iterator();
		    StringBuffer sb = new StringBuffer("MPI Configuration validation errors:\n");
		    while (iter.hasNext())
		    {
		    	sb.append(">> ").append(iter.next()).append("\n");
		    }
		}			
	}

	private MpiConfigDocument loadConfigurationFromSource() throws XmlException, IOException {
		File file = getDefaultConfigurationFile();
		log.debug("Checking for presence of the configuration in file: " + file.getAbsolutePath());
		if (file.exists() && file.isFile()) {
			log.info("Loading configuration from file: " + file.getAbsolutePath());
			return MpiConfigDocument.Factory.parse(file);
		}

		URL fileUrl = Configuration.class.getResource(configFile);
		if (fileUrl != null) {
			log.info("Loading configuration from URL: " + fileUrl);
			return MpiConfigDocument.Factory.parse(fileUrl);
		}
		
		log.error("Unable to load configuration information.");
		throw new RuntimeException("Unable to load configuration information.");
	}

	public void saveConfiguration() {
		File file = getDefaultConfigurationFile();
		log.debug("Storing current configuration in file: " + file.getAbsolutePath());
		try {
			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			configuration.save(file, opts);
		} catch (IOException e) {
			log.error("Unable to save the updated configuration in file: " + file.getAbsolutePath());
			throw new RuntimeException("Unable to save the updated configuration: " + e.getMessage());
		}
	}

	private File getDefaultConfigurationFile() {
		File dir = new File(Context.getOpenEmpiHome() + "/conf");
		log.info("OPENEMPI_HOME property value: " + System.getProperty("OPENEMPI_HOME"));
		File file = new File(dir, configFile);
		return file;
	}
	
	public Object lookupConfigurationEntry(String key) {
		log.debug("Looking up configuration entry with key " + key);
		return configurationRegistry.get(key);
	}

	public void registerConfigurationEntry(String key, Object entry) {
		log.debug("Registering configuration entry " + entry + " with key " + key);
		configurationRegistry.put(key, entry);
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public PrivacyPreservingBlockingSettings getPrivacyPreservingBlockingSettings() {
		return ppbSettings;
	}

	public void setPrivacyPreservingBlockingSettings(PrivacyPreservingBlockingSettings ppbSettings) {
		this.ppbSettings = ppbSettings;
	}

	public AdminConfiguration getAdminConfiguration() {
		return adminConfiguration;
	}

	public void setAdminConfiguration(AdminConfiguration adminConfiguration) {
		this.adminConfiguration = adminConfiguration;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.toString();
	}
}
