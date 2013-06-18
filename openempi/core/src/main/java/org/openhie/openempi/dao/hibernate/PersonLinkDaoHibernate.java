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
package org.openhie.openempi.dao.hibernate;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonLinkDaoHibernate extends UniversalDaoHibernate implements PersonLinkDao
{
	public void createTable(final String tableName, final String leftDatasetTableName,
			final String rightDatasetTableName, final boolean withIndexesAndConstraints) {
		log.trace("Creating table " + tableName);
		ValidationUtil.sanityCheckFieldName(leftDatasetTableName);
		ValidationUtil.sanityCheckFieldName(rightDatasetTableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 1. Create Table
				String tableFullName = getTableFullName(tableName);
				StringBuilder sqlCreateTable = new StringBuilder("CREATE TABLE public." + tableFullName + "(" +
						PERSON_LINK_ID_COLUMN_NAME + " BIGINT NOT NULL," +
						LEFT_PERSON_ID_COLUMN_NAME + " BIGINT NOT NULL," +
						RIGHT_PERSON_ID_COLUMN_NAME + " BIGINT NOT NULL," +
						BINARY_VECTOR_COLUMN_NAME + " varchar(65536)," +
						CONTINOUS_VECTOR_COLUMN_NAME + " varchar(65536)," +
						WEIGHT_COLUMN_NAME + " double precision NOT NULL," +
						LINK_STATUS_COLUMN_NAME + " integer NOT NULL");
				sqlCreateTable.append(");");
				Query query = session.createSQLQuery(sqlCreateTable.toString());
				int num = query.executeUpdate();
				if (withIndexesAndConstraints)
					addIndexesAndConstraintsInHibernate(session, tableFullName, 1L, leftDatasetTableName,
							rightDatasetTableName);
				session.flush();
				return num;
			}
		});
	}

	public void removeTable(final String tableName) {
		log.trace("Removing table " + tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String tableFullName = getTableFullName(tableName);
				// 1. Remove foreign key constraint for left_person_id
				String sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + LEFT_PERSON_ID_COLUMN_NAME + ";";
				Query query = session.createSQLQuery(sqlDropFKConstraint);
				int num = query.executeUpdate();

				// 2. Remove foreign key constraint for right_person_id
				sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + RIGHT_PERSON_ID_COLUMN_NAME + ";";
				query = session.createSQLQuery(sqlDropFKConstraint);
				num = query.executeUpdate();

				// 3. Drop weight index
				String sqlDropIndex = "DROP INDEX " + tableFullName + "_" + WEIGHT_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();

				// 4. Drop right_person_id index
				sqlDropIndex = "DROP INDEX " + tableFullName + "_" + RIGHT_PERSON_ID_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();

				// 5. Drop right_person_id index
				sqlDropIndex = "DROP INDEX " + tableFullName + "_" + LEFT_PERSON_ID_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();

				// 6. Drop primary index
				sqlDropIndex = "DROP INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();

				// 7. Drop primary key constraint
				String sqlDropPKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropPKConstraint);
				num = query.executeUpdate();

				// 8. Drop Sequence
				String sqlDropSequence = "DROP SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropSequence);
				num = query.executeUpdate();

				// 9. Drop Table
				String sqlDropTable = "DROP TABLE public." + tableFullName + ";";
				query = session.createSQLQuery(sqlDropTable);
				num = query.executeUpdate();
				session.flush();
				return num;
			}
		});
	}

	private void addPersonLinkInHibernate(Session session, String tableName, PersonLink personLink) {
		log.debug("Storing a person link.");
		String tableFullName = getTableFullName(tableName);

		boolean generateId = (personLink.getPersonLinkId() == null);
		StringBuilder sqlInsertPerson = new StringBuilder("INSERT INTO public." + tableFullName + " (" +
			PERSON_LINK_ID_COLUMN_NAME + ", " +
			LEFT_PERSON_ID_COLUMN_NAME + ", " +
			RIGHT_PERSON_ID_COLUMN_NAME + ", " +
			(personLink.getBinaryVector() != null ? (BINARY_VECTOR_COLUMN_NAME + ", ") : "") +
			(personLink.getContinousVector() != null ? (CONTINOUS_VECTOR_COLUMN_NAME + ", ") : "") +
			WEIGHT_COLUMN_NAME + ", " +
			LINK_STATUS_COLUMN_NAME +
			") VALUES (" +
			(generateId ? ("nextval('" + tableFullName + SEQUENCE_NAME_POSTFIX + "')") : "?") +
			",?,?" +
			(personLink.getBinaryVector() != null ? ",?" : "") +
			(personLink.getContinousVector() != null ? ",?" : "") +
			",?,?)" +
			(generateId ? (" RETURNING " + PERSON_LINK_ID_COLUMN_NAME) : "") + ";");

		Query query = session.createSQLQuery(sqlInsertPerson.toString());

		int position = 0;
		if (personLink.getPersonLinkId() != null) {
			query.setLong(position, personLink.getPersonLinkId());
			position++;
		}
		query.setLong(position, personLink.getLeftPersonId());
		position++;
		query.setLong(position, personLink.getRightPersonId());
		position++;
		if (personLink.getBinaryVector() != null) {
			query.setString(position, personLink.getBinaryVector());
			position++;
		}
		if (personLink.getContinousVector() != null) {
			query.setString(position, personLink.getContinousVector());
			position++;
		}
		query.setDouble(position, personLink.getWeight());
		position++;
		query.setInteger(position, personLink.getLinkState());

		if (generateId) {
			BigInteger bigInt = (BigInteger)query.uniqueResult();
			long id = bigInt.longValue();
			personLink.setPersonLinkId(id);
			log.debug("Finished saving the person link with id " + id);
		} else {
			query.executeUpdate();
			log.debug("Finished saving the person link with id " + personLink.getPersonLinkId());
		}
	}

	public void addPersonLink(final String tableName, final PersonLink personLink) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addPersonLinkInHibernate(session, tableName, personLink);
				session.flush();
				return 1;
			}
		});
	}

	public void addPersonLinks(final String tableName, final List<PersonLink> personLinks) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int num = 0;
				for (PersonLink personLink : personLinks) {
					addPersonLinkInHibernate(session, tableName, personLink);
					num++;
				}
				session.flush();
				return num;
			}
		});
	}

	private void addIndexesAndConstraintsInHibernate(Session session, final String tableFullName, final long seqStart,
			final String leftDatasetTableName, final String rightDatasetTableName) {
		log.trace("Adding indexes and constraints to table " + tableFullName);
		// 2. Create Sequence
		String sqlCreateSequence = "CREATE SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + " " +
				"START WITH " + seqStart + " " +
				"INCREMENT BY 1 " +
				"NO MAXVALUE " +
				"NO MINVALUE " +
				"CACHE 1;";
		Query query = session.createSQLQuery(sqlCreateSequence);
		@SuppressWarnings("unused")
		int num = query.executeUpdate();
		// 3. Create primary index
		String sqlCreateIndex = "CREATE UNIQUE INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + PERSON_LINK_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 4. Create left_person_id index
		sqlCreateIndex = "CREATE INDEX " + tableFullName + "_" + LEFT_PERSON_ID_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + LEFT_PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 5. Create right_person_id index
		sqlCreateIndex = "CREATE INDEX " + tableFullName + "_" + RIGHT_PERSON_ID_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + RIGHT_PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 6. Create weight index
		sqlCreateIndex = "CREATE INDEX " + tableFullName + "_" + WEIGHT_COLUMN_NAME + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + WEIGHT_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 7. Create primary key constraint
		String sqlAddPKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX +
				" PRIMARY KEY (" + PERSON_LINK_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddPKConstraint);
		num = query.executeUpdate();
		// 8. Create foreign key constraint for left_person_id
		String sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + LEFT_PERSON_ID_COLUMN_NAME +
				" FOREIGN KEY (" + LEFT_PERSON_ID_COLUMN_NAME + ") REFERENCES " +
				DATASET_TABLE_NAME_PREFIX + leftDatasetTableName + "(" + PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddFKConstraint);
		num = query.executeUpdate();
		// 9. Create foreign key constraint for right_person_id
		sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + RIGHT_PERSON_ID_COLUMN_NAME +
				" FOREIGN KEY (" + RIGHT_PERSON_ID_COLUMN_NAME + ") REFERENCES " +
				DATASET_TABLE_NAME_PREFIX + rightDatasetTableName + "(" + PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddFKConstraint);
		num = query.executeUpdate();
	}

	public void addIndexesAndConstraints(final String tableName, final long seqStart, final String leftDatasetTableName,
			final String rightDatasetTableName) {
		ValidationUtil.sanityCheckFieldName(leftDatasetTableName);
		ValidationUtil.sanityCheckFieldName(rightDatasetTableName);
		final String tableFullName = getTableFullName(tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addIndexesAndConstraintsInHibernate(session, tableFullName, seqStart, leftDatasetTableName,
						rightDatasetTableName);
				session.flush();
				return 1;
			}
		});
	}

	public List<PersonLink> getPersonLinks(String tableName, Person leftPerson, Person rightPerson) {
		log.trace("Looking for links between person " + leftPerson + " and " + rightPerson);
		List<PersonLink> links = getPersonLinksPagedInternal(tableName, leftPerson, rightPerson, 0L, 0);
        if (links.isEmpty()) {
        	log.trace("No links found between person " + leftPerson + " and " + rightPerson);
            return null;
        }
    	log.trace("Found " + links.size() + " links between person " + leftPerson + " and " + rightPerson);
        return links;
	}

	public List<PersonLink> getLinksForLeftPerson(String tableName, Person person) {
		log.trace("Looking for links to this left person " + person.getPersonId());
		List<PersonLink> links = getPersonLinksPagedInternal(tableName, person, null, 0L, 0);
		log.trace("Found " + links.size() + " links to left person " + person.getPersonId());
		return links;
	}
	
	public List<PersonLink> getLinksForRightPerson(String tableName, Person person) {
		log.trace("Looking for links to this right person " + person.getPersonId());
		List<PersonLink> links = getPersonLinksPagedInternal(tableName, null, person, 0L, 0);
		log.trace("Found " + links.size() + " links to right person " + person.getPersonId());
		return links;
	}
	
	public List<PersonLink> getPersonLinksPaged(String tableName, Person leftPerson, Person rightPerson,
			long firstResult, int maxResults)
	{
		log.trace("Looking for links paged (" + firstResult + ", " + maxResults + ") between person " + leftPerson + " and " + rightPerson);
		List<PersonLink> links = getPersonLinksPagedInternal(tableName, leftPerson, rightPerson, firstResult, maxResults);
        if (links.isEmpty()) {
        	log.trace("No links found between person " + leftPerson + " and " + rightPerson);
            return null;
        }
    	log.trace("Found page of size " + links.size() + " links between person " + leftPerson + " and " + rightPerson);
        return links;
	}
	
	@SuppressWarnings("unchecked")
	private List<PersonLink> getPersonLinksPagedInternal(final String tableName, final Person leftPerson,
			final Person rightPerson, final long firstResult, final int maxResults)
	{
		log.trace("Looking for links between person " + leftPerson + " and " + rightPerson);
		return (List<PersonLink>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelectLinks = new StringBuilder("SELECT " +
						PERSON_LINK_ID_COLUMN_NAME + ", " +
						LEFT_PERSON_ID_COLUMN_NAME + ", " +
						RIGHT_PERSON_ID_COLUMN_NAME + ", " +
						BINARY_VECTOR_COLUMN_NAME + ", " +
						CONTINOUS_VECTOR_COLUMN_NAME + ", " +
						WEIGHT_COLUMN_NAME +
					" FROM public." + getTableFullName(tableName));
				if (leftPerson != null || rightPerson != null) {
					sqlSelectLinks.append(" WHERE (");
					if (leftPerson != null)
						sqlSelectLinks.append(LEFT_PERSON_ID_COLUMN_NAME + "=" + leftPerson.getPersonId());
					if (rightPerson != null) {
						if (leftPerson != null)
							sqlSelectLinks.append(" AND ");
						sqlSelectLinks.append(RIGHT_PERSON_ID_COLUMN_NAME + "=" + rightPerson.getPersonId());
					}
					sqlSelectLinks.append(")");
				}
				sqlSelectLinks.append(" ORDER BY " + WEIGHT_COLUMN_NAME + " ASC");
				boolean paging = maxResults > 0;
				if (paging) {
					sqlSelectLinks.append(" LIMIT " + maxResults);
					sqlSelectLinks.append(" OFFSET " + firstResult);
				}
				sqlSelectLinks.append(";");
				List<PersonLink> links =
					session.createSQLQuery(sqlSelectLinks.toString()).setResultTransformer(Transformers.aliasToBean(PersonLink.class)).list();
				return links;
			}
		});
	}

	public String getTableFullName(String tableName) {
		ValidationUtil.sanityCheckFieldName(tableName);
		return LINK_TABLE_NAME_PREFIX + tableName.toLowerCase();
	}

}
