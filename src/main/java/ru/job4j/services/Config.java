package ru.job4j.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class.getName());

    private final Properties config = new Properties();

    private Config() {
        try (InputStream in
                     = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            config.load(in);
        } catch (Exception e) {
            LOG.error("Couldn't read the config file", e);
        }
    }

    private static final class Lazy {
        private static final Config INST = new Config();
    }

    public static Config instOf() {
        return Config.Lazy.INST;
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }
}
