package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;

/**
 * Author: kev.Ameda
 */
public record AuthorityName(String name) {

  public AuthorityName{
    Assert.field("name",name).notNull();
  }


}
