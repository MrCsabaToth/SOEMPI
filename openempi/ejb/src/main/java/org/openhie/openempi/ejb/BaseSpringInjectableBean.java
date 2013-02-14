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
package org.openhie.openempi.ejb;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.User;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

public abstract class BaseSpringInjectableBean extends AbstractStatelessSessionBean 
{
	private static final long serialVersionUID = 2898872461151599058L;
	
	private static boolean initialized = false;
	private static Context context;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	protected void onEjbCreate() throws CreateException {
		log.debug("onEjbCreate was invoked via SpringInjectionInterceptor. The bean factory is " + getBeanFactory());

		setContext((Context) getBeanFactory().getBean("context"));
	}

	public void init() {
		if (!initialized) {
			log.debug("init invoked.");
			try {
				ejbCreate();
				initialized = true;
			} catch (CreateException e) {
				log.error("Failed while initializing the bean: " + e, e);
			}
		}
	}
	
	public User isSessionValid(String sessionKey) {
		return Context.authenticate(sessionKey);
	}

	public static Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		BaseSpringInjectableBean.context = context;
	}
	
}
