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
package org.openempi.webapp.client.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.resources.client.model.Gender;
import org.openempi.webapp.resources.client.model.State;

public class InputFormData
{
	public static List<Gender> getGenders() {
		List<Gender> genders = new ArrayList<Gender>();
		genders.add(new Gender("Male", "M"));
		genders.add(new Gender("Female", "F"));
		genders.add(new Gender("Other", "O"));
		return genders;
	}
	
	public static List<State> getStates() {
		List<State> states = new ArrayList<State>();
		states.add(new State("AL", "Alabama", "The Heart of Dixie"));
		states.add(new State("AK", "Alaska", "The Land of the Midnight Sun"));
		states.add(new State("AZ", "Arizona", "The Grand Canyon State"));
		states.add(new State("AR", "Arkansas", "The Natural State"));
		states.add(new State("CA", "California", "The Golden State"));
		states.add(new State("CO", "Colorado", "The Mountain State"));
		states.add(new State("CT", "Connecticut", "The Constitution State"));
		states.add(new State("DE", "Delaware", "The First State"));
		states.add(new State("DC", "District of Columbia", "The Nations Capital"));
		states.add(new State("FL", "Florida", "The Sunshine State"));
		states.add(new State("GA", "Georgia", "The Peach State"));
		states.add(new State("HI", "Hawaii", "The Aloha State"));
		states.add(new State("ID", "Idaho", "Famous Potatoes"));
		states.add(new State("IL", "Illinois", "The Prairie State"));
		states.add(new State("IN", "Indiana", "The Hospitality State"));
		states.add(new State("IA", "Iowa", "The Corn State"));
		states.add(new State("KS", "Kansas", "The Sunflower State"));
		states.add(new State("KY", "Kentucky", "The Bluegrass State"));
		states.add(new State("LA", "Louisiana", "The Bayou State"));
		states.add(new State("ME", "Maine", "The Pine Tree State"));
		states.add(new State("MD", "Maryland", "Chesapeake State"));
		states.add(new State("MA", "Massachusetts", "The Spirit of America"));
		states.add(new State("MI", "Michigan", "Great Lakes State"));
		states.add(new State("MN", "Minnesota", "North Star State"));
		states.add(new State("MS", "Mississippi", "Magnolia State"));
		states.add(new State("MO", "Missouri", "Show Me State"));
		states.add(new State("MT", "Montana", "Big Sky Country"));
		states.add(new State("NE", "Nebraska", "Beef State"));
		states.add(new State("NV", "Nevada", "Silver State"));
		states.add(new State("NH", "New Hampshire", "Granite State"));
		states.add(new State("NJ", "New Jersey", "Garden State"));
		states.add(new State("NM", "New Mexico", "Land of Enchantment"));
		states.add(new State("NY", "New York", "Empire State"));
		states.add(new State("NC", "North Carolina", "First in Freedom"));
		states.add(new State("ND", "North Dakota", "Peace Garden State"));
		states.add(new State("OH", "Ohio", "The Heart of it All"));
		states.add(new State("OK", "Oklahoma", "Oklahoma is OK"));
		states.add(new State("OR", "Oregon", "Pacific Wonderland"));
		states.add(new State("PA", "Pennsylvania", "Keystone State"));
		states.add(new State("RI", "Rhode Island", "Ocean State"));
		states.add(new State("SC", "South Carolina", "Nothing Could be Finer"));
		states.add(new State("SD", "South Dakota", "Great Faces, Great Places"));
		states.add(new State("TN", "Tennessee", "Volunteer State"));
		states.add(new State("TX", "Texas", "Lone Star State"));
		states.add(new State("UT", "Utah", "Salt Lake State"));
		states.add(new State("VT", "Vermont", "Green Mountain State"));
		states.add(new State("VA", "Virginia", "Mother of States"));
		states.add(new State("WA", "Washington", "Green Tree State"));
		states.add(new State("WV", "West Virginia", "Mountain State"));
		states.add(new State("WI", "Wisconsin", "Americas Dairyland"));
		states.add(new State("WY", "Wyoming", "Like No Place on Earth"));
		return states;
	}

}
