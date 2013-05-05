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

import java.util.List;
import java.util.Random;

public abstract class SwapoutBase
{
	public static final String whitespace_chars =
              "\\u0009" // CHARACTER TABULATION
            + "\\u000A" // LINE FEED (LF)
            + "\\u000B" // LINE TABULATION
            + "\\u000C" // FORM FEED (FF)
            + "\\u000D" // CARRIAGE RETURN (CR)
            + "\\u0020" // SPACE
            + "\\u0085" // NEXT LINE (NEL) 
            + "\\u00A0" // NO-BREAK SPACE
            + "\\u1680" // OGHAM SPACE MARK
            + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
            + "\\u2000" // EN QUAD 
            + "\\u2001" // EM QUAD 
            + "\\u2002" // EN SPACE
            + "\\u2003" // EM SPACE
            + "\\u2004" // THREE-PER-EM SPACE
            + "\\u2005" // FOUR-PER-EM SPACE
            + "\\u2006" // SIX-PER-EM SPACE
            + "\\u2007" // FIGURE SPACE
            + "\\u2008" // PUNCTUATION SPACE
            + "\\u2009" // THIN SPACE
            + "\\u200A" // HAIR SPACE
            + "\\u2028" // LINE SEPARATOR
            + "\\u2029" // PARAGRAPH SEPARATOR
            + "\\u202F" // NARROW NO-BREAK SPACE
            + "\\u205F" // MEDIUM MATHEMATICAL SPACE
            + "\\u3000";// IDEOGRAPHIC SPACE
	public static final String whitespace_charsregexsttr = "[" + whitespace_chars + "]";
	public static final String non_whitespace_charsregexsttr = "[^" + whitespace_chars + "]";

	public enum CaseEnum {
		UpperCase,
		LowerCase,
		CamelCase,
		Unknown
	}

	protected static String swapoutCore(List<StringTriple> swapouts, CaseEnum caseType, Random rnd) {
		if (swapouts.size() == 1) {
			return swapouts.get(0).getCase(caseType);
		} else {
			int index = rnd.nextInt(swapouts.size());
			return swapouts.get(index).getCase(caseType);
		}		
	}
	
	protected static String swapoutCore(String input, int pos, CaseEnum caseType, String word, List<StringTriple> swapouts, Random rnd) {
		StringBuilder sb = new StringBuilder(input);
		int index = input.indexOf(word, pos);
		if (index > 0) {
			String after = swapoutCore(swapouts, caseType, rnd);
			sb.replace(index, index + word.length(), after);
		}
		return sb.toString();
	}
	
	public static CaseEnum determineCaseType(String input) {
		if (input == null || input.length() == 0)
			return CaseEnum.Unknown;
		boolean startsWithUpperCase = Character.isUpperCase(input.charAt(0));
		boolean endsWithUpperCase = Character.isUpperCase(input.charAt(input.length() - 1));
		if (startsWithUpperCase && endsWithUpperCase)
			return CaseEnum.UpperCase;
		if (!startsWithUpperCase && !endsWithUpperCase)
			return CaseEnum.LowerCase;
		if (startsWithUpperCase && !endsWithUpperCase)
			return CaseEnum.CamelCase;
		return CaseEnum.Unknown;
	}
	
	public static String convertStringToCaseType(String input, CaseEnum caseType) {
		StringBuilder sb = null;
		switch (caseType) {
		case UpperCase: sb = new StringBuilder(input.toUpperCase()); break;
		case LowerCase: sb = new StringBuilder(input.toLowerCase()); break;
		case CamelCase: {
				sb = new StringBuilder(input.toLowerCase());
				sb.setCharAt(0, Character.toUpperCase(input.charAt(0)));
			}
			break;
		case Unknown: sb = new StringBuilder(input); break;
		}
		return sb.toString();
	}
}
