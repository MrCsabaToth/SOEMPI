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
package org.openhie.openempi.loader.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.configuration.xml.dataloader.Composition;
import org.openhie.openempi.configuration.xml.dataloader.DataField;
import org.openhie.openempi.configuration.xml.dataloader.DataFields;
import org.openhie.openempi.configuration.xml.dataloader.DataLoaderConfig;
import org.openhie.openempi.configuration.xml.dataloader.DataLoaderType;
import org.openhie.openempi.configuration.xml.dataloader.Substring;
import org.openhie.openempi.configuration.xml.dataloader.Substrings;
import org.openhie.openempi.configuration.xml.dataloader.TargetField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.util.ValidationUtil;

/**
 * @author ctoth 
 * @version $Revision: $ $Date:  $
 */
public class DataLoaderConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(Configuration.class);
	
	public void loadAndRegisterTargetField(org.openhie.openempi.configuration.xml.dataloader.TargetField targetField, LoaderTargetField loaderTargetField)
	{
		String fieldName = targetField.getFieldName();
		ValidationUtil.sanityCheckFieldName(fieldName);
		loaderTargetField.setFieldName(fieldName);
		FieldTypeEnum fieldTypeEnum = null;
		switch(targetField.getFieldType().intValue()) {
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_STRING:
			fieldTypeEnum = FieldTypeEnum.String;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_INTEGER:
			fieldTypeEnum = FieldTypeEnum.Integer;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_BIGINT:
			fieldTypeEnum = FieldTypeEnum.BigInt;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_FLOAT:
			fieldTypeEnum = FieldTypeEnum.Float;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_DOUBLE:
			fieldTypeEnum = FieldTypeEnum.Double;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_DATE:
			fieldTypeEnum = FieldTypeEnum.Date;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldType.INT_BLOB:
			fieldTypeEnum = FieldTypeEnum.Blob;		break;
		}
		loaderTargetField.setFieldType(fieldTypeEnum);
		if (targetField.isSetFieldTypeModifier() && targetField.getFieldTypeModifier() != null)
			loaderTargetField.setFieldTypeModifier(targetField.getFieldTypeModifier());
		FieldMeaningEnum fieldMeaningEnum = null;
		switch(targetField.getFieldMeaning().intValue()) {
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ORIGINAL_ID:
			fieldMeaningEnum = FieldMeaningEnum.OriginalId;				break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_GIVEN_NAME:
			fieldMeaningEnum = FieldMeaningEnum.GivenName;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_FAMILY_NAME:
			fieldMeaningEnum = FieldMeaningEnum.FamilyName;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_MIDDLE_NAME:
			fieldMeaningEnum = FieldMeaningEnum.MiddleName;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_NAME_PREFIX:
			fieldMeaningEnum = FieldMeaningEnum.NamePrefix;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_NAME_SUFFIX:
			fieldMeaningEnum = FieldMeaningEnum.NameSuffix;				break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DATE_OF_BIRTH:
			fieldMeaningEnum = FieldMeaningEnum.DateOfBirth;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_BIRTH_WEIGHT:
			fieldMeaningEnum = FieldMeaningEnum.BirthWeight;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_BIRTH_CITY:
			fieldMeaningEnum = FieldMeaningEnum.BirthCity;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_BIRTH_STATE:
			fieldMeaningEnum = FieldMeaningEnum.BirthState;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_BIRTH_COUNTRY:
			fieldMeaningEnum = FieldMeaningEnum.BirthCountry;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_MOTHERS_MAIDEN_NAME:
			fieldMeaningEnum = FieldMeaningEnum.MothersMaidenName;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_MOTHERS_WEIGHT:
			fieldMeaningEnum = FieldMeaningEnum.MothersWeightAtBirth;	break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_SSN:
			fieldMeaningEnum = FieldMeaningEnum.SSN;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_GENDER:
			fieldMeaningEnum = FieldMeaningEnum.Gender;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ETHNIC_GROUP:
			fieldMeaningEnum = FieldMeaningEnum.EthnicGroup;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_RACE:
			fieldMeaningEnum = FieldMeaningEnum.Race;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_NATIONALITY:
			fieldMeaningEnum = FieldMeaningEnum.Nationality;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_LANGUAGE:
			fieldMeaningEnum = FieldMeaningEnum.Language;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_RELIGION:
			fieldMeaningEnum = FieldMeaningEnum.Religion;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_MARITAL_STATUS:
			fieldMeaningEnum = FieldMeaningEnum.MaritalStatus;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DEGREE:
			fieldMeaningEnum = FieldMeaningEnum.Degree;					break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_EMAIL:
			fieldMeaningEnum = FieldMeaningEnum.Email;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_LINE_1:
			fieldMeaningEnum = FieldMeaningEnum.AddressLine1;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_LINE_2:
			fieldMeaningEnum = FieldMeaningEnum.AddressLine2;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CITY:
			fieldMeaningEnum = FieldMeaningEnum.City;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_COUNTY:
			fieldMeaningEnum = FieldMeaningEnum.County;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_STATE:
			fieldMeaningEnum = FieldMeaningEnum.State;					break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_COUNTRY:
			fieldMeaningEnum = FieldMeaningEnum.Country;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_POSTAL_CODE:
			fieldMeaningEnum = FieldMeaningEnum.PostalCode;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_NUMBER:
			fieldMeaningEnum = FieldMeaningEnum.AddressNumber;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_FRACTION:
			fieldMeaningEnum = FieldMeaningEnum.AddressFraction;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_DIRECTION:
			fieldMeaningEnum = FieldMeaningEnum.AddressDirection;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_STREET_NAME:
			fieldMeaningEnum = FieldMeaningEnum.AddressStreetName;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_TYPE:
			fieldMeaningEnum = FieldMeaningEnum.AddressType;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_POST_DIRECTION:
			fieldMeaningEnum = FieldMeaningEnum.AddressPostDirection;	break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS_OTHER:
			fieldMeaningEnum = FieldMeaningEnum.AddressOther;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_ADDRESS:
			fieldMeaningEnum = FieldMeaningEnum.Address;				break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_PHONE_COUNTRY_CODE:
			fieldMeaningEnum = FieldMeaningEnum.PhoneCountryCode;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_PHONE_AREA_CODE:
			fieldMeaningEnum = FieldMeaningEnum.PhoneAreaCode;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_PHONE_NUMBER:
			fieldMeaningEnum = FieldMeaningEnum.PhoneNumber;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_PHONE_EXTENSION:
			fieldMeaningEnum = FieldMeaningEnum.PhoneExtension;			break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DATE_CREATED:
			fieldMeaningEnum = FieldMeaningEnum.DateCreated;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CREATOR_ID:
			fieldMeaningEnum = FieldMeaningEnum.CreatorId;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DATE_CHANGED:
			fieldMeaningEnum = FieldMeaningEnum.DateChanged;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CHANGED_BY_ID:
			fieldMeaningEnum = FieldMeaningEnum.ChangedById;			break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DATE_VOIDED:
			fieldMeaningEnum = FieldMeaningEnum.DateVoided;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_VOIDED_BY_ID:
			fieldMeaningEnum = FieldMeaningEnum.VoidedById;				break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DIAGNOSIS_CODES:
			fieldMeaningEnum = FieldMeaningEnum.DiagnosisCodes;			break;

		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DEATH_INDICATION:
			fieldMeaningEnum = FieldMeaningEnum.DeathIndication;		break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_DEATH_TIME:
			fieldMeaningEnum = FieldMeaningEnum.DeathTime;				break;
			
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_1:
			fieldMeaningEnum = FieldMeaningEnum.Custom1;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_2:
			fieldMeaningEnum = FieldMeaningEnum.Custom2;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_3:
			fieldMeaningEnum = FieldMeaningEnum.Custom3;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_4:
			fieldMeaningEnum = FieldMeaningEnum.Custom4;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_5:
			fieldMeaningEnum = FieldMeaningEnum.Custom5;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_6:
			fieldMeaningEnum = FieldMeaningEnum.Custom6;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_7:
			fieldMeaningEnum = FieldMeaningEnum.Custom7;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_8:
			fieldMeaningEnum = FieldMeaningEnum.Custom8;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_9:
			fieldMeaningEnum = FieldMeaningEnum.Custom9;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_10:
			fieldMeaningEnum = FieldMeaningEnum.Custom10;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_11:
			fieldMeaningEnum = FieldMeaningEnum.Custom11;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_12:
			fieldMeaningEnum = FieldMeaningEnum.Custom12;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_13:
			fieldMeaningEnum = FieldMeaningEnum.Custom13;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_14:
			fieldMeaningEnum = FieldMeaningEnum.Custom14;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_15:
			fieldMeaningEnum = FieldMeaningEnum.Custom15;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_16:
			fieldMeaningEnum = FieldMeaningEnum.Custom16;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_17:
			fieldMeaningEnum = FieldMeaningEnum.Custom17;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_18:
			fieldMeaningEnum = FieldMeaningEnum.Custom18;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_19:
			fieldMeaningEnum = FieldMeaningEnum.Custom19;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_20:
			fieldMeaningEnum = FieldMeaningEnum.Custom20;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_21:
			fieldMeaningEnum = FieldMeaningEnum.Custom21;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_22:
			fieldMeaningEnum = FieldMeaningEnum.Custom22;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_23:
			fieldMeaningEnum = FieldMeaningEnum.Custom23;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_24:
			fieldMeaningEnum = FieldMeaningEnum.Custom24;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_25:
			fieldMeaningEnum = FieldMeaningEnum.Custom25;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_26:
			fieldMeaningEnum = FieldMeaningEnum.Custom26;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_27:
			fieldMeaningEnum = FieldMeaningEnum.Custom27;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_28:
			fieldMeaningEnum = FieldMeaningEnum.Custom28;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_29:
			fieldMeaningEnum = FieldMeaningEnum.Custom29;				break;
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CUSTOM_30:
			fieldMeaningEnum = FieldMeaningEnum.Custom30;				break;
			
		case org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.INT_CBF:
			fieldMeaningEnum = FieldMeaningEnum.CBF;					break;
		}
		loaderTargetField.setFieldMeaning(fieldMeaningEnum);
		if (targetField.isSetFieldTransformation() && targetField.getFieldTransformation() != null) {
			org.openhie.openempi.configuration.xml.FunctionField trafoXml = targetField.getFieldTransformation();
			FunctionField stringTrafoFunction = Configuration.buildFunctionFieldFromXml(trafoXml);
			loaderTargetField.setFieldTransformation(stringTrafoFunction);
		}
	}
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the file loader configuration service
		//
		if (!(configurationFragment instanceof DataLoaderType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the file loader configuration service to use when needed.
		//
		LoaderConfig loaderConfiguration = new LoaderConfig();
		DataLoaderType loaderConfig = (DataLoaderType) configurationFragment;
		log.debug("Received xml fragment to parse: " + loaderConfig);
		if (loaderConfig == null || loaderConfig.getDataLoaderConfig().getDataFields().sizeOfDataFieldArray() == 0) {
			log.warn("No data fields were configured; probably a configuration issue.");
			return;
		}
		
		loaderConfiguration.setDelimiterRegex(loaderConfig.getDataLoaderConfig().getDelimiterRegex());
		loaderConfiguration.setHeaderLinePresent(loaderConfig.getDataLoaderConfig().getHeaderLinePresent());
		for (int i = 0; i < loaderConfig.getDataLoaderConfig().getDataFields().sizeOfDataFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.dataloader.DataField dataField =
				loaderConfig.getDataLoaderConfig().getDataFields().getDataFieldArray(i);
			LoaderDataField loaderDataField = new LoaderDataField();
			if (dataField.getTargetField() != null)
				loadAndRegisterTargetField(dataField.getTargetField(), loaderDataField);
			loaderDataField.setSourceColumnIndex(dataField.getSourceColumnIndex());
			loaderDataField.setSourceFieldName(dataField.getSourceFieldName());
			if (dataField.isSetSubstrings() && dataField.getSubstrings() != null) {
				List<LoaderSubField> loaderSubFields = new ArrayList<LoaderSubField>();
				for (int j = 0; j < dataField.getSubstrings().getSubstringArray().length; j++) {
					org.openhie.openempi.configuration.xml.dataloader.Substring substring =
						dataField.getSubstrings().getSubstringArray(j);
					LoaderSubField loaderSubField = new LoaderSubField();
					loaderSubField.setBeginIndex(substring.getBeginIndex());
					loaderSubField.setEndIndex(substring.getEndIndex());
					if (substring.getTargetField() != null)
						loadAndRegisterTargetField(substring.getTargetField(), loaderSubField);
					loaderSubFields.add(loaderSubField);
				}
				loaderDataField.setSubFields(loaderSubFields);
			}
			if (dataField.isSetComposition() && dataField.getComposition() != null) {
				org.openhie.openempi.configuration.xml.dataloader.Composition composition =
					dataField.getComposition();
				LoaderFieldComposition loaderFieldComposition = new LoaderFieldComposition();
				loaderFieldComposition.setIndex(composition.getIndex());
				loaderFieldComposition.setSeparator(composition.getSeparator());
				if (dataField.getTargetField() != null)
					loadAndRegisterTargetField(dataField.getTargetField(), loaderFieldComposition); // redundant
			}
			loaderConfiguration.addDataField(loaderDataField);
		}
		loaderConfiguration.checkFieldTypesCompatibleWithTransformations();
		registry.registerConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION, 
				loaderConfiguration);
	}

	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationData)
			throws InitializationException {
		LoaderConfig loaderConfiguration = (LoaderConfig) configurationData;
		loaderConfiguration.checkFieldTypesCompatibleWithTransformations();
		DataLoaderType xmlConfigurationFragment = buildConfigurationFileFragment(loaderConfiguration);
		log.debug("Saving file loader configuration info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveDataLoaderConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated file loader configuration in configuration registry: " + loaderConfiguration);
		registry.registerConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION, loaderConfiguration);
	}

	private DataLoaderType buildConfigurationFileFragment(LoaderConfig loaderConfiguration) {
		DataLoaderType newDataLoaderConfig = DataLoaderType.Factory.newInstance();
		DataLoaderConfig dataLoaderConfigNode = newDataLoaderConfig.addNewDataLoaderConfig();
		dataLoaderConfigNode.setDelimiterRegex(loaderConfiguration.getDelimiterRegex());
		dataLoaderConfigNode.setHeaderLinePresent(loaderConfiguration.getHeaderLinePresent());
		DataFields dataFieldsNode = dataLoaderConfigNode.addNewDataFields();
		for (LoaderDataField loaderDataField : loaderConfiguration.getDataFields()) {
			DataField dataFieldNode = dataFieldsNode.addNewDataField();
			if (loaderDataField.getFieldName() != null) {
				TargetField targetField = dataFieldNode.addNewTargetField();
				buildTargetFieldFileFragment(targetField, loaderDataField);
			}
			dataFieldNode.setSourceColumnIndex(loaderDataField.getSourceColumnIndex());
			dataFieldNode.setSourceFieldName(loaderDataField.getSourceFieldName());
			if (loaderDataField.getSubFields() != null) {
				Substrings substrings = dataFieldNode.addNewSubstrings();
				for (LoaderSubField loaderSubField : loaderDataField.getSubFields()) {
					Substring substring = substrings.addNewSubstring();
					substring.setBeginIndex(loaderSubField.getBeginIndex());
					substring.setEndIndex(loaderSubField.getEndIndex());
					if (loaderSubField.getFieldName() != null) {
						TargetField targetField = substring.addNewTargetField();
						buildTargetFieldFileFragment(targetField, loaderSubField);
					}
				}
			}
			if (loaderDataField.getFieldComposition() != null) {
				LoaderFieldComposition loaderFieldComposition = loaderDataField.getFieldComposition();
				Composition composition = dataFieldNode.addNewComposition();
				composition.setIndex(loaderFieldComposition.getIndex());
				composition.setSeparator(loaderFieldComposition.getSeparator());
				if (loaderFieldComposition.getFieldName() != null) {
					// TODO?
				}
			}
		}
		return newDataLoaderConfig;
	}
	
	private void buildTargetFieldFileFragment(TargetField targetField, LoaderTargetField loaderTargetField)
	{
		String fieldName = loaderTargetField.getFieldName();
		ValidationUtil.sanityCheckFieldName(fieldName);
		targetField.setFieldName(fieldName);
		org.openhie.openempi.configuration.xml.dataloader.FieldType.Enum fieldType = null;
		switch(loaderTargetField.getFieldType().getFieldTypeEnum()) {
		case String:	fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.STRING;		break;
		case Integer:	fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.INTEGER;	break;
		case BigInt:	fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.BIGINT;		break;
		case Float:		fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.FLOAT;		break;
		case Double:	fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.DOUBLE;		break;
		case Date:		fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.DATE;		break;
		case Blob:		fieldType = org.openhie.openempi.configuration.xml.dataloader.FieldType.BLOB;		break;
		}
		targetField.setFieldType(fieldType);
		if (loaderTargetField.getFieldTypeModifier() != null)
			targetField.setFieldTypeModifier(loaderTargetField.getFieldTypeModifier());
		org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.Enum fieldMeaning = null;
		switch(loaderTargetField.getFieldMeaning().getFieldMeaningEnum()) {
		case OriginalId:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ORIGINAL_ID;			break;

		case GivenName:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.GIVEN_NAME;			break;
		case FamilyName:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.FAMILY_NAME;			break;
		case MiddleName:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.MIDDLE_NAME;			break;
		case NamePrefix:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.NAME_PREFIX;			break;
		case NameSuffix:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.NAME_SUFFIX;			break;

		case DateOfBirth:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DATE_OF_BIRTH;		break;
		case BirthWeight:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.BIRTH_WEIGHT;			break;
		case BirthCity:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.BIRTH_CITY;			break;
		case BirthState:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.BIRTH_STATE;			break;
		case BirthCountry:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.BIRTH_COUNTRY;		break;
		case MothersMaidenName:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.MOTHERS_MAIDEN_NAME;	break;
		case MothersWeightAtBirth:	fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.MOTHERS_WEIGHT;		break;

		case SSN:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.SSN;					break;
		case Gender:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.GENDER;				break;
		case EthnicGroup:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ETHNIC_GROUP;			break;
		case Race:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.RACE;					break;
		case Nationality:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.NATIONALITY;			break;
		case Language:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.LANGUAGE;				break;
		case Religion:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.RELIGION;				break;
		case MaritalStatus:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.MARITAL_STATUS;		break;
		case Degree:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DEGREE;				break;
			
		case Email:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.EMAIL;				break;
		case AddressLine1:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_LINE_1;		break;
		case AddressLine2:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_LINE_2;		break;
		case City:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CITY;					break;
		case County:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.COUNTY;				break;
		case State:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.STATE;				break;
		case Country:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.COUNTRY;				break;
		case PostalCode:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.POSTAL_CODE;			break;
		case AddressNumber:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_NUMBER;		break;
		case AddressFraction:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_FRACTION;		break;
		case AddressDirection:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_DIRECTION;	break;
		case AddressStreetName:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_STREET_NAME;	break;
		case AddressType:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_TYPE;			break;
		case AddressPostDirection:	fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_POST_DIRECTION;break;
		case AddressOther:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS_OTHER;		break;
		case Address:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.ADDRESS;				break;

		case PhoneCountryCode:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.PHONE_COUNTRY_CODE;	break;
		case PhoneAreaCode:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.PHONE_AREA_CODE;		break;
		case PhoneNumber:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.PHONE_NUMBER;			break;
		case PhoneExtension:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.PHONE_EXTENSION;		break;

		case DateCreated:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DATE_CREATED;			break;
		case CreatorId:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CREATOR_ID;			break;
		case DateChanged:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DATE_CHANGED;			break;
		case ChangedById:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CHANGED_BY_ID;		break;
		case DateVoided:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DATE_VOIDED;			break;
		case VoidedById:			fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.VOIDED_BY_ID;			break;

		case DiagnosisCodes:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DIAGNOSIS_CODES;		break;

		case DeathIndication:		fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DEATH_INDICATION;		break;
		case DeathTime:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.DEATH_TIME;			break;
			
		case Custom1:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_1;				break;
		case Custom2:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_2;				break;
		case Custom3:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_3;				break;
		case Custom4:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_4;				break;
		case Custom5:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_5;				break;
		case Custom6:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_6;				break;
		case Custom7:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_7;				break;
		case Custom8:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_8;				break;
		case Custom9:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_9;				break;
		case Custom10:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_10;			break;
		case Custom11:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_11;			break;
		case Custom12:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_12;			break;
		case Custom13:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_13;			break;
		case Custom14:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_14;			break;
		case Custom15:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_15;			break;
		case Custom16:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_16;			break;
		case Custom17:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_17;			break;
		case Custom18:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_18;			break;
		case Custom19:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_19;			break;
		case Custom20:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_20;			break;
		case Custom21:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_21;			break;
		case Custom22:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_22;			break;
		case Custom23:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_23;			break;
		case Custom24:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_24;			break;
		case Custom25:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_25;			break;
		case Custom26:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_26;			break;
		case Custom27:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_27;			break;
		case Custom28:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_28;			break;
		case Custom29:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_29;			break;
		case Custom30:				fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CUSTOM_30;			break;
			
		case CBF:					fieldMeaning = org.openhie.openempi.configuration.xml.dataloader.FieldMeaning.CBF;					break;
		}
		targetField.setFieldMeaning(fieldMeaning);		
		if (loaderTargetField.getFieldTransformation() != null) {
			org.openhie.openempi.configuration.xml.FunctionField fieldTrafoXml = targetField.addNewFieldTransformation();
			Configuration.buildFunctionFieldFragment(fieldTrafoXml, loaderTargetField.getFieldTransformation());
		}
	}

}
