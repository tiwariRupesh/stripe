package com.stripe.dto;

import lombok.Data;

@Data
public class StripeSubscriptionDto {
	private String productName;
	private String productId;
	private StripePriceDto price;
	private Integer quantity;
	private String customerId;
	private String paymentMethodId;
	private String subscriptionId;
	private Long prorationDate;
	private Boolean isMakeCardDefault;
}
