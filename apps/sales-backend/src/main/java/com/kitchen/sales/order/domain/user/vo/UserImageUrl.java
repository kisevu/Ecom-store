package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record UserImageUrl(String value) {

  public UserImageUrl {
    Assert.field("productId",value).maxLength(1000);
  }
}
