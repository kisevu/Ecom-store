package com.kitchen.sales.product.domain.vo;

import com.kitchen.sales.config.error.domain.Assert;

import java.util.UUID;

/**
 * Author: kev.Ameda
 */
public record PublicId( UUID value ) {
  public PublicId{
    Assert.notNull("publicId",value);
  }
}
