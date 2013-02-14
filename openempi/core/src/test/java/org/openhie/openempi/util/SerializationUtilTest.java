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

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializationUtilTest extends TestCase {
    //~ Instance fields ========================================================

    private final Log log = LogFactory.getLog(SerializationUtilTest.class);

    //~ Constructors ===========================================================

    public SerializationUtilTest(String name) {
        super(name);
    }

    public void testSerializeObject() {
    	String blablaStr = "blabla0123";
    	String tempFolder = "/tmp";
    	String blablaFileName1 = "blabla1.ser";
    	SerializationUtil.serializeObject(tempFolder, blablaFileName1, blablaStr);
    	String balbla1DesStr = (String) SerializationUtil.deserializeObject(tempFolder, blablaFileName1);
        assertEquals(blablaStr, balbla1DesStr);

        String blablaFileName2 = "blabla2.ser";
        String blablaFileFullName2 = tempFolder + "/" + blablaFileName2;
    	SerializationUtil.serializeObject(tempFolder, blablaFileName2, blablaStr);
    	String balbla2DesStr = (String) SerializationUtil.deserializeObject(blablaFileFullName2);
        assertEquals(blablaStr, balbla2DesStr);
    }

    public void testSerializByteArray() {
    	String blablaStr = "blabla0123";
    	byte[] blablaObjectBytes = SerializationUtil.serializeObject(blablaStr);
    	String balblaDesStr = (String) SerializationUtil.deserializeObject(blablaObjectBytes);
        assertEquals(blablaStr, balblaDesStr);

    	byte[] byteArray = new byte[256];
    	byte b = -128;
    	for (int i = 0; i < 256; i++) {
    		byteArray[i] = b;
    		b++;
    	}
    	byte[] byteArrayBytes = SerializationUtil.serializeObject(byteArray);
    	byte[] byteArrayDes = (byte[]) SerializationUtil.deserializeObject(byteArrayBytes);
    	for (int i = 0; i < byteArray.length; i++) {
            assertEquals(byteArray[i], byteArrayDes[i]);
    	}
    }

}
