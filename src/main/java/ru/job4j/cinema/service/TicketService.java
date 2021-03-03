package ru.job4j.cinema.service;

public interface TicketService {
    /**
     * reserves place
     * @param data data needed for reserving
     * @return accepted / declined
     */
    boolean reservePlace(String data);

    /**
     * gets places in String format
     * @return places data
     */
    String getPLaces();
}
