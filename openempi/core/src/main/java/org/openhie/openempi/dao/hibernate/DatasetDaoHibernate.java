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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openhie.openempi.dao.DatasetDao;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.model.User;
import org.openhie.openempi.util.ValidationUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class DatasetDaoHibernate extends UniversalDaoHibernate implements DatasetDao
{
    @SuppressWarnings("unchecked")
	public List<Dataset> getDatasets(final User user) {
    	if (user == null || user.getId() == null) {
    		return null;
    	}
		List<Dataset> datasets = (List<Dataset>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Dataset dataset where dataset.owner.id = :ownerid ");
				query.setParameter("ownerid", user.getId());
				List<Dataset> result = (List<Dataset>) query.list();
				if (result != null)
		    		for (Dataset dataset : result)
		    			hydrateColumnInformation(dataset);
				return result;
			}
		});
		return datasets;
	}

    public Dataset getDataset(final Integer datasetId) {
    	if (datasetId == null) {
    		return null;
    	}
		Dataset dataset = (Dataset) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Dataset result = (Dataset) session.get(Dataset.class, datasetId);
				if (result != null)
	    			hydrateColumnInformation(result);
				return result;
			}
		});
    	return dataset;
    }
    
    @SuppressWarnings("unchecked")
    public Dataset getDataset(final String tableName) {
    	if (tableName == null) {
    		return null;
    	}
    	if (tableName.length() <= 0) {
    		return null;
    	}
    	ValidationUtil.sanityCheckFieldName(tableName);
		List<Dataset> datasets = (List<Dataset>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Dataset dataset where dataset.tableName = :tablename ");
				query.setParameter("tablename", tableName);
				List<Dataset> result = (List<Dataset>) query.list();
				if (result != null)
		    		for (Dataset dataset : result)
		    			hydrateColumnInformation(dataset);
				return result;
			}
		});
		if (datasets == null)
    		return null;
		if (datasets.size() <= 0)
    		return null;
    	return datasets.get(0);
    }
    
	public Dataset saveDataset(final Dataset dataset) {
		log.debug("Storing a dataset.");
		dataset.referenceThisInChildEntities();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.saveOrUpdate(dataset);
				session.flush();
				hydrateColumnInformation(dataset);
				log.debug("Finished saving the dataset.");
				return null;
			}
		});
		return dataset;
	}
	
	public Dataset updateDataset(final Dataset dataset) {
		log.debug("Updating a dataset.");
		dataset.referenceThisInChildEntities();
		Dataset updatedDataset = (Dataset) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Dataset result = (Dataset) session.merge(dataset);
				session.flush();
				hydrateColumnInformation(result);
				log.debug("Finished updating the dataset.");
				return result;
			}
		});
		return updatedDataset;
	}
	
	protected void hydrateColumnInformation(Dataset dataset) {
		// Hydrate ColumnInformation proxy
		List<ColumnInformation> columnInformation = dataset.getColumnInformation();
		if (columnInformation != null) {
			Iterator<ColumnInformation> it = columnInformation.iterator();
			it.hasNext();
		}
	}
	
	public void removeDataset(Dataset dataset) {
		if (dataset == null || dataset.getDatasetId() == null) {
			return;
		}
		Dataset theDataset = (Dataset) getHibernateTemplate().get(Dataset.class, dataset.getDatasetId());
		getHibernateTemplate().delete(theDataset);
		getHibernateTemplate().flush();
	}

    @SuppressWarnings("unchecked")
	public List<String> getTableNames() {
		List<Dataset> datasets = getHibernateTemplate().find("from Dataset dataset");
		if (datasets == null)
    		return null;
		List<String> tableNames = new ArrayList<String>();
		for (Dataset ds : datasets) {
			tableNames.add(ds.getTableName());
		}
		return tableNames;
	}
}
