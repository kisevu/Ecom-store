package com.kitchen.sales.product.infrastructure.primary;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.product.aggregate.Picture;
import com.kitchen.sales.product.aggregate.PictureBuilder;
import org.jilt.Builder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestPicture( byte[] file, String mimeType) {
  public RestPicture {
    Assert.notNull("file",file);
    Assert.notNull("mimeType",mimeType);
  }

  public static Picture toDomain(RestPicture restPicture){
    return PictureBuilder
      .picture()
      .file(restPicture.file())
      .mimeType(restPicture.mimeType())
      .build();
  }

  public static List<Picture> toDomain(List<RestPicture> restPictures){
    return restPictures.stream()
      .map(RestPicture::toDomain)
      .collect(Collectors.toList());
  }

  public static RestPicture fromDomain(Picture picture){
    return RestPictureBuilder
      .restPicture()
      .file(picture.file())
      .mimeType(picture.mimeType())
      .build();
  }

  public static List<RestPicture> fromDomain(List<Picture> pictures){
    return pictures.stream()
      .map(RestPicture::fromDomain)
      .collect(Collectors.toList());
  }
}
