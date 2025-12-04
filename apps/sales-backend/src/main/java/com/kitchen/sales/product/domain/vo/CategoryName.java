package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record CategoryName(String value) {
  public CategoryName{
    Assert.field("publicId",value).notNull().minLength(3);
  }
}
