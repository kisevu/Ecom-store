package com.kitchen.sales.config.Authentication.domain;


import com.kitchen.sales.config.error.domain.Assert;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Author: kev.Ameda
 */
public record Username(String username) {

  public Username {
    Assert.field("username", username).notBlank().maxLength(100);
  }

  public String get() {
    return username();
  }

  public static Optional<Username> of(String username) {
    return Optional.ofNullable(username).filter(StringUtils::isNotBlank).map(Username::new);
  }
}
