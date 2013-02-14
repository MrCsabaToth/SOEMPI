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
package org.openhie.openempi.loader.table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.loader.AbstractLoaderBase;
import org.openhie.openempi.loader.DataLoaderService;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;

public abstract class AbstractTableLoader extends AbstractLoaderBase implements DataLoaderService
{
	enum DBServerType {
		Derby,
		H2,
		HSQLDB,
		Oracle,
		PostgreSQL,
		MySQL,
		SQLServer
	}

	public void loadFile(String filename, String tableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields)
	{
		File file = new File(filename);
		log.debug("Loading file " + file.getAbsolutePath());
		if (!file.isFile() || !file.canRead()) {
			log.error("Input file is not available.");
			throw new RuntimeException("Input file " + filename + " is not readable.");
		}
		parseFile(file, tableName, loaderConfiguration, populateCustomFields);
		// This is a table loader, cannot parse a file
	}

	public void loadTable(String hostAddress, String dbUserName, String dbPassword, String dbName,
			String sourceTableName, String targetTableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields)
	{
		// Open up connection, shovel data
	}

	public void parseFile(File file, String tableName, LoaderConfig loaderConfiguration,
			boolean populateCustomFields) {
		List<ColumnInformation> columnInformationConverted = convertLoaderConfigurationToColumnInformation(loaderConfiguration);
		Dataset dataset = personManagerService.createDatasetTable(tableName, columnInformationConverted, 0, false, false);
		dataset = personManagerService.getDatasetByTableName(tableName);
		columnInformation = dataset.getColumnInformation();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error("Unable to read the input file. Error: " + e);
			throw new RuntimeException("Unable to read the input file.");
		}
		
		long loadedLines = 0;
		try {
			boolean done = false;
			int lineIndex = 0;
			int pageSize = Constants.PAGE_SIZE;
			List<Person> persons = new ArrayList<Person>();
			while (!done) {
				String line = reader.readLine();
				if (line == null) {
					done = true;
					continue;
				}
				Person person = processLine(line, lineIndex++);
				if (person != null) {
					loadedLines++;
					persons.add(person);
					contributePersonToStatistics(person);
					if (persons.size() >= pageSize) {
						loadPersons(tableName, persons, populateCustomFields);
						persons.clear();
					}
				}
			}
			// If the number of records is not divisible by the page size, there will be some
			// remaining list of persons
			if (persons.size() > 0)
				loadPersons(tableName, persons, populateCustomFields);
		} catch (IOException e) {
			log.error("Failed while loading the input file. Error: " + e);
			throw new RuntimeException("Failed while loading the input file.");
		}
		for (ColumnInformation ci : columnInformation) {
			Double averageLen = 0.0;
			if (loadedLines != ci.getNumberOfMissing()) // Otherwise it would be division by zero
				averageLen = ci.getAverageFieldLength() / (loadedLines - ci.getNumberOfMissing());
			ci.setAverageFieldLength(averageLen);
		}
		dataset.setTotalRecords(loadedLines);
		personManagerService.updateDataset(dataset);
		personManagerService.addIndexesAndConstraintsToDatasetTable(tableName);
	}

	protected abstract Person processLine(String line, int lineIndex);
	
}
