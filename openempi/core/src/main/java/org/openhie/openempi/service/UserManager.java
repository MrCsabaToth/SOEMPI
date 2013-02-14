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

import java.util.List;

import org.springframework.security.userdetails.UsernameNotFoundException;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.Dataset;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 */
public interface UserManager extends UniversalManager {

    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     * @param userDao the UserDao implementation to use
     */
    void setUserDao(UserDao userDao);

    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Authentication method. Currently the only authentication method available is using
     * username/password pair credentials. In the future we need to add support for certificate
     * based authentication.
     */
    public User authenticate(String username, String password) throws AuthenticationException;
    
    /**
     * Authentication method using a session Key. This authentication method is used
     * by the EJB layer for the purpose of being able to maintain a conversation with a service
     * after successful authentication using another method.
     * 
     * @param sessionKey Obtained after successful authentication and used by EJB layer methods 
     * to have a "stateful-like" interaction regarding authentication with the stateless services
     * 
     * @return If the session key is recognized and the authentication attempt is successful, the
     * User object of the authenticated user is returned.
     * 
     * @throws AuthenticationException
     */
    public User authenticate(String sessionKey) throws AuthenticationException;
    
    /**
     * Creates a new session usually as a result of a successful authentication event. End users
     * can utilize the sessionId for subsequent requests.
     */
    String createSession(User user);
    
    /**
    /**
     * Finds a user by their username.
     * @param username the user's username used to login
     * @return User a populated user object
     * @throws org.springframework.security.userdetails.UsernameNotFoundException
     *         exception thrown when user not found
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     */
    List<User> getUsers(User user);

    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    User saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
    
}
