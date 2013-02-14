package org.openempi.webapp.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Gender extends BaseModelData
{
	public Gender() {
	}

	public Gender(String name, String code) {
		setName(name);
		setCode(code);
	}

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}

	public String getCode() {
		return get("code");
	}

	public void setCode(String code) {
		set("code", code);
	}
}
