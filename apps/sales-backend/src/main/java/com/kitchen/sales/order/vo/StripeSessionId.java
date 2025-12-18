package com.kitchen.sales.order.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record StripeSessionId(String value, String url) {
  public StripeSessionId {
    Assert.notNull("value",value);
    Assert.notNull("url",url);
  }
}
