package com.nextime.order.infrastructure.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String source;
    private String status;
    private String message;
    @CreatedDate
    private LocalDateTime createdAt;
}
