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
package org.openhie.openempi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.openhie.openempi.dao.FieldDao;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.model.FieldMeaning.FieldMeaningEnum;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.model.FieldType.FieldTypeEnum;
import org.openhie.openempi.service.FieldService;

public class FieldServiceImpl extends BaseServiceImpl implements FieldService
{
	protected Map<String,FieldType> fieldTypeCacheByName = new HashMap<String,FieldType>();
	protected Map<String,FieldMeaning> fieldMeaningCacheByName = new HashMap<String,FieldMeaning>();

	protected FieldDao fieldDao;

	public void init() {
		// Don't pre-cache to speed up deployment/start
		//cacheFieldTypesAndMeanings();
	}

	public FieldType findFieldTypeByName(String fieldTypeName) {
		FieldType fieldType = fieldTypeCacheByName.get(fieldTypeName);
		if (fieldType == null) {
			fieldType = fieldDao.findFieldTypeByName(fieldTypeName);
			fieldTypeCacheByName.put(fieldTypeName, fieldType);
		}
		return fieldType;
	}

	public FieldMeaning findFieldMeaningByName(String fieldMeaningName) {
		FieldMeaning fieldMeaning = fieldMeaningCacheByName.get(fieldMeaningName);
		if (fieldMeaning == null) {
			fieldMeaning = fieldDao.findFieldMeaningByName(fieldMeaningName);
			fieldMeaningCacheByName.put(fieldMeaningName, fieldMeaning);
		}
		return fieldMeaning;
	}

	public FieldType findFieldTypeByCode(String fieldTypeCode) {
		return fieldDao.findFieldTypeByCode(fieldTypeCode);
	}

	public FieldMeaning findFieldMeaningByCode(String fieldMeaningCode) {
		return fieldDao.findFieldMeaningByCode(fieldMeaningCode);
	}

	protected void cacheFieldTypesAndMeanings() {
		FieldTypeEnum[] fieldTypes = FieldType.FieldTypeEnum.values();
		for(int i = 0; i < fieldTypes.length; i++) {
			findFieldTypeByName(fieldTypes[i].name());
		}
		FieldMeaningEnum[] fieldMeanings = FieldMeaning.FieldMeaningEnum.values();
		for(int i = 0; i < fieldMeanings.length; i++) {
			findFieldMeaningByName(fieldMeanings[i].name());
		}
	}

	public FieldDao getFieldDao() {
		return fieldDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

}
