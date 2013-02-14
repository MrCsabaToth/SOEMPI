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
package org.openhie.openempi.dao.hibernate;

import java.util.List;

import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.dao.FieldDao;
import org.openhie.openempi.model.FieldMeaning;
import org.openhie.openempi.util.ValidationUtil;

public class FieldDaoHibernate extends UniversalDaoHibernate implements FieldDao
{
	public FieldType findFieldTypeByName(String fieldTypeName) {
		ValidationUtil.sanityCheckFieldName(fieldTypeName);
		@SuppressWarnings("unchecked")
		List<FieldType> list = (List<FieldType>) getHibernateTemplate().
			find("from FieldType as f where f.fieldTypeName = ?", fieldTypeName);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	public FieldMeaning findFieldMeaningByName(String fieldMeaningName) {
		ValidationUtil.sanityCheckFieldName(fieldMeaningName);
		@SuppressWarnings("unchecked")
		List<FieldMeaning> list = (List<FieldMeaning>) getHibernateTemplate().
			find("from FieldMeaning as f where f.fieldMeaningName = ?", fieldMeaningName);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	public org.openhie.openempi.model.FieldType findFieldTypeByCode(String fieldTypeCode) {
		ValidationUtil.sanityCheckFieldName(fieldTypeCode);
		@SuppressWarnings("unchecked")
		List<FieldType> list = (List<FieldType>) getHibernateTemplate().
			find("from FieldType as f where f.fieldTypeCode = ?", fieldTypeCode);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	public FieldMeaning findFieldMeaningByCode(String fieldMeaningCode) {
		ValidationUtil.sanityCheckFieldName(fieldMeaningCode);
		@SuppressWarnings("unchecked")
		List<FieldMeaning> list = (List<FieldMeaning>) getHibernateTemplate().
			find("from FieldMeaning as f where f.fieldMeaningCode = ?", fieldMeaningCode);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

}
