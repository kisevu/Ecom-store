package com.kitchen.sales.order.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record OrderPrice(double value) {
  public OrderPrice {
    Assert.field("value",value).strictlyPositive();
  }
}
