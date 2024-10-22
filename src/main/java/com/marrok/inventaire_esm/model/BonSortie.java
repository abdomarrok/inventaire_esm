package com.marrok.inventaire_esm.model;

import java.util.Date;

public class BonSortie {
    private int id;
    private int idEmployeur;
    private int idService;
    private Date date; //


    // Constructor
    public BonSortie(int id, int idEmployeur, int idService, Date date) {
        this.id = id;
        this.idEmployeur = idEmployeur;
        this.idService = idService;
        this.date = date;
    }

    // Default constructor
    public BonSortie() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(int idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    @Override
    public String toString() {
        return "BonSortie{" +
                "id=" + id +
                ", idEmployeur=" + idEmployeur +
                ", idService=" + idService +
                ", date=" + date +
                '}';
    }
}
