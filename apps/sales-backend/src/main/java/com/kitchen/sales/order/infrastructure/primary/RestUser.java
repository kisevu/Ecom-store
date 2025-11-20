package com.kitchen.sales.order.infrastructure.primary;

import com.kitchen.sales.order.domain.user.aggregate.Authority;
import com.kitchen.sales.order.domain.user.aggregate.User;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */

@Builder
public record RestUser(
  UUID publicId,
  String firstName,
  String lastName,
  String email,
  String imageUrl,
  Set<String> authorities
) {

  public static RestUser from(User user) {
    RestUserBuilder restUserBuilder = RestUserBuilder.restUser();

    if(user.getImageUrl() != null) {
      restUserBuilder.imageUrl(user.getImageUrl().value());
    }

    return restUserBuilder
      .email(user.getEmail().value())
      .firstName(user.getFirstName().value())
      .lastName(user.getLastName().value())
      .publicId(user.getUserPublicId().value())
      .authorities(RestAuthority.fromSet(user.getAuthorities()))
      .build();
  }

}
