package com.nextime.order.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void shouldHaveAllCategories() {
        // Then
        assertThat(Category.values()).hasSize(4);
        assertThat(Category.SANDWICHES).isNotNull();
        assertThat(Category.SIDES).isNotNull();
        assertThat(Category.DRINKS).isNotNull();
        assertThat(Category.DESSERTS).isNotNull();
    }

    @Test
    void shouldReturnCorrectCategoryString() {
        // Then
        assertThat(Category.SANDWICHES.getCategory()).isEqualTo("SANDWICHES");
        assertThat(Category.SIDES.getCategory()).isEqualTo("SIDES");
        assertThat(Category.DRINKS.getCategory()).isEqualTo("DRINKS");
        assertThat(Category.DESSERTS.getCategory()).isEqualTo("DESSERTS");
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        // Then
        assertThat(Category.valueOf("SANDWICHES")).isEqualTo(Category.SANDWICHES);
        assertThat(Category.valueOf("SIDES")).isEqualTo(Category.SIDES);
        assertThat(Category.valueOf("DRINKS")).isEqualTo(Category.DRINKS);
        assertThat(Category.valueOf("DESSERTS")).isEqualTo(Category.DESSERTS);
    }
}

