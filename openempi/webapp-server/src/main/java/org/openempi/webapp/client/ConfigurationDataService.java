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

import com.google.gwt.user.client.rpc.RemoteService;

public interface ConfigurationDataService extends RemoteService
{
	public List<StringComparatorFunctionWeb> getStringComparatorFunctionList();

	public List<TransformationFunctionWeb> getTransfromationFunctionList();

	public BloomfilterSettingsWeb loadBloomfilterSettings();

	public ComponentSettingsWeb loadComponentSettings();

	public PrivacyPreservingBlockingSettingsWeb loadPrivacyPreservingBlockingSettings();

	public List<MatchFieldWeb> loadMatchFieldConfiguration();

	public MatchConfigurationWeb loadMatchConfigurationParameters();

	public AdminConfigurationWeb loadAdminConfiguration();

	public String saveBloomfilterSettings(BloomfilterSettingsWeb bloomfilterSettings);

	public String saveComponentSettings(ComponentSettingsWeb componentSettings);

	public String savePrivacyPreservingBlockingSettings(PrivacyPreservingBlockingSettingsWeb ppBlockingSettings);

	public String saveMatchFieldConfiguration(List<MatchFieldWeb> matchFields);

	public String saveMatchConfigurationParameters(MatchConfigurationWeb matchConfiguration);

	public String saveAdminConfiguration(AdminConfigurationWeb adminConfiguration);

	public void authenticateUser(String username, String password);
}
