package org.robo.data.impl;

import org.robo.data.ResultInfo;

public class ResultInfoImpl extends PaymentInfoImpl implements ResultInfo {

	private String signatureValue;

	@Override
	public String getSignatureValue() {
		return signatureValue;
	}

	public void setSignatureValue(String sSignatureValue) {
		this.signatureValue = sSignatureValue;
	}

}