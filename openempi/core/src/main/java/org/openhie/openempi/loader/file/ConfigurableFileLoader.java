/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.loader.configuration.FieldCompositionComparator;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.loader.configuration.LoaderFieldComposition;
import org.openhie.openempi.loader.configuration.LoaderSubField;
import org.openhie.openempi.loader.configuration.LoaderTargetField;
import org.openhie.openempi.loader.file.AbstractFileLoader;
import org.openhie.openempi.model.Person;

public class ConfigurableFileLoader extends AbstractFileLoader
{
	public static final String LOADER_ALIAS = "configurableFileLoader";
	protected LoaderConfig loaderConfiguration;

	public void loadFile(String fileName, String tableName, LoaderConfig loaderConfiguration, boolean applyFieldTransformations) {
		this.loaderConfiguration = loaderConfiguration;
		super.loadFile(fileName, tableName, loaderConfiguration, applyFieldTransformations);
	}

	protected Person processLine(String line, int lineIndex) {
		if (lineIndex == 0) {
			// Skip the first line if it is a header.
			if (loaderConfiguration.getHeaderLinePresent()) {
				return null;
			}
		}
		log.debug("Needs to parse the line " + line);
		try {
			Person person = new Person();
			if (getPerson(line, person)) {
				log.debug("Person is:\n" + person);
			} else {
				log.error("Getting person form line: " + line);
				return null;
			}
			return person;
		} catch (ParseException e) {
			log.warn("Failed to parse file line: " + line + " due to " + e);
			return null;
		}
	}

	/**
	 * Column indexes and field names are configured in the mpi-config.xml
	 * @return 
	 */
	private boolean getPerson(String line, Person person) throws ParseException {
		Map<String,List<LoaderFieldComposition>> fieldCompositions = new HashMap<String,List<LoaderFieldComposition>>();
		String[] fields = line.split(loaderConfiguration.getDelimiterRegex());
		List<LoaderDataField> loaderDataFields = loaderConfiguration.getDataFields();
		for (LoaderDataField loaderDataField : loaderDataFields) {
			int sourceColumnIndex = loaderDataField.getSourceColumnIndex();
			if (sourceColumnIndex < 0) {
				log.error("Source column index (" + sourceColumnIndex + ") is smaller than 0");
				return false;
			}
			if (sourceColumnIndex >= fields.length) {
				log.error("Source column index (" + sourceColumnIndex + ") is bigger than the number of fields in the source file: " + fields.length);
				return false;
			}
			if (!processLoaderDataField(loaderDataField, fields[sourceColumnIndex], person, fieldCompositions))
				return false;
		}
		return processFieldCompositions(fieldCompositions, person);
	}

	private boolean processFieldCompositions(Map<String,List<LoaderFieldComposition>> fieldCompositions,
			Person person)
	{
		if (fieldCompositions.size() > 0) {
			for (Map.Entry<String, List<LoaderFieldComposition>> entry : fieldCompositions.entrySet()) {
				List<LoaderFieldComposition> value = entry.getValue();
				Collections.sort(value, new FieldCompositionComparator());
				StringBuilder stringValueBuilder = new StringBuilder("");
				LoaderTargetField loaderTargetField = new LoaderTargetField();
				String key = entry.getKey();
				loaderTargetField.setFieldName(key);
				for (LoaderFieldComposition fieldComp : value) {
					stringValueBuilder.append(fieldComp.getValue());
					if (fieldComp.getSeparator() != null)
						stringValueBuilder.append(fieldComp.getSeparator());
					if (fieldComp.getFieldTransformation() != null)
						loaderTargetField.setFieldTransformation((FunctionField)fieldComp.getFieldTransformation().clone());
				}
				if (!processField(loaderTargetField, stringValueBuilder.toString(), person))
					return false;
			}
		}
		return true;
	}

	private boolean processLoaderDataField(LoaderDataField loaderDataField, String value,
			Person person, Map<String,List<LoaderFieldComposition>> fieldCompositions)
	{
		if (isFieldNullOrEmpty(value))
			return true;
		String fieldName = loaderDataField.getFieldName();
		if (loaderDataField.getSubFields() != null) {
			List<LoaderSubField> subFields = loaderDataField.getSubFields();
			for (LoaderSubField subField : subFields) {
				int beginIndex = subField.getBeginIndex();
				int endIndex = subField.getEndIndex();
				if (endIndex == -1)
					endIndex = value.length() - 1;
				if (beginIndex < value.length() && beginIndex < endIndex) {
					endIndex = Math.min(endIndex, value.length() - 1);
					if (!processField(subField, value.substring(beginIndex, endIndex), person))
						return false;
				}
			}
		} else if (loaderDataField.getFieldComposition() != null) {
			LoaderFieldComposition fieldComp = loaderDataField.getFieldComposition();
			fieldComp.setValue(value);
			if (fieldCompositions.containsKey(fieldName)) {
				List<LoaderFieldComposition> fieldComps = fieldCompositions.get(fieldName);
				fieldComps.add(fieldComp);
			} else {
				List<LoaderFieldComposition> fieldComps = new ArrayList<LoaderFieldComposition>();
				fieldComps.add(fieldComp);
				fieldCompositions.put(fieldName, fieldComps);
			}
		} else if (fieldName.length() > 0) {
			if (!processField(loaderDataField, value, person))
				return false;
		}
		return true;
	}

	private boolean isFieldNullOrEmpty(String field) {
		return (field == null || field.length() <= 0);
	}
	
	private boolean processField(LoaderTargetField loaderTargetField, Object value,
			Person person)
	{
		String fieldName = loaderTargetField.getFieldName();
		if (fieldName.length() == 0)
			return false;

		person.setAttribute(fieldName, value);
		return true;
	}

}
