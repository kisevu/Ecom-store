package com.kitchen.sales.order.infrastructure.secondary.service.stripe;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.exceptions.CartPaymentException;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.aggregate.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: kev.Ameda
 */
@Service
public class StripeService {

  @Value("${application.stripe.api-key}")
  private String apiKey;

  @Value("${application.client-base-url}")
  private String clientBaseUrl;  // where to redirect, ui

  public StripeService() {
  }

  @PostConstruct
  public void setApiKey(){
    Stripe.apiKey = apiKey;
  }

  public StripeSessionId createPayment(User connectedUser,
                                       List<Product> productInformation,
                                       List<DetailCartItemRequest> cartItemRequests){
    SessionCreateParams.Builder session = SessionCreateParams.builder()
      .setMode(SessionCreateParams.Mode.PAYMENT)
      .putMetadata("user_public_id", connectedUser.getUserPublicId().value().toString())
      .setCustomerEmail(connectedUser.getEmail().value())
      .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
      .setSuccessUrl(this.clientBaseUrl + "/cart/success?session_id={CHECKOUT_SESSION_ID}")
      .setCancelUrl(this.clientBaseUrl + "/cart/failure");
    for (DetailCartItemRequest itemRequest: cartItemRequests){
      Product productDetails = productInformation.stream()
        .filter(product -> product.getPublicId().value().equals(itemRequest.productId().value()))
        .findFirst().orElseThrow();
      SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .putMetadata("product_id", productDetails.getPublicId().value().toString())
        .setName(productDetails.getProductName().value())
        .build();
      SessionCreateParams.LineItem.PriceData linePriceData = SessionCreateParams.LineItem.PriceData.builder()
        .setUnitAmountDecimal(BigDecimal.valueOf(Double.valueOf(productDetails.getProductPrice().value()).longValue() * 100))
        .setProductData(productData)
        .setCurrency("EUR")
        .build();

      SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
        .setQuantity(itemRequest.quantity())
        .setPriceData(linePriceData)
        .build();
      session.addLineItem(lineItem);
    }
    return createSession(session.build());
  }

  private StripeSessionId createSession(SessionCreateParams sessionCreateParams) {
    try{
      Session session = Session.create(sessionCreateParams);
      return new StripeSessionId(session.getId(),session.getUrl());
    }catch (StripeException stripeException){
      throw new CartPaymentException(String.format("Error while creating Stripe Session %s: ", stripeException.getMessage()));
    }
  }


}
