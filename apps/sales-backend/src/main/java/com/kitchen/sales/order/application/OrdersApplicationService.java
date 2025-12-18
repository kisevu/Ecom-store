package com.kitchen.sales.order.application;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.infrastructure.secondary.service.stripe.StripeService;
import com.kitchen.sales.order.service.CartReader;
import com.kitchen.sales.order.service.OrderCreator;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.application.ProductsApplicationService;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Service
public class OrdersApplicationService {

  private final ProductsApplicationService productsApplicationService;
  private final CartReader cartReader;
  private final UserApplicationService userApplicationService;
  private OrderCreator orderCreator;


  public OrdersApplicationService(ProductsApplicationService productsApplicationService,
                                  UserApplicationService userApplicationService,
                                  OrderRepository orderRepository,
                                  StripeService stripeService) {
    this.productsApplicationService = productsApplicationService;
    this.userApplicationService = userApplicationService;
    this.orderCreator = new OrderCreator(orderRepository,stripeService);
    this.cartReader = new CartReader();
  }

  @Transactional(readOnly = true)
  public DetailCartResponse getCartDetails(DetailCartRequest detailCartRequest){
    List<PublicId> publicIds = detailCartRequest.items()
      .stream()
      .map(DetailCartItemRequest::productId)
      .toList();
    List<Product> productInformation = productsApplicationService.getProductByPublicIdIn(publicIds);
    return cartReader.getDetails(productInformation);
  }

  @Transactional
  public StripeSessionId createOrder(List<DetailCartItemRequest>items){
    User authenticatedUser = userApplicationService.getAuthenticatedUser();
    List<PublicId> publicIds = items.stream().map(DetailCartItemRequest::productId).toList();
    List<Product> productInfos = productsApplicationService.getProductByPublicIdIn(publicIds);
    return orderCreator.create(productInfos, items, authenticatedUser);
  }

}
