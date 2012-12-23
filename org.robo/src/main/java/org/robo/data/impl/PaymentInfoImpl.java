package org.robo.data.impl;

import org.robo.data.PaymentInfo;

public class PaymentInfoImpl implements PaymentInfo {

	private String outSum;

	private String invId;

	@Override
	public String getOutSum() {
		return outSum;
	}

	@Override
	public String getInvId() {
		return invId;
	}

	public void setOutSum(String nOutSum) {
		this.outSum = nOutSum;
	}

	public void setInvId(String nInvId) {
		this.invId = nInvId;
	}
}