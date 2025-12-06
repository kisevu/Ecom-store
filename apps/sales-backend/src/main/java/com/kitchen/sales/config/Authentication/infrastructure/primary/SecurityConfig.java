package com.kitchen.sales.config.Authentication.infrastructure.primary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Author: kev.Ameda
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
     httpSecurity.authorizeHttpRequests( auth ->
        auth
          .requestMatchers(HttpMethod.GET,"api/categories").permitAll()
          .requestMatchers(HttpMethod.GET, "/api/users/authenticated").permitAll()
          .requestMatchers("/api/**").authenticated())
      .csrf(AbstractHttpConfigurer::disable)
      .oauth2ResourceServer(oauth2 ->
        oauth2.jwt(jwt ->
          jwt.jwtAuthenticationConverter(new KindeJwtAuthenticationConverter())));
      return httpSecurity.build();
  }
}
