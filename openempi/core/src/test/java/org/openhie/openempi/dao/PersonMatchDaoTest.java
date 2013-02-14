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
import org.openhie.openempi.dao.hibernate.UniversalDaoHibernate;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.matching.fellegisunter.MatchConfiguration;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonMatch;
import org.openhie.openempi.model.User;
import org.openhie.openempi.util.PersonUtils;

public class PersonMatchDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private DatasetDao datasetDao;
	private PersonLinkDao personLinkDao;
	private PersonMatchDao personMatchDao;
	private UserDao userDao;

	public void testAddPersonLink() throws ApplicationException {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		String leftDatasetName = "left_test_dataset";
		String rightDatasetName = "right_test_dataset";
		List<Long> leftPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, leftDatasetName, "", datasetDao, true,
				applicationContext, false, null, leftPersonIds);
		List<Long> rightPersonIds = new ArrayList<Long>();
		PersonUtils.createTestPersonTable(personDao, rightDatasetName, "2", datasetDao, true,
				applicationContext, false, null, rightPersonIds);

		User user = (User) userDao.loadUserByUsername("admin");

		Dataset leftDataset = new Dataset(leftDatasetName, "N/A");
		leftDataset.setTotalRecords(6L);
		leftDataset.setOwner(user);
		leftDataset.setDateCreated(new java.util.Date());
		datasetDao.saveDataset(leftDataset);

		Dataset rightDataset = new Dataset(rightDatasetName, "N/A");
		rightDataset.setTotalRecords(6L);
		rightDataset.setOwner(user);
		rightDataset.setDateCreated(new java.util.Date());
		datasetDao.saveDataset(rightDataset);

		String matchName = "match_attempt_1";
		PersonMatch personMatch = new PersonMatch();
		personMatch.setMatchTitle(matchName);
		personMatch.setLeftDataset(leftDataset);
		personMatch.setRightDataset(rightDataset);
		MatchConfiguration matchConfiguration = PersonUtils.constructDummyMatchConfiguration();
		personMatch.setMatchConfiguration(matchConfiguration);
		FellegiSunterParameters fsp1 = PersonUtils.constructDummyFellegiSunterParameters(0.0);
		personMatch.setMatchFellegiSunter(fsp1);
		FellegiSunterParameters fsp2 = PersonUtils.constructDummyFellegiSunterParameters(0.01);
		personMatch.setBlockFellegiSunter(fsp2);
		personMatch.setTotalRecords(16L);
		personMatch.setUserCreatedBy(user);
		personMatch.setDateCreated(new java.util.Date());
		personMatchDao.addPersonMatch(personMatch);

		String linkTableName = "person_match_dao_test";
		Integer personMatchId = personMatch.getPersonMatchId();
		personLinkDao.createTable(linkTableName, leftDatasetName, rightDatasetName, false);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(0), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(1), rightPersonIds.get(1), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(2), rightPersonIds.get(2), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(3), rightPersonIds.get(3), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(4), rightPersonIds.get(4), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(5), rightPersonIds.get(5), user, 10.0, UniversalDaoHibernate.LINK_STATUS_MATCH);

		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(1), user, 0.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(2), user, 1.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(3), user, 0.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(4), user, 1.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(0), rightPersonIds.get(5), user, 1.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);

		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(1), rightPersonIds.get(2), user, 0.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(1), rightPersonIds.get(3), user, 5.0, UniversalDaoHibernate.LINK_STATUS_UNDECIDED);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(1), rightPersonIds.get(4), user, 0.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);
		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(1), rightPersonIds.get(5), user, 0.0, UniversalDaoHibernate.LINK_STATUS_NONMATCH);

		addPersonLink(linkTableName, personMatchId, leftPersonIds.get(4), rightPersonIds.get(5), user, 9.0, UniversalDaoHibernate.LINK_STATUS_UNDECIDED);

		personLinkDao.addIndexesAndConstraints(linkTableName, leftDatasetName, rightDatasetName);

		Person leftPerson = personDao.getPersonById(leftDatasetName, leftPersonIds.get(0));
		Person rightPerson = personDao.getPersonById(rightDatasetName, rightPersonIds.get(0));

		List<PersonLink> links = personLinkDao.getPersonLinks(linkTableName, leftPerson, rightPerson);
		System.out.println("Found a links: " + links);

		links = personLinkDao.getLinksForLeftPerson(linkTableName, leftPerson);
		for (PersonLink alink : links) {
			System.out.println("Found a link using left person: " + alink);
		}

		links = personLinkDao.getLinksForRightPerson(linkTableName, rightPerson);
		for (PersonLink alink : links) {
			System.out.println("Found a link using right person: " + alink);
		}

		Person leftPerson2 = personDao.getPersonById(leftDatasetName, leftPersonIds.get(1));
		links = personLinkDao.getLinksForLeftPerson(linkTableName, leftPerson2);
		for (PersonLink alink : links) {
			System.out.println("Found a link using left person: " + alink);
		}

		Person rightPerson2 = personDao.getPersonById(rightDatasetName, rightPersonIds.get(5));
		links = personLinkDao.getLinksForRightPerson(linkTableName, rightPerson2);
		for (PersonLink alink : links) {
			System.out.println("Found a link using right person: " + alink);
		}

		PersonMatch personMatchBack = personMatchDao.getPersonMatch(personMatchId);
		MatchConfiguration matchConfigurationBack = personMatchBack.getMatchConfiguration();
		assertTrue(matchConfiguration.equals(matchConfigurationBack));
		FellegiSunterParameters fsp1Back = personMatchBack.getMatchFellegiSunter();
		assertTrue(fsp1.equals(fsp1Back));
		FellegiSunterParameters fsp2Back = personMatchBack.getBlockFellegiSunter();
		assertTrue(fsp2.equals(fsp2Back));
	}

	private void addPersonLink(String linkTableName, Integer personMatchId, Long leftPersonId, Long rightPersonId,
			User user, Double weight, Integer linkState)
	{
		PersonLink personLink = new PersonLink();
		personLink.setPersonMatchId(personMatchId);
		personLink.setLeftPersonId(leftPersonId);
		personLink.setRightPersonId(rightPersonId);
		personLink.setCreatorId(user.getId());
		personLink.setDateCreated(new java.util.Date());
		personLink.setWeight(weight);
		personLink.setLinkState(linkState);
		personLinkDao.addPersonLink(linkTableName, personLink);
	}

	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}

	public PersonMatchDao getPersonMatchDao() {
		return personMatchDao;
	}

	public void setPersonMatchDao(PersonMatchDao personMatchDao) {
		this.personMatchDao = personMatchDao;
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

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
