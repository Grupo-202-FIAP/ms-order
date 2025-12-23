package com.nextime.order.application.mapper;

import com.nextime.order.infrastructure.controller.dto.request.ProductRequest;
import com.nextime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nextime.order.infrastructure.persistence.document.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T20:21:38-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toDomain(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( request.name() );
        product.unitPrice( request.unitPrice() );

        return product.build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.name( product.getName() );
        productResponse.unitPrice( product.getUnitPrice() );

        return productResponse.build();
    }
}
