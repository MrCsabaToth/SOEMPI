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
package org.openhie.openempi.blocking.basicblocking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.BaseFieldPair;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.matching.fellegisunter.FellegiSunterParameters;
import org.openhie.openempi.model.NamePairValuePair;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.util.ParallelIteratorUtil;
import org.openhie.openempi.util.ParallelNameValuePairIterator;

public class BasicRecordPairSource implements RecordPairSource
{
	protected final Log log = LogFactory.getLog(getClass());
	private List<BlockingRound> blockingRounds;
	private PersonDao blockingDao;
	private List<List<NamePairValuePair>> valueList;
	private boolean distinctBinsMode;	// true - inhibit check for existing gathered record pairs

	public BasicRecordPairSource(boolean distinctBinsMode) {
		this.distinctBinsMode = distinctBinsMode;
	}

	public void init(String leftTableName, String rightTableName) {
		log.trace("Initializing the Basic Record Pair Source");
		for (BlockingRound round : blockingRounds) {
			List<BaseFieldPair> fields = round.getFields();
			List<String> leftFields = new ArrayList<String>();
			List<String> rightFields = new ArrayList<String>();
			getBlockingFieldList(fields, leftFields, rightFields);
			List<List<NameValuePair>> leftValues = blockingDao.getDistinctValues(leftTableName, leftFields);
			List<List<NameValuePair>> rightValues = blockingDao.getDistinctValues(rightTableName, rightFields);
			addValueListFromLeftAndRightValues(leftValues, rightValues);
			log.debug("Name-value pair list list size: " + valueList.size());
		}
	}

	private void getBlockingFieldList(List<BaseFieldPair> fields, List<String> leftFields, List<String> rightFields) {
		for (BaseFieldPair field : fields) {
			leftFields.add(field.getLeftFieldName());
			rightFields.add(field.getRightFieldName());
		}
	}

	private void addValueListFromLeftAndRightValues(List<List<NameValuePair>> leftValues,
			List<List<NameValuePair>> rightValues)
	{
		valueList = new ArrayList<List<NamePairValuePair>>();
		for (List<NameValuePair> lvs : leftValues) {
			for (List<NameValuePair> rvs : rightValues) {
				boolean sameValues = ParallelIteratorUtil.iterate(lvs, rvs, null,
						new ParallelNameValuePairIterator<NameValuePair, NameValuePair>() {
					public boolean each(NameValuePair lp, NameValuePair rp, List<NamePairValuePair> valuePairList) {
						if (lp.getValue() instanceof byte[]) {
							return Arrays.equals((byte[])lp.getValue(), (byte[])rp.getValue());
						} else if (!lp.getValue().equals(rp.getValue())) {
							return false;
						}
						return true;
					}
				});
				if (sameValues) {
					// construct NamePairValuePair
					List<NamePairValuePair> valuePairList = new ArrayList<NamePairValuePair>();
					ParallelIteratorUtil.iterate(lvs, rvs, valuePairList,
							new ParallelNameValuePairIterator<NameValuePair, NameValuePair>() {
						public boolean each(NameValuePair lp, NameValuePair rp, List<NamePairValuePair> valuePairList) {
							NamePairValuePair npvp = new NamePairValuePair();
							npvp.setLeftName(lp.getName());
							npvp.setRightName(rp.getName());
							npvp.setValue(lp.getValue());
							valuePairList.add(npvp);
							return true;
						}
					});
					valueList.add(valuePairList);
				}
			}
		}
	}

	public List<List<NamePairValuePair>> getBlockingValueList() {
		return valueList;
	}

	public RecordPairIterator iterator(String leftTableName, String rightTableName, boolean emOnly,
			FellegiSunterParameters fellegiSunterParameters) {
		BasicRecordPairIterator iterator = new BasicRecordPairIterator(this, leftTableName, rightTableName,
				distinctBinsMode, emOnly, fellegiSunterParameters);
		return iterator;
	}

	public List<BlockingRound> getBlockingRounds() {
		return blockingRounds;
	}

	public void setBlockingRounds(List<BlockingRound> blockingRounds) {
		this.blockingRounds = blockingRounds;
	}
	
	public PersonDao getBlockingDao() {
		return blockingDao;
	}

	public void setBlockingDao(PersonDao blockingDao) {
		this.blockingDao = blockingDao;
	}

}
