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
 * Thrown when an authentication fails in the SecurityManager.
 * Borrowed from Beginning Google Web Toolkit Book
 * 
 * @author Uri Boness
 */
public class AuthenticationException extends Exception implements Serializable {

    /**
     * Construct a new AuthenticationException
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException with a given message.
     *
     * @param message The given message.
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with a given message and the root cause.
     *
     * @param message The given message.
     * @param cause The root cause of the failure.
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
