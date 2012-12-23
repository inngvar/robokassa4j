package org.robo.factory;

import org.robo.data.RequestData;

/**
 * Factory to create request for Robocassa server
 * @author Orphey
 *
 */
public abstract class RequestFactory {
	
	/**
	 * Create request for payment.
	 * @param invoceId Id of invoice
	 * @param mrchLogin Login of merchant in robocassa
	 * @param sum Sum of the payment
	 * @param culture Culture of the payment
	 * @param currency Currency of the payment 
	 * @param description Description of the payment 
	 * @return Data that will be sended to the robokassa service
	 */
	public abstract RequestData create(int invoceId, String mrchLogin, String sum2, String culture, String currency, String description);

}
