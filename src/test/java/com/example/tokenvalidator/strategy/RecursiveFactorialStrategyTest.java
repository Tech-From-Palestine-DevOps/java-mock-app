package com.example.tokenvalidator.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecursiveFactorialStrategy.
 */
class RecursiveFactorialStrategyTest {

    private RecursiveFactorialStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new RecursiveFactorialStrategy();
    }

    @Test
    void calculate_WithValidInputs_ReturnsCorrectResults() throws InterruptedException {
        // Test cases for key numbers
        assertEquals(1L, strategy.calculate(0));      // Base case
        assertEquals(1L, strategy.calculate(1));      // First number
        assertEquals(2L, strategy.calculate(2));      // Small number
        assertEquals(120L, strategy.calculate(5));    // Medium number
        assertEquals(2432902008176640000L, strategy.calculate(20)); // Max number
    }

    @Test
    void calculate_WithInvalidInput_ThrowsException() throws InterruptedException {
        // Test invalid input
        assertThrows(IllegalArgumentException.class, () -> strategy.calculate(21));
    }
} 