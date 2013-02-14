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
package org.openempi.webapp.server.util;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class BaseModelDataDtoGenerator
{
	private static Logger log = Logger.getLogger(BaseModelDataDtoGenerator.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: BaseModelDataDtoGenerator <FQ-Class-Name> <Destination-Package-Name>");
			System.exit(-1);
		}
		
		String fqClassName = args[0];
		String className = extractClassName(fqClassName);
		String packageName = args[1];
		log.debug("Generating DTO for class: " + fqClassName + " into class " + className);
		StringBuilder sourceCode = generateClassHeader(packageName, className);
		Class<?> beanClass = Class.forName(fqClassName);
		PropertyDescriptor[] descs = PropertyUtils.getPropertyDescriptors(beanClass);
		for (PropertyDescriptor desc : descs) {
			log.debug(desc.getName() + " of type " + desc.getPropertyType());
			
			if (!desc.getName().equalsIgnoreCase("class") && !desc.getPropertyType().getCanonicalName().startsWith("org")) {
				generateGetterSetter(sourceCode, desc);
			}
		}
		sourceCode.append("}");
		System.out.println(sourceCode.toString());
	}

	private static void generateGetterSetter(StringBuilder sourceCode, PropertyDescriptor desc) {
		// getter method declaration
		sourceCode.append("\tpublic ").append(desc.getPropertyType().getCanonicalName()).append(" ").append(desc.getReadMethod().getName()).append("() {\n");
		
		// getter method body
		sourceCode.append("\t\treturn get(\"").append(desc.getName()).append("\");\n\t}\n\n");
		
		// setter method declaration
		sourceCode.append("\tpublic void ").append(desc.getWriteMethod().getName()).append("(")
			.append(desc.getPropertyType().getCanonicalName()).append(" ").append(desc.getName()).append(") {\n");

		// getter method body
		sourceCode.append("\t\tset(\"").append(desc.getName()).append("\", ").append(desc.getName()).append(");\n\t}\n\n");
	}

	private static StringBuilder generateClassHeader(String packageName, String className) {
		StringBuilder code = new StringBuilder("package ");
		// package
		code.append(packageName).append(";\n\n");
		
		// import section
		code.append("import com.extjs.gxt.ui.client.data.BaseModelData;\n\n");
		
		// class definition section
		code.append("public class ").append(className).append(" extends BaseModelData\n{\n");
		
		// class constructor
		code.append("\tpublic ").append(className).append("() {\n\t}\n\n");

		return code;
	}

	private static String extractClassName(String fqClassName) {
		int index = fqClassName.lastIndexOf('.');
		return fqClassName.substring(index + 1);
	}

}
