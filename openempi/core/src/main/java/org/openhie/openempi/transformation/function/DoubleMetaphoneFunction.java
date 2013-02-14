/**
 *
 *  Copyright (C) 2009 SYSNET International, Inc. <support@sysnetint.com>
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
package org.openhie.openempi.transformation.function;

import org.apache.commons.codec.language.DoubleMetaphone;

public class DoubleMetaphoneFunction extends AbstractTransformationFunction
{
	private DoubleMetaphone metaphone;
	
	public DoubleMetaphoneFunction() {
		super();
		metaphone = new DoubleMetaphone();
	}
	
	public Object transform(Object field, java.util.Map<String, Object> parameters) {
		log.debug("Applying the double metaphone transform to field with value: " + field);
		if (field == null) {
			return null;
		}
		String encodedValue = metaphone.encode(field.toString());
		log.debug("The double metaphone value for field: '" + field + "' is '" + encodedValue + "'");
		return encodedValue;
	}
}
