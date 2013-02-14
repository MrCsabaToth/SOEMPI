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
package org.openhie.openempi.transformation;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.transformation.function.TransformationFunction;

public class TransformationServiceTest extends BaseServiceTestCase
{
	
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}

	public void testGetTransformationFunctionTypes() {
		TransformationService transformationService = Context.getTransformationService();
		TransformationFunctionType[] list = transformationService.getTransformationFunctionTypes();
		for (TransformationFunctionType tft : list) {
			System.out.println("Found Transformation Function Type: " + tft.getName());
			TransformationFunction tf = tft.getTransformationFunction();
			System.out.println("\tTransformation Function: " + tf.getName());
		}
	}

	public void testGetTransformationFunctionNames() {
		TransformationService transformationService = Context.getTransformationService();
		List<String> list = transformationService.getAllTransformationFunctionNames();
		System.out.println("All Transformation Function names");
		for (String name : list) {
			System.out.println("\t" + name);
		}
		list = transformationService.getTransformationFunctionNames(FieldType.FieldTypeEnum.String);
		System.out.println("String Transformation Function names");
		for (String name : list) {
			System.out.println("\t" + name);
		}
		list = transformationService.getTransformationFunctionNames(FieldType.FieldTypeEnum.Blob);
		System.out.println("Stream Transformation Function names");
		for (String name : list) {
			System.out.println("\t" + name);
		}
	}

}
