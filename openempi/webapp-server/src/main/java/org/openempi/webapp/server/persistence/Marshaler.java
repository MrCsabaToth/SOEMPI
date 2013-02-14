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
package org.openempi.webapp.server.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.openempi.webapp.client.domain.Options;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * 
 * Marshal and unmarshal XML 
 * 
 */
public abstract class Marshaler {
    private static XStream xStream = initializeXStream();

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    public static String toXML(Object xmlTree) {
        return XML_HEADER + toXMLNoHeader(xmlTree);
    }
    
    public static String toXMLNoHeader(Object xmlTree) {
        return xStream.toXML(xmlTree);
    }

    public static void toXML(Object xmlTree, FileOutputStream fos) {
        xStream.toXML(xmlTree, fos);
    }

    public static Options optionsFromFile(FileInputStream fis) {
    	Options options = new Options();
        xStream.fromXML(fis, options);
        return options;
    }

    private static XStream initializeXStream() {
        // This constructor prevents underscores from being replaced with double
        // underscores
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyReplacer("|", "_")));

        //if you use annotated xstream POJOs register them here
//      xStream.processAnnotations(CssParameter.class);
        return xStream;
    }
}
