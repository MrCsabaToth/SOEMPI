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

import java.util.List;

import org.openhie.openempi.blocking.privacypreserving.PairBucketData;
import org.openhie.openempi.util.SerializationUtil;

public class DisplayPersonBuckets
{
	public DisplayPersonBuckets() {
	}
	
	@SuppressWarnings("unchecked")
	private static void loadFile(String filename) {
		List<PairBucketData> pairBuckets = (List<PairBucketData>)SerializationUtil.deserializeObject(filename);
		System.out.println("Number of buckets: " + pairBuckets.size());
		int i = 0;
		int total = 0;
		for(PairBucketData pairBucket : pairBuckets) {
			List<Long> ids = pairBucket.getIds();
			System.out.println(i + ". bucket, number of ids: " + ids.size());
			if (ids.size() > 0) {
				StringBuilder idsString = new StringBuilder("\t");
				for (Long id : ids)
					idsString.append(id + ", ");
				System.out.println(idsString.toString());
				total += ids.size();
			}
			i++;
		}
		System.out.println("Total ids: " + total);
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		String filename = args[0];
		System.out.println("Displaying the personBucket file " + filename);

		loadFile(filename);
	}
	
	public static void usage() {
		System.out.println("Usage: " + DisplayPersonBuckets.class.getName() + " <file-to-display>");
	}

}
