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

import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ComparisonVectorTest extends BaseServiceTestCase
{
	public void testComparisonVector() {
		MatchField[] fields = new MatchField[4];
		fields[0] = new MatchField();
		fields[0].setLeftFieldName("givenName");
		fields[0].setRightFieldName("givenName");
		fields[1] = new MatchField();
		fields[1].setLeftFieldName("familyName");
		fields[1].setRightFieldName("familyName");
		fields[2] = new MatchField();
		fields[2].setLeftFieldName("phoneNumber");
		fields[2].setRightFieldName("phoneNumber");
		fields[3] = new MatchField();
		fields[3].setLeftFieldName("city");
		fields[3].setRightFieldName("city");
		fields[0].setMatchThreshold(0.725f);
		fields[1].setMatchThreshold(0.725f);
		fields[2].setMatchThreshold(0.725f);
		fields[3].setMatchThreshold(0.725f);
		
		ComparisonVector vector = new ComparisonVector(fields);
		System.out.println("Initialized the vector as: " + vector);
		
		// The following should build the vector [1 0 1 0] given the thresholds above
		vector.setScore(0, 0.5);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.3);
		vector.setScore(3, 0.9);
		
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(10, vector.getBinaryVectorValue());
		
		vector.setScore(0, 0.8);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.8);
		vector.setScore(3, 0.5);
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(7, vector.getBinaryVectorValue());
	}
	
	public void testComparisonVectorAsList() {
		java.util.List<MatchField> fields = new java.util.ArrayList<MatchField>();
		MatchField m = new MatchField();
		m.setLeftFieldName("givenName");
		m.setRightFieldName("givenName");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setLeftFieldName("familyName");
		m.setRightFieldName("familyName");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setLeftFieldName("phoneNumber");
		m.setRightFieldName("phoneNumber");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setLeftFieldName("city");
		m.setRightFieldName("city");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		
		ComparisonVector vector = new ComparisonVector(fields);
		System.out.println("Initialized the vector as: " + vector);
		
		// The following should build the vector [1 0 1 0] given the thresholds above
		vector.setScore(0, 0.5);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.3);
		vector.setScore(3, 0.9);
		
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(10, vector.getBinaryVectorValue());
		
		vector.setScore(0, 0.8);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.8);
		vector.setScore(3, 0.5);
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(7, vector.getBinaryVectorValue());
	}
}
