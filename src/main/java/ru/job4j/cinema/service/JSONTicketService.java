package ru.job4j.cinema.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.job4j.cinema.exceptions.ConstraintViolationException;
import ru.job4j.cinema.model.Order;
import ru.job4j.cinema.model.Viewer;
import ru.job4j.cinema.store.PSQLStore;
import ru.job4j.cinema.store.Store;

public class JSONTicketService implements TicketService {

    private static final String JSON_TEMPLATE = "{\"message\": \"%s\"}";

    private final Store store = PSQLStore.instOf();

    @Override
    public String reservePlace(String data) {
        var rsl = "";
        if (isAccountBalanceOK()) {
            try {
                JsonObject orderData = new Gson().fromJson(data, JsonObject.class);
                var viewerName = orderData.get("name").getAsString();
                var viewerPhone = orderData.get("phone").getAsString();
                var viewer = store.getViewerByNameAndPhone(viewerName, viewerPhone);
                var viewerID = viewer == null ? store.createViewer(
                        new Viewer(0, viewerName, viewerPhone)) : viewer.getId();
                store.createOrder(new Order(0, orderData.get("row").getAsInt(),
                        orderData.get("number").getAsInt(), viewerID)
                );
                rsl = String.format(JSON_TEMPLATE, "Место успешно забронировано");
            } catch (ConstraintViolationException e) {
                rsl = String.format(JSON_TEMPLATE, "К сожалению данное место уже забронировано");
            }
        } else {
            rsl = String.format(JSON_TEMPLATE,
                    "Бронирование не удалось проверьте состояние своего счёта");
        }
        return rsl;
    }

    @Override
    public String getOrders() {
        var orders = store.getAllOrders();
        return new Gson().toJson(orders);
    }

    private boolean isAccountBalanceOK() {
        return true;
    }
}
