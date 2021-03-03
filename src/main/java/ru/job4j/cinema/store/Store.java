package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.model.Viewer;

import java.util.Collection;

public interface Store {
    /**
     * get all places from the store
     * @return list of places
     */
    Collection<Place> getAllPlaces();

    /**
     * gets place from storage with certain coordinates
     * @param row place row
     * @param number place number
     * @return Place object fro, storage
     */
    Place getPlaceByCoordinates(int row, int number);

    /**
     * updates place model in storage
     * @param place updating place
     */
    void updatePlace(Place place);

    /**
     * creates new viewer in storage
     * @param viewer viewer
     * @return created viewer ID
     */
    int createViewer(Viewer viewer);

    /**
     * gets viewer from storage
     * @param name viewer name
     * @param phone viewer phone number
     * @return viewer object / null if not found
     */
    Viewer getViewerByNameAndPhone(String name, String phone);

}
