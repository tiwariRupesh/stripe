package com.stripe.dto;

import lombok.Data;

@Data
public class StripeChecks {

	private String address_line1_check;
	private String address_postal_code_check;
	private String cvc_check;

}
