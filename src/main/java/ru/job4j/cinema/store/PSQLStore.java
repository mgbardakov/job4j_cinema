package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.model.Viewer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PSQLStore implements Store {

    protected final BasicDataSource pool = new BasicDataSource();

    private static final Logger LOGGER = LogManager.getLogger();

    private PSQLStore() {
        init();
    }

    protected void init() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("db.properties"))
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
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PSQLStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM place")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    places.add(new Place(it.getInt("id"), it.getInt("row"),
                            it.getInt("number"), it.getBoolean("available"),
                            it.getInt("viewer_id")));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return places;
    }

    @Override
    public Place getPlaceByCoordinates(int row, int number) {
        Place rslPlace = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT * FROM place WHERE row = ? AND number = ?")
        ) {
            ps.setInt(1, row);
            ps.setInt(2, number);
            ps.execute();
            var rslSet = ps.getResultSet();
            if (rslSet.next()) {
                rslPlace = new Place(rslSet.getInt("id"), rslSet.getInt("row"),
                        rslSet.getInt("number"), rslSet.getBoolean("available"),
                        rslSet.getInt("viewer_id"));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rslPlace;
    }

    @Override
    public void updatePlace(Place place) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "UPDATE place SET row = ?," +
                             " number = ?," +
                             "available = ?," +
                             "viewer_id = ? WHERE id = ?")
        ) {
            ps.setInt(1, place.getRow());
            ps.setInt(2, place.getNumber());
            ps.setBoolean(3, place.isAvailable());
            ps.setInt(4, place.getViewerID());
            ps.setInt(5, place.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public int createViewer(Viewer viewer) {
        var rsl = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO viewer(name, phone_number) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, viewer.getName());
            ps.setString(2, viewer.getPhoneNumber());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    rsl = id.getInt(1);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Viewer getViewerByNameAndPhone(String name, String phone) {
        Viewer rslViewer = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT id, name, phone_number FROM viewer WHERE name = ? AND phone_number = ?",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.execute();
            try (ResultSet it = ps.getResultSet()) {
                if (it.next()) {
                    rslViewer = new Viewer(it.getInt("id"),
                            it.getString("name"),
                            it.getString("phone_number"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rslViewer;
    }
}
