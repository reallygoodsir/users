package org.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationLoader {
    private static Application APPLICATION;

    public static Application loadApplication() {
        if (APPLICATION == null) {
            Properties properties = new Properties();
            try (InputStream input = ApplicationLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new IOException("application.properties file not found in resources folder");
                }
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load application configuration", e);
            }

            APPLICATION = new Application(
                    properties.getProperty("user.service.url"),
                    properties.getProperty("user.service.authorization.token")
            );
        }
        return APPLICATION;
    }
}
