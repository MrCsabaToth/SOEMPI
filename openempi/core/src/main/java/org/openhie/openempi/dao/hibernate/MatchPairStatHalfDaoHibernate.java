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
package org.openhie.openempi.dao.hibernate;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.openhie.openempi.dao.MatchPairStatHalfDao;
import org.openhie.openempi.model.MatchPairStatHalf;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class MatchPairStatHalfDaoHibernate extends UniversalDaoHibernate implements MatchPairStatHalfDao
{
	public void createTable(final String tableName, final String datasetTableName,
			final boolean withIndexesAndConstraints) {
		log.trace("Creating table " + tableName);
		ValidationUtil.sanityCheckFieldName(tableName);
		ValidationUtil.sanityCheckFieldName(datasetTableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 1. Create Table
				String tableFullName = getTableFullName(tableName);
				StringBuilder sqlCreateTable = new StringBuilder("CREATE TABLE public." + tableFullName + "(");
				sqlCreateTable.append(MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + " BIGINT NOT NULL, ");
				sqlCreateTable.append(PERSON_PSEUDO_ID_COLUMN_NAME + " BIGINT NOT NULL, ");
				sqlCreateTable.append(MATCH_STATE_COLUMN_NAME + " boolean NOT NULL");
				sqlCreateTable.append(");");
				Query query = session.createSQLQuery(sqlCreateTable.toString());
				int num = query.executeUpdate();
				if (withIndexesAndConstraints)
					addIndexesAndConstraintsInHibernate(session, tableFullName, 1L, datasetTableName);
				session.flush();
				return num;
			}
		});
	}

	public void removeTable(final String tableName) {
		log.trace("Removing table " + tableName);
		ValidationUtil.sanityCheckFieldName(tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String tableFullName = getTableFullName(tableName);
				// 1. Remove foreign key constraint for person_pseudo_id
				String sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + PERSON_PSEUDO_ID_COLUMN_NAME;
				Query query = session.createSQLQuery(sqlDropFKConstraint);
				int num = query.executeUpdate();
				// 2. Drop primary key constraint
				String sqlDropPKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropPKConstraint);
				num = query.executeUpdate();
				// 3. Drop Index
				String sqlDropIndex = "DROP INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();
				// 4. Drop Index
				sqlDropIndex = "DROP INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX + "2;";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();
				// 5. Drop Sequence
				String sqlDropSequence = "DROP SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropSequence);
				num = query.executeUpdate();
				// 6. Create Table
				String sqlDropTable = "DROP TABLE public." + tableFullName + ";";
				query = session.createSQLQuery(sqlDropTable);
				num = query.executeUpdate();
				session.flush();
				return num;
			}
		});
	}

	private long addMatchPairStatHalfInHibernate(Session session, String tableName, MatchPairStatHalf matchPairStatHalf) {
		log.debug("Saving matchPairStatHalf record: " + matchPairStatHalf);
		String tableFullName = getTableFullName(tableName);

		StringBuilder sqlInsert = new StringBuilder("INSERT INTO public." + tableFullName + " (" +
			MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + ", " +
			PERSON_PSEUDO_ID_COLUMN_NAME + ", " +
			MATCH_STATE_COLUMN_NAME +
			") VALUES (" +
			(matchPairStatHalf.getMatchPairStatHalfId() != null ? "?" : "nextval('" + tableFullName + SEQUENCE_NAME_POSTFIX + "')") + "," +
			"?,?) RETURNING " + MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + ";");

		Query query = session.createSQLQuery(sqlInsert.toString());

		int position = 0;
		if (matchPairStatHalf.getMatchPairStatHalfId() != null) {
			query.setLong(position, matchPairStatHalf.getMatchPairStatHalfId());
			position++;
		}
		query.setLong(position, matchPairStatHalf.getPersonPseudoId());
		position++;
		query.setBoolean(position, matchPairStatHalf.getMatchStatus());

		BigInteger bigInt = (BigInteger)query.uniqueResult();
		long id = bigInt.longValue();
		matchPairStatHalf.setMatchPairStatHalfId(id);
		log.debug("Finished saving the matchPairStatHalf with id " + id);
		return id;
	}

	public void addMatchPairStatHalf(final String tableName, final MatchPairStatHalf matchPairStatHalf) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addMatchPairStatHalfInHibernate(session, tableName, matchPairStatHalf);
				session.flush();
				return 1;
			}
		});
	}

	public void addMatchPairStatHalves(final String tableName, final List<MatchPairStatHalf> matchPairStatHalves) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int num = 0;
				for (MatchPairStatHalf matchPairStatHalf : matchPairStatHalves) {
					addMatchPairStatHalfInHibernate(session, tableName, matchPairStatHalf);
					num++;
				}
				session.flush();
				return num;
			}
		});
	}

	private void addIndexesAndConstraintsInHibernate(Session session, final String tableFullName, final long seqStart,
			final String datasetTableName) {
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
		// 3. Create Index
		String sqlCreateIndex = "CREATE UNIQUE INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 4. Create Index
		sqlCreateIndex = "CREATE INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX + "2" + " ON " +
				tableFullName + " USING btree (" + PERSON_PSEUDO_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 5. Create primary key constraint
		String sqlAddPKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX +
				" PRIMARY KEY (" + MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddPKConstraint);
		num = query.executeUpdate();
		// 6. Create foreign key constraint for person_pseudo_id
		String datasetTableFullName = DATASET_TABLE_NAME_PREFIX + datasetTableName;
		String sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + PERSON_PSEUDO_ID_COLUMN_NAME +
				" FOREIGN KEY (" + PERSON_PSEUDO_ID_COLUMN_NAME + ") REFERENCES " + datasetTableFullName +
				"(" + PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddFKConstraint);
		num = query.executeUpdate();
	}

	public void addIndexesAndConstraints(final String tableName, final long seqStart, final String datasetTableName) {
		ValidationUtil.sanityCheckFieldName(datasetTableName);
		final String tableFullName = getTableFullName(tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addIndexesAndConstraintsInHibernate(session, tableFullName, seqStart, datasetTableName);
				session.flush();
				return 1;
			}
		});
	}

	public void updateMatchPairStatHalf(final String tableName, final MatchPairStatHalf matchPairStatHalf) {
		log.debug("Updateing matchPairStatHalf record: " + matchPairStatHalf);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlUpdate = new StringBuilder("UPDATE public." + getTableFullName(tableName) + " SET ");
				// adding the custom fields
				sqlUpdate.append(MATCH_STATE_COLUMN_NAME + "=" + matchPairStatHalf.getMatchStatus());
				// adding the Id where clause
				sqlUpdate.append(" WHERE (" + MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + "=" + matchPairStatHalf.getMatchPairStatHalfId() + ");");
				SQLQuery query = session.createSQLQuery(sqlUpdate.toString());
				int num = query.executeUpdate();
				log.debug("Finished updating the matchPairStatHalf.");
				session.flush();
				return num;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<MatchPairStatHalf> getMatchPairStatHalvesPaged(final String tableName, final long firstResult,
			final int maxResults) {
		return (List<MatchPairStatHalf>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelect = new StringBuilder();
				sqlSelect.append("SELECT " + MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME + ", " + PERSON_PSEUDO_ID_COLUMN_NAME +
						", " + MATCH_STATE_COLUMN_NAME);
				sqlSelect.append(" FROM public." + getTableFullName(tableName) + " WHERE (true)");
				sqlSelect.append(" LIMIT " + maxResults);
				sqlSelect.append(" OFFSET " + firstResult);
				sqlSelect.append(";");
				List<Map<String, Object>> rows =
					session.createSQLQuery(sqlSelect.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				List<MatchPairStatHalf> matchPairStatHalves = new ArrayList<MatchPairStatHalf>();
				if (rows != null) {
					for(Map<String, Object> rs : rows) {
						if (rs != null)
							matchPairStatHalves.add(getMatchPairStatHalfFromMap(rs));
					}
				}
				return matchPairStatHalves;
			}
		});
	}
	
	private MatchPairStatHalf getMatchPairStatHalfFromMap(Map<String,Object> rs) throws SQLException {
		MatchPairStatHalf m = new MatchPairStatHalf();
		m.setMatchPairStatHalfId(((BigInteger)rs.get(MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME)).longValue());
		m.setPersonPseudoId(((BigInteger)rs.get(PERSON_PSEUDO_ID_COLUMN_NAME)).longValue());
		m.setMatchStatus((Boolean)rs.get(MATCH_STATE_COLUMN_NAME));
		return m;
	}
	
	public String getTableFullName(String tableName) {
		ValidationUtil.sanityCheckFieldName(tableName);
		return MATCHPAIRSTATHALF_TABLE_NAME_PREFIX + tableName;
	}

}
