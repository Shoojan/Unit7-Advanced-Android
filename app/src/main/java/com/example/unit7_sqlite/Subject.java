package com.example.unit7_sqlite;

public class Subject {
    private int id;
    private String name;
    private int color; // Store color as an int

    public Subject(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Subject(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
