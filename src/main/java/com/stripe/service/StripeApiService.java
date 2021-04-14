package com.stripe.service;

import org.springframework.http.ResponseEntity;

import com.stripe.dto.ResponseDTO;
import com.stripe.dto.StripeCheckBalanceDto;
import com.stripe.dto.StripeCustomerDto;
import com.stripe.dto.StripeCustomerSubscriptionDto;

public interface StripeApiService {

	ResponseEntity<ResponseDTO> deleteByCustomerId(String custId);

	ResponseEntity<ResponseDTO> cancelCustomerSubscription(String customerId, String subscriptionId);

	ResponseEntity<ResponseDTO> reactivatingCancelSubscription(String customerId, String subscriptionId);

	ResponseEntity<ResponseDTO> reduceSubscription(Long quantity, String subscriptionId);

	ResponseEntity<ResponseDTO> fetchSubscriptionsByCustId(String custId);

	ResponseEntity<ResponseDTO> updateSubscriptionQtyAndPayment(StripeCustomerSubscriptionDto dto);

	ResponseEntity<ResponseDTO> createCustomerSubscription(StripeCustomerSubscriptionDto dto);

	ResponseEntity<ResponseDTO> retriveSubscriptionInfo(StripeCheckBalanceDto dto);

	ResponseEntity<ResponseDTO> updateCustomer(StripeCustomerDto customerReq);

	ResponseEntity<ResponseDTO> fetchPaymentsMethodInfoByCustId(String custId);

}
