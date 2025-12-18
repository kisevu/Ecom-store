package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.order.domain.user.vo.UserAddressToUpdate;
import com.kitchen.sales.order.vo.StripeSessionId;
import org.jilt.Builder;
import java.util.List;
/**
 * Author: kev.Ameda
 */
@Builder
public record StripeSessionInformation(StripeSessionId stripeSessionId,
                                       UserAddressToUpdate userAddressToUpdate,
                                       List<OrderProductQuantity> orderProductQuantities) {
}
