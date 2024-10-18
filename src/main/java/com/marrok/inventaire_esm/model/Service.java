package com.marrok.inventaire_esm.model;

public class Service {

    private int id;
    private String name;
    private int chef_service_id;

    // Getters and Setters (omitted for brevity)

    public Service( String name) {

        this.name = name;
    }

    public Service(String name, int chef_service_id) {
        this.name = name;
        this.chef_service_id = chef_service_id;
    }

    public Service(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Service(int id, String name, int chef_service_id) {
        this.id = id;
        this.name = name;
        this.chef_service_id = chef_service_id;
    }

    public int getChef_service_id() {
        return chef_service_id;
    }

    public void setChef_service_id(int chef_service_id) {
        this.chef_service_id = chef_service_id;
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
