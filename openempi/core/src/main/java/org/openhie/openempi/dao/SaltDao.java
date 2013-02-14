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

import java.util.List;

import org.openhie.openempi.model.Salt;
import org.springframework.transaction.annotation.Transactional;

public interface SaltDao extends UniversalDao
{
	/**
	 * Add a Salt entity to the salt table
	 * 
	 * @param salt: the salt entity to be added
	 * @return The freshly added salt entity
	 */
	public void addSalt(Salt salt);
	
	/**
	 * Query a Salt entity from the table by a given salt identifier
	 * 
	 * @param saltId: identifier of the salt entity to be returned
	 * @return The salt entity with the specified id, null if not found
	 */
	public Salt getSalt(long saltId);
	
	/**
	 * Query a Salt entities from the table within a given salt identifier range
	 * 
	 * @param startId: identifier of the first salt entity to be returned
	 * @param endId: identifier of the last salt entity to be returned
	 * @return The salt entities within the specified id range
	 */
	@Transactional
	public List<Salt> getSalts(long startId, long endId);

	/**
	 * Delete a Slat entity from the salt table
	 * 
	 * @param salt: the salt entity to be added
	 */
	public void deleteSalt(Salt salt);
}
