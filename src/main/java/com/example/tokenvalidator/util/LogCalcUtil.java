package com.example.tokenvalidator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for handling calculation logging and timing based on strategy and number.
 */
public class LogCalcUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LogCalcUtil.class);
    
    /**
     * Performs calculation logging and timing based on strategy name and number.
     * 
     * @param strategyName The name of the strategy (e.g., "recursive", "iterative")
     * @param number The input number for calculation
     * @throws InterruptedException if the calculation is interrupted
     */
    public static void logCalc(String strategyName, int number) throws InterruptedException {
        logger.info("Calculating factorial for number: {}", number);

        if (number < 0) {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info("Calculating factorial for number: {}", number);
            }
        }

    }
} 