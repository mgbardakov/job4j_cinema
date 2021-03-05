package ru.job4j.cinema.model;

import java.util.Objects;

public class Order {
    private int id;
    private int row;
    private int number;
    private int viewerID;

    public Order(int id, int row, int number, int viewerID) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.viewerID = viewerID;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getViewerID() {
        return viewerID;
    }

    public void setViewerID(int viewerID) {
        this.viewerID = viewerID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return row == order.row &&
                number == order.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }
}
