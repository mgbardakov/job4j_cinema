package ru.job4j.cinema.service;

public interface TicketService {
    /**
     * reserves place
     * @param data data needed for reserving
     * @return message
     */
    String reservePlace(String data);

    /**
     * gets orders in String format
     * @return orders data
     */
    String getOrders();
}
