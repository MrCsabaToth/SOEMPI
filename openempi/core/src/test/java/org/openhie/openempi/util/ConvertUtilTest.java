/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.util;

import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class ConvertUtilTest extends TestCase {
    //~ Instance fields ========================================================

    private final Log log = LogFactory.getLog(ConvertUtilTest.class);

    //~ Constructors ===========================================================

    public ConvertUtilTest(String name) {
        super(name);
    }

    public void testGetInternationalDatePattern() {
        LocaleContextHolder.setLocale(new Locale("nl"));
        assertEquals("dd-MMM-yyyy", DateUtil.getDatePattern());
       
        LocaleContextHolder.setLocale(Locale.FRANCE);
        assertEquals("dd/MM/yyyy", DateUtil.getDatePattern());
        
        LocaleContextHolder.setLocale(Locale.GERMANY);
        assertEquals("dd.MM.yyyy", DateUtil.getDatePattern());
        
        // non-existant bundle should default to default locale
        LocaleContextHolder.setLocale(new Locale("fi"));
        String fiPattern = DateUtil.getDatePattern();
        LocaleContextHolder.setLocale(Locale.getDefault());
        String defaultPattern = DateUtil.getDatePattern();
        
        assertEquals(defaultPattern, fiPattern);
    }

    public void testGetDate() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("db date to convert: " + new Date());
        }

        String date = DateUtil.getDate(new Date());

        if (log.isDebugEnabled()) {
            log.debug("converted ui date: " + date);
        }

        assertTrue(date != null);
    }
    
    public void testGetDateTime() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetDateTime' method");
        }
        String now = DateUtil.getTimeNow(new Date());
        assertTrue(now != null);
        log.debug(now);
    }
}
