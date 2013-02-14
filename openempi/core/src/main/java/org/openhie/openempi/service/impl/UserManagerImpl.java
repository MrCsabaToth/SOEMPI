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

import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityExistsException;

import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.dao.UserSessionDao;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserSession;
import org.openhie.openempi.service.UserExistsException;
import org.openhie.openempi.service.UserManager;
import org.openhie.openempi.service.UserService;
import org.openhie.openempi.util.SessionGenerator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UsernameNotFoundException;


/**
 * Implementation of UserManager interface.
 *
 * @author Odysseas Pentakalos
 */
@WebService(serviceName = "UserService", endpointInterface = "org.openhie.openempi.service.UserService")
public class UserManagerImpl extends UniversalManagerImpl implements UserManager, UserService
{
    private UserDao dao;
    private UserSessionDao userSessionDao;
    private PasswordEncoder passwordEncoder;

    /**
     * Set the PasswordEncoder used to encrypt passwords.
     * @param passwordEncoder the PasswordEncoder implementation
     */
    @Required
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return dao.get(new Integer(userId));
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(User user) {
        return dao.getUsers();
    }
    
    public String createSession(User user) {
    	String sessionKey = SessionGenerator.generateSessionId();
    	UserSession userSession = new UserSession(sessionKey, user, new java.util.Date());
    	userSessionDao.saveUserSession(userSession);
    	return sessionKey;
    }
    
    public User authenticate(String username, String password) throws AuthenticationException {
		log.debug("Authentication request for user with username: " + username);
		if (username == null || password == null) {
			log.warn("Authentication attempt failed due to missing credentials: username=" + username + "; password=" + password);
			throw new AuthenticationException("Authentication failed; no credentials were provided in the request.");
		}
		
		User user = (User) getUserByUsername(username);
		if (user == null) {
			log.warn("Authentication attempt with unknown username " + username);
			throw new AuthenticationException("Authentication failed.");
		}
		
		String encrypted = passwordEncoder.encodePassword(password, null);
		log.debug("Encrypted password is " + encrypted + " as opposed to " + user.getPassword());
		if (!encrypted.equalsIgnoreCase(user.getPassword())) {
			log.warn("Authentication attempt failed due to incorrect password.");
			throw new AuthenticationException("Authentication failed.");
		}
		return user;
    }
    
    public User authenticate(String sessionKey) throws AuthenticationException {
    	log.debug("Authentication request for user with session id: " + sessionKey);
    	UserSession userSession = userSessionDao.findBySessionKey(sessionKey);
    	if (userSession == null) {
    		log.warn("Authentication attempt failed due to invalid session key: " + sessionKey);
    		throw new AuthenticationException("Invalid session key");
    	}
    	log.debug("Authentication attempt succeeded for user: " + userSession.getUser().getUsername() + " and session key " + sessionKey);
    	return userSession.getUser();
    }
    
    /** 
     * TODO Need to add support for a logout operation that removes the session from the system
     */
    public void logout(UserSession userSession) {
    	
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }
        
        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = dao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }
        
        try {
            return dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        dao.remove(new Integer(userId));
    }

    /**
     * {@inheritDoc}
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) dao.loadUserByUsername(username);
    }

	public UserSessionDao getUserSessionDao() {
		return userSessionDao;
	}

	public void setUserSessionDao(UserSessionDao userSessionDao) {
		this.userSessionDao = userSessionDao;
	}
	
    /**
     * Set the Dao for communication with the data layer.
     * @param dao the UserDao that communicates with the database
     */
    @Required
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }
}
