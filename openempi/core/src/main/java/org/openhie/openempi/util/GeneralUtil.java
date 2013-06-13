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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.FunctionField;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.loader.configuration.LoaderFieldComposition;
import org.openhie.openempi.loader.configuration.LoaderSubField;
import org.openhie.openempi.loader.configuration.LoaderTargetField;
import org.openhie.openempi.matching.fellegisunter.MatchField;
import org.openhie.openempi.model.ColumnInformation;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LeanRecordPair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.User;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;


/**
 * Utility class for serialization.
 * 
 * @author <a href="mailto:csaba.toth@vanderbilt.edu">Csaba Toth</a>
 */
public final class GeneralUtil {
    private static final Log log = LogFactory.getLog(GeneralUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private GeneralUtil() {
    }

	/**
	 * Concatenate two byte arrays into one.
	 *
	 * @param b1 The first byte array.
	 * @param b2 The second byte array.
	 * @return The concatenation of b1 and b2.
	 */
	public static byte[] concatByteArraysSimple(byte[] b1, byte[] b2)
	{
		return concatByteArrays(b1, b2, 0, b2.length);
	}

	/**
	 * Concatenate two byte arrays into one.
	 *
	 * @param b1 The first byte array.
	 * @param b2 The second byte array.
	 * @param b2StartPos Copy start position of the 2nd byte array.
	 * @param b2numOfBytesToTake Copy length of the 2nd byte array.
	 * @return The concatenation of b1 and b2.
	 */
	public static byte[] concatByteArrays(byte[] b1, byte[] b2, int b2StartPos, int b2numOfBytesToTake)
	{
		byte[] b3 = new byte[b1.length + b2numOfBytesToTake];
		System.arraycopy(b1, 0, b3, 0, b1.length);
		System.arraycopy(b2, b2StartPos, b3, b1.length, b2numOfBytesToTake);
		return b3;
	}

    public static ComparisonVector scoreRecordPair(Person person, Person personOther,
    		StringComparisonService comparisonService, List<MatchField> matchFields) {
    	ComparisonVector comparisonVector = new ComparisonVector(matchFields);
		int i = 0;
		for (MatchField matchField : matchFields) {
			Object leftValue = null;
			String leftAttributeName = matchField.getLeftFieldName();
			if (person.hasAttribute(leftAttributeName))
				leftValue = person.getAttribute(leftAttributeName);
			else
				log.error("Person doesn't have expected attribute: " + leftAttributeName);
			Object rightValue = null;
			String rightAttributeName = matchField.getRightFieldName();
			if (personOther.hasAttribute(rightAttributeName))
				rightValue = personOther.getAttribute(rightAttributeName);
			else
				log.error("Person doesn't have expected attribute: " + rightAttributeName);
			double distance = Constants.DEFAULT_DISTANCE;
			if (leftValue != null && rightValue != null) {
				if (leftValue instanceof String && ((String)leftValue).equals("") &&
					rightValue instanceof String && ((String)rightValue).equals(""))
				{
					distance = 1.0;
				} else {
					DistanceMetric distanceMetric =
						comparisonService.getDistanceMetricType(matchField.getComparatorFunction().getFunctionName()).getDistanceMetric();
					distance = distanceMetric.score(leftValue, rightValue);
				}
			}
			comparisonVector.setScore(i, distance);
			i++;
		}
		return comparisonVector;
    }
    
	public static List<ColumnInformation> cloneColumnInformationList(List<ColumnInformation> sourceColumnInformationList) {
		List<ColumnInformation> columnInformationList = new ArrayList<ColumnInformation>();
		for (ColumnInformation sourceColumnInformation : sourceColumnInformationList) {
			ColumnInformation columnInformation = sourceColumnInformation.getClone();
			columnInformationList.add(columnInformation);
		}
		return columnInformationList;
	}

	public static PersonLink constructPersonLink(LeanRecordPair pair, long linkId, int linkState) {
		PersonLink personLink = constructPersonLink(linkId, pair.getLeftRecordId(),
				pair.getRightRecordId(), pair.getWeight(), linkState);
		personLink.setBinaryVector(pair.getComparisonVector().getBinaryVectorString());
		personLink.setContinousVector(pair.getComparisonVector().getScoreVectorString());
		return personLink;
	}

	public static PersonLink constructPersonLink(long linkId, long leftPersonId, long rightPersonId,
			double weight, int linkState)
	{
		PersonLink personLink = new PersonLink(linkId, leftPersonId, rightPersonId);
		personLink.setWeight(weight);
		personLink.setLinkState(linkState);
		return personLink;
	}

	private static boolean doesConfigurationNeedKeyServer(LoaderTargetField loaderTargetField) {
		FunctionField functionField = loaderTargetField.getFieldTransformation();
		if (functionField != null) {
			if (functionField.getFunctionName().contains("Bloom") ||
				functionField.getFunctionName().startsWith("HMAC"))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean doesConfigurationNeedKeyServer(LoaderConfig loaderConfiguration) {
		for (LoaderDataField loaderDataField : loaderConfiguration.getDataFields()) {
			if (doesConfigurationNeedKeyServer(loaderDataField)) {
				return true;
			}
			List<LoaderSubField> loaderSubFields = loaderDataField.getSubFields();
			if (loaderSubFields != null) {
				for (LoaderSubField loaderSubField : loaderSubFields) {
					if (doesConfigurationNeedKeyServer(loaderSubField)) {
						return true;
					}
				}
			}
			LoaderFieldComposition loaderFieldComposition = loaderDataField.getFieldComposition();
			if (loaderFieldComposition != null) {
				if (doesConfigurationNeedKeyServer(loaderFieldComposition)) {
					return true;
				}
			}
		}
		return false;
	}

}
