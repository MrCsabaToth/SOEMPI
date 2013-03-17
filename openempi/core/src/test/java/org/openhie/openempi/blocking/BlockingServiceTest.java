/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.blocking;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.blocking.basicblocking.BlockingRound;
import org.openhie.openempi.configuration.BaseFieldPair;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.util.PersonUtils;

public class BlockingServiceTest extends BaseServiceTestCase
{
	public static final String leftDatasetName = "left_test_dataset";
	public static final String rightDatasetName = "right_test_dataset";

	public void testGetRecordPairsWithConfiguration() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<Long> leftPersonIds = new ArrayList<Long>();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, leftDatasetName, "", false, null, false, null, leftPersonIds);
		List<Long> rightPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personManagerService, rightDatasetName, "", false, null, false, null, rightPersonIds);

		BlockingService blockingService = Context.getBlockingService();
		BlockingRound round = new BlockingRound();
		round.addField(new BaseFieldPair(PersonUtils.STATE_NAME, PersonUtils.STATE_NAME));
		round.addField(new BaseFieldPair(PersonUtils.GENDER_NAME, PersonUtils.GENDER_NAME));
		List<BlockingRound> rounds = new ArrayList<BlockingRound>(1);
		rounds.add(round);
		RecordPairSource recordPairSource = blockingService.getRecordPairSource(rounds, leftDatasetName, rightDatasetName);
		int i=0;
		for (RecordPairIterator iter = recordPairSource.iterator(leftDatasetName, rightDatasetName, false, null); iter.hasNext(); ) {
			LeanRecordPair pair = iter.next();
			log.trace("Comparing records " + pair.getLeftRecordId() + " and " + pair.getRightRecordId());
			i++;
		}
		System.out.println("Loaded " + i + " record pairs.");
	}
	
	public void testGetRecordPairs() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		List<Long> leftPersonIds = new ArrayList<Long>();
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonUtils.createTestPersonTable(personManagerService, leftDatasetName, "", false, null, false, null, leftPersonIds);
		List<Long> rightPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personManagerService, rightDatasetName, "", false, null, false, null, rightPersonIds);

		BlockingService blockingService = Context.getBlockingService();
		RecordPairSource recordPairSource = blockingService.getRecordPairSource(leftDatasetName, rightDatasetName);
		int i=0;
		for (RecordPairIterator iter = recordPairSource.iterator(leftDatasetName, rightDatasetName, false, null); iter.hasNext(); ) {
			LeanRecordPair pair = iter.next();
			log.trace("Comparing records " + pair.getLeftRecordId() + " and " + pair.getRightRecordId());
			i++;
		}
		System.out.println("Loaded " + i + " record pairs.");
	}	
}
