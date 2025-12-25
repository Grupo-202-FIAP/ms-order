package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {
    private final IEventRepository eventRepository;
    private final LoggerRepositoryPort logger;

    @Override
    public List<Event> findAllOrderedByCreationDate() {
        try {
            logger.info("[EventRepositoryAdapter.findAllOrderedByCreationDate] Buscando eventos ordenados por data de criação");

            return eventRepository.findAllByOrderByCreatedAtDesc()
                    .stream()
                    .toList();

        } catch (DataAccessException ex) {
            logger.error("[EventRepositoryAdapter.findAllOrderedByCreationDate] Erro ao buscar eventos no banco", ex);
            throw new RepositoryException("Erro ao acessar base de dados", ex);
        }
    }

    @Override
    public Optional<Event> findLatestByOrderId(UUID orderId) {
        try {
            logger.info("[EventRepositoryAdapter.findLatestByOrderId] Buscando último evento por orderId={}", orderId);

            return eventRepository
                    .findTop1ByOrderIdOrderByCreatedAtDesc(orderId);

        } catch (DataAccessException ex) {
            logger.error("[EventRepositoryAdapter.findLatestByOrderId] Erro ao buscar evento por orderId", ex);
            throw new RepositoryException("Erro ao buscar evento por orderId", ex);
        }
    }

    @Override
    public Optional<Event> findLatestByTransactionId(UUID transactionId) {
        try {
            logger.info("[EventRepositoryAdapter.findLatestByTransactionId] Buscando último evento por transactionId={}", transactionId);

            return eventRepository
                    .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId);

        } catch (DataAccessException ex) {
            logger.error("[EventRepositoryAdapter.findLatestByTransactionId] Erro ao buscar evento por transactionId", ex);
            throw new RepositoryException("Erro ao buscar evento por transactionId", ex);
        }
    }

    @Override
    public Event save(Event event) {
        try {
            if (event.getCreatedAt() == null) {
                event.setCreatedAt(LocalDateTime.now());
            }

            final Event saved = eventRepository.save(event);

            logger.info(
                    "[EventRepositoryAdapter.save] Pedido: {} notificado com transactionId: {}",
                    saved.getOrderId(),
                    saved.getTransactionId()
            );

            return saved;

        } catch (DataAccessException ex) {
            logger.error("[EventRepositoryAdapter.save] Erro ao salvar evento", ex);
            throw new RepositoryException("Erro ao salvar evento", ex);
        }
    }
}




