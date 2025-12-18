package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.infrastructure.secondary.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: kev.Ameda
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity,Long> {
}
