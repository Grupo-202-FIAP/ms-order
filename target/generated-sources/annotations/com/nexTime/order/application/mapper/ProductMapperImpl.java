package com.nexTime.order.application.mapper;

import com.nexTime.order.domain.enums.Category;
import com.nexTime.order.infrastructure.controller.dto.request.ProductRequest;
import com.nexTime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nexTime.order.infrastructure.persistence.document.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T01:12:15-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toDomain(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Category category = null;
        BigDecimal unitPrice = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ProductResponse productResponse = new ProductResponse( id, name, category, unitPrice, createdAt, updatedAt );

        return productResponse;
    }
}
