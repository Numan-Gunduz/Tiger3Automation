package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            String configFilePath = "src/test/resources/config.properties";
            FileInputStream fileInputStream = new FileInputStream(configFilePath);
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Config dosyası okunamadı!");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
