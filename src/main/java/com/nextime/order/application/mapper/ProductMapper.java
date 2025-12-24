package com.nextime.order.application.mapper;


import com.nextime.order.infrastructure.controller.dto.request.ProductRequest;
import com.nextime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nextime.order.infrastructure.persistence.document.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductRequest request);

    ProductResponse toResponse(Product product);
}
