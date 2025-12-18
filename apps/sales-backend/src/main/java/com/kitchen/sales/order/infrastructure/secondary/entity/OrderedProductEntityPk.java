package com.kitchen.sales.order.infrastructure.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.jilt.Builder;

import java.util.Objects;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Embeddable
@Builder
public class OrderedProductEntityPk {
  /**
   *   composite key for the order.
   *   order id should contain the order id and product id
   */

  @ManyToOne
  @JoinColumn(name = "fk_order", nullable = false)
  private OrderEntity order;

  @Column(name = "fk_product", nullable = false)
  private UUID productPublicId;

  public OrderedProductEntityPk() {
  }

  public OrderedProductEntityPk(OrderEntity order, UUID productPublicId) {
    this.order = order;
    this.productPublicId = productPublicId;
  }

  public OrderEntity getOrder() {
    return order;
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }

  public UUID getProductPublicId() {
    return productPublicId;
  }

  public void setProductPublicId(UUID productPublicId) {
    this.productPublicId = productPublicId;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof OrderedProductEntityPk that)) return false;
    return Objects.equals(getProductPublicId(), that.getProductPublicId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getProductPublicId());
  }
}
