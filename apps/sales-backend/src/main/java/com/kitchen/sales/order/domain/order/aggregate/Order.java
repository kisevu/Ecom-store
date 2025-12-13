package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.order.domain.user.aggregate.User;
import com.kitchen.sales.order.vo.OrderStatus;
import com.kitchen.sales.order.vo.StripeSessionId;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public class Order {
  private OrderStatus status;
  private User user;
  private String stripeId;
  private PublicId publicId;
  private List<OrderedProduct> orderedProducts;

  public Order(OrderStatus status, User user, String stripeId, PublicId publicId, List<OrderedProduct> orderedProducts) {
    this.status = status;
    this.user = user;
    this.stripeId = stripeId;
    this.publicId = publicId;
    this.orderedProducts = orderedProducts;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public User getUser() {
    return user;
  }

  public String getStripeId() {
    return stripeId;
  }

  public PublicId getPublicId() {
    return publicId;
  }

  public List<OrderedProduct> getOrderedProducts() {
    return orderedProducts;
  }

  public static Order create(User connectedUser, List<OrderedProduct> orderedProducts, StripeSessionId stripeSessionId){
    return OrderBuilder.order()
      .status(OrderStatus.PENDING)
      .user(connectedUser)
      .stripeId(stripeSessionId.value())
      .publicId(new PublicId(UUID.randomUUID()))
      .orderedProducts(orderedProducts)
      .build();
  }

  public void validatePayment(){
    this.status = OrderStatus.PAID;
  }
}
