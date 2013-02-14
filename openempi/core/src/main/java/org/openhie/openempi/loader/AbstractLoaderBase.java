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
package org.openhie.openempi.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.recordlinkage.configuration.BloomfilterSettings;
import org.openhie.openempi.recordlinkage.configuration.PrivacySettings;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.impl.BaseServiceImpl;

public abstract class AbstractLoaderBase extends BaseServiceImpl implements DataLoaderService
{
	protected PersonManagerService personManagerService;
	protected List<ColumnInformation> columnInformation;
	
	public boolean loadPerson(String tableName, Person person, boolean populateCustomFields) {
		log.debug("Attempting to load person entry " + person);
		contributePersonToStatistics(person);
		try {
			personManagerService.addPerson(tableName, person, populateCustomFields, false);
		} catch (Exception e) {
			log.error("Failed while adding person entry to the system. Error: " + e, e);
			if (e.getCause() instanceof org.hibernate.exception.SQLGrammarException) {
				org.hibernate.exception.SQLGrammarException sge = (org.hibernate.exception.SQLGrammarException) e;
				log.error("Cause is: " + sge.getSQL());
			}
//			throw new RuntimeException("Failed while adding person entry to the system.");
			return false;
		}
		return true;
	}

	protected void contributePersonToStatistics(Person person) {
		Map<String, Object> attributes = person.getAttributes();
		for (ColumnInformation ci : columnInformation) {
			boolean missing = false;
			if (attributes.containsKey(ci.getFieldName())) {
				Object o = attributes.get(ci.getFieldName());
				if (o == null)
					missing = true;
			} else {
				missing = true;
			}
			if (missing) {
				ci.setNumberOfMissing(ci.getNumberOfMissing() + 1);
			} else if (ci.getFieldType().getFieldTypeEnum() == FieldType.FieldTypeEnum.String) {
				String strVal = (String)attributes.get(ci.getFieldName());
				ci.setAverageFieldLength(ci.getAverageFieldLength() + strVal.length());
			}
		}
	}

	public boolean loadPersons(String tableName, List<Person> persons, boolean populateCustomFields) {
		log.debug("Attempting to load persons (" + persons.size() + ")");
		try {
			personManagerService.addPersons(tableName, persons, populateCustomFields, false);
		} catch (Exception e) {
			log.error("Failed while adding person entry to the system. Error: " + e, e);
			if (e.getCause() instanceof org.hibernate.exception.SQLGrammarException) {
				org.hibernate.exception.SQLGrammarException sge = (org.hibernate.exception.SQLGrammarException) e;
				log.error("Cause is: " + sge.getSQL());
			}
//			throw new RuntimeException("Failed while adding person entry to the system.");
			return false;
		}
		return true;
	}

	protected List<ColumnInformation> convertLoaderConfigurationToColumnInformation(LoaderConfig loaderConfiguration) {
		List<ColumnInformation> columnInformation = new ArrayList<ColumnInformation>();
		List<LoaderDataField> loaderDataFields = loaderConfiguration.getDataFields();
		for (LoaderDataField loaderDataField : loaderDataFields) {
			ColumnInformation ci = new ColumnInformation();
			ci.setFieldName(loaderDataField.getFieldName());
			ci.setFieldType(loaderDataField.getFieldType().getFieldTypeEnum());
			ci.setFieldTypeModifier(loaderDataField.getFieldTypeModifier());
			ci.setFieldMeaning(loaderDataField.getFieldMeaning().getFieldMeaningEnum());
			FunctionField functionField = loaderDataField.getFieldTransformation();
			if (functionField != null) {
				ci.setFieldTransformation(functionField.getFunctionName());
				if (functionField.getFunctionName().contains("Bloom")) {
					Map<String, Object> parameters = functionField.getFunctionParameters();
					if (parameters != null) {
						if (parameters.containsKey("k"))
							ci.setBloomFilterKParameter((Integer)parameters.get("k"));
						if (parameters.containsKey("m"))
							ci.setBloomFilterMParameter((Integer)parameters.get("m"));
					}
					if (ci.getBloomFilterKParameter() == 0 || ci.getBloomFilterMParameter() == 0) {
						PrivacySettings privacySettings =
								(PrivacySettings)Context.getConfiguration().lookupConfigurationEntry(ConfigurationRegistry.RECORD_LINKAGE_PROTOCOL_SETTINGS);
						BloomfilterSettings bfs = privacySettings.getBloomfilterSettings();
						if (ci.getBloomFilterKParameter() == 0)
							ci.setBloomFilterKParameter(bfs.getDefaultK());
						if (ci.getBloomFilterMParameter() == 0)
							ci.setBloomFilterMParameter(bfs.getDefaultM());
					}
				}
			}
			columnInformation.add(ci);
		}
		return columnInformation;
	}

	public PersonManagerService getPersonManagerService() {
		return personManagerService;
	}

	public void setPersonManagerService(PersonManagerService personManagerService) {
		this.personManagerService = personManagerService;
	}

}
