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

import org.openhie.openempi.Constants;
import org.openhie.openempi.model.Address;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class UserDaoTest extends BaseDaoTestCase {
    private UserDao dao = null;
    private RoleDao rdao = null;
    
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }
    
    public void setRoleDao(RoleDao rdao) {
        this.rdao = rdao;
    }

    public void testGetUserInvalid() throws Exception {
        try {
            dao.get(1000);
            fail("'badusername' found in database, failing test...");
        } catch (DataAccessException d) {
            assertTrue(d != null);
        }
    }

    public void testGetUser() throws Exception {
        User user = dao.get(-1);

        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.isEnabled());
    }

    public void testGetUserPassword() throws Exception {
        User user = dao.get(-1);
        String password = dao.getUserPassword(user.getUsername());
        assertNotNull(password);
        log.debug("password: " + password);
    }

    public void testUpdateUser() throws Exception {
        User user = dao.get(-1);

        Address address = user.getAddress();
        address.setAddress("new address");

        dao.saveUser(user);
        flush();

        user = dao.get(-1);
        assertEquals(address, user.getAddress());
        assertEquals("new address", user.getAddress().getAddress());
        
        // verify that violation occurs when adding new user with same username
        user.setId(null);

        endTransaction();

        try {
            dao.saveUser(user);
            flush();
            fail("saveUser didn't throw DataIntegrityViolationException");
        } catch (DataIntegrityViolationException e) {
            assertNotNull(e);
            log.debug("expected exception: " + e.getMessage());
        }
    }

    public void testAddUserRole() throws Exception {
        User user = dao.get(-1);
        assertEquals(1, user.getRoles().size());

        Role role = rdao.getRoleByName(Constants.ADMIN_ROLE);
        user.addRole(role);
        user = dao.saveUser(user);
        flush();

        user = dao.get(-1);
        assertEquals(2, user.getRoles().size());

        //add the same role twice - should result in no additional role
        user.addRole(role);
        dao.saveUser(user);
        flush();

        user = dao.get(-1);
        assertEquals("more than 2 roles", 2, user.getRoles().size());

        user.getRoles().remove(role);
        dao.saveUser(user);
        flush();

        user = dao.get(-1);
        assertEquals(1, user.getRoles().size());
    }

    public void testAddAndRemoveUser() throws Exception {
        User user = new User("testuserj");
        user.setPassword("testpass");
        user.setFirstName("Test");
        user.setLastName("Last");
        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);
        user.setEmail("testuser@appfuse.org");
        user.setWebsite("http://raibledesigns.com");
        
        Role role = rdao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role.getId());
        user.addRole(role);

        user = dao.saveUser(user);
        flush();

        assertNotNull(user.getId());
        user = dao.get(user.getId());
        assertEquals("testpass", user.getPassword());

        dao.remove(user.getId());
        flush();
        
        try {
            dao.get(user.getId());
            fail("getUser didn't throw DataAccessException");
        } catch (DataAccessException d) {
            assertNotNull(d);
        }
    }
    
    public void testUserExists() throws Exception {
        boolean b = dao.exists(-1);
        assertTrue(b);
    }
    
    public void testUserNotExists() throws Exception {
        boolean b = dao.exists(111);
        assertFalse(b);
    }
}
