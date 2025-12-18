package com.kitchen.sales.order.service;

import com.kitchen.sales.order.domain.order.aggregate.*;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: kev.Ameda
 */

public class OrderUpdater {

  private final OrderRepository orderRepository;

  public OrderUpdater(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public List<OrderedProduct> updateOrderFromStripe(StripeSessionInformation stripeSessionInformation){
    Order order = orderRepository.findByStripeSessionId(stripeSessionInformation)
      .orElseThrow();
    order.validatePayment();
    orderRepository.updateStatusByPublicId(order.getStatus(),order.getPublicId());
    return order.getOrderedProducts();
  }

  public List<OrderProductQuantity> computeQuantity(List<OrderedProduct> orderedProducts){
    List<OrderProductQuantity> orderProductQuantities = new ArrayList<>();
    for (OrderedProduct orderedProduct: orderedProducts){
      OrderProductQuantity orderedProductQuantity = OrderProductQuantityBuilder.orderProductQuantity()
        .productPublicId(orderedProduct.getProductPublicId())
        .orderQuantity(orderedProduct.getQuantity())
        .build();
      orderProductQuantities.add(orderedProductQuantity);
    }
    return orderProductQuantities;
  }

}
