package com.kitchen.sales.order.infrastructure.secondary.entity;

import com.kitchen.sales.order.domain.user.aggregate.Authority;
import com.kitchen.sales.order.domain.user.aggregate.AuthorityBuilder;
import com.kitchen.sales.order.domain.user.vo.AuthorityName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jilt.Builder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: kev.Ameda
 */
@Entity
@Table(name = "authority")
@Builder
public class AuthorityEntity implements Serializable {

  @NotNull
  @Size(max = 50)
  @Id
  @Column(length = 50)
  private String name;

  public AuthorityEntity() {
  }

  public AuthorityEntity(String name) {
    this.name = name;
  }

  public static Set<AuthorityEntity> from(Set<Authority> authorities) {
    return authorities.stream()
      .map(authority -> AuthorityEntityBuilder.authorityEntity()
        .name(authority.getName().name()).build()).collect(Collectors.toSet());
  }

  public static Set<Authority> toDomain(Set<AuthorityEntity> authorityEntities) {
    return authorityEntities.stream()
      .map(authorityEntity -> AuthorityBuilder.authority().name(new AuthorityName(authorityEntity.name)).build())
      .collect(Collectors.toSet());
  }
  public String getName() {
    return name;
  }

  public void setName(@NotNull @Size(max = 50) String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AuthorityEntity that)) return false;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName());
  }
}
