/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
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
package org.openhie.openempi.ejb.person;

import java.util.List;

import javax.ejb.Remote;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Person;

@Remote
public interface PersonQueryService
{
	public Person getPersonById(String sessionKey, String tableName, long personId) throws ApplicationException;

	public List<Person> getPersonsByIds(String sessionKey, String tableName, List<Long> personIds) throws ApplicationException;

	public List<Person> getPersonsByExample(String sessionKey, String tableName, Person person) throws ApplicationException;
	
	public List<Person> getPersonsByExamplePaged(String sessionKey, String tableName, Person person,
			long firstResult, int maxResults) throws ApplicationException;

	public List<Person> getPersonsPaged(String sessionKey, String tableName, long firstResult, int maxResults) throws ApplicationException;
}
