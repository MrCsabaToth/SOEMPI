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
package org.openhie.openempi.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;

public class FieldDaoTest extends BaseDaoTestCase
{
	private FieldDao fieldDao;
	private FieldTypeEnum[] fieldTypeNames = FieldType.FieldTypeEnum.values();
	private FieldMeaningEnum[] fieldMeaningNames = FieldMeaning.FieldMeaningEnum.values();

	public FieldDao getFieldDao() {
		return fieldDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

	@Test
	public void testFindFieldTypeByName() {
		List<FieldType> fieldTypes = new ArrayList<FieldType>();
		for(int i = 0; i < fieldTypeNames.length; i++) {
			FieldType fieldType = fieldDao.findFieldTypeByName(fieldTypeNames[i].name());
			assertTrue(fieldType != null);
			if (fieldType != null)
				fieldTypes.add(fieldType);
			System.out.println("FieldTypeByName " + fieldTypeNames[i].name() + ": " + fieldType);
		}
	}

	@Test
	public void testFindFieldMeaningByName() {
		List<FieldMeaning> fieldMeanings = new ArrayList<FieldMeaning>();
		for(int i = 0; i < fieldMeaningNames.length; i++) {
			FieldMeaning fieldMeaning = fieldDao.findFieldMeaningByName(fieldMeaningNames[i].name());
			assertTrue(fieldMeaning != null);
			if (fieldMeaning != null)
				fieldMeanings.add(fieldMeaning);
			System.out.println("FieldMeaningByName " + fieldMeaningNames[i].name() + ": " + fieldMeaning);
		}
	}

	@Test
	public void testFindFieldTypeByCode() {
		List<FieldType> fieldTypes = new ArrayList<FieldType>();
		for(int i = 0; i < fieldTypeNames.length; i++) {
			fieldTypes.add(fieldDao.findFieldTypeByName(fieldTypeNames[i].name()));
		}
		for(FieldType fieldType  : fieldTypes) {
			FieldType fieldType2 = fieldDao.findFieldTypeByCode(fieldType.getFieldTypeCode());
			assertTrue(fieldType2 != null);
			assertTrue(fieldType.equals(fieldType2));
			System.out.println("FieldTypeByCode " + fieldType2.getFieldTypeCode() + ": " + fieldType2);
		}
	}

	@Test
	public void testFindFieldMeaningByCode() {
		List<FieldMeaning> fieldMeanings = new ArrayList<FieldMeaning>();
		for(int i = 0; i < fieldMeaningNames.length; i++) {
			fieldMeanings.add(fieldDao.findFieldMeaningByName(fieldMeaningNames[i].name()));
		}
		for(FieldMeaning fieldMeaning : fieldMeanings) {
			FieldMeaning fieldMeaning2 = fieldDao.findFieldMeaningByCode(fieldMeaning.getFieldMeaningCode());
			assertTrue(fieldMeaning2 != null);
			assertTrue(fieldMeaning.equals(fieldMeaning2));
			System.out.println("FieldMeaningByCode " + fieldMeaning2.getFieldMeaningCode() + ": " + fieldMeaning2);
		}
	}

}
