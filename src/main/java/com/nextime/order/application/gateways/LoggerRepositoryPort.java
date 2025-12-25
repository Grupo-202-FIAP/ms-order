package com.nextime.order.application.gateways;

public interface LoggerRepositoryPort {
    void info(String msg, Object... args);

    void debug(String msg, Object... args);

    void warn(String msg, Object... args);

    void error(String msg, Throwable t, Object... args);

    void error(String msg, Object... args);
}
