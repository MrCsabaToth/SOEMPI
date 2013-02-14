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
package org.openempi.webapp.server;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.BlockingDataService;
import org.openempi.webapp.client.model.BlockingFieldBaseWeb;
import org.openempi.webapp.client.model.BlockingSettingsWeb;
import org.openhie.openempi.blocking.basicblocking.BasicBlockingConstants;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.blocking.basicblocking.BlockingSettings;
import org.openhie.openempi.configuration.Component;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.Component.ComponentType;
import org.openhie.openempi.context.Context;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BlockingDataServiceImpl extends RemoteServiceServlet implements BlockingDataService
{
	private static final long serialVersionUID = -5952930050947009799L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public BlockingSettingsWeb loadBlockingConfigurationData() {
		log.debug("Received request to load the blocking configuration data.");
		try {
			Configuration configuration = Context.getConfiguration();
			BlockingSettings settings = (BlockingSettings)
					configuration.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_SETTINGS_REGISTRY_KEY);
			return convertToClientModel(settings);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private BlockingSettingsWeb convertToClientModel(BlockingSettings blockingSettings) {
		BlockingSettingsWeb blockingSettingsWeb = new BlockingSettingsWeb();
		blockingSettingsWeb.setNumberOfRecordsToSample(blockingSettings.getNumberOfRecordsToSample());
		List<BlockingRound> rounds = blockingSettings.getBlockingRounds();
		List<BlockingFieldBaseWeb> fields = new java.util.ArrayList<BlockingFieldBaseWeb>(rounds.size());
		int blockingRoundIndex = 1;
		for (BlockingRound blockingRound : rounds) {
			int blockingFieldIndex = 1;
			for (org.openhie.openempi.configuration.BaseFieldPair baseFieldPair : blockingRound.getFields()) {
				BlockingFieldBaseWeb clientField =
					new BlockingFieldBaseWeb(blockingRoundIndex, blockingFieldIndex,
							baseFieldPair.getLeftFieldName(), baseFieldPair.getRightFieldName());
				fields.add(clientField);
				blockingFieldIndex++;
			}
			blockingRoundIndex++;
		}
		blockingSettingsWeb.setBlockingFields(fields);
		return blockingSettingsWeb;
	}

	public String saveBlockingConfigurationData(BlockingSettingsWeb blockingConfiguration) {
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.BLOCKING);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			BlockingSettings blockingSettings = convertFromClientModel(blockingConfiguration);
			loader.saveAndRegisterComponentConfiguration(configuration, blockingSettings);
		} catch (Exception e) {
			log.warn("Failed while saving the blocking configuration: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private BlockingSettings convertFromClientModel(BlockingSettingsWeb blockingConfiguration) {
		BlockingSettings blockingSettings = new BlockingSettings();
		blockingSettings.setNumberOfRecordsToSample(blockingConfiguration.getNumberOfRecordsToSample());
		int roundsCount = 0;
		for (BlockingFieldBaseWeb baseField : blockingConfiguration.getBlockingFields()) {
			if (baseField.getBlockingRound() > roundsCount) {
				roundsCount = baseField.getBlockingRound();
			}
		}
		List<BlockingRound> rounds = new java.util.ArrayList<BlockingRound>(roundsCount);
		for (int currRound=1; currRound <= roundsCount; currRound++) {
			BlockingRound round = new BlockingRound();
			for (BlockingFieldBaseWeb baseField : blockingConfiguration.getBlockingFields()) {
				if (baseField.getBlockingRound() == currRound) {
					round.addField(baseField.getLeftFieldName(), baseField.getRightFieldName());
				}
			}
			rounds.add(round);
		}
		blockingSettings.setBlockingRounds(rounds);
		return blockingSettings;
	}

}
