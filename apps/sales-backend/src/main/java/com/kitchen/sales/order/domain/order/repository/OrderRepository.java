package com.kitchen.sales.order.domain.order.repository;

import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.domain.order.aggregate.StripeSessionInformation;
import com.kitchen.sales.order.domain.user.vo.UserPublicId;
import com.kitchen.sales.order.vo.OrderStatus;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
/**
 * Author: kev.Ameda
 */
public interface OrderRepository {
  void save(Order order);
  void updateStatusByPublicId(OrderStatus orderStatus, PublicId orderPublicId);
  Optional<Order> findByStripeSessionId(StripeSessionInformation stripeSessionInformation);
  Page<Order> findAllByUserPublicId(UserPublicId userPublicId, Pageable pageable);
  Page<Order> findAll(Pageable pageable);
}

