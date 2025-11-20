package com.kitchen.sales.order.domain.user.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.order.domain.user.vo.AuthorityName;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public class Authority {

  private AuthorityName name;

  public Authority(AuthorityName name) {
    Assert.notNull("name", name);
    this.name = name;
  }

  public AuthorityName getName() {
    return name;
  }



}
