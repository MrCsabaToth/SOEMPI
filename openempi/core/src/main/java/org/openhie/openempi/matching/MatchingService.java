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
package org.openhie.openempi.matching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonMatch;

public interface MatchingService
{
	/**
	 * The init method is used for initialized of the matching service. Depending on
	 * the implementation of the service, the initialization requirements will vary. An exact
	 * matching service may not need any initialization at all, whereas a probabilistic 
	 * matching service will require considerable initialization.
	 * 
	 * @throws InitializationException
	 */
	public void init(Map<String, Object> configParameters) throws InitializationException;
	
	/**
	 * The isInitialized method returns the current state of the matching service regarding initialization. It
	 * is used to support lazy initialization of a matching service only when it is needed.
	 * 
	 * @return the initialization state of the matching service.
	 */
	public boolean isInitialized();
	
	/**
	 * Is used to set the initialization state of the matching service.
	 * 
	 * @param initialized
	 */
	public void setInitialized(boolean initialized);
	
	/**
	 * The initializeRepository method first removes all the person associations that have been
	 * established in the database between records that belong to the same physical entity and
	 * performs matching of all the records from the beginning. This operation may be very
	 * time consuming and may be destructive in nature. I should only be performed when an
	 * instance of OpenEMPI is first created or when a different matching algorithm is used
	 * or the matching parameters of the matching algorithm are modified.
	 *   
	 * @throws ApplicationException
	 */
	public void initializeRepository() throws ApplicationException;

	/**
	 * This match method takes a person as a parameter and returns all the records that the
	 * given person is linked to by returning them in the form of record pairs. The first record
	 * in each record pair returned is the person passed into the call.
	 * 
	 * @param blockingServiceTypeName: name of the blocking service
	 * @param linkTableName: name of the link table
	 * @param leftTableName: name of the left table
	 * @param person: the Person we search matches for
	 * @return set of records which matches the specified record
	 */
	public Set<LeanRecordPair> match(String blockingServiceTypeName, String leftTableName, String rightTableName,
			Person person) throws ApplicationException;
	
	/**
	 * This method will generate and persist all the record pairs found in the system based
	 * on the matching algorithm implemented by the particular service. This method should only
	 * be run once after the system is loaded with the initial set of person data. This operation
	 * may be time intensive and the amount of time it takes may be considerable depending on the
	 * complexity of the matching algorithm. The FellegiSunterParameters in case of probabilistic matching
	 * can be queried from the MatchingService after the linkRecords.
	 * 
	 * @param blockingServiceTypeName: name of the blocking service
	 * @param matchingServiceTypeName: name of the matching service
	 * @param linkTableName: name of the link table
	 * @param leftTableName: name of the left table
	 * @param rightTableName: name of the right table
	 * @param blockingServiceCustomParameters: custom parameters for different blocking services
	 * @param matchingServiceCustomParameters: custom parameters for different matching services
	 * @param recordPairs: output parameter for record pairs
	 * @param componentType: type of component: DI performs different linkage than PM
	 * @param emOnly: perform only the EM calculations, this is useful for Parameter Manager
	 * @param persistLinks: store links automatically after creating the link table
	 * @return result of matching, for example FellegiSunterParameters in case of probabilistic match
	 * @throws ApplicationException
	 */
	public PersonMatch linkRecords(String blockingServiceTypeName, Object blockingServiceCustomParameters,
			String matchingServiceTypeName, Object matchingServiceCustomParameters,
			String linkTableName, String leftTableName, String rightTableName,
			List<LeanRecordPair> pairs, ComponentType componentType, boolean emOnly,
			boolean storeLinks) throws ApplicationException;
}
