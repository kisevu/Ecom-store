package com.kitchen.sales.order.application;

import com.kitchen.sales.order.domain.order.aggregate.*;
import com.kitchen.sales.order.domain.order.repository.OrderRepository;
import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.infrastructure.secondary.service.stripe.StripeService;
import com.kitchen.sales.order.service.CartReader;
import com.kitchen.sales.order.service.OrderCreator;
import com.kitchen.sales.order.service.OrderUpdater;
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
  private final OrderUpdater orderUpdater;


  public OrdersApplicationService(ProductsApplicationService productsApplicationService,
                                  UserApplicationService userApplicationService,
                                  OrderRepository orderRepository,
                                  StripeService stripeService) {
    this.productsApplicationService = productsApplicationService;
    this.userApplicationService = userApplicationService;
    this.orderCreator = new OrderCreator(orderRepository,stripeService);
    this.cartReader = new CartReader();
    this.orderUpdater = new OrderUpdater(orderRepository);
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

  @Transactional
  public void updateOrder(StripeSessionInformation stripeSessionInformation){
    List<OrderedProduct> orderedProducts = this.orderUpdater.updateOrderFromStripe(stripeSessionInformation);
    List<OrderProductQuantity> orderProductQuantities = this.orderUpdater.computeQuantity(orderedProducts);
    this.productsApplicationService.updateProductQuantity(orderProductQuantities);
    this.userApplicationService.updateAddress(stripeSessionInformation.userAddressToUpdate());
  }

}
