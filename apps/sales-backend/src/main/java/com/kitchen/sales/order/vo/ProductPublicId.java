package com.kitchen.sales.order.vo;

import com.kitchen.sales.config.error.domain.Assert;

import java.util.UUID;

/**
 * Author: kev.Ameda
 */
public record ProductPublicId(UUID value) {
  public ProductPublicId {
    Assert.notNull("value",value);
  }
}
