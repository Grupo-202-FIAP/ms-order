package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerRepositoryAdapter implements LoggerRepositoryPort {

    private final Logger logger = LoggerFactory.getLogger(LoggerRepositoryAdapter.class);

    @Override
    public void info(String msg, Object... args) {
        logger.info(msg, args);
    }

    @Override
    public void debug(String msg, Object... args) {
        logger.debug(msg, args);
    }

    @Override
    public void warn(String msg, Object... args) {
        logger.warn(msg, args);
    }

    @Override
    public void error(String msg, Throwable t, Object... args) {
        logger.error(String.format(msg, args), t);
    }

    @Override
    public void error(String msg, Object... args) {
        logger.error(msg, args);
    }
}
