package com.stripe.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.dto.ResponseDTO;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.service.InvoiceService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	Environment env;

	@Override
	public ResponseEntity<ResponseDTO> fetchInvoicesInfoByCustId(String custId) {
		// TODO Auto-generated method stub
		ResponseDTO responseDTO = new ResponseDTO();
		String responseMessage;

		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("customer", custId);

		Stripe.apiKey = env.getProperty("StripeAPIKey");

		System.out.println("API key" + env.getProperty("StripeAPIKey"));

		try {
			InvoiceCollection invoices = Invoice.list(params);

			if (invoices != null) {
				responseMessage = "fetching all subscription ";
				log.info(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				responseDTO.setResponseData(invoices);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			} else {
				responseMessage = "Something went wrong while fetching subscription";
				log.warn(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Failed");
				responseDTO.setResponseData(invoices);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage = "Exception while fetching all invoices details : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

	}

}
