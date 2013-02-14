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
package org.openhie.openempi.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.model.NamePairValuePair;
import org.openhie.openempi.stringcomparison.StringComparisonService;

public class ParallelIteratorUtil
{
	public static <T,U> boolean iterate(Collection<T> ct, Collection<U> cu, ParallelIterator<T,U> each) {
		Iterator<T> it = ct.iterator();
		Iterator<U> iu = cu.iterator();
		while (it.hasNext() && iu.hasNext()) {
			if (!each.each(it.next(), iu.next())) {
				return false;
			}
		}
		return !it.hasNext() && !iu.hasNext();
	}

	public static <T,U> boolean iterate(Collection<T> ct, Collection<U> cu, List<MatchPairStat> matchPairStat,
			ParallelMatchPairHalfStatListIterator<T,U> each) throws ApplicationException {
		Iterator<T> it = ct.iterator();
		Iterator<U> iu = cu.iterator();
		while (it.hasNext() && iu.hasNext()) {
			if (!each.each(it.next(), iu.next(), matchPairStat)) {
				return false;
			}
		}
		return !it.hasNext() && !iu.hasNext();
	}

	public static <T,U> boolean iterate(Collection<T> ct, Collection<U> cu, List<LeanRecordPair> pairs,
			StringComparisonService comparisonService, List<MatchField> matchFields, ParallelPersonListIterator<T,U> each) {
		Iterator<T> it = ct.iterator();
		Iterator<U> iu = cu.iterator();
		while (it.hasNext() && iu.hasNext()) {
			if (!each.each(it.next(), iu.next(), pairs, comparisonService, matchFields)) {
				return false;
			}
		}
		return !it.hasNext() && !iu.hasNext();
	}

	public static <T,U> boolean iterate(Collection<T> ct, Collection<U> cu,
			List<List<NamePairValuePair>> valueList, ParallelNameValuePairListIterator<T,U> each) {
		Iterator<T> it = ct.iterator();
		Iterator<U> iu = cu.iterator();
		while (it.hasNext() && iu.hasNext()) {
			if (!each.each(it.next(), iu.next(), valueList)) {
				return false;
			}
		}
		return !it.hasNext() && !iu.hasNext();
	}

	public static <T,U> boolean iterate(Collection<T> ct, Collection<U> cu,
			List<NamePairValuePair> valuePairList, ParallelNameValuePairIterator<T,U> each) {
		Iterator<T> it = ct.iterator();
		Iterator<U> iu = cu.iterator();
		while (it.hasNext() && iu.hasNext()) {
			if (!each.each(it.next(), iu.next(), valuePairList)) {
				return false;
			}
		}
		return !it.hasNext() && !iu.hasNext();
	}
}
