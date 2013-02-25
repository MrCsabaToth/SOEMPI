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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.util.ConvertUtil;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public abstract class BaseManagerTestCase extends AbstractTransactionalDataSourceSpringContextTests {
    //~ Static fields/initializers =============================================

    protected final Log log = LogFactory.getLog(getClass());
    protected static ResourceBundle rb = null;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {"/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
                             "/applicationContext-service.xml", "classpath*:/applicationContext-module*.xml", 
                             "classpath*:/**/applicationContext.xml"};
    }

    //~ Constructors ===========================================================

    public BaseManagerTestCase() {
// Csaba -> Odysseas: not sure about that:
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS, "applicationContext-module-basic-blocking.xml,applicationContext-module-data-loader.xml");
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            //log.warn("No resource bundle found for: " + className);
        }
    }

    //~ Methods ================================================================

    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     *
     * @param obj the model object to populate
     * @return Object populated object
     * @throws Exception if BeanUtils fails to copy properly
     */
    @SuppressWarnings("unchecked")
	protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from
        // its .properties file
        Map map = ConvertUtil.convertBundleToMap(rb);

        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}