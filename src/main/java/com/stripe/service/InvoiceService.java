package com.stripe.service;

import org.springframework.http.ResponseEntity;

import com.stripe.dto.ResponseDTO;

public interface InvoiceService {

	ResponseEntity<ResponseDTO> fetchInvoicesInfoByCustId(String custId);

}
