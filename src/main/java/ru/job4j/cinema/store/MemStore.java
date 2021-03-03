package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.model.Viewer;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final AtomicInteger PLACE_ID = new AtomicInteger(1);
    private static final AtomicInteger VIEWER_ID = new AtomicInteger(1);

    @Override
    public Collection<Place> getAllPlaces() {
        return null;
    }

    @Override
    public Place getPlaceByCoordinates(int row, int number) {
        return null;
    }

    @Override
    public void updatePlace(Place place) {

    }

    @Override
    public int createViewer(Viewer viewer) {
        return 0;
    }

    @Override
    public Viewer getViewerByNameAndPhone(String name, String phone) {
        return null;
    }
}
