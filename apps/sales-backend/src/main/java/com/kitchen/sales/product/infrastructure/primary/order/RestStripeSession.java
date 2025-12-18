package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.vo.StripeSessionId;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestStripeSession(String id) {

  public static RestStripeSession from(StripeSessionId stripeSessionId){
    return RestStripeSessionBuilder.restStripeSession()
      .id(stripeSessionId.value())
      .build();
  }
}
