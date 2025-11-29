package com.kitchen.sales.order.domain.user.repository;

import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.domain.user.vo.UserAddressToUpdate;
import com.kitchen.sales.order.domain.user.vo.UserEmail;
import com.kitchen.sales.order.domain.user.vo.UserPublicId;

import java.util.Optional;

/**
 * Author: kev.Ameda
 */

public interface UserRepository {

  void save(User user);
  Optional<User> get(UserPublicId userPublicId);
  Optional<User> getOneByEmail(UserEmail userEmail);
  void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddressToUpdate);
}
