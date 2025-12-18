package com.kitchen.sales.product.infrastructure.secondary.repository;

import com.kitchen.sales.order.vo.ProductPublicId;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.vo.ProductSize;
import com.kitchen.sales.product.infrastructure.secondary.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface JpaProductRepository extends JpaRepository<ProductEntity,Long> {
  Optional<ProductEntity> findByPublicId(UUID publicId);
  int deleteByPublicId(UUID publicId);
  Page<ProductEntity> findAllByFeaturedTrue(Pageable pageable);
  Page<ProductEntity> findByCategoryPublicIdAndPublicIdNot(Pageable pageable, UUID categoryPublicId,UUID excludedProductPublicId);

  @Query("SELECT product FROM ProductEntity product WHERE (:sizes is null or product.size IN (:sizes)) AND "+
  "product.category.publicId=:categoryPublicId")
  Page<ProductEntity> findByCategoryPublicIdAndSizesIn(Pageable pageable, UUID categoryPublicId, List<ProductSize> sizes);
  List<ProductEntity> findAllByPublicIdIn(List<UUID> publicIdsUUIDs);

  @Modifying
  @Query("UPDATE ProductEntity  product " +
    "SET product.nbInStock = product.nbInStock - :quantity " +
    "WHERE product.publicId = :productPublicId")
  void updateQuantity(UUID productPublicId, long quantity);
}
