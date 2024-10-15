package com.marrok.inventaire_esm.model;

import java.util.Date;

public class BonSortie {
    private int id;
    private int idEmployeur;
    private int idService;
    private Date date; //
    private int idSortie;

    // Constructor
    public BonSortie(int id, int idEmployeur, int idService, Date date, int idSortie) {
        this.id = id;
        this.idEmployeur = idEmployeur;
        this.idService = idService;
        this.date = date;
        this.idSortie = idSortie;
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

    public int getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(int idSortie) {
        this.idSortie = idSortie;
    }

    @Override
    public String toString() {
        return "BonSortie{" +
                "id=" + id +
                ", idEmployeur=" + idEmployeur +
                ", idService=" + idService +
                ", date=" + date +
                ", idSortie=" + idSortie +
                '}';
    }
}
