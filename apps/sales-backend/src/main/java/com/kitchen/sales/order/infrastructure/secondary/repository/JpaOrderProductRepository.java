package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.infrastructure.secondary.entity.OrderedProductEntity;
import com.kitchen.sales.order.infrastructure.secondary.entity.OrderedProductEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: kev.Ameda
 */
@Repository
public interface JpaOrderProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductEntityPk> {
}
