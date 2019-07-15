package com.github.mixalturek.hackhathon.config;

import com.typesafe.config.Config;

import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Reusable helpers for Typesafe config.
 */
public class ConfigUtils {
    private ConfigUtils() {
        // No instance
    }

    public static Properties toProperties(Config config) {
        Properties result = new Properties();
        config.entrySet().forEach(e -> result.setProperty(e.getKey(), config.getString(e.getKey())));
        return result;
    }

    public static String formatKeyValues(Config config) {
        return config.entrySet().stream()
                .map(e -> {
                    String keyLow = e.getKey().toLowerCase();
                    if (keyLow.contains("passw") || keyLow.contains("secret") || keyLow.contains("salt")) {
                        return e.getKey() + " = ********";
                    } else {
                        return "\t" + e.getKey() + " = '" + config.getAnyRef(e.getKey()) + "'";
                    }
                })
                .sorted()
                .collect(Collectors.joining("\n"));
    }
}
