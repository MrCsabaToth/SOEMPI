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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Utility class for serialization.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public final class SerializationUtil {
    private static final Log log = LogFactory.getLog(SerializationUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private SerializationUtil() {
    }

	public static void serializeObject(String configDirectory, String fileName, Object o) {
		String fullFilename = configDirectory + "/" + fileName;
		log.debug("Attempting to serialize object into file: " + fullFilename);
		try {
			ObjectOutputStream ois = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(fullFilename)));
			ois.writeObject(o);
			ois.flush();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while serializing object (into the file" + fullFilename + " ): " + e.getMessage(), e);
			throw new RuntimeException("Failed while serializing object (into the file" + fullFilename + " ): " + e.getMessage());
		}
	}

	public static Object deserializeObject(String configDirectory, String fileName) {
		String fullFilename = configDirectory + "/" + fileName;
		return deserializeObject(fullFilename);
	}
	
	public static Object deserializeObject(String fullFilename) {
		Object obj;
		log.debug("Attempting to deserialize object from file: " + fullFilename);
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(fullFilename)));
			obj = ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while deserializing object (from file " + fullFilename + "): " + e.getMessage(), e);
			throw new RuntimeException("Failed while deserializing object (from file " + fullFilename + "): " + e.getMessage());
		}
		return obj;
	}
	
	public static byte[] serializeObject(Object o) {
		log.debug("Attempting to serialize object into memory");
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream ois = new ObjectOutputStream(
					new BufferedOutputStream(byteArrayOutputStream));
			ois.writeObject(o);
			ois.flush();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while serializing matchConfiguration (into memory): " + e.getMessage(), e);
			throw new RuntimeException("Failed while serializing object (into memory): " + e.getMessage());
		}
		return byteArrayOutputStream.toByteArray();
	}

	public static Object deserializeObject(byte[] o) {
		Object obj;
		log.debug("Attempting to deserialize object from memory");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(o);
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(byteArrayInputStream));
			obj = ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while deserializing object (from memory): " + e.getMessage(), e);
			throw new RuntimeException("Failed while deserializing object (from memory): " + e.getMessage());
		}
		return obj;
	}
	
}
