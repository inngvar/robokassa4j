package org.robo.data.impl;

import org.robo.data.SuccessInfo;

public class SuccessInfoImpl extends ResultInfoImpl implements SuccessInfo {
	
	CultureInfoImpl cultureInfo = new CultureInfoImpl();

	@Override
	public String getCulture() {
		return cultureInfo.getCulture();
	}

	public void setCulture(String sCulture) {
		cultureInfo.setCulture(sCulture);
	}
}