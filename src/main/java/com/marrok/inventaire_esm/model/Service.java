package com.marrok.inventaire_esm.model;

public class Service {

    private int id;
    private String name;

    // Getters and Setters (omitted for brevity)

    public Service( String name) {

        this.name = name;
    }

    public Service(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Additional methods (optional)
    public String getServiceDetails() {
        return "ID: " + id + ", Name: " + name;
    }
}
