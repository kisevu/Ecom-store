package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record UserAddressToUpdate(UserPublicId userPublicId, UserAddress userAddress) {
  public UserAddressToUpdate {
    Assert.notNull("productId",userPublicId);
    Assert.notNull("productId",userAddress);
  }

}
