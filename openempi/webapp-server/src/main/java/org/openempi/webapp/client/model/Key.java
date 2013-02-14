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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Key extends BaseModelData
{
	private static final long serialVersionUID = 3329606159887894366L;

	public static final String KEY_ID = "id";
	public static final String PUBLIC_KEY_PART1_LENGTH = "publicKeyPart1Length";
	public static final String PUBLIC_KEY_PART2_LENGTH = "publicKeyPart2Length";
	public static final String PUBLIC_KEY_PART3_LENGTH = "publicKeyPart3Length";
	public static final String PRIVATE_KEY_PART1_LENGTH = "privateKeyPart1Length";
	public static final String PRIVATE_KEY_PART2_LENGTH = "privateKeyPart2Length";
	public static final String PRIVATE_KEY_PART3_LENGTH = "privateKeyPart3Length";

	public Key() {
	}

	public Long getId() {
		return get(KEY_ID);
	}

	public void setId(Long id) {
		set(KEY_ID, id);
	}

	public Integer getPublicKeyPart1Length() {
		return get(PUBLIC_KEY_PART1_LENGTH);
	}

	public void setPublicKeyPart1Length(Integer publicKeyPart1Length) {
		set(PUBLIC_KEY_PART1_LENGTH, publicKeyPart1Length);
	}

	public Integer getPublicKeyPart2Length() {
		return get(PUBLIC_KEY_PART2_LENGTH);
	}

	public void setPublicKeyPart2Length(Integer publicKeyPart2Length) {
		set(PUBLIC_KEY_PART2_LENGTH, publicKeyPart2Length);
	}

	public Integer getPublicKeyPart3Length() {
		return get(PUBLIC_KEY_PART3_LENGTH);
	}

	public void setPublicKeyPart3Length(Integer publicKeyPart3Length) {
		set(PUBLIC_KEY_PART3_LENGTH, publicKeyPart3Length);
	}

	public Integer getPrivateKeyPart1Length() {
		return get(PRIVATE_KEY_PART1_LENGTH);
	}

	public void setPrivateKeyPart1Length(Integer privateKeyPart1Length) {
		set(PRIVATE_KEY_PART1_LENGTH, privateKeyPart1Length);
	}

	public Integer getPrivateKeyPart2Length() {
		return get(PRIVATE_KEY_PART2_LENGTH);
	}

	public void setPrivateKeyPart2Length(Integer privateKeyPart2Length) {
		set(PRIVATE_KEY_PART2_LENGTH, privateKeyPart2Length);
	}

	public Integer getPrivateKeyPart3Length() {
		return get(PRIVATE_KEY_PART3_LENGTH);
	}

	public void setPrivateKeyPart3Length(Integer privateKeyPart3Length) {
		set(PRIVATE_KEY_PART3_LENGTH, privateKeyPart3Length);
	}

}
