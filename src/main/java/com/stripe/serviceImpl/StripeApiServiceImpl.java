package com.stripe.serviceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.dto.ResponseDTO;
import com.stripe.dto.StripeAddress;
import com.stripe.dto.StripeCard;
import com.stripe.dto.StripeCheckBalanceDto;
import com.stripe.dto.StripeCustomerDto;
import com.stripe.dto.StripeCustomerSubscriptionDto;
import com.stripe.dto.StripePaymentMethod;
import com.stripe.dto.StripeSubscriptionDto;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceUpcomingParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionRetrieveParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.service.StripeApiService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StripeApiServiceImpl implements StripeApiService {

	@Autowired
	private Environment env;

	@Override
	public ResponseEntity<ResponseDTO> deleteByCustomerId(String custId) {

		ResponseDTO responseDTO = new ResponseDTO();
		Customer customer = null;

		String responseMessage;
		try {
			log.info("reading stripe api key from application property file");
			Stripe.apiKey = env.getProperty("StripeAPIKey");
			customer = Customer.retrieve(custId);
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			responseMessage = "Exception while fetching customer deatails";
			e.printStackTrace();
			log.info(responseMessage);
		}

		try {
			if (customer != null) {
				Customer deletedCustomer = customer.delete();

				if (deletedCustomer != null && deletedCustomer.getDeleted() == Boolean.TRUE) {

					responseMessage = "customer deleted successfully";
					responseDTO.setResponseMessage(responseMessage);
					responseDTO.setResult("Success");
					return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
				}

			} else {
				responseMessage = "customer not found for " + custId;
				responseDTO.setResult("Failed");
				responseDTO.setResponseMessage(responseMessage);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

			}

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseMessage = "Something went wrong while deleting customer !";
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.EXPECTATION_FAILED);

		}
		return null;
	}

	@Override
	public ResponseEntity<ResponseDTO> cancelCustomerSubscription(String customerId, String subscriptionId) {
		// TODO Auto-generated method stub
		log.info("Inside cancelCustomerSubscription()");
		ResponseDTO responseDTO = new ResponseDTO();

		String responseMessage;
		log.info("reading stripe api key from application property file");
		Stripe.apiKey = env.getProperty("StripeAPIKey");
		try {
			Subscription subscription = Subscription.retrieve(subscriptionId);

			if (subscription != null) {
				// StripeSubscriptionDto StripesubsDto = customerReq.getSubscriptions();

				Map<String, Object> params2 = new HashMap<>();
//				params2.put("customer", customerId);
				params2.put("cancel_at_period_end", Boolean.TRUE);
//				 params2.put("proration_behavior", "none");
				//

//				 Subscription deletedSubscription =
//						  subscription.cancel();

				Subscription updatedSubscription = subscription.update(params2);

				responseMessage = "Customer subscription  cancelled successfully";

				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				// responseDTO.setResponseData(subscription);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = "Exception while cancelling Subscription Info : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		return null;

	}

	@Override
	public ResponseEntity<ResponseDTO> reactivatingCancelSubscription(String customerId, String subscriptionId) {
		// TODO Auto-generated method stub
		log.info("Inside reactivatingCancelSubscription()");
		ResponseDTO responseDTO = new ResponseDTO();

		String responseMessage;
		log.info("reading stripe api key from application property file");
		Stripe.apiKey = env.getProperty("StripeAPIKey");
		try {
			Subscription subscription = Subscription.retrieve(subscriptionId);

			if (subscription != null) {
				// StripeSubscriptionDto StripesubsDto = customerReq.getSubscriptions();

				Map<String, Object> params2 = new HashMap<>();
//				params2.put("customer", customerId);
				params2.put("cancel_at_period_end", Boolean.FALSE);
//				 params2.put("proration_behavior", "none");
				//

//				 Subscription deletedSubscription =
//						  subscription.cancel();

				Subscription updatedSubscription = subscription.update(params2);

				responseMessage = " customer subscription reactivated successfully";
				log.info("customer subscription reactivated successfully");

				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				// responseDTO.setResponseData(subscription);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = "Exception while cancelling Subscription Info : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		return null;

	}

	@Override
	public ResponseEntity<ResponseDTO> reduceSubscription(Long quantity, String subscriptionId) {
		// TODO Auto-generated method stub
		log.info("Inside reduceSubscription()");
		ResponseDTO responseDTO = new ResponseDTO();

		String responseMessage;
		Stripe.apiKey = env.getProperty("StripeAPIKey");
		try {
			Subscription subscription = Subscription.retrieve(subscriptionId);

			if (subscription != null) {
				// StripeSubscriptionDto StripesubsDto = customerReq.getSubscriptions();

				Map<String, Object> params2 = new HashMap<>();
				// params.put("customer", customerReq.getCustomerId());
				params2.put("quantity", quantity);
				params2.put("proration_behavior", "none");

				Subscription updatedSubscription = subscription.update(params2);

				responseMessage = "license reduced successfully";

				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				responseDTO.setResponseData(subscription);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

			}
		} catch (Exception e) {
			responseMessage = "Exception while reducing Subscription Info : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		return null;

	}

	@Override
	public ResponseEntity<ResponseDTO> fetchSubscriptionsByCustId(String custId) {
		// TODO Auto-generated method stub
		ResponseDTO responseDTO = new ResponseDTO();
		String responseMessage = null;
		Map<String, Object> params = new HashMap<>();
		params.put("customer", custId);

		try {
			Stripe.apiKey = env.getProperty("StripeAPIKey");
			SubscriptionCollection subscriptions = Subscription.list(params);
			if (subscriptions != null) {
				responseMessage = "fetching all subscription ";
				log.info(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				responseDTO.setResponseData(subscriptions);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			} else {
				responseMessage = "Something went wrong while fetching subscription";
				log.warn(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Failed");
				responseDTO.setResponseData(subscriptions);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage = "Exception while fetching all subscription : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<ResponseDTO> updateSubscriptionQtyAndPayment(StripeCustomerSubscriptionDto dto) {
		// TODO Auto-generated method stub
		ResponseDTO responseDTO = new ResponseDTO();
		StripePaymentMethod payMethod = dto.getPaymentMethod();
		String responseMessage = null;

		PaymentMethod paymentMethod = new PaymentMethod();

		Stripe.apiKey = env.getProperty("StripeAPIKey");
		log.info("reading data from application file");

		try {
			log.info("pay method " + payMethod);
			paymentMethod = updatePaymentMethod(payMethod);
		} catch (CardException e) {
			// LOGGER.error("code is" + e.getCharge());
//			log.error("code is " + e.getCode());

			e.printStackTrace();

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			log.error("code is" + e.getCode());
			if (e.getCode().equalsIgnoreCase("invalid_expiry_year")) {
				responseMessage = "The card’s expiration year is incorrect. Check the expiration date";
			} else if (e.getCode().equalsIgnoreCase("invalid_expiry_month")) {
				responseMessage = "The card’s expiration month is incorrect. Check the expiration date";
			}

			else if (e.getCode().trim().equalsIgnoreCase("incorrect_number")) {
				responseMessage = "The card number is incorrect. Check the card’s number or use a different card.";

			} else if (e.getCode().trim().equalsIgnoreCase("invalid_number")) {
				responseMessage = "The card number is invalid. Check the card details";
			}

			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		try {

			if (paymentMethod != null) {
				PaymentMethod updatedPaymentMethod = addAttachmentToPaymentMethod(payMethod, paymentMethod);

			}
		} catch (CardException e) {
			// LOGGER.error("code is" + e.getCharge());
			log.error("code is " + e.getCode());

			if (e.getCode().equalsIgnoreCase("incorrect_cvc")) {
				responseMessage = "The card’s security code is incorrect. Check the card’s security code.";
			} else if (e.getCode().equalsIgnoreCase("invalid_cvc")) {
				responseMessage = "The card’s security code is invalid. Check the card’s security code";

			}

			else if (e.getCode().equalsIgnoreCase("expired_card")) {
				responseMessage = "The card has expired. Check the expiration date ";
			}

			else if (e.getDeclineCode().equalsIgnoreCase("insufficient_funds")) {
				responseMessage = "The associated account does not have a sufficient balance available";
			} else if (e.getCode().equalsIgnoreCase("card_declined")) {
				responseMessage = "Your card was declined";
			} else if (e.getCode().equalsIgnoreCase("stolen_card")) {
				responseMessage = "Your card was declined";
			} else if (e.getCode().equalsIgnoreCase("lost_card")) {
				responseMessage = "Your card was declined";
			}

			log.info(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			log.error("code is" + e.getCode());
			if (e.getCode().equalsIgnoreCase("invalid_expiry_year")) {
				responseMessage = "The card’s expiration year is incorrect. Check the expiration date";
			} else if (e.getCode().equalsIgnoreCase("invalid_expiry_month")) {
				responseMessage = "The card’s expiration month is incorrect. Check the expiration date";
			}

			else if (e.getCode().trim().equalsIgnoreCase("incorrect_number")) {
				responseMessage = "The card number is incorrect. Check the card’s number or use a different card.";

			} else if (e.getCode().trim().equalsIgnoreCase("invalid_number")) {
				responseMessage = "The card number is invalid. Check the card details";
			}

			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		StripeSubscriptionDto subDTO = dto.getSubscriptions();

		try {

			Subscription subscription = Subscription.retrieve(subDTO.getSubscriptionId());
			log.info("subscription object " + subscription);
			Long quantity = new Long(subDTO.getQuantity());

			SubscriptionUpdateParams.Builder updateParamsBuilder = new SubscriptionUpdateParams.Builder();

			updateParamsBuilder.addItem(SubscriptionUpdateParams.Item.builder()
					.setId(subscription.getItems().getData().get(0).getId()).setQuantity(quantity).build());
			updateParamsBuilder.setProrationDate(subDTO.getProrationDate());

			subscription = subscription.update(updateParamsBuilder.addAllExpand(Arrays.asList("plan.product")).build());
			if (paymentMethod != null & subDTO.getIsMakeCardDefault() == Boolean.TRUE) {
				// updateParamsBuilder.setDefaultPaymentMethod(paymentMethod.getId());

				log.info("isMakeCardDefault " + subDTO.getIsMakeCardDefault());

				Map<String, Object> param = new HashMap<>();
				param.put("default_payment_method", paymentMethod.getId());

				Map<String, Object> param1 = new HashMap<>();

				param1.put("invoice_settings", param);

				Customer customer = Customer.retrieve(subscription.getCustomer());

				Customer updatedCustomer = customer.update(param1);
				log.info("default card applied  ");

			}

			String planName = subscription.getPlan().getProductObject().getName();
			String unitLabel = subscription.getPlan().getProductObject().getUnitLabel();

			log.info(subscription.getCustomer());
			log.info(subscription.getId());

			Invoice invoice = Invoice.create(InvoiceCreateParams.builder().setCustomer(subscription.getCustomer())
					.setSubscription(subscription.getId())
					// .setDescription(
					// "Change to " + quantity.toString() + " " + unitLabel + " on the the " +
					// planName + " plan")
					.build());

			invoice = invoice.pay();
			responseMessage = "New license added successfully";
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Success");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
		} catch (StripeException e) {
			e.printStackTrace();
			responseMessage = "Exception  while  adding  new license and making payment!";
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.EXPECTATION_FAILED);

		}

	}

	private PaymentMethod updatePaymentMethod(StripePaymentMethod payMethod) throws StripeException {
		StripeCard stripeCard = payMethod.getCard();
		Map<String, Object> card = new HashMap<>();
		// card.put("name", stripeCard.getName());
		card.put("number", stripeCard.getNumber());
		card.put("exp_month", stripeCard.getExp_month());
		card.put("exp_year", stripeCard.getExp_year());
		card.put("cvc", stripeCard.getCvc());
		Map<String, Object> billingDetails = new HashMap<>();
//		billingDetails.put("name", payMethod.getName());
		Map<String, Object> params = new HashMap<>();
//		params.put("billing_details", billingDetails);
		params.put("type", "card");
		params.put("card", card);
		return PaymentMethod.create(params);
	}

	private PaymentMethod addAttachmentToPaymentMethod(StripePaymentMethod payMethod, PaymentMethod paymentMethod)
			throws StripeException {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("customer", payMethod.getCustomer());
		return paymentMethod.attach(params1);
	}

	@Override
	public ResponseEntity<ResponseDTO> createCustomerSubscription(StripeCustomerSubscriptionDto dto) {
		// TODO Auto-generated method stub
		StripeSubscriptionDto subReq = dto.getSubscriptions();
		String responseMessage = null;
		ResponseDTO responseDTO = new ResponseDTO();
		StripePaymentMethod payReq = dto.getPaymentMethod();
		PaymentMethod updatedPaymentMethod = new PaymentMethod();
		Stripe.apiKey = env.getProperty("StripeAPIKey").trim();
		Customer customer = null;
		Subscription subscription = null;
		StripeCustomerDto customerReq = dto.getCustomer();

		try {

			customer = createStripeCustomer(customerReq);

		} catch (Exception e) {
			responseMessage = "Exception while creating stripe customer **********";
			log.info(responseMessage);
			e.printStackTrace();
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		PaymentMethod paymentMethod = null;
		// create paymentMethod
		try {
			paymentMethod = createPaymentMethod(payReq);
		} catch (CardException e) {
			e.printStackTrace();
			log.error("code is" + e.getCharge());

		} catch (StripeException e) {

			e.printStackTrace();
			log.error("code is " + e.getCode());
			if (e.getCode().equalsIgnoreCase("invalid_expiry_year")) {
				responseMessage = "The card’s expiration year is incorrect. Check the expiration date";
			} else if (e.getCode().equalsIgnoreCase("invalid_expiry_month")) {
				responseMessage = "The card’s expiration month is incorrect. Check the expiration date";
			}

			else if (e.getCode().trim().equalsIgnoreCase("incorrect_number")) {
				responseMessage = "The card number is incorrect. Check the card’s number or use a different card.";

			} else if (e.getCode().trim().equalsIgnoreCase("invalid_number")) {
				responseMessage = "The card number is invalid. Check the card details";
			}

			else {
				responseMessage = "Card details missing";
			}

			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		// add attachment to payment method
		try {

			addAttachmentToPaymentMethod(paymentMethod, customer.getId());

			
		} catch (CardException e) {
			log.error("code is" + e.getCharge());

			e.printStackTrace();

			if (e.getCode().equalsIgnoreCase("incorrect_cvc")) {
				responseMessage = "The card’s security code is incorrect. Check the card’s security code.";
			} else if (e.getCode().equalsIgnoreCase("invalid_cvc")) {
				responseMessage = "The card’s security code is invalid. Check the card’s security code";

			}

			else if (e.getCode().equalsIgnoreCase("expired_card")) {
				responseMessage = "The card has expired. Check the expiration date ";
			}

			else if (e.getDeclineCode().equalsIgnoreCase("insufficient_funds")) {
				responseMessage = "The associated account does not have a sufficient balance available";
			}

			log.info(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);

		} catch (StripeException e) {

			e.printStackTrace();
			log.error("code is " + e.getCode());
			if (e.getCode().equalsIgnoreCase("invalid_expiry_year")) {
				responseMessage = "The card’s expiration year is incorrect. Check the expiration date";
			} else if (e.getCode().equalsIgnoreCase("invalid_expiry_month")) {
				responseMessage = "The card’s expiration month is incorrect. Check the expiration date";
			}

			else if (e.getCode().trim().equalsIgnoreCase("incorrect_number")) {
				responseMessage = "The card number is incorrect. Check the card’s number or use a different card.";

			} else if (e.getCode().trim().equalsIgnoreCase("invalid_number")) {
				responseMessage = "The card number is invalid. Check the card details";
			}

			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		// set default payment method on customer
		try {

			setDefaultPaymentMethodOnCustomer(customer.getId(), paymentMethod.getId());

			
		} catch (Exception e) {
			responseMessage = "Exception while setting Default PaymentMethod On Customer **********";
			log.info(responseMessage);
			e.printStackTrace();
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		subReq.setCustomerId(customer.getId());
		subReq.setPaymentMethodId(updatedPaymentMethod.getId());
		Product product = new Product();
		// create product
		try {
			product = createStripeProduct(subReq);
		} catch (Exception e) {
			responseMessage = "Exception while creating stripe product ********** ";
			log.info(responseMessage);
			e.printStackTrace();
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		Price price = null;
		String priceId;

		// create price
		try {
			price = createStripePrice(subReq, product);
		} catch (Exception e) {
			responseMessage = "Exception while creating stripe price object ********** ";
			log.info(responseMessage);
			e.printStackTrace();
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		try {
			log.info("Recurring interval " + subReq.getPrice().getRecurring_interval());

		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = "Exception while creating stripe price object ********** ";
			log.info(responseMessage);

			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		try {
			// if (registerUser1.getCustomerId() == null) {
			subscription = createSubscription(customer.getId(), price.getId(), subReq.getQuantity());

			// } else {
			// subscription = createSubscription(registerUser1.getCustomerId(), priceId,
			// subReq.getQuantity());
			//
			// }
		} catch (Exception e) {
			responseMessage = "Exception while creating stripe subscription object ********** ";
			log.info(responseMessage);
			e.printStackTrace();
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

		// save billing address

		responseMessage = "customer created successfully";
		log.info(responseMessage);
		responseDTO.setResponseMessage(responseMessage);
		responseDTO.setResponseData(customer);
		responseDTO.setResult("Success");
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

	}

	private Price createStripePrice(StripeSubscriptionDto subReq, Product product) {

		Map<String, Object> recurring = new HashMap<>();
		recurring.put("interval", subReq.getPrice().getRecurring_interval());
		Map<String, Object> params = new HashMap<>();
		params.put("unit_amount", subReq.getPrice().getUnit_amount());
		params.put("currency", subReq.getPrice().getCurrency());
		params.put("recurring", recurring);
		params.put("product", product.getId());

		try {
			Price price = Price.create(params);
			return price;
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Customer createStripeCustomer(StripeCustomerDto customerReq) throws StripeException {
		log.info(" inside createStripeCustomer()");
		Customer customer = new Customer();
		StripeAddress billingAddress = customerReq.getAddress();
		Map<String, Object> address = new HashMap<>();
		address.put("city", billingAddress.getCity());
		address.put("country", billingAddress.getCountry());
		address.put("line1", billingAddress.getLine1());
		address.put("line2", billingAddress.getLine2());
		address.put("postal_code", billingAddress.getPostal_code());
		address.put("state", billingAddress.getState());

		// businessBillingAddress

		// create customer
		Map<String, Object> params = new HashMap<>();
		params.put("address", address);
		if (customerReq.getBalance() != null) {
			params.put("balance", customerReq.getBalance());
		} else {
			params.put("balance", "0");
		}
		params.put("description", customerReq.getDescription());
		params.put("name", customerReq.getName());
		params.put("phone", customerReq.getPhone());
		params.put("email", customerReq.getEmail());
		params.put("metadata", customerReq.getMetadata());
		log.info("customer Object send to stripe ********** " + params);
		if (customerReq.getCouponCode() != null && customerReq.getIsDiscountUser() == Boolean.TRUE) {

			params.put("coupon", "easy2020");
			log.info("coupon code applied");

		}
		customer = Customer.create(params);

		return customer;
	}

	private PaymentMethod createPaymentMethod(StripePaymentMethod req) throws StripeException {
		log.info("Creating Stripe Payment Method");
		StripePaymentMethod payMethod = req;

		StripeCard stripeCard = payMethod.getCard();
		Map<String, Object> card = new HashMap<>();
		card.put("number", stripeCard.getNumber());

		card.put("exp_month", stripeCard.getExp_month());
		card.put("exp_year", stripeCard.getExp_year());
		card.put("cvc", stripeCard.getCvc());

		Map<String, Object> billingDetails = new HashMap<>();
		billingDetails.put("name", stripeCard.getName());

		Map<String, Object> params = new HashMap<>();
		params.put("type", "card");
		params.put("card", card);
//		params.put("billing_details", billingDetails);

		log.info("paymentMethod Object send to stripe ********** " + params);
		PaymentMethod paymentMethod = PaymentMethod.create(params);

		return paymentMethod;
	}

	private Customer setDefaultPaymentMethodOnCustomer(String customerId, String paymentMethodId)
			throws StripeException {
		Customer customer = Customer.retrieve(customerId);
		// Set the default payment method on the customer

		CustomerUpdateParams customerUpdateParams = CustomerUpdateParams.builder()
				.setInvoiceSettings(
						CustomerUpdateParams.InvoiceSettings.builder().setDefaultPaymentMethod(paymentMethodId).build())
				.build();

		customer.update(customerUpdateParams);
		return customer;
	}

	private Product createStripeProduct(StripeSubscriptionDto subReq) throws StripeException {

		Product product = new Product();
		log.info("Creating Stripe Product");
		Map<String, Object> productParams = new HashMap<>();
		productParams.put("name", subReq.getProductName());

		log.info("Product Object send to stripe ********** " + productParams);
		product = Product.create(productParams);

		return product;
	}

	private Subscription createSubscription(String customerId, String newPriceId, Integer quantity)
			throws StripeException {
		// convert Integer quantity to Long
		Long quan = new Long(quantity);
		SubscriptionCreateParams subCreateParams = SubscriptionCreateParams.builder()
				.addItem(SubscriptionCreateParams.Item.builder().setPrice(newPriceId).setQuantity(quan).build())
				.setCustomer(customerId).addAllExpand(Arrays.asList("latest_invoice.payment_intent", "plan.product"))
				.build();

		Subscription subscription = Subscription.create(subCreateParams);
		return subscription;
	}

	@Override
	public ResponseEntity<ResponseDTO> retriveSubscriptionInfo(StripeCheckBalanceDto dto) {
		// TODO Auto-generated method stub
		log.info("Inside retriveSubscriptionInfo()");
		ResponseDTO responseDTO = new ResponseDTO();
		String responseMessage = null;
		try {
			Stripe.apiKey = env.getProperty("StripeAPIKey");
			SubscriptionRetrieveParams subParams = SubscriptionRetrieveParams.builder().addAllExpand(
					Arrays.asList("latest_invoice", "customer.invoice_settings.default_payment_method", "plan.product"))
					.build();

			Subscription subscription = Subscription.retrieve(dto.getSubscriptionId(), subParams, null);

			InvoiceUpcomingParams invoiceParams = InvoiceUpcomingParams.builder().setSubscription(subscription.getId())
					.build();

			Invoice upcomingInvoice = Invoice.upcoming(invoiceParams);

			Map<String, Object> responseData = new HashMap<>();
			responseData.put("card",
					subscription.getCustomerObject().getInvoiceSettings().getDefaultPaymentMethodObject().getCard());
			responseData.put("product_description", subscription.getPlan().getProductObject().getName());
			responseData.put("current_price", subscription.getPlan().getId());
			responseData.put("current_quantity", subscription.getItems().getData().get(0).getQuantity());
			responseData.put("latest_invoice", subscription.getLatestInvoiceObject());
			responseData.put("upcoming_invoice", upcomingInvoice);

			responseMessage = "retrive Subscription Info";
			log.info(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResponseData(responseData);
			responseDTO.setResult("Success");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			responseMessage = "Exception while retriving Subscription Info : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<ResponseDTO> updateCustomer(StripeCustomerDto customerReq) {
		// TODO Auto-generated method stub
		ResponseDTO responseDTO = new ResponseDTO();
		String responseMessage;
		Customer customer = null;
		Stripe.apiKey = env.getProperty("StripeAPIKey");

		try {
			customer = Customer.retrieve(customerReq.getCustomerId());
			if (customer != null) {

				StripeAddress billingAddress = customerReq.getAddress();

				Map<String, Object> address = new HashMap<>();
				address.put("city", billingAddress.getCity());
				address.put("country", billingAddress.getCountry());
				address.put("line1", billingAddress.getLine1());
				address.put("line2", billingAddress.getLine2());
				address.put("postal_code", billingAddress.getPostal_code());
				address.put("state", billingAddress.getState());

				Map<String, Object> params = new HashMap<>();
				params.put("address", address);
				// params.put("balance",customerReq.getBalance());
				params.put("description", customerReq.getDescription());
				params.put("name", customerReq.getName());
				params.put("phone", customerReq.getPhone());
				params.put("email", customerReq.getEmail());
				params.put("metadata", customerReq.getMetadata());

				Customer customerupdate = customer.update(params);
				if (customerupdate != null) {
					responseMessage = "Customer updated successfully for " + customerReq.getCustomerId();
					responseDTO.setResponseMessage(responseMessage);
					responseDTO.setResult("Success");
					return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

				} else {
					responseMessage = "Customer not found for productId : " + customerReq.getCustomerId();
					log.info(responseMessage);
					responseDTO.setResponseMessage(responseMessage);
					responseDTO.setResult("Failed");

					return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

				}
			}

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseMessage = "Exception while updating customer billing details !";
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("failure");
			log.info(responseMessage);
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
		}
		return null;

	}

	@Override
	public ResponseEntity<ResponseDTO> fetchPaymentsMethodInfoByCustId(String custId) {
		// TODO Auto-generated method stub
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> params = new HashMap<>();
		params.put("customer", custId);
		params.put("type", "card");

		String responseMessage;
		try {
			Stripe.apiKey = env.getProperty("StripeAPIKey");
			PaymentMethodCollection paymentMethods = PaymentMethod.list(params);

			if (paymentMethods != null) {
				responseMessage = "fetching all payment ";
				log.info(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Success");
				responseDTO.setResponseData(paymentMethods);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			} else {
				responseMessage = "Something went wrong while fetching payment";
				log.warn(responseMessage);
				responseDTO.setResponseMessage(responseMessage);
				responseDTO.setResult("Failed");
				responseDTO.setResponseData(paymentMethods);
				return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage = "Exception while fetching all payment : " + e.getMessage();
			log.warn(responseMessage);
			responseDTO.setResponseMessage(responseMessage);
			responseDTO.setResult("Failed");
			return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
		}

	}

	private PaymentMethod addAttachmentToPaymentMethod(PaymentMethod paymentMethod, String customerId)
			throws StripeException {
		PaymentMethod updatedPaymentMethod = new PaymentMethod();
		log.info("add Attachment To PaymentMethod");

		// PaymentMethod pm = PaymentMethod.retrieve(paymentMethod.getId());
		// pm.attach(PaymentMethodAttachParams.builder().setCustomer(customer.getId()).build());
		Map<String, Object> params1 = new HashMap<>();
		params1.put("customer", customerId);
		System.out.println("payment" + paymentMethod);
		log.info("updated paymentMethod Object with customer id send to stripe ********** " + params1);
		updatedPaymentMethod = paymentMethod.attach(params1);
		// LOGGER.info("Stripe Updated PaymentMethod Object ********** "+
		// updatedPaymentMethod);

		return updatedPaymentMethod;
	}
}