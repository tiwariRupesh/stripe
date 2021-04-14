package com.stripe.dto;

import lombok.Data;

@Data
public class StripePriceDto {
	private String recurring_interval;
	private Integer unit_amount;
	private String currency;
	private String product;
	private String unit_amount_decimal;

}
