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
package org.openempi.webapp.client;

import java.util.List;

import org.openempi.webapp.client.model.AdminConfigurationWeb;
import org.openempi.webapp.client.model.BloomfilterSettingsWeb;
import org.openempi.webapp.client.model.ComponentSettingsWeb;
import org.openempi.webapp.client.model.MatchConfigurationWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.model.StringComparatorFunctionWeb;
import org.openempi.webapp.client.model.TransformationFunctionWeb;
import org.openempi.webapp.client.model.PrivacyPreservingBlockingSettingsWeb;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationDataServiceAsync
{
	public void getStringComparatorFunctionList(AsyncCallback<List<StringComparatorFunctionWeb>> callback);

	public void getTransfromationFunctionList(AsyncCallback<List<TransformationFunctionWeb>> callback);

	public void loadBloomfilterSettings(AsyncCallback<BloomfilterSettingsWeb> callback);

	public void loadComponentSettings(AsyncCallback<ComponentSettingsWeb> callback);

	public void loadPrivacyPreservingBlockingSettings(AsyncCallback<PrivacyPreservingBlockingSettingsWeb> callback);

	public void loadMatchFieldConfiguration(AsyncCallback<List<MatchFieldWeb>> callback);

	public void loadMatchConfigurationParameters(AsyncCallback<MatchConfigurationWeb> callback);

	public void loadAdminConfiguration(AsyncCallback<AdminConfigurationWeb> callback);

	public void saveBloomfilterSettings(BloomfilterSettingsWeb bloomfilterSettings, AsyncCallback<String> callback);

	public void saveComponentSettings(ComponentSettingsWeb componentSettings, AsyncCallback<String> callback);

	public void savePrivacyPreservingBlockingSettings(PrivacyPreservingBlockingSettingsWeb ppBlockingSettings, AsyncCallback<String> callback);

	public void saveMatchFieldConfiguration(List<MatchFieldWeb> matchFields, AsyncCallback<String> callback);

	public void saveMatchConfigurationParameters(MatchConfigurationWeb matchConfiguration, AsyncCallback<String> callback);

	public void saveAdminConfiguration(AdminConfigurationWeb adminConfiguration, AsyncCallback<String> callback);

	public void authenticateUser(String username, String password, AsyncCallback<Void> callback);
}
