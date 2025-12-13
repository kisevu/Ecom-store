package com.kitchen.sales.order.domain.order.aggregate;

import org.jilt.Builder;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Builder
public record DetailCartRequest(List<DetailCartItemRequest> items) {
}
