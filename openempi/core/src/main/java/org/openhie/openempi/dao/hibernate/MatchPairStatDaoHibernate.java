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
import org.openhie.openempi.dao.MatchPairStatDao;
import org.openhie.openempi.model.MatchPairStat;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class MatchPairStatDaoHibernate extends UniversalDaoHibernate implements MatchPairStatDao
{
	public void createTable(final String tableName, final String leftDatasetTableName,
			final String rightDatasetTableName, final boolean withIndexesAndConstraints) {
		log.trace("Creating table " + tableName);
		ValidationUtil.sanityCheckFieldName(tableName);
		ValidationUtil.sanityCheckFieldName(leftDatasetTableName);
		ValidationUtil.sanityCheckFieldName(rightDatasetTableName);		
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 1. Create Table
				String tableFullName = getTableFullName(tableName);
				StringBuilder sqlCreateTable = new StringBuilder("CREATE TABLE public." + tableFullName + "(");
				sqlCreateTable.append(MATCH_PAIR_STAT_ID_COLUMN_NAME + " BIGINT NOT NULL, ");
				sqlCreateTable.append(LEFT_PERSON_PSEUDO_ID_COLUMN_NAME + " BIGINT NOT NULL, ");
				sqlCreateTable.append(RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME + " BIGINT NOT NULL, ");
				sqlCreateTable.append(MATCH_STATE_COLUMN_NAME + " boolean NOT NULL");
				sqlCreateTable.append(");");
				Query query = session.createSQLQuery(sqlCreateTable.toString());
				int num = query.executeUpdate();
				if (withIndexesAndConstraints)
					addIndexesAndConstraintsInHibernate(session, tableFullName, 1L, leftDatasetTableName,
							rightDatasetTableName);
				return num;
			}
		});
	}

	public void removeTable(final String tableName) {
		ValidationUtil.sanityCheckFieldName(tableName);
		log.trace("Removing table " + tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String tableFullName = getTableFullName(tableName);
				// 1. Remove foreign key constraint for left_person_pseudo_id
				String sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME;
				Query query = session.createSQLQuery(sqlDropFKConstraint);
				int num = query.executeUpdate();
				// 2. Remove foreign key constraint for right_person_pseudo_id
				sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + LEFT_PERSON_PSEUDO_ID_COLUMN_NAME;
				query = session.createSQLQuery(sqlDropFKConstraint);
				num = query.executeUpdate();
				// 3. Drop primary key constraint
				String sqlDropPKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropPKConstraint);
				num = query.executeUpdate();
				// 4. Drop Index
				String sqlDropIndex = "DROP INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX +";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();
				// 5. Drop Sequence
				String sqlDropSequence = "DROP SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropSequence);
				num = query.executeUpdate();
				// 6. Drop Table
				String sqlDropTable = "DROP TABLE public." + tableFullName + ";";
				query = session.createSQLQuery(sqlDropTable);
				num = query.executeUpdate();
				session.flush();
				return num;
			}
		});
	}

	private void addMatchPairStatInHibernate(Session session, String tableName, MatchPairStat matchPairStat) {
		log.debug("Saving matchPairStat record: " + matchPairStat);
		String tableFullName = getTableFullName(tableName);

		boolean generateId = (matchPairStat.getMatchPairStatId() == null);
		StringBuilder sqlInsert = new StringBuilder("INSERT INTO public." + tableFullName + " (" +
			MATCH_PAIR_STAT_ID_COLUMN_NAME + ", " +
			LEFT_PERSON_PSEUDO_ID_COLUMN_NAME + ", " +
			RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME + ", " +
			MATCH_STATE_COLUMN_NAME +
			") VALUES (" +
			(generateId ? ("nextval('" + tableFullName + SEQUENCE_NAME_POSTFIX + "')") : "?") +
			",?,?,?)" +
			(generateId ? (" RETURNING " + MATCH_PAIR_STAT_ID_COLUMN_NAME) : "") + ";");

		Query query = session.createSQLQuery(sqlInsert.toString());

		int position = 0;
		if (!generateId) {
			query.setLong(position, matchPairStat.getMatchPairStatId());
			position++;
		}
		query.setLong(position, matchPairStat.getLeftPersonPseudoId());
		position++;
		query.setLong(position, matchPairStat.getRightPersonPseudoId());
		position++;
		query.setBoolean(position, matchPairStat.getMatchStatus());

		if (generateId) {
			BigInteger bigInt = (BigInteger)query.uniqueResult();
			long id = bigInt.longValue();
			matchPairStat.setMatchPairStatId(id);
			log.debug("Finished saving the matchPairStat with id " + id);
		} else {
			query.executeUpdate();
			log.debug("Finished saving the matchPairStat with id " + matchPairStat.getRightPersonPseudoId());
		}
	}

	public void addMatchPairStat(final String tableName, final MatchPairStat matchPairStat) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addMatchPairStatInHibernate(session, tableName, matchPairStat);
				session.flush();
				return 1;
			}
		});
	}

	public void addMatchPairStats(final String tableName, final List<MatchPairStat> matchPairStats) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int num = 0;
				for (MatchPairStat matchPairStat : matchPairStats) {
					addMatchPairStatInHibernate(session, tableName, matchPairStat);
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
		// 3. Create Index
		String sqlCreateIndex = "CREATE UNIQUE INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + MATCH_PAIR_STAT_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlCreateIndex);
		num = query.executeUpdate();
		// 4. Create primary key constraint
		String sqlAddPKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + tableFullName + PK_CONSTNRAINT_NAME_POSTFIX +
				" PRIMARY KEY (" + MATCH_PAIR_STAT_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddPKConstraint);
		num = query.executeUpdate();
		// 5. Create foreign key constraint for left_person_pseudo_id
		String leftDatasetTableFullName = DATASET_TABLE_NAME_PREFIX + leftDatasetTableName;
		String sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + LEFT_PERSON_PSEUDO_ID_COLUMN_NAME +
				" FOREIGN KEY (" + LEFT_PERSON_PSEUDO_ID_COLUMN_NAME + ") REFERENCES " + leftDatasetTableFullName +
				"(" + PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddFKConstraint);
		num = query.executeUpdate();
		// 6. Create foreign key constraint for right_person_pseudo_id
		String rightDatasetTableFullName = DATASET_TABLE_NAME_PREFIX + rightDatasetTableName;
		sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + tableFullName + "_" + RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME +
				" FOREIGN KEY (" + RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME + ") REFERENCES " + rightDatasetTableFullName +
				"(" + PERSON_ID_COLUMN_NAME + ");";
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

	public void updateMatchPairStat(final String tableName, final MatchPairStat matchPairStat) {
		log.debug("Updateing matchPairStat record: " + matchPairStat);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlUpdate = new StringBuilder("UPDATE public." + getTableFullName(tableName) + " SET ");
				// adding the custom fields
				sqlUpdate.append(MATCH_STATE_COLUMN_NAME + "=" + matchPairStat.getMatchStatus());
				// adding the Id where clause
				sqlUpdate.append(" WHERE (" + MATCH_PAIR_STAT_ID_COLUMN_NAME + "=" + matchPairStat.getMatchPairStatId() + ");");
				SQLQuery query = session.createSQLQuery(sqlUpdate.toString());
				int num = query.executeUpdate();
				log.debug("Finished updating the matchPairStat.");
				session.flush();
				return num;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<MatchPairStat> getMatchPairStatsPaged(final String tableName, final long firstResult,
			final int maxResults) {
		return (List<MatchPairStat>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelect = new StringBuilder();
				sqlSelect.append("SELECT " + MATCH_PAIR_STAT_ID_COLUMN_NAME + ", " + LEFT_PERSON_PSEUDO_ID_COLUMN_NAME + ", ");
				sqlSelect.append(RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME + ", " + MATCH_STATE_COLUMN_NAME);
				sqlSelect.append(" FROM public." + getTableFullName(tableName) + " WHERE (true)");
				sqlSelect.append(" LIMIT " + maxResults);
				sqlSelect.append(" OFFSET " + firstResult);
				sqlSelect.append(";");
				List<Map<String, Object>> rows =
					session.createSQLQuery(sqlSelect.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				List<MatchPairStat> matchPairStats = new ArrayList<MatchPairStat>();
				if (rows != null) {
					for(Map<String, Object> rs : rows) {
						if (rs != null)
							matchPairStats.add(getMatchPairStatFromMap(rs));
					}
				}
				return matchPairStats;
			}
		});
	}
	
	private MatchPairStat getMatchPairStatFromMap(Map<String,Object> rs) throws SQLException {
		MatchPairStat m = new MatchPairStat();
		m.setMatchPairStatId(((BigInteger)rs.get(MATCH_PAIR_STAT_ID_COLUMN_NAME)).longValue());
		m.setLeftPersonPseudoId(((BigInteger)rs.get(LEFT_PERSON_PSEUDO_ID_COLUMN_NAME)).longValue());
		m.setRightPersonPseudoId(((BigInteger)rs.get(RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME)).longValue());
		m.setMatchStatus((Boolean)rs.get(MATCH_STATE_COLUMN_NAME));
		return m;
	}
	
	public String getTableFullName(String tableName) {
		ValidationUtil.sanityCheckFieldName(tableName);
		return MATCHPAIRSTAT_TABLE_NAME_PREFIX + tableName;
	}

}
