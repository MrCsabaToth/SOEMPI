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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openhie.openempi.context.Context;

public class NicknameSwapout extends SwapoutBase
{
	private static final Map<String, List<StringTriple>> NAME_TO_NICK = new HashMap<String, List<StringTriple>>();
	private static final Map<String, List<StringTriple>> NICK_TO_NAME = new HashMap<String, List<StringTriple>>();

	private static void initMap(Map<String, List<StringTriple>> map, String filePath) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to read the input file.");
		}
		
		try {
			boolean done = false;
			int lineIndex = 0;
			while (!done) {
				String line = reader.readLine();
				if (line == null) {
					done = true;
					continue;
				}
				if (lineIndex > 0) {
					int firstComma = line.indexOf(',');
					String tail = line.substring(firstComma + 1);
					String head = line.substring(0, firstComma);
					List<String> swapStringList = Arrays.asList(tail.split(","));
					List<StringTriple> swapStringTriples = new ArrayList<StringTriple>();
					for (String s : swapStringList) {
						swapStringTriples.add(new StringTriple(s, true));
					}
					map.put(head, swapStringTriples);
				}
				lineIndex++;
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Failed while loading the input file.");
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to close input file.");
			}
		}
	}

	public static void initMaps() {
		initMap(NAME_TO_NICK, Context.getOpenEmpiHome() + "/conf/name_to_nick.csv");
		initMap(NICK_TO_NAME, Context.getOpenEmpiHome() + "/conf/nick_to_name.csv");
	}

	public static String swapout(String input, Random rnd) {
		if (NAME_TO_NICK.size() == 0) {
			synchronized(NAME_TO_NICK) {
				if (NAME_TO_NICK.size() == 0) {
					initMaps();
				}
			}
		}
		CaseEnum caseType = SwapoutBase.determineCaseType(input);
		String upperCaseInput = input;
		if (caseType != CaseEnum.UpperCase)
			upperCaseInput = input.toUpperCase();
		if (NAME_TO_NICK.containsKey(upperCaseInput))
			return swapoutCore(NAME_TO_NICK.get(upperCaseInput), caseType, rnd);
		if (NICK_TO_NAME.containsKey(upperCaseInput))
			return swapoutCore(NICK_TO_NAME.get(upperCaseInput), caseType, rnd);
		String[] words = input.split(whitespace_charsregexsttr + "+");
		int pos = 0;
		for (int i = 0; i < words.length; i++) {
			upperCaseInput = words[i];
			if (caseType != CaseEnum.UpperCase)
				upperCaseInput = words[i].toUpperCase();
			if (NAME_TO_NICK.containsKey(upperCaseInput))
				return swapoutCore(input, pos, caseType, words[i], NAME_TO_NICK.get(upperCaseInput), rnd);
			if (NICK_TO_NAME.containsKey(upperCaseInput))
				return swapoutCore(input, pos, caseType, words[i], NICK_TO_NAME.get(upperCaseInput), rnd);
			pos += (words[i].length() + 1);
		}
		return input;
	}

}
