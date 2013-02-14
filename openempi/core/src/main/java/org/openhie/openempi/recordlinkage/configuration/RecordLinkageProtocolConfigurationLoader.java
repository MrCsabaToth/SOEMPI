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
package org.openhie.openempi.recordlinkage.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.xml.recordlinkageprotocol.RecordLinkageProtocolConfig;
import org.openhie.openempi.configuration.xml.recordlinkageprotocol.RecordLinkageProtocolType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.util.ValidationUtil;

/**
 * @author ctoth 
 * @version $Revision: $ $Date:  $
 */
public class RecordLinkageProtocolConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(Configuration.class);

	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the file loader configuration service
		//
		if (!(configurationFragment instanceof RecordLinkageProtocolType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the file loader configuration service to use when needed.
		//
		PrivacySettings privacySettings = new PrivacySettings();
		registry.registerConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS, 
				privacySettings);
		
		RecordLinkageProtocolType recordLinkageProtocolType = (RecordLinkageProtocolType) configurationFragment;
		log.debug("Received xml fragment to parse: " + recordLinkageProtocolType);
		RecordLinkageProtocolConfig recordLinkageProtocolConfig = recordLinkageProtocolType.getRecordLinkageProtocolConfig();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.PrivacySettings privacySett = recordLinkageProtocolConfig.getPrivacySettings();

		BloomfilterSettings bloomfilterSettings = new BloomfilterSettings();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.BloomfilterSettings bloomfilterSettingsConfig =
				privacySett.getBloomfilterSettings();
		bloomfilterSettings.setNGramSize(bloomfilterSettingsConfig.getNGramSize());
		if (bloomfilterSettingsConfig.getNGramSize() < 1)
			throw new IndexOutOfBoundsException("Too NGram size doesn't make any sense.");
		if (bloomfilterSettingsConfig.getDefaultM() < 5)
			throw new IndexOutOfBoundsException("Too small default bit vector length doesn't make any sense.");
		bloomfilterSettings.setDefaultM(bloomfilterSettingsConfig.getDefaultM());
		if (bloomfilterSettingsConfig.getDefaultK() < 3)
			throw new IndexOutOfBoundsException("Too small default hash round count doesn't make any sense.");
		bloomfilterSettings.setDefaultK(bloomfilterSettingsConfig.getDefaultK());
		privacySettings.setBloomfilterSettings(bloomfilterSettings);

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.ComponentSettings componentSettingsXml =
				privacySett.getComponentSettings();

		KeyServerSettings keyServerSettings = new KeyServerSettings();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.KeyserverSettings keyServerSettingsConfig =
			componentSettingsXml.getKeyserverSettings();
		ValidationUtil.sanityCheckServerAddress(keyServerSettingsConfig.getServerAddress());
		keyServerSettings.setServerAddress(keyServerSettingsConfig.getServerAddress());
		if (keyServerSettingsConfig.getNumberOfSalts() < 1)
			throw new IndexOutOfBoundsException("Too low salt count.");
		keyServerSettings.setNumberOfSalts(keyServerSettingsConfig.getNumberOfSalts());
		if (keyServerSettingsConfig.getSaltIdStart() < 0)
			throw new IndexOutOfBoundsException("Too small salt Id start.");
		keyServerSettings.setSaltIdStart(keyServerSettingsConfig.getSaltIdStart());
		if (keyServerSettingsConfig.getSaltIdStride() < 1)
			throw new IndexOutOfBoundsException("Too small salt Id stride.");
		keyServerSettings.setSaltIdStride(keyServerSettingsConfig.getSaltIdStride());

		ParameterManagerSettings parameterManagerSettings = new ParameterManagerSettings();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.ParameterManagerSettings parameterManagerSettingsConfig =
			componentSettingsXml.getParameterManagerSettings();
		ValidationUtil.sanityCheckServerAddress(parameterManagerSettingsConfig.getServerAddress());
		parameterManagerSettings.setServerAddress(parameterManagerSettingsConfig.getServerAddress());

		DataIntegratorSettings dataIntegratorSettings = new DataIntegratorSettings();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.DataIntegratorSettings dataIntegratorSettingsConfig =
			componentSettingsXml.getDataIntegratorSettings();
		ValidationUtil.sanityCheckServerAddress(dataIntegratorSettingsConfig.getServerAddress());
		dataIntegratorSettings.setServerAddress(dataIntegratorSettingsConfig.getServerAddress());

		ComponentSettings componentSettings = new ComponentSettings();
		componentSettings.setKeyServerSettings(keyServerSettings);
		componentSettings.setParameterManagerSettings(parameterManagerSettings);
		componentSettings.setDataIntegratorSettings(dataIntegratorSettings);
		privacySettings.setComponentSettings(componentSettings);
	}

	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationData)
			throws InitializationException {
		PrivacySettings privacySettings = (PrivacySettings) configurationData;
		RecordLinkageProtocolType xmlConfigurationFragment = buildPrivacySettingsFileFragment(privacySettings);
		log.debug("Saving record linkage protocol configuration info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveRecordLinkageProtoclConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated record linkage protocol configuration in configuration registry: " + privacySettings);
		registry.registerConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS, privacySettings);
	}

	private RecordLinkageProtocolType buildPrivacySettingsFileFragment(PrivacySettings privacySettings) {
		RecordLinkageProtocolType recordLinkageProtocolType = RecordLinkageProtocolType.Factory.newInstance();				
		RecordLinkageProtocolConfig recordLinkageProtocolConfig = recordLinkageProtocolType.addNewRecordLinkageProtocolConfig();
		org.openhie.openempi.configuration.xml.recordlinkageprotocol.PrivacySettings privacySett = recordLinkageProtocolConfig.addNewPrivacySettings();

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.ComponentSettings componentSettingsNode =
				privacySett.addNewComponentSettings();

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.KeyserverSettings keyserverSettingsNode =
			componentSettingsNode.addNewKeyserverSettings();
		KeyServerSettings kss = privacySettings.getComponentSettings().getKeyServerSettings();
		ValidationUtil.sanityCheckServerAddress(kss.getServerAddress());
		keyserverSettingsNode.setServerAddress(kss.getServerAddress());
		if (kss.getNumberOfSalts() < 1)
			throw new IndexOutOfBoundsException("Too low salt count.");
		keyserverSettingsNode.setNumberOfSalts(kss.getNumberOfSalts());
		if (kss.getSaltIdStart() < 0)
			throw new IndexOutOfBoundsException("Too small salt Id start.");
		if (kss.getSaltIdStride() < 1)
			throw new IndexOutOfBoundsException("Too small salt Id stride.");
		keyserverSettingsNode.setSaltIdStart(kss.getSaltIdStart());
		keyserverSettingsNode.setSaltIdStride(kss.getSaltIdStride());

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.ParameterManagerSettings parameterManagerSettingsNode =
			componentSettingsNode.addNewParameterManagerSettings();
		ParameterManagerSettings pms = privacySettings.getComponentSettings().getParameterManagerSettings();
		ValidationUtil.sanityCheckServerAddress(pms.getServerAddress());
		parameterManagerSettingsNode.setServerAddress(pms.getServerAddress());

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.DataIntegratorSettings dataIntegratorSettingsNode =
			componentSettingsNode.addNewDataIntegratorSettings();
		DataIntegratorSettings dis = privacySettings.getComponentSettings().getDataIntegratorSettings();
		ValidationUtil.sanityCheckServerAddress(dis.getServerAddress());
		dataIntegratorSettingsNode.setServerAddress(dis.getServerAddress());

		org.openhie.openempi.configuration.xml.recordlinkageprotocol.BloomfilterSettings bloomfilterSettingsNode =
				privacySett.addNewBloomfilterSettings();
		BloomfilterSettings bs = privacySettings.getBloomfilterSettings();
		if (bs.getNGramSize() < 1)
			throw new IndexOutOfBoundsException("Too NGram size doesn't make any sense.");
		bloomfilterSettingsNode.setNGramSize(bs.getNGramSize());
		if (bs.getDefaultM() < 5)
			throw new IndexOutOfBoundsException("Too small default bit vector length doesn't make any sense.");
		bloomfilterSettingsNode.setDefaultM(bs.getDefaultM());
		if (bs.getDefaultK() < 3)
			throw new IndexOutOfBoundsException("Too small default hash round count doesn't make any sense.");
		bloomfilterSettingsNode.setDefaultK(bs.getDefaultK());
		return recordLinkageProtocolType;
	}

}
