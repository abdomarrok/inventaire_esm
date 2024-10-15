package com.marrok.inventaire_esm.model;

import java.util.Date;

public class BonEntree {
    private int id;
    private int idFournisseur; // Foreign key referencing fournisseur
    private Date date; // Use java.util.Date for datetime type
    private int tva; // Tax Value Added

    // Constructor
    public BonEntree(int id, int idFournisseur, Date date, int tva) {
        this.id = id;
        this.idFournisseur = idFournisseur;
        this.date = date;
        this.tva = tva;
    }

    // Default constructor
    public BonEntree() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTva() {
        return tva;
    }

    public void setTva(int tva) {
        this.tva = tva;
    }

    @Override
    public String toString() {
        return "BonEntree{" +
                "id=" + id +
                ", idFournisseur=" + idFournisseur +
                ", date=" + date +
                ", tva=" + tva +
                '}';
    }
}
