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
package org.openhie.openempi.util.cmdline;

import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.util.SerializationUtil;

public class DisplayFellegiSunterParameters
{
	public DisplayFellegiSunterParameters() {
	}
	
	private static void loadFile(String filename) {
		FellegiSunterParameters fellegiSunterParameters = (FellegiSunterParameters)SerializationUtil.deserializeObject(filename);
		System.out.println("Field count: " + fellegiSunterParameters.getFieldCount());
		System.out.println("Vector count: " + fellegiSunterParameters.getVectorCount());
		System.out.println("Vector frequencies:");
		long[] vectorFrequencies = fellegiSunterParameters.getVectorFrequencies();
		for(int i = 0; i < fellegiSunterParameters.getVectorCount(); i++)
			System.out.println(vectorFrequencies[i]);
		System.out.println("Lambda: " + fellegiSunterParameters.getLambda());
		System.out.println("Mu: " + fellegiSunterParameters.getMu());
		System.out.println("Lower bound: " + fellegiSunterParameters.getLowerBound());
		System.out.println("Upper bound: " + fellegiSunterParameters.getUpperBound());
		System.out.println("m and u values:");
		for(int i = 0; i < fellegiSunterParameters.getFieldCount(); i++) {
			System.out.println(i + ". field: m=" + fellegiSunterParameters.getMValue(i) + ", u=" + fellegiSunterParameters.getUValue(i));
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		String filename = args[0];
		System.out.println("Displaying the Fellegi Sunter Parameters file " + filename);

		loadFile(filename);
	}
	
	public static void usage() {
		System.out.println("Usage: " + DisplayFellegiSunterParameters.class.getName() + " <file-to-display>");
	}

}
