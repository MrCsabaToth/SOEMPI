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
package org.openhie.openempi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConvertingWrapDynaBean extends org.apache.commons.beanutils.ConvertingWrapDynaBean
{
	private static final long serialVersionUID = -370897935825063946L;
	
	protected final Log log = LogFactory.getLog(getClass());

	public ConvertingWrapDynaBean(Object instance) {
		 super(instance);
	 }
	 
	 public Object get(String name) {
		 Object value = null;
		 try {	
			 value = PropertyUtils.getNestedProperty(instance, name);
		 } catch (InvocationTargetException ite) {
			 Throwable cause = ite.getTargetException();
			 throw new IllegalArgumentException("Error reading property '" + name +
					 "' nested exception - " + cause);
		 } catch (Throwable t) {
			 throw new IllegalArgumentException("Error reading property '" + name +
					 "', exception - " + t);
		 }
		 return (value);
	 }
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<String> getPropertyNames() {
		 Set<String> properties = new HashSet<String>();
		 try {
			 Map map = BeanUtils.describe(getInstance());
			 for (Iterator<String> iter = (Iterator<String>) map.keySet().iterator(); iter.hasNext(); ) {
				 String name = iter.next();
				 if (!name.equals("class")) {
					 properties.add(name);
				 }
			 }
		 } catch (Exception e) {
			 log.warn("Failed while extracting bean properties: " + e, e);
		 }
		 return properties;
	 }
}
