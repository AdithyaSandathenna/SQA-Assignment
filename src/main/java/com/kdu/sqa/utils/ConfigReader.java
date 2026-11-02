package com.kdu.sqa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration properties from config.properties.
 * Supports command-line property overriding (e.g., -Dbrowser=firefox).
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found in resources folder");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public static String getProperty(String key) {
        // System property overrides file property if set
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        String value = properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val != null) ? val : defaultValue;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        try {
            return val != null ? Integer.parseInt(val) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        return val != null ? Boolean.parseBoolean(val) : defaultValue;
    }
}
