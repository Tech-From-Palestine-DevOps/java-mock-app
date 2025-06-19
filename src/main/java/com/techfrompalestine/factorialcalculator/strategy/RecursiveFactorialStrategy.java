package com.techfrompalestine.factorialcalculator.strategy;

import org.springframework.stereotype.Component;
import com.techfrompalestine.factorialcalculator.util.FactorialValidator;

/**
 * Recursive implementation of factorial calculation.
 */
@Component
public class RecursiveFactorialStrategy implements FactorialStrategy {
    
    @Override
    public long calculate(int number) {
        FactorialValidator.validateInput(number);
        
        if (number == 0 || number == 1) {
            return 1;
        }
        
        return number * calculate(number - 1);
    }
} 