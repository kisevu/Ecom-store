package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequestBuilder;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import com.kitchen.sales.order.application.OrdersApplicationService;
import com.kitchen.sales.order.domain.order.exceptions.CartPaymentException;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/orders")
public class OrderResource {

  private final OrdersApplicationService ordersApplicationService;

  private final Logger log = LoggerFactory.getLogger(OrderResource.class);

  public OrderResource(OrdersApplicationService ordersApplicationService) {
    this.ordersApplicationService = ordersApplicationService;
  }

  @GetMapping("/get-cart-details")
  public ResponseEntity<RestDetailCartResponse> getDetails(@RequestParam List<UUID> productIds ){
    List<DetailCartItemRequest> cartItemRequests = productIds.stream()
      .map(uuid -> new DetailCartItemRequest(new PublicId(uuid), 1))
      .toList();
    DetailCartRequest detailCartRequest = DetailCartRequestBuilder.detailCartRequest()
      .items(cartItemRequests)
      .build();
    DetailCartResponse cartDetails = ordersApplicationService.getCartDetails(detailCartRequest);
    RestDetailCartResponse restDetailCartResponse = RestDetailCartResponse.from(cartDetails);
    log.info(" result : {}", restDetailCartResponse.restProductCarts().stream().map(RestProductCart::picture).toList());
    return ResponseEntity.ok(RestDetailCartResponse.from(cartDetails));
  }

  @PostMapping("/init-payment")
  public ResponseEntity<RestStripeSession> initPayment(@RequestBody List<RestCartItemRequest> items){
    List<DetailCartItemRequest> detailCartItemRequests = RestCartItemRequest.to(items);
    try{
      StripeSessionId stripeSessionInformation = ordersApplicationService.createOrder(detailCartItemRequests);
      RestStripeSession restStripeSession = RestStripeSession.from(stripeSessionInformation);
      return ResponseEntity.ok(restStripeSession);
    }catch (CartPaymentException ex){
      log.error("Error occurred with stripe thing: {}",ex.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }


}
