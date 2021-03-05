package ru.job4j.cinema.store;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cinema.exceptions.ConstraintViolationException;
import ru.job4j.cinema.model.Order;
import ru.job4j.cinema.model.Viewer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLStore implements Store {

    protected DataSource pool;

    public SQLStore(DataSource pool) {
        this.pool = pool;
    }

    private static final Logger LOGGER = LogManager.getLogger();


    @Override
    public int createOrder(Order order) throws ConstraintViolationException {
        var rslID = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO orders(row, number, viewer_id) VALUES(?, ?, ? )",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, order.getRow());
            ps.setInt(2, order.getNumber());
            ps.setInt(3, order.getViewerID());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        rslID = id.getInt(1);
                    }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConstraintViolationException();
        }
        return rslID;
    }

    public Collection<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM orders")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    orders.add(new Order(it.getInt("id"), it.getInt("row"),
                            it.getInt("number"),
                            it.getInt("viewer_id")));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return orders;
    }

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
