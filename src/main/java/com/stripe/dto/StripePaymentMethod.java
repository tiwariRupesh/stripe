package com.stripe.dto;

import lombok.Data;

@Data
public class StripePaymentMethod {
	private StripeBillingInformation billing_details;
	private StripeCard card;
	private Boolean livemode = false;
	private String customer;
	private String name;
	private String type;
}
