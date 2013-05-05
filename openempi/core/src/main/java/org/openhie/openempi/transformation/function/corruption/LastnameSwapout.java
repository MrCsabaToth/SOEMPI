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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.openhie.openempi.context.Context;

public class LastnameSwapout extends SwapoutBase
{
	private static List<LastNameListElement> LASTNAMES = new ArrayList<LastNameListElement>();

	private static void initList() {
		String filePath = Context.getOpenEmpiHome() + "/conf/lastnames.csv";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to read the input file.");
		}
		
		try {
			boolean done = false;
			int lineIndex = 0;
			double total = 0.0;
			while (!done) {
				String line = reader.readLine();
				if (line == null) {
					done = true;
					continue;
				}
				if (lineIndex > 0) {
					int commaPos = line.indexOf(',');
					String tail = line.substring(commaPos + 1);
					String head = line.substring(0, commaPos);
					int weight = Integer.parseInt(tail);
					total += weight;
					LastNameListElement lnle = new LastNameListElement(head, total, 0.0);
					LASTNAMES.add(lnle);
				}
				lineIndex++;
			}
			double lastTotal = 0.0;
			for (LastNameListElement lnle : LASTNAMES) {
				lnle.higherBound = lnle.lowerBound / total;
				lnle.lowerBound = lastTotal;
				lastTotal = lnle.higherBound;
			}
			// Correcting last element for whatever reason
			LASTNAMES.get(LASTNAMES.size() - 1).higherBound = 1.0;
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

	public static String swapoutCore(Double d, CaseEnum caseType, Random rnd) {
		if (LASTNAMES.size() == 0) {
			synchronized(LASTNAMES) {
				if (LASTNAMES.size() == 0) {
					initList();
				}
			}
		}
		LastNameListElement searchElement = new LastNameListElement(null, d, 1.0);
		int index = Collections.binarySearch(LASTNAMES, searchElement, new LastNameListElementComparator());
		if (index < 0 || index >= LASTNAMES.size())
			index = rnd.nextInt(LASTNAMES.size());
		return LASTNAMES.get(index).name.getCase(caseType);
	}

	public static String swapout(CaseEnum caseType, Random rnd) {
		return swapoutCore(rnd.nextDouble(), caseType, rnd);
	}

}
