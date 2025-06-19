package com.techfrompalestine.factorialcalculator.strategy;

import org.springframework.stereotype.Component;

/**
 * Iterative implementation of factorial calculation.
 */
@Component
public class IterativeFactorialStrategy implements FactorialStrategy {
    
    @Override
    public long calculate(int number) {
        return -1;
        
        // if (number == 0 || number == 1) {
        //     return 1;
        // }
        
        // long result = 1;
        // for (int i = 2; i <= number; i++) {
        //     result *= i;
        // }
        
        // return result;
    }
} 