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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class BlockingSettingsWeb extends BaseModelData implements Serializable
{
	private static final long serialVersionUID = 6167574464369668270L;

	public static final String NUMBER_OF_RECORDS_TO_SAMPLE = "numberOfRecordsToSample";
	public static final String BLOCKING_FIELDS = "blockingFields";

	public BlockingFieldBaseWeb dummyBlockingField;	// This is need so BlockingFieldBaseWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public BlockingSettingsWeb() {
	}

	public Integer getNumberOfRecordsToSample() {
		return get(NUMBER_OF_RECORDS_TO_SAMPLE);
	}

	public void setNumberOfRecordsToSample(Integer numberOfRecordsToSample) {
		set(NUMBER_OF_RECORDS_TO_SAMPLE, numberOfRecordsToSample);
	}

	public List<BlockingFieldBaseWeb> getBlockingFields() {
		return get(BLOCKING_FIELDS);
	}

	public void setBlockingFields(List<BlockingFieldBaseWeb> blockingFields) {
		set(BLOCKING_FIELDS, blockingFields);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(NUMBER_OF_RECORDS_TO_SAMPLE + ": ").append(getNumberOfRecordsToSample());
		sb.append("," + BLOCKING_FIELDS + ": ").append(getBlockingFields().toString());
		return sb.toString();
	}
}
