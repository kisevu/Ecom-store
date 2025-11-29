package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record AuthorityName(String name) {

  public AuthorityName{
    Assert.field("name",name).notNull();
  }

}
