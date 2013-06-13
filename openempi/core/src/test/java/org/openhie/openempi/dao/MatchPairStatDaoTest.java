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
package org.openhie.openempi.dao;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.util.PersonUtils;

public class MatchPairStatDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private DatasetDao datasetDao;
	private MatchPairStatDao matchPairStatDao;
	private UserDao userDao;
	private static final String matchPairStatTableName = "dao_test1";

	private void internalTestAddMatchPairStat(boolean deferredIndexesAndConstraints) throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String leftDatasetName = "left_test_dataset";
		String rightDatasetName = "right_test_dataset";
		List<Long> leftPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, leftDatasetName, "", datasetDao, true,
				applicationContext, true, null, leftPersonIds);
		List<Long> rightPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, rightDatasetName, "2", datasetDao, true,
				applicationContext, true, null, rightPersonIds);

		matchPairStatDao.createTable(matchPairStatTableName, leftDatasetName, rightDatasetName, !deferredIndexesAndConstraints);

		long matchPairStatCounter = 1L;
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(0), true);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(1), rightPersonIds.get(1), true);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(2), rightPersonIds.get(2), true);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(3), rightPersonIds.get(3), true);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(4), rightPersonIds.get(4), true);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(5), rightPersonIds.get(5), true);

		List<MatchPairStat> mpsl = new ArrayList<MatchPairStat>();
		mpsl.add(constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(1), false));
		mpsl.add(constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(2), false));
		mpsl.add(constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(3), false));
		mpsl.add(constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(4), false));
		mpsl.add(constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(0), rightPersonIds.get(5), false));
		matchPairStatDao.addMatchPairStats(matchPairStatTableName, mpsl);

		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(1), rightPersonIds.get(2), false);
		MatchPairStat mps1 = constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(1), rightPersonIds.get(3), true);
		matchPairStatDao.addMatchPairStat(matchPairStatTableName, mps1);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(1), rightPersonIds.get(4), false);
		addMatchPairStat(matchPairStatTableName, matchPairStatCounter++, leftPersonIds.get(1), rightPersonIds.get(5), false);

		MatchPairStat mps2 = constructMatchPairStat(matchPairStatCounter++, leftPersonIds.get(4), rightPersonIds.get(5), true);
		matchPairStatDao.addMatchPairStat(matchPairStatTableName, mps2);

		mps1.setMatchStatus(false);
		matchPairStatDao.updateMatchPairStat(matchPairStatTableName, mps1);
		mps2.setMatchStatus(false);
		matchPairStatDao.updateMatchPairStat(matchPairStatTableName, mps2);

		// testGetMatchPairStatPaged
		int pageSize = 4;
		Long pageStart = 0L;
		List<MatchPairStat> mpsl2 = new ArrayList<MatchPairStat>();
		do {
			mpsl2 = matchPairStatDao.getMatchPairStatsPaged(matchPairStatTableName, pageStart, pageSize);
			for (MatchPairStat mps : mpsl2)
				System.out.println("MatchPairStat: " + mps.toString());
			pageStart += pageSize;
		} while (mpsl2.size() > 0);

		if (deferredIndexesAndConstraints)
			matchPairStatDao.addIndexesAndConstraints(matchPairStatTableName, matchPairStatCounter, leftDatasetName, rightDatasetName);

		// testRemoveMatchPairStatTable
		matchPairStatDao.removeTable(matchPairStatTableName);
	}

	public void testAddMatchPairStat() throws ApplicationException {
		internalTestAddMatchPairStat(false);
	}

	public void testAddMatchPairStatWithDeferredConstraints() throws ApplicationException {
		internalTestAddMatchPairStat(true);
	}

	private MatchPairStat constructMatchPairStat(long matchPairStatId, long leftPersonId, long rightPersonId,
			boolean matchState)
	{
		MatchPairStat matchPairStat = new MatchPairStat();
		matchPairStat.setMatchPairStatId(matchPairStatId);
		matchPairStat.setLeftPersonPseudoId(leftPersonId);
		matchPairStat.setRightPersonPseudoId(rightPersonId);
		matchPairStat.setMatchStatus(matchState);
		return matchPairStat;
	}

	private void addMatchPairStat(String matchPairStatTableName, long matchPairStatId, long leftPersonId, long rightPersonId,
			boolean matchState)
	{
		matchPairStatDao.addMatchPairStat(matchPairStatTableName,
				constructMatchPairStat(matchPairStatId, leftPersonId, rightPersonId, matchState));
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public DatasetDao getDatasetDao() {
		return datasetDao;
	}

	public void setDatasetDao(DatasetDao datasetDao) {
		this.datasetDao = datasetDao;
	}

	public MatchPairStatDao getMatchPairStatDao() {
		return matchPairStatDao;
	}

	public void setMatchPairStatDao(MatchPairStatDao matchPairStatDao) {
		this.matchPairStatDao = matchPairStatDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
