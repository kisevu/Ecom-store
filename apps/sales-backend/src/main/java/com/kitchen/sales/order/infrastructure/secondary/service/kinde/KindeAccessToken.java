package com.kitchen.sales.order.infrastructure.secondary.service.kinde;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: kev.Ameda
 */
public record KindeAccessToken(
  @JsonProperty("access_token") String accessToken,
  @JsonProperty("token_type") String tokenType) {
}
