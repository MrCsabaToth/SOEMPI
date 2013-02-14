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

import org.openhie.openempi.dao.UniversalDao;
import org.openhie.openempi.model.User;
import org.jmock.Expectations;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.AssertThrows;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the generic UniversalManager and UniversalManagerImpl implementation.
 */
public class UniversalManagerTest extends BaseManagerMockTestCase {
    protected UniversalManagerImpl manager = new UniversalManagerImpl();
    protected UniversalDao dao;

    @Before
    public void setUp() throws Exception {
        dao = context.mock(UniversalDao.class);
        manager.setDao(dao);
    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        dao = null;
    }

    /**
     * Simple test to verify BaseDao works.
     */
    @Test
    public void testCreate() {
        final User user = createUser();
        context.checking(new Expectations() {{
            one(dao).save(with(same(user)));
            will(returnValue(user));
        }});

        manager.save(user);
    }

    @Test
    public void testRetrieve() {
        final User user = createUser();
        context.checking(new Expectations() {{
            one(dao).get(User.class, "foo");
            will(returnValue(user));
        }});

        User user2 = (User) manager.get(User.class, user.getUsername());
        assertTrue(user2.getUsername().equals("foo"));
    }

    @Test
    public void testUpdate() {
        context.checking(new Expectations() {{
            one(dao).save(createUser());
        }});

        User user = createUser();
        user.getAddress().setCountry("USA");
        manager.save(user);
    }

    @Test
    public void testDelete() {
        final Exception ex = new ObjectRetrievalFailureException(User.class, "foo");

        context.checking(new Expectations() {{
            one(dao).remove(User.class, "foo");
            one(dao).get(User.class, "foo");
            will(throwException(ex));
        }});

        manager.remove(User.class, "foo");
        new AssertThrows(ObjectRetrievalFailureException.class) {
            public void test() {
                manager.get(User.class, "foo");
            }
        }.runTest();
    }
    
    private User createUser() {
        User user = new User();
        // set required fields
        user.setUsername("foo");
        return user;
    }
}
