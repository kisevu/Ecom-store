package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.vo.StripeSessionId;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestStripeSession(String id, String url) {

  public static RestStripeSession from(StripeSessionId stripeSessionId){
    return RestStripeSessionBuilder.restStripeSession()
      .id(stripeSessionId.value())
      .url(stripeSessionId.url())
      .build();
  }
}
