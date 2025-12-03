package com.kitchen.sales.product.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public record Picture( byte[] file, String mimeType) {
  public Picture {
    Assert.notNull("file",file);
    Assert.notNull("mimeType",mimeType);
  }
}
