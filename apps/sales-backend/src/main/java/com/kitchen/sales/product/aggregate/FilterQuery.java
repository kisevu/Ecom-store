package com.kitchen.sales.product.aggregate;

import com.kitchen.sales.product.domain.vo.ProductSize;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Builder
public record FilterQuery(PublicId categoryPublicId,
                          List<ProductSize> sizes) {

}
