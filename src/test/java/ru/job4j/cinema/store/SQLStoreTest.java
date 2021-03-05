package ru.job4j.cinema.store;

import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.SingleInstancePostgresRule;
import org.junit.*;
import ru.job4j.cinema.exceptions.ConstraintViolationException;
import ru.job4j.cinema.model.Order;
import ru.job4j.cinema.model.Viewer;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SQLStoreTest {

    private static String schema;
    private static Connection connection;
    private static Store store;

    @ClassRule
    public static SingleInstancePostgresRule pg = EmbeddedPostgresRules.singleInstance();

    @BeforeClass
    public static void doBefore() throws FileNotFoundException, SQLException {
        schema = new BufferedReader(new FileReader("db/schema.sql"))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        DataSource source = pg.getEmbeddedPostgres().getPostgresDatabase();
        connection = source.getConnection();
        store = new SQLStore(source);
    }

    @Before
    public void runSchema() throws SQLException {
        connection.prepareStatement(schema).execute();
    }

    @AfterClass
    public static void doAfter() throws SQLException {
        connection.close();
    }

    @Test
    public void whenCreateThenGetViewerByNameAndPhone() throws SQLException {
        var id = store.createViewer(new Viewer(0,"Paolo", "555"));
        var rsl = store.getViewerByNameAndPhone("Paolo", "555");
        var expected = new Viewer(id, "Paolo", "555");
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenCreateTwoOrdersThenGetThemAll() throws SQLException, ConstraintViolationException {
        var expected = List.of(
                new Order(1, 1, 1, 0),
                new Order(2, 1, 2,0));
        var viewerID = store.createViewer(new Viewer(0, "Bibson", "555"));
        store.createOrder(new Order(0, 1, 1, 1));
        store.createOrder(new Order(0, 1, 2, 1));
        var rsl = store.getAllOrders();
        assertThat(rsl, is(expected));
    }
}