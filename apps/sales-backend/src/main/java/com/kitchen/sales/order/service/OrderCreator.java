package com.kitchen.sales.order.service;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.domain.order.aggregate.OrderedProduct;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.infrastructure.secondary.service.stripe.StripeService;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.aggregate.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: kev.Ameda
 */
public class OrderCreator {

  private final OrderRepository orderRepository;
  private final StripeService stripeService;

  public OrderCreator(OrderRepository orderRepository, StripeService stripeService) {
    this.orderRepository = orderRepository;
    this.stripeService = stripeService;
  }

  /**
   * injecting the sessionId directly as below
   * */
  public StripeSessionId create(List<Product> productsInformation,
                                List<DetailCartItemRequest> items,
                                User connectedUser){

    StripeSessionId stripeSessionId = this.stripeService.createPayment(connectedUser,productsInformation,items);
    List<OrderedProduct> orderedProducts = new ArrayList<>();
    for (DetailCartItemRequest itemRequest: items ){
      Product productDetails = productsInformation.stream()
        .filter(product -> product.getPublicId().value().equals(itemRequest.productId().value()))
        .findFirst().orElseThrow();
      OrderedProduct orderedProduct = OrderedProduct.create(itemRequest.quantity(), productDetails);
      orderedProducts.add(orderedProduct);
    }
    Order orderToCreate = Order.create(connectedUser, orderedProducts, stripeSessionId);
    orderRepository.save(orderToCreate);
    return stripeSessionId;
  }
}
