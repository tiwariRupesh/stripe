package com.stripe.dto;

import lombok.Data;

@Data
public class ResponseDTO {

	public String responseMessage;
	public String result;
	public Object responseData;

}
