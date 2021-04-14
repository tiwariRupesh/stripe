package com.stripe.dto;

import lombok.Data;

@Data
public class StripeShipping {
	private StripeAddress address;
	private String name;
	private String phone;

}
