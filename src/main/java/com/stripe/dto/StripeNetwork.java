package com.stripe.dto;

import java.util.List;

import lombok.Data;

@Data
public class StripeNetwork {

	private List<String> available;
	private String preferred;

}
