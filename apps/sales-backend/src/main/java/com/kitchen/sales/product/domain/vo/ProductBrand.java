package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record ProductBrand(String value) {
  public ProductBrand{
    Assert.field("productId",value).notNull().minLength(3);
  }
}
