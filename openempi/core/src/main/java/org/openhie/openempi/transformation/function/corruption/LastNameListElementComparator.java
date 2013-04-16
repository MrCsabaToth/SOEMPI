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

import java.util.Comparator;

public class LastNameListElementComparator implements Comparator<LastNameListElement> {
    public LastNameListElementComparator() {
        super();
    }

    private int compareTo(Double d, LastNameListElement e) {
    	if (d < e.lowerBound)
    		return -1;
    	if (d > e.higherBound)
    		return 1;
    	return 0;
    }

    public int compare(LastNameListElement one, LastNameListElement two) {
    	if (one.name == null)
    		return compareTo(one.lowerBound, two);
    	if (two.name == null)
    		return compareTo(two.lowerBound, one);
        return Double.valueOf(one.lowerBound).compareTo(two.lowerBound);
    }
}
