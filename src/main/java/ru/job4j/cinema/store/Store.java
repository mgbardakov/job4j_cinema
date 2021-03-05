package ru.job4j.cinema.store;

import ru.job4j.cinema.exceptions.ConstraintViolationException;
import ru.job4j.cinema.model.Order;
import ru.job4j.cinema.model.Viewer;

import java.util.Collection;

public interface Store {
    /**
     * creates new order in storage
     * @param order viewer
     * @return created order ID
     */
    int createOrder(Order order) throws ConstraintViolationException;
    /**
     * get all orders from the store
     * @return list of orders
     */
    Collection<Order> getAllOrders();

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
