package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record UserLastName(String value) {
  public UserLastName {
    Assert.field("value",value).maxLength(255);
  }
}
