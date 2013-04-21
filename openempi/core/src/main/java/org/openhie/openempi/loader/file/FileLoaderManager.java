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
package org.openhie.openempi.loader.file;

import org.apache.log4j.Logger;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.DataLoaderService;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.util.BaseSpringApp;

public class FileLoaderManager extends BaseSpringApp
{
	protected static Logger log = Logger.getLogger(FileLoaderManager.class);
	
	public FileLoaderManager() {
	}
	
	private void loadFile(String loaderAlias, String filename, String tableName, LoaderConfig loaderConfiguration, boolean applyFieldTransformations) {
		DataLoaderService service = Context.getDataLoaderServiceSelector().getDataLoaderServiceType(loaderAlias).getDataServiceService();
		service.loadFile(filename, tableName, loaderConfiguration, applyFieldTransformations);
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
			System.exit(-1);
		}
		String filename = args[0];
		String loaderAlias = args[1];
		log.info("Loading the data file " + filename + " using loader " + loaderAlias);
		LoaderConfig loaderConfiguration =
			(LoaderConfig)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
		FileLoaderManager fileLoaderManager = new FileLoaderManager();
		fileLoaderManager.loadFile(loaderAlias, filename, "loadertest", loaderConfiguration, true);
	}
	
	public static void usage() {
		System.out.println("Usage: " + FileLoaderManager.class.getName() + " <file-to-load> <loader-alias>");
	}
}
