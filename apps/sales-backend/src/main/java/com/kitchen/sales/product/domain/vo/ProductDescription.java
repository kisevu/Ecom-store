package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record ProductDescription(String value) {
  public ProductDescription {
    Assert.field("productId",value).notNull().minLength(3);
  }
}
