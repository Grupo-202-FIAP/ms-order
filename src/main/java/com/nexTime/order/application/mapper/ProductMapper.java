package com.nexTime.order.application.mapper;


import com.nexTime.order.infrastructure.controller.dto.request.ProductRequest;
import com.nexTime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nexTime.order.infrastructure.persistence.document.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface ProductMapper {
    Product toDomain(ProductRequest request);

    ProductResponse toResponse(Product product);
}