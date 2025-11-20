package com.kitchen.sales.order.infrastructure.secondary.entity;

import com.kitchen.sales.config.jpa.AbstractAuditingEntity;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.domain.user.aggregate.UserBuilder;
import com.kitchen.sales.order.domain.user.vo.*;
import jakarta.persistence.*;
import org.jilt.Builder;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: kev.Ameda
 */
@Entity
@Table(name="ecommerce_user")
@Builder
public class UserEntity extends AbstractAuditingEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
  @SequenceGenerator(name = "userSequenceGenerator", sequenceName = "user_sequence", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "public_id")
  private UUID publicId;

  @Column(name = "address_street")
  private String addressStreet;

  @Column(name = "address_city")
  private String addressCity;

  @Column(name = "address_zip_code")
  private String addressZipCode;

  @Column(name = "address_country")
  private String addressCountry;

  @Column(name = "last_seen")
  private Instant lastSeen;

  public UserEntity(Long id, String firstName, String lastName,
                    String email, String imageUrl, UUID publicId,
                    String addressStreet, String addressCity,
                    String addressZipCode, String addressCountry,
                    Instant lastSeen, Set<AuthorityEntity> authorities) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.imageUrl = imageUrl;
    this.publicId = publicId;
    this.addressStreet = addressStreet;
    this.addressCity = addressCity;
    this.addressZipCode = addressZipCode;
    this.addressCountry = addressCountry;
    this.lastSeen = lastSeen;
    this.authorities = authorities;
  }

  public UserEntity() {
  }

  @ManyToMany(cascade = CascadeType.REMOVE)
  @JoinTable( name = "user_authority",
  joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
  inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "name")})
  private Set<AuthorityEntity> authorities = new HashSet<>();

  public void updateFromUser(User user){
    this.email = user.getEmail().value();
    this.firstName = user.getFirstName().value();
    this.lastName = user.getLastName().value();
    this.imageUrl = user.getImageUrl().value();
    this.lastSeen = user.getLastSeen();
  }

  public static UserEntity from(User user){
    UserEntityBuilder userEntityBuilder = UserEntityBuilder.userEntity();

    if ( user.getImageUrl() !=null ){
      userEntityBuilder.imageUrl(user.getImageUrl().value());
    }

    if (user.getUserPublicId() !=null ){
      userEntityBuilder.publicId(user.getUserPublicId().value());
    }

    if(user.getUserAddress()!=null ){
      userEntityBuilder.addressCity(user.getUserAddress().city());
      userEntityBuilder.addressStreet(user.getUserAddress().street());
      userEntityBuilder.addressCountry(user.getUserAddress().country());
      userEntityBuilder.addressZipCode(user.getUserAddress().zipcode());
    }

    return userEntityBuilder
      .authorities(AuthorityEntity.from(user.getAuthorities()))
      .email(user.getEmail().value())
      .firstName(user.getFirstName().value())
      .lastName(user.getLastName().value())
      .lastSeen(user.getLastSeen())
      .id(user.getDbId())
      .build();
  }

  public static  User toDomain(UserEntity userEntity){
    UserBuilder userBuilder = UserBuilder.user();
    if(userEntity.getImageUrl() !=null ){
      userBuilder.imageUrl(new UserImageUrl(userEntity.getImageUrl()));
    }

    if(userEntity.getAddress_street()!=null){
      userBuilder.userAddress(
        UserAddressBuilder.userAddress()
          .city(userEntity.getAddressCity())
          .country(userEntity.getAddressCountry())
          .zipcode(userEntity.getAddressZipCode())
          .street(userEntity.getAddress_street())
          .build());
    }

    return userBuilder
      .email(new UserEmail(userEntity.getEmail()))
      .firstName(new UserFirstName(userEntity.getFirstName()))
      .lastName(new UserLastName(userEntity.getLastName()))
      .userPublicId(new UserPublicId(userEntity.getPublicId()))
      .authorities(AuthorityEntity.toDomain(userEntity.getAuthorities()))
      .lastModifiedDate(userEntity.getLastModifiedDate())
      .createdDate(userEntity.getCreatedDate())
      .dbId(userEntity.getId())
      .build();
  }

  public static Set<UserEntity> from(List<User> users){
    return users.stream()
      .map(UserEntity::from)
      .collect(Collectors.toSet());
  }

  public static Set<User> toDomain(List<UserEntity> userEntity){
    return userEntity.stream()
      .map(UserEntity::toDomain)
      .collect(Collectors.toSet());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getAddress_street() {
    return addressStreet;
  }

  public void setAddress_street(String address_street) {
    this.addressStreet = address_street;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressZipCode() {
    return addressZipCode;
  }

  public void setAddressZipCode(String addressZipCode) {
    this.addressZipCode = addressZipCode;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public Instant getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(Instant lastSeen) {
    this.lastSeen = lastSeen;
  }

  public Set<AuthorityEntity> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<AuthorityEntity> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof UserEntity that)) return false;
    return Objects.equals(getPublicId(), that.getPublicId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPublicId());
  }
}
