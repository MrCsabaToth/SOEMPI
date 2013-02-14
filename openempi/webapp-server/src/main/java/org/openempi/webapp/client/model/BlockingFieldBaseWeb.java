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

public class BlockingFieldBaseWeb extends BaseFieldPairWeb
{
	private static final long serialVersionUID = -9156981840739861642L;

	public static final String FIELD_INDEX = "fieldIndex";
	public static final String BLOCKING_ROUND = "blockingRound";

	public BlockingFieldBaseWeb() {
	}

	public BlockingFieldBaseWeb(Integer blockingRound, Integer fieldIndex,
			String leftFieldName, String rightFieldName)
	{
		set(BLOCKING_ROUND, blockingRound);
		set(FIELD_INDEX, fieldIndex);
		set(LEFT_FIELD_NAME, leftFieldName);
		set(RIGHT_FIELD_NAME, rightFieldName);
	}
	
	public Integer getBlockingRound() {
		return get(BLOCKING_ROUND);
	}

	public void setBlockingRound(Integer blockingRound) {
		set(BLOCKING_ROUND, blockingRound);
	}
	
	public Integer getFieldIndex() {
		return get(FIELD_INDEX);
	}

	public void setFieldIndex(Integer fieldIndex) {
		set(FIELD_INDEX, fieldIndex);
	}
}
