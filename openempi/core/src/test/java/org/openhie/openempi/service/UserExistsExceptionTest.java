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
package org.openhie.openempi.service;

import org.openhie.openempi.model.User;
import org.springframework.beans.BeanUtils;

public class UserExistsExceptionTest extends BaseManagerTestCase {
    private UserManager manager = null;

    public void setUserManager(UserManager userManager) {
        this.manager = userManager;
    }
    
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {"/applicationContext-service.xml",
                             "/applicationContext-resources.xml",
                             "classpath:/applicationContext-dao.xml"};
    }

    public void testAddExistingUser() throws Exception {
        logger.debug("entered 'testAddExistingUser' method");
        assertNotNull(manager);

        User user = manager.getUser("-1");
        
        // create new object with null id - Hibernate doesn't like setId(null)
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        user2.setId(null);
        user2.setVersion(null);
        user2.setRoles(null);
        
        // try saving as new user, this should fail b/c of unique keys
        try {
            manager.saveUser(user2);
            fail("Duplicate user didn't throw UserExistsException");
        } catch (UserExistsException uee) {
            assertNotNull(uee);
        }
    }    
}
