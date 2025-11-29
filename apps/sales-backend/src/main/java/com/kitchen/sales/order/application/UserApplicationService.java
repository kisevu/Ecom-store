package com.kitchen.sales.order.application;

import com.kitchen.sales.config.Authentication.application.AuthenticatedUser;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.domain.user.repository.UserRepository;
import com.kitchen.sales.order.domain.user.service.UserReader;
import com.kitchen.sales.order.domain.user.service.UserSynchronizer;
import com.kitchen.sales.order.domain.user.vo.UserAddressToUpdate;
import com.kitchen.sales.order.domain.user.vo.UserEmail;
import com.kitchen.sales.order.infrastructure.secondary.service.kinde.KindeService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: kev.Ameda
 */
@Service
public class UserApplicationService {

  /**
   * Handles the framework layer
  * */

  private final UserSynchronizer userSynchronizer;
  private final UserReader userReader;

  public UserApplicationService(UserRepository userRepository,KindeService kindeService) {
    this.userSynchronizer = new UserSynchronizer(userRepository, kindeService);
    this.userReader = new UserReader(userRepository);
  }

  @Transactional
  public User getAuthenticatedUserWithSync(Jwt jwtToken ,boolean forceSync){
    userSynchronizer.syncWithIdp(jwtToken,forceSync);
    return userReader.getByEmail(new UserEmail(AuthenticatedUser.username().get()))
      .orElseThrow();
  }

  @Transactional(readOnly = true)
  public User getAuthenticatedUser(Jwt jwtToken ,boolean forceSync){
    return userReader.getByEmail(new UserEmail(AuthenticatedUser.username().get()))
      .orElseThrow();
  }

  @Transactional
  public void updateAddress(UserAddressToUpdate userAddressToUpdate){
    userSynchronizer.updateAddress(userAddressToUpdate);
  }

}
