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

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReferenceDataServiceAsync
{
	public void getAllTransformationFunctionNames(AsyncCallback<List<String>> callback);

	public void getComparatorFunctionNames(AsyncCallback<List<String>> callback);

	public void getBlockingServiceNames(AsyncCallback<List<String>> callback);

	public void getMatchingServiceNames(AsyncCallback<List<String>> callback);

	public void getRecordLinkageProtocolNames(AsyncCallback<List<String>> callback);
}
