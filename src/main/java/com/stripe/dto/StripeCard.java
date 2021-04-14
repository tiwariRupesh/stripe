package com.stripe.dto;

import lombok.Data;

@Data
public class StripeCard {
	private String name;
	private String brand;
	private StripeChecks checks;
	private String country;
	private Integer exp_month;
	private Integer exp_year;
	private String fingerprint;
	private String funding;
	private String generated_from;
	private String last4;
	private StripeNetwork stripNetwork;
	private StripeThreeDSecureUsage three_d_secure_usage;
	private String wallet;
	private String number;
	private String cvc;

}
