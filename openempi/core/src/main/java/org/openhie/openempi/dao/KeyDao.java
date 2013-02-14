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
package org.openhie.openempi.dao;

import org.openhie.openempi.model.Key;

public interface KeyDao extends UniversalDao
{
	/**
	 * Add a Key entity to the key table
	 * 
	 * @param key: the key entity to be added
	 * @return The freshly added key entity
	 */
	public Key addKey(Key key);
	
	/**
	 * Query a Key entity from the table by a given key identifier
	 * 
	 * @param keyId: identifier of the key entity to be returned
	 * @return The key entity with the specified id, null if not found
	 */
	public Key getKey(long keyId);

	/**
	 * Delete a Key entity from the key table
	 * 
	 * @param key: the key entity to be deleted
	 */
	public void deleteKey(Key key);
}
