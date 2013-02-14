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
package org.openhie.openempi.loader.configuration;

import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.model.ColumnSpecification;

public class LoaderTargetField extends ColumnSpecification
{
	private static final long serialVersionUID = -8158684309625898508L;

	private FunctionField fieldTransformation;

	public LoaderTargetField()
	{
	}
	
	public FunctionField getFieldTransformation() {
		return fieldTransformation;
	}

	public void setFieldTransformation(FunctionField fieldTransformation) {
		this.fieldTransformation = fieldTransformation;
	}

}
