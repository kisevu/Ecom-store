package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record ProductPrice( double value ) {
  public ProductPrice {
    Assert.field("publicId", value).min(0.1);
  }
}
