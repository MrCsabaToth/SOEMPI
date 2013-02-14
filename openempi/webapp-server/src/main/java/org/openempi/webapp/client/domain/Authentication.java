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
package org.openempi.webapp.client.domain;

import java.io.Serializable;

/**
 * Holds user login information
 * Borrowed from Beginning Google Web Toolkit Book
 *
 * @author Uri Boness
 */
public class Authentication implements Serializable {

    /**
     * An authentication object representing an anonymous user.
     */
    public final static Authentication ANONYMOUS = new Authentication();

    private String username;
    private String password;

    /**
     * Constructs a new Anonymous Authentication
     */
    public Authentication() {
        username = "anonymous";
        password = "anonymous";
    }

    /**
     * Constructs a new Authentication with given username and password.
     *
     * @param username The username.
     * @param password The password.
     */
    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Authentication(String username) {
    	this(username,"");
    }

    /**
     * Returns the username of this authentication.
     *
     * @return The username of this authentication.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this authentication.
     *
     * @return The password of this authentication.
     */
    public String getPassword() {
        return password;
    }

}
