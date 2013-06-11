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

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.dao.UniversalDao;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class serves as the a class that can CRUD any object without any
 * Spring configuration. The only down-side is it does require casting
 * from Object to the object class.
 *
 * @author Bryan Noll
 */
public class UniversalDaoHibernate extends HibernateDaoSupport implements UniversalDao {
	/**
	 * Prefix of the table names for dynamic tables
	 */
	public static final String TABLE_NAME_PREFIX = "tbl_";

	/**
	 * Extra prefix of the table names for dynamic dataset tables
	 */
	public static final String DATASET_TABLE_NAME_EXTRA_PREFIX = "ds_";

	/**
	 * Prefix of the table names for dynamic dataset tables
	 */
	public static final String DATASET_TABLE_NAME_PREFIX = TABLE_NAME_PREFIX + DATASET_TABLE_NAME_EXTRA_PREFIX;

	/**
	 * Extra prefix of the table names for dynamic link tables
	 */
	public static final String LINK_TABLE_NAME_EXTRA_PREFIX = "link_";

	/**
	 * Prefix of the table names for dynamic link tables
	 */
	public static final String LINK_TABLE_NAME_PREFIX = TABLE_NAME_PREFIX + LINK_TABLE_NAME_EXTRA_PREFIX;

	/**
	 * Extra prefix of the table names for match pair stat tables
	 */
	public static final String MATCHPAIRSTAT_TABLE_NAME_EXTRA_PREFIX = "mps";

	/**
	 * Prefix of the table names for match pair stat tables
	 */
	public static final String MATCHPAIRSTAT_TABLE_NAME_PREFIX = TABLE_NAME_PREFIX + MATCHPAIRSTAT_TABLE_NAME_EXTRA_PREFIX + "_";

	/**
	 * Extra prefix of the table names for match pair stat half tables
	 */
	public static final String MATCHPAIRSTATHALF_TABLE_NAME_EXTRA_PREFIX = MATCHPAIRSTAT_TABLE_NAME_EXTRA_PREFIX + "h_";

	/**
	 * Prefix of the table names for match pair stat half tables
	 */
	public static final String MATCHPAIRSTATHALF_TABLE_NAME_PREFIX = TABLE_NAME_PREFIX + MATCHPAIRSTATHALF_TABLE_NAME_EXTRA_PREFIX;

	/**
	 * Prefix of the column names for dynamic tables
	 */
	public static final String COLUMN_NAME_PREFIX = "fld_";

	/**
	 * Prefix of index constraints
	 */
	public static final String INDEX_CONSTNRAINT_NAME_POSTFIX = "_idx";

	/**
	 * Prefix of sequence names
	 */
	public static final String SEQUENCE_NAME_POSTFIX = "_seq";

	/**
	 * Prefix of foreign key constraints
	 */
	public static final String FK_CONSTNRAINT_NAME_PREFIX = "fk_";

	/**
	 * Postfix of primary key key constraints
	 */
	public static final String PK_CONSTNRAINT_NAME_POSTFIX = "_pkey";

	/**
	 * Creator ID column name
	 */
	public static final String CREATOR_ID_COLUMN_NAME = "creator_id";

	/**
	 * Person's primary key column name
	 */
	public static final String PERSON_ID_COLUMN_NAME = "person_id";

	/**
	 * Person's primary key column name as pseudo id
	 */
	public static final String PERSON_PSEUDO_ID_COLUMN_NAME = "person_pseudo_id";

	/**
	 * Match pair stat's primary key column name
	 */
	public static final String MATCH_PAIR_STAT_ID_COLUMN_NAME = "match_pair_stat_id";

	/**
	 * Match pair stat half's primary key column name
	 */
	public static final String MATCH_PAIR_STAT_HALF_ID_COLUMN_NAME = "match_pair_stat_half_id";

	/**
	 * Person Link's primary key column name
	 */
	public static final String PERSON_LINK_ID_COLUMN_NAME = "person_link_id";

	/**
	 * Person Match's primary key column name
	 */
	public static final String PERSON_MATCH_ID_COLUMN_NAME = "person_match_id";

	/**
	 * Left word
	 */
	public static final String LEFT_WORD = "left_";

	/**
	 * Right word
	 */
	public static final String RIGHT_WORD = "right_";

	/**
	 * Person left person id's column name
	 */
	public static final String LEFT_PERSON_ID_COLUMN_NAME = LEFT_WORD + PERSON_ID_COLUMN_NAME;

	/**
	 * Person right person id's column name
	 */
	public static final String RIGHT_PERSON_ID_COLUMN_NAME = RIGHT_WORD + PERSON_ID_COLUMN_NAME;

	/**
	 * Person left person id's column name as pseudo id
	 */
	public static final String LEFT_PERSON_PSEUDO_ID_COLUMN_NAME = LEFT_WORD + PERSON_PSEUDO_ID_COLUMN_NAME;

	/**
	 * Person right person id's column name as pseudo id
	 */
	public static final String RIGHT_PERSON_PSEUDO_ID_COLUMN_NAME = RIGHT_WORD + PERSON_PSEUDO_ID_COLUMN_NAME;

	/**
	 * Weight column name
	 */
	public static final String WEIGHT_COLUMN_NAME = "weight";

	/**
	 * Postfix of the BloomFilter (re)encoded table
	 */
	public static final String BF_TABLE_NAME_POSTFIX = "_bf";

	/**
	 * Name of a Composite BloomFilter attribute
	 */
	public static final String CBF_ATTRIBUTE_NAME = "cbf";

	/**
	 * Postfix of the Composite BloomFilter encoded table
	 */
	public static final String CBF_TABLE_NAME_POSTFIX = "_" + CBF_ATTRIBUTE_NAME;

	/**
	 * Link status column name (match/non-match/undecided/...)
	 */
	public static final String LINK_STATUS_COLUMN_NAME = "link_state";
	public static final int LINK_STATUS_MATCH = 1;
	public static final int LINK_STATUS_NONMATCH = 0;
	public static final int LINK_STATUS_UNDECIDED = 2;

	/**
	 * Creation date column name
	 */
	public static final String DATE_CREATED_COLUMN_NAME = "date_created";

	/**
	 * binary vector column name
	 */
	public static final String BINARY_VECTOR_COLUMN_NAME = "binary_vector";

	/**
	 * continous vector column name
	 */
	public static final String CONTINOUS_VECTOR_COLUMN_NAME = "continous_vector";

	/**
	 * match_state column name
	 */
	public static final String MATCH_STATE_COLUMN_NAME = "match_state";

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * {@inheritDoc}
     */
    public Object save(Object o) {
        return getHibernateTemplate().merge(o);
    }

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("rawtypes")
	public Object get(Class clazz, Serializable id) {
        Object o = getHibernateTemplate().get(clazz, id);

        if (o == null) {
            throw new ObjectRetrievalFailureException(clazz, id);
        }

        return o;
    }

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("rawtypes")
	public List getAll(Class clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("rawtypes")
	public void remove(Class clazz, Serializable id) {
        getHibernateTemplate().delete(get(clazz, id));
    }
}
