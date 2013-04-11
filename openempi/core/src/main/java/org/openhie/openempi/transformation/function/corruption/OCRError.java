package org.openhie.openempi.transformation.function.corruption;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OCRError
{
	private static final Map<String, String> CONST_OCR_ERROR_MAP =
	    Collections.unmodifiableMap(new HashMap<String, String>() {{
	    	put("5", "S");
	    	put("5", "s");
	    	put("s", "5");
	    	put("S", "5");
	    	put("2", "Z");
	    	put("Z", "2");
	    	put("2", "z");
	    	put("z", "2");
	    	put("1", "|");
	    	put("6", "G");
	    	put("G", "6");
	    	put("g", "9");
	    	put("9", "g");
	    	put("q", "9");
	    	put("9", "q");
	    	put("q", "4");
	    	put("4", "q");
	    	put("B", "8");
	    	put("A", "4");
	    	put("0", "o");
	    	put("o", "0");
	    	put("0", "O");
	    	put("O", "0");
	    	put("m", "n");
	    	put("u", "v");
	    	put("v", "u");
	    	put("U", "V");
	    	put("V", "U");
	    	put("Y", "V");
	    	put("V", "Y");
	    	put("y", "v");
	    	put("v", "y");
	    	put("D", "O");
	    	put("Q", "O");
	    	put("F", "P");
	    	put("E", "F");
	    	put("l", "J");
	    	put("j", "i");
	    	put("i", "j");
	    	put("l", "1");
	    	put("1", "l");
	    	put("g", "q");
	    	put("q", "g");
	    	put("h", "b");
	    	put("b", "h");
	    	put("l", "I");
	    	put("I", "l");
	    	put("i", "'l");
	    	put("13", "B");
	    	put("12", "R");
	    	put("17", "n");
	    	put("iii", "m");
	    	put("cl", "d");
	    	put("w", "vv");
	    	put("vv", "w");
	    	put("ri", "n");
	    	put("k", "lc");
	    	put("lc", "k");
	    	put("lo", "b");
	    	put("b", "lo");
	    	put("IJ", "U");
	    	put("lJ", "U");
	    	put("LI", "U");
	    	put("I-I", "H");
	    	put("H", "I-I");
	    	put("l>", "b");
	    	put("1>", "b");
	    	put("l<", "k");
	    	put("1<", "k");
	    	put("m", "rn");
	    	put("rn", "m");
	    	put("l", "|");
	    	put("i", ":");
	    	put("'", "\"");
	    	put("`", "\"");
	    	put("`", "'");
	    	put("'", "`");
	    	put("'", "@");
	    	put("`", "@");
	    	put("|", "l");
	    	put("8", "&");
	    }});

	public static String ocrError(String input, Random rnd) {
		StringBuilder sb = new StringBuilder(input);
		int maxTries = 20;
		for(int i = 0; i < maxTries; i++) {
			int substPoint = rnd.nextInt(input.length());
			String before = null;
			String after = null;
			int substPoint2 = substPoint + 1;
			before = Character.toString(input.charAt(substPoint));
			after = CONST_OCR_ERROR_MAP.get(before);
			if (after == null) {
				if (substPoint < input.length() - 1) {	// try to replace a 2 character combination
					substPoint2++;
					before = input.substring(substPoint, substPoint2);
					after = CONST_OCR_ERROR_MAP.get(before);
					if (after == null) {
						if (substPoint < input.length() - 2) {	// try to replace a 3 character combination
							substPoint2++;
							before = input.substring(substPoint, substPoint2);
							after = CONST_OCR_ERROR_MAP.get(before);
						}
					}
				}
			}
			if (after != null) {
				sb.replace(substPoint, substPoint2, after);
				break;
			}
		}
		return sb.toString();
	}

}
