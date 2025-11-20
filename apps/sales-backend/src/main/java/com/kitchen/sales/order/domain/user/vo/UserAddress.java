package com.kitchen.sales.order.domain.user.vo;

import com.kitchen.sales.config.error.domain.Assert;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record UserAddress( String street, String city, String zipcode,String country) {

  public UserAddress {
    Assert.field("street",street).notNull();
    Assert.field("city",city).notNull();
    Assert.field("zipcode",zipcode).notNull();
    Assert.field("country",country).notNull();
  }

}
