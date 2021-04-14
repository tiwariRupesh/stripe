package com.stripe.dto;

import java.util.List;

import lombok.Data;

@Data
public class StripeCustomerDto {
	private String customerId;
	private StripeAddress address;
	private Integer balance;
	private String currency;
	private String default_source;
	private Boolean delinquent;
	private String description;
	private Object discount;
	private String email;
	private String invoice_prefix;

	private Boolean livemode;
	private String name;
	private Object metadata;
	private Integer next_invoice_sequence;
	private String phone;
	private List<String> preferred_locales;
	private StripeShipping shipping;
	private StripeSource sources;
	private String businessId;
	private Boolean isDiscountUser;

	private String couponCode;

}
