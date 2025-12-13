package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record DetailCartItemRequest(PublicId productId, long quantity) {
}
