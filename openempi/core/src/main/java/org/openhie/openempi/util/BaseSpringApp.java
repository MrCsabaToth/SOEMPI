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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class BaseSpringApp
{
	private String[] DEFAULT_CONTEXT_RESOURCES = {
	        "/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
	        "/applicationContext-service.xml", "classpath*:/**/applicationContext.xml"
		};
    protected final Log log = LogFactory.getLog(getClass());

	
	private GenericApplicationContext applicationContext;

	public void startup() {
		Context.startup();
		Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
	}
	
	public void startup(String[] locations) {
		Context.startup();
		Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
	}
	
	public final ConfigurableApplicationContext getApplicationContext() {
		// lazy load, in case startup() has not yet been called.
		if (applicationContext == null) {
			try {
				startup(DEFAULT_CONTEXT_RESOURCES);
			}
			catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Caught exception while retrieving the ApplicationContext for application ["
							+ getClass().getName() + "].", e);
				}
			}
		}
		return this.applicationContext;
	}
}
