package com.kitchen.sales.order.infrastructure.secondary.repository;

import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.domain.order.aggregate.StripeSessionInformation;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.domain.user.vo.UserPublicId;
import com.kitchen.sales.order.infrastructure.secondary.entity.OrderEntity;
import com.kitchen.sales.order.vo.OrderStatus;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

  @Override
  public void updateStatusByPublicId(OrderStatus orderStatus, PublicId orderPublicId) {
    jpaOrderRepository.updateStatusByPublicId(orderStatus,orderPublicId.value());
  }

  @Override
  public Optional<Order> findByStripeSessionId(StripeSessionInformation stripeSessionInformation) {
    return jpaOrderRepository.findByStripeSessionId(stripeSessionInformation.stripeSessionId().value())
      .map(OrderEntity::toDomain);
  }

  @Override
  public Page<Order> findAllByUserPublicId(UserPublicId userPublicId, Pageable pageable) {
    return jpaOrderRepository.findAllByUserPublicId(userPublicId.value(),pageable)
      .map(OrderEntity::toDomain);
  }

  @Override
  public Page<Order> findAll(Pageable pageable) {
    return jpaOrderRepository.findAll(pageable).map(OrderEntity::toDomain);
  }
}
