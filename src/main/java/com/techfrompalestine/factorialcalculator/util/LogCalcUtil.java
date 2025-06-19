package com.techfrompalestine.factorialcalculator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging calculation operations.
 */
public class LogCalcUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LogCalcUtil.class);
    
    /**
     * Logs a calculation operation.
     * 
     * @param strategy The strategy used for calculation
     * @param number The number that was calculated
     */
    public static void logCalc(String strategy, int number) {
        logger.info("Calculation completed using {} strategy for number: {}", strategy, number);
    }
} 