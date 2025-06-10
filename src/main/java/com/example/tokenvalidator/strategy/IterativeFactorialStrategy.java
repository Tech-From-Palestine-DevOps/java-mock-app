package com.example.tokenvalidator.strategy;

import com.example.tokenvalidator.util.FactorialValidator;
import org.springframework.stereotype.Component;

/**
 * Iterative factorial calculation using a simple loop.
 */
@Component
public class IterativeFactorialStrategy implements FactorialStrategy {
    
    @Override
    public long calculate(int number) throws InterruptedException {
        FactorialValidator.validate(number);
        
        if (number <= 1) {
            return 1;
        }
        
        long result = 1;
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        
        return result;
    }
} 