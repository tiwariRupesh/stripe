package com.stripe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.dto.ResponseDTO;
import com.stripe.dto.StripeCheckBalanceDto;
import com.stripe.dto.StripeCustomerDto;
import com.stripe.dto.StripeCustomerSubscriptionDto;
import com.stripe.service.StripeApiService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/stripePayment")
@CrossOrigin
@Log4j2
public class StripeApiController {

	@Autowired
	StripeApiService service;

	@PostMapping("/createCustomerSubscription")
	public ResponseEntity<ResponseDTO> createCustomerSubscription(@RequestBody StripeCustomerSubscriptionDto dto) {
		log.info("Inside stripePayment createCustomer()");
		return service.createCustomerSubscription(dto);

	}

	@PostMapping("/updateSubscriptionQtyAndPayment")
	public ResponseEntity<ResponseDTO> updateSubscriptionQtyAndPayment(@RequestBody StripeCustomerSubscriptionDto dto) {
		log.info("Inside stripePayment updateSubscriptionQtyAndPayment()");

		return service.updateSubscriptionQtyAndPayment(dto);
	}

	@PostMapping("/retriveSubscriptionInfo")
	public ResponseEntity<ResponseDTO> retriveSubscriptionInfo(@RequestBody StripeCheckBalanceDto dto) {
		log.info("Inside stripePayment retriveSubscriptionInfo()");

		return service.retriveSubscriptionInfo(dto);
	}

	@PostMapping("/updateCustomer")
	public ResponseEntity<ResponseDTO> updateCustomer(@RequestBody StripeCustomerDto customerReq) {
		log.info("Inside stripePayment updateCustomer()");
		return service.updateCustomer(customerReq);

	}

	@DeleteMapping("/deleteStripeCustomerById")
	public ResponseEntity<ResponseDTO> deleteByCustomerId(@RequestParam String customerId) {
		log.info("Inside stripePayment deleteByCustomerId()");

		return service.deleteByCustomerId(customerId);

	}

	@GetMapping("/cancelCustomerSubscription")
	public ResponseEntity<ResponseDTO> cancelCustomerSubscription(@RequestParam String customerId,
			@RequestParam String subscriptionId) {

		log.info("Inside stripePayment cancelSubscription()");

		return service.cancelCustomerSubscription(customerId, subscriptionId);

	}

	@GetMapping("/reactivatingCancelSubscription")
	public ResponseEntity<ResponseDTO> reactivatingCancelSubscription(@RequestParam String customerId,
			@RequestParam String subscriptionId) {

		log.info("Inside stripePayment reactivatingCancelSubscription()");

		return service.reactivatingCancelSubscription(customerId, subscriptionId);

	}

	@PostMapping("/reduceSubscription")
	public ResponseEntity<ResponseDTO> reduceSubscription(@RequestParam Long quantity,
			@RequestParam String subscriptionId) {

		log.info("Inside stripePayment reduceSubscription()");

		return service.reduceSubscription(quantity, subscriptionId);

	}

	@GetMapping("/fetchSubscriptionsByCustId")
	public ResponseEntity<ResponseDTO> fetchSubscriptionsByCustId(@RequestParam String customerId) {
		log.info("Inside stripePayment reduceSubscription()");

		return service.fetchSubscriptionsByCustId(customerId);
	}

	@GetMapping("/fetchPaymentsMethodInfoByCustId")
	public ResponseEntity<ResponseDTO> fetchPaymentsMethodInfoByCustId(@RequestParam String customerId) {
		log.info("Inside stripePayment fetchPaymentsMethodInfoByCustId()");
		return service.fetchPaymentsMethodInfoByCustId(customerId);
	}

}
