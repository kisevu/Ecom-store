package com.kitchen.sales.order.domain.user.service;

import com.kitchen.sales.config.Authentication.application.AuthenticatedUser;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.domain.user.repository.UserRepository;
import com.kitchen.sales.order.domain.user.vo.UserAddressToUpdate;
import com.kitchen.sales.order.infrastructure.secondary.service.kinde.KindeService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: kev.Ameda
 */

@Service
public class UserSynchronizer {
  private final UserRepository userRepository;
  private final KindeService kindeService;

  private static final String UPDATE_AT_KEY = "last_signed_in";

  public UserSynchronizer(UserRepository userRepository, KindeService kindeService) {
    this.userRepository = userRepository;
    this.kindeService = kindeService;
  }

  /**
  *  token comes from kinde after authentication of the user
   * So on kinde side the user is known but in the application the user is not known, so to have a local copy
   * in our database we ought to provide a sync between the idp and our app. Also, kinde does not have some of the data i.e the
   * address, and so we need a copy.
  * */

  public void syncWithIdp(Jwt jwtToken, boolean forceResync){
    Map<String, Object> claims = jwtToken.getClaims();
    List<String> rolesFromToken = AuthenticatedUser.extractRolesFromToken(jwtToken);
    Map<String, Object> userInfo = kindeService.getUserInfo(claims.get("sub").toString());
    User user = User.fromTokenAttributes(userInfo, rolesFromToken);
    Optional<User> existingUser = userRepository.getOneByEmail(user.getEmail());
    if (existingUser.isPresent()){
      if (claims.get(UPDATE_AT_KEY) !=null){
        Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
        Instant idpModifiedDate = Instant.ofEpochSecond( (int) claims.get(UPDATE_AT_KEY));
        if (idpModifiedDate.isAfter(lastModifiedDate) | forceResync){
          updateUser(user,existingUser.get());
        }
      }
    } else {
      user.initFieldForSignUp();
      userRepository.save(user);
    }
  }

  private void updateUser(User user, User existingUser) {
    existingUser.updateFromUser(user);
    userRepository.save(existingUser);
}

  public void updateAddress(UserAddressToUpdate userAddressToUpdate){
    userRepository.updateAddress(userAddressToUpdate.userPublicId(),userAddressToUpdate);
  }

}
