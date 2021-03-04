package ru.job4j.cinema.store;

import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.SingleInstancePostgresRule;
import org.junit.*;
import ru.job4j.cinema.model.Place;
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
    public void whenGetPlaceByCoordinates() throws SQLException {
        var rsl = store.getPlaceByCoordinates(1, 1);
        var expected = new Place(1,1, 1, true, 0);
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenCreateThenGetViewerByNameAndPhone() throws SQLException {
        var id = store.createViewer(new Viewer(0,"Paolo", "555"));
        var rsl = store.getViewerByNameAndPhone("Paolo", "555");
        var expected = new Viewer(id, "Paolo", "555");
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenUpdatePLaceThenGetItByCoordinates() {
        var place = store.getPlaceByCoordinates(1, 1);
        var viewerID = store.createViewer(new Viewer(0, "Biba", "555"));
        place.setAvailable(false);
        place.setViewerID(viewerID);
        store.updatePlace(place);
        var rslPlace = store.getPlaceByCoordinates(1, 1);
        assertThat(rslPlace.isAvailable(), is(false));
        assertThat(rslPlace.getViewerID(), is(1));
    }

    @Test
    public void whenGetAllViewers() {
        var expected = List.of(
                new Place(1, 1, 1, false, 0),
                new Place(2, 1, 2, false, 0),
                new Place(3, 1, 3, false, 0),
                new Place(4, 2, 1, false, 0),
                new Place(5, 2, 2, false, 0),
                new Place(6, 2, 3, false, 0),
                new Place(7, 3, 1, false, 0),
                new Place(8, 3, 2, false, 0),
                new Place(9, 3, 3, false, 0));

        var rsl = store.getAllPlaces();
        assertThat(rsl, is(expected));
    }
}