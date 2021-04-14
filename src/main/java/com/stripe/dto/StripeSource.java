package com.stripe.dto;

import java.util.List;

import lombok.Data;

@Data
public class StripeSource {
	private String object;
	private List<Object> data;
	private String url;
	private Boolean has_more;

}
