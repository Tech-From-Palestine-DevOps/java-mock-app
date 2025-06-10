package com.example.tokenvalidator.strategy;

import com.example.tokenvalidator.util.FactorialValidator;

import org.springframework.stereotype.Component;

/**
 * Recursive factorial calculation.
 */
@Component
public class RecursiveFactorialStrategy implements FactorialStrategy {
    
    @Override
    public long calculate(int number) throws InterruptedException {
        FactorialValidator.validate(number);
        
        if (number <= 1) {
            return 1;
        }
        
        return number * calculate(number - 1);
    }
} 