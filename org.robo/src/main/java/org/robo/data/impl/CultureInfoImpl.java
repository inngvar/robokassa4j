package org.robo.data.impl;

import org.robo.data.CultureInfo;

public class CultureInfoImpl implements CultureInfo {

	private String сulture;

	@Override
	public String getCulture() {
		return сulture;
	}

	public void setCulture(String sCulture) {
		this.сulture = sCulture;
	}
}