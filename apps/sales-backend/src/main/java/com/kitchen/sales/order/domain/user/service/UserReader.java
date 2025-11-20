package com.kitchen.sales.order.domain.user.service;

import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.domain.user.repository.UserRepository;
import com.kitchen.sales.order.domain.user.vo.UserEmail;
import com.kitchen.sales.order.domain.user.vo.UserPublicId;

import java.util.Optional;

/**
 * Author: kev.Ameda
 */

public class UserReader {
  private final UserRepository userRepository;

  public UserReader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getByEmail(UserEmail userEmail){
    return userRepository.getOneByEmail(userEmail);
  }

  public Optional<User> getByPublicId(UserPublicId userPublicId){
    return userRepository.get(userPublicId);
  }

}
