package com.marrok.inventaire_esm.model;

public class Localisation {

    private int id;
    private String locName;
    private int floor;
    private int idService;


    // Constructors, getters, and setters

    public Localisation(int id, String locName, int floor) {
        this.id = id;
        this.locName = locName;
        this.floor = floor;

    }

    public Localisation(String locName, int floor,  int idService) {
        this.locName = locName;
        this.floor = floor;
        this.idService = idService;

    }

    public Localisation(int id, String locName, int floor,  int idService) {
        this.id = id;
        this.locName = locName;
        this.floor = floor;
        this.idService = idService;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }


    // Additional methods (optional)
    public String getFullLocation() {
        return locName + ", Floor " + floor ;
    }

    @Override
    public String toString() {
        return "Localisation{" +
                "id=" + id +
                ", locName='" + locName + '\'' +
                ", floor=" + floor +
                ", idService=" + idService +
                '}';
    }
}
