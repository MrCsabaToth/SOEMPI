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
package org.openhie.openempi.dao;

import org.openhie.openempi.model.User;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class tests the generic GenericDao and BaseDao implementation.
 */
public class UniversalDaoTest extends BaseDaoTestCase {
    protected UniversalDao universalDao;

    /**
     * This method is used instead of setUniversalDao b/c setUniversalDao uses
     * autowire byType <code>setPopulateProtectedVariables(true)</code> can also
     * be used, but it's a little bit slower.
     */
    public void onSetUpBeforeTransaction() throws Exception {
        universalDao = (UniversalDao) applicationContext.getBean("universalDao");
    }

    public void onTearDownAfterTransaction() throws Exception {
        universalDao = null;
    }

    /**
     * Simple test to verify CRUD works.
     */
    public void testCRUD() {
        User user = new User();
        // set required fields
        user.setUsername("foo");
        user.setPassword("bar");
        user.setFirstName("first");
        user.setLastName("last");
        user.getAddress().setCity("Denver");
        user.getAddress().setPostalCode("80465");
        user.setEmail("foo@bar.com");

        // create
        user = (User)universalDao.save(user);
        flush();
        assertNotNull(user.getId());

        // retrieve
        user = (User) universalDao.get(User.class, user.getId());
        assertNotNull(user);
        assertEquals("last", user.getLastName());

        // update
        user.getAddress().setCountry("USA");
        universalDao.save(user);
        flush();

        user = (User) universalDao.get(User.class, user.getId());
        assertEquals( "USA", user.getAddress().getCountry());

        // delete
        universalDao.remove(User.class, user.getId());
        flush();
        try {
            universalDao.get(User.class, user.getId());
            fail("User 'foo' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        } catch (InvalidDataAccessApiUsageException e) { // Spring 2.0 throws this one
            assertNotNull(e.getMessage());
        }
    }
}
