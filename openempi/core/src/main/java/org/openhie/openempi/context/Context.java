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
package org.openhie.openempi.context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.BlockingServiceSelector;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.loader.DataLoaderServiceSelector;
import org.openhie.openempi.loader.configuration.DataLoaderConfigurationService;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.matching.MatchingServiceSelector;
import org.openhie.openempi.model.User;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector;
import org.openhie.openempi.recordlinkage.configuration.RecordLinkageProtocolConfigurationService;
import org.openhie.openempi.service.AuditEventService;
import org.openhie.openempi.service.FieldService;
import org.openhie.openempi.service.KeyManagerService;
import org.openhie.openempi.service.KeyServerService;
import org.openhie.openempi.service.KeyServiceLocator;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.RemotePersonService;
import org.openhie.openempi.service.RemotePersonServiceLocator;
import org.openhie.openempi.service.SaltManagerService;
import org.openhie.openempi.service.UserManager;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.util.PropertiesUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context implements ApplicationContextAware
{
	protected static final Log log = LogFactory.getLog(Context.class);

	private static final ThreadLocal<Object[] /* UserContext */> userContextHolder = new ThreadLocal<Object[] /* UserContext */>();

	private static ApplicationContext applicationContext;
	private static UserManager userManager;
	private static PersonManagerService personManagerService;
	private static PersonQueryService personQueryService;
	private static ValidationService validationService;
	private static Configuration configuration;
	private static MatchingService matchingService;
	private static BlockingService blockingService;
	private static RecordLinkageProtocolConfigurationService recordLinkageProtocolConfigurationService;
	private static MatchingServiceSelector matchingServiceSelector;
	private static BlockingServiceSelector blockingServiceSelector;
	private static RecordLinkageProtocolSelector recordLinkageProtocolSelector;
	private static AuditEventService auditEventService;
	private static StringComparisonService stringComparisonService;
	private static TransformationService transformationService;
	private static KeyServiceLocator keyServiceLocator;
	private static KeyServerService keyServerService;
	private static RemotePersonServiceLocator remotePersonServiceLocator;
	private static RemotePersonService remotePersonService;
	private static DataLoaderConfigurationService dataLoaderConfigurationService;
	private static DataLoaderServiceSelector dataLoaderServiceSelector;
	private static KeyManagerService keyManagerService;
	private static SaltManagerService saltManagerService;
	private static FieldService fieldService;

	public static void startup() {
		List<String> configFiles = getConfigLocations();
		try {
			applicationContext = new ClassPathXmlApplicationContext((String[]) configFiles.toArray(new String[]{}));
			applicationContext.getBean("context");
		} catch (Throwable t) {
			log.error("Failed while setting up the context for OpenEMPI: " + t, t);
		}
	}

	public static List<String> getConfigLocations() {
		List<String> configFilesList = generateConfigFileList();

		addExtensionContextsFromPropertiesFile(configFilesList, null);
		addExtensionContextsFromSystemProperty(configFilesList, null);
		return configFilesList;
	}

	public static String getExtensionContextsString() {
		Set<String> extensionContextsSet = new HashSet<String>();

		addExtensionContextsFromPropertiesFile(null, extensionContextsSet);
		addExtensionContextsFromSystemProperty(null, extensionContextsSet);
		StringBuilder configFilesString = new StringBuilder();
		Iterator<String> it = extensionContextsSet.iterator();
		while(it.hasNext()) {
			if (configFilesString.length() > 0)
				configFilesString.append(',');
			configFilesString.append(it.next());
		}
		return configFilesString.toString();
	}

	public static String getOpenEmpiHome() {
		String openEmpiHome = Constants.OPENEMPI_HOME_ENV_VALUE;
		if (openEmpiHome == null || openEmpiHome.length() == 0) {
			openEmpiHome = Constants.OPENEMPI_HOME_VALUE;
		} else {
			System.setProperty(Constants.OPENEMPI_HOME, openEmpiHome);
		}
		log.debug("OPENEMPI_HOME is set to " + openEmpiHome);
		return openEmpiHome;
	}

	private static List<String> generateConfigFileList() {
		List<String> configFiles = new ArrayList<String>();
		String openEmpiHome = getOpenEmpiHome();
		if (openEmpiHome != null && openEmpiHome.length() > 0) {
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-resources.xml");
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-dao.xml");
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-service.xml");
		} else {
			configFiles.add("classpath:/applicationContext-resources.xml");
			configFiles.add("classpath:/applicationContext-dao.xml");
			configFiles.add("classpath:/applicationContext-service.xml");
		}
		return configFiles;
	}

	private static void addExtensionContextsFromSystemProperty(List<String> configFilesList, Set<String> configFilesSet) {
		String extensionContextsString = System.getProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS);
		addExtensionContextsFromCommaSeparatedList(configFilesList, configFilesSet, extensionContextsString);
	}

	private static void addExtensionContextsFromPropertiesFile(List<String> configFilesList, Set<String> configFilesSet) {
		Properties props;
		try {
			String filename = getOpenEmpiHome() + "/conf/" + Constants.OPENEMPI_EXTENSION_CONTEXTS_PROPERTY_FILENAME;
			log.debug("Attempting to load extension contexts from " + filename);
			props = PropertiesUtils.load(new File(filename));
		} catch (Exception e) {
			log.warn("Unable to load the extension contexts properties file; will resort to System property. Error: " + e, e);
			return;
		}
		String extensionContextsString = props.getProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS);
		addExtensionContextsFromCommaSeparatedList(configFilesList, configFilesSet, extensionContextsString);
	}

	private static void addExtensionContextsFromCommaSeparatedList(List<String> configFilesList,
			Set<String> configFilesSet, String extensionContextsString) {
		if (extensionContextsString != null && extensionContextsString.length() > 0) {
			String[] extContextsArray = extensionContextsString.split(",");
			for (String extContext : extContextsArray) {
				log.debug("Adding extension application context from location: " + extContext);
				if (configFilesList != null)
					configFilesList.add(extContext);
				if (configFilesSet != null)
					configFilesSet.add(extContext);
			}
		}
	}
	
	public static String authenticate(String username, String password)
			throws AuthenticationException {
		return getUserContext().authenticate(username, password);
	}
	
	public static User authenticate(String sessionKey)
			throws AuthenticationException {
		return getUserContext().authenticate(sessionKey);
	}

	public static UserContext getUserContext() {
		UserContext userContext = null;
		Object[] arr = userContextHolder.get();
		if (arr == null) {
			log.trace("userContext is null. Creating new userContext");
			userContext = new UserContext();
			userContext.setUserManager(userManager);
			setUserContext(userContext);
		}
		return (UserContext) userContextHolder.get()[0];
	}

	public static void setUserContext(UserContext ctx) {
		log.trace("Setting user context " + ctx);
		Object[] arr = new Object[] { ctx };
		userContextHolder.set(arr);
	}

	public static PersonManagerService getPersonManagerService() {
		return personManagerService;
	}

	public void setPersonManagerService(PersonManagerService personManagerService) {
		Context.personManagerService = personManagerService;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		Context.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static UserManager getUserManager() {
		return Context.userManager;
	}
	
	public void setUserManager(UserManager userManager) {
	    Context.userManager = userManager;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		Context.configuration = configuration;
	}

	public static ValidationService getValidationService() {
		return validationService;
	}

	public void setValidationService(ValidationService validationService) {
		Context.validationService = validationService;
	}

	public static void registerCustomMatchingService(MatchingService matchingService) {
		Context.matchingService = matchingService;
	}
	
	public static MatchingService getMatchingService() {
		return matchingService;
	}

	public void setMatchingService(MatchingService matchingService) {
		Context.matchingService = matchingService;
	}

	public static void registerCustomBlockingService(BlockingService blockingService) {
		Context.blockingService = blockingService;
	}
	
	public static BlockingService getBlockingService() {
		return blockingService;
	}

	public void setBlockingService(BlockingService blockingService) {
		Context.blockingService = blockingService;
	}

	public static MatchingServiceSelector getMatchingServiceSelector() {
		return matchingServiceSelector;
	}

	public void setMatchingServiceSelector(MatchingServiceSelector matchingServiceSelector) {
		Context.matchingServiceSelector = matchingServiceSelector;
	}

	public static void registerCustomRecordLinkageProtocolConfigurationService(RecordLinkageProtocolConfigurationService recordLinkageProtocolConfigurationService) {
		Context.recordLinkageProtocolConfigurationService = recordLinkageProtocolConfigurationService;
	}
	
	public static RecordLinkageProtocolConfigurationService getRecordLinkageProtocolConfigurationService() {
		return recordLinkageProtocolConfigurationService;
	}

	public void setRecordLinkageProtocolConfigurationService(RecordLinkageProtocolConfigurationService recordLinkageProtocolConfigurationService) {
		Context.recordLinkageProtocolConfigurationService = recordLinkageProtocolConfigurationService;
	}

	public static BlockingServiceSelector getBlockingServiceSelector() {
		return blockingServiceSelector;
	}

	public void setBlockingServiceSelector(BlockingServiceSelector blockingServiceSelector) {
		Context.blockingServiceSelector = blockingServiceSelector;
	}

	public static RecordLinkageProtocolSelector getRecordLinkageProtocolSelector() {
		return recordLinkageProtocolSelector;
	}

	public void setRecordLinkageProtocolSelector(RecordLinkageProtocolSelector recordLinkageProtocolSelector) {
		Context.recordLinkageProtocolSelector = recordLinkageProtocolSelector;
	}

	public static PersonQueryService getPersonQueryService() {
		return personQueryService;
	}

	public void setPersonQueryService(PersonQueryService personQueryService) {
		Context.personQueryService = personQueryService;
	}

	public static StringComparisonService getStringComparisonService() {
		return stringComparisonService;
	}
	
	public void setStringComparisonService(StringComparisonService stringComparisonService) {
		Context.stringComparisonService = stringComparisonService;
	}

	public static TransformationService getTransformationService() {
		return transformationService;
	}
	
	public void setTransformationService(TransformationService transformationService) {
		Context.transformationService = transformationService;
	}

	public static AuditEventService getAuditEventService() {
		return auditEventService;
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		Context.auditEventService = auditEventService;
	}

	public static KeyServiceLocator getKeyServiceLocator() {
		return keyServiceLocator;
	}
	
	public void setKeyServiceLocator(KeyServiceLocator keyServiceLocator) {
		Context.keyServiceLocator = keyServiceLocator;
	}

	public static KeyServerService getKeyServerService() {
		return keyServerService;
	}
	
	public void setKeyServerService(KeyServerService keyServerService) {
		Context.keyServerService = keyServerService;
	}

	public static RemotePersonServiceLocator getRemotePersonServiceLocator() {
		return remotePersonServiceLocator;
	}
	
	public void setRemotePersonServiceLocator(RemotePersonServiceLocator remotePersonServiceLocator) {
		Context.remotePersonServiceLocator = remotePersonServiceLocator;
	}

	public static RemotePersonService getRemotePersonService() {
		return remotePersonService;
	}
	
	public void setRemotePersonService(RemotePersonService remotePersonService) {
		Context.remotePersonService = remotePersonService;
	}

	public static void registerCustomDataLoaderConfigurationService(DataLoaderConfigurationService dataLoaderConfigurationService) {
		Context.dataLoaderConfigurationService = dataLoaderConfigurationService;
	}
	
	public static DataLoaderConfigurationService getDataLoaderConfigurationService() {
		return dataLoaderConfigurationService;
	}

	public void setDataLoaderConfigurationService(DataLoaderConfigurationService dataLoaderConfigurationService) {
		Context.dataLoaderConfigurationService = dataLoaderConfigurationService;
	}

	public static DataLoaderServiceSelector getDataLoaderServiceSelector() {
		return dataLoaderServiceSelector;
	}

	public void setDataLoaderServiceSelector(DataLoaderServiceSelector dataLoaderServiceSelector) {
		Context.dataLoaderServiceSelector = dataLoaderServiceSelector;
	}

	public static KeyManagerService getKeyManagerService() {
		return keyManagerService;
	}

	public void setKeyManagerService(KeyManagerService keyManagerService) {
		Context.keyManagerService = keyManagerService;
	}

	public static SaltManagerService getSaltManagerService() {
		return saltManagerService;
	}

	public void setSaltManagerService(SaltManagerService saltManagerService) {
		Context.saltManagerService = saltManagerService;
	}

	public static FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		Context.fieldService = fieldService;
	}

}
