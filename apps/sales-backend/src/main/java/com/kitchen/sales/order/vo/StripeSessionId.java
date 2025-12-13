package com.kitchen.sales.order.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record StripeSessionId(String value) {
  public StripeSessionId {
    Assert.notNull("value",value);
  }
}
