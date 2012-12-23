package org.robo.data.impl;

import org.robo.data.FailedInfo;

public class FailedInfoImpl extends PaymentInfoImpl implements FailedInfo {

	CultureInfoImpl culture = new CultureInfoImpl();

	@Override
	public String getCulture() {
		return culture.getCulture();
	}

	public void setCulture(String sCulture) {
		culture.setCulture(sCulture);
	}
}
