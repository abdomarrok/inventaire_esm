package com.marrok.inventaire_esm.model;

public class Retour {
    private int id;
    private int idArticle;
    private int quantity;
    private int idBr; // id_bon_retour

    // Constructors
    public Retour() {
    }

    public Retour(int id, int idArticle, int quantity, int idBr) {
        this.id = id;
        this.idArticle = idArticle;
        this.quantity = quantity;
        this.idBr = idBr;
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

    public int getIdBr() {
        return idBr;
    }

    public void setIdBr(int idBr) {
        this.idBr = idBr;
    }
}
