package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;

import java.util.UUID;

/**
 * Author: kev.Ameda
 */
public record UserPublicId(UUID value) {

  public UserPublicId {
    Assert.notNull("publicId",value);
  }
}
