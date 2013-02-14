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
package org.openhie.openempi.configuration;

public interface ConfigurationRegistry
{
	public final static String CUSTOM_FIELDS_MAP = "customFieldsMap";
	public final static String CUSTOM_FIELDS_LIST = "customFieldsList";
	public final static String DATA_LOADER_CONFIGURATION = "dataLoaderConfiguration";
	public final static String RECORD_LINKAGE_PROTOCOL_SETTINGS = "recordLinkageProtocolSettings";
	public final static String PRIVACY_PRESERVING_BLOCKING_SETTINGS = "privacyPreservingBlockingSettings";
	public final static String ADMIN_CONFIGURATION = "adminConfiguration";
	
	/**
	 * Used to lookup a configuration entry using a key. Extension components may
	 * register a new configuration entry that specifically supports the configuration
	 * options needed by the component and then lookup this entry during runtime
	 * using this interface.
	 *  
	 * @param key Should uniquely identify the configuration entry
	 * @return
	 */
	public Object lookupConfigurationEntry(String key);
	
	/**
	 * Register a new configuration entry for a component and make it available for
	 * the other modules that make up the component during runtime.
	 *  
	 * @param key A string that uniquely identifies the configuration entry
	 * @param entry The actual configuration entry object itself.
	 */
	public void registerConfigurationEntry(String key, Object entry);
}
