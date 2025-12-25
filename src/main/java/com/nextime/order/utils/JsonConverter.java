package com.nextime.order.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextime.order.application.exception.JsonConversionException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.infrastructure.persistence.document.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;
    private final LoggerRepositoryPort logger;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("[JsonConverter.toJson] Falha para converter objeto para JSON: {}", e);
            throw new JsonConversionException(
                    "Erro ao converter objeto para JSON",
                    e
            );
        }
    }

    public Event toEvent(String json) {
        try {
            return objectMapper.readValue(json, Event.class);
        } catch (Exception e) {
            logger.error("[JsonConverter.toEvent] Falha para converter JSON para Event: {}", e);
            throw new JsonConversionException(
                    "Erro ao converter JSON para Event",
                    e
            );
        }
    }

}
