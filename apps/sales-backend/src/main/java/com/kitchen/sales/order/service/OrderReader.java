package com.kitchen.sales.order.service;

import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: kev.Ameda
 */
public class OrderReader {
  private final OrderRepository orderRepository;

  public OrderReader(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public Page<Order> findAllByUserPublicId(UserPublicId userPublicId, Pageable pageable){
    return orderRepository.findAllByUserPublicId(userPublicId,pageable);
  }

  public Page<Order> findAll(Pageable pageable){
    return orderRepository.findAll(pageable);
  }


}
