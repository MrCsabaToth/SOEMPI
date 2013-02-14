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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.domain.Options;
import org.openempi.webapp.server.persistence.Marshaler;


/**
 * 
 * Not a remote service, just a POJO with options storing and retrieving values.
 * This is separate (not in the Options class) and on the server to make sure 
 * that options are always stored in one place (on the server)
 * 
 * Saves and loads Options from the place where you start the server.
 * In hosted mode with maven this is just inside the web project
 * if you make a war and drop it into an app server, look in the bin dir
 * where you started the server.
 * 
 */
public class OptionsServiceImpl {

    public static final File OPTIES_FILE = new File("gwt-app-options.xml");
    public static final Logger LOGGER = Logger.getLogger(OptionsServiceImpl.class);

    /**
     * Serialize to XML
     */
    public void saveOpties(Options opties) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(OPTIES_FILE);
            Marshaler.toXML(opties, fos);
            LOGGER.info("opties opslaan naar " + OPTIES_FILE.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Deserialize from XML
     * 
     * @return deserialzed Options or new Options object if not found
     */
    public Options loadOptions() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(OPTIES_FILE);
            return Marshaler.optionsFromFile(fis);
        } catch (FileNotFoundException e) {
            LOGGER.debug("Make new Options...");
            return new Options();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

}
