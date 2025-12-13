package com.kitchen.sales.order.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record OrderQuantity(long value) {

  public OrderQuantity {
    Assert.field("value",value).positive();
  }
}

