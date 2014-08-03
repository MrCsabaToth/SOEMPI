package org.openhie.openempi.util;

import java.beans.PropertyEditorSupport;

public final class EnumPropertyEditor extends PropertyEditorSupport
{
	public EnumPropertyEditor() {
	}
	
	@Override
	public String getAsText() {
		return (String) getValue();
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(text);
	}
}
