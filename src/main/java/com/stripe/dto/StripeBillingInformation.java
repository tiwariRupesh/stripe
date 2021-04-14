package com.stripe.dto;

import lombok.Data;

@Data
public class StripeBillingInformation {

	private StripeAddress address;
	private String name;
	private String phone;
	private String email;

}
