package com.example.unit7_sqlite;

public class Subject {
    private final int id;
    private final String name;
    private final int color; // Store color as an int

    public Subject(int id, String name, int color) {
        this.id = id;
        this.name = name;
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
}
