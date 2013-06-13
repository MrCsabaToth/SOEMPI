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
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.util.PersonUtils;

public class MatchPairStatHalfDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private DatasetDao datasetDao;
	private MatchPairStatHalfDao matchPairStatHalfDao;
	private UserDao userDao;
	private static final String matchPairStatHalfTableName = "dao_test2";

	private void internalTestAddMatchPairStat(boolean deferredIndexesAndConstraints) throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String datasetName = "test_dataset2";
		List<Long> personIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, datasetName, "", datasetDao, true,
				applicationContext, true, null, personIds);

		matchPairStatHalfDao.createTable(matchPairStatHalfTableName, datasetName, !deferredIndexesAndConstraints);

		long idCounter = 1L;
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(0), true);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(1), true);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(2), true);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(3), true);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(4), true);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(5), true);

		List<MatchPairStatHalf> mpshl = new ArrayList<MatchPairStatHalf>();
		mpshl.add(constructMatchPairStatHalf(idCounter++, personIds.get(0), false));
		mpshl.add(constructMatchPairStatHalf(idCounter++, personIds.get(0), false));
		mpshl.add(constructMatchPairStatHalf(idCounter++, personIds.get(0), false));
		mpshl.add(constructMatchPairStatHalf(idCounter++, personIds.get(0), false));
		mpshl.add(constructMatchPairStatHalf(idCounter++, personIds.get(0), false));
		matchPairStatHalfDao.addMatchPairStatHalves(matchPairStatHalfTableName, mpshl);

		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(1), false);
		MatchPairStatHalf mpsh1 = constructMatchPairStatHalf(idCounter++, personIds.get(1), true);
		matchPairStatHalfDao.addMatchPairStatHalf(matchPairStatHalfTableName, mpsh1);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(1), false);
		addMatchPairStatHalf(matchPairStatHalfTableName, idCounter++, personIds.get(1), false);

		MatchPairStatHalf mpsh2 = constructMatchPairStatHalf(idCounter++, personIds.get(4), true);
		matchPairStatHalfDao.addMatchPairStatHalf(matchPairStatHalfTableName, mpsh2);

		mpsh1.setMatchStatus(false);
		matchPairStatHalfDao.updateMatchPairStatHalf(matchPairStatHalfTableName, mpsh1);
		mpsh2.setMatchStatus(false);
		matchPairStatHalfDao.updateMatchPairStatHalf(matchPairStatHalfTableName, mpsh2);

		// testGetMatchPairStatPaged
		int pageSize = 4;
		Long pageStart = 0L;
		List<MatchPairStatHalf> mpshl2 = new ArrayList<MatchPairStatHalf>();
		do {
			mpshl2 = matchPairStatHalfDao.getMatchPairStatHalvesPaged(matchPairStatHalfTableName, pageStart, pageSize);
			for (MatchPairStatHalf mpsh : mpshl2)
				System.out.println("MatchPairStat: " + mpsh.toString());
			pageStart += pageSize;
		} while (mpshl2.size() > 0);

		if (deferredIndexesAndConstraints)
			matchPairStatHalfDao.addIndexesAndConstraints(matchPairStatHalfTableName, idCounter, datasetName);

		// testRemoveMatchPairStatTable
		matchPairStatHalfDao.removeTable(matchPairStatHalfTableName);
	}

	public void testAddMatchPairStat() throws ApplicationException {
		internalTestAddMatchPairStat(false);
	}

	public void testAddMatchPairStatDeferredConstraints() throws ApplicationException {
		internalTestAddMatchPairStat(true);
	}

	private MatchPairStatHalf constructMatchPairStatHalf(long matchPairStatHalfId, long personId, boolean matchState)
	{
		MatchPairStatHalf matchPairStatHalf = new MatchPairStatHalf();
		matchPairStatHalf.setMatchPairStatHalfId(matchPairStatHalfId);
		matchPairStatHalf.setPersonPseudoId(personId);
		matchPairStatHalf.setMatchStatus(matchState);
		return matchPairStatHalf;
	}

	private void addMatchPairStatHalf(String matchPairStatHalfTableName, long matchPairStatHalfId, long personId,
			boolean matchState)
	{
		matchPairStatHalfDao.addMatchPairStatHalf(matchPairStatHalfTableName,
				constructMatchPairStatHalf(matchPairStatHalfId, personId, matchState));
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

	public MatchPairStatHalfDao getMatchPairStatHalfDao() {
		return matchPairStatHalfDao;
	}

	public void setMatchPairStatHalfDao(MatchPairStatHalfDao matchPairStatHalfDao) {
		this.matchPairStatHalfDao = matchPairStatHalfDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
