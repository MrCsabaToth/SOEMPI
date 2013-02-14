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

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;
import org.openhie.openempi.model.Person;

@Stateless(name="PersonQueryService")
@Interceptors ({SpringInjectionInterceptor.class})
public class PersonQueryServiceBean extends BaseSpringInjectableBean implements PersonQueryService
{
	private static final long serialVersionUID = 3411779674961698120L;

	public Person getPersonById(String sessionKey, String tableName, long identifier) throws ApplicationException {
		log.trace("In getPersonById method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService =
				Context.getPersonQueryService();
		return personService.getPersonById(tableName, identifier);
	}

	public List<Person> getPersonsByIds(String sessionKey, String tableName, List<Long> personIds) throws ApplicationException
	{
		log.trace("In getPersonsByIds method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService =
				Context.getPersonQueryService();
		return personService.getPersonsByIds(tableName, personIds);		
	}

	public List<Person> getPersonsByExample(String sessionKey, String tableName, Person person) throws ApplicationException {
		log.trace("In getPersonsByExample method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService =
				Context.getPersonQueryService();
		return personService.getPersonsByExample(tableName, person);
	}
	
	public List<Person> getPersonsByExamplePaged(String sessionKey, String tableName, Person person,
			long firstResult, int maxResults) throws ApplicationException
	{
		log.trace("In getPersonsByExamplePaged method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService =
				Context.getPersonQueryService();
		return personService.getPersonsByExamplePaged(tableName, person, firstResult, maxResults);		
	}

	public List<Person> getPersonsPaged(String sessionKey, String tableName, long firstResult, int maxResults) throws ApplicationException
	{
		log.trace("In getPersonsPaged method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService =
				Context.getPersonQueryService();
		return personService.getPersonsPaged(tableName, firstResult, maxResults);		
	}
}
