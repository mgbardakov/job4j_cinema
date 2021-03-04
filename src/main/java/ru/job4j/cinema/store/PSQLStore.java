package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class PSQLStore extends SQLStore {

    private static final BasicDataSource source = new BasicDataSource();
    static {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(PSQLStore.class.getClassLoader().getResourceAsStream("db.properties"))
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        source.setDriverClassName(cfg.getProperty("jdbc.driver"));
        source.setUrl(cfg.getProperty("jdbc.url"));
        source.setUsername(cfg.getProperty("jdbc.username"));
        source.setPassword(cfg.getProperty("jdbc.password"));
        source.setMinIdle(5);
        source.setMaxIdle(10);
        source.setMaxOpenPreparedStatements(100);
    }

    private PSQLStore() {
        super(source);
    }

    private static final class Lazy {
        private static final Store INST = new PSQLStore();
    }
    public static Store instOf() {
        return Lazy.INST;
    }
}
