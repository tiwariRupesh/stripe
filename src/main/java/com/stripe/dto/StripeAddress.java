package com.stripe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class StripeAddress {
	private String line1;
	private String city;
	private String country;
	private String line2;
	private String postal_code;
	private String state;

}
