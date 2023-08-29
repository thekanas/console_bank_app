package by.stolybko.database.util;

import lombok.SneakyThrows;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        try (InputStream inputStream = PropertiesManager.class.getClassLoader().getResourceAsStream("application.yml")) {
            PROPERTIES.load(inputStream);
        }
    }
}
