package com.example.tokenvalidator.service;

import com.example.tokenvalidator.strategy.FactorialStrategy;
import com.example.tokenvalidator.strategy.IterativeFactorialStrategy;
import com.example.tokenvalidator.strategy.RecursiveFactorialStrategy;
import com.example.tokenvalidator.util.LogCalcUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * Service for mathematical calculations using strategy pattern.
 */
@Service
public class CalculationService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);
    
    private final FactorialStrategy iterativeStrategy;
    private final FactorialStrategy recursiveStrategy;
    
    @Value("${app.factorial.strategy:iterative}")
    private String strategyType;
    
    public CalculationService(IterativeFactorialStrategy iterativeStrategy, 
                            RecursiveFactorialStrategy recursiveStrategy) {
        this.iterativeStrategy = iterativeStrategy;
        this.recursiveStrategy = recursiveStrategy;
    }
    
    /**
     * Calculates factorial using the configured strategy.
     * 
     * @param number The number to calculate factorial for
     * @return The factorial result
     * @throws InterruptedException if the calculation is interrupted
     */
    public long factorial(int number) throws InterruptedException {
        logger.debug("Calculating factorial for number: {}", number);
        
        Map<String, FactorialStrategy> strategies = Map.of(
            "recursive", recursiveStrategy,
            "iterative", iterativeStrategy
        );
        FactorialStrategy strategy = strategies.get(strategyType);

        LogCalcUtil.logCalc(strategyType, number);
        
        return strategy.calculate(number);
    }
    
    /**
     * Gets the maximum safe input for factorial calculation.
     */
    public int getMaxSafeFactorialInput() {
        return 20;
    }
    
    /**
     * Checks if a number is valid for factorial calculation.
     */
    public boolean isValidFactorialInput(int number) {
        return number >= 0 && number <= 20;
    }

    /**
     * Gets the current strategy type being used.
     * @return The strategy type (iterative or recursive)
     */
    public String getStrategyType() {
        return strategyType;
    }
} 