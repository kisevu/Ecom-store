package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record UserEmail(String value) {

  public UserEmail {
    Assert.field("publicId",value).maxLength(255);
  }

}
