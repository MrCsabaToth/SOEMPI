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

public class MatchFieldWeb extends BaseFieldPairWeb implements Serializable
{
	private static final long serialVersionUID = -4740682528149680879L;

	public static final String AGREEMENT_PROBABILITY = "agreementProbability";
	public static final String DISAGREEMENT_PROBABILITY = "disagreementProbability";
	public static final String COMPARATOR_FUNCTION = "comparatorFunction";
	public static final String COMPARATOR_FUNCTION_NAME = "comparatorFunctionName";	// redundant field, comes from comparatorFunction.comparatorFunctionName
	public static final String MATCH_THRESHOLD = "matchThreshold";
	// fields for rendered buttons
	public static final String ADD_BUTTON = "addButton";
	public static final String EDIT_BUTTON = "editButton";
	public static final String DELETE_BUTTON = "deleteButton";
	public static final String DND_IMAGE = "dndImage";

	public BaseFieldWeb dummyBaseField;	// This is need so BaseFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public FunctionFieldWeb dummyFunctionField;	// This is need so FunctionFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public MatchFieldWeb() {
		setAddButton("+");
		setEditButton("E");
		setDeleteButton("X");
		setDndImage("M");
	}

	public Double getAgreementProbability() {
		return get(AGREEMENT_PROBABILITY);
	}

	public void setAgreementProbability(Double agreementProbability) {
		set(AGREEMENT_PROBABILITY, agreementProbability);
	}

	public Double getDisagreementProbability() {
		return get(DISAGREEMENT_PROBABILITY);
	}

	public void setDisagreementProbability(Double disagreementProbability) {
		set(DISAGREEMENT_PROBABILITY, disagreementProbability);
	}

	public FunctionFieldWeb getComparatorFunction() {
		return get(COMPARATOR_FUNCTION);
	}

	public void setComparatorFunction(FunctionFieldWeb comparatorFunction) {
		set(COMPARATOR_FUNCTION, comparatorFunction);
	}

	public String getComparatorFunctionName() {
		return get(COMPARATOR_FUNCTION_NAME);
	}

	public void setComparatorFunctionName(String comparatorFunctionName) {
		FunctionFieldWeb functionField = getComparatorFunction();
		functionField.setFunctionName(comparatorFunctionName);
		set(COMPARATOR_FUNCTION_NAME, comparatorFunctionName);
	}

	public Double getMatchThreshold() {
		return get(MATCH_THRESHOLD);
	}

	public void setMatchThreshold(Double matchThreshold) {
		set(MATCH_THRESHOLD, matchThreshold);
	}

	public void updateRedunantFields()
	{
		FunctionFieldWeb functionField = getComparatorFunction();
		setComparatorFunctionName(functionField.getFunctionName());
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
