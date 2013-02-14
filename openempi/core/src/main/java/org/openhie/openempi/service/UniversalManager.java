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

import java.io.Serializable;
import java.util.List;

/**
 * Business Facade interface. 
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * Modifications and comments by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * This thing used to be named simply 'GenericManager' in versions of AppFuse prior to 2.0.
 * It was renamed in an attempt to distinguish and describe it as something
 * different than GenericManager.  GenericManager is intended for sub-classing, and was
 * named Generic because 1) it has very general functionality and 2) is
 * 'generic' in the Java 5 sense of the word it uses Generics.
 *
 * Implementations of this class are not intended for sub-classing. You most
 * likely would want to subclass GenericManager.  The only real difference is that
 * instances of Class are passed into the methods in this class, and
 * they are part of the constructor in the GenericManager, hence you'll have to do
 * some casting if you use this one.
 *
 * @see com.einvite.service.GenericManager
 */
public interface UniversalManager {
    /**
     * Generic method used to get a all objects of a particular type. 
     * @param clazz the type of objects 
     * @return List of populated objects
     */
    @SuppressWarnings("unchecked")
	List getAll(Class clazz);

    /**
     * Generic method to get an object based on class and identifier. 
     * 
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     * @return a populated object 
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    @SuppressWarnings("unchecked")
	Object get(Class clazz, Serializable id);

    /**
     * Generic method to save an object.
     * @param o the object to save
     * @return a populated object
     */
    Object save(Object o);

    /**
     * Generic method to delete an object based on class and id
     * @param clazz model class to lookup
     * @param id the identifier of the class
     */
    @SuppressWarnings("unchecked")
	void remove(Class clazz, Serializable id);
}
