package com.kitchen.sales.order.domain.order.exceptions;

/**
 * Author: kev.Ameda
 */
public class CartPaymentException extends RuntimeException{
  public CartPaymentException(String message) {
    super(message);
  }
}
