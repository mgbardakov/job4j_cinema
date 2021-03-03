package ru.job4j.cinema.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.job4j.cinema.model.Viewer;
import ru.job4j.cinema.store.PSQLStore;
import ru.job4j.cinema.store.Store;

public class JSONTicketService implements TicketService {

    private final Store store = PSQLStore.instOf();

    @Override
    public boolean reservePlace(String data) {
        var rsl = false;
        if (isAccountBalanceOK()) {
            JsonObject orderData = new Gson().fromJson(data, JsonObject.class);
            var viewerName = orderData.get("name").getAsString();
            var viewerPhone = orderData.get("phone").getAsString();
            var viewer = store.getViewerByNameAndPhone(viewerName, viewerPhone);
            var viewerID = viewer == null ? store.createViewer(
                    new Viewer(0, viewerName, viewerPhone)) : viewer.getId();
            var place = store.getPlaceByCoordinates(orderData.get("row").getAsInt(),
                    orderData.get("number").getAsInt());
            place.setAvailable(false);
            place.setViewerID(viewerID);
            store.updatePlace(place);
            rsl = true;
        }
        return rsl;
    }

    @Override
    public String getPLaces() {
        var places = store.getAllPlaces();
        return new Gson().toJson(places);
    }

    private boolean isAccountBalanceOK() {
        return true;
    }
}
