package com.kitchen.sales.order.domain.user.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.order.domain.user.vo.*;
import org.jilt.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: kev.Ameda
 */

@Builder
public class User {

  private UserFirstName firstName;
  private UserLastName lastName;
  private UserEmail email;
  private UserPublicId userPublicId;
  private UserImageUrl imageUrl;
  private Instant lastModifiedDate;
  private Instant createdDate;
  private Set<Authority> authorities;
  private Long dbId;
  private UserAddress userAddress;
  private Instant lastSeen;

  public User(UserFirstName firstName, UserLastName lastName,
              UserEmail email, UserPublicId userPublicId,
              UserImageUrl imageUrl, Instant lastModifiedDate,
              Instant createdDate, Set<Authority> authorities,
              Long dbId, UserAddress userAddress, Instant lastSeen) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.userPublicId = userPublicId;
    this.imageUrl = imageUrl;
    this.lastModifiedDate = lastModifiedDate;
    this.createdDate = createdDate;
    this.authorities = authorities;
    this.dbId = dbId;
    this.userAddress = userAddress;
    this.lastSeen = lastSeen;
  }

  public void assertMandatoryFields(){
    Assert.notNull("firstName",firstName);
    Assert.notNull("lastName",lastName);
    Assert.notNull("email",email);
    Assert.notNull("authorities",authorities);
  }

  public void updateFromUser(User user){
    this.email = user.email;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.imageUrl = user.imageUrl;
  }
  public void initFieldForSignUp(){
    this.userPublicId =
      new UserPublicId(UUID.randomUUID());
  }

  public static User fromTokenAttributes(Map<String,Object> attributes, List<String> rolesFromAccessToken) {
    UserBuilder userBuilder = UserBuilder.user();

    if (attributes.containsKey("preferred_email")){
      userBuilder.email(new UserEmail(attributes.get("preferred_email").toString()));
    }

    if (attributes.containsKey("last_name")){
      userBuilder.lastName(new UserLastName(attributes.get("last_name").toString()));
    }

    if (attributes.containsKey("first_name")){
      userBuilder.firstName(new UserFirstName(attributes.get("first_name").toString()));
    }

    if (attributes.containsKey("picture")){
      userBuilder.imageUrl(new UserImageUrl(attributes.get("picture").toString()));
    }

    if (attributes.containsKey("last_signed_in")){
      userBuilder.lastSeen(Instant.parse(attributes.get("last_signed_in").toString()));
    }

    Set<Authority> authorities = rolesFromAccessToken.stream()
      .map(authority -> AuthorityBuilder.authority().name(new AuthorityName(authority)).build())
      .collect(Collectors.toSet());
    userBuilder.authorities(authorities);
    return userBuilder.build();
  }


  public UserFirstName getFirstName() {
    return firstName;
  }

  public UserLastName getLastName() {
    return lastName;
  }

  public UserEmail getEmail() {
    return email;
  }

  public UserPublicId getUserPublicId() {
    return userPublicId;
  }

  public UserImageUrl getImageUrl() {
    return imageUrl;
  }

  public Instant getLastModifiedDate() {
    return lastModifiedDate;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public Long getDbId() {
    return dbId;
  }

  public UserAddress getUserAddress() {
    return userAddress;
  }

  public Instant getLastSeen() {
    return lastSeen;
  }
}
