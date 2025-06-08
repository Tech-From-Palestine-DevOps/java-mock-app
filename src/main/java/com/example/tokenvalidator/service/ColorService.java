package com.example.tokenvalidator.service;

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
 * Different color ranges are used for different purposes:
 * - Environment: More vibrant colors for better visibility
 * - Author: Softer colors for readability
 * - Version: Medium saturation for balance
 */
@Service
public class ColorService {
    
    private static final Logger logger = LoggerFactory.getLogger(ColorService.class);
    
    // Color generation algorithm constants
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String FALLBACK_COLOR = "#2196F3";
    
    // Color range configurations for different purposes
    private static final ColorRange ENVIRONMENT_RANGE = new ColorRange(0.6f, 0.8f);
    private static final ColorRange AUTHOR_RANGE = new ColorRange(0.4f, 0.6f);
    private static final ColorRange VERSION_RANGE = new ColorRange(0.5f, 0.7f);
    
    // Color space constants
    private static final int RGB_BYTES_COUNT = 3;
    private static final int RGB_MAX_VALUE = 255;
    private static final float HSL_HUE_DIVISOR = 6f;
    private static final float HSL_THIRD = 1f / 3f;
    private static final float HSL_HALF = 1f / 2f;
    private static final float HSL_TWO_THIRDS = 2f / 3f;
    private static final float HSL_SIXTH = 1f / 6f;
    
    /**
     * Generates a color for environment badges.
     * Uses more vibrant colors for better visibility and distinction.
     * 
     * @param environment The environment name
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForEnv(String environment) {
        return generateColor(environment, ENVIRONMENT_RANGE);
    }
    
    /**
     * Generates a color for author badges.
     * Uses softer colors for better readability.
     * 
     * @param author The author name
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForAuthor(String author) {
        return generateColor(author, AUTHOR_RANGE);
    }
    
    /**
     * Generates a color for version badges.
     * Uses medium saturation for balanced appearance.
     * 
     * @param version The version string
     * @return A hex color string (e.g., "#FF5733")
     */
    public String getColorForVersion(String version) {
        return generateColor(version, VERSION_RANGE);
    }
    
    /**
     * Generates a color based on input string and saturation range.
     * 
     * @param input The input string to generate color from
     * @param colorRange The saturation range to use
     * @return A hex color string
     */
    private String generateColor(String input, ColorRange colorRange) {
        if (input == null || input.trim().isEmpty()) {
            logger.warn("Empty or null input provided for color generation, using fallback color");
            return FALLBACK_COLOR;
        }
        
        try {
            byte[] hash = generateHash(input);
            RgbColor originalColor = extractRgbFromHash(hash);
            HslColor hslColor = originalColor.toHsl();
            
            // Adjust saturation to desired range
            HslColor adjustedColor = hslColor.withSaturationInRange(colorRange);
            RgbColor finalColor = adjustedColor.toRgb();
            
            String hexColor = finalColor.toHexString();
            
            logger.debug("Generated color {} for input '{}'", hexColor, input);
            
            return hexColor;
            
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
    
    /**
     * Extracts RGB values from hash bytes.
     */
    private RgbColor extractRgbFromHash(byte[] hash) {
        int red = hash[0] & 0xFF;
        int green = hash[1] & 0xFF;
        int blue = hash[2] & 0xFF;
        
        return new RgbColor(red, green, blue);
    }
    
    /**
     * Represents a color range for saturation values.
     */
    private static class ColorRange {
        private final float minSaturation;
        private final float maxSaturation;
        
        public ColorRange(float minSaturation, float maxSaturation) {
            this.minSaturation = Math.max(0f, Math.min(minSaturation, 1f));
            this.maxSaturation = Math.max(this.minSaturation, Math.min(maxSaturation, 1f));
        }
        
        public float getMinSaturation() {
            return minSaturation;
        }
        
        public float getMaxSaturation() {
            return maxSaturation;
        }
    }
    
    /**
     * Represents an RGB color.
     */
    private static class RgbColor {
        private final int red;
        private final int green;
        private final int blue;
        
        public RgbColor(int red, int green, int blue) {
            this.red = clampColorValue(red);
            this.green = clampColorValue(green);
            this.blue = clampColorValue(blue);
        }
        
        private int clampColorValue(int value) {
            return Math.max(0, Math.min(RGB_MAX_VALUE, value));
        }
        
        public HslColor toHsl() {
            float r = red / (float) RGB_MAX_VALUE;
            float g = green / (float) RGB_MAX_VALUE;
            float b = blue / (float) RGB_MAX_VALUE;
            
            float max = Math.max(Math.max(r, g), b);
            float min = Math.min(Math.min(r, g), b);
            float delta = max - min;
            
            float hue = calculateHue(r, g, b, max, delta);
            float lightness = (max + min) / 2f;
            float saturation = calculateSaturation(delta, lightness, max, min);
            
            return new HslColor(hue, saturation, lightness);
        }
        
        private float calculateHue(float r, float g, float b, float max, float delta) {
            if (delta == 0) return 0f;
            
            float hue;
            if (max == r) {
                hue = (g - b) / delta + (g < b ? 6 : 0);
            } else if (max == g) {
                hue = (b - r) / delta + 2;
            } else {
                hue = (r - g) / delta + 4;
            }
            
            return hue / HSL_HUE_DIVISOR;
        }
        
        private float calculateSaturation(float delta, float lightness, float max, float min) {
            if (delta == 0) return 0f;
            
            return lightness > 0.5f 
                ? delta / (2f - max - min) 
                : delta / (max + min);
        }
        
        public String toHexString() {
            return String.format("#%02x%02x%02x", red, green, blue);
        }
    }
    
    /**
     * Represents an HSL color.
     */
    private static class HslColor {
        private final float hue;
        private final float saturation;
        private final float lightness;
        
        public HslColor(float hue, float saturation, float lightness) {
            this.hue = hue;
            this.saturation = Math.max(0f, Math.min(1f, saturation));
            this.lightness = Math.max(0f, Math.min(1f, lightness));
        }
        
        public HslColor withSaturationInRange(ColorRange range) {
            float adjustedSaturation = range.getMinSaturation() + 
                (saturation * (range.getMaxSaturation() - range.getMinSaturation()));
            
            return new HslColor(hue, adjustedSaturation, lightness);
        }
        
        public RgbColor toRgb() {
            if (saturation == 0) {
                // Grayscale
                int grayValue = Math.round(lightness * RGB_MAX_VALUE);
                return new RgbColor(grayValue, grayValue, grayValue);
            }
            
            float q = lightness < 0.5f 
                ? lightness * (1 + saturation) 
                : lightness + saturation - lightness * saturation;
            float p = 2 * lightness - q;
            
            float r = hueToRgb(p, q, hue + HSL_THIRD);
            float g = hueToRgb(p, q, hue);
            float b = hueToRgb(p, q, hue - HSL_THIRD);
            
            return new RgbColor(
                Math.round(r * RGB_MAX_VALUE),
                Math.round(g * RGB_MAX_VALUE),
                Math.round(b * RGB_MAX_VALUE)
            );
        }
        
        private float hueToRgb(float p, float q, float t) {
            if (t < 0) t += 1;
            if (t > 1) t -= 1;
            if (t < HSL_SIXTH) return p + (q - p) * 6 * t;
            if (t < HSL_HALF) return q;
            if (t < HSL_TWO_THIRDS) return p + (q - p) * (HSL_TWO_THIRDS - t) * 6;
            return p;
        }
    }
} 