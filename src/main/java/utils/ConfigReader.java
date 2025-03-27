package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Config dosyası yüklenemedi.", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
    public static String get(String key, String defaultValue) {
        try {
            return properties.getProperty(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
