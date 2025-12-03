package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record ProductName(String value) {
  public ProductName {
    Assert.notNull("value",value);
    Assert.field("value",value).notNull().minLength(3).maxLength(256);
  }
}
