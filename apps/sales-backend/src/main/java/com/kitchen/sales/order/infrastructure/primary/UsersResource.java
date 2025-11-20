package com.kitchen.sales.order.infrastructure.primary;

import com.kitchen.sales.order.application.UserApplicationService;
import com.kitchen.sales.order.domain.user.aggregate.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/users")
public class UsersResource {

  private final UserApplicationService userApplicationService;

  public UsersResource(UserApplicationService userApplicationService) {
    this.userApplicationService = userApplicationService;
  }

  @GetMapping("/authenticated")
  public ResponseEntity<RestUser> getAuthenticatedUser(@AuthenticationPrincipal Jwt  jwtToken,
                                                       @RequestParam boolean forceSync){
    User authenticatedUser = userApplicationService.getAuthenticatedUserWithSync(jwtToken, forceSync);
    RestUser restUser = RestUser.from(authenticatedUser);
    return ResponseEntity.ok(restUser);
  }

}
