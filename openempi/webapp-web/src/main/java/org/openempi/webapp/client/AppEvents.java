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

import com.extjs.gxt.ui.client.event.EventType;

public class AppEvents
{
	public static final EventType AddPersonComplete = new EventType();
	
	public static final EventType AddPersonInitiate = new EventType();
	
	public static final EventType AdvancedSearchInitiate = new EventType();
	
	public static final EventType AdvancedSearchRenderData = new EventType();
	
	public static final EventType AdvancedSearchView = new EventType();

	public static final EventType AddPersonView = new EventType();

	public static final EventType BasicSearchInitiate = new EventType();

	public static final EventType BasicSearchRenderData = new EventType();

	public static final EventType BasicSearchView = new EventType();

	public static final EventType RealSearchView = new EventType();

	public static final EventType RealSearchInitiate = new EventType();

	public static final EventType RealSearchRenderData = new EventType();

	public static final EventType BlockingConfigurationReceived = new EventType();

	public static final EventType BlockingConfigurationSave = new EventType();

	public static final EventType BlockingConfigurationView = new EventType();

	public static final EventType BlockingConfigurationRequest = new EventType();

	public static final EventType CustomFieldsConfigurationReceived = new EventType();

	public static final EventType CustomFieldsConfigurationRequest = new EventType();

	public static final EventType CustomFieldsConfigurationSave = new EventType();

	public static final EventType CustomFieldsConfigurationView = new EventType();

	public static final EventType Error = new EventType();

	public static final EventType FileEntryRemove = new EventType();

	public static final EventType FileEntryPreImport = new EventType();

	public static final EventType FileEntryImport = new EventType();

	public static final EventType FileEntryPostImport = new EventType();

	public static final EventType FileEntryDelete = new EventType();

	public static final EventType DatasetSend = new EventType();

	public static final EventType AfterDatasetSend = new EventType();

	public static final EventType DatasetListView = new EventType();

	public static final EventType DatasetListRender = new EventType();

	public static final EventType DatasetListUpdateForImport = new EventType();

	public static final EventType DatasetListSaveToFile = new EventType();

	public static final EventType DatasetListShowColumnsRequest = new EventType();

	public static final EventType DatasetListColumnInformationsArrived = new EventType();

	public static final EventType FileLoaderConfigNeedsKeyServerRequest = new EventType();

	public static final EventType FileLoaderConfigNeedsKeyServerResponse = new EventType();

	public static final EventType FileLoaderConfigurationView = new EventType();

	public static final EventType FileLoaderConfigurationSave = new EventType();

	public static final EventType FileLoaderConfigurationReceived = new EventType();

	public static final EventType FileLoaderConfigurationRequest = new EventType();

	public static final EventType Init = new EventType();

	public static final EventType Login = new EventType();

	public static final EventType Logout = new EventType();

	public static final EventType AuthenticateWithCredentials = new EventType();

	public static final EventType MatchFieldConfigurationReceived = new EventType();

	public static final EventType MatchFieldConfigurationRequest = new EventType();

	public static final EventType MatchFieldConfigurationSave = new EventType();

	public static final EventType MatchFieldConfigurationView = new EventType();

	public static final EventType MatchConfigurationParametersReceived = new EventType();

	public static final EventType MatchConfigurationParametersRequest = new EventType();

	public static final EventType MatchConfigurationParametersSave = new EventType();

	public static final EventType MatchConfigurationParametersView = new EventType();

	public static final EventType MatchFieldComparisonFunctionParametersEdited = new EventType();

	public static final EventType MatchView = new EventType();

	public static final EventType MatchInitiate = new EventType();

	public static final EventType MatchRenderData = new EventType();

	public static final EventType PersonMatchShowMatchColumnsRequest = new EventType();

	public static final EventType PersonMatchColumnMatchInformationsArrived = new EventType();

	public static final EventType PersonMatchShowEMResultsRequest = new EventType();

	public static final EventType PersonMatchFellegiSunterParametersArrived = new EventType();

	public static final EventType PersonMatchShowScoreChartRequest = new EventType();

	public static final EventType PersonMatchScoreDataArrived = new EventType();

	public static final EventType PersonMatchShowRecordPairListRequest = new EventType();

	public static final EventType PersonMatchRecordPairListArrived = new EventType();

	public static final EventType PersonMatchShowPersonAttributesRequest = new EventType();

	public static final EventType PersonMatchPersonAttributesArrived = new EventType();

	public static final EventType DatasetSelectionView = new EventType();

	public static final EventType LeftDatasetSelected = new EventType();

	public static final EventType LeftDatasetColumnNamesArrived = new EventType();

	public static final EventType RightDatasetSelected = new EventType();

	public static final EventType RightDatasetColumnNamesArrived = new EventType();

	public static final EventType DatasetTableNameRefreshRequest = new EventType();

	public static final EventType DatasetTableNamesArrived = new EventType();

	public static final EventType NavMail = new EventType();

	public static final EventType NavTasks = new EventType();

	public static final EventType NavContacts = new EventType();

	public static final EventType AllTransformationFunctionNamesReceived = new EventType();

	public static final EventType ComparatorFunctionNamesReceived = new EventType();

	public static final EventType BlockingServiceNamesReceived = new EventType();

	public static final EventType MatchingServiceNamesReceived = new EventType();

	public static final EventType RecordLinkageProtocolNamesReceived = new EventType();

	public static final EventType AdminConfigurationReceived = new EventType();

	public static final EventType AdminConfigurationToSave = new EventType();

	public static final EventType FileImportWizardSelected = new EventType();

	public static final EventType RecordLinkageWizardSelected = new EventType();
  
	public static final EventType ExitWizardSelected = new EventType();

	public static final EventType WizardEnded = new EventType();

	public static final EventType TransformationFunctionNamesReceived = new EventType();

	public static final EventType PrivacyPreservingBlockingConfigurationView = new EventType();

	public static final EventType PrivacyPreservingBlockingConfigurationSave = new EventType();

	public static final EventType PrivacyPreservingBlockingConfigurationReceived = new EventType();

	public static final EventType PrivacyPreservingBlockingConfigurationRequest = new EventType();

	public static final EventType FileLoaderDataFieldTrafoParametersEdited = new EventType();

	public static final EventType FileLoaderSubFieldTrafoParametersEdited = new EventType();

	public static final EventType BloomfilterSettingsView = new EventType();

	public static final EventType BloomfilterSettingsSave = new EventType();

	public static final EventType BloomfilterSettingsReceived = new EventType();

	public static final EventType BloomfilterSettingsRequest = new EventType();

	public static final EventType ComponentSettingsView = new EventType();

	public static final EventType ComponentSettingsSave = new EventType();

	public static final EventType ComponentSettingsReceived = new EventType();

	public static final EventType ComponentSettingsRequest = new EventType();

	public static final EventType KeyManagementView = new EventType();
  
	public static final EventType KeyManagementAddInitiate = new EventType();

	public static final EventType KeyManagementAddRenderData = new EventType();

	public static final EventType SaltManagementView = new EventType();
  
	public static final EventType SaltManagementAddInitiate = new EventType();

	public static final EventType SaltManagementAddRenderData = new EventType();

}
