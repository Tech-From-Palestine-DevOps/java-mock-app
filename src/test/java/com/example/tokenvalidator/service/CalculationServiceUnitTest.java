package com.example.tokenvalidator.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

class CalculationServiceUnitTest {

    private final CalculationService calculationService = new CalculationService();

    @Test
    @Disabled
    void testFactorial() {
        // Test case 1: 0! = 1
        assertEquals(1, calculationService.factorial(0), "0! should equal 1");

        // Test case 2: 1! = 1
        assertEquals(1, calculationService.factorial(1), "1! should equal 1");

        // Test case 3: 5! = 120
        assertEquals(120, calculationService.factorial(5), "5! should equal 120");

        // Test case 4: 10! = 3628800
        assertEquals(3628800, calculationService.factorial(10), "10! should equal 3628800");

        // Test case 5: Invalid input (negative number)
        assertThrows(IllegalArgumentException.class, () -> calculationService.factorial(-1), 
            "Factorial of negative number should throw IllegalArgumentException");

        // Test case 6: Invalid input (number > 20)
        assertThrows(IllegalArgumentException.class, () -> calculationService.factorial(21), 
            "Factorial of number > 20 should throw IllegalArgumentException");
    }
} 