package com.kitchen.sales.config.Authentication.infrastructure.primary;

import com.kitchen.sales.config.Authentication.application.AuthenticatedUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: kev.Ameda
 */
@Configuration
public class KindeJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  /**
   * This class satisfies a custom JWT-to-Authentication converter that merges
   * standard OAuth2 authorities with kinde-specific roles from the JWT token,creating a unified Authentication
   * object for spring security
  * */
  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt source) {
    return  new JwtAuthenticationToken(source,
      Stream.concat(
        new JwtGrantedAuthoritiesConverter()
          .convert(source)
          .stream(),extractResourceRoles(source).stream()
      ).collect(Collectors.toSet()));
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt){
    return AuthenticatedUser.extractRolesFromToken(jwt)
      .stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());
  }

}
