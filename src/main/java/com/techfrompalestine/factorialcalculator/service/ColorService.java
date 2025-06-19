package com.techfrompalestine.factorialcalculator.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service responsible for generating consistent colors based on input strings.
 * Uses SHA-256 hashing to ensure the same input always produces the same color.
 * 
 * Uses Material Design colors for better visual appeal and consistency.
 */
@Service
public class ColorService {
    
    private static final Logger logger = LoggerFactory.getLogger(ColorService.class);
    
    // Color generation algorithm constants
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String FALLBACK_COLOR = "#2196F3";
    
    // Material Design colors - optimized for background use with white text
    private static final String[] MATERIAL_COLORS = {
        "#F44336", // Red
        "#E91E63", // Pink
        "#9C27B0", // Purple
        "#673AB7", // Deep Purple
        "#3F51B5", // Indigo
        "#2196F3", // Blue
        "#03A9F4", // Light Blue
        "#00BCD4", // Cyan
        "#009688", // Teal
        "#4CAF50", // Green
        "#8BC34A", // Light Green
        "#CDDC39", // Lime
        "#FFEB3B", // Yellow
        "#FFC107", // Amber
        "#FF9800", // Orange
        "#FF5722", // Deep Orange
        "#795548", // Brown
        "#9E9E9E", // Grey
        "#607D8B", // Blue Grey
        "#E91E63", // Pink (500)
        "#9C27B0", // Purple (500)
        "#673AB7", // Deep Purple (500)
        "#3F51B5", // Indigo (500)
        "#2196F3", // Blue (500)
        "#03A9F4", // Light Blue (500)
        "#00BCD4", // Cyan (500)
        "#009688", // Teal (500)
        "#4CAF50", // Green (500)
        "#8BC34A", // Light Green (500)
        "#CDDC39", // Lime (500)
        "#FF9800", // Orange (500)
        "#FF5722", // Deep Orange (500)
        "#795548", // Brown (500)
        "#607D8B", // Blue Grey (500)
        "#F44336", // Red (500)
        "#E91E63", // Pink (500)
        "#9C27B0", // Purple (500)
        "#673AB7", // Deep Purple (500)
        "#3F51B5", // Indigo (500)
        "#2196F3", // Blue (500)
        "#03A9F4", // Light Blue (500)
        "#00BCD4", // Cyan (500)
        "#009688", // Teal (500)
        "#4CAF50", // Green (500)
        "#8BC34A", // Light Green (500)
        "#CDDC39", // Lime (500)
        "#FF9800", // Orange (500)
        "#FF5722"  // Deep Orange (500)
    };
    
    /**
     * Generates a color for environment badges.
     * 
     * @param environment The environment name
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForEnv(String environment) {
        return generateColor(environment);
    }
    
    /**
     * Generates a color for author badges.
     * 
     * @param author The author name
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForAuthor(String author) {
        return generateColor(author);
    }
    
    /**
     * Generates a color for version badges.
     * 
     * @param version The version string
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForVersion(String version) {
        return generateColor(version);
    }
    
    /**
     * Generates a color based on input string.
     * 
     * @param input The input string to generate color from
     * @return A hex color string
     */
    private String generateColor(String input) {
        if (input == null || input.trim().isEmpty()) {
            logger.warn("Empty or null input provided for color generation, using fallback color");
            return FALLBACK_COLOR;
        }
        
        try {
            byte[] hash = generateHash(input);
            int colorIndex = Math.abs(hash[0]) % MATERIAL_COLORS.length;
            String selectedColor = MATERIAL_COLORS[colorIndex];
            
            logger.debug("Generated color {} for input '{}'", selectedColor, input);
            
            return selectedColor;
            
        } catch (Exception e) {
            logger.error("Failed to generate color for input '{}': {}", input, e.getMessage());
            return FALLBACK_COLOR;
        }
    }
    
    /**
     * Generates SHA-256 hash for the input string.
     */
    private byte[] generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return digest.digest(input.getBytes(StandardCharsets.UTF_8));
    }
} 