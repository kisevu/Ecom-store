package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequestBuilder;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import com.kitchen.sales.order.application.OrdersApplicationService;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/orders")
public class OrderResource {

  private final OrdersApplicationService ordersApplicationService;

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
    return ResponseEntity.ok(RestDetailCartResponse.from(cartDetails));
  }

}
