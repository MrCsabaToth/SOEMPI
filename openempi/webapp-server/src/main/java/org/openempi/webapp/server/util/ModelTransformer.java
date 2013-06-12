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
package org.openempi.webapp.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.openempi.webapp.client.model.ColumnInformationWeb;
import org.openempi.webapp.client.model.ColumnMatchInformationWeb;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;
import org.openempi.webapp.client.model.FieldMeaningWeb;
import org.openempi.webapp.client.model.FieldTypeWeb;
import org.openempi.webapp.client.model.FunctionFieldWeb;
import org.openempi.webapp.client.model.FunctionParameterWeb;
import org.openempi.webapp.client.model.Key;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonMatchWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.Salt;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ColumnMatchInformation;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.configuration.FunctionField;

public class ModelTransformer
{
	public static Logger log = Logger.getLogger(ModelTransformer.class);
	
	public static <T> T map(Object sourceObject, Class<T> destClass) {
		Mapper mapper = new DozerBeanMapper();
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + sourceObject.getClass() + " to type " + destClass);
		}
		return mapper.map(sourceObject, destClass);
	}
	
	public static <T> Set<T> mapSet(Set<?> sourceObjects, Class<T> destClass) {
		if (sourceObjects == null || sourceObjects.size() == 0) {
			return new HashSet<T>();
		}
		if (log.isDebugEnabled()) {
			log.debug("Transforming collection of objects of type " + sourceObjects.iterator().next().getClass() + " to type " + destClass);
		}
		Mapper mapper = new DozerBeanMapper();
		Set<T> collection = new HashSet<T>(sourceObjects.size());
		for (Object o : sourceObjects) {
			T mo = mapper.map(o, destClass);
			collection.add(mo);
		}
		return collection;
	}
	
	public static <T> List<T> mapList(List<?> sourceObjects, Class<T> destClass) {
		if (sourceObjects == null || sourceObjects.size() == 0) {
			return new ArrayList<T>();
		}
		if (log.isDebugEnabled()) {
			log.debug("Transforming collection of objects of type " + sourceObjects.get(0).getClass() + " to type " + destClass);
		}
		Mapper mapper = new DozerBeanMapper();
		List<T> collection = new ArrayList<T>(sourceObjects.size());
		for (Object o : sourceObjects) {
			T mo = mapper.map(o, destClass);
			collection.add(mo);
		}
		return collection;
	}

	public static Person mapToPerson(PersonWeb personWeb, Class<Person> personClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + personWeb.getClass() + " to type " + personClass);
		}
		Person thePerson = new Person();
		thePerson.setPersonId(personWeb.getPersonId());
		Map<String, Object> attributes = personWeb.getAttributes();
		if (attributes.size() > 0) {
			for (Map.Entry<String, Object> pairs : attributes.entrySet()) {
				thePerson.setAttribute(pairs.getKey(), pairs.getValue());
			}
		}
		return thePerson;
	}

	public static FunctionFieldWeb convertToClientModel(FunctionField functionField)
	{
		FunctionFieldWeb ffw = new FunctionFieldWeb(functionField.getFunctionName());
		if (functionField.getFunctionParameters() != null) {
			List<FunctionParameterWeb> functionParameters = new ArrayList<FunctionParameterWeb>();
			for (Map.Entry<String, Object> pairs : functionField.getFunctionParameters().entrySet()) {
				FunctionParameterWeb functionParameter = new FunctionParameterWeb();
				functionParameter.setParameterName(pairs.getKey());
				Object parameterValue = pairs.getValue();
				if (parameterValue instanceof String) {
					functionParameter.setParameterValue((String)parameterValue);
					functionParameter.setParameterType(org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.STRING);
				} else if (parameterValue instanceof Integer) {
					functionParameter.setParameterValue(Integer.toString((Integer)parameterValue));
					functionParameter.setParameterType(org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.INTEGER);
				} else if (parameterValue instanceof Double) {
					functionParameter.setParameterValue(Double.toString((Double)parameterValue));
					functionParameter.setParameterType(org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.DOUBLE);
				} else if (parameterValue instanceof Float) {
					functionParameter.setParameterValue(Float.toString((Float)parameterValue));
					functionParameter.setParameterType(org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.FLOAT);
				} else {
					// TODO: error
				}
				functionParameters.add(functionParameter);
		    }
		    ffw.setFunctionParameters(functionParameters);
		}
		return ffw;
	}

	public static FunctionField convertFromClientModel(FunctionFieldWeb functionFieldWeb)
	{
		FunctionField functionField = new FunctionField(functionFieldWeb.getFunctionName());
		if (functionFieldWeb.getFunctionParameters() != null) {
			Map<String, Object> functionParameters = new HashMap<String, Object>();
			for (FunctionParameterWeb functionParameterWeb : functionFieldWeb.getFunctionParameters())
			{
				Object value =  null;
				if (functionParameterWeb.getParameterType() == org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.STRING)
					value = functionParameterWeb.getParameterValue();
				else if (functionParameterWeb.getParameterType() == org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.INTEGER)
					value = Integer.valueOf(functionParameterWeb.getParameterValue());
				else if (functionParameterWeb.getParameterType() == org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.DOUBLE)
					value = Double.valueOf(functionParameterWeb.getParameterValue());
				else if (functionParameterWeb.getParameterType() == org.openempi.webapp.client.model.FunctionParameterWeb.ParameterTypeWeb.FLOAT)
					value = Float.valueOf(functionParameterWeb.getParameterValue());
				functionParameters.put(functionParameterWeb.getParameterName(), value);
			}
			functionField.setFunctionParameters(functionParameters);
		}
		return functionField;
	}

	public static FieldTypeWeb convertToClientModel(FieldType fieldType)
	{
		// TODO: replace with automatic conversion
		FieldTypeWeb fieldTypeWeb = null;
		switch(fieldType.getFieldTypeEnum()) {
		case String:	fieldTypeWeb = FieldTypeWeb.String;		break;
		case Integer:	fieldTypeWeb = FieldTypeWeb.Integer;	break;
		case BigInt:	fieldTypeWeb = FieldTypeWeb.BigInt;		break;
		case Float:		fieldTypeWeb = FieldTypeWeb.Float;		break;
		case Double:	fieldTypeWeb = FieldTypeWeb.Double;		break;
		case Date:		fieldTypeWeb = FieldTypeWeb.Date;		break;
		case Blob:		fieldTypeWeb = FieldTypeWeb.Blob;		break;
		}
		return fieldTypeWeb;
	}

	public static FieldTypeEnum convertFromClientModel(FieldTypeWeb fieldTypeWeb)
	{
		// TODO: replace with automatic conversion
		FieldTypeEnum fieldTypeEnum = null;
		switch(fieldTypeWeb) {
		case String:	fieldTypeEnum = FieldTypeEnum.String;	break;
		case Integer:	fieldTypeEnum = FieldTypeEnum.Integer;	break;
		case BigInt:	fieldTypeEnum = FieldTypeEnum.BigInt;	break;
		case Float:		fieldTypeEnum = FieldTypeEnum.Float;	break;
		case Double:	fieldTypeEnum = FieldTypeEnum.Double;	break;
		case Date:		fieldTypeEnum = FieldTypeEnum.Date;		break;
		case Blob:		fieldTypeEnum = FieldTypeEnum.Blob;		break;
		}
		return fieldTypeEnum;
	}

	public static FieldMeaningWeb convertToClientModel(FieldMeaning fieldMeaning)
	{
		// TODO: replace with automatic conversion
		FieldMeaningWeb fieldMeaningWeb = null;
		switch(fieldMeaning.getFieldMeaningEnum()) {
		case OriginalId:			fieldMeaningWeb = FieldMeaningWeb.OriginalId;			break;

		case GivenName:				fieldMeaningWeb = FieldMeaningWeb.GivenName;			break;
		case FamilyName:			fieldMeaningWeb = FieldMeaningWeb.FamilyName;			break;
		case MiddleName:			fieldMeaningWeb = FieldMeaningWeb.MiddleName;			break;
		case NamePrefix:			fieldMeaningWeb = FieldMeaningWeb.NamePrefix;			break;
		case NameSuffix:			fieldMeaningWeb = FieldMeaningWeb.NameSuffix;			break;

		case DateOfBirth:			fieldMeaningWeb = FieldMeaningWeb.DateOfBirth;			break;
		case BirthWeight:			fieldMeaningWeb = FieldMeaningWeb.BirthWeight;			break;
		case BirthCity:				fieldMeaningWeb = FieldMeaningWeb.BirthCity;			break;
		case BirthState:			fieldMeaningWeb = FieldMeaningWeb.BirthState;			break;
		case BirthCountry:			fieldMeaningWeb = FieldMeaningWeb.BirthCountry;			break;
		case MothersMaidenName:		fieldMeaningWeb = FieldMeaningWeb.MothersMaidenName;	break;
		case MothersWeightAtBirth:	fieldMeaningWeb = FieldMeaningWeb.MothersWeightAtBirth;	break;

		case SSN:					fieldMeaningWeb = FieldMeaningWeb.SSN;					break;
		case Gender:				fieldMeaningWeb = FieldMeaningWeb.Gender;				break;
		case EthnicGroup:			fieldMeaningWeb = FieldMeaningWeb.EthnicGroup;			break;
		case Race:					fieldMeaningWeb = FieldMeaningWeb.Race;					break;
		case Nationality:			fieldMeaningWeb = FieldMeaningWeb.Nationality;			break;
		case Language:				fieldMeaningWeb = FieldMeaningWeb.Language;				break;
		case Religion:				fieldMeaningWeb = FieldMeaningWeb.Religion;				break;
		case MaritalStatus:			fieldMeaningWeb = FieldMeaningWeb.MaritalStatus;		break;
		case Degree:				fieldMeaningWeb = FieldMeaningWeb.Degree;				break;

		case Email:					fieldMeaningWeb = FieldMeaningWeb.Email;				break;
		case AddressLine1:			fieldMeaningWeb = FieldMeaningWeb.AddressLine1;			break;
		case AddressLine2:			fieldMeaningWeb = FieldMeaningWeb.AddressLine2;			break;
		case City:					fieldMeaningWeb = FieldMeaningWeb.City;					break;
		case County:				fieldMeaningWeb = FieldMeaningWeb.County;				break;
		case State:					fieldMeaningWeb = FieldMeaningWeb.State;				break;
		case Country:				fieldMeaningWeb = FieldMeaningWeb.Country;				break;
		case PostalCode:			fieldMeaningWeb = FieldMeaningWeb.PostalCode;			break;
		case AddressNumber:			fieldMeaningWeb = FieldMeaningWeb.AddressNumber;		break;
		case AddressFraction:		fieldMeaningWeb = FieldMeaningWeb.AddressFraction;		break;
		case AddressDirection:		fieldMeaningWeb = FieldMeaningWeb.AddressDirection;		break;
		case AddressStreetName:		fieldMeaningWeb = FieldMeaningWeb.AddressStreetName;	break;
		case AddressType:			fieldMeaningWeb = FieldMeaningWeb.AddressType;			break;
		case AddressPostDirection:	fieldMeaningWeb = FieldMeaningWeb.AddressPostDirection;	break;
		case AddressOther:			fieldMeaningWeb = FieldMeaningWeb.AddressOther;			break;
		case Address:				fieldMeaningWeb = FieldMeaningWeb.Address;				break;

		case PhoneCountryCode:		fieldMeaningWeb = FieldMeaningWeb.PhoneCountryCode;		break;
		case PhoneAreaCode:			fieldMeaningWeb = FieldMeaningWeb.PhoneAreaCode;		break;
		case PhoneNumber:			fieldMeaningWeb = FieldMeaningWeb.PhoneNumber;			break;
		case PhoneExtension:		fieldMeaningWeb = FieldMeaningWeb.PhoneExtension;		break;

		case DateCreated:			fieldMeaningWeb = FieldMeaningWeb.DateCreated;			break;
		case CreatorId:				fieldMeaningWeb = FieldMeaningWeb.CreatorId;			break;
		case DateChanged:			fieldMeaningWeb = FieldMeaningWeb.DateChanged;			break;
		case ChangedById:			fieldMeaningWeb = FieldMeaningWeb.ChangedById;			break;
		case DateVoided:			fieldMeaningWeb = FieldMeaningWeb.DateVoided;			break;
		case VoidedById:			fieldMeaningWeb = FieldMeaningWeb.VoidedById;			break;

		case DiagnosisCodes:		fieldMeaningWeb = FieldMeaningWeb.DiagnosisCodes;		break;

		case DeathIndication:		fieldMeaningWeb = FieldMeaningWeb.DeathIndication;		break;
		case DeathTime:				fieldMeaningWeb = FieldMeaningWeb.DeathTime;			break;

		case Custom1:				fieldMeaningWeb = FieldMeaningWeb.Custom1;				break;
		case Custom2:				fieldMeaningWeb = FieldMeaningWeb.Custom2;				break;
		case Custom3:				fieldMeaningWeb = FieldMeaningWeb.Custom3;				break;
		case Custom4:				fieldMeaningWeb = FieldMeaningWeb.Custom4;				break;
		case Custom5:				fieldMeaningWeb = FieldMeaningWeb.Custom5;				break;
		case Custom6:				fieldMeaningWeb = FieldMeaningWeb.Custom6;				break;
		case Custom7:				fieldMeaningWeb = FieldMeaningWeb.Custom7;				break;
		case Custom8:				fieldMeaningWeb = FieldMeaningWeb.Custom8;				break;
		case Custom9:				fieldMeaningWeb = FieldMeaningWeb.Custom9;				break;
		case Custom10:				fieldMeaningWeb = FieldMeaningWeb.Custom10;				break;
		case Custom11:				fieldMeaningWeb = FieldMeaningWeb.Custom11;				break;
		case Custom12:				fieldMeaningWeb = FieldMeaningWeb.Custom12;				break;
		case Custom13:				fieldMeaningWeb = FieldMeaningWeb.Custom13;				break;
		case Custom14:				fieldMeaningWeb = FieldMeaningWeb.Custom14;				break;
		case Custom15:				fieldMeaningWeb = FieldMeaningWeb.Custom15;				break;
		case Custom16:				fieldMeaningWeb = FieldMeaningWeb.Custom16;				break;
		case Custom17:				fieldMeaningWeb = FieldMeaningWeb.Custom17;				break;
		case Custom18:				fieldMeaningWeb = FieldMeaningWeb.Custom18;				break;
		case Custom19:				fieldMeaningWeb = FieldMeaningWeb.Custom19;				break;
		case Custom20:				fieldMeaningWeb = FieldMeaningWeb.Custom20;				break;
		case Custom21:				fieldMeaningWeb = FieldMeaningWeb.Custom21;				break;
		case Custom22:				fieldMeaningWeb = FieldMeaningWeb.Custom22;				break;
		case Custom23:				fieldMeaningWeb = FieldMeaningWeb.Custom23;				break;
		case Custom24:				fieldMeaningWeb = FieldMeaningWeb.Custom24;				break;
		case Custom25:				fieldMeaningWeb = FieldMeaningWeb.Custom25;				break;
		case Custom26:				fieldMeaningWeb = FieldMeaningWeb.Custom26;				break;
		case Custom27:				fieldMeaningWeb = FieldMeaningWeb.Custom27;				break;
		case Custom28:				fieldMeaningWeb = FieldMeaningWeb.Custom28;				break;
		case Custom29:				fieldMeaningWeb = FieldMeaningWeb.Custom29;				break;
		case Custom30:				fieldMeaningWeb = FieldMeaningWeb.Custom30;				break;

		case CBF:					fieldMeaningWeb = FieldMeaningWeb.CBF;					break;
		}
		return fieldMeaningWeb;
	}

	public static FieldMeaningEnum convertFromClientModel(FieldMeaningWeb fieldMeaningWeb)
	{
		// TODO: replace with automatic conversion
		FieldMeaningEnum fieldMeaningEnum = null;
		switch(fieldMeaningWeb) {
		case OriginalId:			fieldMeaningEnum = FieldMeaningEnum.OriginalId;				break;
		
		case GivenName:				fieldMeaningEnum = FieldMeaningEnum.GivenName;				break;
		case FamilyName:			fieldMeaningEnum = FieldMeaningEnum.FamilyName;				break;
		case MiddleName:			fieldMeaningEnum = FieldMeaningEnum.MiddleName;				break;
		case NamePrefix:			fieldMeaningEnum = FieldMeaningEnum.NamePrefix;				break;
		case NameSuffix:			fieldMeaningEnum = FieldMeaningEnum.NameSuffix;				break;

		case DateOfBirth:			fieldMeaningEnum = FieldMeaningEnum.DateOfBirth;			break;
		case BirthWeight:			fieldMeaningEnum = FieldMeaningEnum.BirthWeight;			break;
		case BirthCity:				fieldMeaningEnum = FieldMeaningEnum.BirthCity;				break;
		case BirthState:			fieldMeaningEnum = FieldMeaningEnum.BirthState;				break;
		case BirthCountry:			fieldMeaningEnum = FieldMeaningEnum.BirthCountry;			break;
		case MothersMaidenName:		fieldMeaningEnum = FieldMeaningEnum.MothersMaidenName;		break;
		case MothersWeightAtBirth:	fieldMeaningEnum = FieldMeaningEnum.MothersWeightAtBirth;	break;

		case SSN:					fieldMeaningEnum = FieldMeaningEnum.SSN;					break;
		case Gender:				fieldMeaningEnum = FieldMeaningEnum.Gender;					break;
		case EthnicGroup:			fieldMeaningEnum = FieldMeaningEnum.EthnicGroup;			break;
		case Race:					fieldMeaningEnum = FieldMeaningEnum.Race;					break;
		case Nationality:			fieldMeaningEnum = FieldMeaningEnum.Nationality;			break;
		case Language:				fieldMeaningEnum = FieldMeaningEnum.Language;				break;
		case Religion:				fieldMeaningEnum = FieldMeaningEnum.Religion;				break;
		case MaritalStatus:			fieldMeaningEnum = FieldMeaningEnum.MaritalStatus;			break;
		case Degree:				fieldMeaningEnum = FieldMeaningEnum.Degree;					break;

		case Email:					fieldMeaningEnum = FieldMeaningEnum.Email;					break;
		case AddressLine1:			fieldMeaningEnum = FieldMeaningEnum.AddressLine1;			break;
		case AddressLine2:			fieldMeaningEnum = FieldMeaningEnum.AddressLine2;			break;
		case City:					fieldMeaningEnum = FieldMeaningEnum.City;					break;
		case County:				fieldMeaningEnum = FieldMeaningEnum.County;					break;
		case State:					fieldMeaningEnum = FieldMeaningEnum.State;					break;
		case Country:				fieldMeaningEnum = FieldMeaningEnum.Country;				break;
		case PostalCode:			fieldMeaningEnum = FieldMeaningEnum.PostalCode;				break;
		case AddressNumber:			fieldMeaningEnum = FieldMeaningEnum.AddressNumber;			break;
		case AddressFraction:		fieldMeaningEnum = FieldMeaningEnum.AddressFraction;		break;
		case AddressDirection:		fieldMeaningEnum = FieldMeaningEnum.AddressDirection;		break;
		case AddressStreetName:		fieldMeaningEnum = FieldMeaningEnum.AddressStreetName;		break;
		case AddressType:			fieldMeaningEnum = FieldMeaningEnum.AddressType;			break;
		case AddressPostDirection:	fieldMeaningEnum = FieldMeaningEnum.AddressPostDirection;	break;
		case AddressOther:			fieldMeaningEnum = FieldMeaningEnum.AddressOther;			break;
		case Address:				fieldMeaningEnum = FieldMeaningEnum.Address;				break;

		case PhoneCountryCode:		fieldMeaningEnum = FieldMeaningEnum.PhoneCountryCode;		break;
		case PhoneAreaCode:			fieldMeaningEnum = FieldMeaningEnum.PhoneAreaCode;			break;
		case PhoneNumber:			fieldMeaningEnum = FieldMeaningEnum.PhoneNumber;			break;
		case PhoneExtension:		fieldMeaningEnum = FieldMeaningEnum.PhoneExtension;			break;

		case DateCreated:			fieldMeaningEnum = FieldMeaningEnum.DateCreated;			break;
		case CreatorId:				fieldMeaningEnum = FieldMeaningEnum.CreatorId;				break;
		case DateChanged:			fieldMeaningEnum = FieldMeaningEnum.DateChanged;			break;
		case ChangedById:			fieldMeaningEnum = FieldMeaningEnum.ChangedById;			break;
		case DateVoided:			fieldMeaningEnum = FieldMeaningEnum.DateVoided;				break;
		case VoidedById:			fieldMeaningEnum = FieldMeaningEnum.VoidedById;				break;

		case DiagnosisCodes:		fieldMeaningEnum = FieldMeaningEnum.DiagnosisCodes;			break;

		case DeathIndication:		fieldMeaningEnum = FieldMeaningEnum.DeathIndication;		break;
		case DeathTime:				fieldMeaningEnum = FieldMeaningEnum.DeathTime;				break;

		case Custom1:				fieldMeaningEnum = FieldMeaningEnum.Custom1;				break;
		case Custom2:				fieldMeaningEnum = FieldMeaningEnum.Custom2;				break;
		case Custom3:				fieldMeaningEnum = FieldMeaningEnum.Custom3;				break;
		case Custom4:				fieldMeaningEnum = FieldMeaningEnum.Custom4;				break;
		case Custom5:				fieldMeaningEnum = FieldMeaningEnum.Custom5;				break;
		case Custom6:				fieldMeaningEnum = FieldMeaningEnum.Custom6;				break;
		case Custom7:				fieldMeaningEnum = FieldMeaningEnum.Custom7;				break;
		case Custom8:				fieldMeaningEnum = FieldMeaningEnum.Custom8;				break;
		case Custom9:				fieldMeaningEnum = FieldMeaningEnum.Custom9;				break;
		case Custom10:				fieldMeaningEnum = FieldMeaningEnum.Custom10;				break;
		case Custom11:				fieldMeaningEnum = FieldMeaningEnum.Custom11;				break;
		case Custom12:				fieldMeaningEnum = FieldMeaningEnum.Custom12;				break;
		case Custom13:				fieldMeaningEnum = FieldMeaningEnum.Custom13;				break;
		case Custom14:				fieldMeaningEnum = FieldMeaningEnum.Custom14;				break;
		case Custom15:				fieldMeaningEnum = FieldMeaningEnum.Custom15;				break;
		case Custom16:				fieldMeaningEnum = FieldMeaningEnum.Custom16;				break;
		case Custom17:				fieldMeaningEnum = FieldMeaningEnum.Custom17;				break;
		case Custom18:				fieldMeaningEnum = FieldMeaningEnum.Custom18;				break;
		case Custom19:				fieldMeaningEnum = FieldMeaningEnum.Custom19;				break;
		case Custom20:				fieldMeaningEnum = FieldMeaningEnum.Custom20;				break;
		case Custom21:				fieldMeaningEnum = FieldMeaningEnum.Custom21;				break;
		case Custom22:				fieldMeaningEnum = FieldMeaningEnum.Custom22;				break;
		case Custom23:				fieldMeaningEnum = FieldMeaningEnum.Custom23;				break;
		case Custom24:				fieldMeaningEnum = FieldMeaningEnum.Custom24;				break;
		case Custom25:				fieldMeaningEnum = FieldMeaningEnum.Custom25;				break;
		case Custom26:				fieldMeaningEnum = FieldMeaningEnum.Custom26;				break;
		case Custom27:				fieldMeaningEnum = FieldMeaningEnum.Custom27;				break;
		case Custom28:				fieldMeaningEnum = FieldMeaningEnum.Custom28;				break;
		case Custom29:				fieldMeaningEnum = FieldMeaningEnum.Custom29;				break;
		case Custom30:				fieldMeaningEnum = FieldMeaningEnum.Custom30;				break;

		case CBF:					fieldMeaningEnum = FieldMeaningEnum.CBF;					break;
		}
		return fieldMeaningEnum;
	}

	public static List<ColumnInformationWeb> convertToClientModel(List<ColumnInformation> columnInformation) {
		List<ColumnInformationWeb> columnInformationsWeb = new ArrayList<ColumnInformationWeb>();
		for(ColumnInformation ci : columnInformation) {
			ColumnInformationWeb columnInformationWeb = new ColumnInformationWeb(ci.getFieldName());
			columnInformationWeb.setFieldType(convertToClientModel(ci.getFieldType()));
			columnInformationWeb.setFieldTypeModifier(ci.getFieldTypeModifier());
			columnInformationWeb.setFieldMeaning(convertToClientModel(ci.getFieldMeaning()));
			columnInformationWeb.setColumnInformationId(ci.getColumnInformationId());
			columnInformationWeb.setDatasetId(ci.getDataset().getDatasetId());
			columnInformationWeb.setFieldTransformation(ci.getFieldTransformation());
			columnInformationWeb.setBloomFilterMParameter(ci.getBloomFilterMParameter());
			columnInformationWeb.setBloomFilterKParameter(ci.getBloomFilterKParameter());
			columnInformationWeb.setAverageFieldLength(ci.getAverageFieldLength());
			columnInformationWeb.setNumberOfMissing(ci.getNumberOfMissing());
			columnInformationsWeb.add(columnInformationWeb);
		}
		return columnInformationsWeb;
	}

	public static List<ColumnMatchInformationWeb> convertToClientModel2(List<ColumnMatchInformation> columnMatchInformation) {
		List<ColumnMatchInformationWeb> columnMatchInformationsWeb = new ArrayList<ColumnMatchInformationWeb>();
		for(ColumnMatchInformation cmi : columnMatchInformation) {
			ColumnMatchInformationWeb cmiWeb = new ColumnMatchInformationWeb();
			cmiWeb.setColumnMatchInformationId(cmi.getColumnMatchInformationId());
			cmiWeb.setPersonMatchTitle(cmi.getPersonMatch().getMatchTitle());
			cmiWeb.setLeftFieldName(cmi.getLeftFieldName());
			cmiWeb.setRightFieldName(cmi.getRightFieldName());
			cmiWeb.setFieldType(convertToClientModel(cmi.getFieldType()));
			cmiWeb.setFieldTypeModifier(cmi.getFieldTypeModifier());
			cmiWeb.setFieldMeaning(convertToClientModel(cmi.getFieldMeaning()));
			cmiWeb.setFellegiSunterMValue(cmi.getFellegiSunterMValue());
			cmiWeb.setFellegiSunterUValue(cmi.getFellegiSunterUValue());
			cmiWeb.setBloomFilterProposedM(cmi.getBloomFilterProposedM());
			cmiWeb.setBloomFilterPossibleM(cmi.getBloomFilterPossibleM());
			cmiWeb.setBloomFilterFinalM(cmi.getBloomFilterFinalM());
			columnMatchInformationsWeb.add(cmiWeb);
		}
		return columnMatchInformationsWeb;
	}

	public static PersonMatchWeb convertToClientModel(PersonMatch personMatch) {
		PersonMatchWeb personMatchWeb = new PersonMatchWeb();
		personMatchWeb.setPersonMatchId(personMatch.getPersonMatchId());
		personMatchWeb.setMatchTitle(personMatch.getMatchTitle());
		personMatchWeb.setLeftDatasetName(personMatch.getLeftDataset().getTableName());
		personMatchWeb.setRightDatasetName(personMatch.getRightDataset().getTableName());
		personMatchWeb.setBloomFilterKParameter(personMatch.getBloomFilterKParameter());
		personMatchWeb.setBloomFilterFillFactor(personMatch.getBloomFilterFillFactor());
		personMatchWeb.setTotalRecords(personMatch.getTotalRecords());
		personMatchWeb.setDateCreated(personMatch.getDateCreated());
		return personMatchWeb;
	}

	public static List<PersonLinkWeb> convertToClientModel3(List<PersonLink> personLinks) {
		List<PersonLinkWeb> personLinksWeb = new ArrayList<PersonLinkWeb>();
		if (personLinks != null) {
			for (PersonLink personLink : personLinks) {
				PersonLinkWeb personLinkWeb = new PersonLinkWeb();
				personLinkWeb.setPersonLinkId(personLink.getPersonLinkId());
				personLinkWeb.setLeftPersonId(personLink.getLeftPersonId());
				personLinkWeb.setRightPersonId(personLink.getRightPersonId());
				personLinkWeb.setBinaryVector(personLink.getBinaryVector());
				personLinkWeb.setContinousVector(personLink.getContinousVector());
				personLinkWeb.setWeight(personLink.getWeight());
				personLinkWeb.setLinkState(personLink.getLinkState());
				personLinksWeb.add(personLinkWeb);
			}
		}
		return personLinksWeb;
	}

	public static PersonWeb convertToClientModel(Person person) {
		PersonWeb personWeb = new PersonWeb();
		personWeb.setPersonId(person.getPersonId());
		personWeb.setAttributes(convertToClientModel(person.getAttributes(), 0));
		return personWeb;
	}

	private static Map<String, Object> convertToClientModel(Map<String, Object> attributes, int numberOfAttributes) {
		if (attributes.size() <= 0)
			return null;
		Map<String, Object> attributesWeb = new HashMap<String, Object>();
		int i = 0;
		for (Map.Entry<String, Object> pairs : attributes.entrySet()) {
			if (numberOfAttributes > 0 && i >= numberOfAttributes)
				break;
			if (pairs.getValue() != null) {
				if (pairs.getValue() instanceof String) {
					attributesWeb.put(pairs.getKey(), pairs.getValue());
				} else {
					attributesWeb.put(pairs.getKey(), pairs.getValue().toString());
				}
				i++;
			}
		}
		return attributesWeb;
	}

	public static FellegiSunterParametersWeb convertToClientModel(FellegiSunterParameters fellegiSunterParameters) {
		FellegiSunterParametersWeb fspWeb = new FellegiSunterParametersWeb();
		List<Long> vectorFrequencies = new ArrayList<Long>();
		for (int i = 0; i < fellegiSunterParameters.getVectorCount(); i++) {
			vectorFrequencies.add(fellegiSunterParameters.getVectorFrequency(i));
		}
		fspWeb.setVectorFrequencies(vectorFrequencies);
		List<Double> mValues = new ArrayList<Double>();
		List<Double> uValues = new ArrayList<Double>();
		for (int i = 0; i < fellegiSunterParameters.getFieldCount(); i++) {
			mValues.add(fellegiSunterParameters.getMValue(i));
			uValues.add(fellegiSunterParameters.getUValue(i));
		}
		fspWeb.setMValues(mValues);
		fspWeb.setUValues(uValues);
		fspWeb.setLowerBound(fellegiSunterParameters.getLowerBound());
		fspWeb.setUpperBound(fellegiSunterParameters.getUpperBound());
		fspWeb.setLambda(fellegiSunterParameters.getLambda());
		fspWeb.setMu(fellegiSunterParameters.getMu());
		return fspWeb;
	}

	public static Key transformKey(org.openhie.openempi.model.Key key) {
		Key clientKey = new Key();
		clientKey.setId(key.getId());
		clientKey.setPublicKeyPart1Length(key.getPublicKeyPart1().length);
		clientKey.setPublicKeyPart2Length(key.getPublicKeyPart2().length);
		clientKey.setPublicKeyPart3Length(key.getPublicKeyPart3().length);
		clientKey.setPrivateKeyPart1Length(key.getPrivateKeyPart1().length);
		clientKey.setPrivateKeyPart2Length(key.getPrivateKeyPart2().length);
		clientKey.setPrivateKeyPart3Length(key.getPrivateKeyPart3().length);
		return clientKey;
	}

	public static Salt transformSalt(org.openhie.openempi.model.Salt salt) {
		Salt clientSalt = new Salt();
		clientSalt.setId(salt.getId());
		clientSalt.setSaltLength(salt.getSalt().length);
		return clientSalt;
	}

}
