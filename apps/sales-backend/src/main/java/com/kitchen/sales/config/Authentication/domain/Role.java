package com.kitchen.sales.config.Authentication.domain;

import com.kitchen.sales.config.error.domain.Assert;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: kev.Ameda
 */
public enum Role {
  ADMIN,
  USER,
  ANONYMOUS,
  UNKNOWN;

  private static final String PREFIX ="ROLE_";
  private static final Map<String, Role> ROLES = buildRoles();

  private static Map<String, Role> buildRoles() {
    return Stream.of(values()).collect(Collectors.toUnmodifiableMap(Role::key, Function.identity()));
  }

  public String key(){
    return PREFIX + name();
  }

  public static Role from( String role){
    Assert.notBlank("role",role);
    return ROLES.getOrDefault(role,UNKNOWN);
  }

}
