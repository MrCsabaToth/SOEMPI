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
package org.openempi.webapp.client.model;

import java.io.Serializable;

public class LoaderTargetFieldWeb extends ColumnSpecificationWeb implements Serializable
{
	private static final long serialVersionUID = 7062526016395000725L;

	public static final String FIELD_TRANSFORMATION = "fieldTransformation";
	public static final String FIELD_TRANSFORMATION_NAME = "fieldTransformationName";	// redundant field, comes from fieldTransformation.functionName
	// fields for rendered buttons
	public static final String ADD_BUTTON = "addButton";
	public static final String EDIT_BUTTON = "editButton";
	public static final String DELETE_BUTTON = "deleteButton";
	public static final String DND_IMAGE = "dndImage";

	public FunctionFieldWeb dummyFunctionField;	// This is need so FunctionFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public LoaderTargetFieldWeb() {
	}

	public LoaderTargetFieldWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
		setAddButton("+");
		setEditButton("E");
		setDeleteButton("X");
		setDndImage("M");
	}
	
	public LoaderTargetFieldWeb(String fieldName, String fieldTransformation) {
		set(FIELD_NAME, fieldName);
		set(FIELD_TRANSFORMATION, fieldTransformation);
		setAddButton("+");
		setEditButton("E");
		setDeleteButton("X");
		setDndImage("M");
	}

	public FunctionFieldWeb getFieldTransformation() {
		return get(FIELD_TRANSFORMATION);
	}

	public void setFieldTransformation(FunctionFieldWeb fieldTransformation) {
		set(FIELD_TRANSFORMATION, fieldTransformation);
	}

	public String getFieldTransformationName() {
		return get(FIELD_TRANSFORMATION_NAME);
	}

	public void setFieldTransformationName(String fieldTransformationName) {
		set(FIELD_TRANSFORMATION_NAME, fieldTransformationName);
	}

	public void updateRedunantFields()
	{
		FunctionFieldWeb transformationFunction = getFieldTransformation();
		if (transformationFunction != null)
			setFieldTransformationName(transformationFunction.getFunctionName());
		else
			setFieldTransformationName(null);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + FIELD_TRANSFORMATION + ": ").append(getFieldTransformation());
		return sb.toString();
	}

	public String getAddButton() {
		return get(ADD_BUTTON);
	}

	public void setAddButton(String addButton) {
		set(ADD_BUTTON, addButton);
	}

	public String getEditButton() {
		return get(EDIT_BUTTON);
	}

	public void setEditButton(String editButton) {
		set(EDIT_BUTTON, editButton);
	}

	public String getDeleteButton() {
		return get(DELETE_BUTTON);
	}

	public void setDeleteButton(String deleteButton) {
		set(DELETE_BUTTON, deleteButton);
	}

	public String getDndImage() {
		return get(DND_IMAGE);
	}

	public void setDndImage(String dndImage) {
		set(DND_IMAGE, dndImage);
	}

}
