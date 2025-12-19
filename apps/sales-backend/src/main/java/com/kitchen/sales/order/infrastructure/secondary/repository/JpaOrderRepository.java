package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.infrastructure.secondary.entity.OrderEntity;
import com.kitchen.sales.order.vo.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity,Long> {

  @Modifying
  @Query("UPDATE OrderEntity order SET order.status =:orderStatus WHERE order.publicId =:orderPublicId")
  void updateStatusByPublicId(OrderStatus orderStatus, UUID orderPublicId);
  Optional<OrderEntity> findByStripeSessionId(String stripeSessionId);
  Page<OrderEntity> findAllByUserPublicId(UUID userPublicId, Pageable pageable);
}
