package com.stripe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.dto.ResponseDTO;
import com.stripe.service.InvoiceService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/invoice")
@CrossOrigin
@Log4j2
public class InvoiceController {
	
	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("/fetchInvoicesInfoByCustId")
	public ResponseEntity<ResponseDTO> fetchInvoicesInfoByCustId(@RequestParam String customerId) {
		log.info("Inside CustomerInvoiceController fetchInvoicesInfoByCustId()");
		return invoiceService.fetchInvoicesInfoByCustId(customerId);
	}


}
