package com.kitchen.sales.product.infrastructure.primary.exceptions;

/**
 * Author: kev.Ameda
 */
public class EntityCreationFailedException extends RuntimeException{

  public EntityCreationFailedException() {
  }

  public EntityCreationFailedException(String message) {
    super(message);
  }
}
