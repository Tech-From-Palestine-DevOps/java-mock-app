package com.techfrompalestine.factorialcalculator.strategy;

/**
 * Strategy interface for factorial calculation algorithms.
 */
public interface FactorialStrategy {
    
    /**
     * Calculates the factorial of a given number.
     * 
     * @param number The number to calculate factorial for
     * @return The factorial result
     * @throws IllegalArgumentException if the input is invalid
     */
    long calculate(int number);
} 