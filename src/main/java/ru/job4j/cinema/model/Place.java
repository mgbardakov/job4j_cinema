package ru.job4j.cinema.model;

import java.util.Objects;

public class Place {
    private int id;
    private int row;
    private int number;
    private boolean available;
    private int viewerID;

    public Place(int id, int row, int number, boolean available, int viewerID) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.available = available;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
        Place place = (Place) o;
        return row == place.row &&
                number == place.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }
}
