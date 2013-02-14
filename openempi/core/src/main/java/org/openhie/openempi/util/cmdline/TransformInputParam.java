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

import org.openhie.openempi.transformation.function.DoubleMetaphoneFunction;
import org.openhie.openempi.transformation.function.MetaphoneFunction;
import org.openhie.openempi.transformation.function.RefinedSoundexFunction;
import org.openhie.openempi.transformation.function.SoundexFunction;
import org.openhie.openempi.transformation.function.TransformationFunction;

public class TransformInputParam
{
	public TransformInputParam() {
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
			System.exit(-1);
		}
		String trafoName = args[0];
		String value = args[1];

		TransformationFunction trafo = null;
		if (trafoName.equals("DoubleMetaphone")) {
			trafo = new DoubleMetaphoneFunction();
		} else if (trafoName.equals("Metaphone")) {
			trafo = new MetaphoneFunction();
		} else if (trafoName.equals("Soundex")) {
			trafo = new SoundexFunction();
		} else if (trafoName.equals("RefinedSoundex")) {
			trafo = new RefinedSoundexFunction();
		}
		Object result = trafo.transform(value, null); 
		System.out.print(result);
	}
	
	public static void usage() {
		System.out.println("Usage: " + TransformInputParam.class.getName() + " <transformation-function-name> <value-to-transform>");
		System.out.println("Transformation function names: DoubleMetaphone, Metaphone, Soundex, RefinedSoundex");
	}

}
