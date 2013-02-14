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
package org.openhie.openempi.ejb.service;

import javax.ejb.Remote;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Key;

@Remote
public interface KeyManagerService
{
	public Key addKey(String sessionKey) throws ApplicationException;
	
	public void deleteKey(String sessionKey, long keyId) throws ApplicationException;
	
	public Key getKey(String sessionKey, long keyId) throws ApplicationException;
}
