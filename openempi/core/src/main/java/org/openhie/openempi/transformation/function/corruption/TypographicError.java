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

public class TypographicError
{
	public static String insert(String input, char minChar, char maxChar, Random rnd) {
		StringBuilder sb = new StringBuilder(input);
		int insertionPoint = rnd.nextInt(input.length() + 1);
		sb.insert(insertionPoint, getCharacterFromRange(minChar, maxChar, rnd));
		return sb.toString();
	}

	public static char getCharacterFromRange(char minChar, char maxChar, Random rnd) {
		if (minChar == maxChar)
			return minChar;
		return (char)(minChar + (char)rnd.nextInt(maxChar - minChar));
	}
	
	public static String insertNumber(String input, Random rnd) {
		return insert(input, '0', '9', rnd);
	}

	public static String insertLetter(String input, boolean lowerCase, Random rnd) {
		if (lowerCase)
			return insert(input, 'a', 'z', rnd);
		else
			return insert(input, 'A', 'Z', rnd);
	}

	public static String insertLetter(String input, Random rnd) {
		return insertLetter(input, rnd.nextBoolean(), rnd);
	}

	public static String delete(String input, Random rnd) {
		if (input.length() == 0)
			return "";
		StringBuilder sb = new StringBuilder(input);
		int deletionPoint = rnd.nextInt(input.length());
		sb.deleteCharAt(deletionPoint);
		return sb.toString();
	}

	public static String substitute(String input, char minChar, char maxChar, Random rnd) {
		if (input.length() == 0)
			return "";
		StringBuilder sb = new StringBuilder(input);
		int substPoint = rnd.nextInt(input.length());
		sb.setCharAt(substPoint, getCharacterFromRange(minChar, maxChar, rnd));
		return sb.toString();
	}

	public static String substituteNumber(String input, Random rnd) {
		return substitute(input, '0', '9', rnd);
	}

	public static String substituteLetter(String input, boolean lowerCase, Random rnd) {
		if (lowerCase)
			return substitute(input, 'a', 'z', rnd);
		else
			return substitute(input, 'A', 'Z', rnd);
	}

	public static String substituteLetter(String input, Random rnd) {
		return substituteLetter(input, rnd.nextBoolean(), rnd);
	}

	public static String transpose(String input, Random rnd) {	// Only neighbors
		if (input.length() <= 1)
			return input;
		StringBuilder sb = new StringBuilder(input);
		int transpPoint = rnd.nextInt(input.length() - 1);
		char ch = sb.charAt(transpPoint);
		sb.setCharAt(transpPoint, sb.charAt(transpPoint + 1));
		sb.setCharAt(transpPoint + 1, ch);
		return sb.toString();
	}
}
