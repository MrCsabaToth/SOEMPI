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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OCRError
{
	private static final List<StringTuple> CONST_OCR_ERROR_MAP =
	    Collections.unmodifiableList(new ArrayList<StringTuple>() {{
	    	add(new StringTuple("5", "S"));
	    	add(new StringTuple("5", "s"));
	    	add(new StringTuple("s", "5"));
	    	add(new StringTuple("S", "5"));
	    	add(new StringTuple("2", "Z"));
	    	add(new StringTuple("Z", "2"));
	    	add(new StringTuple("2", "z"));
	    	add(new StringTuple("z", "2"));
	    	add(new StringTuple("1", "|"));
	    	add(new StringTuple("6", "G"));
	    	add(new StringTuple("G", "6"));
	    	add(new StringTuple("g", "9"));
	    	add(new StringTuple("9", "g"));
	    	add(new StringTuple("q", "9"));
	    	add(new StringTuple("9", "q"));
	    	add(new StringTuple("q", "4"));
	    	add(new StringTuple("4", "q"));
	    	add(new StringTuple("B", "8"));
	    	add(new StringTuple("A", "4"));
	    	add(new StringTuple("0", "o"));
	    	add(new StringTuple("o", "0"));
	    	add(new StringTuple("0", "O"));
	    	add(new StringTuple("O", "0"));
	    	add(new StringTuple("m", "n"));
	    	add(new StringTuple("u", "v"));
	    	add(new StringTuple("v", "u"));
	    	add(new StringTuple("U", "V"));
	    	add(new StringTuple("V", "U"));
	    	add(new StringTuple("Y", "V"));
	    	add(new StringTuple("V", "Y"));
	    	add(new StringTuple("y", "v"));
	    	add(new StringTuple("v", "y"));
	    	add(new StringTuple("D", "O"));
	    	add(new StringTuple("Q", "O"));
	    	add(new StringTuple("F", "P"));
	    	add(new StringTuple("E", "F"));
	    	add(new StringTuple("l", "J"));
	    	add(new StringTuple("j", "i"));
	    	add(new StringTuple("i", "j"));
	    	add(new StringTuple("l", "1"));
	    	add(new StringTuple("1", "l"));
	    	add(new StringTuple("g", "q"));
	    	add(new StringTuple("q", "g"));
	    	add(new StringTuple("h", "b"));
	    	add(new StringTuple("b", "h"));
	    	add(new StringTuple("l", "I"));
	    	add(new StringTuple("I", "l"));
	    	add(new StringTuple("i", "'l"));
	    	add(new StringTuple("13", "B"));
	    	add(new StringTuple("12", "R"));
	    	add(new StringTuple("17", "n"));
	    	add(new StringTuple("iii", "m"));
	    	add(new StringTuple("cl", "d"));
	    	add(new StringTuple("w", "vv"));
	    	add(new StringTuple("vv", "w"));
	    	add(new StringTuple("ri", "n"));
	    	add(new StringTuple("k", "lc"));
	    	add(new StringTuple("lc", "k"));
	    	add(new StringTuple("lo", "b"));
	    	add(new StringTuple("b", "lo"));
	    	add(new StringTuple("IJ", "U"));
	    	add(new StringTuple("lJ", "U"));
	    	add(new StringTuple("LI", "U"));
	    	add(new StringTuple("I-I", "H"));
	    	add(new StringTuple("H", "I-I"));
	    	add(new StringTuple("l>", "b"));
	    	add(new StringTuple("1>", "b"));
	    	add(new StringTuple("l<", "k"));
	    	add(new StringTuple("1<", "k"));
	    	add(new StringTuple("m", "rn"));
	    	add(new StringTuple("rn", "m"));
	    	add(new StringTuple("l", "|"));
	    	add(new StringTuple("i", ":"));
	    	add(new StringTuple("'", "\""));
	    	add(new StringTuple("`", "\""));
	    	add(new StringTuple("`", "'"));
	    	add(new StringTuple("'", "`"));
	    	add(new StringTuple("'", "@"));
	    	add(new StringTuple("`", "@"));
	    	add(new StringTuple("|", "l"));
	    	add(new StringTuple("8", "&"));
	    }});

	public static String ocrError(String input, Random rnd) {
		StringBuilder sb = new StringBuilder(input);
		int maxTries = CONST_OCR_ERROR_MAP.size();
		for(int i = 0; i < maxTries; i++) {
			int ocrErrorIndex = rnd.nextInt(maxTries);
			StringTuple tuple = CONST_OCR_ERROR_MAP.get(ocrErrorIndex);
			int substPoint = rnd.nextInt(input.length());
			int index = input.indexOf(tuple.before, substPoint);
			if (index == -1)
				index = input.lastIndexOf(tuple.before, substPoint);
			if (index != -1) {
				int substPoint2 = substPoint + tuple.before.length();
				sb.replace(substPoint, substPoint2, tuple.after);
				break;
			}
		}
		return sb.toString();
	}

}
