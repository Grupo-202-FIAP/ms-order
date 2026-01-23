package com.nextime.order.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentStatusTest {

    @Test
    void shouldHaveAllPaymentStatuses() {
        // Then
        assertThat(PaymentStatus.values()).hasSize(3);
        assertThat(PaymentStatus.PROCESSED).isNotNull();
        assertThat(PaymentStatus.PENDING).isNotNull();
        assertThat(PaymentStatus.EXPIRED).isNotNull();
    }

    @Test
    void shouldReturnCorrectStatusString() {
        // Then
        assertThat(PaymentStatus.PROCESSED.getStatus()).isEqualTo("PROCESSED");
        assertThat(PaymentStatus.PENDING.getStatus()).isEqualTo("PENDING");
        assertThat(PaymentStatus.EXPIRED.getStatus()).isEqualTo("EXPIRED");
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        // Then
        assertThat(PaymentStatus.valueOf("PROCESSED")).isEqualTo(PaymentStatus.PROCESSED);
        assertThat(PaymentStatus.valueOf("PENDING")).isEqualTo(PaymentStatus.PENDING);
        assertThat(PaymentStatus.valueOf("EXPIRED")).isEqualTo(PaymentStatus.EXPIRED);
    }
}

