package com.marrok.inventaire_esm.model;

public class Sortie {
    private int id;
    private int idArticle;
    private int quantity;
    private int idBs; // id_bon_sortie

    // Constructor
    public Sortie(int id, int idArticle, int quantity, int idBs) {
        this.id = id;
        this.idArticle = idArticle;
        this.quantity = quantity;
        this.idBs = idBs;
    }

    // Default constructor
    public Sortie() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIdBs() {
        return idBs;
    }

    public void setIdBs(int idBs) {
        this.idBs = idBs;
    }

    @Override
    public String toString() {
        return "Sortie{" +
                "id=" + id +
                ", idArticle=" + idArticle +
                ", quantity=" + quantity +
                ", idBs=" + idBs +
                '}';
    }
}
