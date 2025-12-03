package com.kitchen.sales.product.infrastructure.secondary.repository;

import com.kitchen.sales.product.infrastructure.secondary.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: kev.Ameda
 */
public interface JpaProductPictureRepository extends JpaRepository<PictureEntity,Long> {

}
