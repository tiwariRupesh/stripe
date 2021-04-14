package com.stripe.dto;

import lombok.Data;

@Data
public class StripeCustomerSubscriptionDto {
	private StripeCustomerDto customer;

	private StripePaymentMethod paymentMethod;
	private StripeSubscriptionDto subscriptions;

}
