package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import org.jilt.Builder;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestDetailCartResponse(List<RestProductCart> restProductCarts) {

  public static  RestDetailCartResponse from(DetailCartResponse detailCartResponse){
    return RestDetailCartResponseBuilder.restDetailCartResponse()
      .restProductCarts(detailCartResponse.getProductCarts().stream().map(RestProductCart::from).toList())
      .build();
  }
}
