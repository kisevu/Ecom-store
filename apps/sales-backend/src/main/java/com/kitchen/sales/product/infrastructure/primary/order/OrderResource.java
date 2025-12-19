package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.*;
import com.kitchen.sales.order.application.OrdersApplicationService;
import com.kitchen.sales.order.domain.order.exceptions.CartPaymentException;
import com.kitchen.sales.order.domain.user.vo.*;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.domain.vo.PublicId;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Address;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/orders")
public class OrderResource {

  private final OrdersApplicationService ordersApplicationService;

  @Value("${application.stripe.webhook-secret}")
  private String webhookSecret;

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

  @PostMapping("/webhook")
  public ResponseEntity<Void> webhookStripe(@RequestBody String paymentEvent,
                                            @RequestHeader("Stripe-Signature") String stripeSignature){
    Event event = null;
    try{
      event = Webhook.constructEvent( paymentEvent, stripeSignature, webhookSecret);
    }catch (SignatureVerificationException ex){
      return ResponseEntity.badRequest().build();
    }
    Optional<StripeObject> rawPaymentIntent = event.getDataObjectDeserializer().getObject();
    switch (event.getType()){
      case "checkout.session.completed":
        handleCheckoutSessionCompleted(rawPaymentIntent.orElseThrow());
        break;
    }
    return ResponseEntity.ok().build();
  }

  private void handleCheckoutSessionCompleted(StripeObject rawStripeObject) {
    if (rawStripeObject instanceof Session session){
      Address address = session.getCustomerDetails().getAddress();
      UserAddress userAddress = UserAddressBuilder.userAddress()
        .city(address.getCity())
        .country(address.getCountry())
        .zipcode(address.getPostalCode())
        .street(address.getLine1())
        .build();

      UserAddressToUpdate userAddressToUpdate = UserAddressToUpdateBuilder.userAddressToUpdate()
        .userAddress(userAddress)
        .userPublicId(new UserPublicId(UUID.fromString(session.getMetadata().get("user_public_id"))))
        .build();

      StripeSessionInformation stripeSessionInformation = StripeSessionInformationBuilder.stripeSessionInformation()
        .userAddressToUpdate(userAddressToUpdate)
        .stripeSessionId(new StripeSessionId(session.getId()))
        .build();
      ordersApplicationService.updateOrder(stripeSessionInformation);
    }
  }

  @GetMapping("/user")
  public ResponseEntity<Page<RestOrderRead>> getOrdersForConnectedUser(Pageable pageable){
    Page<Order> ordersForConnectedUser = ordersApplicationService.findOrdersForConnectedUser(pageable);
    PageImpl<RestOrderRead> restOrderReads = new PageImpl<>(
      ordersForConnectedUser.getContent().stream().map(RestOrderRead::from).toList(),
      pageable,
      ordersForConnectedUser.getTotalElements()
    );
    return ResponseEntity.ok(restOrderReads);
  }


  @GetMapping("/admin")
  public ResponseEntity<Page<RestOrderReadAdmin>> getOrdersForAdmin(Pageable pageable){
    Page<Order> orders = ordersApplicationService.findOrdersForAdmin(pageable);
    PageImpl<RestOrderReadAdmin> restOrderReadAdmins = new PageImpl<>(
      orders.getContent().stream().map(RestOrderReadAdmin::from).toList(),
      pageable,
      orders.getTotalElements()
    );
    return ResponseEntity.ok(restOrderReadAdmins);
  }


}
