package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.infrastructure.secondary.entity.OrderEntity;
import org.springframework.stereotype.Repository;

/**
 * Author: kev.Ameda
 */
@Repository
public class SpringDataOrderRepository implements OrderRepository {

  private final JpaOrderProductRepository orderProductRepository;
  private final JpaOrderRepository jpaOrderRepository;

  public SpringDataOrderRepository(JpaOrderProductRepository orderProductRepository,
                                   JpaOrderRepository jpaOrderRepository) {
    this.orderProductRepository = orderProductRepository;
    this.jpaOrderRepository = jpaOrderRepository;
  }

  @Override
  public void save(Order order) {
    OrderEntity orderEntityToCreate = OrderEntity.fromDomain(order);
    OrderEntity savedOrderEntity = jpaOrderRepository.save(orderEntityToCreate);
    savedOrderEntity.getOrderedProducts()
      .forEach(orderedProductEntity -> orderedProductEntity.getId().setOrder(savedOrderEntity));
    orderProductRepository.saveAll(savedOrderEntity.getOrderedProducts());
  }


}
