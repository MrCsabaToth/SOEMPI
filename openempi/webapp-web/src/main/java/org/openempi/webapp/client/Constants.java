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
package org.openempi.webapp.client;

public class Constants
{
	public final static int HEADER_OFFSET = 90;

	// Registry constants used throughout the application for data sharing
	//
	public static final String WEST_PANEL = "west";
	public static final String NORTH_PANEL = "north";
	public static final String VIEWPORT = "viewport";
	public static final String CENTER_PANEL = "center";

	public static final String SERVICE = "mailservice";

	public static final String BLOCKING_DATA_SERVICE = "blockingDataService";
	public static final String PERSON_DATA_SERVICE = "personDataService";
	public static final String REF_DATA_SERVICE = "referenceDataService";
	public static final String FILE_LOADER_DATA_SERVICE = "fileLoaderDataService";
	public static final String CONFIGURATION_DATA_SERVICE = "configurationDataService";
	public static final String KEY_DATA_SERVICE = "keyDataService";
	public static final String SALT_DATA_SERVICE = "saltDataService";
	
	public static final String ALL_TRANSFORMATION_FUNCTION_NAME_MODEL_PROPERTY_LIST = "allTransformationFunctionNameModelPropertyList";
	public static final String ALL_TRANSFORMATION_FUNCTION_NAME_SIMPLE_VALUE_LIST = "allTransformationFunctionNameSimpleValueList";
	public static final String COMPARATOR_FUNCTION_NAME_MODEL_PROPERTY_LIST = "comparatorFunctionNameModelPropertyList";
	public static final String COMPARATOR_FUNCTION_NAME_SIMPLE_VALUE_LIST = "comparatorFunctionNameSimpleValueList";
	public static final String FIELD_TYPE_NAME_MODEL_PROPERTY_LIST = "fieldTypeNameModelPropertyList";
	public static final String FIELD_TYPE_NAME_MODEL_SIMPLE_VALUE_LIST = "fieldTypeNameSimpleValueList";
	public static final String FIELD_MEANING_NAME_MODEL_PROPERTY_LIST = "fieldMeaningNameModelPropertyList";
	public static final String FIELD_MEANING_NAME_MODEL_SIMPLE_VALUE_LIST = "fieldMeaningNameSimpleValueList";
	public static final String TABLE_NAMES = "tableNames";
	public static final String LEFT_TABLE_COLUMN_SPECIFICATION = "leftTableColumnSpecification";
	public static final String LEFT_TABLE_FIELD_NAMES = "leftTableFieldNames";
	public static final String LEFT_TABLE_FIELD_NAME_MODEL_PROPERTY_LIST = "leftTableFieldNamesModelPropertyList";
	public static final String RIGHT_TABLE_COLUMN_SPECIFICATION = "rightTableColumnSpecification";
	public static final String RIGHT_TABLE_FIELD_NAMES = "rightTableFieldNames";
	public static final String RIGHT_TABLE_FIELD_NAME_MODEL_PROPERTY_LIST = "rightTableFieldNamesModelPropertyList";
	public static final String BLOCKING_SERVICE_NAMES = "blockingServiceNames";
	public static final String MATCHING_SERVICE_NAMES = "matchingServiceNames";
	public static final String RECORD_LINKAGE_PROTOCOL_NAMES = "recordLinkageProtocolNames";
	public static final String ADMIN_CONFIGURATION = "adminConfiguration";

	public static final String WIZARD_MODE = "wizardMode";
	public static final Integer NO_WIZARD_MODE = 0;
	public static final Integer FILE_IMPORT_WIZARD_MODE = 1;
	public static final Integer RECORD_LINKAGE_WIZARD_MODE = 2;
	public static final String EXIT_WIZARD_BUTTON_TEXT = "Cancel Wizard";
	public static final String NEXT_PAGE_WIZARD_BUTTON_TEXT = "Next page";
	public static final String PREVIOUS_PAGE_WIZARD_BUTTON_TEXT = "Previous Page";
	public static final String SAVE_BUTTON_TEXT = "Save";

	public static final Integer PERSON_LINK_PAGE_SIZE = 20;
	public static final Integer MAXIMUM_POINTS_ON_GRAPH = 100;

	public static final String APP_BASE_URL = "/SOEMPI/webapp-web/war/";
	public static final String CHART_URL_POSTFIX = "gxt/chart/open-flash-chart.swf";

    /**
     * The username of the default Administrator user
     */
    public static final String DEFAULT_ADMIN_USERNAME = "admin";

    /**
     * The password of the default Administrator user
     */
    public static final String DEFAULT_ADMIN_PASSWORD = DEFAULT_ADMIN_USERNAME;
}
