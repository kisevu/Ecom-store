package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.order.vo.OrderQuantity;
import com.kitchen.sales.order.vo.ProductPublicId;
import org.jilt.Builder;
/**
 * Author: kev.Ameda
 */
@Builder
public record OrderProductQuantity(OrderQuantity orderQuantity,
                                   ProductPublicId productPublicId) {
}
