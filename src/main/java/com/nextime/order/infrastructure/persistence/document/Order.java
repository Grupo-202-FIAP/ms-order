package com.nextime.order.infrastructure.persistence.document;

import com.nextime.order.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private UUID id;
    private UUID transactionId;
    private String identifier;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private UUID customerId;
    private OrderStatus status;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final int END_ID = 4;

    public BigDecimal calculateTotalPrice() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> item.getProduct().getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String generateOrderId() {
        final String shortUUID = UUID.randomUUID().toString().substring(0, END_ID).toUpperCase();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        final String datetime = LocalDateTime.now().format(dtf);
        final String shuffledDateTime = shuffleString(datetime);
        return "ORD-" + shortUUID + "-" + shuffledDateTime.substring(0, END_ID);
    }

    private String shuffleString(String input) {
        final List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        final StringBuilder output = new StringBuilder(input.length());
        for (char c : characters) {
            output.append(c);
        }
        return output.toString();
    }


}
