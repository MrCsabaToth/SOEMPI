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
package org.openhie.openempi.transformation.function.corruption;

import java.util.Random;

import junit.framework.TestCase;

public class TypographicErrortestTest extends TestCase
{
	public void testInsertWithEmptyInput() {
		Random rnd = new Random();
		String output = TypographicError.insert("", 'b', 'b', rnd);
		assertEquals(output.length(), 1);
		assertEquals(output, "b");
	}

	public void testInsertWithOneCharInput() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insert("a", 'b', 'b', rnd);
			assertEquals(output.length(), 2);
			assertEquals(output.contains("b"), true);
			if (output.startsWith("a"))
				assertEquals(output, "ab");
			if (output.startsWith("b"))
				assertEquals(output, "ba");
		}
	}
	
	public void testInsertWithTwoCharInput() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insert("aa", 'b', 'b', rnd);
			assertEquals(output.length(), 3);
			assertEquals(output.contains("b"), true);
			if (output.startsWith("aa"))
				assertEquals(output, "aab");
			else if (output.startsWith("a"))
				assertEquals(output, "aba");
			else
				assertEquals(output, "baa");
		}
	}
	
	public void testInsertWithOneCharInput2() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insert("a", 'b', 'z', rnd);
			assertEquals(output.length(), 2);
			assertTrue(output.contains("a"));
		}
	}

	public void testInsertLongInput() {
		Random rnd = new Random();
		String input = "aaaaaaaaaaaaaaaaaaaa";
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insert(input, 'A', 'Z', rnd);
			assertEquals(output.length(), input.length() + 1);
		}
	}
	
	public void testInsertWithEmptyInputForProperCharacterInsertion1() {
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
		String output = TypographicError.insert("", 'b', 'z', rnd);
			assertEquals(output.length(), 1);
			assertFalse(output.contains("a"));
		}
	}

	public void testInsertWithEmptyInputForProperCharacterInsertion2() {
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
		String output = TypographicError.insert("", 'a', 'y', rnd);
			assertEquals(output.length(), 1);
			assertFalse(output.contains("z"));
		}
	}

	public void testGetCharacterFromRange1() {
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
			char c = TypographicError.getCharacterFromRange('b', 'z', rnd);
			assertFalse('a' == c);
		}
	}

	public void testGetCharacterFromRange2() {
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
			char c = TypographicError.getCharacterFromRange('a', 'y', rnd);
			assertFalse('z' == c);
		}
	}

	public void testInsertNumber() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insertNumber("", rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isDigit(output.charAt(0)));
		}
	}

	public void testInsertLetter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insertLetter("", rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isLetter(output.charAt(0)));
		}
	}

	public void testInsertLowercaseLetter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insertLetter("", true, rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isLetter(output.charAt(0)));
			assertTrue(Character.isLowerCase(output.charAt(0)));
		}
	}

	public void testInsertUppercaseLetter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.insertLetter("", false, rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isLetter(output.charAt(0)));
			assertTrue(Character.isUpperCase(output.charAt(0)));
		}
	}

	public void testDeleteFromThreeCharacters() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.delete("aaa", rnd);
			assertEquals(output.length(), 2);
			assertEquals("aa", output);
		}
	}

	public void testDeleteFromOneCharacter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.delete("a", rnd);
			assertEquals(output.length(), 0);
			assertEquals("", output);
		}
	}

	public void testDeleteFromEmptyString() {
		Random rnd = new Random();
		String output = TypographicError.delete("", rnd);
		assertEquals(output.length(), 0);
		assertEquals("", output);
	}

	public void testSubstituteWithEmptyString() {
		Random rnd = new Random();
		String output = TypographicError.substitute("", 'a', 'z', rnd);
		assertEquals(output.length(), 0);
		assertEquals("", output);
	}

	public void testSubstituteWithOneCharString() {
		Random rnd = new Random();
		String output = TypographicError.substitute("a", 'b', 'b', rnd);
		assertEquals(output.length(), 1);
		assertEquals("b", output);
	}

	public void testSubstituteWithTwoCharInput() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.substitute("aa", 'b', 'b', rnd);
			assertEquals(output.length(), 2);
			assertEquals(output.contains("b"), true);
			if (output.startsWith("a"))
				assertEquals(output, "ab");
			if (output.startsWith("b"))
				assertEquals(output, "ba");
		}
	}
	
	public void testSubstituteWithThreeCharInput() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.substitute("aaa", 'b', 'b', rnd);
			assertEquals(output.length(), 3);
			assertEquals(output.contains("b"), true);
			if (output.startsWith("aa"))
				assertEquals(output, "aab");
			else if (output.startsWith("a"))
				assertEquals(output, "aba");
			else
				assertEquals(output, "baa");
		}
	}
	
	public void testSubstituteNumber() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.substituteNumber("a", rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isDigit(output.charAt(0)));
		}
	}

	public void testSubstituteLowercaseLetter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.substituteLetter("1", true, rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isLetter(output.charAt(0)));
			assertTrue(Character.isLowerCase(output.charAt(0)));
		}
	}

	public void testSubstituteUppercaseLetter() {
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			String output = TypographicError.substituteLetter("1", false, rnd);
			assertEquals(output.length(), 1);
			assertTrue(Character.isLetter(output.charAt(0)));
			assertTrue(Character.isUpperCase(output.charAt(0)));
		}
	}

	//transpose
	
}
