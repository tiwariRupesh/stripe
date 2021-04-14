package com.stripe.dto;

import lombok.Data;

@Data
public class StripeCheckBalanceDto {
	private String customerId;
	private String subscriptionId;
	private String newPriceId;
	private Long quantity;
	private String emailId;
	private StripeCard card;
	private String paymentMethodId;
}
