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
package org.openempi.webapp.server;

import java.util.HashMap;
import java.util.Map;

import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.model.Person;

public class ModelTransformerTest extends BaseServiceTestCase
{
	public void testMapPersonWebToPerson() {
		PersonWeb personWeb = new PersonWeb();
		personWeb.setPersonId(100L);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("givenName", "John");
		attributes.put("familyName", "Doe");
		attributes.put("address1", "1000 Someplace Drive");
		attributes.put("address2", "Suite 2000");
		attributes.put("city", "Palo Alto");
		attributes.put("state", "CA");
		attributes.put("postalCode", "94301");
		attributes.put("dateOfBirth", new java.util.Date());
		attributes.put("gender", "M");
		personWeb.setAttributes(attributes);
		Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
		log.debug("Transformed object is " + person);
	}
}
