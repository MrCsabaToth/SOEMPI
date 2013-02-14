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
package org.openhie.openempi.transformation.function.bloomfilter;

public class NgramSequencer implements NgramIterator
{
	public static final char ngramPrefix = '%';	// prefix, preferably shouldn't occur in the source string
	public static final char ngramPostfix = '$';	// prefix, preferably shouldn't occur in the source string
	
	private StringBuilder source = null;	// for speeding up string manipulations
	private int ngramSize = 0;	// 2 = bigram, 3 = bigram, etc
	private int index = 0;	// which n-gram we are at
	private boolean padding = false;

	public void init(String source, int ngramSize, boolean padding) throws IndexOutOfBoundsException
	{
		if (ngramSize < 1)
			throw new IndexOutOfBoundsException("n-gram size must be bigger than 0!");
		this.source = new StringBuilder();
		for(int i = 0; i < ngramSize - 1; i++)
			this.source.append(ngramPrefix);
		this.source.append(source);
		for(int i = 0; i < ngramSize - 1; i++)
			this.source.append(ngramPostfix);
		this.ngramSize = ngramSize;
		this.padding = padding;
		index = 0;
	}
	
	public boolean hasNext() {
		return (index <= source.length() - ngramSize);
	}

	public String next() {
		int currentindex = index++;
		if (!padding && (currentindex < ngramSize || currentindex > source.length() - 2 * ngramSize + 1)) {
			if (currentindex < ngramSize) {
				return source.substring(ngramSize - 1, ngramSize + currentindex);
			} else  if (currentindex > source.length() - 2 * ngramSize + 1) {
				return source.substring(currentindex, source.length() - ngramSize + 1);
			}
		}
		return source.substring(currentindex, currentindex + ngramSize);
	}

	public void remove() {
		source = null;
	}

}
