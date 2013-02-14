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
package org.openempi.webapp.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;

public class InitializationServlet extends HttpServlet
{
	private static final long serialVersionUID = 4750862367734490578L;

	private Logger log = Logger.getLogger(getClass());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Context.startup();
			config.getServletContext().setAttribute(WebappConstants.APPLICATION_CONTEXT, Context.getApplicationContext());
		} catch (Throwable t) {
			log.error("Failed to start: " + t.getMessage(), t);
		}
	}

}
