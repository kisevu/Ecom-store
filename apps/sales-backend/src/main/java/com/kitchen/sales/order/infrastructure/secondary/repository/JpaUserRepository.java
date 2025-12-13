package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.infrastructure.secondary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity,Long> {
  Optional<UserEntity> findByEmail(String email);
  List<UserEntity> findByPublicIdIn(List<UUID> publicIds);
  Optional<UserEntity> findOneByPublicId(UUID publicId);
//
//  @Modifying
//  @Query("UPDATE UserEntity user "+
//  "set user.addressStreet=:street, user.addressCity=:city, user.addressCountry=:country "+
//  " user.addressZipCode=:zipCode "+
//  " WHERE user.productId =: productId")
//  void updateAddress(UUID productId,String street, String zipCode, String city, String country);

  @Modifying
  @Query("UPDATE UserEntity  user " +
    "SET user.addressStreet = :street, user.addressCity = :city, " +
    " user.addressCountry = :country, user.addressZipCode = :zipCode " +
    "WHERE user.publicId = :userPublicId")
  void updateAddress(UUID publicId, String street, String zipCode, String city, String country);
}
