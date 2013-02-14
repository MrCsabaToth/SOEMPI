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
package org.openhie.openempi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.dao.UniversalDao;
import org.openhie.openempi.service.UniversalManager;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UniversalManagerImpl implements UniversalManager
{
    /**
     * Log instance for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * UniversalDao instance, ready to charge forward and persist to the database
     */
    protected UniversalDao dao;
 
    public void setDao(UniversalDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public Object get(Class clazz, Serializable id) {
        return dao.get(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List getAll(Class clazz) {
        return dao.getAll(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public void remove(Class clazz, Serializable id) {
        dao.remove(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    public Object save(Object o) {
        return dao.save(o);
    }
}
