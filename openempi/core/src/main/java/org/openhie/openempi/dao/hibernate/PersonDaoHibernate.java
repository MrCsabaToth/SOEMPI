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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonDaoHibernate extends UniversalDaoHibernate implements PersonDao
{
	public void createTable(final String tableName, final List<ColumnInformation> columnInformation,
			final boolean withIndexesAndConstraints) {
		log.trace("Creating table " + tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 1. Create Table
				String tableFullName = getTableFullName(tableName);
				StringBuilder sqlCreateTable = new StringBuilder("CREATE TABLE public." + tableFullName + "(" +
						PERSON_ID_COLUMN_NAME + " BIGINT NOT NULL");
				for (ColumnInformation ci : columnInformation) {
					ValidationUtil.sanityCheckFieldName(ci.getFieldName());
					sqlCreateTable.append(", " + COLUMN_NAME_PREFIX + ci.getFieldName().toLowerCase() + " ");
					switch(ci.getFieldType().getFieldTypeEnum()) {
					case String:	sqlCreateTable.append("varchar(" + ci.getFieldTypeModifier() + ")"); break;
					case Integer:	sqlCreateTable.append("integer");			break;
					case BigInt:	sqlCreateTable.append("bigint");			break;
					case Float:		sqlCreateTable.append("real");				break;
					case Double:	sqlCreateTable.append("double precision");	break;
					case Date:		sqlCreateTable.append("date");				break;
					case Blob:		sqlCreateTable.append("bytea");				break;
					}
				}
				sqlCreateTable.append(");");
				Query query = session.createSQLQuery(sqlCreateTable.toString());
				int num = query.executeUpdate();
				// 2. Create Sequence
				String sqlCreateSequence = "CREATE SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + " " +
						"START WITH 1 " +
						"INCREMENT BY 1 " +
						"NO MAXVALUE " +
						"NO MINVALUE " +
						"CACHE 1;";
				query = session.createSQLQuery(sqlCreateSequence);
				num = query.executeUpdate();
				if (withIndexesAndConstraints)
					addIndexesAndConstraintsInHibernate(session, tableFullName);
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
				// 1. Remove foreign key constraint for creator_id
				/*String sqlDropFKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + CREATOR_ID_COLUMN_NAME + ";";
				query = session.createSQLQuery(sqlDropFKConstraint);
				num = query.executeUpdate();*/

				// 2. Drop primary key constraint
				String sqlDropPKConstraint = "ALTER TABLE " + tableFullName +
						" DROP CONSTRAINT " + tableFullName + "_" + PERSON_ID_COLUMN_NAME + PK_CONSTNRAINT_NAME_POSTFIX + ";";
				Query query = session.createSQLQuery(sqlDropPKConstraint);
				int num = query.executeUpdate();
				// 3. Drop Index
				String sqlDropIndex = "DROP INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropIndex);
				num = query.executeUpdate();
				// 4. Drop Sequence
				String sqlDropSequence = "DROP SEQUENCE " + tableFullName + SEQUENCE_NAME_POSTFIX + ";";
				query = session.createSQLQuery(sqlDropSequence);
				num = query.executeUpdate();
				// 5. Create Table
				String sqlDropTable = "DROP TABLE public." + tableFullName + ";";
				query = session.createSQLQuery(sqlDropTable);
				num = query.executeUpdate();
				session.flush();
				return num;
			}
		});
	}

	private Long addPersonInHibernate(Session session, String tableName, Person person) {
		log.debug("Saving person record: " + person);
		String tableFullName = getTableFullName(tableName);
		StringBuilder sqlInsertPerson = new StringBuilder("INSERT INTO public." + tableFullName + " (" +
			// adding the Id - it is auto generated
			PERSON_ID_COLUMN_NAME);
		// adding the custom fields
		Map<String, Object> attributes = person.getAttributes();
		for (Map.Entry<String, Object> pairs : attributes.entrySet()) {
			ValidationUtil.sanityCheckFieldName(pairs.getKey());
			if (pairs.getValue() != null)
				sqlInsertPerson.append(", " + COLUMN_NAME_PREFIX + pairs.getKey().toLowerCase());
		}
		sqlInsertPerson.append(") VALUES (" +
			(person.getPersonId() != null ? "?" : ("nextval('" + tableFullName + SEQUENCE_NAME_POSTFIX + "')")));
		for (Map.Entry<String, Object> pairs : attributes.entrySet()) {
			if (pairs.getValue() != null)
				sqlInsertPerson.append(",?");
		}
		sqlInsertPerson.append(") RETURNING " + PERSON_ID_COLUMN_NAME + ";");

		Query query = session.createSQLQuery(sqlInsertPerson.toString());

		int position = 0;
		if (person.getPersonId() != null) {
			query.setLong(position, person.getPersonId());
			position++;
		}
		for (Map.Entry<String, Object> pairs : attributes.entrySet()) {
			if (pairs.getValue() != null) {
				query.setParameter(position, pairs.getValue());
				position++;
			}
		}

		BigInteger bigInt = (BigInteger)query.uniqueResult();
		long id = bigInt.longValue();
		person.setPersonId(id);
		log.debug("Finished saving the person with id " + id);
		return id;
	}

	public void addPerson(final String tableName, final Person person) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addPersonInHibernate(session, tableName, person);
				session.flush();
				return 1;
			}
		});
	}

	public void addPersons(final String tableName, final List<Person> persons) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int num = 0;
				for (Person person : persons) {
					addPersonInHibernate(session, tableName, person);
					num++;
				}
				session.flush();
				return num;
			}
		});
	}

	private void addIndexesAndConstraintsInHibernate(Session session, final String tableFullName) {
		log.trace("Adding indexes and constraints to table " + tableFullName);
		// 3. Create Index
		String sqlCreateIndex = "CREATE UNIQUE INDEX " + tableFullName + INDEX_CONSTNRAINT_NAME_POSTFIX +
				" ON " + tableFullName + " USING btree (" + PERSON_ID_COLUMN_NAME + ");";
		Query query = session.createSQLQuery(sqlCreateIndex);
		@SuppressWarnings("unused")
		int num = query.executeUpdate();
		// 4. Create primary key constraint
		String sqlAddPKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + tableFullName + "_" + PERSON_ID_COLUMN_NAME + PK_CONSTNRAINT_NAME_POSTFIX +
				" PRIMARY KEY (" + PERSON_ID_COLUMN_NAME + ");";
		query = session.createSQLQuery(sqlAddPKConstraint);
		num = query.executeUpdate();
		// 5. Create foreign key constraint for creator_id
		/*String sqlAddFKConstraint = "ALTER TABLE ONLY " + tableFullName +
				" ADD CONSTRAINT " + FK_CONSTNRAINT_NAME_PREFIX + CREATOR_ID_COLUMN_NAME +
				" FOREIGN KEY (" + CREATOR_ID_COLUMN_NAME + ") REFERENCES app_user(id);";
		query = session.createSQLQuery(sqlAddFKConstraint);
		num = query.executeUpdate();*/
	}

	public void addIndexesAndConstraints(final String tableName) {
		ValidationUtil.sanityCheckFieldName(tableName);
		final String tableFullName = getTableFullName(tableName);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				addIndexesAndConstraintsInHibernate(session, tableFullName);
				session.flush();
				return 1;
			}
		});
	}

	public void updatePerson(final String tableName, final Person person) {
		log.debug("Updateing person record: " + person);
		// TODO: update only the difference
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlUpdatePerson = new StringBuilder("UPDATE public." + getTableFullName(tableName) + " SET ");
				// adding the custom fields
				generateSqlSnipFromPerson(person, sqlUpdatePerson, ", ");
				// adding the Id where clause
				sqlUpdatePerson.append(" WHERE (" + PERSON_ID_COLUMN_NAME + "=?);");
				
				SQLQuery query = session.createSQLQuery(sqlUpdatePerson.toString());

				int position = addParametersFromPerson(person, query, 0);
				query.setLong(position, person.getPersonId());

				int num = query.executeUpdate();
				log.debug("Finished updating the person.");
				session.flush();
				return num;
			}
		});
	}
	
	public Person getPersonById(String tableName, long personId) {
		return getPersonById(tableName, personId, null);
	}
	
	public Person getPersonById(String tableName, long personId, List<String> fields) {
		List<Long> personIds = new ArrayList<Long>();
		personIds.add(personId);
		List<Person> persons = getPersonsByIds(tableName, personIds, fields);
		if (persons == null)
			return null;
		if (persons.size() <= 0)
			return null;
		return persons.get(0);
	}
	
	public List<Person> getPersonsByIds(String tableName, List<Long> personIds) {
		return getPersonsByIds(tableName, personIds, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> getPersonsByIds(final String tableName, final List<Long> personIds, final List<String> fields) {
		return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelectPerson = new StringBuilder();
				appendSelectPersonFirstHalf(sqlSelectPerson, tableName, fields);
				sqlSelectPerson.append("(");
				boolean firstIteration = true;
				for (Long personId : personIds) {
					if (personId != null) {
						if (!firstIteration)
							sqlSelectPerson.append(" OR ");
						else
							firstIteration = false;
						sqlSelectPerson.append(PERSON_ID_COLUMN_NAME + "=?");
					}
				}
				sqlSelectPerson.append(");");
				SQLQuery query = session.createSQLQuery(sqlSelectPerson.toString());

				int position = 0;
				for (Long personId : personIds) {
					if (personId != null) {
						query.setLong(position, personId);
						position++;
					}
				}

				List<Map<String, Object>> rows = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				List<Person> persons = new ArrayList<Person>();
				if (rows != null) {
					for(Map<String, Object> rs : rows) {
						if (rs != null)
							persons.add(getPersonFromMap(rs));
					}
				}
				return persons;
			}
		});
	}
	
	public List<Person> getPersonsByExample(String tableName, Person example) {
		return getPersonsByExamplePaged(tableName, example, null, 0L, 0, false, false);
	}
	
	public List<Person> getPersonsByExample(String tableName, Person example, List<String> fields) {
		return getPersonsByExamplePaged(tableName, example, fields, 0L, 0, false, false);
	}
	
	public List<Person> getPersonsByExamplePaged(String tableName, Person example, long firstResult, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, null, firstResult, maxResults, false, false);
	}

	public List<Person> getPersonsByExamplePaged(String tableName, Person example, List<String> fields, long firstResult, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, fields, firstResult, maxResults, false, false);
	}

	public List<Person> getRandomPersonsByExample(String tableName, Person example, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, null, 0L, maxResults, true, false);
	}
	
	public List<Person> getRandomPersonsByExample(String tableName, Person example, List<String> fields, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, fields, 0L, maxResults, true, false);
	}
	
	public List<Person> getRandomNotNullPersonsByExample(String tableName, Person example, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, null, 0L, maxResults, true, true);
	}
	
	public List<Person> getRandomNotNullPersonsByExample(String tableName, Person example, List<String> fields, int maxResults) {
		return getPersonsByExamplePaged(tableName, example, fields, 0L, maxResults, true, true);
	}
	
	@SuppressWarnings("unchecked")
	private List<Person> getPersonsByExamplePaged(final String tableName, final Person example, final List<String> fields,
			final long firstResult, final int maxResults, final boolean randomize, final boolean notNull) {
		return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelectPerson = new StringBuilder();
				appendSelectPersonFirstHalf(sqlSelectPerson, tableName, fields);
				sqlSelectPerson.append("(");
				generateSqlSnipFromPerson(example, sqlSelectPerson, " OR ");	// add where clause according to the example
				sqlSelectPerson.append(") ");
				if (notNull)
					generateNotNullSqlCriteriaFromFields(sqlSelectPerson, fields);	// add where clause for not null criteria
				if (randomize) {
					sqlSelectPerson.append(" ORDER BY random()");
					// TODO: in MySQL this should be: "order by rand()"
				}
				boolean paging = maxResults > 0;
				if (paging || randomize)
					sqlSelectPerson.append(" LIMIT ?");
				if (paging && !randomize)
					sqlSelectPerson.append(" OFFSET ?");
				sqlSelectPerson.append(";");
				SQLQuery query = session.createSQLQuery(sqlSelectPerson.toString());

				int position = addParametersFromPerson(example, query, 0);
				if (paging || randomize) {
					query.setInteger(position, maxResults);
					position++;
				}
				if (paging && !randomize) {
					query.setLong(position, firstResult);
					position++;
				}

				List<Map<String, Object>> rows = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				List<Person> persons = new ArrayList<Person>();
				if (rows != null) {
					for(Map<String, Object> rs : rows) {
						if (rs != null)
							persons.add(getPersonFromMap(rs));
					}
				}
				return persons;
			}
		});
	}
	
	private void generateSqlSnipFromPerson(Person person, StringBuilder sqlQueryString, String concatenationString) {
		// adding the custom field names for criteria
		boolean criteriaAdded = false;
		if (person != null) {
			Map<String, Object> attributes = person.getAttributes();
			if (attributes.size() > 0) {
				Iterator<Entry<String, Object>> it = attributes.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
					//ValidationUtil.sanityCheckFieldName(pairs.getKey());
					String columnName = pairs.getKey().toLowerCase();
					if (pairs.getValue() == null)
						sqlQueryString.append(COLUMN_NAME_PREFIX + columnName + "=null");
					else
						sqlQueryString.append(COLUMN_NAME_PREFIX + columnName + "=?");
					if (it.hasNext())
						sqlQueryString.append(concatenationString);
					criteriaAdded = true;
				}
			}
		}
		if (!criteriaAdded)
			sqlQueryString.append("TRUE");
	}
	
	private int addParametersFromPerson(Person person, Query query, int position) {
		// adding the custom field values
		if (person != null) {
			Map<String, Object> attributes = person.getAttributes();
			if (attributes.size() > 0) {
				Iterator<Entry<String, Object>> it = attributes.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
					if (pairs.getValue() != null) {
						query.setParameter(position, pairs.getValue());
						position++;
					}
				}
			}
		}
		return position;
	}
	
	private void generateNotNullSqlCriteriaFromFields(StringBuilder sqlSelect, List<String> fields) {
		for (String field : fields) {
			//ValidationUtil.sanityCheckFieldName(field);
			sqlSelect.append("AND ");
			sqlSelect.append(COLUMN_NAME_PREFIX + field);
			sqlSelect.append(" IS NOT NULL ");
		}		
	}
	
	private void appendSelectPersonFirstHalf(StringBuilder sqlSelect, String tableName, List<String> fields) {
		sqlSelect.append("SELECT ");
		if (fields == null || (fields != null && fields.size() == 0))
			appendFieldsToSelectFromColumnInformation(sqlSelect);
		else
			appendFieldsToSelectFromFieldList(sqlSelect, fields);
		sqlSelect.append(" FROM public." + getTableFullName(tableName) + " WHERE ");
	}

	private void appendFieldsToSelectFromColumnInformation(StringBuilder sqlSelect)
	{
		sqlSelect.append("*");	// TODO: get column information somehow
	}

	private void appendFieldsToSelectFromFieldList(StringBuilder sqlSelect, List<String> fields) {
		sqlSelect.append(PERSON_ID_COLUMN_NAME);
		for (String field : fields) {
			//ValidationUtil.sanityCheckFieldName(field);
			sqlSelect.append(", ");
			sqlSelect.append(COLUMN_NAME_PREFIX + field);
		}
	}

	private Person getPersonFromMap(Map<String,Object> rs) throws SQLException {
		Person p = new Person();
		p.setPersonId(((BigInteger)rs.get(PERSON_ID_COLUMN_NAME)).longValue());
		for (Map.Entry<String, Object> pairs : rs.entrySet()) {
			if (pairs.getKey().startsWith(COLUMN_NAME_PREFIX))
				p.setAttribute(pairs.getKey().substring(4), pairs.getValue());
		}
		return p;
	}

	public List<Person> blockRecords(String tableName, Person example, List<String> fields) {
		// TODO: is this correct?
		List<Person> persons = getPersonsByExample(tableName, example, fields);
		return persons;
	}

	@SuppressWarnings("unchecked")
	public List<NameValuePair> getDistinctValues(final String tableName, final String field) {
		if (field == null || field.length() == 0) {
			return new java.util.ArrayList<NameValuePair>();
		}
		return (List<NameValuePair>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String fieldLowerCase = field.toLowerCase();
				StringBuilder sqlSelect = new StringBuilder("SELECT DISTINCT ");
				sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
				sqlSelect.append(" FROM public." + getTableFullName(tableName));
				sqlSelect.append(" WHERE ");
				sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
				sqlSelect.append(" IS NOT NULL");	// Do not include null value into the distinct list
				sqlSelect.append(" ORDER BY " + COLUMN_NAME_PREFIX + fieldLowerCase + " ASC;");
				List<List<Object>> listOfLists = (List<List<Object>>)
					session.createSQLQuery(sqlSelect.toString()).setResultTransformer(Transformers.TO_LIST).list();
		        log.trace("Found the following: " + listOfLists);
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        for (List<Object> value : listOfLists) {
		        	if (value != null) {
		        		if (value.size() > 0) {
		        			nameValuePairs.add(new NameValuePair(fieldLowerCase, value.get(0)));
		        		}
		        	}
		        }
		        return nameValuePairs;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<List<NameValuePair>> getDistinctValues(final String tableName, final List<String> fields) {
		return (List<List<NameValuePair>>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sqlSelect = new StringBuilder("SELECT DISTINCT ");
				StringBuilder sqlSelectPart2 = new StringBuilder(" ORDER BY ");
				for (String field : fields) {
					//ValidationUtil.sanityCheckFieldName(field);
					sqlSelect.append(COLUMN_NAME_PREFIX + field.toLowerCase() + ", ");
					sqlSelectPart2.append(COLUMN_NAME_PREFIX + field.toLowerCase() + " ASC, ");
				}
				sqlSelect.setCharAt(sqlSelect.length() - 2, ' ');	// blank the last unneeded comma
				sqlSelectPart2.setCharAt(sqlSelectPart2.length() - 2, ' ');	// blank the last unneeded comma
				sqlSelect.append(" FROM public." + getTableFullName(tableName));
				if (fields.size() == 1) {
					sqlSelect.append(" WHERE ");
					sqlSelect.append(COLUMN_NAME_PREFIX + fields.get(0).toLowerCase());
					sqlSelect.append(" IS NOT NULL");	// Do not include null value into the distinct list
				}
				sqlSelect.append(sqlSelectPart2 + ";");
				List<List<Object>> listOfLists = (List<List<Object>>)
					session.createSQLQuery(sqlSelect.toString()).setResultTransformer(Transformers.TO_LIST).list();
		        log.trace("Found the following: " + listOfLists);

				List<List<NameValuePair>> pairs = new ArrayList<List<NameValuePair>>(listOfLists.size());
				if (listOfLists.size() == 0) {
					return pairs;
				}
				for (List<Object> row : listOfLists) {
					List<NameValuePair> distinctRowValues = new ArrayList<NameValuePair>(row.size());
					for (int i = 0; i < row.size(); i++) {
						NameValuePair pair = new NameValuePair(fields.get(i), row.get(i));
						distinctRowValues.add(pair);
					}
					pairs.add(distinctRowValues);
				}
				return pairs;
			}
		});
	}

	public double getFieldAverageLength(final String tableName, final String field) {
		if (field == null || field.length() == 0) {
			return 0.0;
		}
		//ValidationUtil.sanityCheckFieldName(field);
		return (Double) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String fieldLowerCase = field.toLowerCase();
				StringBuilder sqlSelect = new StringBuilder("SELECT AVG(CHARACTER_LENGTH(");
				sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
				sqlSelect.append(")) FROM public." + getTableFullName(tableName));
				sqlSelect.append(" WHERE ");
				sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
				sqlSelect.append(" IS NOT NULL;");
				Object avgLenObj = session.createSQLQuery(sqlSelect.toString()).uniqueResult();
				if (avgLenObj ==  null)
					return 0.0;
				java.math.BigDecimal avgLen = (java.math.BigDecimal)avgLenObj;
				double avgLength = avgLen.doubleValue();
				log.trace("Found the following: " + avgLength);
				double presentRatio = getFieldMissingRatioInHibernate(session, tableName, fieldLowerCase, false);
				return avgLength * presentRatio;	// Correct the ratio because of the NULL cells
			}
		});
	}

	public double getFieldMissingRatio(final String tableName, final String field) {
		if (field == null || field.length() == 0) {
			return 0.0;
		}
		//ValidationUtil.sanityCheckFieldName(field);
		return  (Double) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return getFieldMissingRatioInHibernate(session, tableName, field, true);
			}
		});
	}

	public long getNumberOfRecords(final String tableName) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				long totalNum = getTotalNumberOfRecordsInHibernate(session, tableName);
				return totalNum;
			}
		});
	}

	public int getFieldNumberOfMissing(final String tableName, final String field) {
		if (field == null || field.length() == 0) {
			return 0;
		}
		//ValidationUtil.sanityCheckFieldName(field);
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String fieldLowerCase = field.toLowerCase();
				StringBuilder sqlSelect = new StringBuilder("SELECT SUM(CASE WHEN ");
				sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
				sqlSelect.append(" IS NULL THEN 1 ELSE 0 END) FROM ");
				sqlSelect.append(getTableFullName(tableName) + ";");
				java.math.BigInteger bigInt =
					(java.math.BigInteger)session.createSQLQuery(sqlSelect.toString()).uniqueResult();
				int numOfMissing = bigInt.intValue();
				return numOfMissing;
			}
		});
	}

	private double getFieldMissingRatioInHibernate(Session session, String tableName, String field, boolean missingOrPresent) {
		//ValidationUtil.sanityCheckFieldName(field);
		int numOfNotNull = getFieldPresentNumberInHibernate(session, tableName, field);
		long totalNum = getTotalNumberOfRecordsInHibernate(session, tableName);
		if (totalNum == 0L) {
			if (missingOrPresent)
				return 1.0;
			else
				return 0.0;
		}
		long nominator = 0L;
		if (missingOrPresent)
			nominator = totalNum - numOfNotNull;
		else
			nominator = numOfNotNull;
		log.trace("Found the following: " + nominator + " / " + totalNum);
		return Double.valueOf(nominator) / totalNum;
	}

	private int getFieldPresentNumberInHibernate(Session session, String tableName, String field) {
		//ValidationUtil.sanityCheckFieldName(field);
		String fieldLowerCase = field.toLowerCase();
		StringBuilder sqlSelect = new StringBuilder("SELECT COUNT(");
		sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
		sqlSelect.append(") FROM public." + getTableFullName(tableName));
		sqlSelect.append(" WHERE ");
		sqlSelect.append(COLUMN_NAME_PREFIX + fieldLowerCase);
		sqlSelect.append(" IS NOT NULL;");
		java.math.BigInteger bigInt =
			(java.math.BigInteger)session.createSQLQuery(sqlSelect.toString()).uniqueResult();
		int numOfNotNull = bigInt.intValue();
		// Another possible solution for the opposite (counting NULL columns):
		// SELECT SUM(CASE WHEN ColumnName IS NULL THEN 1 ELSE 0 END) FROM Table
		return numOfNotNull;
	}

	private long getTotalNumberOfRecordsInHibernate(Session session, String tableName) {
		StringBuilder sqlSelect = new StringBuilder("SELECT COUNT(*) FROM public." + getTableFullName(tableName) + ";");
		java.math.BigInteger bigInt =
			(java.math.BigInteger)session.createSQLQuery(sqlSelect.toString()).uniqueResult();
		long totalNum = bigInt.longValue();
		return totalNum;
	}

	public String getTableFullName(String tableName) {
		//ValidationUtil.sanityCheckFieldName(tableName);
		return DATASET_TABLE_NAME_PREFIX + tableName;
	}

}
