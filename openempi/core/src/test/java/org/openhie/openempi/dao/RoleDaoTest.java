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

import java.util.HashMap;
import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.model.Role;

public class RoleDaoTest extends BaseDaoTestCase {
    private RoleDao dao;

    public void setRoleDao(RoleDao dao) {
        this.dao = dao;
    }

    public void testGetRoleInvalid() throws Exception {
        Role role = dao.getRoleByName("badrolename");
        assertNull(role);
    }

    public void testGetRole() throws Exception {
        Role role = dao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role);
    }

    public void testUpdateRole() throws Exception {
        Role role = dao.getRoleByName("ROLE_USER");
        role.setDescription("test descr");
        dao.save(role);
        flush();
        
        role = dao.getRoleByName("ROLE_USER");
        assertEquals("test descr", role.getDescription());
    }

    public void testAddAndRemoveRole() throws Exception {
        Role role = new Role("testrole");
        role.setDescription("new role descr");
        dao.save(role);
        flush();
        
        role = dao.getRoleByName("testrole");
        assertNotNull(role.getDescription());

        dao.removeRole("testrole");
        flush();

        role = dao.getRoleByName("testrole");
        assertNull(role);
    }
    
    /**
     * Tests the generic findByNamedQuery method
     * @throws Exception
     */
    public void testFindByNamedQuery() throws Exception {
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("name", Constants.USER_ROLE);
        List<Role> roles = dao.findByNamedQuery("findRoleByName", queryParams);
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }
}
