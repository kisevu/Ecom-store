package com.kitchen.sales.order.domain.order.repository;

import com.kitchen.sales.order.domain.order.aggregate.Order;

/**
 * Author: kev.Ameda
 */
public interface OrderRepository {
  void save(Order order);
}

