package com.nextime.order.infrastructure.persistence.document;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String source;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
